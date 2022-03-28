package history;

public class Node<Task> {
    private Task data;
    private Node<Task> next;
    private Node<Task> prev;

    public Node(Node<Task> prev, Node<Task> next, Task data) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }


    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }
}
