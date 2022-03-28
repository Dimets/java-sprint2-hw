package history;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer,Node<Task>> historyMap = new HashMap<>();
    private TaskLinkedList<Task> taskTaskLinkedList = new TaskLinkedList<>();

    class TaskLinkedList<Task> {
        private Node<Task> headNode;
        private Node<Task> tailNode;
        private int size = 0;

        public Node<Task> linkLast(Task task) {
            Node<Task> lastNode = tailNode;
            Node<Task> newNode = new Node<>(tailNode, null, task);

            tailNode = newNode;

            if (lastNode == null) {
                headNode = newNode;
            } else {
                lastNode.setNext(newNode);
            }
            size++;

            return newNode;
        }

        public List<Task> getTasks() {
            List<Task> historyList = new ArrayList<>();

            for (Node<Task> i = headNode; i != null; i = i.getNext()) {
                historyList.add(i.getData());
            }

            return historyList;
        }

        public void removeNode(Node<Task> node) {
            Node<Task> prev = node.getPrev();
            Node<Task> next = node.getNext();

            if (size == 1) {
                headNode = null;
                tailNode = null;
                clearNode(node);
            } else if (size > 1) {
                if (node == headNode) {
                    headNode = next;
                    next.setPrev(null);
                    clearNode(node);
                } else if (node == tailNode) {
                    prev.setNext(null);
                    tailNode = prev;
                    clearNode(node);
                } else {
                    next.setPrev(prev);
                    prev.setNext(next);
                    clearNode(node);
                }
            }

            }

        private void clearNode(Node<Task> node) {
            node.setPrev(null);
            node.setNext(null);
            node.setData(null);
        }

    }


    @Override
    public void add(Task task) {
        int id = task.getId();

        if (historyMap.containsKey(id)) {
            historyMap.remove(id);
        }
        historyMap.put(id, taskTaskLinkedList.linkLast(task));

    }

    @Override
    public void remove(int id) {
        taskTaskLinkedList.removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return taskTaskLinkedList.getTasks();
    }
}
