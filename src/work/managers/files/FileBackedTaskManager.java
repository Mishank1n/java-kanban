package work.managers.files;

import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;
import work.types.Type;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    String fileName;
    String errorWorkFileMessage = "Ошибка при работе с файлом!";
    String errorFindFileMessage;

    public FileBackedTaskManager(String fileName) {
        super();
        this.fileName = fileName;
        errorFindFileMessage = "Файл "+fileName+" не найден!";
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

    private String toString(Task task) {
        if (task.getType().equals(Type.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            return subTask.getId() + "," + subTask.getType().toString() + "," + subTask.getTitle() + "," + subTask.getStatus() + "," + subTask.getDescriptionOfTask() + "," + subTask.getEpicId();
        } else {
            return task.getId() + "," + task.getType().toString() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescriptionOfTask();
        }
    }

    private Task fromString(String taskString) {
        String[] elementsOfTask = taskString.split(",");
        switch (elementsOfTask[1]) {
            case "TASK":
                Task task = new Task(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]));
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
                SubTask subTask = new SubTask(elementsOfTask[2], elementsOfTask[4], getStatusFromString(elementsOfTask[3]), Integer.parseInt(elementsOfTask[5]));
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
            writer.write("id,type,name,status,description,epic\n");
            for (int i = 0; i < nextId; i++) {
                if (tasks.containsKey(i)) {
                    if (i + 1 == nextId) {
                        writer.write(toString(tasks.get(i)));
                    } else {
                        writer.write(toString(tasks.get(i)) + ",\n");
                    }
                } else if (epics.containsKey(i)) {
                    if (i + 1 == nextId) {
                        writer.write(toString(epics.get(i)));
                    } else {
                        writer.write(toString(epics.get(i)) + ",\n");
                    }
                } else if (subTasks.containsKey(i)) {
                    if (i + 1 == nextId) {
                        writer.write(toString(subTasks.get(i)));
                    } else {
                        writer.write(toString(subTasks.get(i)) + ",\n");
                    }
                }
            }
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
                if (task.getType().equals(Type.SUBTASK)) {
                    SubTask subTask = (SubTask) task;
                    if (epics.containsKey(subTask.getEpicId())) {
                        Epic epic = epics.get(subTask.getEpicId());
                        epic.addSubTaskId(subTask.getId());
                        checkEpicStatus(epic);
                        subTasks.put(subTask.getId(), subTask);
                        if (subTask.getId() >= nextId) {
                            nextId = subTask.getId() + 1;
                        }

                    }
                } else if (task.getType().equals(Type.TASK)) {
                    tasks.put(task.getId(), task);
                    if (task.getId() >= nextId) {
                        nextId = task.getId() + 1;
                    }
                } else {
                    Epic epic = (Epic) task;
                    checkEpicStatus(epic);
                    epics.put(epic.getId(), epic);
                    if (epic.getId() >= nextId) {
                        nextId = epic.getId() + 1;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(errorFindFileMessage);
        } catch (IOException e) {
            throw new ManagerSaveException(errorWorkFileMessage);
        }
    }
}