package it.entando.pa.spid.model;

import java.io.IOException;
import java.util.Base64;

public class Credentials {

  private String username;
  private String password;

  public Credentials(String username, String password) {
    try {
      this.password = new String(Base64.getDecoder().decode(password));
      this.username = new String(Base64.getDecoder().decode(username));
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
