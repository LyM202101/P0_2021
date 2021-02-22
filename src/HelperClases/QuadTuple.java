package HelperClases;

public class QuadTuple<L,CL,CR,R> {
        public L left;
        public CL centerLeft;
        public CR centerRight;
        public R right;

        public QuadTuple(L left, CL centerLeft, CR centerRight, R right){
            this.left = left;
            this.centerLeft = centerLeft;
            this.centerRight = centerRight;
            this.right = right;
        }

    public L getLeft() {
        return left;
    }

    public CL getCenterLeft() {
        return centerLeft;
    }

    public CR getCenterRight() {
        return centerRight;
    }

    public R getRight() {
        return right;
    }
}
