package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.ProdutosAdapter;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class ActivityLogin extends AppCompatActivity {

    private static final String TAG = "ActivityLogin";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        final EditText edEmail = findViewById((R.id.edEmailLogin));
        final EditText edSenha = findViewById(R.id.edSenhaLogin);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edEmail.getText().toString(), edSenha.getText().toString());


            }
        });

        findViewById(R.id.btnCadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(edEmail.getText().toString(), edSenha.getText().toString());


            }
        });

    }

    private void signIn(String email, String senha) {

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(ActivityLogin.this, "Logado com Sucesso.",
                                    Toast.LENGTH_SHORT).show();

                            AppSetup.user = mAuth.getCurrentUser();
                            startActivity(new Intent(ActivityLogin.this, ProdutosActivity.class));
                            //updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signUp(String email, String senha) {

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(ActivityLogin.this, "Cadastro Realizado com Sucesso, Valide seu e-mail para logar !",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }

                });

    }
}