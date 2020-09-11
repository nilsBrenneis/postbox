package de.bre.postbox.actuator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(TelegramActuator.class);

  private long lastHitOnApi;

  @Override
  public void notifyUser() {
    if (lastHitOnApiMoreThanOneMinuteAgo()) {
      sendTelegramMessage();
      log.info("Benachrichtigung versendet mit {}", this.getClass().getName());
    }
  }

  private boolean lastHitOnApiMoreThanOneMinuteAgo() {
    if (System.currentTimeMillis() - lastHitOnApi > TimeUnit.MINUTES.toMillis(1)) {
      lastHitOnApi = System.currentTimeMillis();
      return true;
    }
    return false;
  }

  private void sendTelegramMessage() {
    String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    urlString = String.format(urlString, apiToken, chatId, message);

    URL url;
    try {
      url = new URL(urlString);

      URLConnection conn = url.openConnection();

      StringBuilder sb = new StringBuilder();
      InputStream is = new BufferedInputStream(conn.getInputStream());

      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        sb.append(inputLine);
      }
      log.info("Versendete Nachricht: {}", sb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

