package org.cs23sw612.Ladder.Visualization;

import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class SVGRung {
    private final double height;
    /**
     * Adjusts text position in horizontally.
     */
    private final static double TEXT_OFFSET = 7d;
    private final List<SVGRungElement> gates;
    private final SVGRungElement coil;
    private final Point2D.Double attachmentPoint;
    private final Point2D.Double endAttachmentPoint;

    public SVGRung(double height, Point2D.Double startAttachmentPoint, Point2D.Double endAttachmentPoint,
            SVGRungElement coil, List<SVGRungElement> gates) {
        this.height = height;
        this.attachmentPoint = startAttachmentPoint;
        this.endAttachmentPoint = endAttachmentPoint;
        this.gates = gates;
        this.coil = coil;
    }

    /**
     * Draw the rung onto the given svg.
     * 
     * @param svg
     *            The SVG to draw on.
     */
    public void draw(SVGGraphics2D svg) {
        var path = new Path2D.Double();
        path.moveTo(attachmentPoint.x, attachmentPoint.y);
        path.lineTo(attachmentPoint.x, height);
        for (SVGRungElement svgRungElement : gates) {
            svg.drawString(svgRungElement.text, (float) (svgRungElement.x + TEXT_OFFSET), (float) height - 20);
            path.lineTo(svgRungElement.x, height);
            path.append(svgRungElement.getShape(), true);
        }
        if (endAttachmentPoint != null) {
            path.lineTo(endAttachmentPoint.x, height);
            path.lineTo(endAttachmentPoint.x, endAttachmentPoint.y);
        } else {
            path.lineTo(coil.x, coil.y);
            path.append(coil.getShape(), true);
            svg.drawString(coil.text, (float) (coil.x + TEXT_OFFSET), (float) (coil.y - 20));
        }
        svg.draw(path);
    }

}
