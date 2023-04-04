package org.cs23sw612.Ladder.Visualization;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class svg_components {
    public static void NO(Graphics2D g, int x, int y){
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+5,y);
        path.moveTo(x+5, y-15);
        path.lineTo(x+5, y+15);
        path.moveTo(x+15, y-15);
        path.lineTo(x+15, y+15);
        path.moveTo(x+15, y);
        path.lineTo(x+20, y);
        g.draw(path);
    }
    public static void NC(Graphics2D g, int x, int y){

    }

}
