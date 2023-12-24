package ru.clevertec.util.writer;


import java.util.List;

public interface WriterInPdf {

    <T> void write(T t, String filePath, String outputFilePath);

    <T> void write(List<T> data, String filePath, String outputFilePath);
}
