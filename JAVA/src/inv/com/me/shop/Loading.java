/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inv.com.me.shop;

import javax.swing.SwingUtilities;

/* @author shantanu */
public class Loading extends javax.swing.JFrame {

    /**
     * Creates new form Loading
     */
    static final int MY_MINIMUM = 0;

    static final int MY_MAXIMUM = 100;
    public Loading() {
        //final Loading it = new Loading();
        initComponents();
        loadingBar.setMinimum(MY_MINIMUM);
        loadingBar.setMaximum(MY_MAXIMUM);
        setVisible(true);
        
        for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
            final int percent = i;
            try {
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  updateBar(percent);
                }
              });
              java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
              ;
            }
          }
    }
    public void updateBar(int newValue) {
        loadingBar.setValue(newValue);
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadingBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(loadingBar, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(loadingBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(147, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar loadingBar;
    // End of variables declaration//GEN-END:variables
}