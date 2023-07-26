package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.NewLadder;
import org.cs23sw612.Ladder.Rungs.CompositeRung;
import org.cs23sw612.Ladder.Rungs.NewRung;
import org.cs23sw612.Ladder.Rungs.SimpleRung;
import org.cs23sw612.Ladder.Visualization.SVG.SVGCoil;
import org.cs23sw612.Ladder.Visualization.SVG.SVGGate;
import org.cs23sw612.Ladder.Visualization.SVG.SVGRungElement;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewVisualizer {

    public static ArrayList<SVGRungElement> NewAddOutputCoils(List<String> outputCoils){
        ArrayList<SVGRungElement> Coils = new ArrayList<>();
        for(var c : outputCoils){
            SVGCoil coil = new SVGCoil(1.0,2.0, c);
            Coils.add(coil);
        }
        return Coils;
    }

    public static ArrayList<SVGRungElement> NewAddGates(NewRung rung){
        ArrayList<SVGRungElement> gates = new ArrayList<>();
        if(rung instanceof SimpleRung sRung){
            System.out.println("sRung label: " + sRung.label);
            SVGGate gate = new SVGGate(1.0,2.0,  sRung.label, sRung.open);
            gates.add(gate);
            if (sRung.followingRungs.size() >= 1){
                for (var r : sRung.followingRungs) {
                    gates.addAll(NewAddGates(r));
                }
            }
        }
        if(rung instanceof CompositeRung cRung){
            if (cRung.left != null){
                gates.add(new SVGGate(1.0,2.0, cRung.label, false));//todo confirm open boolean
                gates.addAll(NewAddGates(cRung.left));
                System.out.println("cRung label:  " + cRung.label);
            }
            if (cRung.right != null){
                gates.add(new SVGGate(1.0,2.0, cRung.label, true));
                gates.addAll(NewAddGates(cRung.right));
            }
        }
        /*else if(rung instanceof CompositeRung cRung){
            gates.add(new SVGGate(1.0,2.0, cRung.label, true));
            if(cRung.left != null){

            }
        }*/
        return gates;
    }

    public static SVGGraphics2D layoutSVG(NewLadder ladder) {
        var svg = new SVGGraphics2D(1000,1500, SVGUnits.PX); //Todo: fix this to generate size based on ladder dimensions
        System.out.println("no?");
        for (Map.Entry<List<String>, NewRung> listNewRungEntry : ladder.gates.entrySet()) {

            for (var x : listNewRungEntry.getKey()){
                System.out.println("key:" + x); //output gates k.list
            }
            var rung = listNewRungEntry.getValue();
            ArrayList<SVGRungElement> gates = NewAddGates(rung);
            System.out.println("value:" + listNewRungEntry.getValue());
            System.out.println("PLZ: " + listNewRungEntry);
            //ArrayList<SVGRungElement> SVGgates = new ArrayList<>();

            /*if (rung instanceof SimpleRung) {
                double height = (V_SPACING * 2 + RUNG_HEIGHT) * currentRungNumber;
                double x = GATE_WIDTH + H_SPACING;
                SVGgates.add(new SVGGate(x, height, ((SimpleRung) rung).label,((SimpleRung) rung).open));
                for (int y=0; y <= ((SimpleRung) rung).followingRungs.size(); ++y){
                    System.out.println("hej" + y);
            }
            }*/
        }


        /*for (var x : ladder.gates) {
            addRungsToSVG(rung.gates, svg, true, rung.outputGates);
            for (Ladder.GateSequence orRung : rung.orRungs) {
                addRungsToSVG(orRung, svg, false, null);
            }
        }*/

        return svg;
    }
}
