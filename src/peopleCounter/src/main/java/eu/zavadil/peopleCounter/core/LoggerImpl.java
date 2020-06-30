package eu.zavadil.peopleCounter.core;

import eu.zavadil.peopleCounter.ui.MainWindowController;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class LoggerImpl implements Logger {

    @Autowired
    MainWindowController mainWindowController;

    @Override
    public void log(LoggerLevel level, String str) {
        LoggerMessage message = new LoggerMessage(str, level);
        mainWindowController.writeToConsole(message.toString());
        System.out.println(message.toString());
    }

    @Override
    public void log(String str) {
        log(LoggerLevel.INFO, str);
    }

    @Override
    public void log(String str, Object... args) {
        log(String.format(str, args));
    }

    @Override
    public void log(LoggerLevel level, String str, Object... args) {
        log(level, String.format(str, args));
    }

}
