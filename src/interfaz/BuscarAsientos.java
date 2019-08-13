/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

//awt
import java.awt.Color;
import java.awt.event.KeyEvent;

//swing
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//Propios
import hetec.*;
import javax.swing.ImageIcon;

/**
 *
 * @author Richard
 */
public class BuscarAsientos extends javax.swing.JDialog {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    int id = -1;
    DefaultTableModel modelo;

    /**
     * Creates new form BuscarAsientos
     */
    public BuscarAsientos(java.awt.Frame parent, boolean modal, hetec.OracleBD oracle, int empresa, String usuario) {
        super(parent, modal);
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Buscar Asientos " + usuario_g);
        cargarAsientos(empresa);
        CancelarBtn.requestFocus();

        /*ESTILO*/
        //POSITION DE PANTALLA
        this.setLocationRelativeTo(null);
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDE
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO PANEL
        panel1.setBackground(Color.white);
        //FONDO TABLA
        AsientosTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        AsientosTbl.setOpaque(true);
        AsientosTbl.setFillsViewportHeight(true);
        AsientosTbl.setBackground(Color.white);
        /*Logo*/
        setIconImage(new ImageIcon(getClass().getResource("../Imagenes/Icon.png")).getImage());
        /*TERMINA ESTILO*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new java.awt.Panel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AsientosTbl = new javax.swing.JTable();
        CancelarBtn = new javax.swing.JButton();
        AceptarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
        jLabel25.setText("BUSCAR ASIENTOS");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(118, 20));

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AsientosTbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        AsientosTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Numero", "Fecha", "Tipo", "Origen", "Descripcion", "Debito", "Credito"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AsientosTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                AsientosTblMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(AsientosTbl);

        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setText("Cancelar");
        CancelarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        CancelarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CancelarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseExited(evt);
            }
        });
        CancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarBtnActionPerformed(evt);
            }
        });
        CancelarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CancelarBtnKeyPressed(evt);
            }
        });

        AceptarBtn.setBackground(new java.awt.Color(255, 255, 255));
        AceptarBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        AceptarBtn.setForeground(new java.awt.Color(32, 116, 174));
        AceptarBtn.setText("Aceptar");
        AceptarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        AceptarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AceptarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AceptarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AceptarBtnMouseExited(evt);
            }
        });
        AceptarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AceptarBtnActionPerformed(evt);
            }
        });
        AceptarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AceptarBtnKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(581, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 436, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(88, 88, 88)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(52, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AsientosTblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AsientosTblMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            aceptar();
        }
    }//GEN-LAST:event_AsientosTblMousePressed

    private void AceptarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseEntered
       AceptarBtn.setBackground(new java.awt.Color(32, 116, 174));
        AceptarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_AceptarBtnMouseEntered

    private void AceptarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseExited
         AceptarBtn.setBackground(new java.awt.Color(255,255,255));
        AceptarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_AceptarBtnMouseExited

    private void AceptarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AceptarBtnActionPerformed
        aceptar();
    }//GEN-LAST:event_AceptarBtnActionPerformed

    private void AceptarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AceptarBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            aceptar();
        }
    }//GEN-LAST:event_AceptarBtnKeyPressed

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
        CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
        CancelarBtn.setBackground(new java.awt.Color(255,255,255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void CancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_CancelarBtnActionPerformed

    private void CancelarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CancelarBtnKeyPressed
        this.dispose();
    }//GEN-LAST:event_CancelarBtnKeyPressed

    public void aceptar() {
        if (AsientosTbl.getSelectedRow() > -1) {
            id = (Integer) (AsientosTbl.getValueAt(AsientosTbl.getSelectedRow(), 0));
            this.dispose();
        }
    }

    public int getId() {
        return id;
    }

    //Cargar la Tabla de Compras Creadas.
    public void cargarAsientos(int empresa) {
        modelo = new tableModel();
        for (int i = 0; i < 8; i++) {
            modelo.addColumn(AsientosTbl.getColumnName(i));
        }
        AsientosTbl.setModel(modelo);
        configurarTabla();
        oraclebd.getAsientosCreados(modelo, empresa);
    }

    public void configurarTabla() {
        AsientosTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Id
        AsientosTbl.getColumn(AsientosTbl.getColumnName(0)).setPreferredWidth(100);
        //Numero
        AsientosTbl.getColumn(AsientosTbl.getColumnName(1)).setPreferredWidth(100);
        //Fecha
        AsientosTbl.getColumn(AsientosTbl.getColumnName(2)).setPreferredWidth(100);
        //Origen
        AsientosTbl.getColumn(AsientosTbl.getColumnName(3)).setPreferredWidth(50);
        //Descripcion Origen
        AsientosTbl.getColumn(AsientosTbl.getColumnName(4)).setPreferredWidth(100);
        //Descripcion
        AsientosTbl.getColumn(AsientosTbl.getColumnName(5)).setPreferredWidth(200);
        //Estado
        AsientosTbl.getColumn(AsientosTbl.getColumnName(6)).setPreferredWidth(100);
        //Tipo
        AsientosTbl.getColumn(AsientosTbl.getColumnName(7)).setPreferredWidth(100);
    }

    public class tableModel extends DefaultTableModel {

        tableModel() {

        }

        @Override
        public boolean isCellEditable(int row, int cols) {
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuscarAsientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscarAsientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscarAsientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscarAsientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscarAsientos dialog = new BuscarAsientos(new javax.swing.JFrame(), true, null, -1, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AceptarBtn;
    private javax.swing.JTable AsientosTbl;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
