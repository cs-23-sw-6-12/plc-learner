package org.cs23sw612.Ladder.Visualization.SVG;

import java.awt.geom.Path2D;

public class SVGGate extends SVGRungElement {
    boolean open;
    private final double horizontalLineLength = 6.25;
    private final double verticalLineHalfLength = 15;
    private final double rightVerticalLineX = 18.75;
    private final double diagonaleLineStartPointX = 6.45;
    private final double diagonaleLineStartPointY = 14.9;
    private final double diagonaleLineEndPointX = 18.55;
    private final double diagonaleLineEndPointY = 14.9;
    private final double rightHorizontalLineEndPointX = rightVerticalLineX + horizontalLineLength;

    public SVGGate(double gateNumber, double rungNumber, String text, boolean open) {
        super(gateNumber, rungNumber, text);
        this.open = open;
    }

    public Path2D getShape(double x, double y) {
        Path2D path = new Path2D.Double();

        path.moveTo(x, y);
        path.lineTo(x + horizontalLineLength, y);
        path.moveTo(x + horizontalLineLength, y - verticalLineHalfLength);
        path.lineTo(x + horizontalLineLength, y + verticalLineHalfLength);
        if (!open) {
            path.moveTo(x + diagonaleLineStartPointX, y + diagonaleLineStartPointY);
            path.lineTo(x + diagonaleLineEndPointX, y - diagonaleLineEndPointY);
        }
        path.moveTo(x + rightVerticalLineX, y - verticalLineHalfLength);
        path.lineTo(x + rightVerticalLineX, y + verticalLineHalfLength);
        path.moveTo(x + rightVerticalLineX, y);
        path.lineTo(x + rightHorizontalLineEndPointX, y);
        return path;
    }
}
