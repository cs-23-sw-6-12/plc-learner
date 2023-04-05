package org.cs23sw612.Ladder.Visualization;

import java.awt.*;
import java.awt.geom.Path2D;

public class SVGJunction extends SVGRungElement {

    public SVGJunction(float x, float y) {
        super(x, y);
    }

    @Override
    public Shape getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x+WIDTH, y);
        return path;
    }
}
