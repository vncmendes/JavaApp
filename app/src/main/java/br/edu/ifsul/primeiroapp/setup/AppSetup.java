package br.edu.ifsul.primeiroapp.setup;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.primeiroapp.model.Cliente;
import br.edu.ifsul.primeiroapp.model.Item;
import br.edu.ifsul.primeiroapp.model.Produto;

public class AppSetup {

    private static final String TAG = "appSetup";
    public static FirebaseUser user = null;
    private static DatabaseReference myRef = null;
    public static Cliente cliente = null;
    public static List<Item> itens = new ArrayList<>();
    public static List<Produto> produtos = new ArrayList<>();
    private static FirebaseAuth mAuth = null;


    public static DatabaseReference getInstance() {

            if (myRef == null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                myRef = database.getReference("vendas");
                return myRef;
            }
        return myRef;
    }
    public static FirebaseAuth getAuthInstace(){
        //se ainda não tem a referência para o autenticador
        if(mAuth == null) {
            // obtém o objeto de referência para a coleção de objetos
            mAuth = mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, "Obteve o objeto de acesso ao serviço Auth. " + mAuth);
        }
        //se já tem a referência, só a retorna
        return mAuth;
    }
}
