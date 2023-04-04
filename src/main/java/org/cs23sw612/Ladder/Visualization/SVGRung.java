package org.cs23sw612.Ladder.Visualization;

import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.util.List;

public class SVGRung {
    private final float height;
    private final List<SVGRungElement> gates;

    public SVGRung(float height, List<SVGRungElement> gates) {
        this.height = height;
        this.gates = gates;
    }

    public void draw(SVGGraphics2D svg) {
        //draw start of line.

        var path = new Path2D.Double();
        path.moveTo(0, height);
        for (SVGRungElement svgRungElement : gates) {
            path.lineTo(svgRungElement.x, svgRungElement.y);
            path.append(svgRungElement.getShape(), true);
        }
        svg.draw(path);
    }
}
