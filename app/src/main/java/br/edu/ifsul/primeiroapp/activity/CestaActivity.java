package br.edu.ifsul.primeiroapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.NumberFormat;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.CestaAdapter;
import br.edu.ifsul.primeiroapp.adapter.ClientesAdapter;
import br.edu.ifsul.primeiroapp.model.Item;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class CestaActivity extends AppCompatActivity {



    private ListView lvCesta;
    private TextView tvNomeCliente;
    private TextView total;
    private TextView vendedor;
    public int i;
    private Double valortotal = new Double(0);
    private TextView tvTotalItemCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        lvCesta = findViewById(R.id.lvCesta);

        tvNomeCliente = findViewById(R.id.tvNomeClienteCesta);
        total = findViewById(R.id.tvTotalItemCesta);
        //vendedor = findViewById(R.id.tvVendedorCesta);

        tvNomeCliente.setText(AppSetup.cliente.getNome().toString());
        //total.setText(AppSetup.itens.get(0).getTotalItem().toString());

        for (i=0;i<AppSetup.itens.size() ;i++){
            valortotal += AppSetup.itens.get(i).getTotalItem();
        }

        total.setText(valortotal.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        lvCesta.setAdapter(new CestaAdapter(CestaActivity.this, AppSetup.itens));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cesta, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuitem_salvar:{
                alertDialogSalvarPedido("Quase lá", "\nTotal do Pedido = " + NumberFormat.getCurrencyInstance().format(valortotal) + " . Confirmar?");
                break;
            }
            case R.id.menuitem_cancelar:{
                alertDialogCancelarPedido("Ops! Vai cancelar?", "Tem certeza que quer cancelar este pedido?");
                break;
            }
        }

        return true;
    }

    private void alertDialogSalvarPedido(String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //add the title and text
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        //add the buttons
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CestaActivity.this, "Ótimo! Vendido.", Toast.LENGTH_SHORT).show();
                /*
                 *  persistir a o pedido no Firebase aqui!!!!!!!!!!!! Lembrar de atualizar o estoque
                 *  e controlar as exceções.
                 */
                AppSetup.itens.clear();
                AppSetup.cliente = null;
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
        for(Item itemPedido : AppSetup.itens){
            valortotal += itemPedido.getTotalItem();
        }
        tvTotalItemCesta.setText(NumberFormat.getCurrencyInstance().format(valortotal));
    }

    private void alertDialogCancelarPedido(String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //add the title and text
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        //add the buttons
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CestaActivity.this, "Que pena! Pedido cancelado.", Toast.LENGTH_SHORT).show();
                AppSetup.itens.clear();
                AppSetup.cliente = null;
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
}