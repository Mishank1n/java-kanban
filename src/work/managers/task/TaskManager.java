package work.managers.task;

import work.managers.files.ManagerSaveException;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void addSubTask(SubTask subTask) throws ManagerSaveException;

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
