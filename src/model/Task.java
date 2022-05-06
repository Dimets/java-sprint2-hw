package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/*Класс для реализации объектов типа задача*/
public class Task implements Comparable<Task> {
    private String name;
    private String description;
    private final int id;
    protected TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;
    private final TaskType taskType = TaskType.TASK;

    public Task(String name, String description, int id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = null;
        this.duration = null;
    }
    public Task(String name, String description, int id, TaskStatus status,
                LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,", getId(), getTaskType(), getName(), getStatus(),
                getDescription(),getStartTime(),getDuration());
    }

    public static Task fromString(String value) {
        String[] taskData = value.split(",");
        LocalDateTime startTime;
        Duration duration;

        if (taskData[5].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(taskData[5]);
        }

        if (taskData[6].equals("null")) {
            duration = null;
        } else {
            duration = Duration.parse(taskData[6]);
        }

        return new Task(taskData[2], taskData[4], Integer.parseInt(taskData[0]),
                        TaskStatus.valueOf(taskData[3]), startTime, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration) && taskType == task.taskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration, taskType);
    }

    @Override
    public int compareTo(Task o) {
        if ((startTime == null && o.startTime == null) && id > o.id) {
            return 1;
        } else if ((startTime == null && o.startTime == null) && id < o.id) {
            return -1;
        }

        if (startTime == null) {
            return 1;
        }

        if (o.startTime == null) {
            return -1;
        }

        if (startTime.isBefore(o.startTime)) {
            return -1;
        } else if (startTime.isAfter(o.startTime)) {
            return 1;
        }

        return 0;
    }
}
