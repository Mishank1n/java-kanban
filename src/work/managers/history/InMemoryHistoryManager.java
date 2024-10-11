package work.managers.history;

import work.types.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyList = new ArrayList<Task>();



    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        Task addTask = new Task(task.getTitle(), task.getDescriptionOfTask(), task.getStatus());
        if (historyList.size()==10) {
            historyList.remove(0);
        }
        historyList.add(addTask);
    }
}
