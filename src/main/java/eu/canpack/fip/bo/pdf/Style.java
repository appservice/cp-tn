package eu.canpack.fip.bo.pdf;


import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;

import java.awt.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 20.08.2017.
 */



    public class Style {

        public static void headerCellStyle(PdfPCell cell){

            // alignment
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // padding
            cell.setPaddingTop(0f);
            cell.setPaddingBottom(7f);

            // background color
            cell.setBackgroundColor(new Color(0, 121, 182));

            // border
            cell.setBorder(0);
            cell.setBorderWidthBottom(2f);

        }
        public static void labelCellStyle(PdfPCell cell){
            // alignment
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // padding
//            cell.setPaddingLeft(3f);
//            cell.setPaddingTop(3f);
            cell.setPadding(3f);
            cell.setPaddingTop(7f);
            cell.setPaddingBottom(7f);

            // background color
            cell.setBackgroundColor(new Color(238,236,226));

            // border
//            cell.setBorder(1);
//            cell.setBorderWidthBottom(1);
            cell.setBorderColorBottom(Color.BLACK);

            // height
            cell.setMinimumHeight(18f);
        }

        public static void label2CellStyle(PdfPCell cell){
            // alignment
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // padding
//            cell.setPaddingLeft(3f);
//            cell.setPaddingTop(3f);
            cell.setPadding(3f);
            cell.setPaddingTop(7f);
            cell.setPaddingBottom(7f);

            // background color
            cell.setBackgroundColor(Color.WHITE);

            // border
//            cell.setBorder(1);
//            cell.setBorderWidthBottom(1);
            cell.setBorder(0);
            cell.setBorderWidthTop(1f);
            cell.setBorderColorBottom(Color.BLACK);
//            cell.setBorderWidthRight(0.5f);
//            cell.setBorderWidthLeft(0.5f);
            cell.setBorderWidthBottom(0);

            // height
            cell.setMinimumHeight(18f);
        }

        public static void valueCellStyle(PdfPCell cell){
            // alignment
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // padding
            cell.setPaddingTop(5f);
            cell.setPaddingBottom(5f);

            // border
//            cell.setBorder(1);
//            cell.setBorderWidthBottom(0.5f);

            // height
            cell.setMinimumHeight(18f);
        }

        public static void value2CellStyle(PdfPCell cell){
            // alignment
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // padding
            cell.setPaddingTop(10f);
            cell.setPaddingBottom(10f);

            // border
            cell.setBorder(0);
            cell.setBorderWidthBottom(1);
//            cell.setBorderWidthRight(0.5f);
//           cell.setBorderWidthLeft(0.5f);
//            cell.setBorder(1);
//            cell.setBorderWidthBottom(0.5f);

            // height
            cell.setMinimumHeight(18f);
        }
    }

