package pdfgen;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;

public class AddInvoiceHeaderFooter implements IEventHandler{
	protected Document doc;
	private Customer customer;
	private Invoice invoice;
	private AppTrn appTrn;
	private InvoiceNew taxInvoice;
	private String doNo;
	private String appName;
	Font boldFont;
	Font normalFont;
	boolean addHeader;
	public AddInvoiceHeaderFooter(String appName,Document document,Customer customer,Invoice invoice,AppTrn appTrn, boolean addHeader, String doNo,InvoiceNew invoiceNew){
		this.doNo = doNo;
		this.taxInvoice = invoiceNew;
		this.appTrn = appTrn;
		this.customer = customer;
		this.appName = appName;
		this.invoice = invoice;
		this.boldFont = new Font(Font.SANS_SERIF, 18, Font.BOLD);
		this.normalFont = new Font(Font.SANS_SERIF, 18, Font.PLAIN);
		this.doc=document;
		this.addHeader = addHeader;
	}
	@Override
	public void handleEvent(Event event) {
		ImageData backgroundImage = null;
		try {
			backgroundImage = ImageDataFactory.create("/app/tomcat/images/"+appName+".png");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		};
//		ImageData footerImage = null;
//		try {
//			footerImage = ImageDataFactory.create("/app/tomcat/images/footer.png");
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		};

		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
        Rectangle pageSize = docEvent.getPage().getPageSize();
        if(addHeader)
        	canvas.addImage(backgroundImage, new Rectangle(0, pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight()), true);
//        canvas.addImage(footerImage, new Rectangle(0, 0, pageSize.getWidth(), 65), true);
//        canvas.beginText();
//        try {
//            canvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD), 11);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        canvas.moveText(doc.getLeftMargin(), pageSize.getHeight()-175)
//    	.showText("Quotation No : "+invoice.getQuatationNo())
//        .moveText(doc.getLeftMargin(), pageSize.getHeight()-175)
//        	.showText("Quotation No : "+invoice.getQuatationNo())
//        	.endText();
        Rectangle rect =new Rectangle(0+doc.getLeftMargin(), pageSize.getTop()-321, pageSize.getWidth()-36, 200);
        canvas.setStrokeColor(ColorConstants.WHITE)
        	.rectangle(rect)
        	.stroke();
        Canvas can = new Canvas(canvas, canvas.getDocument(), rect);
        try {
        	can.add(getHeaderTable());
			can.add(getCustomerTable());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        can.close();
//        canvas.moveText((pageSize.getRight() - doc.getRightMargin() - (pageSize.getLeft() + doc.getLeftMargin())) / 2 + doc.getLeftMargin(), pageSize.getTop() - doc.getTopMargin() + 10)
//                .showText("this is a header")
//                .moveText(0, (pageSize.getBottom() + doc.getBottomMargin()) - (pageSize.getTop() + doc.getTopMargin()) - 20)
//                .showText("this is a footer")
//                .endText()
//                .release();
//		
	}
	
	public Table getCustomerTable() throws IOException {
		Table customerTable = new Table(new float[]{1,1}).useAllAvailableWidth();
    	customerTable.setWidth(550);
    	
    	Table leftTable = new Table(new float[] {95,5,250});
//    	leftTable.setWidth(300);
    	
    	Table rightTable = new Table(new float[] {1,1}).useAllAvailableWidth();
//    	rightTable.setWidth(250);
    	
//    	leftTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
//    	rightTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    	
//    	customerTable.setBorder(Border.Side());
//    	leftTable.setBorder(Border.NO_BORDER);
//    	customerTable.setBorder(Border.NO_BORDER);
//    	rightTable.setBorder(Border.NO_BORDER);
    	
    	leftTable.addCell(addCell("Customer Name\r\n ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(addCell(": ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(new Cell().add(new Paragraph(new Text(customer.getName()+"\r\n").setFont(MearFonts.getFonts().TITLE_FONT)).setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)).setBorder(Border.NO_BORDER).setPaddingRight(30));
    	leftTable.addCell(addCell("Invoice Address\r\n ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(addCell(": ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(addCell(customer.getCustomerProfileInvAddr()+"\r\n",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER).setPaddingRight(30));
    	leftTable.addCell(addCell("Project Address\r\n ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(addCell(": ",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	leftTable.addCell(addCell(customer.getCustomerProfileProAddr()+"\r\n",TextAlignment.LEFT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER).setPaddingRight(30));
    	
    	
    	Cell leftCell = new Cell();
    	leftCell.setBorder(Border.NO_BORDER);
    	leftCell.add(leftTable);
    	customerTable.addCell(leftCell);
    	
    	
    	rightTable.addCell(addCell("LPO :\r\n ",TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	rightTable.addCell(addCell(invoice.getLpo(),TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	rightTable.addCell(addCell("TRN :\r\n ",TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	rightTable.addCell(addCell(customer.getTrn(),TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	rightTable.addCell(addCell("Contact :\r\n ",TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	rightTable.addCell(addCell(customer.getCustomerProfilePhone(),TextAlignment.RIGHT,VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
    	
    	Cell rightCell = new Cell();
    	rightCell.setBorder(Border.NO_BORDER);
    	rightCell.add(rightTable);
    	customerTable.addCell(rightCell);
    	return customerTable;
	}
    public Cell addCell(String text,TextAlignment textAlignment,VerticalAlignment alignment) {
    	Cell cell = new Cell();
//    	if(textAlignment == TextAlignment.RIGHT) {
//    		cell.setPaddingRight(8);
//    	}
    	cell.setTextAlignment(textAlignment);
    	Paragraph p = new Paragraph(new Text(text));
    	new MearFonts();
    	p.setFont(MearFonts.getFonts().SUBTITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(alignment);
    	return cell;
    }
    
    public Table getHeaderTable() {
    	Table headerTable = new Table(new float[] {1}).useAllAvailableWidth();
    	Table qTable = new Table(new float[] {1,1}).useAllAvailableWidth();
    	headerTable.addCell(new Cell().add(new Paragraph().setFont((MearFonts.getFonts().TITLE_FONT)).setFontSize(16).setTextAlignment(TextAlignment.CENTER).add(new Text("TAX INVOICE").setUnderline())).setBorder(Border.NO_BORDER));
    	headerTable.addCell(new Cell().add(new Paragraph("TRN : "+appTrn.getTrn()).setFont((MearFonts.getFonts().TITLE_FONT)).setFontSize(11).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
    	qTable.addCell(new Cell().add(new Paragraph("Invoice No : "+taxInvoice.getInvoiceNo().replace('-', '/')).setFont(MearFonts.getFonts().TITLE_FONT).setFontSize(11)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
    	qTable.addCell(new Cell().add(new Paragraph("Date : "+invoice.getCreatedDate()).setFont(MearFonts.getFonts().TITLE_FONT).setFontSize(11)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
    	qTable.addCell(new Cell().add(new Paragraph("Do No : "+doNo.replace('-', '/')).setFont(MearFonts.getFonts().SUBTITLE_FONT).setFontSize(11)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
    	qTable.addCell(new Cell().add(new Paragraph("Quo No : "+invoice.getQuatationNo().replace('-', '/')).setFont(MearFonts.getFonts().SUBTITLE_FONT).setFontSize(11)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
    	headerTable.addCell(new Cell().add(qTable).setBorder(Border.NO_BORDER));
    	return headerTable;
    	
    }

}
