package org.cs23sw612.Ladder.Visualization;

import java.awt.*;
import java.awt.geom.Path2D;

public class SVGNOC extends SVGRungElement {
    public SVGNOC(float x, float y) {
        super(x, y);
    }

    public Shape getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + 5, y);
        path.moveTo(x + 5, y - 15);
        path.lineTo(x + 5, y + 15);
        path.moveTo(x + 15, y - 15);
        path.lineTo(x + 15, y + 15);
        path.moveTo(x + 15, y);
        path.lineTo(x + 20, y);
        return path;
    }
}
