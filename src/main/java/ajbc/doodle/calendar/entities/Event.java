package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "guests")

@JsonInclude(Include.NON_NULL)
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

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany
	@JoinTable(name = "user_events", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> guests;

	@OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
	private Set<Notification> notifications;

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
