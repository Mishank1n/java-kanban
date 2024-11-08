package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeAll
    public static void beforeAll() {
        managers = new Managers();
        manager = Managers.getDefault();
    }

    /*Проверка на добавление эпиков, тасков и сабтасков, проверка на получение и равенство*/
    @Test
    public void addAndGetTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        assertEquals(1, manager.getAllTasks().size());
        Integer taskId = task.getId();
        Task secondTask = manager.getTaskById(taskId);
        assertEquals(task, secondTask);
    }

    @Test
    public void addAndGetSubTaskAndEpic() {
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
    }

    @Test
    public void addEpicAndSubTasksCheckDeleteFromEpic() {
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
    }
}