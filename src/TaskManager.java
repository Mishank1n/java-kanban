import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1; 

    public void addTask (Task task){
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addEpic (Epic epic){
        epic.setId(nextId++);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
    }

    public void addSubTask (SubTask subTask){
        subTask.setId(nextId++);
        subTasks.put(subTask.getId(), subTask);

    }

    public ArrayList printSubTaskInEpic(Epic epic){
        ArrayList<SubTask> printTasks = new ArrayList<>();
        for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
            printTasks.add(subTasks.get(i));
        }
        return printTasks;
    }

    public void deleteAllTask(){
        tasks.clear();
    }

    public void deleteAllSubTask(){
        subTasks.clear();
        for (Epic epic : epics.values()) {
            checkEpicStatus(epic);
        }
    }

    public void deleteAllEpics(){
        epics.clear();
    }

    public void deleteAllTracker(){
        deleteAllEpics();
        deleteAllSubTask();
        deleteAllTask();
    }

    public Task getTaskById(int id){
        if (tasks.containsKey(id)){
            return tasks.get(id);
        } else {
            System.out.println("Error!");
            return null;
        } 
    }

    public SubTask getSubTaskById(int id){
        if (subTasks.containsKey(id)){
            return subTasks.get(id);
        } else {
            System.out.println("Error!");
            return null;
        } 
    }

    public Epic getEpicById(int id){
        if (epics.containsKey(id)){
            return epics.get(id);
        } else {
            System.out.println("Error!");
            return null;
        } 
    }

    public void removeTaskById(int id){
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Error!");
        }
    }

    public void removeSubTaskById(int id){
        if (subTasks.containsKey(id)) {
            subTasks.remove(id);
        } else {
            System.out.println("Error!");
        }
    }

    public void removeEpicById(int id){
        if (epics.containsKey(id)) {
            for (Integer subTaskId : epics.get(id).getSubTaskIds()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        } else {
            System.out.println("Error!");
        }
    }

    private void checkEpicStatus(Epic epic){
        if (epics.isEmpty()) {
            epic.setStatus(TASK_STATUS.NEW);
        } else {
            boolean isAllNew = false;
            boolean isAllDone = false;
            for (Integer subTaskId : epic.getSubTaskIds()) {
                if (subTasks.get(subTaskId).getStatus()==TASK_STATUS.IN_PROGRESS) {
                    epic.setStatus(TASK_STATUS.IN_PROGRESS);
                    return;
                } else if (subTasks.get(subTaskId).getStatus()==TASK_STATUS.DONE) {
                    isAllDone=true;
                } else {
                    isAllDone=true;
                }
            }
            if (isAllDone) {
                epic.setStatus(TASK_STATUS.DONE);
            } else {
                epic.setStatus(TASK_STATUS.NEW);
            }
        }
    }

    public void updateTask(Task updateTask){
        for (Task task : tasks.values()) {
            if(task.getId()==updateTask.getId()){
                task.setTitle(updateTask.getTitle());
                task.setDescriptionOfTask(updateTask.getDescriptionOfTask());
                task.setStatus(updateTask.getStatus());
            }
        }
    }

    public void updateEpic(Epic updateEpic){
        for (Epic epic : epics.values()) {
            if (epic.getId()==updateEpic.getId()) {
                epic.setTitle(updateEpic.getTitle());
                epic.setDescriptionOfTask(updateEpic.getDescriptionOfTask());
                epic.setSubTaskIds(updateEpic.getSubTaskIds());
                checkEpicStatus(epic);
            }
        }
    }

    public void updateSubTask(SubTask updateSubTask){
        for (SubTask subTask: subTasks.values()) {
            if (subTask.getId()==updateSubTask.getId()) {
                subTask.setTitle(updateSubTask.getTitle());
                subTask.setDescriptionOfTask(updateSubTask.getDescriptionOfTask());
                subTask.setEpicId(updateSubTask.getEpicId());
                subTask.setStatus(updateSubTask.getStatus());
                for (Epic epic : epics.values()) {
                    if (epic.getId()==subTask.getEpicId()) {
                        checkEpicStatus(epic);
                    }
                }   
            }
        }
    }

    public ArrayList getAllTasks(){
        ArrayList<Task> gotTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            gotTasks.add(task);
        }
        return gotTasks;
    }

    public ArrayList getAllSubTasks(){
        ArrayList<SubTask> gotSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            gotSubTasks.add(subTask);
        }
        return gotSubTasks;
    }

    public ArrayList getAllEpics(){
        ArrayList<Epic> gotEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            gotEpics.add(epic);
        }
        return gotEpics;
    }
}