package model;

import java.util.List;
import java.util.Objects;

/*Класс для реализации объектов типа задача*/
public class Task {
    private String name;
    private String description;
    private int id;
    protected TaskStatus status;

    private TaskType taskType = TaskType.TASK;

    public Task(String name, String description, int id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return taskType.name();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() && getName().equals(task.getName()) && Objects.equals(getDescription(),
                task.getDescription()) && getStatus() == task.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", getId(), getTaskType(), getName(), getStatus(), getDescription());
    }

    public static Task fromString(String value) {
        String[] taskData = value.split(",");

        return new Task(taskData[2], taskData[4], Integer.parseInt(taskData[0]),
                        TaskStatus.valueOf(taskData[3]));

    }
}
