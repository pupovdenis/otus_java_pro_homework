package ru.pupov.dataprocessor;

import ru.pupov.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
