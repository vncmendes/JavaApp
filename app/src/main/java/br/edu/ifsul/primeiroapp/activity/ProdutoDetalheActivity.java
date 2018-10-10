package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Item;
import br.edu.ifsul.primeiroapp.model.Produto;
import br.edu.ifsul.primeiroapp.setup.AppSetup;


public class ProdutoDetalheActivity extends AppCompatActivity {

    private static final String TAG = "produtoDetalheActivity";
    private Produto produto;
    private TextView tvNome;
    private TextView tvDescricao;
    private TextView tvValor;
    private TextView tvQuantidade;
    private EditText etQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.edu.ifsul.primeiroapp.R.layout.activity_detalheproduto);


        //1 mapeia os componentes da View
        tvNome = findViewById(R.id.tvNomeProduto);
        tvDescricao = findViewById(R.id.tvQtdEstoque);
        tvValor = findViewById(R.id.tvValorProduto);
        tvQuantidade = findViewById(R.id.tvQuantProduto);
        etQuantidade = findViewById(R.id.etQuantidade);

        //2 obtem o objeto anexado na intent
        produto = (Produto) getIntent().getSerializableExtra("produto");

        //3 atualizar view
        //atualizarView();

        Button btnComprar = findViewById(R.id.tvBtnProduto);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setProduto(produto);
                item.setQuantidade(Integer.valueOf(etQuantidade.getText().toString()));
                item.setSituacao(true);
                item.setTotalItem(Integer.valueOf(etQuantidade.getText().toString()) * produto.getValor());
                AppSetup.itens.add(item);
                startActivity(new Intent(ProdutoDetalheActivity.this, CestaActivity.class));

            }
        });


    }

    private void atualizarView() {
        tvNome.setText(produto.getNome());
        tvDescricao.setText(produto.getDescricao());
        tvValor.setText(produto.getValor().toString());
        tvQuantidade.setText(produto.getQuantidade().toString());
    }

}
