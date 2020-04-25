package pdfgen;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;

public class RoundedCell extends TableRenderer {
	double height;
	double width;
    public RoundedCell(Table modelElement) {
    	super(modelElement);
//    	height = modelElement.getHeight().getValue();
//    	modelElement.setHeight(30);
//        width = modelElement.getWidth().getValue();
    }

    @Override
    public void draw(DrawContext drawContext) {
    	PdfCanvas canvas = drawContext.getCanvas();
    	double x = getOccupiedAreaBBox().getX();
    	double y = getOccupiedAreaBBox().getY();
    	System.out.println("Height : "+width);
    	canvas.roundRectangle(getOccupiedAreaBBox().getX(), getOccupiedAreaBBox().getY(),
                getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight(), 4);
//    	canvas.moveTo(x-10, y+10);
//    	canvas.lineTo(x+18, y+10).lineTo(x+18, y+15).lineTo(x-10, y+15).lineTo(x-10, y+10).clip();
        drawContext.getCanvas().setStrokeColor(ColorConstants.BLACK).stroke();
        super.draw(drawContext);
    }
}
