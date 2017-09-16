package eu.canpack.fip.bo.drawing;

import eu.canpack.fip.bo.drawing.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Drawing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DrawingRepository extends JpaRepository<Drawing,Long> {

}
