package de.bre.postbox;

import de.bre.postbox.actuator.TelegramActuator;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private static final Logger log = LoggerFactory.getLogger(Controller.class);

  @Resource(name = "telegramActuator")
  public TelegramActuator telegramActuator;

  @GetMapping(path = "/postbox")
  public ResponseEntity postbox() {
    log.info("API aufgerufen");
    telegramActuator.notifyUser();

    return new ResponseEntity(HttpStatus.OK);
  }
}