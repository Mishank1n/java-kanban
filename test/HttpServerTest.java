import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.adapter.EpicListAdapter;
import work.adapter.SubTaskListAdapter;
import work.adapter.TaskListAdapter;
import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.http.HttpTaskServer;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    InMemoryTaskManager manager = Managers.getDefault();

    HttpTaskServer server = new HttpTaskServer(manager);

    public HttpServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws ManagerSaveException {
        manager.deleteAllTracker();
        server.start();
    }

    @AfterEach
    public void setDown() {
        server.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        String taskString = server.getGson().toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskString)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(1, manager.getTasks().size());

    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        manager.addTask(task);
        Task secondTask = task;
        secondTask.setStatus(TaskStatus.DONE);
        String taskString = server.getGson().toJson(secondTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskString)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(1, manager.getTasks().size());
        assertEquals(secondTask, manager.getAllTasks().getFirst());
    }

    @Test
    public void getTask() throws IOException, InterruptedException, NotFoundException {
        Task task = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task task1 = server.getGson().fromJson(response.body(), Task.class);
        assertEquals(manager.getTaskById(1), task1);
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        Task task2 = new Task("Test", "Testing task", TaskStatus.NEW, "19.10.2024 20:40", "15");
        manager.addTask(task1);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> taskList = server.getGson().fromJson(response.body(), new TaskListAdapter().getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, taskList.size());
        assertEquals(manager.getAllTasks(), taskList);
    }

    @Test
    public void addSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask Test", "SubTask Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        String gsonSubTask = server.getGson().toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(gsonSubTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getSubTasks().size());
    }

    @Test
    public void updateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask Test", "SubTask Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(subTask);
        SubTask secondSubTask = subTask;
        secondSubTask.setStatus(TaskStatus.IN_PROGRESS);

        String gsonSubTask = server.getGson().toJson(secondSubTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(gsonSubTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getSubTasks().size());
        assertEquals(secondSubTask, manager.getAllSubTasks().getFirst());
    }

    @Test
    public void getSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask Test", "SubTask Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask gotSubTask = server.getGson().fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subTask, gotSubTask);
    }

    @Test
    public void getAllSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask firstSubTask = new SubTask("SubTask 1 Test", "SubTask 1 Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(firstSubTask);
        SubTask secondSubTask = new SubTask("SubTask 2 Test", "SubTask 2 Test", TaskStatus.DONE, "11.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(secondSubTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> subTasksList = server.getGson().fromJson(response.body(), new SubTaskListAdapter().getType());
        List<SubTask> allSubTasks = manager.getAllSubTasks();
        assertEquals(200, response.statusCode());
        assertEquals(allSubTasks, subTasksList);
    }

    @Test
    public void deleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask Test", "SubTask Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    public void addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        String gsonEpic = server.getGson().toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(gsonEpic)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllEpics().size());
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        Epic secondEpic = epic;
        secondEpic.setTitle("New Epic Test");
        String gsonEpic = server.getGson().toJson(secondEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json;charset=utf-8").uri(uri).POST(HttpRequest.BodyPublishers.ofString(gsonEpic)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(secondEpic, manager.getAllEpics().getFirst());
    }

    @Test
    public void getEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic gotEpic = server.getGson().fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(epic, gotEpic);
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1 Test", "Epic 1 Test");
        Epic epic2 = new Epic("Epic 2 Test", "Epic 2 Test");
        Epic epic3 = new Epic("Epic 3 Test", "Epic 3 Test");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> allEpics = server.getGson().fromJson(response.body(), new EpicListAdapter().getType());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllEpics(), allEpics);
    }

    @Test
    public void getSubTasksInEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);
        SubTask firstSubTask = new SubTask("SubTask 1 Test", "SubTask 1 Test", TaskStatus.DONE, "10.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(firstSubTask);
        SubTask secondSubTask = new SubTask("SubTask 2 Test", "SubTask 2 Test", TaskStatus.DONE, "11.10.2024 10:45", "15", epic.getId());
        manager.addSubTask(secondSubTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> allSubTasksInEpic = server.getGson().fromJson(response.body(), new SubTaskListAdapter().getType());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTasksInEpic(epic), allSubTasksInEpic);
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Epic Test");
        manager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        Task task2 = new Task("Test", "Testing task", TaskStatus.NEW, "19.10.2024 20:40", "15");
        manager.addTask(task1);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedList = server.getGson().fromJson(response.body(), new TaskListAdapter().getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, prioritizedList.size());
        assertEquals(manager.getPrioritizedTasksList(), prioritizedList);
    }

    @Test
    public void getHistory() throws IOException, NotFoundException, InterruptedException {
        Task task1 = new Task("Test", "Testing task", TaskStatus.NEW, "20.10.2024 20:40", "15");
        Task task2 = new Task("Test", "Testing task", TaskStatus.NEW, "19.10.2024 20:40", "15");
        manager.addTask(task1);
        manager.addTask(task2);

        manager.getTaskById(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> historyList = server.getGson().fromJson(response.body(), new TaskListAdapter().getType());

        assertEquals(200, response.statusCode());
        assertEquals(manager.history.getHistory(), historyList);
        assertEquals(task2, manager.history.getHistory().getFirst());
    }

}
