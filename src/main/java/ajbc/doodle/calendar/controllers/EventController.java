package ajbc.doodle.calendar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;

@RestController
@RequestMapping("events")
public class EventController {

	@Autowired
	private EventService eventService;
	
	@PostMapping
	public ResponseEntity<?> createEvent(Integer id, Event event){
	try {
		eventService.addEventByUser(id, event);
		event = eventService.getEventById(event.getEventId());
		return ResponseEntity.status(HttpStatus.CREATED).body(event);
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
