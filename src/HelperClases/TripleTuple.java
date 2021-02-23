package HelperClases;

public class TripleTuple<L,C,R>{

    public L left;
    public C center;
    public R right;


    public TripleTuple(L left, C center, R right){
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public C getCenter() {
        return center;
    }

    public R getRight() {
        return right;
    }
}
