package tech.bananaz.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import tech.bananaz.models.Listing;

public interface ListingConfigPagingRepository extends PagingAndSortingRepository<Listing, Long> {
	
}
