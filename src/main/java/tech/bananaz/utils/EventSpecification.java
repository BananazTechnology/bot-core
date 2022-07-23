package tech.bananaz.utils;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import lombok.Getter;
import tech.bananaz.enums.Operation;
import tech.bananaz.models.Event;

@SuppressWarnings("serial")
public class EventSpecification implements Specification<Event> {
	
	@Getter
    private List<EventSearchCriteria> searchCriteria = new ArrayList<>();

    public void addSearchCriteria(EventSearchCriteria eventSearchCriteria) {
        this.searchCriteria.add(eventSearchCriteria);
    }

	@Override
	public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
        for (EventSearchCriteria criteria : this.searchCriteria) {
        	// a > b
        	if(criteria.getOperation().equals(Operation.GREATER_THAN))
        		predicates.add(criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
        	// a < b
        	if(criteria.getOperation().equals(Operation.LESS_THAN))
        		predicates.add(criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
        	// a >= b
        	if(criteria.getOperation().equals(Operation.GREATER_THAN_EQUAL))
        		predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
        	// a <= b
        	if(criteria.getOperation().equals(Operation.LESS_THAN_EQUAL))
        		predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
        	// a = b
        	if(criteria.getOperation().equals(Operation.EQUAL))
        		predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
        }
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

}
