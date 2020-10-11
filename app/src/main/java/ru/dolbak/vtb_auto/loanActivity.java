package ru.dolbak.vtb_auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.dolbak.vtb_auto.calculator.APIConnector;
import ru.dolbak.vtb_auto.calculator.CalculatedLoan;
import ru.dolbak.vtb_auto.calculator.CalculatorSettings;
import ru.dolbak.vtb_auto.calculator.LoanSettings;

import java.util.ArrayList;

public class loanActivity extends AppCompatActivity {
    private int price;
    private String manufacturer = "";
    private String model = "";
    private int kasko;
    private Context ctx;
    private double interest;
    TextView term;
    EditText loan;
    int loanNum, pr=1;
    EditText initialPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: разобрать intent на price, manufacturer, model
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        ctx = this;
        price = getIntent().getIntExtra("price", 0);
        kasko = (int) (price * 0.05);
        manufacturer = getIntent().getStringExtra("brand");
        model = getIntent().getStringExtra("model");
        loan  = findViewById(R.id.editLoanAmount);
        loan.setText(String.valueOf(price));
        final EditText kaskoBox = findViewById(R.id.editKasko);
        kaskoBox.setText(String.valueOf(kasko));
        APIConnector.SetAPIKey("0439608400f9f2356484a27a123cd381");
        final SeekBar seekBar = findViewById(R.id.seekBar);
        term = findViewById(R.id.textView5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                final Button confirmButton = findViewById(R.id.confirmLoan);
                confirmButton.setEnabled(false);
                term.setText(String.valueOf(progress+1 + " лет"));
                pr = progress+1;

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        initialPayment = findViewById(R.id.editInitialPayment);
        initialPayment.setText(String.valueOf((int) (price * 0.2)));
        initialPayment.setBackgroundResource(R.drawable.editok);
        final TextView labelPayment = findViewById(R.id.labelPayment);
        labelPayment.setText("Первоначальный платёж (от " + (int) (price * 0.2) + " до " + price + "):");
        final Button calculate = findViewById(R.id.buttonCalculate);
        initialPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int value;
                final Button confirmButton = findViewById(R.id.confirmLoan);
                confirmButton.setEnabled(false);
                try {
                    value = Integer.parseInt(initialPayment.getText().toString());
                } catch(Exception e) {
                    initialPayment.setBackgroundResource(R.drawable.editerr);
                    calculate.setEnabled(false);
                    return;
                }

                if(value < (int) (price * 0.2) || value > price) {
                    initialPayment.setBackgroundResource(R.drawable.editerr);
                    calculate.setEnabled(false);
                } else {
                    initialPayment.setBackgroundResource(R.drawable.editok);
                    calculate.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        final CheckBox kaskoCheck = findViewById(R.id.checkKasko);
        kaskoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Button confirmButton = findViewById(R.id.confirmLoan);
                confirmButton.setEnabled(false);
            }
        });
        final CheckBox insuranceCheck = findViewById(R.id.checkInsurance);
        insuranceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Button confirmButton = findViewById(R.id.confirmLoan);
                confirmButton.setEnabled(false);
            }
        });
    }

    public void onCalculateClick(final View v) {
        final Button b = (Button)v;
        final CheckBox kaskoBox = findViewById(R.id.checkKasko);
        final CheckBox insuranceBox = findViewById(R.id.checkInsurance);
        final EditText initialPayment = findViewById(R.id.editInitialPayment);
        final SeekBar termSeekBar = findViewById(R.id.seekBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CalculatorSettings calculatorSettings;
                try {
                    calculatorSettings = APIConnector.GetSettings();
                } catch (Exception e) {
                    Log.println(Log.ERROR, "Test", e.getMessage());
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Ошибка получения данных", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

                final LoanSettings settings = new LoanSettings();
                settings.clientTypes = new String[]{"ac43d7e4-cd8c-4f6f-b18a-5ccbc1356f75"};
                settings.cost = price;
                settings.initialFee = Integer.parseInt(initialPayment.getText().toString());
                settings.settingsName = "Haval";
                settings.kaskoValue = 0;
                final ArrayList<String> conditions = new ArrayList<String>();
                if(kaskoBox.isChecked()) {
                    settings.kaskoValue = kasko;
                    conditions.add("b907b476-5a26-4b25-b9c0-8091e9d5c65f");
                }
                conditions.add("cbfc4ef3-af70-4182-8cf6-e73f361d1e68");
                if(insuranceBox.isChecked()) {
                    conditions.add("57ba0183-5988-4137-86a6-3d30a4ed8dc9");
                }
                settings.specialConditions = conditions.toArray(new String[0]);
                settings.term = termSeekBar.getProgress()+1;
                settings.language = "ru-RU";
                final CalculatedLoan loan;
                try {
                    loan = APIConnector.CalculateLoan(settings);
                } catch (Exception e) {
                    Log.println(Log.ERROR, "Test", e.getMessage());
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Ошибка получения данных", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                interest = loan.result.contractRate;

                v.post(new Runnable() {
                    @Override
                    public void run() {
                        final TextView monthyPayment = findViewById(R.id.monthyPayment);
                        final Button confirmButton = findViewById(R.id.confirmLoan);
                        monthyPayment.setText(loan.result.payment + " руб/мес");
                        monthyPayment.setVisibility(View.VISIBLE);
                        confirmButton.setVisibility(View.VISIBLE);
                        confirmButton.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    public void onClickFill(View v) {
        int termInt = pr;
        int vehCost = price;
        int loanAmm = vehCost -  Integer.parseInt(initialPayment.getText().toString());
        Intent intent = new Intent(loanActivity.this, Main2Activity.class);
        intent.putExtra("term", termInt * 12);
        intent.putExtra("cost", vehCost);
        intent.putExtra("loan", loanAmm);
        intent.putExtra("interest", interest);
        intent.putExtra("brand", getIntent().getStringExtra("brand"));
        startActivity(intent);
    }
}