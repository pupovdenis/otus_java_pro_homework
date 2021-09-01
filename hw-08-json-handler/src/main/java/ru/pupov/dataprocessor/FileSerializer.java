package ru.pupov.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {

    private final String fileName;
    private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        logger.debug("Start serializing");
        logger.info("serializing data ({} elements)", data.size());
        //формирует результирующий json и сохраняет его в файл
        var file = new File(this.fileName);
        var mapper = new ObjectMapper();
        var sortedMap = data.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        try {
            mapper.writeValue(file,sortedMap);
        } catch (IOException e) {
            String report = String.format("couldn't create file %s", this.fileName);
            logger.error(report);
            throw new RuntimeException(report, e);
        }
        logger.debug("End serializing");
    }
}
