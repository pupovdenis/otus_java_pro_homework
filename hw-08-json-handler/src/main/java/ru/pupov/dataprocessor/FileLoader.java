package ru.pupov.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.pupov.model.Measurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoader implements Loader {

    private final String fileName;
    private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        logger.debug("Start loading");
        logger.info("loading {} file", fileName);
        //читает файл, парсит и возвращает результат
        List<Measurement> resultList = new ArrayList<>();
        Path pathIn;
        try {
            pathIn = Paths.get(ClassLoader.getSystemResource(this.fileName).toURI());
        } catch (URISyntaxException e) {
            String report = "file " + this.fileName + " isn't exist";
            logger.error(report);
            throw new RuntimeException(report, e);
        }
        try {
            for (JsonNode node : new ObjectMapper().readTree(pathIn.toFile())) {
                String name = node.get("name").asText();
                double value = node.get("value").asDouble();
                resultList.add(new Measurement(name, value));
            }
        } catch (IOException e) {
            String report = String.format("couldn't mapping data from %s file", this.fileName);
            logger.error(report);
            throw new RuntimeException(report, e);
        }
        logger.debug("End loading");
        return resultList;
    }
}
