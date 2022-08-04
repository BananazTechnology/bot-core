package tech.bananaz.models;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="sale")
public class Sale extends Config {
	
	@Column(nullable = false, columnDefinition="TINYINT(1) UNSIGNED DEFAULT 0")
	private Boolean burnWatcher;
	@Column(nullable = false, columnDefinition="TINYINT(1) UNSIGNED DEFAULT 0")
	private Boolean mintWatcher;

}
