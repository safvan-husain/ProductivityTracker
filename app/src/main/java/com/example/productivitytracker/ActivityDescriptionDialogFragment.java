package com.example.productivitytracker;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ActivityDescriptionDialogFragment extends DialogFragment {

    private EditText activityEditText;

    public interface ActivityDescriptionListener {
        void onActivityDescriptionEntered(String description);
    }

    private ActivityDescriptionListener listener;

    public void setListener(ActivityDescriptionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter Activity Description");
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_activity_description, null);
        activityEditText = view.findViewById(R.id.activity_edit_text);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = activityEditText.getText().toString();
                if (listener != null) {
                    listener.onActivityDescriptionEntered(description);
                }
            }
        });
        return builder.create();
    }
}
