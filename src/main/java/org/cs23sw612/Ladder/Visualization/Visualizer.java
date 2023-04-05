package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Visualizer {
    private static final float SPACING = 30;

    public static SVGGraphics2D layoutSVG(Ladder ladder){
        var svg = new SVGGraphics2D(1920, 1080, SVGUnits.PX);

        for (int i = 0; i < ladder.rungs.size(); i++) {

            var gateList = new ArrayList<SVGRungElement>();
            var next = i+1 < ladder.rungs.size()? ladder.rungs.get(i+1) : null;
            var isOrRung = ladder.rungs.get(i) instanceof Ladder.ORRung;
            var height = RUNG_HEIGHT * (i+1);
            /*
            if(hasOrRung){
                gateList.add(new SVGJunction(0, RUNG_HEIGHT*i));
            }
             */

            ArrayList<Ladder.Gate> gates = ladder.rungs.get(i).gates;
            int j = 0;
            for (; j < gates.size(); j++) {
                Ladder.Gate gate = gates.get(j);

                gateList.add(gateToSvg(gate, (j+1) * (GATE_WIDTH + SPACING), height));
            }

            /*
            if(hasOrRung){
                gateList.add(new SVGJunction(j, RUNG_HEIGHT*i));
            }
            */
            var point = new Point2D.Double(0,0);
            Point2D.Double endpoint = null;
            if (isOrRung){
                point.x = GATE_WIDTH/2;
                point.y = RUNG_HEIGHT*i;
                endpoint = new Point2D.Double((GATE_WIDTH+SPACING)*(gateList.size()+1) + GATE_WIDTH/2, height - RUNG_HEIGHT);

            }
            var svgRung = new SVGRung(height, point, endpoint, gateList);

            //drawRung(svg, layoutRung(ladder.rungs.get(i), (i+1) * RUNG_HEIGHT));
            drawRung(svg, svgRung);

        }


        return svg;
    }

    private static void drawRung(SVGGraphics2D svg, SVGRung rung) {
        rung.draw(svg);
    }


    public static float GATE_WIDTH = 20f;
    public static float RUNG_HEIGHT = 50f;

    public static SVGRung layoutRung(Ladder.Rung rung, float height){

        var gateList = new ArrayList<SVGRungElement>();
        ArrayList<Ladder.Gate> gates = rung.gates;
        for (int i = 0; i < gates.size(); i++) {
            Ladder.Gate gate = gates.get(i);

            gateList.add(gateToSvg(gate, i * GATE_WIDTH, height));
        }


        return null; //new SVGRung(height, gateList);
    }

    private static SVGRungElement gateToSvg(Ladder.Gate gate, float x, float y){
        if (gate instanceof Ladder.NOC)
            return new SVGNOC(x, y);
        else if (gate instanceof Ladder.NCC) {
            return new SVGNCC(x, y);
        }
        else throw new RuntimeException("Unexpexted gate type");
    }

}

