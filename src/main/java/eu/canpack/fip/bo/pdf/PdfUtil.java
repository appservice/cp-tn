package eu.canpack.fip.bo.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CP S.A.
 * Created by lukasz.mochel on 21.08.2017.
 */
public class PdfUtil {
    private static final Logger log = LoggerFactory.getLogger(PdfUtil.class);


    static BaseFont getArialUnicodeBaseFont() {
        try {
            return BaseFont.createFont("/fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);//"Cp1250"

        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    static Image getImageLogo(Rectangle documentSize) {
        try {
            Image image = Image.getInstance(PdfUtil.class.getResource("/images/logo.jpg"));
            image.setAbsolutePosition(documentSize.getWidth() - 120, documentSize.getHeight() - 90);
            image.scalePercent(50);
            return image;

        } catch (BadElementException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    static String formatDate(ZonedDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

}


