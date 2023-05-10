package org.cs23sw612.Ladder.Visualization.SVG;

import java.awt.geom.Path2D;

public abstract class SVGRungElement {
    public final double x;
    public final double y;
    public final String text;

    public SVGRungElement(double x, double y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    /**
     * Generate a path element to represent this rung element. This path should be
     * drawn on an SVG.
     * 
     * @return This element as a Path2D.
     */
    public abstract Path2D getShape();
}
