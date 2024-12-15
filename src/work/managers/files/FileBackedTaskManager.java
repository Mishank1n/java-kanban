package work.managers.files;

import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;
import work.types.Type;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String fileName;
    private final String errorWorkFileMessage = "Ошибка при работе с файлом!";
    private final String errorFindFileMessage;
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public FileBackedTaskManager(String fileName) {
        super();
        this.fileName = fileName;
        errorFindFileMessage = "Файл " + fileName + " не найден!";
    }


    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) throws ManagerSaveException {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void updateTask(Task updateTask) throws ManagerSaveException {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) throws ManagerSaveException {
        super.updateSubTask(updateSubTask);
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllTask() throws ManagerSaveException {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubTask() throws ManagerSaveException {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void deleteAllTracker() throws ManagerSaveException {
        super.deleteAllTracker();
        save();
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) throws ManagerSaveException {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        super.removeEpicById(id);
        save();
    }

    private String toString(Task task) {
        if (task.getType().equals(Type.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            if (subTask.getStartTime() == null) {
                return subTask.getId() + "," + subTask.getType().toString() + "," + subTask.getTitle() + "," + subTask.getStatus() + "," + subTask.getDescriptionOfTask() + "," + subTask.getEpicId();
            } else {
                return subTask.getId() + "," + subTask.getType().toString() + "," + subTask.getTitle() + "," + subTask.getStatus() + "," + subTask.getDescriptionOfTask() + "," + subTask.getEpicId() + "," + subTask.getStartTime().format(outputFormatter) + "," + subTask.getDuration().toMinutes() + "," + subTask.getEndTime().format(outputFormatter);
            }
        } else {
            if (task.getStartTime() == null || task.getDuration() == null) {
                return task.getId() + "," + task.getType().toString() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescriptionOfTask();
            } else {
                return task.getId() + "," + task.getType().toString() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescriptionOfTask() + "," + task.getStartTime().format(outputFormatter) + "," + task.getDuration().toMinutes() + "," + task.getEndTime().format(outputFormatter);
            }
        }
    }

    private Task fromString(String taskString) {
        String[] elementsOfTask = taskString.split(",");
        switch (elementsOfTask[1]) {
            case "TASK":
                Task task;
                if (elementsOfTask.length > 5) {
                    task = new Task(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]), elementsOfTask[5], elementsOfTask[6]);
                } else {
                    task = new Task(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]));
                }
                task.setId(Integer.parseInt(elementsOfTask[0]));
                if (task.getId() >= nextId) {
                    nextId = task.getId() + 1;
                }
                return task;
            case "EPIC":
                Epic epic = new Epic(elementsOfTask[2], elementsOfTask[4]);
                epic.setId(Integer.parseInt(elementsOfTask[0]));
                if (epic.getId() >= nextId) {
                    nextId = epic.getId() + 1;
                }

                return epic;
            case "SUBTASK":
                SubTask subTask;
                if (elementsOfTask.length > 6) {
                    subTask = new SubTask(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]), elementsOfTask[6], elementsOfTask[7], Integer.parseInt(elementsOfTask[5]));
                } else {
                    subTask = new SubTask(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]), Integer.parseInt(elementsOfTask[5]));
                }
                subTask.setId(Integer.parseInt(elementsOfTask[0]));
                if (subTask.getId() >= nextId) {
                    nextId = subTask.getId() + 1;
                }
                return subTask;
            default:
                return null;
        }
    }

    private TaskStatus getStatusFromString(String status) {
        if (status.equals("DONE")) {
            return TaskStatus.DONE;
        } else if (status.equals("NEW")) {
            return TaskStatus.NEW;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    private void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("id,type,name,status,description,epic,start,duration,end\n");
            IntStream.range(0, nextId).forEach(i -> {
                if (tasks.containsKey(i)) {
                    if (i + 1 == nextId) {
                        try {
                            writer.write(toString(tasks.get(i)));
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    } else {
                        try {
                            writer.write(toString(tasks.get(i)) + ",\n");
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    }
                } else if (epics.containsKey(i)) {
                    Epic epic = epics.get(i);
                    checkEpicStatus(epic);
                    setStartTimeForEpic(epic);
                    setDurationInEpic(epic);
                    if (i + 1 == nextId) {
                        try {
                            writer.write(toString(epic));
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    } else {
                        try {
                            writer.write(toString(epic) + ",\n");
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    }
                } else if (subTasks.containsKey(i)) {
                    if (i + 1 == nextId) {
                        try {
                            writer.write(toString(subTasks.get(i)));
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    } else {
                        try {
                            writer.write(toString(subTasks.get(i)) + ",\n");
                        } catch (IOException e) {
                            throw new RuntimeException(errorWorkFileMessage);
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(errorFindFileMessage);
        } catch (IOException e) {
            throw new ManagerSaveException(errorWorkFileMessage);
        }
    }


    public void addAllTasksFromFile() throws ManagerSaveException {
        try (Reader reader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                Task task = fromString(bufferedReader.readLine());
                boolean isNotIntersecting = getAllTasks().stream().noneMatch(task1 -> isIntersecting(task, task1));
                if (isNotIntersecting) {
                    if (task.getType().equals(Type.SUBTASK)) {
                        SubTask subTask = (SubTask) task;
                        if (epics.containsKey(subTask.getEpicId())) {
                            Epic epic = epics.get(subTask.getEpicId());
                            epic.addSubTaskId(subTask.getId());
                            subTasks.put(subTask.getId(), subTask);
                            checkEpicStatus(epic);
                            if (subTask.getStartTime() != null) {
                                this.getPrioritizedTasks().add(subTask);
                            }
                            if (subTask.getId() >= nextId) {
                                nextId = subTask.getId() + 1;
                            }
                        }
                    } else if (task.getType().equals(Type.TASK)) {
                        tasks.put(task.getId(), task);
                        if (task.getId() >= nextId) {
                            nextId = task.getId() + 1;
                        }
                        if (task.getStartTime() != null) {
                            this.getPrioritizedTasks().add(task);
                        }
                    } else {
                        Epic epic = (Epic) task;
                        epics.put(epic.getId(), epic);
                        if (epic.getId() >= nextId) {
                            nextId = epic.getId() + 1;
                        }
                        if (epic.getStartTime() != null) {
                            this.getPrioritizedTasks().add(epic);
                        }
                    }
                } else {
                    throw new ManagerSaveException(errorOfIntersecting);
                }
            }

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(errorFindFileMessage);
        } catch (IOException e) {
            throw new ManagerSaveException(errorWorkFileMessage);
        }
    }
}