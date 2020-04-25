package pdfgen;

import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class MearFonts {
	public PdfFont TITLE_FONT;
	public PdfFont SUBTITLE_FONT;
	
	public MearFonts() {
		try {
			TITLE_FONT = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
			SUBTITLE_FONT = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MearFonts getFonts() {
		return new MearFonts();
	}

}
