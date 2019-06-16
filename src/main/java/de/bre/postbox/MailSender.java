package de.bre.postbox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class MailSender {
  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private String port;

  /**
   * sets needed server info und credentials.
   * @return ready to use mailSender
   */
  @Bean(name = "myMailSender")
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(Integer.parseInt(port));
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    return mailSender;
  }
}