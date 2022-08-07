package tech.bananaz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
@AllArgsConstructor
public class Field {
	
	private String data;
	
	public Field() {}

}
