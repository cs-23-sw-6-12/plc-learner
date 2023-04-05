package org.cs23sw612.Ladder.Visualization;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class svg_components {
   
    public static void NO(Graphics2D g, double x, double y){
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+6.25,y);
        path.moveTo(x+6.25, y-15);
        path.lineTo(x+6.25, y+15);
        path.moveTo(x+18.75, y-15);
        path.lineTo(x+18.75, y+15);
        path.moveTo(x+18.75, y);
        path.lineTo(x+25, y);
        g.draw(path);
    }
    public static void NC(Graphics2D g, double x, double y){
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+6.25, y);
        path.moveTo(x+6.25, y-15);
        path.lineTo(x+6.25, y+15);
        path.moveTo(x+6.45, y+14.9);
        path.lineTo(x+18.55,y-14.9);
        path.moveTo(x+18.75,y-15);
        path.lineTo(x+18.75, y+15);
        path.moveTo(x+18.75, y);
        path.lineTo(x+25, y);
        g.draw(path);
    }
    public static void Coil(Graphics2D g2, double x, double y) {
        Path2D path = new Path2D.Double();
        path.moveTo(x,y);
        path.lineTo(x+2.5,y);
        path.moveTo(x+7.5, y-15);
        path.curveTo(x+7.5,y-15,x-3.8,y,x+7.5,y+15);
        path.moveTo(x+17.5,y-15);
        path.curveTo(x+17.5,y-15,x+28.8,y,x+17.5,y+15);
        path.moveTo(x+22.5,y);
        path.lineTo(x+25,y);
        g2.draw(path);
    }
}
