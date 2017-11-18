package eu.canpack.fip.bo.attachment;

import eu.canpack.fip.bo.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CP S.A.
 * Created by lukasz.mochel on 02.08.2017.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long>{

    List<Attachment> findAllByDrawingIsNull();
}
