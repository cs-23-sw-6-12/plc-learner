package org.cs23sw612.Ladder.Visualization.SVG;

import java.awt.geom.Path2D;

public class SVGCoil extends SVGRungElement {
    private final double horizontalLineLength = 2.5;
    private final double leftCurveStartAndEndXValue = 7.5;
    private final double leftCurveXValue = 3.8;
    private final double curveYValue = 15;
    private final double rightCurveStartAndEndXValue = 17.5;
    private final double rightCurveXValue = 28.8;
    private final double rightHorizontalLineStartPointX = 22.5;
    private final double rightHorizontalLineEndPointx = rightHorizontalLineStartPointX + horizontalLineLength;

    public SVGCoil(Double x, Double y, String text) {
        super(x, y, text);
    }

    @Override
    public Path2D getShape(double x, double y) {
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + horizontalLineLength, y);
        path.moveTo(x + leftCurveStartAndEndXValue, y - curveYValue);
        path.curveTo(x + leftCurveStartAndEndXValue, y - curveYValue, x - leftCurveXValue, y,
                x + leftCurveStartAndEndXValue, y + curveYValue);
        path.moveTo(x + rightCurveStartAndEndXValue, y - curveYValue);
        path.curveTo(x + rightCurveStartAndEndXValue, y - curveYValue, x + rightCurveXValue, y,
                x + rightCurveStartAndEndXValue, y + curveYValue);
        path.moveTo(x + rightHorizontalLineStartPointX, y);
        path.lineTo(x + rightHorizontalLineEndPointx, y);
        return path;
    }
}
