package org.cs23sw612.Ladder.Visualization;

import org.jfree.svg.SVGGraphics2D;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SVGRung {
    private final double height;
    private final List<SVGRungElement> gates;
    private final Point2D.Double attachmentPoint;
    private String coilString = "";
    private SVGRungElement coil;
    private Point2D.Double endAttachmentPoint = null;

    public SVGRung(double height, Point2D.Double attachmentPoint, List<SVGRungElement> gates) {
        this.height = height;
        this.gates = gates;
        this.attachmentPoint = attachmentPoint;
    }

    public SVGRung(double height, Point2D.Double attachmentPoint, Point2D.Double endAttachmentPoint, List<SVGRungElement> gates){
        this(height, attachmentPoint, gates);
        this.endAttachmentPoint = endAttachmentPoint;
    }

    public SVGRung(double height, Point2D.Double attachmentPoint, String Coil, List<SVGRungElement> gates){
        this(height, attachmentPoint, gates);
        this.coilString = Coil;
    }

    public SVGRung(double height, Point2D.Double startAttachmentPoint, Point2D.Double endAttachmentPoint, SVGRungElement coil, List<SVGRungElement> gates){
        this(height, startAttachmentPoint, endAttachmentPoint, gates);
        this.coil = coil;
    }

    public void draw(SVGGraphics2D svg) {
        //draw start of line.

        var path = new Path2D.Double();
        path.moveTo(attachmentPoint.x, attachmentPoint.y);
        path.lineTo(attachmentPoint.x, height);
        for (SVGRungElement svgRungElement : gates) {
            svg.drawString(svgRungElement.text, (float) (svgRungElement.x), (float) height -20);
            path.lineTo(svgRungElement.x, height);
            path.append(svgRungElement.getShape(), true);
        }
        if (endAttachmentPoint != null){
            path.lineTo(endAttachmentPoint.x, height);
            path.lineTo(endAttachmentPoint.x, endAttachmentPoint.y);
        }else{
            var coil = new SVGCoil(svg.getWidth() - SVGCoil.WIDTH, height, coilString);
            path.lineTo(coil.x, coil.y);
            path.append(coil.getShape(), true);
            svg.drawString(coil.text, (float) coil.x, (float)(coil.y-20));
        }
        svg.draw(path);
    }

    public static class Builder {
        public SVGGraphics2D svg;
        public List<Contact> contactList = new ArrayList<>();
        private double height;
        private boolean isOrRung = false;
        private double nextX;
        private Point2D.Double start = new Point2D.Double(0,0);
        private Point2D.Double end = null;


        public Builder(SVGGraphics2D svg){
            this.svg = svg;
        }

        public Builder attachAt(Point2D.Double start, Point2D.Double end){
            this.start = start;
            this.end = end;
            return this;
        }

        /**
         * If a rung is an OrRung, then the builder will automatically attach it
         * at the beginning and end of the previous rung.
         *
         * @return Builder.
         * */
        public Builder intoOrRung(){
            isOrRung = true;
            return this;
        }

        public Builder withNOC(String text){
            contactList.add(
                    new Contact(ContactType.NOC, text));

            return this;
        }

        public Builder withNCC(String text){
            contactList.add(
                    new Contact(ContactType.NOC, text));
            return this;
        }

        public Builder withCoil(String text){
            contactList.add(
                    new Contact(ContactType.Coil, text));
            return this;
        }

        private double reserveX(double width){
            var x = nextX;
            nextX += (width + Visualizer.SPACING);
            return x;
        }

        public SVGRung build(){

            var contacts = new ArrayList<SVGRungElement>();
            SVGRungElement coil = null;
            for (Contact contact: contactList) {
                SVGRungElement elem;
                switch (contact.type){
                    case NOC ->
                            contacts.add(new SVGNOC(reserveX(SVGNOC.WIDTH),height, contact.text));
                    case NCC ->
                            contacts.add(new SVGNCC(reserveX(SVGNOC.WIDTH), height, contact.text));
                    case Coil -> {
                        coil = new SVGCoil(reserveX(SVGCoil.WIDTH), height, contact.text);
                    }
                }
            }
            return new SVGRung(height, start, end, coil, contacts);
        }

        public Builder withHeight(Double height) {
            this.height = height;
            return this;
        }

        private record Contact(ContactType type, String text) {}
        private enum ContactType{
            NOC,
            NCC,
            Coil
        }
    }
}
