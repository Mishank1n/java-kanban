package work.managers.task;

import work.exceptions.ManagerSaveException;
import work.exceptions.NotFoundException;
import work.managers.history.InMemoryHistoryManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;
import work.types.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    public final HashMap<Integer, Task> tasks = new HashMap<>();
    public final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public final HashMap<Integer, Epic> epics = new HashMap<>();
    public InMemoryHistoryManager history = new InMemoryHistoryManager();
    public int nextId = 1;
    public String errorOfIntersecting = "Задача пересекается с другими задачами!";
    TreeSet<Task> prioritizedTasks = new TreeSet<Task>(((o1, o2) -> {
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else {
            return 1;
        }
    }));


    @Override
    public boolean isIntersecting(Task task1, Task task2) {
        if (task1.getEndTime() == null || task2.getStartTime() == null || task1.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        } else if (task1.getEndTime().isAfter(task2.getStartTime()) && task1.getStartTime().isBefore(task2.getStartTime())) {
            return true;
        } else if (task2.getEndTime().isAfter(task1.getStartTime()) && task2.getStartTime().isBefore(task1.getStartTime())) {
            return true;
        } else
            return task1.getStartTime().equals(task2.getStartTime()) || task1.getStartTime().equals(task2.getEndTime()) || task1.getEndTime().equals(task2.getEndTime()) || task1.getEndTime().equals(task2.getStartTime());
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        boolean isNotIntersecting = getAllTasks().stream().filter(task1 -> !task.getType().equals(Type.EPIC)).noneMatch(task1 -> isIntersecting(task, task1));
        if (isNotIntersecting) {
            task.setId(nextId++);
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        } else {
            throw new ManagerSaveException(errorOfIntersecting);
        }
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        epic.setId(nextId++);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {
        boolean isNotIntersecting = getAllTasks().stream().filter(task -> !task.getType().equals(Type.EPIC)).noneMatch(task -> isIntersecting(subTask, task));
        if (isNotIntersecting) {
            subTask.setId(nextId++);
            if (epics.containsKey(subTask.getEpicId())) {
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubTaskId(subTask.getId());
                subTasks.put(subTask.getId(), subTask);
                checkEpicStatus(epic);
                setStartTimeForEpic(epic);
                setDurationInEpic(epic);
                epic.setEndTime();
                if (subTask.getStartTime() != null) {
                    prioritizedTasks.add(subTask);
                }
            }
        } else {
            throw new ManagerSaveException(errorOfIntersecting);
        }
    }

    @Override
    public List<SubTask> getSubTaskInEpic(Epic epic) {
        return  epic.getSubTaskIds().stream().map(subTasks::get).toList();
    }

    //Фильтр для проверки наличия времени у задачи
    @Override
    public void deleteAllTask() throws ManagerSaveException {
        tasks.values().stream().filter(task -> task.getStartTime() != null).forEach(task -> prioritizedTasks.remove(task));
        tasks.clear();
    }

    @Override
    public void deleteAllSubTask() throws ManagerSaveException {
        subTasks.values().stream().filter(subTask -> subTask.getStartTime() != null).forEach(subTask -> prioritizedTasks.remove(subTask));
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubTaskIds().clear();
            checkEpicStatus(epic);
        });
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        deleteAllSubTask();
        epics.clear();
    }

    @Override
    public void deleteAllTracker() throws ManagerSaveException {
        deleteAllEpics();
        deleteAllTask();
        nextId = 1;
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException {
        if (tasks.containsKey(id)) {
            history.add(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    @Override
    public SubTask getSubTaskById(int id) throws NotFoundException {
        if (subTasks.containsKey(id)) {
            history.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    @Override
    public Epic getEpicById(int id) throws NotFoundException {
        if (epics.containsKey(id)) {
            history.add(epics.get(id));
            return epics.get(id);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException, NotFoundException {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            history.remove(id);
        } else {
            throw new NotFoundException("Нет такой задачи");
        }
    }

    @Override
    public void removeSubTaskById(int id) throws ManagerSaveException, NotFoundException {
        if (subTasks.containsKey(id)) {
            Epic epicForCheck = epics.get(subTasks.get(id).getEpicId());
            history.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
            epicForCheck.getSubTaskIds().remove((Integer) id);
            subTasks.remove(id);
            checkEpicStatus(epicForCheck);
            setDurationInEpic(epicForCheck);
            setStartTimeForEpic(epicForCheck);
            epicForCheck.setEndTime();
        } else {
            throw new NotFoundException("Нет такой задачи");
        }
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException, NotFoundException {
        if (epics.containsKey(id)) {
            epics.get(id).getSubTaskIds().forEach(integer -> {
                subTasks.remove(integer);
                history.remove(integer);
            });
            epics.remove(id);
            history.remove(id);
        } else {
            throw new NotFoundException("Нет такой задачи");
        }
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean isAllDone = epic.getSubTaskIds().stream().map(subTasks::get).allMatch(subTask -> subTask.getStatus() == TaskStatus.DONE);
            boolean isAllNew = epic.getSubTaskIds().stream().map(subTasks::get).allMatch(subTask -> subTask.getStatus() == TaskStatus.NEW);
            if (isAllDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (isAllNew) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }


    @Override
    public void updateTask(Task updateTask) throws ManagerSaveException {
        Optional<Task> taskToUpdate = tasks.values().stream().filter(task -> task.getId() == updateTask.getId()).findFirst();
        if (taskToUpdate.isPresent()) {
            Task task = taskToUpdate.get();
            task.setTitle(updateTask.getTitle());
            task.setDescriptionOfTask(updateTask.getDescriptionOfTask());
            task.setStatus(updateTask.getStatus());
            task.setStartTime(updateTask.getStartTime());
            task.setDuration(updateTask.getDuration());
        } else {
            throw new ManagerSaveException(String.format("Задачи с id %d нет!", updateTask.getId()));
        }
    }

    @Override
    public void updateEpic(Epic updateEpic) throws ManagerSaveException {
        Optional<Epic> epicToUpdate = epics.values().stream().filter(epic -> epic.getId() == updateEpic.getId()).findFirst();
        if (epicToUpdate.isPresent()) {
            Epic epic = epicToUpdate.get();
            epic.setTitle(updateEpic.getTitle());
            epic.setDescriptionOfTask(updateEpic.getDescriptionOfTask());
            epic.setSubTaskIds(updateEpic.getSubTaskIds());
            checkEpicStatus(epic);
            setStartTimeForEpic(epic);
            setDurationInEpic(epic);
            epic.setEndTime();
        } else {
            throw new ManagerSaveException(String.format("Задачи с id %d нет!", updateEpic.getId()));
        }
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) throws ManagerSaveException {
        Optional<SubTask> subTaskToUpdate = subTasks.values().stream().filter(subTask -> subTask.getId() == updateSubTask.getId()).findFirst();
        if (subTaskToUpdate.isPresent()) {
            SubTask subTask = subTaskToUpdate.get();
            subTask.setTitle(updateSubTask.getTitle());
            subTask.setDescriptionOfTask(updateSubTask.getDescriptionOfTask());
            subTask.setEpicId(updateSubTask.getEpicId());
            subTask.setStatus(updateSubTask.getStatus());
            subTask.setStartTime(updateSubTask.getStartTime());
            subTask.setDuration(updateSubTask.getDuration());
            Optional<Epic> epicToCheck = epics.values().stream().filter(epic -> epic.getId() == subTask.getEpicId()).findFirst();
            if (epicToCheck.isPresent()) {
                Epic epic = epicToCheck.get();
                checkEpicStatus(epic);
                setStartTimeForEpic(epic);
                setDurationInEpic(epic);
                epic.setEndTime();
            } else {
                throw new ManagerSaveException("SubTask не принадлежит ни одному из эпиков!");
            }
        } else {
            throw new ManagerSaveException(String.format("Задачи с id %d нет!", updateSubTask.getId()));
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasksInManager() {
        ArrayList<Task> allTasksInManager = new ArrayList<>(tasks.values());
        allTasksInManager.addAll(subTasks.values());
        allTasksInManager.addAll(epics.values());
        return allTasksInManager;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void setDurationInEpic(Epic epic) {
        long durationOfSubtasks = epic.getSubTaskIds().stream().map(integer -> subTasks.get(integer).getDuration().toMinutes()).mapToLong(Long::longValue).sum();
        epic.setDuration(Duration.ofMinutes(durationOfSubtasks));

    }

    public void setStartTimeForEpic(Epic epic) {
        Optional<LocalDateTime> earlierDateTime = epic.getSubTaskIds().stream().map(integer -> subTasks.get(integer).getStartTime()).filter(Objects::nonNull).min(LocalDateTime::compareTo);
        epic.setStartTime(earlierDateTime.orElse(null));
    }
}