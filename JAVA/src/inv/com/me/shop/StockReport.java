/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inv.com.me.shop;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* @author shantanu */
public class StockReport extends javax.swing.JFrame {

    String path = "";

    /**
     * Creates new form StockReport
     */
    public StockReport() {
        initComponents();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        dir = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(680, 400));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stock Report", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 3, 18))); // NOI18N

        ok.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ok.setText("Save Stock Report as PDF");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        ok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okKeyPressed(evt);
            }
        });

        dir.setText("Not Chosen ...");

        jButton1.setText("Choose Directory");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(133, Short.MAX_VALUE)
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(dir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(ok)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed

        if (path.equals("") || path.equals("cancelled")) {
            AlertMessage msg = new AlertMessage("Please select directory to save the report");
        } else {
            JSONArray mainArray = JsonClass.getJsonArray(Constants.STOCK_FILE);

            //[{"carret":"22","unit":"gram","salePrice":"20","name":"Payal","id":1,"purchasePrice":"10","remaining":8.0}
            JSONParser parser = new JSONParser();
            Object o;
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(path+"/stock_report.pdf"));                    //change the name of the file according to the cudtomer id
                document.open();

                SimpleDateFormat y = new SimpleDateFormat("dd/MM/yyyy");
                Date d = Calendar.getInstance().getTime();
                Paragraph address = new Paragraph("Stock Report as on " + y.format(d), FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.ITALIC, BaseColor.BLACK));
                address.setAlignment(Element.ALIGN_CENTER);
                document.add(address);

                document.add(new Paragraph(" "));

                float[] columnWidths = new float[]{2f, 4f, 3f, 2f, 4f, 3f, 3f};
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                table.setWidths(columnWidths);

                table.addCell("Stock id");
                table.addCell("Item Name");
                table.addCell("Unit");
                table.addCell("Carret");
                table.addCell("Purchase Price");
                table.addCell("Sale Price");
                table.addCell("Availability");

                for (int i = 0; i < mainArray.size(); i++) {

                    o = parser.parse(mainArray.get(i).toString());
                    JSONObject obj = (JSONObject) o;
                    String carret = obj.get("carret").toString();
                    String unit = obj.get("unit").toString();
                    String sp = obj.get("salePrice").toString();
                    String name = obj.get("name").toString();
                    String id = obj.get("id").toString();
                    String pp = obj.get("purchasePrice").toString();
                    String remaining = obj.get("remaining").toString();

                    PdfPCell c = new PdfPCell();
                    c.addElement(new Paragraph(id));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);

                    table.addCell(c);
                    table.addCell(name);
                    table.addCell(unit);
                    table.addCell(carret);
                    table.addCell(pp);
                    table.addCell(sp);
                    table.addCell(remaining);
                }
                document.add(table);
                document.close();

            } catch (DocumentException ex) {
                Logger.getLogger(StockReport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StockReport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(StockReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
             AlertMessage msg = new AlertMessage("Report saved - "+path);
            
        }


    }//GEN-LAST:event_okActionPerformed

    private void okKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            ok.doClick();
        }
    }//GEN-LAST:event_okKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        path = Helper.choseDir();

        dir.setText(path);

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dir;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton ok;
    // End of variables declaration//GEN-END:variables
}
