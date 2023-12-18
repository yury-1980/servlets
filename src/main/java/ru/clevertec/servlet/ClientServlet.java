package ru.clevertec.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.gson.LocalDateAdapter;
import ru.clevertec.gson.LocalDateSerializer;
import ru.clevertec.gson.OffsetDateTimeAdapter;
import ru.clevertec.gson.OffsetDateTimeSerializer;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.service.impl.ClientServiceImpl;
import ru.clevertec.valid.Validator;
import ru.clevertec.valid.impl.ValidatorImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@WebServlet("/v1/clients/*")
public class ClientServlet extends HttpServlet {

    private static final String ID = "id";
    private static final String NUM_PAGE = "numPage";
    private static final String PAGE_SIZE = "pageSize";
    private final Validator validator = new ValidatorImpl();
    private final MapperClient mapperClient = new MapperClientImpl();
    private final ClientDao clientDao = new ClientDaoImpl(new ConnectionPoolManager());
    private final ClientService clientService = new ClientServiceImpl(clientDao, mapperClient, validator);
    private final Gson json = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        Long numPage = Long.parseLong(req.getParameter(NUM_PAGE));
        Long pageSize = Long.parseLong(req.getParameter(PAGE_SIZE));

        if (id != null) {
            ClientDto byId = clientService.findById(Long.parseLong(id));

            try (PrintWriter respWriter = resp.getWriter()) {
                respWriter.write(json.toJson(byId));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            List<ClientDto> serviceByAll = clientService.findByAll(numPage, pageSize);

            try (PrintWriter out = resp.getWriter()) {
                out.write(json.toJson(serviceByAll));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        ClientDto clientDto = json.fromJson(reader, ClientDto.class);
        Client newClient = clientService.create(clientDto);

        try (PrintWriter respWriter = resp.getWriter()) {
            respWriter.write(json.toJson(newClient));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter(ID));
        BufferedReader reader = req.getReader();
        ClientDto clientDto = json.fromJson(reader, ClientDto.class);
        Client newClient = clientService.update(id, clientDto);

        try (PrintWriter respWriter = resp.getWriter()) {
            respWriter.write(json.toJson(newClient));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter(ID));
        clientService.delete(id);

        try (PrintWriter respWriter = resp.getWriter()) {
            respWriter.write("Удаление успешно!");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
