
package pdfgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;


public class CreateHeaderlessDo{
	Document document;
	PdfDocument pdfDoc;
	int tablePageNum;
	private Customer customer;
	private Invoice invoice;
	public CreateHeaderlessDo(String appName,String dest, Customer customer,Invoice invoice, AppTrn appTrn, String doDisplayNo) throws FileNotFoundException{
		this.customer = customer;
		this.invoice = invoice;
//		String dest = "/Invoices/invoice.pdf";
//		String dest=getServletConfig.getServletContext.getRealPath("/someFolder");
		File file = new File(dest);
		if(file.exists()) {
			file.delete();
		}
        file.getParentFile().mkdirs();
    	pdfDoc = new PdfDocument(new PdfWriter(dest));
    	pdfDoc.setDefaultPageSize(PageSize.A4);
    	document = new Document(pdfDoc);
    	document.setMargins(310, 18, 70, 18);
    	pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new AddDoHeaderFooter(appName,document,customer,invoice,appTrn,false,doDisplayNo));
//    	pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new AddFooter(document));
    	
	}

    
    public void create(List<InvoiceItem> invoiceData) throws FileNotFoundException {
    	
    	addInvoiceTable(invoiceData);
//    	addFooterTable();
    	int totalPages = pdfDoc.getNumberOfPages();
    	float currentMargin =  document.getBottomMargin();
    	while(totalPages!=tablePageNum) {
    		for(int i=totalPages;i>0;i--) {
    			pdfDoc.removePage(i);
    		}
    		document=new Document(pdfDoc);
    		document.setMargins(310, 18, currentMargin+20, 18);
    		addInvoiceTable(invoiceData);
//        	addFooterTable();
        	totalPages = pdfDoc.getNumberOfPages();
        	currentMargin = currentMargin+20;
    	}
    	document.close();

    }
    
    public void addInvoiceTable(List<InvoiceItem> invoiceData) {
    	Table table = new Table(new float[]{1,2,5,1,1});
    	table.setWidth(550);
    	table.addHeaderCell(addHeaderCell("No"));
    	table.addHeaderCell(addHeaderCell("Item"));
    	table.addHeaderCell(addHeaderCell("Description"));
    	table.addHeaderCell(addHeaderCell("UOM"));
    	table.addHeaderCell(addHeaderCell("Qty"));
    	Integer no = 1;
    	for(InvoiceItem i:invoiceData) {
    		table.addCell(addCell((no++).toString()));
    		table.addCell(addCell(i.getInventoryItemCode(),TextAlignment.LEFT));
    		table.addCell(addCell(i.getInventoryDescription(),TextAlignment.LEFT));
    		table.addCell(addCell(i.getUom()));
    		table.addCell(addCell(i.getQuantity()));
    	}
    	table.setHorizontalAlignment(HorizontalAlignment.CENTER);
    	document.add(table);
    	tablePageNum=pdfDoc.getNumberOfPages();
    }
    
//    public void addFooterTable() {
//    	Table footerTable = new Table(new float[] {1});
//    	Table priceTable = new Table(new float[] {1,1});
//    	Table priceMasterTable = new Table(new float[] {4,1});
//    	footerTable.setWidth(550);
//    	footerTable.setMarginTop(12);
//    	footerTable.setWidth(550);
//    	footerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
//    	priceTable.addCell(addPriceCell("SUB TOTAL",TextAlignment.RIGHT));
//    	priceTable.addCell(addPriceCell(invoice.getTotal(),TextAlignment.LEFT));
////    	priceTable.addCell(addPriceCell("DISCOUNT",TextAlignment.RIGHT));
////    	priceTable.addCell(addPriceCell(invoice.getDiscount(),TextAlignment.LEFT));
//    	priceTable.addCell(addPriceCell("VAT | 5%",TextAlignment.RIGHT));
//    	priceTable.addCell(addPriceCell(invoice.getVat(),TextAlignment.LEFT));
//    	priceTable.addCell(addPriceCellBold("TOTAL",TextAlignment.RIGHT));
//    	priceTable.addCell(addPriceCellBold(invoice.getNet(),TextAlignment.LEFT));
//    	priceTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
//    	Cell priceCell = new Cell().setWidth(200).setPadding(0).setBorder(Border.NO_BORDER);
//    	priceCell.add(priceTable);
//    	priceMasterTable.addCell(addPriceCellBold(invoice.getAmtInWords(),TextAlignment.CENTER).setBorder(Border.NO_BORDER));
//    	priceMasterTable.addCell(priceCell).setHorizontalAlignment(HorizontalAlignment.RIGHT);
//    	footerTable.addCell(addHeaderCell("Terms and Conditions",TextAlignment.LEFT).setBackgroundColor(ColorConstants.LIGHT_GRAY));
//    	footerTable.addCell(addCell(invoice.getTerms(),TextAlignment.LEFT));
//    	footerTable.addCell(new Cell().setBorder(Border.NO_BORDER));
//    	footerTable.addCell(new Cell().add(priceMasterTable).setPadding(0));
//    	document.add(footerTable);
//    }
    public Cell addCell(String text, TextAlignment alignment) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	if(alignment == TextAlignment.LEFT) {
    		cell.setPaddingLeft(8);
    	}
    	else if(alignment == TextAlignment.RIGHT) {
    		cell.setPaddingRight(8);
    	}
    	cell.setTextAlignment(alignment);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().SUBTITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	
    	return cell;
    }
    
    public Cell addCell(String text) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	cell.setTextAlignment(TextAlignment.CENTER);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().SUBTITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	return cell;
    }
    public Cell addCell(String text,TextAlignment textAlignment,VerticalAlignment alignment) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	if(textAlignment == TextAlignment.LEFT) {
    		cell.setPaddingLeft(8);
    	}
    	else if(textAlignment == TextAlignment.RIGHT) {
    		cell.setPaddingRight(8);
    	}
    	cell.setTextAlignment(textAlignment);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().SUBTITLE_FONT);
		p.setFontSize(11);
		cell.add(p);
    	cell.setVerticalAlignment(alignment);
    	return cell;
    }
    public Cell addHeaderCell(String text) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	cell.setTextAlignment(TextAlignment.CENTER);
    	cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().TITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	cell.setHeight(25);
    	return cell;
    	
    }
    public Cell addHeaderCell(String text, TextAlignment textAlignment) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	cell.setTextAlignment(textAlignment);
    	cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().TITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	cell.setHeight(25);
    	return cell;
    	
    }
    public Cell addPriceCell(String text,TextAlignment alignment) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	cell.setPaddingLeft(22);
    	cell.setPaddingRight(12);
    	cell.setTextAlignment(alignment);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().SUBTITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	return cell;
    }
    public Cell addPriceCellBold(String text,TextAlignment alignment) {
    	Cell cell = new Cell();
    	cell.setKeepTogether(true);
    	cell.setPaddingLeft(22);
    	cell.setPaddingRight(12);
    	cell.setTextAlignment(alignment);
    	Paragraph p = new Paragraph(text);
    	p.setFont(MearFonts.getFonts().TITLE_FONT);
		p.setFontSize(11);
    	cell.add(p);
    	cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    	return cell;
    }

}