package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Item;
import br.edu.ifsul.primeiroapp.setup.AppSetup;


public class ProdutoDetalheActivity extends AppCompatActivity {

    private static final String TAG = "produtoDetalheActivity";
    private Integer position = -1;
    private TextView tvNome, tvDescricao, tvValor, tvEstoque;
    private EditText etQuantidade;
    private Button btnVender;
    private long quantidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.edu.ifsul.primeiroapp.R.layout.activity_detalheproduto);

        //1 mapeia os componentes da View
        tvNome = findViewById(R.id.tvNomeProduto);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvValor = findViewById(R.id.tvValorProduto);
        tvEstoque = findViewById(R.id.tvQtdEstoque);
        etQuantidade = findViewById(R.id.etQuantidade);
        btnVender = findViewById(R.id.BtnVenderProduto);


        //obtém o objeto produto anexado a intent
        position = getIntent().getExtras().getInt("position");
        Log.d(TAG, "Positon = " + position);
        Log.d(TAG, "Objeto selecionado = " + AppSetup.produtos.get(position));

        //3 atualizar view
//        atualizarView();




        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppSetup.cliente == null){
                    Toast.makeText(ProdutoDetalheActivity.this, "Selecione um cliente para venda.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProdutoDetalheActivity.this, ClientesActivity.class));
                }else{
                    if(!etQuantidade.getText().toString().isEmpty()){
                        if(Integer.parseInt(etQuantidade.getText().toString()) <= AppSetup.produtos.get(position).getQuantidade().intValue()){
                            //cria o item vendido e o adicona no carrinho
                            Item itemPedido = new Item();
                            itemPedido.setQuantidade(Integer.valueOf(etQuantidade.getText().toString()));
                            itemPedido.setProduto(AppSetup.produtos.get(position));
                            itemPedido.setTotalItem(AppSetup.produtos.get(position).getValor()*itemPedido.getQuantidade());
                            AppSetup.itens.add(itemPedido);
                            // atualiza estoque no Firebase
                            DatabaseReference myRef = AppSetup.getInstance().child("produtos").child(AppSetup.produtos.get(position).getKey()).child("quantidade");
                            myRef.setValue(AppSetup.produtos.get(position).getQuantidade() - itemPedido.getQuantidade());
                            Toast.makeText(ProdutoDetalheActivity.this,"Item adicionado ao carrinho.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProdutoDetalheActivity.this, CestaActivity.class));
                            finish();
                        }else{
                            Toast.makeText(ProdutoDetalheActivity.this, "Ultrapassa a quantidade em estoque.", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(ProdutoDetalheActivity.this, "Digite a quantidade.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //atualiza estoque pelo Firebase
        final DatabaseReference myRef = AppSetup.getInstance().child("produtos").child(AppSetup.produtos.get(position).getKey()).child("quantidade");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //busca a posição de estoque atual
                quantidade = (long) dataSnapshot.getValue();
                tvEstoque.setText(String.format("%s %s", getString(R.string.text_estoque), quantidade));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarView();
    }

    private void atualizarView() {
        tvNome.setText(AppSetup.produtos.get(position).getNome());
        if (!(AppSetup.produtos.get(position).isSituacao()) || !(AppSetup.produtos.get(position).getQuantidade().intValue() > 0)) {
            etQuantidade.setEnabled(false);
            btnVender.setEnabled(false);
            Toast.makeText(this, "Faltou no estoque", Toast.LENGTH_SHORT).show();
        }
        tvDescricao.setText(AppSetup.produtos.get(position).getDescricao());
        tvValor.setText(NumberFormat.getCurrencyInstance().format(AppSetup.produtos.get(position).getValor()));
        tvEstoque.setText(String.format("%s%s", "Estoque:", String.valueOf(AppSetup.produtos.get(position).getQuantidade())));
    }
}
