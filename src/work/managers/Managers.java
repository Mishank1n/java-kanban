package work.managers;

import work.managers.files.FileBackedTaskManager;
import work.managers.files.ManagerSaveException;
import work.managers.history.InMemoryHistoryManager;
import work.managers.task.InMemoryTaskManager;

import java.io.File;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.toString());
        manager.addAllTasksFromFile();
        return manager;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
