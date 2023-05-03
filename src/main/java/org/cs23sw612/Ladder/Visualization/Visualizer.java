package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.SVG.*;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;

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

    private static double rung_length = 0;
    private static int currentRungNumber = 1;

    public static SVGGraphics2D layoutSVG(Ladder ladder) {
        var svg = GenerateNewSVGFromLadderDimensions(ladder);

        for (Ladder.Rung rung : ladder.rungs) {
            addRungsToSVG(rung.gates, svg, true, rung.outputGates);
            for (Ladder.GateSequence orRung : rung.orRungs) {
                addRungsToSVG(orRung, svg, false, null);
            }
        }
        return svg;
    }

    private static SVGGraphics2D GenerateNewSVGFromLadderDimensions(Ladder ladder) {
        int numberOfOrRungs = 0;
        for (Ladder.Rung rung : ladder.rungs) {
            numberOfOrRungs += Math.max(rung.orRungs.size(), rung.outputGates.size());
        }
        double ladder_height = (ladder.rungs.size() + numberOfOrRungs) * (RUNG_HEIGHT + V_SPACING * 2) + V_SPACING;

        rung_length = 0;
        for (Ladder.Rung rung : ladder.rungs) {
            double len = rung.gates.count() * (GATE_WIDTH + H_SPACING) + END_SPACING_Y + GATE_WIDTH;
            if (rung_length < len)
                rung_length = len;
        }
        return new SVGGraphics2D(rung_length + END_SPACING_X, ladder_height, SVGUnits.PX);
    }

    private static void addRungsToSVG(Ladder.GateSequence gateSequence, SVGGraphics2D svg, boolean mainRung,
            LinkedHashSet<Ladder.Gate> outputGates) {
        double height = (V_SPACING * 2 + RUNG_HEIGHT) * currentRungNumber;
        ArrayList<SVGRungElement> gates = addGateSequence(gateSequence, height);

        Point2D.Double point = new Point2D.Double(0, 0);
        ArrayList<SVGRungElement> coils = null;
        Point2D.Double endpoint = null;

        if (mainRung) {
            coils = addOutputCoils(outputGates, height);
        } else {
            point.x = GATE_WIDTH / 2;
            point.y = height - RUNG_HEIGHT - V_SPACING * 2;
            endpoint = new Point2D.Double((GATE_WIDTH + H_SPACING) * (gates.size() + 1) + GATE_WIDTH / 2, point.y);
        }

        new SVGRung(height, point, endpoint, coils, gates).draw(svg);
        currentRungNumber++;
    }

    private static ArrayList<SVGRungElement> addGateSequence(Ladder.GateSequence gateSequence, double height) {
        var gates = new ArrayList<SVGRungElement>();

        for (int i = 0; i < gateSequence.count(); i++) {
            Ladder.Gate ladderGate = gateSequence.get(i);
            SVGRungElement svgGate;
            double x = (i + 1) * (GATE_WIDTH + H_SPACING);
            svgGate = new SVGGate(x, height, ladderGate.gate, ladderGate.isOpen());
            gates.add(svgGate);
        }
        return gates;
    }

    private static ArrayList<SVGRungElement> addOutputCoils(LinkedHashSet<Ladder.Gate> outputGates, double height) {
        ArrayList<SVGRungElement> coils = new ArrayList<>();
        for (int i = 0; i < outputGates.size(); i++) {
            Ladder.Gate outputGate = outputGates.stream().toList().get(i);
            double y = height + (RUNG_HEIGHT + V_SPACING * 2) * i;
            SVGRungElement svgCoil = new SVGCoil(rung_length - GATE_WIDTH, y, outputGate.gate);
            coils.add(svgCoil);
        }
        return coils;
    }

    /**
     * Show an svg in the browser or, if that fails, your default svg-application.
     *
     * @param svg
     *            The svg to display.
     * @throws IOException
     *             If the SVG file could not be created.
     */
    public static void showSVG(SVGGraphics2D svg) throws IOException {
        var svgDocumentString = svg.getSVGDocument();
        var imageURI = "data:image/svg+xml;charset=utf-8;base64,"
                + Base64.getEncoder().encodeToString(svgDocumentString.getBytes(StandardCharsets.UTF_8));

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
    }
}
