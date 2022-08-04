package tech.bananaz.repositories;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.bananaz.enums.EventType;
import tech.bananaz.models.Event;

@Repository
public interface EventPagingRepository extends PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {
	
	Event	    findById(long id);
	boolean 	existsByIdAndConsumedFalse(long id);
	List<Event> findByConfigIdAndConsumedFalseAndEventTypeOrderByCreatedDateAsc(long configId, EventType eventType);
	
	@Transactional
	@Modifying
	@Query("UPDATE Event e SET e.consumed = 1, e.consumedBy = ?2 WHERE e.id = ?1 AND e.consumed = 0")
	int		    updateByIdSetConsumedTrueAndConsumedBy(long id, String consumedBy);
	
}
