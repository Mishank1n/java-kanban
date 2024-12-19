package work.http.handlers;

import com.google.common.base.Utf8;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import work.managers.Managers;
import work.managers.task.TaskManager;
import work.types.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class BaseHttpHandler implements HttpHandler {

    protected TaskManager manager;
    protected Gson gson;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = Managers.getGson();
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes();
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, resp.length);
        try (OutputStream outputStream = httpExchange.getResponseBody()){
            outputStream.write(resp);
        }
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        byte[] resp = "Not Found".getBytes();
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(404, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        byte[] resp = "Not Acceptable".getBytes();
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(406, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes());
    }

    protected int parseToId(String pathId){
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected void sendErrorWithFile(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes();
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(500, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }
}