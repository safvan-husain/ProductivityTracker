package com.example.productivitytracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity  extends AppCompatActivity {

    DataBaseHelper dbHelper;
    private ListView listView;
    private CustomAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DataBaseHelper.getInstance(this);
        setContentView(R.layout.history_screen);

        listView = findViewById(R.id.listView);

        List<DataModel> history = dbHelper.getHistory();
        adapter = new CustomAdapter(this, history);
        // Set the adapter to the ListView
        listView.setAdapter(adapter);
    }


    private void loadHistory() {

    }
}
