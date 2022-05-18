package model;

import java.time.Duration;
import java.time.LocalDateTime;

/*Класс для реализации объектов типа подзадача*/
public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, id, status, null, null);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, int id, TaskStatus status, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,", getId(), getTaskType(), getName(),
                getStatus(), getDescription(),getEpicId(),getStartTime(),getDuration());
    }

    public static SubTask fromString(String value) {
        String[] taskData = value.split(",");
        LocalDateTime startTime;
        Duration duration;

        if (taskData[6].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(taskData[6]);
        }

        if (taskData[7].equals("null")) {
            duration = null;
        } else {
            duration = Duration.parse(taskData[7]);
        }

        return new SubTask(taskData[2], taskData[4], Integer.parseInt(taskData[0]),
                TaskStatus.valueOf(taskData[3]), Integer.parseInt(taskData[5]), startTime, duration);
    }
}
