/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author KEERTHI
 */
package utils;

import models.CartItem;
import models.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillGenerator {

    /**
     * Generate PDF Bill using iText 5.5.13
     */
    public static String generatePdfBill(int orderId, User user, List<CartItem> items, double total) {
        try {
            String fileName = "Bill_Order_" + orderId + ".pdf";
            
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            // ============ HEADER ============
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(52, 152, 219));
            Paragraph title = new Paragraph("️  SHOPVIBE INVOICE\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            Font subFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
            Paragraph subtitle = new Paragraph(
                    "Order #" + orderId + " | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    subFont
            );
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            
            document.add(new Paragraph("\n"));
            
            // ============ DIVIDER ============
            Paragraph divider1 = new Paragraph("__________________________________________________________________________________");
            divider1.setAlignment(Element.ALIGN_CENTER);
            document.add(divider1);
            document.add(new Paragraph("\n"));
            
            // ============ CUSTOMER INFORMATION ============
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(44, 62, 80));
            Paragraph custTitle = new Paragraph(" CUSTOMER INFORMATION", sectionFont);
            document.add(custTitle);
            
            PdfPTable custTable = new PdfPTable(2);
            custTable.setWidthPercentage(50);
            
            addCustomerInfoRow(custTable, "Name:", user.getUsername());
            addCustomerInfoRow(custTable, "Email:", user.getEmail());
            addCustomerInfoRow(custTable, "Phone:", user.getPhone() != null ? user.getPhone() : "N/A");
            addCustomerInfoRow(custTable, "Address:", user.getAddress() != null ? user.getAddress() : "N/A");
            
            document.add(custTable);
            document.add(new Paragraph("\n"));
            
            // ============ DIVIDER ============
            Paragraph divider2 = new Paragraph("__________________________________________________________________________________");
            divider2.setAlignment(Element.ALIGN_CENTER);
            document.add(divider2);
            document.add(new Paragraph("\n"));
            
            // ============ ORDER ITEMS TABLE ============
            Paragraph itemsTitle = new Paragraph(" ORDER ITEMS", sectionFont);
            document.add(itemsTitle);
            document.add(new Paragraph("\n"));
            
            PdfPTable itemsTable = new PdfPTable(4);
            itemsTable.setWidthPercentage(100);
            float[] columnWidths = {3, 1, 1.5f, 1.5f};
            itemsTable.setWidths(columnWidths);
            
            // Header Row
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            BaseColor headerBgColor = new BaseColor(52, 152, 219);
            
            addTableHeaderCell(itemsTable, "Product Name", headerFont, headerBgColor);
            addTableHeaderCell(itemsTable, "Qty", headerFont, headerBgColor);
            addTableHeaderCell(itemsTable, "Unit Price", headerFont, headerBgColor);
            addTableHeaderCell(itemsTable, "Subtotal", headerFont, headerBgColor);
            
            // Data Rows
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            for (CartItem item : items) {
                addTableDataCell(itemsTable, item.getProduct().getName(), dataFont);
                addTableDataCell(itemsTable, String.valueOf(item.getQuantity()), dataFont);
                addTableDataCell(itemsTable, "Rs." + String.format("%.2f", item.getProduct().getPrice()), dataFont);
                addTableDataCell(itemsTable, "Rs." + String.format("%.2f", item.getSubtotal()), dataFont);
            }
            
            document.add(itemsTable);
            document.add(new Paragraph("\n"));
            
            // ============ TOTAL SECTION ============
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            
            Font totalLabelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
            BaseColor totalBgColor = new BaseColor(52, 152, 219);
            
            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("TOTAL AMOUNT", totalLabelFont));
            totalLabelCell.setBackgroundColor(totalBgColor);
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalLabelCell.setPadding(10);
            totalTable.addCell(totalLabelCell);
            
            PdfPCell totalAmountCell = new PdfPCell(new Paragraph("Rs." + String.format("%.2f", total), totalLabelFont));
            totalAmountCell.setBackgroundColor(totalBgColor);
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalAmountCell.setPadding(10);
            totalTable.addCell(totalAmountCell);
            
            document.add(totalTable);
            document.add(new Paragraph("\n"));
            
            // ============ FOOTER ============
            Paragraph divider3 = new Paragraph("__________________________________________________________________________________");
            divider3.setAlignment(Element.ALIGN_CENTER);
            document.add(divider3);
            
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Thank you for shopping with us! 🎉\n" +
                    "This is an automated invoice. No signature required.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(15);
            document.add(footer);
            
            document.close();
            
            System.out.println("✓ PDF Bill generated: " + fileName);
            
            // Open PDF automatically
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(fileName));
            } catch (Exception e) {
                System.out.println("⚠️ PDF saved as: " + fileName);
            }
            
            return fileName;
            
        } catch (DocumentException | FileNotFoundException ex) {
            System.err.println("✗ Error generating PDF bill: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    // Helper method to add customer info row
    private static void addCustomerInfoRow(PdfPTable table, String label, String value) {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, labelFont));
        labelCell.setBorder(PdfPCell.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Paragraph(value, valueFont));
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }
    
    // Helper method to add header cell
    private static void addTableHeaderCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        table.addCell(cell);
    }
    
    // Helper method to add data cell
    private static void addTableDataCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(6);
        cell.setBorder(PdfPCell.BOX);
        table.addCell(cell);
    }
}