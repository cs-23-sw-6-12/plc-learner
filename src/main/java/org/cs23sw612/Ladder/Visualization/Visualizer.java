package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Visualizer {
    public static SVGGraphics2D layoutSVG(Ladder ladder){
        var svg = new SVGGraphics2D(1920, 1080);

        var first = ladder.rungs.stream().findFirst();
        var rung = layoutRung(first.get());

        drawRung(svg, rung);

        return svg;
    }

    private static void drawRung(SVGGraphics2D svg, SVGRung rung) {
        rung.draw(svg);
    }

    public static float RUNG_HEIGHT = 100f;
    public static float GATE_WIDTH = 50f;

    public static SVGRung layoutRung(Ladder.Rung rung){

        var gateList = new ArrayList<SVGGate>();
        ArrayList<Ladder.Gate> gates = rung.gates;
        for (int i = 0; i < gates.size(); i++) {
            Ladder.Gate gate = gates.get(i);
            gateList.add(gateToSvg(gate, i * GATE_WIDTH, RUNG_HEIGHT));
        }

        return new SVGRung(RUNG_HEIGHT, gateList);
    }

    private static SVGGate gateToSvg(Ladder.Gate gate, float x, float y){
        GateType type = null;
        if (gate instanceof Ladder.NOC)
            type=GateType.NOC;
        else if (gate instanceof Ladder.NCC) {
            type=GateType.NCC;
        }
        if(type == null)
            throw new RuntimeException("Unexpexted gate type");

        return new SVGGate(type, x, y);
    }

    public record SVGRung(float height, List<SVGGate> gates){
        public void draw(SVGGraphics2D svg) {
            //draw start of line.

            var path = new Path2D.Double();
            path.moveTo(0, RUNG_HEIGHT);
            for (SVGGate gate : gates){
                path.lineTo(gate.x, gate.y);
                path.append(gate.getShape(), true);
            }
            svg.draw(path);
        }
    }

    public record SVGGate(GateType type, float x, float y){
        public Shape getShape(){
            Path2D path = new Path2D.Double();
            path.moveTo(x,y);
            path.lineTo(x+5,y);
            path.moveTo(x+5, y-15);
            path.lineTo(x+5, y+15);
            path.moveTo(x+15, y-15);
            path.lineTo(x+15, y+15);
            path.moveTo(x+15, y);
            path.lineTo(x+20, y);
            return path;
        }
    }

    public enum GateType{
        NOC,
        NCC
    }
}

