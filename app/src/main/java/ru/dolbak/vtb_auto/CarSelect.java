package ru.dolbak.vtb_auto;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CarSelect extends AppCompatActivity {

    TextView tv;
    String carBrand, carModel;
    HashMap<String, String> countriesFlags;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        tv = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        HashMap<String, String[]> carNames = new HashMap<>();
        carNames.put("Mazda 6", new String[]{"Mazda", "6"});
        carNames.put("Mazda 3", new String[]{"Mazda", "6"});
        carNames.put("Cadillac ESCALADE", new String[]{"Cadillac", "ESCALADE"});
        carNames.put("Jaguar F-PACE", new String[]{"Jaguar", "F-PACE"});
        carNames.put("BMW 5", new String[]{"BMW", "5 серия"});
        carNames.put("KIA Sportage", new String[]{"KIA", "Sportage"});
        carNames.put("Chevrolet Tahoe", new String[]{"Chevrolet", "Tahoe"});
        carNames.put("KIA K5", new String[]{"KIA", "K5"});
        carNames.put("Hyundai Genesis", new String[]{"Hyundai", "Genesis"});
        carNames.put("Toyota Camry", new String[]{"Toyota", "Camry"});
        carNames.put("Mercedes A", new String[]{"Mercedes", "A"});
        carNames.put("Land Rover RANGE ROVER VELAR", new String[]{"Land Rover", "RANGE ROVER VELAR"});
        carNames.put("BMW 3", new String[]{"BMW", "3 серии"});
        carNames.put("KIA Optima", new String[]{"KIA", "Optima"});

        countriesFlags = new HashMap<>();
        countriesFlags.put("Германия", "\uD83C\uDDE9\uD83C\uDDEA");
        countriesFlags.put("США", "\uD83C\uDDFA\uD83C\uDDF8");
        countriesFlags.put("Япония", "\uD83C\uDDEF\uD83C\uDDF5");
        countriesFlags.put("Россия", "\uD83C\uDDF7\uD83C\uDDFA");
        countriesFlags.put("Великобритания", "\uD83C\uDDEC\uD83C\uDDE7");
        countriesFlags.put("Южная Корея", "\uD83C\uDDF0\uD83C\uDDF7");


        String carShortName = getIntent().getStringExtra("CarName");
        Log.d("Cars", carShortName);
        Log.d("Cars", carNames.get(carShortName).toString());
        carBrand = carNames.get(carShortName)[0];
        carModel = carNames.get(carShortName)[1];
        Network network = new Network();
        network.execute();

    }

    class Network extends AsyncTask<Void, Void, String> {
        String postHttpResponse() {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://gw.hackathon.vtb.ru/vtb/hackathon/marketplace")
                    .get()
                    .addHeader("x-ibm-client-id", "168ed85c91a2a30d05ea0f35439058cb")
                    .addHeader("accept", "application/json")
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
            super.onPostExecute(response);
            Log.d("HTML", response);
            //tv.setText(response);
            JSONObject jObject;
            try {
                jObject = new JSONObject(response);
                JSONArray jArray =  jObject.getJSONArray("list");
                Log.d("HTTP", jArray.toString());
                JSONObject carinfo = null;
                for (int i = 0; i < jArray.length(); i++){
                    String brand = jArray.getJSONObject(i).getString("title");
                    if (brand.equals(carBrand)){
                        JSONArray jModelsArray = jArray.getJSONObject(i).getJSONArray("models");
                        for (int j = 0; j < jModelsArray.length(); j++){
                            String model = jModelsArray.getJSONObject(j).getString("title");
                            if (model.equals(carModel)){
                                carinfo = jModelsArray.getJSONObject(j);
                                break;
                            }
                        }
                    }
                }

                if (carinfo == null){
                    tv.setText("Авто не найдено!");
                    return;
                }

                Log.d("carINFO", carinfo.toString());
                int doors=0, price=0;
                String carType="", enType="", photo1="", photo2="", photo3="", photo4="", photo5="", photo6="", photo7="", country="";
                if (carinfo.has("bodies")){
                    doors = carinfo.getJSONArray("bodies").getJSONObject(0).getInt("doors");
                }
                if (carinfo.has("bodies")) {
                    if (carinfo.getJSONArray("bodies").length() > 0) {
                        if (carinfo.getJSONArray("bodies").getJSONObject(0).has("title")){
                            carType = carinfo.getJSONArray("bodies").getJSONObject(0).getString("title");
                        }
                        if (carinfo.getJSONArray("bodies").getJSONObject(0).has("type")){
                            enType = carinfo.getJSONArray("bodies").getJSONObject(0).getString("type");
                        }
                        if (carinfo.getJSONArray("bodies").getJSONObject(0).has("photo")){
                            photo1 = carinfo.getJSONArray("bodies").getJSONObject(0).getString("photo");
                        }
                    }
                }
                if (carinfo.has("photo")){
                    photo2 = carinfo.getString("photo");
                }
                if (carinfo.has("renderPhotos")){
                    if (carinfo.getJSONObject("renderPhotos").has("main")){
                        if (carinfo.getJSONObject("renderPhotos").getJSONObject("main").has(enType)){
                            photo3 = carinfo.getJSONObject("renderPhotos").getJSONObject("main").getJSONObject(enType).getString("path");
                        }
                    }
                    if (carinfo.getJSONObject("renderPhotos").has("render_widget_right")){
                        if (carinfo.getJSONObject("renderPhotos").getJSONObject("render_widget_right").has(enType)){
                            photo4 = carinfo.getJSONObject("renderPhotos").getJSONObject("render_widget_right").getJSONObject(enType).getString("path");
                        }

                    }
                    if(carinfo.getJSONObject("renderPhotos").has("render_front")) {
                        if (carinfo.getJSONObject("renderPhotos").getJSONObject("render_front").has(enType)) {
                            photo5 = carinfo.getJSONObject("renderPhotos").getJSONObject("render_front").getJSONObject(enType).getString("path");
                        }
                    }

                    if(carinfo.getJSONObject("renderPhotos").has("render_side")) {
                        if (carinfo.getJSONObject("renderPhotos").getJSONObject("render_side").has(enType)) {
                            photo6 = carinfo.getJSONObject("renderPhotos").getJSONObject("render_side").getJSONObject(enType).getString("path");
                        }
                    }
                }

                if (carinfo.has("sizesPhotos")){
                    if (carinfo.has("width250")){
                        photo7 = carinfo.getJSONObject("sizesPhotos").getString("width250");
                    }
                }

                if (carinfo.has("minPrice")){
                    price = carinfo.getInt("minPrice");
                }

                if (carinfo.has("brand")){
                    if (carinfo.getJSONObject("brand").has("country")){
                        if (carinfo.getJSONObject("brand").getJSONObject("country").has("title")){
                            country = carinfo.getJSONObject("brand").getJSONObject("country").getString("title");
                        }
                    }
                }
                String flag = "";
                if (countriesFlags.containsKey(country)){
                    flag = countriesFlags.get(country);
                }
                String carText = carBrand + " " + carModel + "\nЦена: " + Integer.toString(price) +
                        "\nТип корпуса: " + carType + "\nКолличество дверей: " + Integer.toString(doors) +
                        "\nСтрана производитель: " + flag + country;
                tv.setText(carText);
                Network2 network1 = new Network2();
                network1.execute(photo2);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    class Network2 extends AsyncTask<String, Void, Bitmap> {

        Bitmap getImg(String link){
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(link)
                    .get()
                    .build();


            Log.d("HTTP", "Method started ");
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    //tv.setText(responseStr);
                    return bitmap;
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
        protected Bitmap doInBackground(String... strings) {
            return getImg(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }
    }
}
