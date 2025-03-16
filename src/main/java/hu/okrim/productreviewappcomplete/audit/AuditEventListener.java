package hu.okrim.productreviewappcomplete.audit;

import hu.okrim.productreviewappcomplete.model.Log;
import hu.okrim.productreviewappcomplete.repository.LogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AuditEventListener {

    private final LogRepository logRepository;

    public AuditEventListener(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Async
    @EventListener
    public void handleAuditEvent(AuditEvent event) {
        Log log = new Log();
        log.setDate(event.timestamp());
        log.setUserName(event.userName());
        log.setDmlType(event.action());
        log.setTableName(event.tableName());
        log.setDescription(event.description());

        logRepository.save(log);
    }
}
