package com.example.excessbaggagecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // declare variables
    Toolbar mainToolbar;
    TextView txtBaggageWeight, txtBaggageCharge, txtAmountPayable;
    EditText etBaggageWeight, etBaggageRate;
    Spinner spTicketClass;
    Button btnCalculate, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // setup toolbar
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setSubtitle("Travel Fee Estimator");
        }

        // setup spinner
        spTicketClass = findViewById(R.id.spTicketClass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ticket_class, R.layout.spinner_item_main);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTicketClass.setAdapter(adapter);

        // initialize views
        etBaggageWeight = findViewById(R.id.etBaggageWeight);
        etBaggageRate = findViewById(R.id.etBaggageRate);
        txtBaggageWeight = findViewById(R.id.txtBaggageWeight);
        txtBaggageCharge = findViewById(R.id.txtBaggageCharge);
        txtAmountPayable = findViewById(R.id.txtAmountPayable);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);

        // button click listener
        btnCalculate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    // create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // handle menu item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // menu share button
        if (item.getItemId() == R.id.action_share) {

            // share app link
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/humyraa103/excessbaggagecalculator");
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Check out this Excess Baggage Calculator App!");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;

            // menu about page
        } else if (item.getItemId() == R.id.action_about) {

            // open about page
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }
        return false;
    }

    // handle button click
    @Override
    public void onClick(View view) {
        // calculate button
        if (view == btnCalculate) {

            // declare variables
            String ticketClass;
            double x, weight, rate, excessWeight, charge, payableAmount;

            // get selected type
            ticketClass = spTicketClass.getSelectedItem().toString().trim();

            // check empty input
            if (etBaggageWeight.getText().toString().isEmpty() || etBaggageRate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // check spinner selection
            if (spTicketClass.getSelectedItem().toString().equals("Select Class")) {
                Toast.makeText(this, "Please select class", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // convert input to number
                weight = Double.parseDouble(etBaggageWeight.getText().toString());
                rate = Double.parseDouble(etBaggageRate.getText().toString());

                // set max baggage weight
                if (ticketClass.equalsIgnoreCase("economy")) {
                    x = 20;
                } else {
                    x = 40;
                }

                // calculate excess baggage weight
                excessWeight = weight - x;

                // ensure extra baggage weight is not negative
                if (excessWeight <= 0) {
                    excessWeight = 0;
                }

                // calculate excess baggage charge
                charge = excessWeight * rate;

                // calculate total amount payable (8%)
                payableAmount = charge + (charge * 0.08);

                // show result
                txtBaggageWeight.setText(String.format(Locale.US, "%.2f kg", excessWeight));
                txtBaggageCharge.setText(String.format(Locale.US, "RM %.2f", charge));
                txtAmountPayable.setText(String.format(Locale.US, "RM %.2f", payableAmount));
            }
            catch (NumberFormatException e) {
                // error input
                Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();
            }

            // clear button
        } else if (view == btnClear) {
            etBaggageWeight.setText("");
            etBaggageRate.setText("");
            spTicketClass.setSelection(0);
            txtBaggageWeight.setText("0.00 kg");
            txtBaggageCharge.setText("RM 0.00");
            txtAmountPayable.setText("RM 0.00");
        }
    }
}