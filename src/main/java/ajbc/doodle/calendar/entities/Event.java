package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

	@OneToMany(targetEntity = User.class, mappedBy = "userId", fetch = FetchType.EAGER)
	private List<User> guests;

	public Event(Integer ownerId, String title, String address, Boolean isAllDay, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String description) {
		this.ownerId = ownerId;
		this.title = title;
		this.address = address;
		this.isAllDay = isAllDay;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.description = description;
	}
}
