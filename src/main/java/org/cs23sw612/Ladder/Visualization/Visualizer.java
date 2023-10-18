package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Rungs.CompositeRung;
import org.cs23sw612.Ladder.Rungs.Rung;
import org.cs23sw612.Ladder.Rungs.SimpleRung;
import org.cs23sw612.Ladder.Visualization.SVG.*;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static final double END_SPACING_Y = H_SPACING * 5;

    public static final double END_SPACING_X = 40d;
    /**
     * The horizontal length of a gate on the rung.
     */
    public static double GATE_WIDTH = 25d;
    /**
     * The maximum height of gates on the rung.
     */
    public static double RUNG_HEIGHT = 15d;

    public static int outputOffset = 1;
    private static double currentNumberOfRungs = 0;

    public static SVGGraphics2D layoutSVG(Ladder ladder) {

        ArrayList<SVGRung> rungs = new ArrayList<>();

        for (Map.Entry<List<String>, Rung> listNewRungEntry : ladder.gates.entrySet()) {
            // construct a list of SVGgates for this rung.
            Rung rung = listNewRungEntry.getValue();
            ArrayList<SVGRungElement> gateSequence = new ArrayList<>();
            addGate(rung, gateSequence, 1, currentNumberOfRungs + 1);

            // construct a list of SVGcoils for this rung.
            ArrayList<SVGRungElement> outputSequence = new ArrayList<>();
            double i = 0;
            for (String coilLabel : listNewRungEntry.getKey()) {
                i++;
                outputSequence.add(
                        new SVGCoil(ladder.horizontalMaxLength + outputOffset, currentNumberOfRungs + i, coilLabel));
            }

            // Update current number of rungs.
            if (gateSequence.get(gateSequence.size() - 1).rungNumber < currentNumberOfRungs + i) {
                currentNumberOfRungs += i;
            } else {
                currentNumberOfRungs = gateSequence.get(gateSequence.size() - 1).rungNumber;
            }

            rungs.add(new SVGRung(outputSequence, gateSequence));
        }

        // Add state update rung
        for (Map.Entry<String, String> stringEntry : ladder.stateUpd.entrySet()) {
            ArrayList<SVGRungElement> gate = new ArrayList<>();
            gate.add(new SVGGate(1., currentNumberOfRungs + 1, stringEntry.getValue(), true));

            ArrayList<SVGRungElement> coil = new ArrayList<>();
            coil.add(new SVGCoil(ladder.horizontalMaxLength + outputOffset, currentNumberOfRungs + 1,
                    stringEntry.getKey()));

            currentNumberOfRungs += 1;
            rungs.add(new SVGRung(coil, gate));
        }

        var svg = GenerateNewSVGFromLadderDimensions(ladder);

        // draw rungs on svg
        for (var rung : rungs) {
            rung.draw(svg);
        }

        // Left line
        var path = new Path2D.Double();
        path.moveTo(0, 0);
        path.lineTo(0, svg.getHeight());
        path.moveTo(svg.getWidth(), 0);
        path.lineTo(svg.getWidth(), svg.getHeight());
        svg.draw(path);

        return svg;
    }

    /// Returns how many additional vertical elements were added (the y coordinate)
    /// - The results will be the number of or-rungs minus 1
    public static double addGate(Rung rung, ArrayList<SVGRungElement> gateSequence, double x, double y) {
        double addedHeight = 0;
        if (rung instanceof SimpleRung sRung) {
            var gate = new SVGGate(x, y, sRung.label, sRung.open);
            gateSequence.add(gate);

            for (Rung r : sRung.followingRungs) {
                addedHeight += addGate(r, gateSequence, x + 1, y + addedHeight);
            }
            addedHeight = Math.max(addedHeight, sRung.followingRungs.size() - 1);
        } else if (rung instanceof CompositeRung cRung) {
            if (cRung.printRightLabel()) {
                var gate = new SVGGate(x, y, cRung.label, true);
                gateSequence.add(gate);
                addedHeight += addGate(cRung.right, gateSequence, x + 1, y);
            } else
                addedHeight += addGate(cRung.right, gateSequence, x, y);

            if (cRung.printLeftLabel()) {
                var gate = new SVGGate(x, y + addedHeight + 1, cRung.label, false);
                gateSequence.add(gate);
                addedHeight += addGate(cRung.left, gateSequence, x + 1, y + addedHeight + 1);
            } else
                addedHeight += addGate(cRung.left, gateSequence, x, y + addedHeight + 1);
            addedHeight += 1;
        }
        return addedHeight;
    }

    private static SVGGraphics2D GenerateNewSVGFromLadderDimensions(Ladder ladder) {
        var height = (currentNumberOfRungs + 1) * SVGRung.rungHeight;
        var width = (ladder.horizontalMaxLength + outputOffset + 1) * SVGRung.gateWidth + SVGRung.hSpacing;
        return new SVGGraphics2D(width, height, SVGUnits.PX);
    }

    /**
     * Show a svg in the browser or, if that fails, your default svg-application.
     *
     * @param svg
     *            The svg to display.
     * @throws IOException
     *             If the SVG file could not be created.
     */
    public static void showSVG(SVGGraphics2D svg) throws IOException {
        var tempfile = File.createTempFile("plc-learner-", "-ladder.svg");

        var tempFileWriter = new FileWriter(tempfile);
        saveSVG(svg, tempFileWriter);
        tempFileWriter.close();

        showSVG(tempfile.toURI());
    }

    public static void showSVG(URI path) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(path);
        } catch (UnsupportedOperationException ex) {
            desktop.open(new File(path));
        }
    }

    public static void saveSVG(SVGGraphics2D svg, FileWriter fileWriter) throws IOException {
        fileWriter.write(svg.getSVGDocument());
        fileWriter.flush();
    }
}
