package managers;

import managers.history.InMemoryHistoryManager;
import managers.task.InMemoryTaskManager;

public class Managers {

    public static InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
