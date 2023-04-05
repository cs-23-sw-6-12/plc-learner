package org.cs23sw612.Ladder.Visualization;

import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class SVGRung {
    private final float height;
    private final List<SVGRungElement> gates;
    private final Point2D.Double attachmentPoint;
    private String coil = null;
    private Point2D.Double endAttachmentPoint = null;

    public SVGRung(float height, Point2D.Double attachmentPoint, List<SVGRungElement> gates) {
        this.height = height;
        this.gates = gates;
        this.attachmentPoint = attachmentPoint;
    }

    public SVGRung(float height, Point2D.Double attachmentPoint, Point2D.Double endAttachmentPoint, List<SVGRungElement> gates){
        this(height, attachmentPoint, gates);
        this.endAttachmentPoint = endAttachmentPoint;
    }

    public SVGRung(float height, Point2D.Double attachmentPoint, String Coil, List<SVGRungElement> gates){
        this(height, attachmentPoint, gates);
        this.coil = Coil;
    }

    public void draw(SVGGraphics2D svg) {
        //draw start of line.

        var path = new Path2D.Double();
        path.moveTo(attachmentPoint.x, attachmentPoint.y);
        path.lineTo(attachmentPoint.x, height);
        for (SVGRungElement svgRungElement : gates) {
            path.lineTo(svgRungElement.x, height);
            path.append(svgRungElement.getShape(), true);
        }
        if (endAttachmentPoint != null){
            path.lineTo(endAttachmentPoint.x, height);
            path.lineTo(endAttachmentPoint.x, endAttachmentPoint.y);
        }else{
            var coil = new SVGCoil((float) svg.getWidth() - SVGCoil.WIDTH, height);
            path.lineTo(coil.x, coil.y);
            path.append(coil.getShape(), true);
        }
        svg.draw(path);
    }
}
