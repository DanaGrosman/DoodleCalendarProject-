package ajbc.doodle.calendar;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class NotificationThread implements Runnable {

	private Notification notification;

//	@Autowired
	private MessagePushService messagePushService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserService userService;

	public NotificationThread(Notification notification, MessagePushService messagePushService) {
		super();
		this.notification = notification;
		this.messagePushService = messagePushService;
	}
	
	@Override
	public void run() {
		
		User user;
		try {
			user = notification.getUser();
			messagePushService.sendPushMessage(user,
					messagePushService.encryptMessage(user, new PushMessage("message: ", notification.toString())));
			
		} catch ( InvalidKeyException | JsonProcessingException | NoSuchAlgorithmException
				| InvalidKeySpecException | InvalidAlgorithmParameterException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
