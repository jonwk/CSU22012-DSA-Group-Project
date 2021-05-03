// package part2;

public class TSTNode {
    char data;
    boolean isEnd;
    TSTNode left, middle, right;

    /** Constructor **/
    public TSTNode(char data) {
        this.data = data;
        this.isEnd = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
}
