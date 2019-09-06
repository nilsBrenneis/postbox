package de.bre.postbox.actuator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramActuator implements Actuator {

  @Value("${spring.telegram.apiToken}")
  private String apiToken;

  @Value("${spring.telegram.chatId}")
  private String chatId;

  @Value("${spring.notify.message}")
  private String message;

  @Override
  public void notifyUser() throws Exception {
    String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    urlString = String.format(urlString, apiToken, chatId, message);

    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();

    StringBuilder sb = new StringBuilder();
    InputStream is = new BufferedInputStream(conn.getInputStream());

    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      sb.append(inputLine);
    }
  }
}
