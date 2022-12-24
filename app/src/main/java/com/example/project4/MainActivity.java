package com.example.project4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project4.Adapters.CriteriaViewAdapter;
import com.example.project4.Login.StartActivity;
import com.example.project4.Objects.SelectedCriterion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  CriteriaViewAdapter criteriaViewAdapter;
  FirebaseFirestore db;
  ArrayList<SelectedCriterion> criteriaList;
  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Fetching Data...");
    progressDialog.show();

    db = FirebaseFirestore.getInstance();

    recyclerView = findViewById(R.id.items_list);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    criteriaList = new ArrayList<>();
    criteriaViewAdapter = new CriteriaViewAdapter(MainActivity.this, criteriaList);
    recyclerView.setAdapter(criteriaViewAdapter);

    EventChangeListener();
  }

  private void EventChangeListener() {
    String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

    assert currentUser != null;

    db.collection("NBA").get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        for (QueryDocumentSnapshot document : task.getResult()) {
          criteriaList.add(new SelectedCriterion(document.getId(), ((Long) Objects.requireNonNull(document.get("value"))).intValue()));
          Collections.sort(criteriaList, (o1, o2) -> Integer.compare(o1.getPosition(), o2.getPosition()));
        }
        if (progressDialog.isShowing()) {
          progressDialog.dismiss();
        }
        criteriaViewAdapter.notifyDataSetChanged();
      } else {
        Log.e(TAG, "Error getting documents: ", task.getException());
      }
    });
  }

  //3 dots menu
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
      startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
