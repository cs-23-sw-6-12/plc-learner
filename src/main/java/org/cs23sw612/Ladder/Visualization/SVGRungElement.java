package org.cs23sw612.Ladder.Visualization;

import java.awt.*;

public abstract class SVGRungElement {
    public final float x;
    public final float y;
    public static float WIDTH = 50f;

    public SVGRungElement(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract Shape getShape();

    private static final int LENGTH = 50;
}

