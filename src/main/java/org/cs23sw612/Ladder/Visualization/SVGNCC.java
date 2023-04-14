package org.cs23sw612.Ladder.Visualization;

import java.awt.geom.Path2D;

public class SVGNCC extends SVGRungElement {

    public SVGNCC(double x, double y, String text) {
        super(x, y, text);
    }

    @Override
    public Path2D getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + 6.25, y);
        path.moveTo(x + 6.25, y - 15);
        path.lineTo(x + 6.25, y + 15);
        path.moveTo(x + 6.45, y + 14.9);
        path.lineTo(x + 18.55, y - 14.9);
        path.moveTo(x + 18.75, y - 15);
        path.lineTo(x + 18.75, y + 15);
        path.moveTo(x + 18.75, y);
        path.lineTo(x + 25, y);
        return path;
    }
}
