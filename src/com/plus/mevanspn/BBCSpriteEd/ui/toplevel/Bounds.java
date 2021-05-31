package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import java.awt.*;

public final class Bounds {
    public Bounds(Point pointA, Point pointB) {
        this.left = Math.min(pointA.x, pointB.x);
        this.top = Math.min(pointA.y, pointB.y);
        this.right = Math.max(pointA.x, pointB.x);
        this.bottom = Math.max(pointA.y, pointB.y);
        this.width = this.right - this.left;
        this.height = this.bottom - this.top;
    }

    public Bounds(int left, int right, int top, int bottom) {
        this.left = Math.min(left, right);
        this.top = Math.min(top, bottom);
        this.right = Math.max(left, right);
        this.bottom = Math.max(top, bottom);
        this.width = this.right - this.left;
        this.height = this.bottom - this.top;
    }

    public int left, top, right, bottom, width, height;
}
