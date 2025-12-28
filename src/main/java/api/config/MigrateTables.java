package api.config;

import java.sql.Connection;
import java.sql.Statement;

public class MigrateTables {

  public static void migrate() throws Exception {
    String[] sqlStatements = new String[]{
        "CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "email VARCHAR(100) UNIQUE NOT NULL," +
            "password VARCHAR(255) NOT NULL" +
            ")",

        "CREATE TABLE IF NOT EXISTS product (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "price DECIMAL(10,2) NOT NULL" +
            ")",

        "CREATE TABLE todo (" +
            "    id INT AUTO_INCREMENT PRIMARY KEY," +
            "    title VARCHAR(255) NOT NULL," +
            "    completed BOOLEAN DEFAULT FALSE" +
            ")"
    };

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement()) {

      for (String sql : sqlStatements) {
        stmt.execute(sql);
      }
    }
  }
}
