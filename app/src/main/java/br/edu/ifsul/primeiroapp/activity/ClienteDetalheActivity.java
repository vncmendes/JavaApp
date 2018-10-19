package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Cliente;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class ClienteDetalheActivity extends AppCompatActivity {

    private static final String TAG = "clienteDetalheActivity";
    private Cliente cliente;
    private TextView tvNome;
    private TextView tvSobrenome;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhecliente);


        tvNome = findViewById(R.id.tvNomeCliente);
        tvSobrenome = findViewById(R.id.tvSobrenomeCliente);

        imageView = findViewById(R.id.imgFotoCliente);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        atualizarView();

        Button btnSelect = findViewById(R.id.btnSelectCliente);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSetup.cliente = cliente;
                finish();
            }
        });

    }

    private void atualizarView() {

        tvNome.setText(cliente.getNome());
        tvSobrenome.setText(cliente.getSobrenome());

    }
}