package de.bre.postbox;

import de.bre.postbox.actuator.TelegramActuator;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @Resource(name = "telegramActuator")
  public TelegramActuator telegramActuator;

  /**
   * when called, sends mail or telegram message to desired adress. To prevent spam/errors it does
   * this only every minute.
   *
   * @return http 200
   */
  @RequestMapping(path = "/postbox", method = RequestMethod.GET)
  public ResponseEntity<?> postbox() {
    log.info("API aufgerufen");
    if (System.currentTimeMillis() - lastHitOnApi > TimeUnit.MINUTES.toMillis(1)) {
      try {
        telegramActuator.notifyUser();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      log.info("Benachrichtigung versendet mit " + telegramActuator.toString());
      lastHitOnApi = System.currentTimeMillis();
    }
    return new ResponseEntity(HttpStatus.OK);
  }
}