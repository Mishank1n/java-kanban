package tests;

import work.managers.task.InMemoryTaskManager;
import work.managers.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;
import work.status.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;
    @BeforeAll
    public static void beforeAll(){
        managers = new Managers();
        manager = (InMemoryTaskManager) managers.getDefault();
    }

    @Test
    public void addTaskToManagerWithoutChanges(){
        Task task = new Task("Test CheckChanges", "Test CheckChanges description", TaskStatus.NEW);
        manager.addTask(task);
        assertTrue(task.equals(manager.getAllTasks().get(0)));
    }

    @Test
    public void checkEqualsForTasksWithSameId() {
        Task firstTask = new Task("Test CheckEquals", "Test CheckEquals description",
                TaskStatus.NEW);
        manager.addTask(firstTask);
        Task secondTask = new Task("Test CheckEquals", "Test CheckEquals description",
                TaskStatus.NEW);
        manager.addTask(secondTask);
        System.out.println(firstTask.getId());
        System.out.println(secondTask.getId());
        assertFalse(firstTask.equals(secondTask)); //Проверяет при разных id
        secondTask.setId(firstTask.getId());
        assertTrue(firstTask.equals(secondTask)); //Проверяет при равных id
    }

    @Test
    public void checkEqualsForSubTasksWithSameId(){
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description",
                TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(firstSubTask);
        SubTask secondSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description",
                TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(secondSubTask);
        System.out.println(firstSubTask.getId());
        System.out.println(secondSubTask.getId());
        assertFalse(firstSubTask.equals(secondSubTask)); //Проверяет при разных id сабтасков
        secondSubTask.setId(firstSubTask.getId());
        assertTrue(firstSubTask.equals(secondSubTask)); //Проверяет при равных id*/
    }

    @Test
    public void checkEqualsForEpicsWithSameId(){
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        Epic secondEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(secondEpic);
        assertFalse(firstEpic.equals(secondEpic));
        secondEpic.setId(firstEpic.getId());
        assertTrue(firstEpic.equals(secondEpic));
    }
}