package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	private Integer ownerId;
	private String title;
	private String address;
	private Boolean isAllDay;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String description;

	@ManyToMany
	@JoinTable(
	  name = "user_events", 
	  joinColumns = @JoinColumn(name = "eventId"), 
	  inverseJoinColumns = @JoinColumn(name = "userId"))
	@JsonIgnore
	private List<User> guests;

	public Event(Integer ownerId, String title, String address, Boolean isAllDay, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String description, List<User> guests) {
		super();
		this.ownerId = ownerId;
		this.title = title;
		this.address = address;
		this.isAllDay = isAllDay;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.description = description;
		this.guests = guests;
	}
}
