package de.bre.postbox;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private static final Logger log = LoggerFactory.getLogger(PostboxApplication.class);

  private Controller() {
    lastHitOnApi = System.currentTimeMillis();
  }

  private long lastHitOnApi;

  @Resource(name = "myMailSender")
  public JavaMailSender emailSender;

  @Value("${spring.mail.from}")
  private String from;

  @Value("${spring.mail.to}")
  private String to;

  @Value("${spring.mail.subject}")
  private String subject;

  @Value("${spring.mail.text}")
  private String text;

  /**
   * when called, sends mail to desired adress.
   * To prevent spam/errors it does this only every minute.
   * @return http 200
   */
  @RequestMapping(path = "/postbox", method = RequestMethod.GET)
  public ResponseEntity<?>  postbox() {
    log.info("API aufgerufen");
    if (System.currentTimeMillis() - lastHitOnApi > TimeUnit.MINUTES.toMillis(1)) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(from);
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      emailSender.send(message);
      log.info("gültiges Postbox-Event. Versende Mail");

      lastHitOnApi = System.currentTimeMillis();
    }
    return new ResponseEntity(HttpStatus.OK);
  }
}