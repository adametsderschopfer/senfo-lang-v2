package com.senfo.lib;

import java.util.Arrays;

public final class ArrayValue implements IValue {
    private final IValue[] elements;

    public ArrayValue(int size) {
        this.elements = new IValue[size];
    }

    public ArrayValue(IValue[] elements) {
        this.elements = new IValue[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }

    public ArrayValue(ArrayValue array) {
        this(array.elements);
    }

    public IValue get(int index) {
        return elements[index];
    }

    public void set(int index, IValue value) {
        elements[index] = value;
    }

    @Override
    public double asNumber() {
        throw new RuntimeException("Cant cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(elements);
    }

    @Override
    public String toString() {
        return asString();
    }
}
