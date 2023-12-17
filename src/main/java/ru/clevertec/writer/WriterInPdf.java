package ru.clevertec.writer;


import java.util.List;

public interface WriterInPdf {

    void write(String str, String filePath, String outputFilePath);

    <T> void write(T t, String filePath, String outputFilePath);

    <T> void write(List<T> data, String filePath, String outputFilePath);
}
