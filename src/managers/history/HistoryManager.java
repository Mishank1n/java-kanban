package managers.history;

import java.util.List;

import types.Task;

public interface HistoryManager {

    public void add(Task task) throws CloneNotSupportedException;

    public List<Task> getHistory();
}
