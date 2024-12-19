package work.http.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import work.adapter.TaskListAdapter;
import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.managers.Managers;
import work.managers.files.FileBackedTaskManager;
import work.managers.task.TaskManager;
import work.types.Task;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {

    protected Gson gson1  = Managers.getGson();

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod){
                case "GET":
                    if (Pattern.matches("^/tasks$", path)){
                        System.out.println(manager.getAllTasks());
                        List<Task> taskList = manager.getAllTasks();
                        String response = gson.toJson(taskList);
                        System.out.println(response);
                        sendText(exchange, response);
                        break;
                    } else if (Pattern.matches("^/tasks/\\d+$", path)){
                        int taskId = parseToId(path.replaceFirst("/tasks/", ""));
                        System.out.println(taskId);
                        if (taskId!=-1){
                            try {
                                String response = manager.getTaskById(taskId).toString();
                                System.out.println(response);
                                sendText(exchange, response);
                                break;
                            } catch (NotFoundException e) {
                                sendNotFound(exchange);
                                break;
                            }
                        } else {
                            sendNotFound(exchange);
                            break;
                        }
                    } else {
                        sendNotFound(exchange);
                        break;
                    }
                case "POST":
                    if (Pattern.matches("^/tasks$", path)){
                        String taskString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        if (!taskString.isEmpty()){
                            Task task = gson.fromJson(taskString, Task.class);
                            System.out.println(task);
                            try {
                                manager.addTask(task);
                            } catch (ManagerSaveException e) {
                                if (manager.getClass().equals(FileBackedTaskManager.class)){
                                    if (e.getMessage().equals(((FileBackedTaskManager) manager).getErrorFindFileMessage())){
                                        sendErrorWithFile(exchange, ((FileBackedTaskManager) manager).getErrorFindFileMessage());
                                    } else if (e.getMessage().equals(((FileBackedTaskManager) manager).getErrorWorkFileMessage())){
                                        sendErrorWithFile(exchange, ((FileBackedTaskManager) manager).getErrorWorkFileMessage());
                                    } else {
                                        sendHasInteractions(exchange);
                                    }
                                } else {
                                    sendHasInteractions(exchange);
                                }

                            }
                            exchange.sendResponseHeaders(201,0);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/\\d+$", path)){
                        int taskId = parseToId(path.replaceFirst("/tasks/",""));
                        if (taskId!=-1){
                            try {
                                manager.removeTaskById(taskId);
                                sendText(exchange, String.format("Задача с id %d удалена", taskId));
                            } catch (NotFoundException e) {
                                sendNotFound(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
            }
        } catch (EOFException e){
            e.printStackTrace();
        }
    }
}
