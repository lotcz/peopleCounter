package eu.zavadil.peopleCounter.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoggerMessage {

    private final String message;

    private final LoggerLevel level;

    @Override
    public String toString() {
        return String.format("%s: %s", level.toString(), message);
    }
}
