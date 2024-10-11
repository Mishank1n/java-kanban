package tests;

import work.managers.task.InMemoryTaskManager;
import work.managers.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeAll
    public static void beforeAll(){
        managers = new Managers();
        manager = managers.getDefault();
    }

    @Test
    public void addTaskToHistoryAndSaveVersionOfTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertTrue(manager.history.getHistory().size()==1);
        task.setTitle("New title");
        manager.getTaskById(task.getId());
        assertFalse(task.equals(manager.history.getHistory().get(0)));
    }

    @Test
    public void addSubTaskToHistoryAndSaveVersionOfSubTask(){
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("Test CheckHistory", "Test CheckHistory description",
                TaskStatus.NEW, epic.getId());
        manager.addSubTask(subTask);
        manager.getSubTaskById(subTask.getId());
        assertTrue(manager.history.getHistory().size()==1);
        subTask.setTitle("New Title!");
        assertFalse(subTask.equals(manager.history.getHistory().get(0)));
    }

    @Test
    public void addEpicToHistoryAndSaveVersionOfEpic(){
        Epic epic = new Epic("Test CheckHistory", "Test CheckHistory description");
        manager.addEpic(epic);
        manager.getEpicById(epic.getId());
        assertTrue(manager.history.getHistory().size()==1);
        epic.setTitle("New Title");
        assertFalse(epic.equals(manager.history.getHistory().get(0)));
    }

}