package ru.clevertec.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.service.ClientService;
import ru.clevertec.util.gson.LocalDateAdapter;
import ru.clevertec.util.gson.LocalDateSerializer;
import ru.clevertec.util.gson.OffsetDateTimeAdapter;
import ru.clevertec.util.gson.OffsetDateTimeSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    ClientService clientService;

    @Mock
    private HttpServletResponse mockResponse;
    private ClientServlet clientServlet;
    private PrintWriter printWriter;
    private final String ID = "id";

    @BeforeEach
    void setUp() {
        Gson json = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
                .create();
        clientServlet = new ClientServlet(json, clientService);
        StringWriter stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void shouldReturnStatusOkDoGet() throws IOException {
        // given
        when(mockRequest.getParameter(ID))
                .thenReturn("3");
        when(mockResponse.getWriter())
                .thenReturn(printWriter);

        // when
        clientServlet.doGet(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldReturnStatusOkDoGetAll() throws IOException {
        // given
        String pageNum = "pageNum";
        String pageSize = "pageSize";

        when(mockRequest.getParameter(ID))
                .thenReturn(null);
        when(mockRequest.getParameter(pageNum))
                .thenReturn("1");
        when(mockRequest.getParameter(pageSize))
                .thenReturn("1");
        when(mockResponse.getWriter())
                .thenReturn(printWriter);

        // when
        clientServlet.doGet(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldReturnStatusNotFoundDoGet() throws IOException {
        // given
        String pageNum = "pageNum";
        String pageSize = "pageSize";

        when(mockRequest.getParameter(ID))
                .thenReturn(null);
        when(mockRequest.getParameter(pageNum))
                .thenReturn(null);
        when(mockRequest.getParameter(pageSize))
                .thenReturn(null);
        when(mockResponse.getWriter())
                .thenReturn(printWriter);

        // when
        clientServlet.doGet(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void shouldReturnStatusCreateDoPost() throws IOException {
        // given
        String str = "{\"clientName\":\"Пётр\",\"familyName\":\"Петров\",\"surName\":\"Петрович\",\"birthDay\":\"2000-01-01\"}";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes())));

        when(mockResponse.getWriter())
                .thenReturn(printWriter);
        when(mockRequest.getReader())
                .thenReturn(reader);
        // when
        clientServlet.doPost(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void shouldReturnStatusUpdateDoPut() throws IOException {
        // given
        String str = "{\"clientName\":\"Пётр\",\"familyName\":\"Петров\",\"surName\":\"Петрович\",\"birthDay\":\"2000-01-01\"}";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes())));

        when(mockRequest.getParameter(ID))
                .thenReturn("3");
        when(mockResponse.getWriter())
                .thenReturn(printWriter);
        when(mockRequest.getReader())
                .thenReturn(reader);
        // when
        clientServlet.doPut(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldReturnStatusDoDelete() throws IOException {
        // given
        when(mockRequest.getParameter(ID))
                .thenReturn("1");
        when(mockResponse.getWriter())
                .thenReturn(printWriter);

        // when()
        clientServlet.doDelete(mockRequest, mockResponse);

        // then
        verify(mockResponse)
                .setStatus(HttpServletResponse.SC_OK);
    }
}