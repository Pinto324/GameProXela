/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.Cajero;

import Controladores.CClientes;
import Objetos.clientes;
import UI.Utilidades;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author branp
 */
public class ModificarCliente extends javax.swing.JDialog {

    CClientes controlador = new CClientes();
    String[] datos;
    ArrayList<clientes> info;
    java.awt.Frame parents;
    /**
     * Creates new form RellenarI
     */
    public ModificarCliente(java.awt.Frame parent, boolean modal, String[] da) {
        super(parent, modal);
        parents =parent;
        initComponents();
        this.setLocationRelativeTo(null);
        Utilidades.SoloNumeros(jTextFieldNit);
        datos = da;
        llenarcombo();    
        estadoinicial();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanelbuscar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldJNombre = new javax.swing.JTextField();
        jTextFieldNit = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanelModificar = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanelFondo.setBackground(new java.awt.Color(102, 204, 255));
        jPanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 204, 255));
        jLabel1.setText("Modificar Cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelFondo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 590, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 204, 255));
        jLabel3.setText("Seleccione el cliente:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 28));

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 204, 255));
        jLabel4.setText("Nit");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, -1));

        jPanelbuscar.setBackground(new java.awt.Color(102, 204, 255));
        jPanelbuscar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelbuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelbuscar.setEnabled(false);
        jPanelbuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelbuscarMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.setEnabled(false);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelbuscarLayout = new javax.swing.GroupLayout(jPanelbuscar);
        jPanelbuscar.setLayout(jPanelbuscarLayout);
        jPanelbuscarLayout.setHorizontalGroup(
            jPanelbuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelbuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelbuscarLayout.setVerticalGroup(
            jPanelbuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelbuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanelbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, -1, -1));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 204, 255));
        jLabel6.setText("Tarjeta de puntos");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, -1, 16));

        jTextFieldJNombre.setEnabled(false);
        jTextFieldJNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldJNombreActionPerformed(evt);
            }
        });
        jPanel2.add(jTextFieldJNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 200, -1));

        jTextFieldNit.setEnabled(false);
        jPanel2.add(jTextFieldNit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 130, -1));

        jCheckBox1.setEnabled(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, -1, -1));

        jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox1PopupMenuWillBecomeVisible(evt);
            }
        });
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 200, -1));

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 204, 255));
        jLabel7.setText("Nombre del cliente");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, -1, 28));

        jPanelModificar.setBackground(new java.awt.Color(102, 204, 255));
        jPanelModificar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelModificarMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Modificar");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelModificarLayout = new javax.swing.GroupLayout(jPanelModificar);
        jPanelModificar.setLayout(jPanelModificarLayout);
        jPanelModificarLayout.setHorizontalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelModificarLayout.setVerticalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanelModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, -1, -1));

        jPanelFondo.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 500, 260));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanelFondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 0, 40, 29));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();

    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        funcionamientoBotonBuscar();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanelbuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelbuscarMouseClicked
        funcionamientoBotonBuscar();
    }//GEN-LAST:event_jPanelbuscarMouseClicked

    private void jTextFieldJNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldJNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldJNombreActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jComboBox1PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeVisible
        jPanelbuscar.setEnabled(true);
        jLabel5.setEnabled(true);
    }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeVisible

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        funcionamientoBotonModificar();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanelModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelModificarMouseClicked
        funcionamientoBotonModificar();
    }//GEN-LAST:event_jPanelModificarMouseClicked
    public void llenarcombo() {
        info = controlador.buscarClientes();
        for (int i = 0; i < info.size(); i++) {
            jComboBox1.addItem(info.get(i).getNit() + " " + info.get(i).getNombre());
        }
    }

    public void funcionamientoBotonBuscar() {
        jTextFieldJNombre.setText(info.get(jComboBox1.getSelectedIndex()).getNombre());
        jTextFieldNit.setText(info.get(jComboBox1.getSelectedIndex()).getNit());
        if (info.get(jComboBox1.getSelectedIndex()).getTipoTarjeta().equals("0")) {
            jCheckBox1.setEnabled(true);
        }
        jTextFieldJNombre.setEnabled(true);
        jTextFieldNit.setEnabled(true);
        jPanelModificar.setVisible(true);
        jLabel8.setVisible(true);
        jPanelbuscar.setVisible(false);
        jLabel5.setVisible(false);
        jComboBox1.setEnabled(false);
    }

    public void funcionamientoBotonModificar() {
        loginModificar dialog = new loginModificar(parents, true);
        dialog.setVisible(true);
        if (dialog.isLogeo()) {
            int id = Integer.valueOf(info.get(jComboBox1.getSelectedIndex()).getId());
            boolean tarjeta = (jCheckBox1.isEnabled() && jCheckBox1.isSelected()) ? true : false;
            if (controlador.modificarCliente(id, jTextFieldJNombre.getText(), Integer.valueOf(jTextFieldNit.getText()), tarjeta)) {
                JOptionPane.showMessageDialog(null, "el usuario se modifico correctamente", "aviso", JOptionPane.INFORMATION_MESSAGE);
                estadoinicial();
                llenarcombo();
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio un error.", "error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "El usuario ingresado no es administrador.", "error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void estadoinicial(){
        jPanelModificar.setVisible(false);
        jLabel8.setVisible(false);
        jPanelbuscar.setVisible(true);
        jLabel5.setVisible(true);
        jComboBox1.setEnabled(true);
        jTextFieldJNombre.setEnabled(false);
        jTextFieldNit.setEnabled(false);
        jCheckBox1.setEnabled(false);
        jTextFieldJNombre.setText("");
        jTextFieldNit.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JPanel jPanelModificar;
    private javax.swing.JPanel jPanelbuscar;
    private javax.swing.JTextField jTextFieldJNombre;
    private javax.swing.JTextField jTextFieldNit;
    // End of variables declaration//GEN-END:variables
}
