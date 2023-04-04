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
    public static void Coil(Graphics2D g2) {
        g2.setPaint(Color.GREEN);
        //g2.drawRect(0, 20, 70, 50);
        g2.setPaint(Color.RED);
        Path2D path1 = new Path2D.Double();
        double[] pts = calculateReferenceArc(90);
        path1.moveTo(pts[0], pts[1]);
        path1.curveTo(pts[2], pts[3], pts[4], pts[5], pts[6], pts[7]);
        AffineTransform t = new AffineTransform();
        t.translate(35, 45);
        t.scale(35, 25);
        //t.rotate(Math.PI / 4);
        path1.transform(t);
        g2.draw(path1);

        Path2D path0 = new Path2D.Double();
        path0.moveTo(10,10);
        path0.curveTo(10,24,24,43,10,57);
        g2.draw(path0);

        Path2D path2 = new Path2D.Double();
        path2.moveTo(pts[0], pts[1]);
        path2.curveTo(pts[2], pts[3], pts[4], pts[5], pts[6], pts[7]);
        AffineTransform t2 = new AffineTransform();
        t2.rotate(3 * Math.PI / 4);
        t2.scale(35, 25);
        t2.translate(35, 35);
        path2.transform(t2);
        //g2.draw(path2);
        Path2D arc = new Path2D.Double();
        arc.append(path1, false);
        arc.append(path2, false);
        // g2.draw(arc);
        //g2.draw(path1);
        //g2.transform(t);
        g2.setPaint(Color.BLUE);
        //g2.drawArc(0, 20, 70, 50, 0, -270);
        //Arc2D arc2d = new Arc2D.Double(0d, 20d, 70d, 50d, 0d, 90, Arc2D.OPEN);
        //g2.draw(arc2d);
    }

    private static double[] calculateReferenceArc(double angle) {
        double a = (angle / 180 * Math.PI) / 2.0;
        double x0 = Math.cos(a);
        double y0 = Math.sin(a);
        double x1 = (4 - x0) / 3;
        double y1 = (1 - x0) * (3 - x0) / (3 * y0);
        double x2 = x1;
        double y2 = - y1;
        double x3 = x0;
        double y3 = -y0;
        System.out.println(x0);
        System.out.println(y0);
        System.out.println(x1);
        System.out.println(y1);
        System.out.println(x2);
        System.out.println(y2);
        System.out.println(x3);
        System.out.println(y3);
        return new double[] { x0, y0, x1, y1, x2, y2, x3, y3 };
    }
}
