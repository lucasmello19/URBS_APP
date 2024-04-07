package com.example.urbs.ui.Line;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.urbs.R;
import com.example.urbs.data.model.LineResponse;
import com.example.urbs.data.model.ShapeResponse;
import com.example.urbs.service.ApiManager;
import com.example.urbs.ui.map.MapsActivity;
import com.example.urbs.utils.AccessTokenManager;

import java.util.ArrayList;
import java.util.List;

public class LinesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<LineResponse> lineList;
    private List<ShapeResponse> shapeList;

    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);

        apiManager = new ApiManager(LinesActivity.this);
        getLines();
    }

    public void getLines() {
        apiManager.getLine(new ApiManager.ApiCallback<ArrayList<LineResponse>>() {
            @Override
            public void onSuccess(ArrayList<LineResponse> result) {
                // Tratar sucesso da chamada
                lineList = result;
                // Configurar o RecyclerView
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(LinesActivity.this));

                // Criar e configurar o adapter
                LineAdapter adapter = new LineAdapter(lineList);
                recyclerView.setAdapter(adapter);

                // Definir o ouvinte de clique no adapter
                adapter.setOnItemClickListener(new LineAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(LineResponse line) {
                        // Tratar clique no item da lista
                        Log.d("LinesActivity", "Item selecionado: " + line.getNOME());
                        getShape(line.getCOD());
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Tratar falha na chamada
                Log.e("LinesActivity", "Falha ao obter linhas: " + throwable.getMessage());
            }
        });
    }

    public void getShape(String cod) {
        apiManager.getShape(cod, new ApiManager.ApiCallback<ArrayList<ShapeResponse>>() {
            @Override
            public void onSuccess(ArrayList<ShapeResponse> result) {
                // Tratar sucesso da chamada
                shapeList = result;
                // Configurar o RecyclerView
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Tratar falha na chamada
                Log.e("LinesActivity", "Falha ao obter linhas: " + throwable.getMessage());
            }
        });
    }

}
