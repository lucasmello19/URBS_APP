package com.example.urbs.ui.Line;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.urbs.R;
import com.example.urbs.data.model.LineResponse;
import com.example.urbs.data.model.ShapeResponse;
import com.example.urbs.data.model.StopResponse;
import com.example.urbs.service.ApiManager;
import com.example.urbs.ui.login.LoginActivity;
import com.example.urbs.ui.map.MapsActivity;
import com.example.urbs.utils.AccessTokenManager;

import java.util.ArrayList;
import java.util.List;

public class LinesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private LineAdapter adapter;
    private List<LineResponse> lineList;
    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);
        setTitle("URBS");
        apiManager = new ApiManager(LinesActivity.this);
        searchEditText = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLines();
        setupSearchListener();
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterLines(s.toString());
            }
        });
    }

    private void getLines() {
        apiManager.getLine(new ApiManager.ApiCallback<ArrayList<LineResponse>>() {
            @Override
            public void onSuccess(ArrayList<LineResponse> result) {
                lineList = result;
                adapter = new LineAdapter(LinesActivity.this, lineList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Tratar falha na chamada
                throwable.printStackTrace();
            }
        });
    }

    private void filterLines(String query) {
        if (adapter != null) {
            adapter.filter(query);
        }
    }
}
