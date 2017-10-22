package eu.canpack.fip.repository;

import eu.canpack.fip.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Unit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {
 //   List<Unit> findUnitByNameContains(String sentence);

    List<Unit> findAllByNameContainingIgnoreCase(String sentence);
}
