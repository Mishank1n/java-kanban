import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.exceptions.ManagerSaveException;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        managers = new Managers();
        manager = Managers.getDefault();
    }

    @Test
    public void addTaskToManagerWithoutChanges() throws ManagerSaveException {
        Task task = new Task("Test CheckChanges", "Test CheckChanges description", TaskStatus.NEW);
        manager.addTask(task);
        assertEquals(task, manager.getAllTasks().get(0));
    }

    /* Проверка на равенство и неравенство при разных и равных id для всех типов*/
    @Test
    public void checkEqualsForTasksWithSameId() throws ManagerSaveException {
        Task firstTask = new Task("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW);
        manager.addTask(firstTask);
        Task secondTask = new Task("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW);
        manager.addTask(secondTask);
        assertNotEquals(firstTask, secondTask);
        secondTask.setId(firstTask.getId());
        assertEquals(firstTask, secondTask);
    }

    @Test
    public void checkEqualsForSubTasksWithSameId() throws ManagerSaveException {
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(firstSubTask);
        SubTask secondSubTask = new SubTask("Test CheckEquals", "Test CheckEquals description", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(secondSubTask);
        assertNotEquals(firstSubTask, secondSubTask);
        secondSubTask.setId(firstSubTask.getId());
        assertEquals(firstSubTask, secondSubTask);
    }

    @Test
    public void checkEqualsForEpicsWithSameId() throws ManagerSaveException {
        Epic firstEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(firstEpic);
        Epic secondEpic = new Epic("Test CheckEquals", "Test CheckEquals description");
        manager.addEpic(secondEpic);
        assertNotEquals(firstEpic, secondEpic);
        secondEpic.setId(firstEpic.getId());
        assertEquals(firstEpic, secondEpic);
    }
}