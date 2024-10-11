package work.managers;

import work.managers.history.InMemoryHistoryManager;
import work.managers.task.InMemoryTaskManager;

public class Managers {

    public static InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
