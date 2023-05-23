package com.example.taskmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NewTypeDialog extends DialogFragment {

    //Context return NewEvent
    View dialogView;

    //Views
    EditText typeName, typeIcon;
    Button typeColor;

    //Values
    String name, icon, color;

    int defaultColor;


    public interface NewTypeDialogListener { //listeners
            void onDialogPositiveClick(DialogFragment dialog, String data);
            void onDialogNegativeClick(DialogFragment dialog);
    }
    private NewTypeDialogListener mListener;


    public NewTypeDialog() {

    }

    public void setListener(NewTypeDialogListener listener) {
            mListener = listener;
        }

        @Override

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            dialogView = inflater.inflate(R.layout.add_type_popup_layout, null);

            //--------Type PopUP---------
            typeName = dialogView.findViewById(R.id.typeName);
            typeColor = dialogView.findViewById(R.id.typeColor);
            //--------Type PopUP---------

            //--------Color Selector-----

            defaultColor = ContextCompat.getColor(dialogView.getContext(),R.color.purple_700);
            typeColor.setOnClickListener(l -> showColorPicker());

            //--------Color Selector-----

            name = icon = color = null;

            builder.setView(dialogView)
                    .setTitle("Add New Type")
                    .setPositiveButton("OK", (dialog, which) -> {
                        name = typeName.getText().toString();
                        icon = typeIcon.getText().toString();
                        color = String.valueOf(defaultColor);
                        mListener.onDialogPositiveClick(NewTypeDialog.this, name);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        mListener.onDialogNegativeClick(NewTypeDialog.this);
                    });

            return builder.create();
        }

    private void showColorPicker() {
        AmbilWarnaDialog aWDialog = new AmbilWarnaDialog(dialogView.getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
                        defaultColor = color;
                        typeColor.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
        });
        aWDialog.show();
    }
    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }


}
