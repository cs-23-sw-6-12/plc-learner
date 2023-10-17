package org.cs23sw612.Ladder.Visualization.SVG;

import java.awt.geom.Path2D;

public abstract class SVGRungElement {
    public final double gateNumber;
    public final double rungNumber;
    public final String text;

    public SVGRungElement(double gateNumber, double y, String text) {
        this.gateNumber = gateNumber;
        this.rungNumber = y;
        this.text = text;
    }

    /**
     * Generate a path element to represent this rung element. This path should be
     * drawn on an SVG.
     * 
     * @return This element as a Path2D.
     */
    public abstract Path2D getShape(double x, double y);
}
