package com.example.project4.Objects;

public class SelectedCriterion {

  private String selected_criterion;
  private int position;

  public SelectedCriterion(String selected_criterion, int position) {
    this.selected_criterion = selected_criterion;
    this.position = position;
  }

  public String getSelected_criterion() {
    return selected_criterion;
  }

  public int getPosition() {
    return position;
  }

  public void setSelected_criterion(String selected_criterion) {
    this.selected_criterion = selected_criterion;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
