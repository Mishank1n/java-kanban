package tests;

import managers.task.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import status.TaskStatus;
import types.Epic;
import types.SubTask;
import types.Task;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static Managers managers;
    private static InMemoryTaskManager manager;

    @BeforeAll
    public static void beforeAll(){
        managers = new Managers();
        manager = managers.getDefault();
    }

    @Test
    public void addAndGetTask(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        manager.addTask(task);
        assertTrue(manager.getAllTasks().size()==1);
        Integer taskId = task.getId();
        Task secondTask = manager.getTaskById(taskId);
        assertTrue(task.equals(secondTask));
    }

    @Test
    public void addAndGetSubTaskAndEpic(){
        Epic firstEpic = new Epic("Test CheckManager", "Test CheckManager description");
        manager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Test CheckManager", "Test CheckManager description",
                TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(firstSubTask);
        assertTrue(manager.getAllEpics().size()==1); //Проверка на добавление в список эпиков
        assertTrue(manager.getAllSubTasks().size()==1); //Проверка на добавление в список сабтасков
        Epic secondEpic = manager.getEpicById(firstEpic.getId());
        SubTask secondSubTask = manager.getSubTaskById(firstSubTask.getId());
        assertTrue(firstSubTask.equals(secondSubTask)); //Проверка на получение и равенство, аналогично в TaskTest
        assertTrue(firstEpic.equals(secondEpic)); //Проверка на получение и равенство, аналогично в TaskTest
    }
}