package ajbc.doodle.calendar.controllers;

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

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;

@RestController
@RequestMapping("events")
public class EventController {

	@Autowired
	private EventService eventService;

	@PostMapping
	public ResponseEntity<?> createEvent(Integer id, Event event) {
		try {
			eventService.addEventByUser(id, event);
			event = eventService.getEventById(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getEvents(@RequestParam Map<String, String> map) {
		Set<String> keys = map.keySet();
		List<Event> events = new ArrayList<Event>();

		try {
			if (keys.contains("userId"))
				events = eventService.getEventsByUserId(Integer.valueOf(map.get("userId")));
			else
				events = eventService.getAllEvents();
			return ResponseEntity.ok(events);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateEvent(@RequestBody Event event, @PathVariable Integer id) {
		try {
			event.setEventId(id);
			eventService.updateEvent(event.getOwnerId(), event);
			event = eventService.getEventById(event.getEventId());
			return ResponseEntity.status(HttpStatus.OK).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update event in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
}
