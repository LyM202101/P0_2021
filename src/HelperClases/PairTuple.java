package HelperClases;

public class PairTuple <L, R> {
    public L left;
    public R right;

    public PairTuple(L left, R right){
        this.left = left;
        this.right = right;
    }

    public L getLeft(){return left;}
    public R getRight(){return right;}
}