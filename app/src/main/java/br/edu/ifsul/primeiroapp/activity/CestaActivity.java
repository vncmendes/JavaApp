package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.CestaAdapter;
import br.edu.ifsul.primeiroapp.adapter.ClientesAdapter;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class CestaActivity extends AppCompatActivity {

    private ListView lvCesta;
    private TextView tvNomeCliente;
    private TextView total;
    private TextView vendedor;
    public int i;
    private Double valortotal = new Double(0);

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
}