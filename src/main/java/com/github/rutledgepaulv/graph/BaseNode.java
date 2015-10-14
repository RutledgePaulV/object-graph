package com.github.rutledgepaulv.graph;

public class BaseNode<P extends BaseNode, T extends BaseNode> {

    protected P parent;

    public BaseNode(P parent) {
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    public T setParent(P parent) {
        this.parent = parent;
        return (T) this;
    }


}
