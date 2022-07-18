package tech.bananaz.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.bananaz.models.Event;

@Repository
public interface EventPagingRepository extends PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {}
