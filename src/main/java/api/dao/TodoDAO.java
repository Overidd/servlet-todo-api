package api.dao;

import api.config.DBConnection;
import api.model.Todo;
import api.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

  public List<Todo> getAll() throws SQLException {
    List<Todo> todos = new ArrayList<>();
    String sql = "SELECT * FROM todo";
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Todo t = new Todo();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setCompleted(rs.getBoolean("completed"));
        t.setCategory(rs.getString("category"));
        todos.add(t);
      }
    }
    return todos;
  }

  public Todo getById(int id) throws SQLException {
    String sql = "SELECT * FROM todo WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Todo t = new Todo();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setCompleted(rs.getBoolean("completed"));
        t.setCompleted(rs.getBoolean("category"));
        return t;
      }
    }
    return null;
  }

  public Todo create(Todo todo) throws SQLException {
    String sql = "INSERT INTO todo(title, completed, category) VALUES(?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, todo.getTitle());
      ps.setBoolean(2, todo.isCompleted());
      ps.setString(3, todo.getCategory());
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        todo.setId(rs.getInt(1));
      }
    }
    return todo;
  }

  public boolean update(Todo todo) throws SQLException {
    String sql = "UPDATE todo SET title = ?, completed = ?, category = ?, WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, todo.getTitle());
      ps.setBoolean(2, todo.isCompleted());
      ps.setString(3, todo.getCategory());
      ps.setInt(3, todo.getId());

      return ps.executeUpdate() > 0;
    }
  }

  public boolean delete(int id) throws SQLException {
    String sql = "DELETE FROM todo WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      return ps.executeUpdate() > 0;
    }
  }

  public int deleteAll() throws SQLException {
    String sql = "DELETE FROM todo";
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement()) {
      return stmt.executeUpdate(sql);
    }
  }

  public User getUserByEmail(String email) throws SQLException {
    String sql = "SELECT id, email, name, password FROM users WHERE email = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
      }
    }
    return null;
  }

}
