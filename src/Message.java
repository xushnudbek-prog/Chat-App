import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Message {
    private User sender;
    private String text;
    private String receiverEmail;
    private LocalDateTime sentTime;

    public Message(User sender, String text, String receiverEmail, LocalDateTime sentTime) {
        this.sender = sender;
        this.text = text;
        this.receiverEmail = receiverEmail;
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return "Message: \"%s\",%nSent by: %s (%s),%nSent to %s, at: %s%n".formatted(
                text, sender.getName(), sender.getEmail(),
                receiverEmail, sentTime.format(DateTimeFormatter.ofPattern("HH:mm MM.dd.yyyy"))
        );
    }
}
