package backend.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private final String content;
    private final String timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // GetterはJSONシリアライズのために必須
    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}