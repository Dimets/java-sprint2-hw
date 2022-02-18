import java.util.ArrayList;
import java.util.HashMap;

/*Класс для реализации объектов типа эпик*/
public class Epic extends Task {
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description, int id, TaskStatus epicStatus) {
        super(name, description, id, epicStatus);
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void addSubtask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask );
    }


    public void updateStatus() {
        switch(getStatus()) {
            case NEW:
                for (SubTask subTask : subTasks.values()) {
                    if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                        status = TaskStatus.IN_PROGRESS;
                    }
                }
                break;
            case IN_PROGRESS:
                int countOfDoneSubtasks = 0;

                for (SubTask subTask : subTasks.values()) {
                    if (subTask.getStatus() == TaskStatus.DONE) {
                        countOfDoneSubtasks++;
                    }
                }

                if (countOfDoneSubtasks == subTasks.keySet().size()) {
                    status = TaskStatus.DONE;
                }
                break;
            case DONE:
                break;
        }

    }


}
