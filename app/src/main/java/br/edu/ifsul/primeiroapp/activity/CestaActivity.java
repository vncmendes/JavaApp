package br.edu.ifsul.primeiroapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.CestaAdapter;
import br.edu.ifsul.primeiroapp.adapter.ClientesAdapter;
import br.edu.ifsul.primeiroapp.model.Item;
import br.edu.ifsul.primeiroapp.model.Pedido;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class CestaActivity extends AppCompatActivity {


    private ListView lvCesta;
    private TextView tvNomeCliente;
    private TextView tvTotalItemCesta;
    private TextView vendedor;
    public int i;
    private Double valortotal = new Double(0);
    private List<Item> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        lvCesta = findViewById(R.id.lvCesta);

        tvNomeCliente = findViewById(R.id.tvNomeClienteCesta);
        tvTotalItemCesta = findViewById(R.id.tvTotalItemCesta);
        //vendedor = findViewById(R.id.tvVendedorCesta);

        tvNomeCliente.setText(AppSetup.cliente.getNome().toString());

        //trata o click longo no cartão da Listview
        lvCesta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogExcluirItem("Atenção", "Você realmente deseja excluir este item?", position);

                return true;
            }
        });

        //trata o click curto no cartão da Listview
        lvCesta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CestaActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("produto", AppSetup.itens.get(position).getProduto());
                startActivity(intent);
                AppSetup.itens.remove(position);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AppSetup.itens.isEmpty()){
            atualizarView();
        }
        //faz uma cópia do carrinho para usar na atualização do estoque
        itens = new ArrayList<>();
        itens.addAll(AppSetup.itens);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cesta, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuitem_salvar: {
                if (AppSetup.itens.isEmpty()) {
                    Toast.makeText(this, "O carrinho está vazio", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialogSalvarPedido("Quase lá", "\nTotal do Pedido = " + NumberFormat.getCurrencyInstance().format(valortotal) + " . Confirmar?");
                }

                break;
            }
            case R.id.menuitem_cancelar: {
                if (!AppSetup.itens.isEmpty()) {
                    alertDialogCancelarPedido("Ops! Vai cancelar?", "Tem certeza que quer cancelar este pedido?");
                } else {
                    Toast.makeText(this, "O carrinho está vazio.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

        return true;
    }



    private void alertDialogSalvarPedido(String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //add the title and text
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        //add the buttons
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //obtém a instância do database
                DatabaseReference myRef = AppSetup.getInstance();
                //prepara o pedido para salvá-lo
                Pedido pedido = new Pedido();
                pedido.setFormaDePagamento("dinheiro");
                pedido.setEstado("aberto");
                pedido.setDataCriacao(Calendar.getInstance().getTime());
                pedido.setDataModificacao(Calendar.getInstance().getTime());
                pedido.setTotalPedido(valortotal);
                pedido.setSituacao(true);
                pedido.setItens(AppSetup.itens);
                pedido.setCliente(AppSetup.cliente);
                //salva o pedido no database
                myRef.child("pedidos").push().setValue(pedido);
                //limpa o setup
                AppSetup.itens.clear();
                AppSetup.cliente = null;
                Toast.makeText(CestaActivity.this, "Ótimo! Vendido.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CestaActivity.this, "Ótimo.Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void atualizarView() {
        lvCesta.setAdapter(new CestaAdapter(CestaActivity.this, AppSetup.itens));
        //totaliza o pedido
        valortotal = new Double(0);
        for (Item itemPedido : AppSetup.itens) {
            valortotal += itemPedido.getTotalItem();
        }
        tvTotalItemCesta.setText(NumberFormat.getCurrencyInstance().format(valortotal));
    }

    private void alertDialogCancelarPedido(String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //add the title and text
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        //add the buttons
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j < itens.size(); j++) {
                    atualizaEstoque(i);
                }
                AppSetup.itens.clear();
                AppSetup.cliente = null;
                Toast.makeText(CestaActivity.this, "Que pena! Pedido cancelado.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CestaActivity.this, "Ótimo! Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void alertDialogExcluirItem(String titulo, String mensagem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //add the title and text
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        //add the buttons
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppSetup.itens.remove(position); //remove do carrinho
                Toast.makeText(CestaActivity.this, "Produto removido.", Toast.LENGTH_SHORT).show();
                atualizarView();
                atualizaEstoque(position);

            }

        });

        builder.show();
    }

    private void atualizaEstoque(final int position) {
        //atualiza estoque no Firebase (Essa atualização é temporária, ao efetivar o pedido isso deverá ser validado.)
        final DatabaseReference myRef = AppSetup.getInstance().child("produtos").child(itens.get(position).getProduto().getKey()).child("quantidade");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //busca a posição de estoque atual
                long quantidade = (long) dataSnapshot.getValue();
                //atualiza o estoque
                myRef.setValue(itens.get(position).getQuantidade() + quantidade);
                atualizarView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}