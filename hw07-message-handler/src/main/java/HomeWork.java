import handler.ComplexProcessor;
import listener.HistoryListener;
import model.Message;
import model.ObjectForMessage;
import processor.ProcessorChangeField11Field12;
import processor.ProcessorThrowingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        var processors = List.of(new ProcessorChangeField11Field12(),
                new ProcessorThrowingException(LocalDateTime.now()));

        var complexProcessor = new ComplexProcessor(processors, ex -> System.err.println(ex.getMessage()));
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        List<String> data = new ArrayList<>(){};
        data.add("data1");
        data.add("data2");
        data.add("data3");

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(data);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);
    }
}
