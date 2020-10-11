package ru.dolbak.vtb_auto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    static final int GALLERY_REQUEST = 1;
    String postStr = "";
    //ImageView imageView;
    //TextView tv;
    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //imageView = findViewById(R.id.imageView);
        //tv = findViewById(R.id.textView);
    }

    public void onClickButton1(View view){
        MyDialogFragmentChoosePhotoSource myDialogFragmentChooseCar = new MyDialogFragmentChoosePhotoSource();
        myDialogFragmentChooseCar.show(this.getFragmentManager(), "myDialog");
    }

    public void onClickButton2(View view){
        MyDialogFragmentChooseCar myDialogFragmentChooseCar = new MyDialogFragmentChooseCar();
        myDialogFragmentChooseCar.show(this.getFragmentManager(), "myDialog");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        Log.d("bla", "onActivityResult: ");
        Toast.makeText(this,
                "Фото сделано1" ,
                Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку
            bitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(bitmap);
        }
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //imageView.setImageBitmap(bitmap);
        }
        if (bitmap != null){
            int height = (int) (bitmap.getHeight() / (bitmap.getHeight() * 1.0 / 250));
            int width = (int) (bitmap.getWidth() / (bitmap.getWidth() * 1.0 / 250));
            //matrix = new Matrix();
            Matrix matrix = new Matrix();
            matrix.postScale((float) height / bitmap.getHeight(), (float) width / bitmap.getWidth());
            Log.d("Image", bitmap.getHeight() + " " + bitmap.getWidth() + " " + height + " " + width);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            postStr ="";
            for (int i = 0; i < encodedImage.length(); i++){
                if (encodedImage.charAt(i) != '\n'){
                    postStr += encodedImage.charAt(i);
                }
            }
            Network network = new Network();
            network.execute(postStr);
            //Log.d("API", req);

        }

    }


    class Network extends AsyncTask<String, Void, String> {
        String postHttpResponse(String requestParam) {
            Log.d("HTTP", Integer.toString(requestParam.length()));
            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://gw.hackathon.vtb.ru/vtb/hackathon/car-recognize";
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"content\":\"" + requestParam + "\"}");
            Request request = new Request.Builder()
                    .addHeader("x-ibm-client-id", "168ed85c91a2a30d05ea0f35439058cb")
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            Log.d("HTTP", requestParam);
            Response response = null;
            Log.d("HTTP", "Method started ");
            try {
                response = httpClient.newCall(request).execute();
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
        protected String doInBackground(String... strings) {
            Log.d("HTTP", "Start HTTP");
            String response = this.postHttpResponse(strings[0]);
            return response;
            //return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //tv.setText(response);
            JSONObject jObject;
            try {
                String[] carsNames = {"Mazda 6", "Mazda 3", "Cadillac ESCALADE", "Jaguar F-PACE",
                        "BMW 5", "KIA Sportage", "Chevrolet Tahoe", "KIA K5", "Hyundai Genesis",
                        "Toyota Camry", "Mercedes A", "Land Rover RANGE ROVER VELAR", "BMW 3",
                        "KIA Optima"};
                double[] carResults = new double[14];
                jObject = new JSONObject(response);
                JSONObject carData = jObject.getJSONObject("probabilities");
                carResults[0] = carData.getDouble("Mazda 6");
                carResults[1] = carData.getDouble("Mazda 3");
                carResults[2] = carData.getDouble("Cadillac ESCALADE");
                carResults[3] = carData.getDouble("Jaguar F-PACE");
                carResults[4] = carData.getDouble("BMW 5");
                carResults[5] = carData.getDouble("KIA Sportage");
                carResults[6] = carData.getDouble("Chevrolet Tahoe");
                carResults[7] = carData.getDouble("KIA K5");
                carResults[8] = carData.getDouble("Hyundai Genesis");
                carResults[9] = carData.getDouble("Toyota Camry");
                carResults[10] = carData.getDouble("Mercedes A");
                carResults[11] = carData.getDouble("Land Rover RANGE ROVER VELAR");
                carResults[12] = carData.getDouble("BMW 3");
                carResults[13] = carData.getDouble("KIA Optima");
                Log.d("HTTP", jObject.get("probabilities").toString());
                int pos = -1;
                double val = -1;
                for (int i = 0; i < 14; i++){
                    if (carResults[i] > val){
                        val = carResults[i];
                        pos = i;
                    }
                }
                Toast.makeText(MainActivity.this, carsNames[pos], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, CarSelect.class);
                intent.putExtra("CarName", carsNames[pos]);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
