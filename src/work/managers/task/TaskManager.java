package work.managers.task;

import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void addSubTask(SubTask subTask) throws ManagerSaveException;

    List getSubTaskInEpic(Epic epic);

    void deleteAllTask() throws ManagerSaveException;

    void deleteAllSubTask() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    void deleteAllTracker() throws ManagerSaveException;

    Task getTaskById(int id) throws NotFoundException;

    SubTask getSubTaskById(int id) throws NotFoundException;

    Epic getEpicById(int id) throws NotFoundException;

    void removeTaskById(int id) throws ManagerSaveException, NotFoundException;

    void removeSubTaskById(int id) throws ManagerSaveException, NotFoundException;

    void removeEpicById(int id) throws ManagerSaveException, NotFoundException;

    void checkEpicStatus(Epic epic) throws ManagerSaveException;

    void updateTask(Task updateTask) throws ManagerSaveException;

    void updateEpic(Epic updateEpic) throws ManagerSaveException;

    void updateSubTask(SubTask updateSubTask) throws ManagerSaveException;

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    ArrayList<Task> getAllTasksInManager();

    TreeSet<Task> getPrioritizedTasks();

    boolean isIntersecting(Task task1, Task task2);
}
