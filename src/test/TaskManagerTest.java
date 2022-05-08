package test;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import taskengine.EpicForSubTaskNotExistException;
import taskengine.ManualChangeEpicStatusException;
import taskengine.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;


    @Test
    void shouldGetTaskId() {
        final int taskId = taskManager.getTaskId();

        Assertions.assertNotNull(taskId, "ID не получен");
        Assertions.assertEquals(1, taskId, "ID различаются");

        final int nextTaskId = taskManager.getTaskId();

        Assertions.assertNotEquals(taskId, nextTaskId, "ID дублируются");
    }

    @Test
    void shouldGetTasks() {
        final Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);
        final Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        final Map<Integer, Task> tasks = taskManager.getTasks();

        Assertions.assertNotNull(tasks, "Задачи не получены");
        Assertions.assertEquals(2, tasks.size(), "Количество задач не совпадает");
    }

    @Test
    void shouldGetSubTasks() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.DONE);
        taskManager.createEpic(epic);

        final SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId());
        final SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId());

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        final Map<Integer, SubTask> subTasks = taskManager.getSubTasks();

        Assertions.assertNotNull(subTasks, "Подзадачи не получены");
        Assertions.assertEquals(2, subTasks.size(), "Количество подзадач не совпадает");
    }

    @Test
    void shouldGetEpics() {
        final Epic epic1 = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.DONE);
        final Epic epic2 = new Epic("Второй эпик", "Описание второго эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        final Map<Integer, Epic> epics = taskManager.getEpics();

        Assertions.assertNotNull(epics, "Эпики не получены");
        Assertions.assertEquals(2, epics.size(), "Количество эпиков не совпадает");
    }

    @Test
    void shouldDeleteAllTasks() {
        final Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createTask(task);

        taskManager.deleteAllTasks();

        final Map<Integer, Task> tasks = taskManager.getTasks();

        Assertions.assertEquals(0, tasks.size(), "Задачи полностью не удалились");
    }

    @Test
    void shouldDeleteAllEpics() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.DONE);
        taskManager.createEpic(epic);

        taskManager.deleteAllEpics();

        final Map<Integer, Epic> epics = taskManager.getEpics();

        Assertions.assertEquals(0, epics.size(), "Эпики полностью не удалились");
    }

    @Test
    void shouldDeleteAllSubTasks() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createSubTask(subTask);

        taskManager.deleteAllSubTasks();

        final Map<Integer, SubTask> subTasks = taskManager.getSubTasks();

        Assertions.assertEquals(0, subTasks.size(), "Подзадачи полностью не удалились");
    }

    @Test
    void shouldGetTaskById() {
        final Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createTask(task);

        final Task gotTask = taskManager.getTaskById(task.getId());

        Assertions.assertNotNull(gotTask, "Задача не получена");
        Assertions.assertEquals(task, gotTask, "Задачи не совпадают");
    }

    @Test
    void shouldGetEpicById() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final Epic gotEpic = taskManager.getEpicById(epic.getId());

        Assertions.assertNotNull(gotEpic, "Эпик не получен");
        Assertions.assertEquals(epic, gotEpic, "Эпики не совпадают");
    }

    @Test
    void shouldGetSubTaskById() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createSubTask(subTask);

        final SubTask gotSubTask = taskManager.getSubTaskById(subTask.getId());

        Assertions.assertNotNull(gotSubTask, "Подзадача не получена");
        Assertions.assertEquals(subTask, gotSubTask);
    }

    @Test
    void shouldCreateTask() {
        final Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, null, null);
        taskManager.createTask(task);

        final Task createdTask = taskManager.getTaskById(task.getId());

        Assertions.assertNotNull(createdTask, "Задача не создана");
        Assertions.assertEquals(task, createdTask, "Задачи не совпадают после создания");

        final Map<Integer, Task> tasks = taskManager.getTasks();

        Assertions.assertNotNull(tasks, "Задача не сохранена");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
        Assertions.assertEquals(task, tasks.get(task.getId()), "Задачи не совпадают после извлечения");
    }

    @Test
    void shouldCheckStartTimeEndTimeWhenCreateOrUpdateTask() {
        final Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, null, null);
        taskManager.createTask(task1);

        final Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now(), Duration.ofDays(1));
        taskManager.createTask(task2);

        final Map<Integer, Task> tasks = taskManager.getTasks();

        Assertions.assertEquals(task2, tasks.get(task2.getId()));

        final Task task3 = new Task("Третья задача", "Описание третьй задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().plusDays(3), Duration.ofDays(5));
        taskManager.createTask(task3);

        Assertions.assertEquals(task3, tasks.get(task3.getId()));

        final Task task4 = new Task("Четвертая задача", "Описание четвертой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().plusHours(2), Duration.ofDays(5));
        taskManager.createTask(task4);

        Assertions.assertEquals(3, tasks.size());
    }

    @Test
    void shouldCreateEpic() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.DONE);
        taskManager.createEpic(epic);

        final Epic createdEpic = taskManager.getEpicById(epic.getId());

        Assertions.assertNotNull(createdEpic, "Эпик не создан");
        Assertions.assertEquals(epic, createdEpic, "Эпики не совпадают после создания");

        final Map<Integer, Epic> epics = taskManager.getEpics();

        Assertions.assertNotNull(epics, "Эпик не сохранен");
        Assertions.assertEquals(1, epics.size(), "Неверное окличество эпиков");
        Assertions.assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают после извлечения");
    }


    @Test
    void shouldCreateSubtask() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), null, null);
        taskManager.createSubTask(subTask);

        final SubTask createdSubTask = taskManager.getSubTaskById(subTask.getId());

        Assertions.assertNotNull(createdSubTask, "Подзадача не создана");
        Assertions.assertEquals(subTask, createdSubTask, "Подзадачи не совпадают после создания");

        final Map<Integer, SubTask> subTasks = taskManager.getSubTasks();

        Assertions.assertNotNull(subTasks, "Подзадача не сохранена");
        Assertions.assertEquals(1, subTasks.size());
        Assertions.assertEquals(subTask, subTasks.get(subTask.getId()),
                "Подзадачи не совпадают после извлечения");

        final EpicForSubTaskNotExistException exception = Assertions.assertThrows(
                EpicForSubTaskNotExistException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                                taskManager.getTaskId(), TaskStatus.NEW, -1, LocalDateTime.now(), Duration.ofHours(8));
                        taskManager.createSubTask(subTask);
                    }
                }
        );
    }

    @Test
    void shouldUpdateTask() {
        final LocalDateTime startTime = LocalDateTime.now();

        final Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, startTime, Duration.ofHours(8));
        taskManager.createTask(task);

        final Task updatedTask = new Task("Первая задача", "Описание первой задачи",
                task.getId(), TaskStatus.IN_PROGRESS, startTime, Duration.ofHours(8));
        taskManager.updateTask(updatedTask);

        Assertions.assertEquals(updatedTask, taskManager.getTaskById(updatedTask.getId()), "Задача не обновилась");
    }

    @Test
    void shouldUpdateEpic() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final Epic updatedStatusEpic = new Epic("Первый эпик", "Описание первого эпика",
                epic.getId(), TaskStatus.IN_PROGRESS);

        final ManualChangeEpicStatusException exception = Assertions.assertThrows(
                ManualChangeEpicStatusException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.updateEpic(updatedStatusEpic);
                    }
                }, "Статус эпика изменен вручную"
        );

        final Epic updatedEpic = new Epic("Измененный первый эпик", "Описание первого эпика",
                epic.getId(), TaskStatus.NEW);

        taskManager.updateEpic(updatedEpic);

        Assertions.assertEquals(updatedEpic, taskManager.getEpicById(epic.getId()), "Эпик не обновился");
    }

    @Test
    void shouldUpdateSubTask() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(),null, null);
        taskManager.createSubTask(subTask);

        final SubTask updatedSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                subTask.getId(), TaskStatus.IN_PROGRESS, epic.getId(), null, null);

        taskManager.updateSubTask(updatedSubTask);

        Assertions.assertEquals(updatedSubTask, taskManager.getSubTaskById(subTask.getId()), "Задача не обновилась");
    }

    @Test
    void shouldUpdateEpicStartTimeEndTimeDuration() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(),LocalDateTime.now().minusDays(1), Duration.ofDays(5));
        taskManager.createSubTask(subTask1);

        Assertions.assertEquals(epic.getStartTime(),subTask1.getStartTime());
        Assertions.assertEquals(epic.getEndTime(), subTask1.getEndTime());
        Assertions.assertEquals(epic.getDuration(), subTask1.getDuration());

        final SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(),null, null);
        taskManager.createSubTask(subTask2);

        Assertions.assertEquals(epic.getStartTime(),subTask1.getStartTime());
        Assertions.assertEquals(epic.getEndTime(), subTask1.getEndTime());
        Assertions.assertEquals(epic.getDuration(), subTask1.getDuration());

        final SubTask subTask3 = new SubTask("Третья подзадача", "Описание третьей подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(),LocalDateTime.now().minusDays(10), Duration.ofDays(1));
        taskManager.createSubTask(subTask3);

        Assertions.assertEquals(epic.getStartTime(),subTask3.getStartTime());
        Assertions.assertEquals(epic.getEndTime(),subTask1.getEndTime());
        Assertions.assertEquals(epic.getDuration(),subTask1.getDuration().plus(subTask3.getDuration()));

        final SubTask subTask4 = new SubTask("Четвертая подзадача", "Описание четвертой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(),LocalDateTime.now().plusDays(5), Duration.ofDays(1));
        taskManager.createSubTask(subTask4);

        Assertions.assertEquals(epic.getStartTime(), subTask3.getStartTime());
        Assertions.assertEquals(epic.getEndTime(), subTask4.getEndTime());
        Assertions.assertEquals(epic.getDuration(), subTask1.getDuration().plus(subTask3.getDuration()).plus(subTask4.getDuration()));

        taskManager.deleteSubTaskById(subTask4.getId());

        Assertions.assertEquals(epic.getStartTime(),subTask3.getStartTime());
        Assertions.assertEquals(epic.getEndTime(),subTask1.getEndTime());
        Assertions.assertEquals(epic.getDuration(),subTask1.getDuration().plus(subTask3.getDuration()));
    }

    @Test
    void shouldDeleteTaskById() {
        final Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createTask(task);

        taskManager.deleteTaskById(task.getId());

        final Map<Integer, Task> tasks = taskManager.getTasks();

        Assertions.assertNull(tasks.get(task.getId()), "Задача не удалилась");
    }

    @Test
    void shouldDeleteEpicById() {
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        taskManager.deleteEpicById(epic.getId());

        final Map<Integer, Epic> epics = taskManager.getEpics();

        Assertions.assertNull(epics.get(epic.getId()), "Эпик не удалился");
    }

    @Test
    void shouldDeleteSubTaskById(){
        final Epic epic = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic);

        final SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic.getId(), LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createSubTask(subTask);

        taskManager.deleteSubTaskById(subTask.getId());

        final Map<Integer, SubTask> subTasks = taskManager.getSubTasks();
        Assertions.assertNull(subTasks.get(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    void shouldGetHistory() {
        final Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createTask(task1);

        final Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);

        final Epic epic1 = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic1);

        final Epic epic2 = new Epic("Второй эпик", "Описание второго эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic2);

        final SubTask subTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId());
        taskManager.createSubTask(subTask);

        taskManager.deleteTaskById(task1.getId());

        final List<Task> referenceHistory = List.of(epic1, subTask);

        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getTaskById(task1.getId());

        final List<Task> gotHistory = taskManager.history();

        Assertions.assertNotNull(gotHistory, "История не получена");
        Assertions.assertEquals(referenceHistory, gotHistory, "История получена некорректно");

    }


    @Test
    void shouldGetPrioritizedTasks() {
        final Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createTask(task1);

        final Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().minusHours(12),Duration.ofDays(1));
        taskManager.createTask(task2);

        final Task task3 = new Task("Третья задача", "Описание третьей задачи",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createTask(task3);

        Set<Task> referenceTaskSet = new TreeSet<>();
        referenceTaskSet.add(task1);
        referenceTaskSet.add(task2);
        referenceTaskSet.add(task3);

        Set<Task> prioritizedTasksSet = taskManager.getPrioritizedTasks();


        Assertions.assertIterableEquals(referenceTaskSet,prioritizedTasksSet, "Список задач по приоритеам некорректный");

        final Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic1);

        final SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId(), LocalDateTime.now().minusDays(5), Duration.ofDays(3));
        taskManager.createSubTask(subTask1);

        final SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic1.getId(), LocalDateTime.now().minusDays(4), Duration.ofHours(8));
        taskManager.createSubTask(subTask2);

        referenceTaskSet.add(subTask1);

        prioritizedTasksSet = taskManager.getPrioritizedTasks();

        Assertions.assertIterableEquals(referenceTaskSet, prioritizedTasksSet, "Список задач по приоритеам некорректный");
    }

}
