package ru.dolbak.vtb_auto;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class MyDialogFragmentChoosePhotoSource  extends DialogFragment {
    static final int GALLERY_REQUEST = 1;
    static final int CAMERA_REQUEST = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Загрузка фотографии";
        String message = "Откуда будем брать фотографию?";
        String button1String = "Сделать фото";
        String button2String = "Выбрать из памяти устройства";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        builder.setCancelable(true);

        return builder.create();

    }

}
