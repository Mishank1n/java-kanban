package work.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import work.http.handlers.*;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int port = 8080;
    private final HttpServer server;
    private final Gson gson;
    private InMemoryTaskManager taskManager = Managers.getDefault();

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        server.createContext("/subtasks", new SubTaskHandler(taskManager));
    }

    public HttpServer getServer() {
        return server;
    }

    public Gson getGson() {
        return gson;
    }

    public InMemoryTaskManager getTaskManager() {
        return taskManager;
    }


    public void start() {
        System.out.printf("Сервер начал работу на порту %d !", port);
        System.out.printf("http://localhost:%d/tasks\n", port);
        server.start();
    }

    public void stop() {
        System.out.println("Сервер прекратил работу!");
        server.stop(0);
    }
}
