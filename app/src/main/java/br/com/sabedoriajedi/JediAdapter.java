package br.com.sabedoriajedi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class JediAdapter extends ArrayAdapter<Jedi> {

    private ArrayList<Jedi> jedi; //Lista de Jedi
    private Context context; //Verificar o contexto de tela (Contexto da tela principal)

    public JediAdapter(Context context, ArrayList<Jedi> objetoJedi) {
        super(context, 0,objetoJedi);
        this.context = context;
        this.jedi = objetoJedi;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View viewResumida = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Inflar o layout de adapter
        assert inflater != null;
        viewResumida = inflater.inflate(R.layout.adapter_layout, parent, false);

        ImageView ivListaResumidaJedi = viewResumida.findViewById(R.id.ivListaResumidaJedi); //Imagem de canto para simbolizar um Jedi
        TextView txtViewNomeJedi = viewResumida.findViewById(R.id.txtViewNomeJedi); //Nome do Jedi
        TextView txtViewLadodaForca = viewResumida.findViewById(R.id.txtViewLadodaForca); //Lado da força em que está
        TextView txtViewMestre = viewResumida.findViewById(R.id.txtViewMestre); //Mestre do Jedi
        TextView txtViewLutou = viewResumida.findViewById(R.id.txtViewLutou); //Lutou contra Darth Vader
        Jedi jedi2 = jedi.get(position);

        //Atribuindo agora os valores para cada Jedi
        ivListaResumidaJedi.setImageResource(R.drawable.starwars); //Imagem do lado esquerdo
        txtViewNomeJedi.setText(jedi2.getNome()); //Nome do jedi
        txtViewLadodaForca.setText(jedi2.getLado()); //Lado da força
        txtViewMestre.setText(jedi2.getMestre()); //Mestre do Jedi
        txtViewLutou.setText("Duelou contra Darth Vader: "+jedi2.getLutou());
        return viewResumida;
    }
}
