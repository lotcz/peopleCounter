package eu.zavadil.peopleCounter.core;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Simplifies creation of message strings.
 */
public class MessageBuilder  {

    private StringBuffer buffer = new StringBuffer();

    public void prepend(String str) {
        buffer.insert(0, str);
    }

    public void prependLine(String str) {
        this.prepend(System.getProperty("line.separator"));
        this.prepend(str);
    }

    public void append(String str) {
        buffer.append(str);
    }
    
    public void appendLine(String str) {
        this.append(str);
        this.append(System.getProperty("line.separator"));
    }

    public void append(String str, Object... args) {
        this.append(String.format(str, args));
    }

    public void appendLine(String str, Object... args) {
        this.appendLine(String.format(str, args));
    }

    public void prepend(String str, Object... args) {
        this.prepend(String.format(str, args));
    }

    public void prependLine(String str, Object... args) {
        this.prependLine(String.format(str, args));
    }
    
    public void appendValue(String name, String value) {
        append(name);
        append(": ");
        appendLine(value);
    }
    
    public void appendValue(String name, Object value) {
        appendValue(name, String.valueOf(value));
    }
    
    @Override
    public String toString() {
        return buffer.toString();
    }

    public static MessageBuilder createBuilderFromException(Exception e) {
        MessageBuilder builder = new MessageBuilder();
        builder.appendLine(e.getMessage());
        builder.appendLine(buildExceptionStackTrace(e));
        return builder;
    }

    public static String buildExceptionMessage(Exception e) {
        MessageBuilder builder = createBuilderFromException(e);
        return builder.toString();
    }

    public static String buildExceptionStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
