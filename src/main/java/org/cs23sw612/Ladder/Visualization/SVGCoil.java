package org.cs23sw612.Ladder.Visualization;

import java.awt.*;
import java.awt.geom.Path2D;

public class SVGCoil extends SVGRungElement {

    public SVGCoil(float x, float y) {
        super(x, y);
    }

    public static float WIDTH = 50f;

    @Override
    public Shape getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+2.5,y);
        path.moveTo(x+7.5, y-15);
        path.curveTo(x+7.5,y-15,x-3.8,y,x+7.5,y+15);
        path.moveTo(x+12.5,y-15);
        path.curveTo(x+12.5,y-15,x+23.8,y,x+12.5,y+15);
        path.moveTo(x+17.5,y);
        path.lineTo(x+20,y);
        return path;
    }
}
