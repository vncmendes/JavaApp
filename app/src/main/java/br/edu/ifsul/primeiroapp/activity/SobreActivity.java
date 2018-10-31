package br.edu.ifsul.primeiroapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.edu.ifsul.primeiroapp.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        //mapeia e escreve no componente
        //findViewById(R.id.tvSobre);
    }
}
