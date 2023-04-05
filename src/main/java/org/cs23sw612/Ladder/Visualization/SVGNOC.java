package org.cs23sw612.Ladder.Visualization;

import java.awt.*;
import java.awt.geom.Path2D;

public class SVGNOC extends SVGRungElement {
    public SVGNOC(float x, float y) {
        super(x, y);
    }

    public Shape getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+6.25,y);
        path.moveTo(x+6.25, y-15);
        path.lineTo(x+6.25, y+15);
        path.moveTo(x+18.75, y-15);
        path.lineTo(x+18.75, y+15);
        path.moveTo(x+18.75, y);
        path.lineTo(x+25, y);
        return path;
    }
}
