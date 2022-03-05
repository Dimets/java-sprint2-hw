package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*Класс для реализации объектов типа эпик*/
public class Epic extends Task {
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description, int id, TaskStatus epicStatus) {
        super(name, description, id, epicStatus);
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

// метод автоматического изменения статуса эпика
    public void updateStatus() {
        int countOfNewSubTasks = 0; //количество задач в эпике со статусом NEW
        int countOfInProgressSubTasks = 0; //количество задач в эпике со статусом IN_PROGRESS
        int countOfDoneSubTasks = 0; //количество задач в эпике со статусом DONE

        if (subTasks.keySet().size() > 0) { //если подзадач нет, то статус эпика всегда NEW

            for (SubTask subTask : subTasks.values()) {
                if (subTask.getStatus() == TaskStatus.NEW) {
                    countOfNewSubTasks++;
                } else if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                    countOfInProgressSubTasks++;
                } else {
                    countOfDoneSubTasks++;
                }
            }


            switch (getStatus()) {
                case NEW:
                    if (countOfInProgressSubTasks > 0) {
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


}
