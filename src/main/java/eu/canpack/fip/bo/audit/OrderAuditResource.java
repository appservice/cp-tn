package eu.canpack.fip.bo.audit;


import eu.canpack.fip.security.AuthoritiesConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing Audit.
 */
@RestController
@RequestMapping("/api")
public class OrderAuditResource {
    private final AuditRepository auditRepository;

    public OrderAuditResource(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("audits/by-orderId/{orderId}")
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.AUDITOR})
    public List<Audit> getAuditByOrderId(@PathVariable(name = "orderId") Long orderId) {
        return auditRepository.findAllByOrderId(orderId).stream()
            .sorted(Comparator.comparing(Audit::getCreatedAt))
            .collect(Collectors.toList());

    }
}
