package com.assignment.androidphoto91.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.assignment.androidphoto91.R;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddAlbumDialogFragment extends DialogFragment {

    private EditText editTextAlbumName;
    private AddAlbumDialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_album, null);
        editTextAlbumName = view.findViewById(R.id.editText_albumName);

        builder.setTitle("Add Album");
        builder.setView(view);
        builder.setPositiveButton("Add", (dialog, id) ->
                listener.onAddAlbumDialogPositiveClick(AddAlbumDialogFragment.this));
        builder.setNegativeButton("Cancel", (dialog, id) ->  { /* do nothing */ });
        return builder.create();
    }

    public String getAlbumName() {
        return editTextAlbumName.getText().toString();
    }

    public interface AddAlbumDialogListener {
        public void onAddAlbumDialogPositiveClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddAlbumDialogListener) context;
        } catch (ClassCastException e) {
            // ...
        }
    }
}
