package api.model;

public class Todo {
  private int id;
  private String title;
  private boolean completed;
  private String category;

  public Todo() {
  }

  public Todo(String title, boolean completed, String category) {
    this.title = title;
    this.completed = completed;
    this.category = category;
  }

  // Getters y setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
