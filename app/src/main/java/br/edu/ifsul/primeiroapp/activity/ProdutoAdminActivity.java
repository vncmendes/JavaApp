package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.Barcode;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.primeiroapp.model.Produto;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class ProdutoAdminActivity extends AppCompatActivity {

    private static final String TAG = "produtoAdminActivity";
    private static final int RC_BARCODE_CAPTURE = 9001;
    private EditText etCodigoDeBarras, etNome, etDescricao, etValor, etQuantidade;
    private Button btInserir;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_admin);

        //mapeia os componentes da UI
        etCodigoDeBarras = findViewById(R.id.etCodigoProduto);
        etNome = findViewById(R.id.etNomeProdutoAdmin);
        etDescricao = findViewById(R.id.etDescricaoProdutoAdmin);
        etValor = findViewById(R.id.etValorProdutoAdmin);
        etQuantidade = findViewById(R.id.etQuantidadeProdutoAdmin);
        btInserir = findViewById(R.id.btInserirProdutoAdmin);

        //inicializa o objeto de modelo
        produto = new Produto();

        btInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCodigoDeBarras.getText().toString().isEmpty() &&
                        !etNome.getText().toString().isEmpty() &&
                        !etDescricao.getText().toString().isEmpty() &&
                        !etValor.getText().toString().isEmpty() &&
                        !etQuantidade.getText().toString().isEmpty()){
                    produto.setCodigoDeBarras(Long.valueOf(etCodigoDeBarras.getText().toString()));
                    produto.setNome(etNome.getText().toString());
                    produto.setDescricao(etDescricao.getText().toString());
                    produto.setValor(Double.valueOf(etValor.getText().toString()));
                    produto.setQuantidade(Integer.valueOf(etQuantidade.getText().toString()));
                    produto.setSituacao(true); //rever isso ... colocar a imagem como situação
                    Log.d(TAG, "Produto a ser cadastrado: " + produto);
                    //salva no Firebase
                    AppSetup.getInstance().child("produtos").push().setValue(produto)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProdutoAdminActivity.this, R.string.toast_otimo_produto_cadastrado, Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProdutoAdminActivity.this, R.string.toast_produto_nao_cadastrado, Toast.LENGTH_SHORT).show();
                                }
                            });

                    limparForm();
                }else{
                    Toast.makeText(ProdutoAdminActivity.this, R.string.toast_todos_campos_devem_preenchidos, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void limparForm() {
        produto = new Produto();
        etCodigoDeBarras.setText(null);
        etNome.setText(null);
        etDescricao.setText(null);
        etValor.setText(null);
        etQuantidade.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_produto_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_barcode_admin:{
                // launch barcode activity.
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //true liga a funcionalidade autofoco
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false); //true liga a lanterna (fash)
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;
            }

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    etCodigoDeBarras.setText(barcode.displayValue);
                }
            } else {
                Toast.makeText(this, String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
