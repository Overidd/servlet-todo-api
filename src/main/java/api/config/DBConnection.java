package api.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

  private static final String HOST = "crossover.proxy.rlwy.net";
  private static final String PORT = "36600";
  private static final String DATABASE = "railway";
  private static final String USER = "root";
  private static final String PASSWORD = "bLzbrsTGbJYIuXKdLNrVHIIvGcZLnCei";

  public static Connection getConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
          + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

      System.out.println("Conectando a: " + url);
      return DriverManager.getConnection(url, USER, PASSWORD);

    } catch (Exception e) {
      throw new RuntimeException("Error conectando a la base de datos", e);
    }
  }
}
