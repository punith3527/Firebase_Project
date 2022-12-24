package com.example.project4.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project4.Objects.SelectedSub;
import com.example.project4.R;
import com.example.project4.ShowCriteriaItemsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CriterionSubSectionsAdapter extends RecyclerView.Adapter<CriterionSubSectionsAdapter.MyViewHolder> {

  Context context;
  ArrayList<SelectedSub> selectedSubArrayList;
  FirebaseFirestore db;
  String criterion_selected_value = CriteriaViewAdapter.criterion_selected_value;

  public CriterionSubSectionsAdapter(Context context, ArrayList<SelectedSub> selectedSubArrayList) {
    this.context = context;
    this.selectedSubArrayList = selectedSubArrayList;
  }

  @NonNull
  @Override
  public CriterionSubSectionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(context).inflate(R.layout.criterion_sub_item, parent, false);
    return new MyViewHolder(v);

  }

  @Override
  public void onBindViewHolder(@NonNull CriterionSubSectionsAdapter.MyViewHolder holder, int position) {

    db = FirebaseFirestore.getInstance();
    ShowCriteriaItemsActivity activity = (ShowCriteriaItemsActivity) context;

    SelectedSub selectedSub = selectedSubArrayList.get(position);

    holder.title1.setText(selectedSub.getTitle());
    holder.link1.setMovementMethod(LinkMovementMethod.getInstance());
    holder.link1.setLinkTextColor(Color.BLUE);
    holder.link1.setText(Html.fromHtml(selectedSub.getLink()));

    holder.edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert currentUser != null;
        if (currentUser.equals(selectedSub.getUser())) {
          showBottomSheetView();
        } else {
          Toast.makeText(context, "You don't have access", Toast.LENGTH_SHORT).show();
        }
      }

      private void showBottomSheetView() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.SheetDialog);

        bottomSheetDialog.setContentView(R.layout.update_sub_section);

        EditText link = bottomSheetDialog.findViewById(R.id.item_link);
        Button update = bottomSheetDialog.findViewById(R.id.item_update);

        assert link != null;
        link.setText(selectedSub.getLink());

        assert update != null;
        update.setOnClickListener(v -> {

            if (link.getText().toString().trim().length() > 0) {
              AlertDialog.Builder alert = new AlertDialog.Builder(context);
              alert.setTitle("Edit entry");
              alert.setMessage("Are you sure you want to Edit?");
              alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                //Continue with Update
                db.collection("NBA").document(criterion_selected_value).collection(criterion_selected_value)
                  .document(selectedSub.getTitle()).update("link", String.valueOf(link.getText()))
                  .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                      Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();
                      activity.recreate();
                    } else {
                      Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                  });
              });
              alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
              alert.show();
            } else {
              Toast.makeText(context, "Drive Link Can't be Empty", Toast.LENGTH_SHORT).show();
            }
          }
        );

        bottomSheetDialog.show();
      }
    });

    holder.delete.setOnClickListener(v -> {

      String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
      assert currentUser != null;
      if (currentUser.equals(selectedSub.getUser())) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
          // continue with delete
          db.collection("NBA").document(criterion_selected_value).collection(criterion_selected_value)
            .document(selectedSub.getTitle())
            .delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
              activity.recreate();
            } else {
              Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
          });
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
        alert.show();
      } else {
        Toast.makeText(context, "You don't have access", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return selectedSubArrayList.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title1, link1;
    ImageButton edit, delete;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      title1 = itemView.findViewById(R.id.item_title_1);
      link1 = itemView.findViewById(R.id.item_link_1);
      edit = itemView.findViewById(R.id.edit);
      delete = itemView.findViewById(R.id.delete);
    }
  }
}
