package itsmy.com.br.bingo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String ESTADO_LISTA_BOLAS_SORTEADAS = "ESTADO_LISTA_BOLAS_SORTEADAS";
    private final String ESTADO_LISTA_BOLAS_DISPONIVEIS = "ESTADO_LISTA_BOLAS_DISPONIVEIS";

    ArrayList<Integer> listaBolasSorteadas;
    ArrayList<Integer> listaBolasDisponiveis;

    private Integer maximoBolasBingo = 75;

    private ArrayList<Integer> bolasSorteadas;
    private ArrayList<Integer> bolasDisponiveis;
    private ArrayAdapter<Integer> adapterBolasSorteadas;
    private ArrayAdapter<Integer> adapterBolasDisponiveis;

    private TextView textViewBolaSorteada;
    private ListView listViewBolasSorteadas;
    private ListView listViewBolasDisponiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listaBolasSorteadas = new ArrayList<>();
        this.listaBolasDisponiveis = new ArrayList<>();
        this.textViewBolaSorteada = findViewById(R.id.bolaSorteadaTextView);
        this.listViewBolasSorteadas = findViewById(R.id.listaBolasSorteadasListView);
        this.listViewBolasDisponiveis = findViewById(R.id.listaBolasDisponiveisListView);
        for(int x = 0; x < this.maximoBolasBingo; x ++) this.listaBolasDisponiveis.add(x+1);
        reseteBolasSorteadas();
        preencherListaBolasDisponiveis();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList(this.ESTADO_LISTA_BOLAS_DISPONIVEIS, this.listaBolasDisponiveis);
        outState.putIntegerArrayList(this.ESTADO_LISTA_BOLAS_SORTEADAS, this.listaBolasSorteadas);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState  != null) {

            this.listaBolasSorteadas = savedInstanceState.getIntegerArrayList(this.ESTADO_LISTA_BOLAS_SORTEADAS);
            this.listaBolasDisponiveis = savedInstanceState.getIntegerArrayList(this.ESTADO_LISTA_BOLAS_DISPONIVEIS);

            for(int i = 0; i < listaBolasSorteadas.size(); i++){
                ArrayAdapter<Integer> adapterAuxBolasSorteadas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.listaBolasSorteadas);
                this.listViewBolasSorteadas.setAdapter(adapterAuxBolasSorteadas);
                adapterAuxBolasSorteadas.notifyDataSetChanged();
            }

            for(int i = 0; i < listaBolasDisponiveis.size(); i++){
                ArrayAdapter<Integer> adapterAuxBolasDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.listaBolasDisponiveis);
                this.listViewBolasDisponiveis.setAdapter(adapterAuxBolasDisponiveis);
                adapterAuxBolasDisponiveis.notifyDataSetChanged();
            }

        }

    }

    private void reseteBolasSorteadas() {
        this.bolasSorteadas = new ArrayList<>();
        this.adapterBolasSorteadas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.bolasSorteadas);
        this.listViewBolasSorteadas.setAdapter(this.adapterBolasSorteadas);
    }

    private void preencherListaBolasSorteadas() {
        this.adapterBolasSorteadas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.bolasSorteadas);
        this.listViewBolasSorteadas.setAdapter(this.adapterBolasSorteadas);
        this.adapterBolasSorteadas.notifyDataSetChanged();
    }

    private void preencherListaBolasDisponiveis() {
        this.bolasDisponiveis = new ArrayList<>();
        for (int x = 0; x < maximoBolasBingo; x++) {
            this.bolasDisponiveis.add(x + 1);
        }
        this.adapterBolasDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.bolasDisponiveis);
        this.listViewBolasDisponiveis.setAdapter(this.adapterBolasDisponiveis);
    }

    private void retirarBolaListaBolasDisponiveis(Integer bolaRetirada) {
        for (int x = 0; x < this.bolasDisponiveis.size(); x++) {
            if (this.bolasDisponiveis.get(x) == bolaRetirada) {
                this.bolasDisponiveis.remove(bolaRetirada);
            }
        }
        this.adapterBolasDisponiveis = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.bolasDisponiveis);
        this.listViewBolasDisponiveis.setAdapter(this.adapterBolasDisponiveis);
    }

    public void sortearBola(View botao) {
        Integer valorBola = sortear();
        if (valorBola != null) this.textViewBolaSorteada.setText(valorBola.toString());
        else this.textViewBolaSorteada.setText(null);
    }

    private Integer sortear() {
        Integer valorBolaSorteada = null;
        try {
            if (this.bolasDisponiveis.size() <= 0) {
                Toast.makeText(this, "Todos os números já foram sorteados.", Toast.LENGTH_SHORT).show();
            } else {
                boolean validacao;
                Random ran = new Random(System.currentTimeMillis());
                do {
                    valorBolaSorteada = ran.nextInt(this.maximoBolasBingo) + 1;
                    validacao = validacaoBolaSorteada(valorBolaSorteada);

                    if (validacao) {

                        // saveEstado AUX
                        this.listaBolasSorteadas.add(valorBolaSorteada);
                        // saveEstado AUX
                        this.listaBolasDisponiveis.remove(valorBolaSorteada);

                        this.bolasSorteadas.add(valorBolaSorteada);
                        preencherListaBolasSorteadas();
                        retirarBolaListaBolasDisponiveis(valorBolaSorteada);
                    }

                } while (!validacao);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            return valorBolaSorteada;
        }
    }

    private boolean validacaoBolaSorteada(Integer valorBolaSorteada) {
        boolean validacao = true;
        try {
            for (int x = 0; x < this.bolasSorteadas.size(); x++) {
                if (this.bolasSorteadas.get(x) == valorBolaSorteada) validacao = false;
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            return validacao;
        }
    }

    public void limpar(View botao) {
        Toast.makeText(this, "Reiniciando as listas.", Toast.LENGTH_SHORT).show();
        this.textViewBolaSorteada.setText(null);
        reseteBolasSorteadas();
        preencherListaBolasDisponiveis();
        this.listaBolasSorteadas = new ArrayList<>();
        this.listaBolasDisponiveis = new ArrayList<>();
        for(int x = 0; x < this.maximoBolasBingo; x ++) this.listaBolasDisponiveis.add(x+1);
    }

}
