package com.example.project4.Objects;

public class SelectedSub {

  private String title;
  private String user;
  private String link;

  public SelectedSub(){}

  public SelectedSub(String link, String title, String user) {
    this.title = title;
    this.user = user;
    this.link = link;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) { this.user = user; }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }
}
