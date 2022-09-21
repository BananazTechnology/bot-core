package tech.bananaz.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import tech.bananaz.models.Mint;

public interface MintConfigPagingRepository extends PagingAndSortingRepository<Mint, Long> {
	
}
