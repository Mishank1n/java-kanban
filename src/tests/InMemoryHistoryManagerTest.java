package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        managers = new Managers();
        manager = Managers.getDefault();
    }

    @Test
    public void addTaskToHistoryAndSaveVersionOfTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertEquals(1, manager.history.getHistory().size());
        task.setTitle("New title");
        manager.getTaskById(task.getId());
        assertNotEquals(task, manager.history.getHistory().get(0));
    }

    @Test
    public void addSubTaskToHistoryAndSaveVersionOfSubTask() {
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("Test CheckHistory", "Test CheckHistory description", TaskStatus.NEW, epic.getId());
        manager.addSubTask(subTask);
        manager.getSubTaskById(subTask.getId());
        assertEquals(1, manager.history.getHistory().size());
        subTask.setTitle("New Title!");
        assertNotEquals(subTask, manager.history.getHistory().get(0));
    }

    @Test
    public void addEpicToHistoryAndSaveVersionOfEpic() {
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(1, manager.history.getHistory().size());
        epic.setTitle("New Title");
        assertNotEquals(epic, manager.history.getHistory().get(0));
    }
    /*Проверим, что epic обновился и тем самым проверили удаление редактирование связного списка */
    @Test
    public void checkLinkedListAddAndGet() {
        Task task = new Task("Test checkLinkedList", "Test checkLinkedList description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        Epic epic = new Epic("Test checkLinkedList epic", "Test checkLinkedList description");
        manager.addEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(epic, manager.history.getHistory().get(0));
        epic.setTitle("New Test checkLinkedList");
        manager.updateEpic(epic);
        manager.getEpicById(epic.getId());
        assertEquals(epic, manager.history.getHistory().get(0));
    }

}