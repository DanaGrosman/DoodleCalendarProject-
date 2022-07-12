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

	private Unit unit;
	private int quantity;
	private LocalDateTime localDateTime;
	private boolean isActive;
	private boolean isAlerted;

	public Notification(Integer eventId, Integer userId, Unit unit, int quantity, LocalDateTime localDateTime) {
		super();
		this.eventId = eventId;
		this.userId = userId;
		this.unit = unit;
		this.quantity = quantity;
		this.localDateTime = localDateTime;
	}

	public LocalDateTime computeAlertTime() {
		return (unit == Unit.HOURS) ? this.localDateTime.minusHours(quantity) : this.localDateTime.minusMinutes(quantity);
	}

//	
//	
//	public Notification(Unit unit, int quantity, LocalDateTime localDateTime) {
//		super();
//		this.unit = unit;
//		this.quantity = quantity;
//		this.localDateTime = localDateTime;
//	}
//
//	public Notification(Integer notificationId, Unit unit, int quantity,
//			LocalDateTime localDateTime) {
//		super();
//		this.notificationId = notificationId;
//		this.unit = unit;
//		this.quantity = quantity;
//		this.localDateTime = localDateTime;
//	}
}
