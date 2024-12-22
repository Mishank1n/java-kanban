package work.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.managers.files.FileBackedTaskManager;
import work.managers.task.InMemoryTaskManager;
import work.types.Epic;
import work.types.SubTask;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/epics$", path)) {
                        List<Epic> epicList = manager.getAllEpics();
                        String response = gson.toJson(epicList);
                        sendText(exchange, response);
                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        int taskId = parseToId(path.replaceFirst("/epics/", ""));
                        if (taskId != -1) {
                            try {
                                String response = gson.toJson(manager.getEpicById(taskId));
                                sendText(exchange, response);
                            } catch (NotFoundException e) {
                                sendNotFound(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                    } else if (Pattern.matches("^/epics/\\d+/subtasks", path)) {
                        int taskId = parseToId(path.replaceFirst("/epics/", "").replaceFirst("/subtasks", ""));
                        if (taskId != -1) {
                            try {
                                List<SubTask> subTaskList = manager.getSubTasksInEpic(manager.getEpicById(taskId));
                                String response = gson.toJson(subTaskList);
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
                    break;
                case "POST":
                    if (Pattern.matches("^/epics$", path)) {
                        String epicString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        if (!epicString.isEmpty()) {
                            Epic epic = gson.fromJson(epicString, Epic.class);
                            try {
                                if (manager.getEpics().containsKey(epic.getId())) {
                                    manager.updateEpic(epic);
                                    sendText(exchange, String.format("Обновили задачу с id - %d", epic.getId()));
                                } else {
                                    manager.addEpic(epic);
                                    sendText(exchange, String.format("Добавили задачу с id - %d", epic.getId()));
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
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        int taskId = parseToId(path.replaceFirst("/epics/", ""));
                        if (taskId != -1) {
                            try {
                                manager.removeEpicById(taskId);
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
                    break;
            }
        } catch (EOFException e) {
            e.printStackTrace();
        }
    }
}
