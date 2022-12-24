package com.example.project4.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project4.Adapters.CriteriaViewAdapter;
import com.example.project4.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;
import static com.example.project4.ShowCriteriaItemsActivity.existingDocuments;

public class NewItemFragment extends BottomSheetDialogFragment {

  String criterion_selected_value = CriteriaViewAdapter.criterion_selected_value;
  FirebaseFirestore db;
  String currentUser;
  ProgressDialog progressDialog;

  @Override
  public int getTheme() {
    return R.style.NoBackgroundDialogTheme;
  }

  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.new_sub_section, container, false);
    v.setBackgroundResource(R.drawable.bottom_sheet_background);

    Button add = v.findViewById(R.id.item_add);
    EditText title = v.findViewById(R.id.item_title);
    EditText link = v.findViewById(R.id.item_link);
    db = FirebaseFirestore.getInstance();
    currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

    Log.e(TAG, existingDocuments.toString());

    add.setOnClickListener(view -> {

      progressDialog = new ProgressDialog(getActivity());
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Adding");
      progressDialog.show();

      String title1 = title.getText().toString();
      String link1 = link.getText().toString();

      if (link.getText().toString().trim().length() > 0 && title.getText().toString().trim().length() > 0) {

        if (existingDocuments.contains(title1)) {
          if (progressDialog.isShowing()) {
            progressDialog.dismiss();
          }
          Toast.makeText(getActivity(), "Document already exists", Toast.LENGTH_SHORT).show();

        } else {

          Map<String, Object> project = new HashMap<>();
          project.put("title", title1);
          project.put("user", currentUser);
          project.put("link", link1);
          db.collection("NBA").document(criterion_selected_value).collection(criterion_selected_value)
            .document(title1).set(project).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              if (progressDialog.isShowing()) {
                progressDialog.dismiss();
              }
              Toast.makeText(getActivity(), "Values added!", Toast.LENGTH_SHORT).show();
              dismiss();
            }
          });
        }
      } else {
        Toast.makeText(getActivity(), "Title or Link Can't be Empty", Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing()) {
          progressDialog.dismiss();
        }
      }
    });
    return v;
  }
}
