package work.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.managers.files.FileBackedTaskManager;
import work.managers.task.InMemoryTaskManager;
import work.types.SubTask;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubTaskHandler extends BaseHttpHandler {
    public SubTaskHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/subtasks$", path)) {
                        List<SubTask> allSubTasks = manager.getAllSubTasks();
                        String response = gson.toJson(allSubTasks);
                        sendText(exchange, response);
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        int taskId = parseToId(path.replaceFirst("/subtasks/", ""));
                        if (taskId != -1) {
                            try {
                                String response = gson.toJson(manager.getSubTaskById(taskId));
                                sendText(exchange, response);
                            } catch (NotFoundException e) {
                                sendNotFound(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                case "POST":
                    if (Pattern.matches("^/subtasks$", path)) {
                        String taskString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        if (!taskString.isEmpty()) {
                            SubTask subTask = gson.fromJson(taskString, SubTask.class);
                            subTask.setEndTime();
                            try {
                                if (manager.getSubTasks().containsKey(subTask.getId())) {
                                    manager.updateSubTask(subTask);
                                    sendText(exchange, String.format("Обновили задачу с id - %d", subTask.getId()));
                                } else {
                                    manager.addSubTask(subTask);
                                    sendText(exchange, String.format("Добавили задачу с id - %d", subTask.getId()));
                                }
                            } catch (ManagerSaveException e) {
                                if (manager.getClass().equals(FileBackedTaskManager.class)) {
                                    if (e.getMessage().equals(((FileBackedTaskManager) manager).getErrorFindFileMessage())) {
                                        sendErrorWithFile(exchange, ((FileBackedTaskManager) manager).getErrorFindFileMessage());
                                    } else if (e.getMessage().equals(((FileBackedTaskManager) manager).getErrorWorkFileMessage())) {
                                        sendErrorWithFile(exchange, ((FileBackedTaskManager) manager).getErrorWorkFileMessage());
                                    } else {
                                        sendHasInteractions(exchange);
                                    }
                                } else {
                                    sendHasInteractions(exchange);
                                }

                            }
                            exchange.sendResponseHeaders(201, 0);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        int taskId = parseToId(path.replaceFirst("/subtasks/", ""));
                        if (taskId != -1) {
                            try {
                                manager.removeSubTaskById(taskId);
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
                default:
                    sendNotFound(exchange);
            }
        } catch (EOFException e) {
            e.printStackTrace();
        }
    }
}
