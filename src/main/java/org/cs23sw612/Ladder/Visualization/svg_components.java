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
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+5, y);
        path.moveTo(x+5, y-15);
        path.lineTo(x+5, y+15);
        path.moveTo(x+5.2, y+14.9);
        path.lineTo(x+14.8,y-14.9);
        path.moveTo(x+15,y-15);
        path.lineTo(x+15, y+15);
        path.moveTo(x+15, y);
        path.lineTo(x+20, y);
        g.draw(path);
    }
    public static void Coil(Graphics2D g2, int x, int y) {
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+2.5,y);
        path.moveTo(x+7.5, y-15);
        path.curveTo(x+7.5,y-15,x-3.8,y,x+7.5,y+15);
        path.moveTo(x+12.5,y-15);
        path.curveTo(x+12.5,y-15,x+23.8,y,x+12.5,y+15);
        path.moveTo(x+17.5,y);
        path.lineTo(x+20,y);
        g2.draw(path);
    }
}
