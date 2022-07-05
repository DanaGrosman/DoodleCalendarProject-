package ajbc.doodle.calendar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<?> createUser(User user){
	try {
		userService.addUser(user);
		user = userService.getUserById(user.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	} catch (DaoException e) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
	}
}
	
//	@GetMapping
//	public ResponseEntity<?> getCategories(){
//		try {
//			List<Category> catList = catService.getAllCategory();
//			return ResponseEntity.ok(catList);
//		} catch (DaoException e) {
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
//		}
//	}
}
