package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*Класс для реализации объектов типа эпик*/
public class Epic extends Task {
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    //private LocalDateTime startTime;
    //private Duration duration;
    private LocalDateTime endTime;

    public Epic(String name, String description, int id, TaskStatus epicStatus) {
        super(name, description, id, epicStatus);
        this.taskType = TaskType.EPIC;
        this.endTime = null;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void addSubtask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask );
    }

    public void deleteSubTaskById(int subTaskId) {
        subTasks.remove(subTaskId);
    }

// метод автоматического изменения эпика
    public void updateEpic() {
        int countOfNewSubTasks = 0; //количество задач в эпике со статусом NEW
        int countOfInProgressSubTasks = 0; //количество задач в эпике со статусом IN_PROGRESS
        int countOfDoneSubTasks = 0; //количество задач в эпике со статусом DONE

        if (subTasks.keySet().size() > 0) { //если подзадач нет, то статус эпика всегда NEW
            startTime = null;
            endTime = null;
            duration = null;
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getStatus() == TaskStatus.NEW) {
                    countOfNewSubTasks++;
                } else if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                    countOfInProgressSubTasks++;
                } else {
                    countOfDoneSubTasks++;
                }

                if (subTask.getEndTime() != null) {
                    if (startTime == null) {
                        startTime = subTask.getStartTime();
                    } else {
                        if (subTask.getStartTime().isBefore(startTime)) {
                            startTime = subTask.getStartTime();
                        }
                    }

                    if (duration == null) {
                        duration = subTask.getDuration();
                    } else {
                        duration = duration.plus(subTask.getDuration());
                    }

                    if (endTime == null) {
                        endTime = subTask.getEndTime();
                    } else {
                        if (subTask.getEndTime().isAfter(endTime)) {
                            endTime = subTask.getEndTime();
                        }
                    }
                }
            }

            switch (getStatus()) {
                case NEW:
                    if (countOfInProgressSubTasks > 0 || (countOfDoneSubTasks > 0 && countOfNewSubTasks > 0)) {
                        status = TaskStatus.IN_PROGRESS;
                    } else if (countOfDoneSubTasks == subTasks.keySet().size()) {
                        status = TaskStatus.DONE;
                    }
                        break;
                case IN_PROGRESS:
                    if (countOfDoneSubTasks == subTasks.keySet().size()) {
                        status = TaskStatus.DONE;
                    } else if (countOfNewSubTasks == subTasks.keySet().size()) {
                        status = TaskStatus.NEW;
                    }
                    break;
                case DONE:
                    if (countOfInProgressSubTasks > 0) {
                        status = TaskStatus.IN_PROGRESS;
                    } else if (countOfNewSubTasks == subTasks.keySet().size()) {
                        status = TaskStatus.NEW;
                    }
                    break;
            }
        } else {
            status = TaskStatus.NEW;
        }
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public static Epic fromString(String value) {
        String[] taskData = value.split(",");

        return new Epic(taskData[2], taskData[4], Integer.parseInt(taskData[0]),
                TaskStatus.valueOf(taskData[3]));

    }



}
