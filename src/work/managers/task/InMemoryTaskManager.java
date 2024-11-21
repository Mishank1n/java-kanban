package work.managers.task;

import work.managers.files.ManagerSaveException;
import work.managers.history.InMemoryHistoryManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {
    public final HashMap<Integer, Task> tasks = new HashMap<>();
    public final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public final HashMap<Integer, Epic> epics = new HashMap<>();
    public InMemoryHistoryManager history = new InMemoryHistoryManager();
    public int nextId = 1;

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        epic.setId(nextId++);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {
        subTask.setId(nextId++);
        if (epics.containsKey(subTask.getEpicId())) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            checkEpicStatus(epic);
            subTasks.put(subTask.getId(), subTask);
        }
    }

    @Override
    public ArrayList printSubTaskInEpic(Epic epic) {
        ArrayList<SubTask> printTasks = new ArrayList<>();
        for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
            printTasks.add(subTasks.get(i));
        }
        return printTasks;
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            checkEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllTracker() {
        deleteAllEpics();
        deleteAllSubTask();
        deleteAllTask();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            history.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Error!");
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            history.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            System.out.println("Error!");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            history.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Error!");
            return null;
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            history.remove(id);
        } else {
            System.out.println("Error!");
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            int epicIdForCheck = subTasks.get(id).getEpicId();
            history.remove(id);
            subTasks.remove(id);
            for (int i = 0; i < epics.get(epicIdForCheck).getSubTaskIds().size(); i++) {
                if (epics.get(epicIdForCheck).getSubTaskIds().get(i) == id) {
                    epics.get(epicIdForCheck).getSubTaskIds().remove(i);
                }
            }
            checkEpicStatus(epics.get(epicIdForCheck));
        } else {
            System.out.println("Error!");
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subTaskId : epics.get(id).getSubTaskIds()) {
                subTasks.remove(subTaskId);
                history.remove(subTaskId);
            }
            epics.remove(id);
            history.remove(id);
        } else {
            System.out.println("Error!");
        }
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        if (epics.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean isAllDone = false;
            for (Integer subTaskId : epic.getSubTaskIds()) {
                if (subTasks.containsKey(subTaskId) && subTasks.get(subTaskId).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    return;
                } else if (subTasks.containsKey(subTaskId) && subTasks.get(subTaskId).getStatus().equals(TaskStatus.DONE)) {
                    isAllDone = true;
                } else {
                    isAllDone = true;
                }
            }
            if (isAllDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

    @Override
    public void updateTask(Task updateTask) {
        for (Task task : tasks.values()) {
            if (task.getId() == updateTask.getId()) {
                task.setTitle(updateTask.getTitle());
                task.setDescriptionOfTask(updateTask.getDescriptionOfTask());
                task.setStatus(updateTask.getStatus());
            }
        }
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == updateEpic.getId()) {
                epic.setTitle(updateEpic.getTitle());
                epic.setDescriptionOfTask(updateEpic.getDescriptionOfTask());
                epic.setSubTaskIds(updateEpic.getSubTaskIds());
                checkEpicStatus(epic);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getId() == updateSubTask.getId()) {
                subTask.setTitle(updateSubTask.getTitle());
                subTask.setDescriptionOfTask(updateSubTask.getDescriptionOfTask());
                subTask.setEpicId(updateSubTask.getEpicId());
                subTask.setStatus(updateSubTask.getStatus());
                for (Epic epic : epics.values()) {
                    if (epic.getId() == subTask.getEpicId()) {
                        checkEpicStatus(epic);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> gotTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            gotTasks.add(task);
        }
        return gotTasks;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> gotSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            gotSubTasks.add(subTask);
        }
        return gotSubTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> gotEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            gotEpics.add(epic);
        }
        return gotEpics;
    }
}