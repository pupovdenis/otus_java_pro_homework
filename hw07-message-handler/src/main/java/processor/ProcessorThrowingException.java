package processor;

import listener.DateTimeProvider;
import listener.DateTimeProviderImpl;
import model.Message;

import java.time.LocalDateTime;

public class ProcessorThrowingException implements Processor{
    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowingException(LocalDateTime localDateTime) {
        this.dateTimeProvider = new DateTimeProviderImpl(localDateTime);
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 != 0) throw new RuntimeException("Четная секунда!");
        return message;
    }
}
