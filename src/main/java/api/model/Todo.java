package api.model;

public class Todo {

  private int id;
  private String title;
  private String completed;
  private String category;
  private int userId; // 👈 nuevo campo

  public Todo() {
  }

  public Todo(String title, String completed, String category, int userId) {
    this.title = title;
    this.completed = completed;
    this.category = category;
    this.userId = userId;
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

  public String isCompleted() {
    return completed;
  }

  public void setCompleted(String completed) {
    this.completed = completed;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  // 👇 nuevos getters/setters
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
