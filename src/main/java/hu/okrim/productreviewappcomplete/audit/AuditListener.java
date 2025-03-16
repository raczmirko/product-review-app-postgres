package hu.okrim.productreviewappcomplete.audit;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditListener {

    private static ApplicationEventPublisher eventPublisher;

    public AuditListener(ApplicationEventPublisher eventPublisher) {
        AuditListener.eventPublisher = eventPublisher;
    }

    @PostPersist
    public void afterCreate(Object entity) {
        publishEvent("INSERT", entity);
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
        publishEvent("UPDATE", entity);
    }

    @PreRemove
    public void beforeDelete(Object entity) {
        publishEvent("DELETE", entity);
    }

    private void publishEvent(String action, Object entity) {
        String userName = getCurrentUser();
        AuditEvent auditEvent = new AuditEvent(
                action,
                entity.getClass().getSimpleName(),
                entity.toString(),
                userName,
                Instant.now()
        );

        eventPublisher.publishEvent(auditEvent);
    }

    private String getCurrentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
            return principal.toString();
        } catch (Exception e) {
            return "UNKNOWN_USER";
        }
    }
}
