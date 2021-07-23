package ru.pupov;

import com.google.common.base.Strings;

import java.util.stream.IntStream;

public class HelloOtus {
    public static void main(String... args) {
        IntStream.range(0, 10)
                .forEach(it -> System.out.println(Strings.padStart(String.valueOf(it), 30, '_')));
    }
}
