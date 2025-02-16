package hu.okrim.productreviewappcomplete.util;

import hu.okrim.productreviewappcomplete.model.Log;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditListener {

    private static EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        AuditListener.entityManager = entityManager;
    }

    @PostPersist
    public void afterCreate(Object entity) {
        logAction("INSERT", entity);
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
        logAction("UPDATE", entity);
    }

    @PreRemove
    public void beforeDelete(Object entity) {
        logAction("DELETE", entity);
    }

    private void logAction(String action, Object entity) {
        if (entityManager == null) {
            System.out.println("EntityManager is null! Audit logging won't work.");
            return;
        }

        Log log = new Log();
        log.setDate(Instant.now());
        log.setUserName(getCurrentUser());
        log.setDmlType(action);
        log.setTableName(entity.getClass().getSimpleName());
        log.setDescription(entity.toString());

        entityManager.persist(log);
    }

    private String getCurrentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
            return principal.toString();
        } catch (Exception e) {
            return "UNKNOWN_USER";
        }
    }
}