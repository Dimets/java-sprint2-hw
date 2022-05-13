package test;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskengine.InMemoryTaskManager;
import taskengine.Managers;
import taskengine.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    void shouldSerializeSubTask() {
        Epic epic = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic.getId(),
                LocalDateTime.now().plusDays(10), Duration.ofDays(1));

        Gson gson = new Gson();
        String serializedSubTask = gson.toJson(subTask);

        System.out.println(serializedSubTask);
    }
}
