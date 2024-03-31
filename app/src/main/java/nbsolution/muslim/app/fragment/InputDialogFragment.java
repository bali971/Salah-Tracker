package nbsolution.muslim.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import nbsolution.muslim.app.R;

public class InputDialogFragment extends DialogFragment {

    public interface InputDialogListener {
        void onSaveClicked(String city, String country);
    }

    private InputDialogListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (InputDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement InputDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_input, null);
        final EditText editTextCity = view.findViewById(R.id.editTextCity);
        final EditText editTextCountry = view.findViewById(R.id.editTextCountry);

        builder.setView(view)
                .setTitle("Enter City and Country")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextCity.getText().toString().isEmpty()){
                            Toast.makeText(view.getContext(), "Please enter the City to continue", Toast.LENGTH_LONG).show();
                        }else if (editTextCountry.getText().toString().isEmpty()){
                            Toast.makeText(view.getContext(), "Please enter the Country to continue", Toast.LENGTH_LONG).show();
                        }else if (editTextCountry.getText().toString().isEmpty() && editTextCity.getText().toString().isEmpty()){
                            Toast.makeText(view.getContext(), "Please enter the City and Country to continue", Toast.LENGTH_LONG).show();
                        }else {
                            String city = editTextCity.getText().toString();
                            String country = editTextCountry.getText().toString();
                            mListener.onSaveClicked(city, country);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
