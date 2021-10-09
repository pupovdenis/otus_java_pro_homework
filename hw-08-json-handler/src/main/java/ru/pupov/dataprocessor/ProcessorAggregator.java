package ru.pupov.dataprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorAggregator.class);

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        logger.debug("Start processing");
        logger.info("processing data ({} elements)", data.size());
        var result = data.stream()
                .collect(Collectors.groupingBy(Measurement::getName, Collectors.summingDouble(Measurement::getValue)));
        logger.debug("End processing");
        return result;
    }
}
