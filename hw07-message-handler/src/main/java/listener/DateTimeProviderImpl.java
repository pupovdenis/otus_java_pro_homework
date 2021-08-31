package listener;

import java.time.LocalDateTime;

public class DateTimeProviderImpl implements DateTimeProvider {
    private final LocalDateTime localDateTime;

    public DateTimeProviderImpl(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime getDate() {
        return this.localDateTime;
    }
}
