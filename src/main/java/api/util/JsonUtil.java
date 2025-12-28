package api.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonUtil {
  private static final Gson gson = new Gson();

  public static <T> T fromJson(HttpServletRequest request, Class<T> clazz) throws IOException {
    BufferedReader reader = request.getReader();
    return gson.fromJson(reader, clazz);
  }

  public static void toJson(HttpServletResponse response, Object object) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    out.print(gson.toJson(object));
    out.flush();
  }

  public static String toJson(Object obj) {
    return gson.toJson(obj);
  }
}
