package model;

/*Класс для реализации объектов типа подзадача*/
public class SubTask extends Task {
    private int epicId;

    private TaskType taskType = TaskType.SUBTASK;

    public SubTask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
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
        return String.format("%d,%s,%s,%s,%s,%d", getId(), getTaskType(), getName(),
                getStatus(), getDescription(),getEpicId());
    }

    public static SubTask fromString(String value) {
        String[] taskData = value.split(",");
        return new SubTask(taskData[2], taskData[4], Integer.parseInt(taskData[0]),
                TaskStatus.valueOf(taskData[3]), Integer.parseInt(taskData[5]));
    }
}
