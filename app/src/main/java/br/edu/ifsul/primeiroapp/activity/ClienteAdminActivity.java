package br.edu.ifsul.primeiroapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Cliente;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class ClienteAdminActivity extends AppCompatActivity {

    private TextView tvCodigoDeBarras;
    private ImageView imgFoto;
    private EditText etNome, etSobrenome, etCPF;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_admin);

        //mapeia os componentes da UI
//        tvCodigoDeBarras = findViewById(R.id.tvCodigoDeBarrasClienteTelaAdmin);
        imgFoto = findViewById(R.id.imgClienteAdm);
        etNome = findViewById(R.id.etNomeClienteAdm);
        etSobrenome = findViewById(R.id.etSobrenomeClienteAdm);
        etCPF = findViewById(R.id.etCpfClienteAdm);
        btnCadastrar = findViewById(R.id.btnCadastrarClienteAdm);

        //trata evento onClick do botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNome.getText().toString().isEmpty()
                        || etSobrenome.getText().toString().isEmpty()
                        || etCPF.getText().toString().isEmpty()){
                    Toast.makeText(ClienteAdminActivity.this, R.string.toast_todos_campos_devem_preenchidos ,Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference myRef = AppSetup.getInstance().child("clientes");
                    Cliente cliente = new Cliente();
                    //pega os dados da tela e os coloca no objeto de modelo
                    cliente.setNome(etNome.getText().toString());
                    cliente.setSobrenome(etSobrenome.getText().toString());
                    cliente.setCpf(etCPF.getText().toString());
                    cliente.setSituacao(true);
                    cliente.setCodigoDeBarras(0L);
                    myRef.push().setValue(cliente)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ClienteAdminActivity.this, "Cadastrou, vlw éusguri ", Toast.LENGTH_SHORT).show();
                                    limparTela();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ClienteAdminActivity.this, "Não bombo !", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void limparTela() {
        etNome.setText(null);
        etSobrenome.setText(null);
        etCPF.setText(null);
    }
}
