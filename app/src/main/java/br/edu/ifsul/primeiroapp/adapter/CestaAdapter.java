package br.edu.ifsul.primeiroapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Item;

public class CestaAdapter extends ArrayAdapter<Item> {

    private static final String TAG = "cestaAdapter";
    private final Context context;
    private final List<Item> itens;

    public CestaAdapter(@NonNull Context context, List<Item> itens) {
        super(context, 0, itens);
        this.itens = itens;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Item item = itens.get(position);
        Log.d(TAG, "" + itens);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cesta_adapter, parent, false);
        }

        TextView tvCestaNomeItem = convertView.findViewById(R.id.tvCestaNomeItem);
        tvCestaNomeItem.setText(item.getProduto().getNome());
        TextView tvCestaPrecoItem = convertView.findViewById(R.id.tvCestaPrecoItem);
        tvCestaPrecoItem.setText(item.getTotalItem().toString());

        return convertView;
    }
}
