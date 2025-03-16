package hu.okrim.productreviewappcomplete.audit;

import java.time.Instant;

public record AuditEvent(
        String action,
        String tableName,
        String description,
        String userName,
        Instant timestamp
) {
}
