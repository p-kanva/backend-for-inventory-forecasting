package com.example.inventory.logs;

import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private final LogEntryRepository repo;

    public LoggingService(LogEntryRepository repo) {
        this.repo = repo;
    }

    public void log(Long userId, String eventType, String endpoint, String details, String correlationId) {
        LogEntry le = new LogEntry();
        le.setUserId(userId);
        le.setEventType(eventType);
        le.setEndpoint(endpoint);
        le.setDetails(details);
        le.setCorrelationId(correlationId);
        repo.save(le);
    }
}
