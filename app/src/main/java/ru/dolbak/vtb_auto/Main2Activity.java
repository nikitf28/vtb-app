package ru.dolbak.vtb_auto;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    TextView secondName, firstName, middleName, bDate, city, email, income, phone;
    RadioGroup gender;
    Double interest;
    String brand, term, cost, loan, secondNameStr, firstNameStr, middleNameStr, bDateStr, cityStr, emailStr, incomeStr, phoneStr, genderStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        secondName = findViewById(R.id.editSecondName);
        firstName = findViewById(R.id.editFirstName);
        middleName = findViewById(R.id.editMiddleName);
        bDate = findViewById(R.id.editBDate);
        city = findViewById(R.id.editCity);
        email = findViewById(R.id.editEmail);
        income = findViewById(R.id.editIncome);
        phone = findViewById(R.id.editPhoneNumber);
        gender = findViewById(R.id.gender);
        Intent intent = getIntent();
        brand = intent.getStringExtra("brand");
        term = intent.getStringExtra("term");
        cost = intent.getStringExtra("cost");
        loan = intent.getStringExtra("loan");
        interest = intent.getDoubleExtra("interest", 0.0);
        Log.d("bla", brand + " " + term + " " + cost + " " + loan + " " + interest);

    }

    public void onClickSend(View view){
        secondNameStr = (String) secondName.getText().toString();
        firstNameStr = (String) firstName.getText().toString();
        middleNameStr = (String) middleName.getText().toString();
        bDateStr = (String) bDate.getText().toString();
        cityStr = (String) city.getText().toString();
        emailStr = (String) email.getText().toString();
        incomeStr = (String) income.getText().toString();
        phoneStr = (String) phone.getText().toString();
        int button = gender.getCheckedRadioButtonId();
        RadioButton myRadioButton = (RadioButton) findViewById(button);
        genderStr = (String) myRadioButton.getText().toString();
        if (genderStr.equals("Мужской")){
            genderStr = "male";
        }
        else{
            genderStr = "female";
        }
        Network network = new Network();
        network.execute();

    }

    class Network extends AsyncTask<Void, Void, String> {
        String postHttpResponse() {
            OkHttpClient client = new OkHttpClient();
            String url = "https://gw.hackathon.vtb.ru/vtb/hackathon/carloan";
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{" +
                    "  \"comment\": \"Комментарий\"," +
                    "  \"customer_party\": {" +
                    "    \"email\": \"" + emailStr + "\"," +
                    "    \"income_amount\":\"" + incomeStr + "\"," +
                    "    \"person\": {" +
                    "      \"birth_date_time\": \"1985-02-20\"," +
                    "      \"birth_place\": \"" + cityStr + "\"," +
                    "      \"family_name\": \"" + secondNameStr + "\"," +
                    "      \"first_name\": \"" + firstNameStr + "\"," +
                    "      \"gender\": \"" + genderStr + "\"," +
                    "      \"middle_name\": \"" + middleNameStr + " \"," +
                    "      \"nationality_country_code\": \"RU\"" +
                    "    }," +
                    "    \"phone\": \"" + phoneStr +"\"" +
                    "  }," +
                    "  \"datetime\": \"" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) +
                    "T" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "Z\"," +
                    "  \"interest_rate\":"+ interest + "," +
                    "  \"requested_amount\":\"" + 26226 + "\"," +
                    "  \"requested_term\":\""  + 36 + "\"," +
                    "  \"trade_mark\": \"" + brand + "\"," +
                    "  \"vehicle_cost\":\"" +  2626262 + "\"" +
                    "}");
            Request request = new Request.Builder()
                    .addHeader("x-ibm-client-id", "168ed85c91a2a30d05ea0f35439058cb")
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();


            Log.d("HTTP", "Method started ");
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("HTTP", responseStr);
                    //tv.setText(responseStr);
                    return responseStr;
                }
                else {
                    Log.e("HTTP", response.body().string());
                }

            } catch (IOException e) {
                Log.e("HTTP", "error in getting response post request okhttp");
            }
            return null;

        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("HTTP", "Start HTTP");
            String response = this.postHttpResponse();
            return response;
            //return null;
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(Main2Activity.this,
                    "Заявка на кредит успешно отправлена. Ожидайте звонок из банка.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Main2Activity.this, EndActivity.class);
            startActivity(intent);
        }
    }
}
