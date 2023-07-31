package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.NewLadder;
import org.cs23sw612.Ladder.Rungs.CompositeRung;
import org.cs23sw612.Ladder.Rungs.NewRung;
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

public class NewVisualizer {
    
    public static void addGate(NewRung rung, ArrayList<NewSVGRungElement> gateSequence, double x, double y){
        if (rung instanceof SimpleRung sRung){
            var gate = new NewSVGGate(x,y, sRung.label, sRung.open);
            gateSequence.add(gate);

            if (sRung.followingRungs.size()==1){
                addGate(sRung.followingRungs.get(0) ,gateSequence, x+1, y);
            }

            if (sRung.followingRungs.size() > 1){
                for (var i = 1; i <= sRung.followingRungs.size(); i++) {
                    addGate(sRung.followingRungs.get(i), gateSequence, x+1, y+i);
                }
            }

        } else if (rung instanceof CompositeRung cRung) {
            var gate1 = new NewSVGGate(x,y, cRung.label, true);
            gateSequence.add(gate1);

            addGate(cRung.right, gateSequence, x+1, y);

            var gate2 = new NewSVGGate(x,y+1, cRung.label, false);
            gateSequence.add(gate2);

            addGate(cRung.left, gateSequence, x+1, y+1);
        }
    }


    public static SVGGraphics2D layoutSVG(NewLadder ladder) {
        var svg = new SVGGraphics2D(1000,1500, SVGUnits.PX); //Todo: fix this to generate size based on ladder dimensions
        double currentNumberOfRungs = 0;
        ArrayList<NewSVGRung> rungs = new ArrayList<>();

        for (Map.Entry<List<String>, NewRung> listNewRungEntry : ladder.gates.entrySet()) {
            //construct a list of SVGgates for this rung.
            NewRung rung = listNewRungEntry.getValue();
            ArrayList<NewSVGRungElement> gateSequence = new ArrayList<>();
            addGate(rung, gateSequence, 1, currentNumberOfRungs+1);

            //construct a list of SVGcoils for this rung.
            ArrayList<NewSVGRungElement> outputSequence = new ArrayList<>();
            double i = 0;
            for (String coilLabel : listNewRungEntry.getKey()){
                i++;
                outputSequence.add(new NewSVGCoil(6.0, currentNumberOfRungs+i, coilLabel)); //todo fix hvordan vi finder ud af antallet af gates på samme rung
            }                                                                                       //så vi kan beregne hvornår outputs skal tegnes. ATM harcoded to 6.

            //Update current number of rungs.
            if (gateSequence.get(gateSequence.size()-1).rungNumber < currentNumberOfRungs + i){
                currentNumberOfRungs += i;
            }
            else {
                currentNumberOfRungs = gateSequence.get(gateSequence.size()-1).rungNumber;
            }

            rungs.add(new NewSVGRung(outputSequence, gateSequence));
        }

        for (Map.Entry<String, String> stringEntry : ladder.stateUpd.entrySet()) {
            ArrayList<NewSVGRungElement> gate = new ArrayList<>();
            gate.add(new NewSVGGate(1., currentNumberOfRungs+1, stringEntry.getValue(), true));

            ArrayList<NewSVGRungElement> coil = new ArrayList<>();
            coil.add(new NewSVGCoil(6., currentNumberOfRungs+1, stringEntry.getKey()));

            currentNumberOfRungs += 1;
            rungs.add(new NewSVGRung(coil, gate));
        }
        drawRungs(rungs, svg);

        return svg;
    }

    public static void drawRungs(ArrayList<NewSVGRung> rungs, SVGGraphics2D svg){
        var path = new Path2D.Double();
        for (var rung: rungs) {
            rung.draw(svg);
        }

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
    }
}
