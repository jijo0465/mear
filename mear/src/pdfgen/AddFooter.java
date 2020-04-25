package pdfgen;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
 

public class AddFooter implements IEventHandler{
	protected Document doc;
	
	public AddFooter(Document document) {
		this.doc=document;
	}
	@Override
	public void handleEvent(Event event) {
		System.out.println("Event Handler Called");
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
        Rectangle pageSize = docEvent.getPage().getPageSize();
//        doc.add(new Paragraph("Footer"));
        
		
	}

}
