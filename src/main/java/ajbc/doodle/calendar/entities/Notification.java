package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;
	private Integer userId;
	private Integer eventId;
	private Unit unit;
	private int quantity;
	private LocalDateTime localDateTime;

	public Notification(Integer userId, Integer eventId, Unit unit, int quantity, LocalDateTime localDateTime) {
		super();
		this.userId = userId;
		this.eventId = eventId;
		this.unit = unit;
		this.quantity = quantity;
		this.localDateTime = localDateTime;
	}

}
