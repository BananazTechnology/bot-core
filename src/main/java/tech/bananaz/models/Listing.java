package tech.bananaz.models;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="listing")
public class Listing extends Config {}