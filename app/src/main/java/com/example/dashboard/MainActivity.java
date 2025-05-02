package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int TASK_REQUEST_CODE = 100;
    private ImageButton viewAllButton;
    private ArrayList<String> allTasks;
    private ListView taskPreviewList;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        taskPreviewList = findViewById(R.id.taskPreviewList);
        viewAllButton = findViewById(R.id.btnViewAllTasks);

        // Retrieve saved tasks from ItemStorage
        allTasks = ItemStorage.getItemList(this);
        if (allTasks == null) {
            allTasks = new ArrayList<>();
        }

        // Use CustomAdapter instead of ArrayAdapter
        adapter = new CustomAdapter(this, allTasks);
        taskPreviewList.setAdapter(adapter);

        // View all tasks button opens AddItemActivity
        viewAllButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            intent.putStringArrayListExtra("itemList", allTasks);
            startActivityForResult(intent, TASK_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> updated = data.getStringArrayListExtra("itemList");
            if (updated != null) {
                allTasks.clear();
                allTasks.addAll(updated);
                ItemStorage.saveItemList(this, allTasks);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
