/**
 Copyright 2011 Kevin J. Jones (http://www.kevinjjones.co.uk)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package uk.co.kevinjjones;

import java.awt.Container;
import javax.swing.JDialog;

public class OptionsDialog extends javax.swing.JPanel {

    /** Creates new form OptionsDialog */
    public OptionsDialog() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        DegF = new javax.swing.JRadioButton();
        this.DegF.setSelected(!RunManager.getInstance().isDegC());
        DegC = new javax.swing.JRadioButton();
        this.DegC.setSelected(RunManager.getInstance().isDegC());
        jPanel1 = new javax.swing.JPanel();
        KPA = new javax.swing.JRadioButton();
        this.KPA.setSelected(RunManager.getInstance().isKPA());
        PSI = new javax.swing.JRadioButton();
        this.PSI.setSelected(!RunManager.getInstance().isKPA());
        jPanel3 = new javax.swing.JPanel();
        KPH = new javax.swing.JRadioButton();
        this.KPH.setSelected(RunManager.getInstance().isKPH());
        MPH = new javax.swing.JRadioButton();
        this.MPH.setSelected(!RunManager.getInstance().isKPH());
        jPanel4 = new javax.swing.JPanel();
        Lambda = new javax.swing.JRadioButton();
        this.Lambda.setSelected(RunManager.getInstance().isLambda());
        AF = new javax.swing.JRadioButton();
        this.AF.setSelected(!RunManager.getInstance().isLambda());
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(200, 325));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Temperature"));
        jPanel2.setForeground(new java.awt.Color(179, 68, 68));

        buttonGroup1.add(DegF);
        DegF.setText("Deg F");
        DegF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DegFActionPerformed(evt);
            }
        });

        buttonGroup1.add(DegC);
        DegC.setText("Deg C");
        DegC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DegCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(DegC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DegF)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(DegC)
                .addComponent(DegF))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pressure"));
        jPanel1.setPreferredSize(new java.awt.Dimension(199, 49));
        jPanel1.setRequestFocusEnabled(false);

        buttonGroup2.add(KPA);
        KPA.setText("Bar/KPA");
        KPA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KPAActionPerformed(evt);
            }
        });

        buttonGroup2.add(PSI);
        PSI.setText("PSI");
        PSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PSIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(KPA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PSI)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(KPA)
                .addComponent(PSI))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Speed"));
        jPanel3.setPreferredSize(new java.awt.Dimension(199, 49));
        jPanel3.setRequestFocusEnabled(false);

        buttonGroup3.add(KPH);
        KPH.setText("KPH");
        KPH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KPHActionPerformed(evt);
            }
        });

        buttonGroup3.add(MPH);
        MPH.setText("MPH");
        MPH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MPHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(KPH)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MPH)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(KPH)
                .addComponent(MPH))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Lambda"));
        jPanel4.setPreferredSize(new java.awt.Dimension(199, 49));
        jPanel4.setRequestFocusEnabled(false);

        buttonGroup4.add(Lambda);
        Lambda.setText("Lambda");
        Lambda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LambdaActionPerformed(evt);
            }
        });

        buttonGroup4.add(AF);
        AF.setText("A/F");
        AF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(Lambda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AF)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(Lambda)
                .addComponent(AF))
        );

        jLabel1.setText("Set these options to");

        jLabel2.setText("match DTASWin settings");

        jLabel3.setText("from File->System Units");

        jButton1.setText("Done");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Container p=this;
        while (p!=null) {
            if (p instanceof JDialog)
                p.setVisible(false);
            p=p.getParent();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void DegFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DegFActionPerformed
        RunManager.getInstance().setDegC(false);
    }//GEN-LAST:event_DegFActionPerformed

    private void DegCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DegCActionPerformed
        RunManager.getInstance().setDegC(true);
    }//GEN-LAST:event_DegCActionPerformed

    private void KPAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KPAActionPerformed
        RunManager.getInstance().setKPA(true);
    }//GEN-LAST:event_KPAActionPerformed

    private void PSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PSIActionPerformed
        RunManager.getInstance().setKPA(false);
    }//GEN-LAST:event_PSIActionPerformed

    private void KPHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KPHActionPerformed
        RunManager.getInstance().setKPH(true);
    }//GEN-LAST:event_KPHActionPerformed

    private void MPHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MPHActionPerformed
        RunManager.getInstance().setKPH(false);
    }//GEN-LAST:event_MPHActionPerformed

    private void LambdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LambdaActionPerformed
        RunManager.getInstance().setLambda(true);
    }//GEN-LAST:event_LambdaActionPerformed

    private void AFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AFActionPerformed
        RunManager.getInstance().setLambda(false);
    }//GEN-LAST:event_AFActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AF;
    private javax.swing.JRadioButton DegC;
    private javax.swing.JRadioButton DegF;
    private javax.swing.JRadioButton KPA;
    private javax.swing.JRadioButton KPH;
    private javax.swing.JRadioButton Lambda;
    private javax.swing.JRadioButton MPH;
    private javax.swing.JRadioButton PSI;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}