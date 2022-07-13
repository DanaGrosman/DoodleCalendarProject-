package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@ToString(exclude = { "event", "user" })

@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;

	@Column(name = "eventId", insertable = false, updatable = false)
	private Integer eventId;

	@Column(name = "userId", insertable = false, updatable = false)
	private Integer userId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "eventId")
	private Event event;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	private LocalDateTime alertTime;
	private boolean isActive;
	private boolean isAlerted;

	public Notification(Integer eventId, Integer userId, LocalDateTime alertTime) {
		super();
		this.eventId = eventId;
		this.userId = userId;
		this.alertTime = alertTime;
	}
}
