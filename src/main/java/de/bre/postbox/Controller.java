package de.bre.postbox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {

    private Controller() {
        lastHitOnAPI = System.currentTimeMillis();
    }

    private long lastHitOnAPI;

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

    @RequestMapping(path="/postbox", method = RequestMethod.GET)
    public ResponseEntity<?>  postbox() {
        if (System.currentTimeMillis() - lastHitOnAPI > TimeUnit.MINUTES.toMillis(1)) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);

            lastHitOnAPI = System.currentTimeMillis();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}