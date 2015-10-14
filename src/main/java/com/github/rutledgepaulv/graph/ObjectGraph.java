package com.github.rutledgepaulv.graph;

import com.github.rutledgepaulv.exceptions.InvalidPathException;
import com.github.rutledgepaulv.util.ClassificationUtils;
import com.github.rutledgepaulv.util.FieldUtils;
import com.github.rutledgepaulv.util.PathUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectGraph extends BaseNode<ObjectGraph, ObjectGraph> {

    private Class<?> clazz;
    private LinkedList<Field> resolutions = new LinkedList<>();
    private Map<String, BaseNode> children = new HashMap<>();

    public ObjectGraph(Class<?> clazz) {
        super(null);
        this.clazz = clazz;
        this.construct();
    }

    public ObjectGraph(ObjectGraph parent, Field self) {
        super(parent);
        this.resolutions = new LinkedList<>(parent.resolutions);
        this.resolutions.add(self);
        this.clazz = self.getType();
        this.construct();
    }

    private void construct() {
        List<Field> fields = FieldUtils.getUniqueFieldsWithRespectForInheritanceChain(clazz);
        List<Field> leaves = ClassificationUtils.filterToPrimitives(fields);
        List<Field> objects = ClassificationUtils.filterToObjects(fields);
        leaves.forEach(leaf -> children.put(leaf.getName(), new ValueNode(this, resolutions, leaf)));
        objects.forEach(node -> children.put(node.getName(), new ObjectGraph(this, node)));
    }

    public ValueNode resolveValueNode(String path) {
        return resolve(path, ValueNode.class);
    }

    public ObjectGraph resolveObjectNode(String path) {
        return resolve(path, ObjectGraph.class);
    }

    public boolean hasValueAtPath(String path) {
        return test(path, ValueNode.class);
    }

    public boolean hasObjectAtPath(String path) {
        return test(path, ObjectGraph.class);
    }

    private <T extends BaseNode> boolean test(String path, Class<T> clazz) {
        Iterator<String> iter = PathUtils.iterator(path);
        String first = iter.next();

        if (!children.containsKey(first)) {
            return false;
        } else {
            BaseNode node = children.get(first);
            if (iter.hasNext() && node instanceof ObjectGraph) {
                return ((ObjectGraph) node).test(PathUtils.combine(iter), clazz);
            } else {
                return !iter.hasNext() && clazz.isInstance(node);
            }
        }
    }

    private <T extends BaseNode> T resolve(String path, Class<T> clazz) {
        Iterator<String> iter = PathUtils.iterator(path);
        String first = iter.next();
        BaseNode node = children.get(first);

        if (node == null) {
            throw new InvalidPathException("'" + first + "' does not resolve to anything in the object graph.");
        }

        if (iter.hasNext() && node instanceof ObjectGraph) {
            return ((ObjectGraph) node).resolve(PathUtils.combine(iter), clazz);
        } else if (!iter.hasNext() && clazz.isInstance(node)) {
            return (T) node;
        } else {
            throw new InvalidPathException("'" + first + "' does not resolve to the appropriate type of node.");
        }
    }

}
