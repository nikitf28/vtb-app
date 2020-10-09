package ru.dolbak.vtb_auto;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    static final int GALLERY_REQUEST = 1;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
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
            imageView.setImageBitmap(bitmap);
        }
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
        if (bitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        }
    }

}
