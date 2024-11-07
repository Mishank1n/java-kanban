package work.managers.history;

import java.util.List;

import work.types.Task;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();

    public void remove(int id);
}
