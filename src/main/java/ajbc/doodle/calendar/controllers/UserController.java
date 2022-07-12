package ajbc.doodle.calendar.controllers;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.SubscriptionData;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import ajbc.doodle.calendar.entities.webpush.SubscriptionEndpoint;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	MessagePushService messagePushService;

	@PostMapping
	public ResponseEntity<?> createUsers(@RequestBody User user) {
//		List<User> addedUsers = new ArrayList<User>();
		try {
//			for (int i = 0; i < users.size(); i++) {
			userService.addUser(user);
			user = userService.getUserById(user.getUserId());
			return ResponseEntity.status(HttpStatus.CREATED).body(user);

//			}
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

//	@GetMapping
//	public ResponseEntity<?> getUsers() {
//		try {
//			List<User> users = userService.getAllUsers();
//			return ResponseEntity.ok(users);
//		} catch (DaoException e) {
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
//		}
//	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
		try {
			User user = userService.getUserById(id);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to find user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@GetMapping
	public ResponseEntity<?> getUserByEmailOrEventId(@RequestParam Map<String, String> map) {
		Set<String> keys = map.keySet();
		List<User> users = new ArrayList<User>();

		try {
			if (keys.contains("email"))
				users.add(userService.getUserByEmail(map.get("email")));
			else if (keys.contains("eventId"))
				users = userService.getUsersByEventId(Integer.valueOf(map.get("eventId")));
			else
				users = userService.getAllUsers();
			return ResponseEntity.status(HttpStatus.OK).body(users);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to find user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Integer id) {

		try {
			user.setUserId(id);
			userService.updateUser(id, user);
			user = userService.getUserById(user.getUserId());
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@PostMapping("/subscribe/{email}")
	@ResponseStatus(HttpStatus.CREATED)
	public void login(@RequestBody Subscription subscription, @PathVariable(required = false) String email)
			throws DaoException, InvalidKeyException, JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 1 if user exist (by email) set login flag to true
		User user = userService.getUserByEmail(email);

		if (user != null) {
			user.setIsLogged(true);

			// 2 for each user save the subscription info 3 things
			System.out.println("public key:" + subscription.getKeys().getP256dh());
			System.out.println("auth key: " + subscription.getKeys().getAuth());
			System.out.println("End Point: " + subscription.getEndpoint());

			String publicKey = subscription.getKeys().getP256dh();
			String authKey = subscription.getKeys().getAuth();
			String endPoint = subscription.getEndpoint();

			SubscriptionData subData = new SubscriptionData(publicKey, authKey, endPoint);
			user.setSubscriptionData(subData);
			userService.updateUser(user.getUserId(), user);
			user = userService.getUserById(user.getUserId());

			messagePushService.sendPushMessage(user, messagePushService.encryptMessage(user, new PushMessage("message: ", "hello")));
			
			System.out.println("Subscription added with email " + email);
		}
	}

	@PostMapping("/unsubscribe/{email}")
	public void logout(@RequestBody SubscriptionEndpoint subscription, @PathVariable(required = false) String email)
			throws DaoException {
		// 1 if user exist (by email) set login flag to false
		User user = userService.getUserByEmail(email);
		if (user != null) {
			user.setIsLogged(false);
			user.setSubscriptionData(null);
			userService.updateUser(user.getUserId(), user);
			System.out.println("Subscription with email " + email + " got removed!");
		}
	}

	@PostMapping("/isSubscribed")
	public boolean isSubscribed(@RequestBody SubscriptionEndpoint subscription) throws DaoException {
		List<User> users = userService.getAllUsers();
		for (User user : users) {
			if (user.getSubscriptionData() != null) {
				if (user.getSubscriptionData().getEndPoint().equals(subscription.getEndpoint()))
					return true;
			}
		}
		return false;
	}
}
