package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientsApiServlet extends HttpServlet {
    private static final int ID_PATH_PARAM_POSITION = 1;
    private final DbServiceClientImpl dbServiceClient;

    public ClientsApiServlet(DbServiceClientImpl dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    private class Cli {
        String clientName;
        String street;
        ArrayList<String> phones;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletInputStream in = req.getInputStream();
        String ss = new String(in.readAllBytes());
        Gson gson = new Gson();
        Cli cli = gson.fromJson(ss, Cli.class);
        ArrayList<Phone> PhoneList = new ArrayList<>();
        for (String sss : cli.phones){
            PhoneList.add(new Phone(null, sss));
        }
        Client client = new Client(null, cli.clientName, new Address(null, cli.street), PhoneList);
        dbServiceClient.saveClient(client);
        resp.setStatus(201);
    }

}
