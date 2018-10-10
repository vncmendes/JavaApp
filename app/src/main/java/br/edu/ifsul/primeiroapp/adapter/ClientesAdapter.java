package br.edu.ifsul.primeiroapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Cliente;

public class ClientesAdapter extends ArrayAdapter<Cliente> {

    private static final String TAG = "clientesAdapter";
    private final Context context;
    private final List<Cliente> clientes;

    public ClientesAdapter(@NonNull Context context, List<Cliente> clientes) {
        super(context, 0, clientes);
        this.clientes = clientes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Cliente cliente = clientes.get(position);
        Log.d(TAG, "" + clientes);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clientes_adapter, parent, false);
        }

        TextView tvNomeItem = convertView.findViewById(R.id.tvNomeItemAdapter);
        tvNomeItem.setText(cliente.getNome());
        TextView tvCpfItem = convertView.findViewById(R.id.tvCpfItemAdapter);
        tvCpfItem.setText(cliente.getCpf());
        TextView tvCodigo = convertView.findViewById(R.id.tvCodigoItemAdapter);
        tvCodigo.setText(cliente.getCodigo().toString());
        ImageView fotoCliente = convertView.findViewById(R.id.imgItemAdapter);
        if(cliente.getUrl_foto() != null) {
            fotoCliente.setImageURI(Uri.parse(cliente.getUrl_foto()));
        }
        else {
            fotoCliente.setImageResource(R.mipmap.ic_launcher_round);
        }

        return convertView;
    }
}
