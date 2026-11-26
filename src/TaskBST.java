public class TaskBST {
    private static class Node {
        Task data;
        Node left;
        Node right;

        Node(Task data) {
            this.data = data;
        }
    }

    private Node root;

    public void insert(Task task) {
        root = insertRec(root, task);
    }

    private Node insertRec(Node current, Task task) {
        if (current == null) {
            return new Node(task);
        }
        if (task.title.compareToIgnoreCase(current.data.title) < 0) {
            current.left = insertRec(current.left, task);
        } else {
            current.right = insertRec(current.right, task);
        }
        return current;
    }

    public void printInOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(Node node) {
        if (node == null) return;
        inOrderRec(node.left);
        System.out.println(" - " + node.data.title + " (" + node.data.deadline + ")");
        inOrderRec(node.right);
    }

    public void printPreOrder() {
        preOrderRec(root);
    }

    private void preOrderRec(Node node) {
        if (node == null) return;
        System.out.println(" - " + node.data.title + " (" + node.data.deadline + ")");
        preOrderRec(node.left);
        preOrderRec(node.right);
    }
}
