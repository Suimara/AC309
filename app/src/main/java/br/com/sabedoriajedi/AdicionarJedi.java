package br.com.sabedoriajedi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class AdicionarJedi extends Activity {

    private EditText edtNomeJedi;
    private EditText edtLado;
    private EditText edtMestre;
    private RadioButton rbSimLutouContraVader;
    private RadioButton rbNaoLutouContraVader;
    private Button btnSalvar;

    private Jedi jediEditar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_jedi);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setText("Salvar");
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSalvar.getText().equals("Salvar")) {
                    salvar(); //Método para salvar o Jedi
                    finish(); //Fechar a tela atual de cadastro e abrir a tela principal
                }else if (btnSalvar.getText().equals("Editar")){
                    editar();
                    finish(); //Fechar a tela atual de cadastro e abrir a tela principal
                }
            }
        });

        reconhecerComponentes();

        Bundle args = getIntent().getExtras();

        if (args != null) {
            jediEditar = (Jedi) args.getSerializable("editaJedi");
            assert jediEditar != null;
            atualizarJedi(jediEditar);
        }
        assert jediEditar != null;


    }

    private void editar() {
        reconhecerComponentes();

        Jedi jedi = new Jedi();

        jedi.setUid(jediEditar.getUid());

        jedi.setUid(jediEditar.getUid()); //ID para o Jedi
        jedi.setNome(edtNomeJedi.getText().toString()); //Inserindo o nome do Jedi
        jedi.setLado(edtLado.getText().toString()); //Inserindo o lado do Jedi
        jedi.setMestre(edtMestre.getText().toString()); //Inserindo o mestre do Jedi
        if (rbSimLutouContraVader.isChecked()) { //Inserindo se lutou contra Darth Vader
            jedi.setLutou(rbSimLutouContraVader.getText().toString());
        } else {
            jedi.setLutou(rbNaoLutouContraVader.getText().toString());
        }

        //ATUALIZA INFORMAÇÕES NO FIREBASE
        try {
            ConfiguracaoFirebase.getFirebase().child("Jedi").child(jedi.getUid()).setValue(jedi);
            Toast.makeText(AdicionarJedi.this, "Jedi alterado com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AdicionarJedi.this, "Falha ao editar!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void atualizarJedi(Jedi editaJedi){
        reconhecerComponentes();

        edtNomeJedi.setText(editaJedi.getNome());// Nome do Jedi
        edtLado.setText(editaJedi.getLado());//Lado do Jedi
        edtMestre.setText(editaJedi.getMestre());//Mestre do Jedi
        if (editaJedi.getLutou().equals("Sim")) {
            rbSimLutouContraVader.setChecked(true);//Lutou contra Darth Vader
        } else {
            rbNaoLutouContraVader.setChecked(true);//Não lutou contra Darth Vader
        }
        btnSalvar.setText("Editar");
    }

    private void reconhecerComponentes() { //Reconhecendo os componentes da tela para usá-los
        edtNomeJedi = findViewById(R.id.edtNomeJedi);
        edtLado = findViewById(R.id.edtLado);
        edtMestre = findViewById(R.id.edtMestre);
        rbSimLutouContraVader = findViewById(R.id.rbSimLutouContraVader);
        rbNaoLutouContraVader = findViewById(R.id.rbNaoLutouContraVader);
    }

    private void salvar() {
        reconhecerComponentes(); //Instanciar os componentes na tela e pegar seus valores
        Jedi jedi = new Jedi(); //Novo Jedi

        jedi.setUid(UUID.randomUUID().toString()); //Inserindo um ID aleatório para o Jedi
        jedi.setNome(edtNomeJedi.getText().toString()); //Inserindo o nome do Jedi
        jedi.setLado(edtLado.getText().toString()); //Inserindo o lado do Jedi
        jedi.setMestre(edtMestre.getText().toString()); //Inserindo o mestre do Jedi
        if (rbSimLutouContraVader.isChecked()) { //Inserindo se lutou contra Darth Vader
            jedi.setLutou(rbSimLutouContraVader.getText().toString());
        } else {
            jedi.setLutou(rbNaoLutouContraVader.getText().toString());
        }

        try { //Verificar sem derrubar o sistema
            ConfiguracaoFirebase.getFirebase().child("Jedi").child(jedi.getUid()).setValue(jedi); //Criando o filho Jedi e atribuindo seus objetos

            Toast.makeText(AdicionarJedi.this, "Jedi adicionado com sucesso!", Toast.LENGTH_SHORT).show(); //Mensagem de sucesso

        } catch (Exception e) { //Caso dê errado gravar no banco de dados
            e.printStackTrace(); //Mostra o erro
            Toast.makeText(AdicionarJedi.this, "Falha!", Toast.LENGTH_SHORT).show();
        }
    }
}
