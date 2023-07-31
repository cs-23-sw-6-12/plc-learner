package org.cs23sw612.Ladder.Visualization.SVG;

import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class NewSVGRung {
    private final double height;//todo fix
    /**
     * Adjusts text position in horizontally.
     */
    private final static double TEXT_OFFSET = 7d;
    public final List<NewSVGRungElement> gates;
    public final List<NewSVGRungElement> coils;
    private final Point2D.Double attachmentPoint = new Point2D.Double(0.0,0.0);
    private final Point2D.Double endAttachmentPoint = new Point2D.Double(5.0,5.0);
    private final double OUTPUT_END_SPACING = Visualizer.GATE_WIDTH + Visualizer.H_SPACING;
    private final double OUTPUT_START_SPACING = Visualizer.H_SPACING;

    private final double gateWidth = Visualizer.GATE_WIDTH*2;
    private final double hSpacing = Visualizer.H_SPACING/2;
    private final double rungHeight = Visualizer.V_SPACING*2 + Visualizer.RUNG_HEIGHT;
    private final double endAttachmentPointX = getXPos(5);
    private final double startOutputPointX = endAttachmentPointX + 10;
    private final double endOutputPointX = getXPos(7);

    public NewSVGRung(List<NewSVGRungElement> coils, List<NewSVGRungElement> gates) {
        this.gates = gates;
        this.coils = coils;
        this.height = gates.get(0).rungNumber * rungHeight;
    }

    /**
     * Draw the rung onto the given svg.
     * 
     * @param svg
     *            The SVG to draw on.
     */
    public void draw(SVGGraphics2D svg) {
        var rungNumber = gates.get(0).rungNumber;
        var path = new Path2D.Double();
        path.moveTo(0, height);
        path.lineTo(Visualizer.H_SPACING, height);
        int i = 0;
        for (NewSVGRungElement svgRungElement : gates){
            svg.drawString(svgRungElement.text, (float) (getXPos(svgRungElement.gateNumber) + TEXT_OFFSET), (float) getYPos(svgRungElement.rungNumber) - 20);
            if (rungNumber != svgRungElement.rungNumber){
                path.lineTo(endAttachmentPointX, getYPos(rungNumber));

                path.moveTo(getXPos(svgRungElement.gateNumber), getYPos(rungNumber));
                path.lineTo(getXPos(svgRungElement.gateNumber), getYPos(svgRungElement.rungNumber));
                path.lineTo(getXPos(svgRungElement.gateNumber), getYPos(svgRungElement.rungNumber));
                rungNumber = svgRungElement.rungNumber;
            }
            else{
                path.lineTo(getXPos(svgRungElement.gateNumber), getYPos(svgRungElement.rungNumber));
            }
            path.append(svgRungElement.getShape(getXPos(svgRungElement.gateNumber),getYPos(svgRungElement.rungNumber)), true);
            if (++i == gates.size()){
                path.lineTo(endAttachmentPointX, getYPos(rungNumber));
                path.lineTo(endAttachmentPointX, height);
            }
        }

        svg.drawString(coils.get(0).text, (float) (getXPos(coils.get(0).gateNumber) + TEXT_OFFSET), (float) getYPos(coils.get(0).rungNumber) - 20);
        path.lineTo(getXPos(coils.get(0).gateNumber), getYPos(coils.get(0).rungNumber));
        path.append(coils.get(0).getShape(getXPos(coils.get(0).gateNumber),getYPos(coils.get(0).rungNumber)), true);
        path.lineTo(endOutputPointX+10, getYPos(coils.get(0).rungNumber));
        if (coils.size() >= 2){
            for (int j = 1; j <= coils.size()-1; j++) {
                svg.drawString(coils.get(j).text, (float) (getXPos(coils.get(j).gateNumber) + TEXT_OFFSET), (float) getYPos(coils.get(j).rungNumber) - 20);                path.moveTo(startOutputPointX, getYPos(coils.get(0).rungNumber));
                path.lineTo(startOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(getXPos(coils.get(j).gateNumber), getYPos(coils.get(j).rungNumber));
                path.append(coils.get(j).getShape(getXPos(coils.get(j).gateNumber),getYPos(coils.get(j).rungNumber)), true);
                path.lineTo(endOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(endOutputPointX, getYPos(coils.get(0).rungNumber));
            }
        }


        /*path.moveTo(attachmentPoint.x, attachmentPoint.y);
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
        }*/
        svg.draw(path);
    }

    private double getXPos(double gateNumber){
        return gateNumber * gateWidth + hSpacing;
    }
    private double getYPos(double rungNumber){
        return rungNumber * rungHeight;
    }
}
