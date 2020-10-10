package ru.dolbak.vtb_auto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class MyDialogFragmentChooseCar extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] stringArray = getResources().getStringArray(R.array.my_string_array);
        Arrays.sort(stringArray);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите авто")
                .setItems(stringArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),
                                "Вы выбрали: " + stringArray[which],
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CarSelect.class);
                        intent.putExtra("CarName", stringArray[which]);
                        startActivity(intent);
                    }
                });

        return builder.create();
    }
}
