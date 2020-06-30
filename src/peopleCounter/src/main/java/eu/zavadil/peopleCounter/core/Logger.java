package eu.zavadil.peopleCounter.core;

import eu.zavadil.peopleCounter.core.LoggerLevel;

public interface Logger {

    void log(String str);

    void log(LoggerLevel level, String str);

    void log(String str, Object... args);

    void log(LoggerLevel level, String str, Object... args);

}
