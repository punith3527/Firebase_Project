package com.example.project4.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project4.Objects.SelectedCriterion;
import com.example.project4.R;
import com.example.project4.ShowCriteriaItemsActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CriteriaViewAdapter extends RecyclerView.Adapter<CriteriaViewAdapter.MyViewHolder> {

  public static String criterion_selected_value;
  ArrayList<SelectedCriterion> criteriaList;
  Context context;

  public CriteriaViewAdapter(Context context, ArrayList<SelectedCriterion> criterion) {
    this.context = context;
    this.criteriaList = criterion;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.criterion_item, viewGroup, false);
    return new MyViewHolder(v);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    //final String criteriaItem = criteriaList.get(position);

    SelectedCriterion selectedCriterion = criteriaList.get(position);

    holder.name.setText(selectedCriterion.getSelected_criterion());
    holder.itemView.setOnClickListener(v -> {
      Intent intent = new Intent(context, ShowCriteriaItemsActivity.class);
      criterion_selected_value = selectedCriterion.getSelected_criterion();
      context.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {
    return criteriaList.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    TextView name;

    public MyViewHolder(View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.textView);
    }
  }
}
