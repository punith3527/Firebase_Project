package com.example.project4.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.project4.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

  @Override
  protected void onStart() {
    super.onStart();

    final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

    if (activeNetwork != null && activeNetwork.isConnected()) {
      if (FirebaseAuth.getInstance().getCurrentUser() != null) {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
      } else {
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
      }
    } else {
      Toast.makeText(getApplicationContext(), "Please connect to Internet", Toast.LENGTH_LONG).show();
      finish();
      System.exit(0);
    }
  }
}
