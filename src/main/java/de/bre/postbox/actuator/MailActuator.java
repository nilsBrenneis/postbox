package de.bre.postbox.actuator;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailActuator implements Actuator {

  private static final Logger log = LoggerFactory.getLogger(MailActuator.class);

  @Resource(name = "myMailSender")
  public JavaMailSender emailSender;

  @Value("${spring.mail.from}")
  private String from;

  @Value("${spring.mail.to}")
  private String to;

  @Value("${spring.mail.subject}")
  private String subject;

  @Value("${spring.notify.message}")
  private String text;

  private long lastHitOnApi;

  @Override
  public void notifyUser() {
    if (lastHitOnApiMoreThanOneMinuteAgo()) {
      sendMail();
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

  private void sendMail() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
    log.info("Versende Mail mit Inhalt: {}", message);
  }
}
