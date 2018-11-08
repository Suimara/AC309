package br.com.sabedoriajedi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Jedi> jedi;
    private ArrayAdapter<Jedi> jediAdapter;
    private ListView listView;
    private Context context;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerAudiencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        jedi = new ArrayList<>();

        listView = findViewById(R.id.lvJediPrincipal);
        jediAdapter = new JediAdapter(context, jedi);
        listView.setAdapter(jediAdapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("Jedi");

        valueEventListenerAudiencia = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jedi.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Jedi AudienciaNova = dados.getValue(Jedi.class);

                    jedi.add(AudienciaNova);
                }
                jediAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
            }
        };

        registerForContextMenu(listView);

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Abrir tela para adicionar Jedi
                Intent adicionarJedi = new Intent(MainActivity.this, AdicionarJedi.class);
                startActivity(adicionarJedi);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_jedi, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.menu_editar:
                //Pega o objeto selecionado na lista
                Jedi jediEditar = (Jedi) listView.getItemAtPosition(info.position);

                //Serializable do Jedi
                Bundle bundle = new Bundle();
                bundle.putSerializable("editaJedi", jediEditar);

                //abrir a tela de adicionar e inserir as informações já lá
                Intent abrirCadastroEditar = new Intent(MainActivity.this, AdicionarJedi.class);

                //Anexa o serializable no intent para levar para a classe de CadastroEditarActivity
                abrirCadastroEditar.putExtras(bundle);

                //Inicia a classe e envia o objeto para ela
                startActivity(abrirCadastroEditar);
                return true;

            case R.id.menu_excluir: //menu para excluir o Jedi
                excluirJedi(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    private void excluirJedi(int position) {
        final Jedi jediExcluir = jediAdapter.getItem(position);

        //ALERT DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //Titulo do Alert Dialog
        builder.setTitle("Excluir");

        //Mensagem do Alert Dialog
        if (jediExcluir != null) {
            builder.setMessage("Quer mesmo excluir?\n\n" + "Nome: " + jediExcluir.getNome());
        }

        //Botão SIM do Alert Dialog
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebase = ConfiguracaoFirebase.getFirebase().child("Jedi");
                if (jediExcluir != null) {
                    firebase.child(jediExcluir.getUid()).removeValue();
                }

                Toast.makeText(context, "Item excluído", Toast.LENGTH_SHORT).show();
            }
        });

        //Botão NAO do Alert Dialog
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Item mantido", Toast.LENGTH_SHORT).show();
            }
        });

        //Criar Alert Dialog
        AlertDialog alerta = builder.create();

        //Exibir Alert Dialog
        alerta.show();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerAudiencia);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerAudiencia);
    }
}
