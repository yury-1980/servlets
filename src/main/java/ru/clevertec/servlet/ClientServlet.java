package ru.clevertec.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.gson.LocalDateAdapter;
import ru.clevertec.gson.LocalDateSerializer;
import ru.clevertec.gson.OffsetDateTimeAdapter;
import ru.clevertec.gson.OffsetDateTimeSerializer;
import ru.clevertec.service.ClientService;
import ru.clevertec.service.impl.ClientServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@WebServlet("/v1/clients")
public class ClientServlet extends HttpServlet {

    private static final String ID = "id";
    private final Gson json = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
            .create();

    private final ClientService clientService = new ClientServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(ID);
        ClientDto byId = clientService.findById(Long.getLong(id, 1L));

        try (PrintWriter out = resp.getWriter()) {
            out.write(json.toJson(byId));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
