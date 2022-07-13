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

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;

@RestController
@RequestMapping("events")
public class EventController {

	private static final String MINUTES = "minutes";
	private static final String HOURS = "hours";
	private static final String FAILED_TO_FIND = "failed to find event in db";
	private static final String FAILED_TO_UPDATE = "failed to update event in db";
	private static final String USER_ID = "userId";
	private static final String END = "end";
	private static final String START = "start";

	@Autowired
	private EventService eventService;

	@PostMapping("/{userId}")
	public ResponseEntity<?> createEvent(@RequestBody Event event, @PathVariable Integer userId) {
		try {
			eventService.addEventByUser(userId, event);
			event = eventService.getEventById(event.getEventId());
//			event.setGuests(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@PostMapping("/addList")
	public ResponseEntity<?> addListOfEvents(@RequestBody List<Event> events) {
		try {
			List<Event> addedEvents = new ArrayList<Event>();

			for (int i = 0; i < events.size(); i++) {
				eventService.addEventByUser(events.get(i).getOwnerId(), events.get(i));
				Event event = eventService.getEventById(events.get(i).getEventId());
				event.setGuests(null);
				addedEvents.add(event);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(addedEvents);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getEvents(@RequestParam Map<String, String> map) {
		Set<String> keys = map.keySet();
		List<Event> events = new ArrayList<Event>();

		try {
			if (keys.contains(START) && keys.contains(END) && keys.contains(USER_ID)) {
				LocalDateTime start = LocalDateTime.parse(map.get(START));
				LocalDateTime end = LocalDateTime.parse(map.get(END));
				events = eventService.getEventsByRangeAndUserId(Integer.valueOf(map.get(USER_ID)), start, end);

			} else if (keys.contains(USER_ID) && keys.contains(HOURS)) {
				events = eventService.getEventsByRangeAndUserId(Integer.valueOf(map.get(USER_ID)), LocalDateTime.now(),
						LocalDateTime.now().plusHours(Integer.valueOf(map.get(HOURS))));
			}

			else if (keys.contains(USER_ID) && keys.contains(MINUTES)) {
				events = eventService.getEventsByRangeAndUserId(Integer.valueOf(map.get(USER_ID)), LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(Integer.valueOf(map.get(MINUTES))));

			} else if (keys.contains(USER_ID))
				events = eventService.getEventsByUserId(Integer.valueOf(map.get(USER_ID)));

			else if (keys.contains(START) && keys.contains(END)) {
				LocalDateTime start = LocalDateTime.parse(map.get(START));
				LocalDateTime end = LocalDateTime.parse(map.get(END));
				events = eventService.getEventsByRange(start, end);
			}

			else
				events = eventService.getAllEvents();

			for (int i = 0; i < events.size(); i++) {
				events.get(i).setGuests(null);
			}

			return ResponseEntity.ok(events);
		} catch (

		DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}

	@GetMapping("/{userId}/upcomingevents")
	public ResponseEntity<?> getUpcomingEventsByUserId(@PathVariable Integer userId) {
		try {
			List<Event> events = eventService.getUpcomingEventsByUserId(userId);
			return ResponseEntity.status(HttpStatus.OK).body(events);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage(FAILED_TO_FIND);
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {
		try {
			Event event = eventService.getEventById(id);
			event.setGuests(null);
			return ResponseEntity.status(HttpStatus.OK).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage(FAILED_TO_FIND);
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
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
			errorMessage.setMessage(FAILED_TO_UPDATE);
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updateList")
	public ResponseEntity<?> updateListOfEvents(@RequestBody List<Event> events) {
		try {
			List<Event> addedEvents = new ArrayList<Event>();

			for (int i = 0; i < events.size(); i++) {
//				events.get(i).setEventId(id);
				eventService.updateEvent(events.get(i).getOwnerId(), events.get(i));
				Event event = eventService.getEventById(events.get(i).getEventId());
				addedEvents.add(event);
			}
			return ResponseEntity.status(HttpStatus.OK).body(addedEvents);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage(FAILED_TO_UPDATE);
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/softDelete")
	public ResponseEntity<?> softDeleteEvents(@RequestBody List<Integer> eventsIds) {

		try {
			List<Event> deletedEvents = new ArrayList<Event>();
			for (int i = 0; i < eventsIds.size(); i++) {
				Event event = eventService.softDeleteEvent(eventsIds.get(i));
				deletedEvents.add(event);
			}

			return ResponseEntity.status(HttpStatus.OK).body(deletedEvents);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to soft delete event in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/hardDelete")
	public ResponseEntity<?> hardDeleteEvents(@RequestBody List<Integer> eventsIds) {

		try {
			List<Event> deletedEvents = new ArrayList<Event>();
			for (int i = 0; i < eventsIds.size(); i++) {
				Event event = eventService.hardDeleteEvent(eventsIds.get(i));
				deletedEvents.add(event);
			}

			return ResponseEntity.status(HttpStatus.OK).body(deletedEvents);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to delete event in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
}
