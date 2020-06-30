package eu.zavadil.prototype1.core;

/**
 * Simplifies creation of message strings.
 */
public class MessageBuilder  {
    private StringBuffer buffer = new StringBuffer();
    
    public void append(String str) {
        buffer.append(str);
    }
    
    public void appendLine(String str) {
        buffer.append(str);
        buffer.append(System.getProperty("line.separator"));
    }
    
    public void appendLine(String name, String value) {
        append(name);
        append(": ");
        appendLine(value);
    }
    
    public void appendLine(String name, Object value) {
        appendLine(name, String.valueOf(value));        
    }
    
    @Override
    public String toString() {
        return buffer.toString();
    }
    
}
