import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.files.ManagerSaveException;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {
    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void addTaskToHistory() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertEquals(1, manager.history.getHistory().size());
    }

    @Test
    public void addSubTaskToHistoryAndCheckZeroHistory() throws ManagerSaveException {
        assertTrue(manager.history.getHistory().isEmpty());
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("Test CheckHistory", "Test CheckHistory description", TaskStatus.NEW, epic.getId());
        manager.addSubTask(subTask);
        manager.getSubTaskById(subTask.getId());
        assertEquals(1, manager.history.getHistory().size());
    }

    @Test
    public void addEpicToHistory() throws ManagerSaveException {
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(1, manager.history.getHistory().size());
    }

    /*Проверим, что epic обновился и тем самым проверили удаление редактирование связного списка */
    @Test
    public void checkLinkedListAddAndGet() throws ManagerSaveException {
        Task task = new Task("Test checkLinkedList", "Test checkLinkedList description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        Epic epic = new Epic("Test checkLinkedList epic", "Test checkLinkedList description");
        manager.addEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(epic, manager.history.getHistory().getFirst());
        epic.setTitle("New Test checkLinkedList");
        manager.updateEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(epic, manager.history.getHistory().getFirst());
    }

    @Test
    public void removeTaskFromStartHistory() throws ManagerSaveException {
        Task task1 = new Task("Test remove Task 1 ", "Description of test remove 1", TaskStatus.NEW, "13.12.2024 17:00", "30");
        Task task2 = new Task("Test remove Task 2", "Description of test remove 2", TaskStatus.NEW, "13.12.2024 17:31", "30");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        manager.addTask(task2);
        manager.getTaskById(task2.getId());
        assertEquals(2, manager.history.getHistory().size());
        manager.history.remove(task1.getId());
        assertEquals(1, manager.history.getHistory().size());
        assertEquals(manager.history.getHistory().getFirst(), task2);
    }

    @Test
    public void removeTaskFromEndHistory() throws ManagerSaveException {
        Task task1 = new Task("Test remove Task 1 ", "Description of test remove 1", TaskStatus.NEW, "13.12.2024 17:00", "30");
        Task task2 = new Task("Test remove Task 2", "Description of test remove 2", TaskStatus.NEW, "13.12.2024 17:31", "30");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        manager.addTask(task2);
        manager.getTaskById(task2.getId());
        assertEquals(2, manager.history.getHistory().size());
        manager.history.remove(task2.getId());
        assertEquals(1, manager.history.getHistory().size());
        assertEquals(manager.history.getHistory().getFirst(), task1);
    }

    @Test
    public void removeTaskFromMiddleHistory() throws ManagerSaveException {
        Task task1 = new Task("Test remove Task 1", "Description of test remove 1", TaskStatus.NEW, "13.12.2024 17:00", "30");
        Task task2 = new Task("Test remove Task 2", "Description of test remove 2", TaskStatus.NEW, "13.12.2024 17:31", "30");
        Task task3 = new Task("Test remove Task 3", "Description of test remove 3", TaskStatus.DONE, "13.12.2024 18:30", "30");
        manager.addTask(task1);
        manager.getTaskById(task1.getId());
        manager.addTask(task2);
        manager.getTaskById(task2.getId());
        manager.addTask(task3);
        manager.getTaskById(task3.getId());
        assertEquals(3, manager.history.getHistory().size());
        manager.history.remove(task2.getId());
        assertEquals(2, manager.history.getHistory().size());
        assertEquals(manager.history.getHistory().getFirst(), task3);
        assertEquals(manager.history.getHistory().getLast(), task1);
    }

    @Test
    public void checkDoubleTasks() throws ManagerSaveException {
        Task task = new Task("Test checkDoubleTasks 1", "Test checkDoubleTasks 1 description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertEquals(1, manager.history.getHistory().size());
        assertEquals(task, manager.history.getHistory().getFirst());
        task.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task);
        manager.getTaskById(task.getId());
        assertEquals(1, manager.history.getHistory().size());
        assertEquals(task, manager.history.getHistory().getFirst());
    }
}