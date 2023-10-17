package org.cs23sw612.Ladder.Visualization.SVG;

import org.cs23sw612.Ladder.Visualization.NewVisualizer;
import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.util.List;

public class NewSVGRung {
    private final double initialRungHeight;
    /**
     * Adjusts text position in horizontally.
     */
    private final static double TEXT_OFFSET = 7d;
    public final List<NewSVGRungElement> gates;
    public final List<NewSVGRungElement> coils;

    public static final double gateWidth = Visualizer.GATE_WIDTH*2;
    public static final double hSpacing = Visualizer.H_SPACING/2;
    public static final double rungHeight = Visualizer.V_SPACING*2 + Visualizer.RUNG_HEIGHT;
    private final double endAttachmentPointX;
    private final double startOutputPointX;
    private final double endOutputPointX;

    public NewSVGRung(List<NewSVGRungElement> coils, List<NewSVGRungElement> gates) {
        this.gates = gates;
        this.coils = coils;
        this.initialRungHeight = gates.get(0).rungNumber * rungHeight;
        this.endAttachmentPointX = getXPos(coils.get(0).gateNumber - (NewVisualizer.outputOffset-1));
        this.startOutputPointX = endAttachmentPointX + gateWidth + hSpacing;
        this.endOutputPointX = getXPos(coils.get(0).gateNumber+1);
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
        path.moveTo(0, initialRungHeight);
        path.lineTo(Visualizer.H_SPACING, initialRungHeight);
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
                path.lineTo(endAttachmentPointX, initialRungHeight);
            }
        }

        svg.drawString(coils.get(0).text, (float) (getXPos(coils.get(0).gateNumber) + TEXT_OFFSET), (float) getYPos(coils.get(0).rungNumber) - 20);
        path.lineTo(getXPos(coils.get(0).gateNumber), getYPos(coils.get(0).rungNumber));
        path.append(coils.get(0).getShape(getXPos(coils.get(0).gateNumber),getYPos(coils.get(0).rungNumber)), true);
        path.lineTo(endOutputPointX+10, getYPos(coils.get(0).rungNumber));
        if (coils.size() >= 2){
            for (int j = 1; j <= coils.size()-1; j++) {
                svg.drawString(coils.get(j).text, (float) (getXPos(coils.get(j).gateNumber) + TEXT_OFFSET), (float) getYPos(coils.get(j).rungNumber) - 20);
                path.moveTo(startOutputPointX, getYPos(coils.get(0).rungNumber));
                path.lineTo(startOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(getXPos(coils.get(j).gateNumber), getYPos(coils.get(j).rungNumber));
                path.append(coils.get(j).getShape(getXPos(coils.get(j).gateNumber),getYPos(coils.get(j).rungNumber)), true);
                path.lineTo(endOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(endOutputPointX, getYPos(coils.get(0).rungNumber));
            }
        }
        svg.draw(path);
    }

    private double getXPos(double gateNumber){
        return gateNumber * gateWidth + hSpacing;
    }
    private double getYPos(double rungNumber){
        return rungNumber * rungHeight;
    }
}
