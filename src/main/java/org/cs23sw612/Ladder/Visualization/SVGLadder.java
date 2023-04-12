package org.cs23sw612.Ladder.Visualization;

import org.jfree.svg.SVGGraphics2D;

import java.util.ArrayList;
import java.util.List;

public class SVGLadder {

    public List<SVGRung> rungs;

    public SVGLadder(ArrayList<SVGRung> rungs) {
        this.rungs = rungs;
    }

    public void draw(SVGGraphics2D svg){
        rungs.forEach(rung -> rung.draw(svg));
    }

    public static class Builder{
        private final ArrayList<SVGRung.Builder> rungBuilders = new ArrayList<>();
        private double height = 0;
        private final SVGGraphics2D svg;

        public Builder(SVGGraphics2D svg){
            this.svg = svg;
        }

        public Double reserveHeight(){
            height += Visualizer.RUNG_HEIGHT;
            return height;
        }
        /*'

        public SVGRung.Builder buildRung(){
            var rungBuilder = new SVGRung.Builder(svg)
                    .withHeight(this.reserveHeight());

            rungBuilders.add(rungBuilder);
            return rungBuilder;
        }

        public SVGRung.Builder buildOrRung(){
            var height = this.reserveHeight();
            var p1 = new Point2D.Double(Visualizer.GATE_WIDTH/2,height);
            var length = rungBuilders.get(rungBuilders.size()-1).contacts
                    .stream()
                    .map(contact -> contact.WIDTH)
                    .reduce(0d, Double::sum);
            var p2 = new Point2D.Double(length + Visualizer.GATE_WIDTH/2,height);

            var rungBuilder = new SVGRung.Builder(svg)
                    .withHeight(height)
                    .attachAt(p1, p2);

            rungBuilders.add(rungBuilder);
            return rungBuilder;
        }
         */

        public SVGLadder build(){
            var rungList = new ArrayList<SVGRung>();
            rungBuilders.forEach(rungBuilder -> {
                rungList.add(rungBuilder.build());
            });
            return new SVGLadder(rungList);
        }

        public Builder withRungBuilder(SVGRung.Builder rungBuilder) {
            rungBuilders.add(rungBuilder);
            return this;
        }
    }
}
