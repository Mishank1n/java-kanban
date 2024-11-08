package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        managers = new Managers();
        manager = Managers.getDefault();
    }

    @Test
    public void addTaskToManagerWithoutChanges() {
        Task task = new Task("Test CheckChanges", "Test CheckChanges description", TaskStatus.NEW);
        manager.addTask(task);
        assertEquals(task, manager.getAllTasks().get(0));
    }

    /* Проверка на равенство и неравенство при разных и равных id для всех типов*/
    @Test
    public void checkEqualsForTasksWithSameId() {
        Task firstTask = new Task("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW);
        manager.addTask(firstTask);
        Task secondTask = new Task("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW);
        manager.addTask(secondTask);
        System.out.println(firstTask.getId());
        System.out.println(secondTask.getId());
        assertNotEquals(firstTask, secondTask);
        secondTask.setId(firstTask.getId());
        assertEquals(firstTask, secondTask);
    }

    @Test
    public void checkEqualsForSubTasksWithSameId() {
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(firstSubTask);
        SubTask secondSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(secondSubTask);
        System.out.println(firstSubTask.getId());
        System.out.println(secondSubTask.getId());
        assertNotEquals(firstSubTask, secondSubTask);
        secondSubTask.setId(firstSubTask.getId());
        assertEquals(firstSubTask, secondSubTask);
    }

    @Test
    public void checkEqualsForEpicsWithSameId() {
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        Epic secondEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(secondEpic);
        assertNotEquals(firstEpic, secondEpic);
        secondEpic.setId(firstEpic.getId());
        assertEquals(firstEpic, secondEpic);
    }
}