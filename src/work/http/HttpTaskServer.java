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

    /*public static void main(String[] args) throws IOException, NotFoundException {
        HttpTaskServer httpServer = new HttpTaskServer(new InMemoryTaskManager());

        Task task = new Task("Test", "Testing task",
                TaskStatus.NEW, "20.10.2024 20:40","15");
        Epic epic = new Epic("Test CheckDeleteFromEpic", "Test CheckDeleteFromEpic description");
        httpServer.getTaskManager().addEpic(epic);
        SubTask firstSubTask = new SubTask("Test CheckDeleteFromEpic", "Test CheckDeleteFromEpic description", TaskStatus.NEW,"19.10.2024 20:40" , "20", epic.getId());
        SubTask secondSubTask = new SubTask("Test CheckDeleteFromEpic 2", "Test CheckDeleteFromEpic description 2", TaskStatus.IN_PROGRESS, epic.getId());
        httpServer.getTaskManager().addSubTask(firstSubTask);
        httpServer.getTaskManager().addSubTask(secondSubTask);
        httpServer.getTaskManager().addTask(task);
        httpServer.getTaskManager().getTaskById(task.getId());
        httpServer.getTaskManager().getSubTaskById(firstSubTask.getId());
        String res = httpServer.gson.toJson(firstSubTask);
        httpServer.start();
        httpServer.stop();
    }*/

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
