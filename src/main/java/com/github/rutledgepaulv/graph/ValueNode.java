package com.github.rutledgepaulv.graph;

import com.github.rutledgepaulv.util.FieldUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ValueNode extends BaseNode<ObjectGraph, ValueNode> {

    private List<Field> resolutions = new LinkedList<>();
    private Field field;

    public ValueNode(ObjectGraph parent, List<Field> resolutions, Field field) {
        super(parent);
        this.resolutions = resolutions;
        this.field = field;
    }

    public Object value(Object instance) {
        return FieldUtils.resolveValueForFieldOnInstance(field, resolveFromParent(instance));
    }

    private Object resolveFromParent(Object instance) {
        for(Field field : resolutions) {
            instance = FieldUtils.resolveValueForFieldOnInstance(field, instance);
        }
        return instance;
    }

}
