package ajbc.doodle.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.NotificationService;

@RestController
@RequestMapping("notifications")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@PostMapping
	public ResponseEntity<?> createNotification(Notification notification){
	try {
		notificationService.addNotificationByEventAndUser(notification);
		notification = notificationService.getNotificationById(notification.getNotificationId());
		return ResponseEntity.status(HttpStatus.CREATED).body(notification);
	} catch (DaoException e) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
	}
}
	
	@GetMapping
	public ResponseEntity<?> getNotifications(){
		try {
			List<Notification> catList = notificationService.getAllNotifications();
			return ResponseEntity.ok(catList);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}
}
