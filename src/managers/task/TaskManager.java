package managers.task;

import types.Epic;
import types.SubTask;
import types.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    ArrayList printSubTaskInEpic(Epic epic);

    void deleteAllTask();

    void deleteAllSubTask();

    void deleteAllEpics();

    void deleteAllTracker();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    void checkEpicStatus(Epic epic);

    void updateTask(Task updateTask);

    void updateEpic(Epic updateEpic);

    void updateSubTask(SubTask updateSubTask);

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();
}
