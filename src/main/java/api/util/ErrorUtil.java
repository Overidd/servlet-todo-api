package api.util;

public class ErrorUtil extends Exception {
  private final int statusCode;

  public ErrorUtil(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
