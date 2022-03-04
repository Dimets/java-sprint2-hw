package history;

import model.Task;
import taskengine.Managers;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    private int historySize = 10;

    @Override
    public void add(Task task) {
        history.add(0, task);

        if (history.size() > historySize) {
                history.remove(10);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
