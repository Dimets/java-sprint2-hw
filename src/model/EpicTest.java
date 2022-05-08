package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskengine.InMemoryTaskManager;
import taskengine.Managers;
import taskengine.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    //private static TaskManager taskManager = Managers.getDefault();
    private static TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void shouldNotChangeEpicStatusWithoutSubtasks() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика изменился");
    }

    @Test
    public void shouldEpicStatusIsNewWithAllSubtasksInStatusNew() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now().plusDays(3), Duration.ofHours(8));

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика изменился");
    }

    @Test
    public void shouldEpicStatusIsDoneWithAllSubtasksInStatusDone() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic.getId(), LocalDateTime.now().plusDays(3), Duration.ofHours(8));

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика не изменился");
    }

    @Test
    public void shouldEpicStatusIsInProgressWithSubtasksInStatusNewAndDone() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic.getId(), LocalDateTime.now().plusDays(3), Duration.ofHours(8));

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика не изменился");
    }

    @Test
    public void shouldChangeEpicStatusToInProgressFromNewWithSubtaskInStatusInProgress() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика не изменился");
    }

    @Test
    public void shouldChangeEpicStatusToInProgressFromDoneWithSubtaskInStatusInProgress() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.DONE);
        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),"Статус эпика не изменился");
    }
}