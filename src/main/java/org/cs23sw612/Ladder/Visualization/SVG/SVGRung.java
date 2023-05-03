package org.cs23sw612.Ladder.Visualization.SVG;

import org.cs23sw612.Ladder.Visualization.Visualizer;
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
    private final List<SVGRungElement> coils;
    private final Point2D.Double attachmentPoint;
    private final Point2D.Double endAttachmentPoint;
    private final double OUTPUT_END_SPACING = Visualizer.GATE_WIDTH + Visualizer.H_SPACING;
    private final double OUTPUT_START_SPACING = Visualizer.H_SPACING;

    public SVGRung(double height, Point2D.Double startAttachmentPoint, Point2D.Double endAttachmentPoint,
            List<SVGRungElement> coils, List<SVGRungElement> gates) {
        this.height = height;
        this.attachmentPoint = startAttachmentPoint;
        this.endAttachmentPoint = endAttachmentPoint;
        this.gates = gates;
        this.coils = coils;
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
            path.lineTo(coils.get(0).x, coils.get(0).y);
            path.append(coils.get(0).getShape(), true);
            svg.drawString(coils.get(0).text, (float) (coils.get(0).x + TEXT_OFFSET), (float) (coils.get(0).y - 20));
            path.lineTo(path.getCurrentPoint().getX() + Visualizer.H_SPACING + 10, coils.get(0).y);

            for (int i = 1; i < coils.size(); i++) {
                path.moveTo(coils.get(i - 1).x - OUTPUT_START_SPACING, coils.get(i - 1).y);
                path.lineTo(coils.get(i).x - OUTPUT_START_SPACING, coils.get(i).y);
                path.lineTo(coils.get(i).x, coils.get(i).y);
                path.append(coils.get(i).getShape(), true);
                svg.drawString(coils.get(i).text, (float) (coils.get(i).x + TEXT_OFFSET),
                        (float) (coils.get(i).y - 20));
                path.lineTo(coils.get(i).x + OUTPUT_END_SPACING, coils.get(i).y);
                path.lineTo(coils.get(i - 1).x + OUTPUT_END_SPACING, coils.get(i - 1).y);
            }
        }
        svg.draw(path);
    }

}
