import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.files.FileBackedTaskManager;
import work.managers.files.ManagerSaveException;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    @BeforeEach
    public void setUp() throws ManagerSaveException {
        manager = Managers.loadFromFile("result.txt");
    }

    @Test
    public void checkLoadFromFile() throws ManagerSaveException {
        assertEquals(1, manager.getAllTasksInManager().size());
        manager.deleteAllTracker();
        Task taskForAddInEnd = new Task("Test Task", "Test Task description", TaskStatus.NEW);
        manager.addTask(taskForAddInEnd);
    }

    @Test
    public void checkLoadToFile() throws ManagerSaveException {
        Task task = new Task("Test checkLoad 2", "Test checkLoad description", TaskStatus.NEW, "12.10.2024 14:22", "15");
        manager.addTask(task);
        Epic epic = new Epic("Test checkLoad 3", "Test checkLoad description");
        manager.addEpic(epic);
        Task task1 = new Task("Test checkLoad 4", "Test checkLoad description", TaskStatus.IN_PROGRESS);
        manager.addTask(task1);
        SubTask subTask = new SubTask("Test checkLoad 5", "Test checkLoad description", TaskStatus.IN_PROGRESS, "11.10.2024 18:22", "15", epic.getId());
        manager.addSubTask(subTask);
        subTask.setStatus(TaskStatus.NEW);
        manager.updateSubTask(subTask);
        assertEquals(5, manager.getAllTasksInManager().size());
        manager.deleteAllTracker();
        Task taskForAddInEnd = new Task("Test Task", "Test Task description", TaskStatus.NEW);
        manager.addTask(taskForAddInEnd);
    }

    @Test
    public void checkTrowsManagerSaveException() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager fileBackedTaskManager = Managers.loadFromFile("falseResult.txt");
        });
    }
}
