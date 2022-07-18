package tech.bananaz.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import tech.bananaz.models.Sale;

public interface SaleConfigPagingRepository extends PagingAndSortingRepository<Sale, Long> {
	
}
