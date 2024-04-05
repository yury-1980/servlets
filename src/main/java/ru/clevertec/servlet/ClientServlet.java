package ru.clevertec.servlet;

import com.google.gson.Gson;
import ru.clevertec.config.Config;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.service.ClientService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/v1/clients")
public class ClientServlet extends HttpServlet {

    private static final String ID = "id";
    private static final String PAGE_NUM = "pageNum";
    private static final String PAGE_SIZE = "pageSize";
    private final Gson json = Config.getConfig()
            .getJson();

    private final ClientService clientService = Config.getConfig()
            .getClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        String pageNum = req.getParameter(PAGE_NUM);
        String pageSize = req.getParameter(PAGE_SIZE);

        try (PrintWriter respWriter = resp.getWriter()) {

            if (id != null) {
                ClientDto clientDto = clientService.findById(Long.parseLong(id));

                respWriter.write(json.toJson(clientDto));
                resp.setStatus(HttpServletResponse.SC_OK);

            } else if (pageNum != null && pageSize != null) {
                List<ClientDto> serviceByAll = clientService.findByAll(Long.parseLong(pageNum), Long.parseLong(pageSize));

                respWriter.write(json.toJson(serviceByAll));
                resp.setStatus(HttpServletResponse.SC_OK);

            } else {
                respWriter.write(json.toJson("Ресурс не найден."));
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
            resp.setStatus(HttpServletResponse.SC_CREATED);
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
