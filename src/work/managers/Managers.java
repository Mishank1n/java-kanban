package work.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import work.adapter.DurationAdapter;
import work.adapter.LocalDateTimeAdapter;
import work.exceptions.ManagerSaveException;
import work.managers.files.FileBackedTaskManager;
import work.managers.history.InMemoryHistoryManager;
import work.managers.task.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;


public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(String file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.addAllTasksFromFile();
        return manager;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}
