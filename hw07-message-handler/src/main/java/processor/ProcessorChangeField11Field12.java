package processor;

import model.Message;

public class ProcessorChangeField11Field12 implements Processor{
    @Override
    public Message process(Message message) {
        var field11 = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(field11).build();
    }
}
