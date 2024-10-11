package manager;

import interfaces.TaskManager;

public class Managers {

    public static InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
