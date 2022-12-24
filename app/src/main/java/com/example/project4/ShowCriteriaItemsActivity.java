package com.example.project4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project4.Adapters.CriteriaViewAdapter;
import com.example.project4.Adapters.CriterionSubSectionsAdapter;
import com.example.project4.Fragments.NewItemFragment;
import com.example.project4.Login.StartActivity;
import com.example.project4.Objects.SelectedSub;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowCriteriaItemsActivity extends AppCompatActivity {

  ArrayList<SelectedSub> selectedSubArrayList;
  CriterionSubSectionsAdapter criterionSubSectionsAdapter;
  RecyclerView recyclerView1;
  FirebaseFirestore db;
  String criterion_selected_value = CriteriaViewAdapter.criterion_selected_value;
  ProgressDialog progressDialog;
  public static List<String> existingDocuments;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.criterion_sub_sections);

    //Back Button
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(criterion_selected_value);

    progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Fetching Data...");
    progressDialog.show();

    db = FirebaseFirestore.getInstance();

    recyclerView1 = findViewById(R.id.criterion_list);
    recyclerView1.setHasFixedSize(true);
    recyclerView1.setLayoutManager(new LinearLayoutManager(this));
    selectedSubArrayList = new ArrayList<>();
    criterionSubSectionsAdapter = new CriterionSubSectionsAdapter(ShowCriteriaItemsActivity.this, selectedSubArrayList);
    recyclerView1.setAdapter(criterionSubSectionsAdapter);

    EventChangeListener();

    //Add Button
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(v -> {
      NewItemFragment bottomSheet = new NewItemFragment();
      bottomSheet.show(getSupportFragmentManager(),
        "NewItem");
    });
  }

  private void EventChangeListener() {

    db.collection("NBA").document(criterion_selected_value).collection(criterion_selected_value)
      .addSnapshotListener((value, error) -> {
        if (error != null) {
          Log.e("Firestore Error", error.getMessage());
          return;
        }
        assert value != null;
        existingDocuments = new ArrayList<>();
        for (DocumentChange dc1 : value.getDocumentChanges()) {
          if (dc1.getType() == DocumentChange.Type.ADDED) {
            selectedSubArrayList.add(dc1.getDocument().toObject(SelectedSub.class));
            existingDocuments.add(dc1.getDocument().getId());
          }
        }
        if (progressDialog.isShowing()) {
          progressDialog.dismiss();
        }
        criterionSubSectionsAdapter.notifyDataSetChanged();
      });
  }


  //3dots
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.action_logout) {
      FirebaseAuth.getInstance().signOut();
      startActivity(new Intent(ShowCriteriaItemsActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
      return true;
    }

    if (id == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

