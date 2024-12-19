package work.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import work.http.handlers.TaskHandler;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.managers.task.TaskManager;
import work.status.TaskStatus;
import work.types.Task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer{
    private final static int PORT = 8080;

    private HttpServer server;
    private Gson gson;

    private static TaskManager taskManager = Managers.getDefault();

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = Managers.getDefault();
    }



    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler(this.taskManager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpServer = new HttpTaskServer(new InMemoryTaskManager());

        Task task = new Task("Test", "Testing task",
                TaskStatus.NEW, "20.10.2024 20:40","15");
        taskManager.addTask(task);
        httpServer.start();
        //httpServer.stop();
    }

    private void start(){
        System.out.println(String.format("Сервер начал работу на порту %d !", PORT));
        System.out.printf("http://localhost:%d/tasks\n", PORT);
        server.start();
    }

    private void stop(){
        System.out.println("Сервер прекратил работу!");
        server.stop(0);
    }
}
