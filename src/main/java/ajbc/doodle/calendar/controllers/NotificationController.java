package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.NotificationManager;
import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;

@RestController
@RequestMapping("notifications")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	MessagePushService messagePushService;

	@Autowired
	NotificationManager notificationManager;

	@PostMapping("/{userId}/{eventId}")
	public ResponseEntity<?> addNotification(@RequestBody Notification notification, @PathVariable Integer userId,
			@PathVariable Integer eventId) {
		try {
			notification.setEventId(eventId);
			notification.setUserId(userId);
			notificationService.addNotification(notification);
			notification = notificationService.getNotificationById(notification.getNotificationId());
			notificationManager.addNotification(notification);
			return ResponseEntity.status(HttpStatus.CREATED).body(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add notification to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@PostMapping("/{userId}/{eventId}/addList")
	public ResponseEntity<?> addNotifications(@RequestBody List<Notification> notifications,
			@PathVariable Integer userId, @PathVariable Integer eventId) {
		try {
			List<Notification> addedNotifications = new ArrayList<Notification>();

			for (int i = 0; i < notifications.size(); i++) {
				notifications.get(i).setEventId(eventId);
				notifications.get(i).setUserId(userId);
				notificationService.addNotification(notifications.get(i));

				Notification notification = notificationService
						.getNotificationById(notifications.get(i).getNotificationId());
				addedNotifications.add(notification);
				notificationManager.addNotification(notification);
			}

			return ResponseEntity.status(HttpStatus.CREATED).body(addedNotifications);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add notification to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@GetMapping
	public ResponseEntity<?> getNotifications(@RequestParam Map<String, String> map) {
		Set<String> keys = map.keySet();
		List<Notification> notifications = new ArrayList<Notification>();

		try {
			if (keys.contains("userId"))
				notifications = notificationService.getNotificationByUserId(Integer.valueOf(map.get("userId")));
			else if (keys.contains("eventId"))
				notifications = notificationService.getNotificationByEventId(Integer.valueOf(map.get("eventId")));
			else
				notifications = notificationService.getAllNotifications();

			return ResponseEntity.ok(notifications);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getNotificationById(@PathVariable Integer id) {
		try {
			Notification notification = notificationService.getNotificationById(id);
			return ResponseEntity.status(HttpStatus.OK).body(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to find notification in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "{id}")
	public ResponseEntity<?> updateNotification(@RequestBody Notification notification, @PathVariable Integer id) {
		try {
			if (notification.getAlertTime().isAfter(LocalDateTime.now()))
				notification.setAlerted(false);
			notification.setNotificationId(id);
			notificationService.updateNotification(notification);
			notification = notificationService.getNotificationById(notification.getNotificationId());

			return ResponseEntity.status(HttpStatus.OK).body(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateList")
	public ResponseEntity<?> updateListNotifications(@RequestBody List<Notification> notifications) {
		try {
			List<Notification> addedNotifications = new ArrayList<Notification>();

			for (int i = 0; i < notifications.size(); i++) {
				if (notifications.get(i).getAlertTime().isAfter(LocalDateTime.now()))
					notifications.get(i).setAlerted(false);

				notificationService.updateNotification(notifications.get(i));
				Notification notification = notificationService
						.getNotificationById(notifications.get(i).getNotificationId());
				addedNotifications.add(notification);
			}

			return ResponseEntity.status(HttpStatus.OK).body(addedNotifications);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/softDelete")
	public ResponseEntity<?> softDeleteNotifications(@RequestBody List<Integer> notificationIds) {

		try {
			List<Notification> deletedNotifications = new ArrayList<Notification>();
			for (int i = 0; i < notificationIds.size(); i++) {
				Notification notification = notificationService.softDeleteNotification(notificationIds.get(i));
				deletedNotifications.add(notification);
			}

			return ResponseEntity.status(HttpStatus.OK).body(deletedNotifications);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/hardDelete")
	public ResponseEntity<?> hardDeleteNotifications(@RequestBody List<Integer> notificationIds) {

		try {
			List<Notification> deletedNotifications = new ArrayList<Notification>();
			for (int i = 0; i < notificationIds.size(); i++) {
				Notification notification = notificationService.hardDeleteNotification(notificationIds.get(i));
				deletedNotifications.add(notification);
			}

			return ResponseEntity.status(HttpStatus.OK).body(deletedNotifications);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
	public byte[] publicSigningKey() {
		return messagePushService.getServerKeys().getPublicKeyUncompressed();
	}

	@GetMapping(path = "/publicSigningKeyBase64")
	public String publicSigningKeyBase64() {
		return messagePushService.getServerKeys().getPublicKeyBase64();
	}
}
