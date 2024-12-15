package work.managers.task;

import work.managers.files.ManagerSaveException;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void addSubTask(SubTask subTask) throws ManagerSaveException;

    ArrayList printSubTaskInEpic(Epic epic);

    void deleteAllTask() throws ManagerSaveException;

    void deleteAllSubTask() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    void deleteAllTracker() throws ManagerSaveException;

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    void removeTaskById(int id) throws ManagerSaveException;

    void removeSubTaskById(int id) throws ManagerSaveException;

    void removeEpicById(int id) throws ManagerSaveException;

    void checkEpicStatus(Epic epic) throws ManagerSaveException;

    void updateTask(Task updateTask) throws ManagerSaveException;

    void updateEpic(Epic updateEpic) throws ManagerSaveException;

    void updateSubTask(SubTask updateSubTask) throws ManagerSaveException;

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Task> getAllTasksInManager();

    TreeSet<Task> getPrioritizedTasks();

    boolean isIntersecting(Task task1, Task task2);
}
