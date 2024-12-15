import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.files.ManagerSaveException;
import work.managers.task.TaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    @BeforeEach
    abstract void setUp() throws ManagerSaveException;

    @Test
    public void addAndGetAndRemoveTask() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        assertEquals(1, manager.getAllTasks().size());
        int taskId = task.getId();
        Task secondTask = manager.getTaskById(taskId);
        assertEquals(task, secondTask);
        manager.removeTaskById(taskId);
        assertEquals(0, manager.getAllTasks().size());
        manager.deleteAllTracker();
    }

    @Test
    public void addAndGetSubTaskAndEpic() throws ManagerSaveException {
        Epic firstEpic = new Epic("Test CheckManager", "Test CheckManager description");
        manager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Test CheckManager", "Test CheckManager description", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(firstSubTask);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(1, manager.getAllSubTasks().size());
        Epic secondEpic = manager.getEpicById(firstEpic.getId());
        SubTask secondSubTask = manager.getSubTaskById(firstSubTask.getId());
        assertEquals(firstSubTask, secondSubTask);
        assertEquals(firstEpic, secondEpic);
        manager.deleteAllTracker();
    }

    @Test
    public void addEpicAndSubTasksCheckDeleteFromEpicAndDeleteEpic() throws ManagerSaveException {
        Epic epic = new Epic("Test CheckDeleteFromEpic", "Test CheckDeleteFromEpic description");
        manager.addEpic(epic);
        SubTask firstSubTask = new SubTask("Test CheckDeleteFromEpic", "Test CheckDeleteFromEpic description", TaskStatus.NEW, epic.getId());
        SubTask secondSubTask = new SubTask("Test CheckDeleteFromEpic 2", "Test CheckDeleteFromEpic description 2", TaskStatus.IN_PROGRESS, epic.getId());
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        assertEquals(2, epic.getSubTaskIds().size());
        manager.removeSubTaskById(secondSubTask.getId());
        assertEquals(1, epic.getSubTaskIds().size());
        assertFalse(epic.getSubTaskIds().contains(secondSubTask.getId()));
        manager.removeEpicById(epic.getId());
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getAllSubTasks().size());
        manager.deleteAllTracker();
    }

    @Test
    public void checkDeleteAllTasks() throws ManagerSaveException {
        Task task1 = new Task("Test delete 1", "Test delete 1 description", TaskStatus.DONE, "10.10.2024 10:00", "100");
        Task task2 = new Task("Test delete 2", "Test delete 2 description", TaskStatus.IN_PROGRESS, "11.10.2024 10:00", "100");
        Task task3 = new Task("Test delete 3", "Test delete 3 description", TaskStatus.NEW, "12.10.2024 10:00", "100");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertEquals(3, manager.getAllTasks().size());
        manager.deleteAllTask();
        assertEquals(0, manager.getAllTasks().size());
        manager.deleteAllTracker();
    }

    @Test
    public void checkGetEpicFromSubTaskAndDeleteAllSubTasks() throws ManagerSaveException {
        Epic epic = new Epic("Epic Test delete", "Epic Test delete description");
        manager.addEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1 Test delete", "SubTask 1 Test delete description", TaskStatus.DONE, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2 Test delete", "SubTask 2 Test delete description", TaskStatus.IN_PROGRESS, epic.getId());
        SubTask subTask3 = new SubTask("SubTask 3 Test delete", "SubTask 3 Test delete description", TaskStatus.DONE, epic.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        Epic epicForCheck = manager.getEpicById(subTask1.getEpicId());
        assertEquals(epicForCheck, epic);
        assertEquals(3, epic.getSubTaskIds().size());
        assertEquals(3, manager.getAllSubTasks().size());
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        manager.deleteAllSubTask();
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, epic.getSubTaskIds().size());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        manager.deleteAllTracker();
        Task taskForAddInEnd = new Task("Test Task", "Test Task description", TaskStatus.NEW);
        manager.addTask(taskForAddInEnd);
    }

    @Test
    public void firstCheckThrowExceptionThanTasksAreIntersecting() {
        assertThrows(ManagerSaveException.class, () -> {
            Task task1 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 10:45", "15");
            Task task2 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 11:00", "15");
            manager.addTask(task1);
            manager.addTask(task2);
        });
    }

    @Test
    public void secondCheckThrowExceptionThanTasksAreIntersecting() {
        assertThrows(ManagerSaveException.class, () -> {
            Task task1 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 10:45", "15");
            Task task2 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 10:50", "15");
            manager.addTask(task1);
            manager.addTask(task2);
        });
    }

    @Test
    public void thirdCheckThrowExceptionThanTasksAreIntersecting() {
        assertThrows(ManagerSaveException.class, () -> {
            Task task1 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 10:45", "15");
            Task task2 = new Task("Test throw exception than tasks are intersecting", "Test throw exception than tasks are intersecting description", TaskStatus.NEW, "10.10.2024 10:30", "15");
            manager.addTask(task1);
            manager.addTask(task2);
        });
    }
}