package com.example.logonpf.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logonpf.notepad.api.NotaAPI;
import com.example.logonpf.notepad.model.Nota;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etTitulo;
    private EditText etTexto;

    private Nota nota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etTexto = (EditText) findViewById(R.id.etTexto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.salvar:
                salvar();
                return true;
            case R.id.pesquisar:
                pesquisar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://notepadsaas.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void salvar() {
        NotaAPI service = getRetrofit().create(NotaAPI.class);
        if(nota == null)
            nota = new Nota();

        nota.setTexto(etTexto.getText().toString());
        nota.setTitulo(etTitulo.getText().toString());

        service.salvar(nota).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(),
                        "Nota gravada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Deu ruim", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pesquisar() {
        NotaAPI service = getRetrofit().create(NotaAPI.class);
        service.buscarNota(etTitulo.getText().toString())
                .enqueue(new Callback<List<Nota>>() {
                    @Override
                    public void onResponse(Call<List<Nota>> call,
                                           Response<List<Nota>> response) {

                        if(response.body() != null) {
                            if(response.body().size() > 0) {
                                nota = response.body()
                                        .get(0);
                                etTexto.setText(nota.getTexto());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Nota>> call, Throwable t) {

                    }
                });

    }
}










