package org.cs23sw612.Ladder.Visualization.SVG;

import java.awt.geom.Path2D;

public class NewSVGGate extends NewSVGRungElement {
    boolean open;
    public NewSVGGate(double gateNumber, double rungNumber, String text, boolean open) {
        super(gateNumber, rungNumber, text);
        this.open = open;
    }

    public Path2D getShape(double x, double y) { //Todo lav linjen i starten og slutningen længere :)
        Path2D path = new Path2D.Double();

        path.moveTo(x, y);
        path.lineTo(x + 6.25, y);
        path.moveTo(x + 6.25, y - 15);
        path.lineTo(x + 6.25, y + 15);
        if (!open) {
            path.moveTo(x + 6.45, y + 14.9);
            path.lineTo(x + 18.55, y - 14.9);
        }
        path.moveTo(x + 18.75, y - 15);
        path.lineTo(x + 18.75, y + 15);
        path.moveTo(x + 18.75, y);
        path.lineTo(x + 25, y);
        return path;
    }
}
