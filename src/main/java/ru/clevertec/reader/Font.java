package ru.clevertec.reader;

public enum Font {

    ANTQUAB("src/main/resources/font/ANTQUAB.TTF"),
    ANTQUABI("src/main/resources/font/ANTQUABI.TTF"),
    ANTQUAI("src/main/resources/font/ANTQUAI.TTF"),
    BKANT("src/main/resources/font/BKANT.TTF");

    private final String path;

    Font(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
