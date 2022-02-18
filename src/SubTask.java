
/*Класс для реализации объектов типа подзадача*/
public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}
