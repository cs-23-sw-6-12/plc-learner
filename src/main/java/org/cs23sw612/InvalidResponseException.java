package org.cs23sw612;

public class InvalidResponseException extends RuntimeException {
  public InvalidResponseException(int received, int expected) {
    super(String.format("Invalid Response. Expected {%d}, got {%d}", expected, received));
  }
}
