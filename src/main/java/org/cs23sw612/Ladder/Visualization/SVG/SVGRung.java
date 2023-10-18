package org.cs23sw612.Ladder.Visualization.SVG;

import org.cs23sw612.Ladder.Visualization.Visualizer;
import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.util.List;

public class SVGRung {
    public final double initialRungHeight;
    /**
     * Adjusts text position in horizontally.
     */
    private final static float TEXT_X_OFFSET = 7;
    private final static float TEXT_Y_OFFSET = 20;
    public final List<SVGRungElement> gates;
    public final List<SVGRungElement> coils;

    public static final double gateWidth = Visualizer.GATE_WIDTH * 2;
    public static final double hSpacing = Visualizer.H_SPACING / 2;
    public static final double rungHeight = Visualizer.V_SPACING * 2 + Visualizer.RUNG_HEIGHT;
    private final double startOutputPointX;
    private final double endOutputPointX;

    public SVGRung(List<SVGRungElement> coils, List<SVGRungElement> gates) {
        this.gates = gates;
        this.coils = coils;
        this.initialRungHeight = gates.get(0).rungNumber * rungHeight;
        this.startOutputPointX = getXPos(coils.get(0).gateNumber) + Visualizer.GATE_WIDTH - hSpacing;
        this.endOutputPointX = startOutputPointX + Visualizer.GATE_WIDTH + hSpacing*2;
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
        //path.lineTo(10, initialRungHeight+10);
        double outerXPos = 0;
        for (SVGRungElement svgRungElement : gates) {
            // If new rung-level (or-rung)

            if (rungNumber != svgRungElement.rungNumber) {
                outerXPos = getXPos(svgRungElement.gateNumber) + Visualizer.GATE_WIDTH + hSpacing;
                var xPos = getXPos(svgRungElement.gateNumber) - hSpacing;

                path.lineTo(outerXPos, getYPos(rungNumber));

                path.moveTo(xPos, getYPos(rungNumber));
                path.lineTo(xPos, getYPos(svgRungElement.rungNumber));
                path.lineTo(xPos + hSpacing, getYPos(svgRungElement.rungNumber));

                path.moveTo(outerXPos, getYPos(rungNumber));
                path.lineTo(outerXPos, getYPos(svgRungElement.rungNumber));
                path.lineTo(outerXPos - hSpacing, getYPos(svgRungElement.rungNumber));

                path.moveTo(xPos, getYPos(svgRungElement.rungNumber));
                rungNumber = svgRungElement.rungNumber;
            } else {
                outerXPos = getXPos(svgRungElement.gateNumber) + Visualizer.GATE_WIDTH;
                path.lineTo(getXPos(svgRungElement.gateNumber)-hSpacing, getYPos(svgRungElement.rungNumber));
            }

            // Actually drawing the gate
            svg.drawString(svgRungElement.text, (float) (getXPos(svgRungElement.gateNumber) + TEXT_X_OFFSET),
                    (float) getYPos(svgRungElement.rungNumber) - TEXT_Y_OFFSET);
            path.append(svgRungElement.getShape(getXPos(svgRungElement.gateNumber), getYPos(svgRungElement.rungNumber)),
                    true);
        }

        var coilStart = startOutputPointX+hSpacing;
        var coilY = getYPos(coils.get(0).rungNumber);
        path.moveTo(outerXPos, coilY);
        path.lineTo(startOutputPointX - Visualizer.H_SPACING, coilY);

        svg.drawString(coils.get(0).text, (float) (coilStart + TEXT_X_OFFSET),
                (float) getYPos(coils.get(0).rungNumber) - TEXT_Y_OFFSET);
        path.lineTo(startOutputPointX, coilY);
        path.append(coils.get(0).getShape(coilStart, coilY), true);
        path.lineTo(endOutputPointX, coilY);

        if (coils.size() >= 2) {
            for (int j = 1; j <= coils.size() - 1; j++) {
                svg.drawString(coils.get(j).text, (float) (coilStart + TEXT_X_OFFSET),
                        (float) getYPos(coils.get(j).rungNumber) - TEXT_Y_OFFSET);
                path.moveTo(startOutputPointX, coilY);
                path.lineTo(startOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(coilStart, getYPos(coils.get(j).rungNumber));
                path.append(coils.get(j).getShape(coilStart, getYPos(coils.get(j).rungNumber)),
                        true);
                path.lineTo(endOutputPointX, getYPos(coils.get(j).rungNumber));
                path.lineTo(endOutputPointX, coilY);
            }
        }
        path.moveTo(startOutputPointX + gateWidth, coilY);
        path.lineTo(svg.getWidth(), coilY);
        svg.draw(path);
    }

    private double getXPos(double gateNumber) {
        return gateNumber * gateWidth - Visualizer.H_SPACING;
    }
    private double getYPos(double rungNumber) {
        return rungNumber * rungHeight;
    }
}
