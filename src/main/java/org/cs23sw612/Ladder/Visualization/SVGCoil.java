package org.cs23sw612.Ladder.Visualization;

import java.awt.geom.Path2D;

public class SVGCoil extends SVGRungElement {


    public static float WIDTH = 50f;

    public SVGCoil(Double x, Double y, String text) {
        super(x, y, text);
    }


    @Override
    public Path2D getShape() {
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+2.5,y);
        path.moveTo(x+7.5, y-15);
        path.curveTo(x+7.5,y-15,x-3.8,y,x+7.5,y+15);
        path.moveTo(x+17.5,y-15);
        path.curveTo(x+17.5,y-15,x+28.8,y,x+17.5,y+15);
        path.moveTo(x+22.5,y);
        path.lineTo(x+25,y);
        return path;
    }
}
