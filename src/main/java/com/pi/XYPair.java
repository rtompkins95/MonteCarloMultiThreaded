package com.pi;

import java.util.Objects;

public class XYPair {
    public final double x;
    public final double y;

    public XYPair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XYPair xyPair = (XYPair) o;
        return Double.compare(x, xyPair.x) == 0 && Double.compare(y, xyPair.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "XYPair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
