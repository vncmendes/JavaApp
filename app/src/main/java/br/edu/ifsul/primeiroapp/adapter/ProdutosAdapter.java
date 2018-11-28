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

import java.util.List;
import br.edu.ifsul.primeiroapp.R;
import br.edu.ifsul.primeiroapp.model.Produto;



public class ProdutosAdapter extends ArrayAdapter<Produto> {

    private static final String TAG = "produtosAdapter";
    private Context context;
    private List<Produto> produtos;

    public ProdutosAdapter(@NonNull Context context, @NonNull List<Produto> produtos) {
        super(context, 0, produtos);
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Produto produto = produtos.get(position);
        Log.d(TAG, "" + produtos);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_produtos_adapter, parent, false);
        }


        TextView tvNomeProduto = convertView.findViewById(R.id.tvNomeProdutoAdapter);
        tvNomeProduto.setText(produto.getNome());
        TextView tvEstoque = convertView.findViewById(R.id.tvEstoqueAdapter);
        tvEstoque.setText(produto.getQuantidade().toString());
        ImageView fotoProduto = convertView.findViewById(R.id.imgFotoProdutoAdapter);
        if(produto.getUrl_foto() != null) {
            fotoProduto.setImageURI(Uri.parse(produto.getUrl_foto()));
        }
        else {
            fotoProduto.setImageResource(R.mipmap.ic_launcher_round);
        }

        return convertView;
    }


}
