package ru.clevertec.writer.impl;

import com.google.gson.Gson;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.reader.Font;
import ru.clevertec.writer.WriterInPdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Slf4j
@AllArgsConstructor
public class WriterInPdfImpl implements WriterInPdf {

    private Gson gson;
    private final LocalDateTime dateTime = LocalDateTime.now();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String date;

    /**
     * Запись текста в pdf файл.
     *
     * @param str            Входящий параметр - строка.
     * @param filePath       Путь к файлу, для получения подложки.
     * @param outputFilePath Путь к создаваемому файлу pdf.
     */
    @Override
    public void write(String str, String filePath, String outputFilePath) {

        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(filePath),
                new PdfWriter(outputFilePath))) {
            Document document = docPdf(pdfDocument);
            document.add(new Paragraph(str));
            document.add(new Paragraph(date).setTextAlignment(TextAlignment.RIGHT));

            log.info("PDF-документ успешно изменен.");
        } catch (IOException e) {
            log.error("Ошибка при изменении PDF-документа: " + e.getMessage());
        }
    }

    /**
     * Запись одного объекта в pdf файл.
     *
     * @param t              Входящий объект с обобщённым типом.
     * @param filePath       Путь к файлу, для получения подложки.
     * @param outputFilePath Путь к создаваемому файлу pdf.
     * @param <T>            Обобщённый тип данных.
     */
    @Override
    public <T> void write(T t, String filePath, String outputFilePath) {

        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(filePath),
                new PdfWriter(outputFilePath))) {
            String json = gson.toJson(t);

            Document document = docPdf(pdfDocument);
            document.add(new Paragraph(json));
            document.add(new Paragraph(date).setTextAlignment(TextAlignment.RIGHT));

            log.info("PDF-документ успешно изменен.");
        } catch (IOException e) {
            log.error("Ошибка при изменении PDF-документа: " + e.getMessage());
        }
    }

    /**
     * Запись списка объектов в pdf файл.
     *
     * @param data           Входящий List<T> с обобщённым параметром.
     * @param filePath       Путь к файлу, для получения подложки.
     * @param outputFilePath Путь к создаваемому файлу pdf.
     * @param <T>            Обобщённый тип данных.
     */
    @Override
    public <T> void write(java.util.List<T> data, String filePath, String outputFilePath) {

        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(filePath),
                new PdfWriter(outputFilePath))) {
            Document document = docPdf(pdfDocument);

            List list = new List();

            data.stream()
                    .map(o -> gson.toJson(o))
                    .forEach(list::add);
            document.add(list);

            PdfFormXObject backgroundXObject = pdfDocument.getFirstPage().copyAsFormXObject(pdfDocument);

            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
                PdfPage page = pdfDocument.getPage(i);
                PdfStream stream = page.newContentStreamBefore();
                new PdfCanvas(stream, page.getResources(), pdfDocument).addXObjectAt(backgroundXObject, 0, 0);
            }
            document.add(new Paragraph(date).setTextAlignment(TextAlignment.RIGHT));

            log.info("PDF-документ успешно изменен.");
        } catch (IOException e) {
            log.error("Ошибка при изменении PDF-документа: " + e.getMessage());
        }
    }

    /**
     * Обеспечивает настройку документа: размер страниц и настройку шрифта.
     *
     * @param pdfDocument Документ.
     * @return Настроенный документ.
     * @throws IOException Если не удалось получить шрифт.
     */
    private Document docPdf(PdfDocument pdfDocument) throws IOException {
        date = dateTime.format(FORMATTER);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        PdfFont font = PdfFontFactory.createFont(Font.BKANT.getPath());
        Document document = new Document(pdfDocument);

        document.setFont(font)
                .setFontColor(ColorConstants.BLUE)
                .setTopMargin(200);

        return document;
    }
}
