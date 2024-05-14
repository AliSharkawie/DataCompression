class Node implements Comparable<Node> {
    char data;
    int frequency;
    Node left, right;

    public Node(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Node node) {
        return this.frequency - node.frequency;
    }
}