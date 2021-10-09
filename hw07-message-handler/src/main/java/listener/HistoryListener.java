package listener;

import model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageMap;

    public HistoryListener() {
        messageMap = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        messageMap.put(msg.getId(), msg.getCopy());
        System.out.println("added to history: " + msg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageMap.get(id)).map(Message::getCopy);
    }
}
