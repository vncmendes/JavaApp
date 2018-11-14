package br.edu.ifsul.primeiroapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.adapter.ClientesAdapter;
import br.edu.ifsul.primeiroapp.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.primeiroapp.model.Cliente;
import br.edu.ifsul.primeiroapp.model.Produto;
import br.edu.ifsul.primeiroapp.setup.AppSetup;

public class ClientesActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 1;
    private ListView lvclientes;
    private List<Cliente> clientes;
    public static final String TAG = "clientesActivity";
    private DatabaseReference myRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        lvclientes = findViewById(R.id.lvclientes);
        lvclientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chamaDetalheCliente(position);
            }
        });
        clientes = new ArrayList<>();

        myRef = AppSetup.getInstance().child("clientes");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                GenericTypeIndicator<List<Cliente>> type = new GenericTypeIndicator<List<Cliente>>() {};
                clientes = dataSnapshot.getValue(type);
                clientes.remove(null);
                Log.d(TAG, "Value is: " + clientes);
                atualizarView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }


        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_barcode: {

                Intent intent = new Intent (this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;
            }
        }

        return true;
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    //localiza o produto na lista (ou n?o)
                    boolean flag = true;
                    for (Cliente cliente : clientes){
                        if(String.valueOf(cliente.getCodigoDeBarras()).equals(barcode.displayValue)){
                            flag = false;
                            Intent intent = new Intent(ClientesActivity.this, ClienteDetalheActivity.class);
                            intent.putExtra("cliente", cliente);
                            startActivity(intent);
                            break;
                        }
                    }
                    if(flag){
                        Toast.makeText(this, "Cliente não cadastrado.", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(this, R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            }

            else {
                Toast.makeText(this, String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void chamaDetalheCliente(int position) {
        Intent intent = new Intent( ClientesActivity.this, ClienteDetalheActivity.class);
        intent.putExtra("cliente", clientes.get(position));
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_clientes, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_pesquisar).getActionView();
        searchView.setQueryHint("Digite o nome do cliente");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //uma lista para nova camada de modelo da RecyclerView
                List<Cliente> clientesFilter = new ArrayList<>();
                //um for-each na camada de modelo atual
                for (Cliente cliente : clientes) {
                    //se o nome do produto comeca com o nome digitado
                    if (cliente.getNome().contains(newText)) {
                        clientesFilter.add(cliente);
                    }
                }

                //coloca a nova lista como fonte de dados do novo adapter de RecyclerView
                //(Context, fonte de dados)
                lvclientes.setAdapter(new ClientesAdapter(ClientesActivity.this, clientesFilter));

                return true;
            }
        });

        return true;
    }

    private void atualizarView() {
        lvclientes.setAdapter(new ClientesAdapter(ClientesActivity.this, clientes));
    }
}
