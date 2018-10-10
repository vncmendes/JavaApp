package br.edu.ifsul.primeiroapp.setup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.primeiroapp.model.Cliente;
import br.edu.ifsul.primeiroapp.model.Item;

public class AppSetup {

    public static FirebaseUser user = null;
    private static DatabaseReference myRef = null;
    public static Cliente cliente = null;
    public static List<Item> itens = new ArrayList<>();



    public static DatabaseReference getInstance() {

            if (myRef == null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                myRef = database.getReference("vendas");
                return myRef;
            }
        return myRef;
    }
}
