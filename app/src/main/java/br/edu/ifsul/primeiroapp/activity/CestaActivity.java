package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.CestaAdapter;
import br.edu.ifsul.primeiroapp.adapter.ClientesAdapter;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class CestaActivity extends AppCompatActivity {

    private ListView lvCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        lvCesta = findViewById(R.id.lvCesta);

    }

    @Override
    protected void onResume() {
        super.onResume();
        lvCesta.setAdapter(new CestaAdapter(CestaActivity.this, AppSetup.itens));

    }
}