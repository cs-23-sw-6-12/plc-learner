package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;
import org.jfree.svg.SVGUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class Visualizer {
    /**
     * The horizontal spacing between gates.
     */
    public static final double H_SPACING = 30d;
    /**
     * The vertical space between the rungs.
     */
    public static final double V_SPACING = 30d;
    /**
     * The space between the last rung
     */
    public static final double END_SPACING = H_SPACING * 5;

    /**
     * The horizontal length of a gate on the rung.
     */
    public static double GATE_WIDTH = 25d;
    /**
     * The maximum height of gates on the rung.
     */
    public static double RUNG_HEIGHT = 15d;

    public static SVGGraphics2D layoutSVG(Ladder ladder) {
        double rung_length = 0;
        double ladder_height = ladder.rungs.size() * (RUNG_HEIGHT + V_SPACING * 2) + V_SPACING;

        for (var rung : ladder.rungs) {
            var len = rung.gates.size() * (GATE_WIDTH + H_SPACING) + END_SPACING + GATE_WIDTH;
            if (rung_length < len)
                rung_length = len;
        }

        var svg = new SVGGraphics2D(rung_length, ladder_height, SVGUnits.PX);

        for (int i = 0; i < ladder.rungs.size(); i++) {

            var rung = ladder.rungs.get(i);
            var gates = new ArrayList<SVGRungElement>();
            var isOrRung = rung instanceof Ladder.ORRung;
            var height = (V_SPACING * 2 + RUNG_HEIGHT) * (i + 1);

            for (int j = 0; j < rung.gates.size(); j++) {
                Ladder.Gate ladderGate = rung.gates.get(j);
                SVGRungElement svgGate;
                var x = (j + 1) * (GATE_WIDTH + H_SPACING);

                if (ladderGate instanceof Ladder.NOC)
                    svgGate = new SVGNOC(x, height, ladderGate.gate);
                else if (ladderGate instanceof Ladder.NCC)
                    svgGate = new SVGNCC(x, height, ladderGate.gate);
                else
                    throw new RuntimeException("Unexpected gate type");

                gates.add(svgGate);
            }

            var point = new Point2D.Double(0, 0);
            SVGCoil coil = null;
            Point2D.Double endpoint = null;

            if (isOrRung) {
                point.x = GATE_WIDTH / 2;
                point.y = height - RUNG_HEIGHT - V_SPACING * 2;
                endpoint = new Point2D.Double((GATE_WIDTH + H_SPACING) * (gates.size() + 1) + GATE_WIDTH / 2, point.y);
            } else
                coil = new SVGCoil(rung_length - GATE_WIDTH, height, rung.outputgate.gate);

            new SVGRung(height, point, endpoint, coil, gates).draw(svg);
        }

        return svg;
    }

    public static void showSVG(Ladder l) throws IOException {
        var tempfile = File.createTempFile("plc-learner-", "-ladder.svg");
        saveSVG(l, tempfile.toURI());

        showSVG(tempfile.toURI());
    }

    public static void showSVG(URI path) throws IOException{
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(path);
        } catch (UnsupportedOperationException ex) {
            desktop.open(new File(path));
        }
    }

    public static void saveSVG(Ladder l, URI path) throws IOException {
        var svg = Visualizer.layoutSVG(l);
        var file = new File(path);
        SVGUtils.writeToSVG(file, svg.getSVGElement());
    }
}
