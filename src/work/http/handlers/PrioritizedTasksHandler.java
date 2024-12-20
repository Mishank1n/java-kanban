package work.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import work.managers.task.InMemoryTaskManager;
import work.types.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class PrioritizedTasksHandler extends BaseHttpHandler {
    public PrioritizedTasksHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equals("GET") && Pattern.matches("^/prioritized$", path)) {
                List<Task> prioritizedTasks = manager.getPrioritizedTasksList();
                String response = gson.toJson(prioritizedTasks);
                sendText(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
