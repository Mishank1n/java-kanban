import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.files.FileBackedTaskManager;
import work.managers.files.ManagerSaveException;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {

    private static Managers managers;
    private static FileBackedTaskManager manager;

    @BeforeEach
    public void beforeEach() throws ManagerSaveException {
        managers = new Managers();
        manager = Managers.loadFromFile("result.txt");
    }

    @Test
    public void checkLoadFromFile() {
        assertEquals(2, manager.getAllTasks().size());
    }

    @Test
    public void checkLoadToFile() throws ManagerSaveException {
        Task task = new Task("Test checkLoad", "Test checkLoad description", TaskStatus.NEW);
        manager.addTask(task);
        Epic epic = new Epic("Test checkLoad", "Test checkLoad description");
        manager.addEpic(epic);
        SubTask subTask = new SubTask("Test checkLoad", "Test checkLoad description", TaskStatus.NEW, epic.getId());
        manager.addSubTask(subTask);
        assertEquals(2, manager.getAllTasks().size());
    }
}
