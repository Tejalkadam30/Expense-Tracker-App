package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    EditText etTitle, etAmount;
    Spinner spinnerCategory;
    Button btnAdd, btnDate;
    TextView tvTotal;
    ListView listView;

    ArrayList<String> expenseList;
    ArrayAdapter<String> adapter;

    double totalAmount = 0;
    String selectedDate = "";

    Map<String, Double> monthlyExpenses = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAdd = findViewById(R.id.btnAdd);
        btnDate = findViewById(R.id.btnDate);
        tvTotal = findViewById(R.id.tvTotal);
        listView = findViewById(R.id.listView);

        // Categories
        String[] categories = {"Food", "Travel", "Shopping"};
        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories);
        spinnerCategory.setAdapter(categoryAdapter);

        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                expenseList);

        listView.setAdapter(adapter);

        // Date Picker
        btnDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        selectedDate = d + "/" + (m + 1) + "/" + y;
                        btnDate.setText(selectedDate);
                    },
                    year, month, day);

            dialog.show();
        });

        // Add Expense
        btnAdd.setOnClickListener(v -> {

            String title = etTitle.getText().toString().trim();
            String amountText = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || amountText.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountText);
            totalAmount += amount;

            tvTotal.setText("Total: ₹" + totalAmount);

            String expenseEntry = title + " - ₹" + amount +
                    " (" + category + ") - " + selectedDate;

            expenseList.add(expenseEntry);
            adapter.notifyDataSetChanged();

            // Monthly Summary
            String[] dateParts = selectedDate.split("/");
            String monthKey = dateParts[1] + "/" + dateParts[2];

            monthlyExpenses.put(monthKey,
                    monthlyExpenses.getOrDefault(monthKey, 0.0) + amount);

            // Clear fields
            etTitle.setText("");
            etAmount.setText("");
        });
    }
}

