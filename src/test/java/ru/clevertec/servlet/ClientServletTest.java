package ru.clevertec.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;
    private ClientServlet clientServlet;
    private PrintWriter printWriter;
    String ID = "id";

    @BeforeEach
    void setUp() {
        clientServlet = new ClientServlet();
        StringWriter stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void shouldReturnStatusDoGet() throws IOException {
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
    void shouldReturnStatusUpdatedoPut() throws IOException {
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
                .setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}