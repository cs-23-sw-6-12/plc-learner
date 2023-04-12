package org.cs23sw612.Ladder.Visualization;

import java.awt.geom.Path2D;

public abstract class SVGRungElement {
    public final double x;
    public final double y;
    public static double WIDTH = 25d;
    public final String text;

    public SVGRungElement(double x, double y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public abstract Path2D getShape();
}

