/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

//awt
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

//swing
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

//util
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.Date;

//text
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//Propios
import hetec.*;

/**
 *
 * @author Richard
 */
public class Asientos extends javax.swing.JFrame {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    DefaultTableModel modelo;
    int idCta, idProy, idTerc;
    double[] total = new double[2]; //Debe, Haber

    /**
     * Creates new form Asientos
     */
    public Asientos(hetec.OracleBD oracle, String usuario) {
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Asientos " + usuario_g);
        configurarTabla();
        //Llenado de ComboBox Empresas.
        oraclebd.getEmpresas(EmpresaCB);
        /*ESTILO*/
        //POSITION DE PANTALLA
        this.setLocationRelativeTo(null);
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDE
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO PANEL
        panel1.setBackground(Color.white);
        //FONDO COMBO BOX
        EmpresaCB.setBackground(Color.white);
        //FONDO TABLA
        AsientosTbl.setOpaque(true);
        AsientosTbl.setFillsViewportHeight(true);
        AsientosTbl.setBackground(Color.white);
        /*TERMINA ESTILO*/
        String key;
        Action salirAction = new AbstractAction("Salir F2") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                        "Salir", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        System.exit(0);
                    }
                }
            }
        };
        key = "Salir";
        CancelarBtn.setAction(salirAction);
        salirAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F2);
        CancelarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), key);
        CancelarBtn.getActionMap().put(key, salirAction);

        Action buscarAction = new AbstractAction("Buscar F3") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buscarTrans();
            }
        };
        key = "Buscar";
        BuscarTransBtn.setAction(buscarAction);
        buscarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F3);
        BuscarTransBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), key);
        BuscarTransBtn.getActionMap().put(key, buscarAction);

        Action limpiarAction = new AbstractAction("Limpiar F4") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "Los datos no guardados se perdarán, ¿desea limpiar?", //Mensaje
                        "Limpiar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        limpiar();
                    }
                }
            }
        };
        key = "Limpiar";
        LimpiarBtn.setAction(limpiarAction);
        limpiarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F4);
        LimpiarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), key);
        LimpiarBtn.getActionMap().put(key, limpiarAction);

        Action autorizarAction = new AbstractAction("Autorizar F7") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "¿desea autorizar?", //Mensaje
                        "Autorizar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        autorizar();
                    }
                }

            }
        };
        key = "Autorizar";
        AutorizarBtn.setAction(autorizarAction);
        autorizarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F8);
        AutorizarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), key);
        AutorizarBtn.getActionMap().put(key, autorizarAction);

        Action mayorizarAction = new AbstractAction("Mayorizar F8") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "¿desea mayorizar?", //Mensaje
                        "Mayorizar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        mayorizar();
                    }
                }

            }
        };
        key = "Mayorizar";
        MayorizarBtn.setAction(mayorizarAction);
        mayorizarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F8);
        MayorizarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), key);
        MayorizarBtn.getActionMap().put(key, mayorizarAction);

        FocusListener resaltatxt = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                Color lightBlue = new Color(240, 240, 240, 255);
                e.getComponent().setBackground(lightBlue);
                JTextField textField = (JTextField) e.getComponent();
                textField.setText(textField.getText());
                textField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        };

        FocusListener resaltacb = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                Color lightBlue = new Color(240, 240, 240, 255);
                e.getComponent().setBackground(lightBlue);
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        };

        NumeroTxt.addFocusListener(resaltatxt);
        DescripcionTxt.setDocument(new JTextFieldLimit(250, true));
        NumeroTxt.setDocument(new JTextFieldLimit(10, true));
        EmpresaCB.addFocusListener(resaltacb);
        limpiar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        DebitoLbl = new javax.swing.JLabel();
        CreditoLbl = new javax.swing.JLabel();
        DiferenciaLbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        panel1 = new java.awt.Panel();
        jLabel38 = new javax.swing.JLabel();
        MenuBtn = new javax.swing.JButton();
        LimpiarBtn = new javax.swing.JButton();
        CancelarBtn = new javax.swing.JButton();
        AutorizarBtn = new javax.swing.JButton();
        MayorizarBtn = new javax.swing.JButton();
        ReversarBtn = new javax.swing.JButton();
        DuplicarBtn = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        CuentaTxt = new javax.swing.JTextField();
        DescripcionLbl = new javax.swing.JLabel();
        BuscarArtBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        TipoLinCB = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        ValorTxt = new javax.swing.JTextField();
        IncluirBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        FechaLinDC = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        NumeroLinTxt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        ProyectoTxt = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        BuscarProBtn = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        TerceroTxt = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        TerceroLbl = new javax.swing.JLabel();
        BuscarTerBtn = new javax.swing.JButton();
        ProyectoLbl = new javax.swing.JLabel();
        DetalleLinTxt = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AsientosTbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        AsientoLbl = new javax.swing.JLabel();
        BuscarTransBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        EstadoLbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        EmpresaCB = new javax.swing.JComboBox();
        PeriodoCB = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        NumeroTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        FechaDC = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        TipoCB = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        OrigenCB = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        DescripcionTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(32, 116, 174));
        jLabel13.setText("Debito:");
        jLabel13.setToolTipText("Subtotal de todo el documento.");

        jLabel14.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(32, 116, 174));
        jLabel14.setText("Credito:");
        jLabel14.setToolTipText("Total descuentos del documento.");

        DebitoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        DebitoLbl.setForeground(new java.awt.Color(32, 116, 174));
        DebitoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DebitoLbl.setText("0");

        CreditoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        CreditoLbl.setForeground(new java.awt.Color(32, 116, 174));
        CreditoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CreditoLbl.setText("0");

        DiferenciaLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        DiferenciaLbl.setForeground(new java.awt.Color(32, 116, 174));
        DiferenciaLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DiferenciaLbl.setText("0");

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(32, 116, 174));
        jLabel15.setText("Diferencia:");
        jLabel15.setToolTipText("Total descuentos del documento.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(DebitoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(CreditoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DiferenciaLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(212, 212, 212))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DebitoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CreditoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DiferenciaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel1.setName(""); // NOI18N

        jLabel38.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(32, 116, 174));
        jLabel38.setText("ASIENTOS");

        MenuBtn.setBackground(new java.awt.Color(255, 255, 255));
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        MenuBtn.setToolTipText("Menu");
        MenuBtn.setBorder(null);
        MenuBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MenuBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MenuBtnMouseExited(evt);
            }
        });

        LimpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        LimpiarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setText("Limpiar (F4)");
        LimpiarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        LimpiarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LimpiarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseExited(evt);
            }
        });
        LimpiarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LimpiarBtnKeyPressed(evt);
            }
        });

        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setText("Salir (F2)");
        CancelarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        CancelarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CancelarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseExited(evt);
            }
        });
        CancelarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CancelarBtnKeyPressed(evt);
            }
        });

        AutorizarBtn.setBackground(new java.awt.Color(255, 255, 255));
        AutorizarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        AutorizarBtn.setForeground(new java.awt.Color(32, 116, 174));
        AutorizarBtn.setText("Autorizar (F7)");
        AutorizarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        AutorizarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AutorizarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AutorizarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AutorizarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AutorizarBtnMouseExited(evt);
            }
        });
        AutorizarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AutorizarBtnKeyPressed(evt);
            }
        });

        MayorizarBtn.setBackground(new java.awt.Color(255, 255, 255));
        MayorizarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        MayorizarBtn.setForeground(new java.awt.Color(32, 116, 174));
        MayorizarBtn.setText("Mayorizar (F8)");
        MayorizarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        MayorizarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MayorizarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MayorizarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MayorizarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MayorizarBtnMouseExited(evt);
            }
        });
        MayorizarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MayorizarBtnKeyPressed(evt);
            }
        });

        ReversarBtn.setBackground(new java.awt.Color(255, 255, 255));
        ReversarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        ReversarBtn.setForeground(new java.awt.Color(32, 116, 174));
        ReversarBtn.setText("Reversar");
        ReversarBtn.setToolTipText("");
        ReversarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        ReversarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ReversarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReversarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ReversarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ReversarBtnMouseExited(evt);
            }
        });
        ReversarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReversarBtnKeyPressed(evt);
            }
        });

        DuplicarBtn.setBackground(new java.awt.Color(255, 255, 255));
        DuplicarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        DuplicarBtn.setForeground(new java.awt.Color(32, 116, 174));
        DuplicarBtn.setText("Duplicar");
        DuplicarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        DuplicarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        DuplicarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DuplicarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                DuplicarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                DuplicarBtnMouseExited(evt);
            }
        });
        DuplicarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DuplicarBtnKeyPressed(evt);
            }
        });

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel37.setPreferredSize(new java.awt.Dimension(118, 20));

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ReversarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MayorizarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AutorizarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38))
                    .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DuplicarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AutorizarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MayorizarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReversarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DuplicarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 200));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 200));

        jLabel19.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel19.setText("Proyecto:");

        CuentaTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CuentaTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CuentaTxtKeyPressed(evt);
            }
        });

        DescripcionLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        BuscarArtBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarArtBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setText("Buscar F6");
        BuscarArtBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarArtBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarArtBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseExited(evt);
            }
        });
        BuscarArtBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarArtBtnKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel11.setText("Tipo:");

        TipoLinCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoLinCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Debito", "Credito" }));
        TipoLinCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoLinCBKeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel24.setText("Valor:");

        ValorTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ValorTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ValorTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ValorTxtKeyPressed(evt);
            }
        });

        IncluirBtn.setBackground(new java.awt.Color(255, 255, 255));
        IncluirBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        IncluirBtn.setForeground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setText("Incluir");
        IncluirBtn.setAlignmentX(0.5F);
        IncluirBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        IncluirBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        IncluirBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseExited(evt);
            }
        });
        IncluirBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IncluirBtnKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel4.setText("Fecha:");
        jLabel4.setToolTipText("Fecha del documento de compra.");

        FechaLinDC.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FechaLinDC.setPreferredSize(new java.awt.Dimension(87, 22));
        FechaLinDC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FechaLinDCKeyPressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel12.setText("Numero:");

        NumeroLinTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        NumeroLinTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NumeroLinTxtKeyPressed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel16.setText("Descripcion:");

        jLabel20.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel20.setText("Cuenta:");

        ProyectoTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ProyectoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProyectoTxtKeyPressed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel33.setText("Descripcion:");

        BuscarProBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarProBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarProBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarProBtn.setText("Buscar F6");
        BuscarProBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarProBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarProBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarProBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarProBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarProBtnMouseExited(evt);
            }
        });
        BuscarProBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarProBtnKeyPressed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel21.setText("Tercero:");

        TerceroTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TerceroTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerceroTxtKeyPressed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel34.setText("Descripcion:");

        TerceroLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        BuscarTerBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarTerBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarTerBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarTerBtn.setText("Buscar F6");
        BuscarTerBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarTerBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarTerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarTerBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarTerBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarTerBtnMouseExited(evt);
            }
        });
        BuscarTerBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarTerBtnKeyPressed(evt);
            }
        });

        ProyectoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        DetalleLinTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DetalleLinTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetalleLinTxtKeyPressed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel25.setText("Detalle:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CuentaTxt)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BuscarArtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(DescripcionLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TipoLinCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(ValorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(IncluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(DetalleLinTxt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BuscarTerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(FechaLinDC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(NumeroLinTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(ProyectoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(BuscarProBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ProyectoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TerceroTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(TerceroLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BuscarArtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(jLabel11)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(DescripcionLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TipoLinCB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ValorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(IncluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(DetalleLinTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BuscarTerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CuentaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NumeroLinTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TerceroLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(FechaLinDC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(ProyectoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel16)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(BuscarProBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(TerceroTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ProyectoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(58, 58, 58))
        );

        jScrollPane1.setFont(new java.awt.Font("Roboto Condensed", 0, 13)); // NOI18N

        AsientosTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "CuentaId", "Cuenta", "Descripcion", "Debito", "Credito", "Fecha", "Numero", "Detalle", "Proyecto", "Tercero"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(AsientosTbl);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setText("Asiento:");

        AsientoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        AsientoLbl.setText("0");

        BuscarTransBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarTransBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setText("Buscar F3");
        BuscarTransBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarTransBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarTransBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseExited(evt);
            }
        });
        BuscarTransBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarTransBtnKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("Estado:");

        EstadoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel5.setText("Empresa:");

        EmpresaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        EmpresaCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EmpresaCBItemStateChanged(evt);
            }
        });
        EmpresaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmpresaCBKeyPressed(evt);
            }
        });

        PeriodoCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        PeriodoCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PeriodoCBKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel10.setText("Periodo:");

        NumeroTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        NumeroTxt.setText("0");
        NumeroTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NumeroTxtKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel8.setText("Numero:");

        FechaDC.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FechaDC.setPreferredSize(new java.awt.Dimension(87, 22));
        FechaDC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FechaDCKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setText("Fecha:");
        jLabel3.setToolTipText("Fecha del documento de compra.");

        TipoCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Cierre" }));
        TipoCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoCBKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel7.setText("Tipo:");

        OrigenCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        OrigenCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OrigenCBKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel6.setText("Origen:");

        DescripcionTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DescripcionTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DescripcionTxtKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel9.setText("Descripcion:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(FechaDC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(TipoCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(OrigenCB, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(DescripcionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(AsientoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BuscarTransBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(jLabel2))
                                    .addComponent(EstadoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel5))
                                    .addComponent(EmpresaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(PeriodoCB, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(NumeroTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BuscarTransBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EmpresaCB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EstadoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AsientoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PeriodoCB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NumeroTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(FechaDC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(TipoCB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(6, 6, 6)
                        .addComponent(OrigenCB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(6, 6, 6)
                        .addComponent(DescripcionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BuscarTransBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseClicked
        // TODO add your handling code here:
        buscarTrans();
    }//GEN-LAST:event_BuscarTransBtnMouseClicked

    private void BuscarTransBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseEntered
        BuscarTransBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_BuscarTransBtnMouseEntered

    private void BuscarTransBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseExited
        BuscarTransBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_BuscarTransBtnMouseExited

    private void BuscarTransBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarTransBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarTrans();
        }
    }//GEN-LAST:event_BuscarTransBtnKeyPressed

    private void EmpresaCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EmpresaCBItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Item item = (Item) EmpresaCB.getSelectedItem();
            oraclebd.getOrigen(OrigenCB, item.getId());
            oraclebd.getPeriodo(PeriodoCB, item.getId());
            PeriodoCB.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBItemStateChanged

    private void EmpresaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpresaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PeriodoCB.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBKeyPressed

    private void OrigenCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OrigenCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DescripcionTxt.requestFocus();
        }
    }//GEN-LAST:event_OrigenCBKeyPressed

    private void NumeroTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NumeroTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            FechaDC.requestFocus();
        }
    }//GEN-LAST:event_NumeroTxtKeyPressed

    private void DescripcionTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescripcionTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CuentaTxt.requestFocus();
        }
    }//GEN-LAST:event_DescripcionTxtKeyPressed

    private void TipoCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            OrigenCB.requestFocus();
        }
    }//GEN-LAST:event_TipoCBKeyPressed

    private void FechaDCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaDCKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TipoCB.requestFocus();
        }
    }//GEN-LAST:event_FechaDCKeyPressed

    private void MenuBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Salir", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                JFrame Menu = new Menu(oraclebd, usuario_g);
                Menu.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_MenuBtnMouseClicked

    private void MenuBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseEntered
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menuhover_icon.png"))); // NOI18N
        jLabel38.setForeground(new java.awt.Color(253, 141, 42));
    }//GEN-LAST:event_MenuBtnMouseEntered

    private void MenuBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseExited
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        jLabel38.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_MenuBtnMouseExited

    private void LimpiarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea limpiar?", //Mensaje
                "Limpiar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                limpiar();
            }
        }
    }//GEN-LAST:event_LimpiarBtnMouseClicked

    private void LimpiarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseEntered
        LimpiarBtn.setBackground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_LimpiarBtnMouseEntered

    private void LimpiarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseExited
        LimpiarBtn.setBackground(new java.awt.Color(255,255,255));
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_LimpiarBtnMouseExited

    private void CancelarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Salir", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                System.exit(0);
            }
        }
    }//GEN-LAST:event_CancelarBtnMouseClicked

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
       CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
        CancelarBtn.setBackground(new java.awt.Color(255,255,255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void ReversarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReversarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea reversar?", //Mensaje
                "Reversar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                reversar();
            }
        }
    }//GEN-LAST:event_ReversarBtnMouseClicked

    private void ReversarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReversarBtnMouseEntered
       ReversarBtn.setBackground(new java.awt.Color(32, 116, 174));
        ReversarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_ReversarBtnMouseEntered

    private void ReversarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReversarBtnMouseExited
        ReversarBtn.setBackground(new java.awt.Color(255,255,255));
        ReversarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_ReversarBtnMouseExited

    private void PeriodoCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PeriodoCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumeroTxt.requestFocus();
        }
    }//GEN-LAST:event_PeriodoCBKeyPressed

    private void AutorizarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AutorizarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea autorizar?", //Mensaje
                "Autorizar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                autorizar();
            }
        }
    }//GEN-LAST:event_AutorizarBtnMouseClicked

    private void AutorizarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AutorizarBtnMouseEntered
       AutorizarBtn.setBackground(new java.awt.Color(32, 116, 174));
        AutorizarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_AutorizarBtnMouseEntered

    private void AutorizarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AutorizarBtnMouseExited
        AutorizarBtn.setBackground(new java.awt.Color(255,255,255));
        AutorizarBtn.setForeground(new java.awt.Color(32, 116, 174)); 
    }//GEN-LAST:event_AutorizarBtnMouseExited

    private void MayorizarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MayorizarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea mayorizar?", //Mensaje
                "Mayorizar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                mayorizar();
            }
        }
    }//GEN-LAST:event_MayorizarBtnMouseClicked

    private void MayorizarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MayorizarBtnMouseEntered
       MayorizarBtn.setBackground(new java.awt.Color(32, 116, 174));
        MayorizarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_MayorizarBtnMouseEntered

    private void MayorizarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MayorizarBtnMouseExited
        MayorizarBtn.setBackground(new java.awt.Color(255,255,255));
        MayorizarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_MayorizarBtnMouseExited

    private void DuplicarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DuplicarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea duplicar?", //Mensaje
                "Duplicar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                duplicar();
            }
        }
    }//GEN-LAST:event_DuplicarBtnMouseClicked

    private void DuplicarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DuplicarBtnMouseEntered
        DuplicarBtn.setBackground(new java.awt.Color(32, 116, 174));
        DuplicarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_DuplicarBtnMouseEntered

    private void DuplicarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DuplicarBtnMouseExited
        DuplicarBtn.setBackground(new java.awt.Color(255,255,255));
        DuplicarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_DuplicarBtnMouseExited

    private void LimpiarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LimpiarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea limpiar?", //Mensaje
                "Limpiar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                limpiar();
            }
        }
    }//GEN-LAST:event_LimpiarBtnKeyPressed

    private void CancelarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CancelarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Salir", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                System.exit(0);
            }
        }
    }//GEN-LAST:event_CancelarBtnKeyPressed

    private void AutorizarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AutorizarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea autorizar?", //Mensaje
                "Autorizar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                autorizar();
            }
        }
    }//GEN-LAST:event_AutorizarBtnKeyPressed

    private void MayorizarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MayorizarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea mayorizar?", //Mensaje
                "Mayorizar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                mayorizar();
            }
        }
    }//GEN-LAST:event_MayorizarBtnKeyPressed

    private void ReversarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReversarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea reversar?", //Mensaje
                "Reversar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                reversar();
            }
        }
    }//GEN-LAST:event_ReversarBtnKeyPressed

    private void DuplicarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DuplicarBtnKeyPressed
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "¿desea duplicar?", //Mensaje
                "Duplicar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                duplicar();
            }
        }
    }//GEN-LAST:event_DuplicarBtnKeyPressed

    private void DetalleLinTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetalleLinTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            FechaLinDC.requestFocus();
        }
    }//GEN-LAST:event_DetalleLinTxtKeyPressed

    private void BuscarTerBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarTerBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarTercero();
        }
    }//GEN-LAST:event_BuscarTerBtnKeyPressed

    private void BuscarTerBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTerBtnMouseExited
         BuscarTerBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarTerBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_BuscarTerBtnMouseExited

    private void BuscarTerBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTerBtnMouseEntered
        BuscarTerBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarTerBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_BuscarTerBtnMouseEntered

    private void BuscarTerBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTerBtnMouseClicked
        // TODO add your handling code here:
        buscarTercero();
    }//GEN-LAST:event_BuscarTerBtnMouseClicked

    private void TerceroTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerceroTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (TerceroTxt.getText().equals("")) {
                buscarTercero();
            } else {
                String[] tercero = oraclebd.getTercero(TerceroTxt.getText());
                if (tercero[0] != null) {
                    idTerc = Integer.parseInt(tercero[0]);
                    TerceroLbl.setText(tercero[1]);
                    IncluirBtn.requestFocus();
                } else {
                    TerceroTxt.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarTercero();
        }
    }//GEN-LAST:event_TerceroTxtKeyPressed

    private void BuscarProBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarProBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarProyecto();
        }
    }//GEN-LAST:event_BuscarProBtnKeyPressed

    private void BuscarProBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProBtnMouseExited
         BuscarProBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarProBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_BuscarProBtnMouseExited

    private void BuscarProBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProBtnMouseEntered
       BuscarProBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarProBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_BuscarProBtnMouseEntered

    private void BuscarProBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProBtnMouseClicked
        // TODO add your handling code here:
        buscarProyecto();
    }//GEN-LAST:event_BuscarProBtnMouseClicked

    private void ProyectoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProyectoTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ProyectoTxt.getText().equals("")) {
                buscarProyecto();
            } else {
                Item item;
                item = (Item) EmpresaCB.getSelectedItem();
                String selectedItem = String.valueOf(item.getId());
                String[] proyecto = oraclebd.getProyecto(ProyectoTxt.getText(), Integer.valueOf(selectedItem));
                if (proyecto[0] != null) {
                    idProy = Integer.parseInt(proyecto[0]);
                    ProyectoLbl.setText(proyecto[1]);
                    TerceroTxt.requestFocus();
                } else {
                    ProyectoTxt.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarProyecto();
        }
    }//GEN-LAST:event_ProyectoTxtKeyPressed

    private void NumeroLinTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NumeroLinTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ProyectoTxt.requestFocus();
        }
    }//GEN-LAST:event_NumeroLinTxtKeyPressed

    private void FechaLinDCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaLinDCKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumeroLinTxt.requestFocus();
        }
    }//GEN-LAST:event_FechaLinDCKeyPressed

    private void IncluirBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IncluirBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                incluirDetalle();
            } catch (MyException e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_IncluirBtnKeyPressed

    private void IncluirBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseExited
        IncluirBtn.setBackground(new java.awt.Color(255,255,255));
        IncluirBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_IncluirBtnMouseExited

    private void IncluirBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseEntered
       IncluirBtn.setBackground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_IncluirBtnMouseEntered

    private void IncluirBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseClicked
        // TODO add your handling code here:
        try {
            incluirDetalle();
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_IncluirBtnMouseClicked

    private void ValorTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ValorTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            IncluirBtn.requestFocus();
        }
    }//GEN-LAST:event_ValorTxtKeyPressed

    private void TipoLinCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoLinCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ValorTxt.requestFocus();
        }
    }//GEN-LAST:event_TipoLinCBKeyPressed

    private void BuscarArtBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarArtBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarCuenta();
        }
    }//GEN-LAST:event_BuscarArtBtnKeyPressed

    private void BuscarArtBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseExited
        BuscarArtBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_BuscarArtBtnMouseExited

    private void BuscarArtBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseEntered
        BuscarArtBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_BuscarArtBtnMouseEntered

    private void BuscarArtBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseClicked
        // TODO add your handling code here:
        buscarCuenta();
    }//GEN-LAST:event_BuscarArtBtnMouseClicked

    private void CuentaTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CuentaTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (CuentaTxt.getText().equals("")) {
                buscarCuenta();
            } else {
                Item item;
                item = (Item) EmpresaCB.getSelectedItem();
                String selectedItem = String.valueOf(item.getId());
                String[] cuenta = oraclebd.getCuenta(CuentaTxt.getText(), Integer.valueOf(selectedItem));
                if (cuenta[0] != null) {
                    idCta = Integer.parseInt(cuenta[0]);
                    DescripcionLbl.setText(cuenta[1]);
                    TipoLinCB.requestFocus();
                } else {
                    CuentaTxt.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarCuenta();
        }
    }//GEN-LAST:event_CuentaTxtKeyPressed

    public void buscarCuenta() {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String selectedItem = String.valueOf(item.getId());
        BuscarCuentas cuentasFrm = new BuscarCuentas(this, true, oraclebd, Integer.valueOf(selectedItem), usuario_g);
        cuentasFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                idCta = cuentasFrm.getId();
                CuentaTxt.setText(cuentasFrm.getCuenta());
                DescripcionLbl.setText(cuentasFrm.getDescripcion());
                TipoLinCB.requestFocus();
            }
        });
        cuentasFrm.setVisible(true);
    }

    public void buscarProyecto() {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String selectedItem = String.valueOf(item.getId());
        BuscarProyectos proyectosFrm = new BuscarProyectos(this, true, oraclebd, Integer.valueOf(selectedItem), usuario_g);
        proyectosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                idProy = proyectosFrm.getId();
                ProyectoTxt.setText(proyectosFrm.getProyecto());
                ProyectoLbl.setText(proyectosFrm.getDescripcion());
                TerceroTxt.requestFocus();
            }
        });
        proyectosFrm.setVisible(true);
    }

    public void buscarTercero() {
        BuscarTerceros tercerosFrm = new BuscarTerceros(this, true, oraclebd, usuario_g);
        tercerosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                idTerc = tercerosFrm.getId();
                ProyectoTxt.setText(tercerosFrm.getCedula());
                TerceroLbl.setText(tercerosFrm.getNombre());
                IncluirBtn.requestFocus();
            }
        });
        tercerosFrm.setVisible(true);
    }

    //Método del Botón Buscar Transacciónes Creadas.
    public void buscarTrans() {
        Item item = (Item) EmpresaCB.getSelectedItem();
        BuscarAsientos asientosFrm = new BuscarAsientos(this, true, oraclebd, item.getId(), usuario_g);
        asientosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                if (asientosFrm.getId() > -1) {
                    llenarDatos(asientosFrm.getId());
                }
            }
        });
        asientosFrm.setVisible(true);
    }

    public void llenarDatos(int transaccion_p) {
        configurarTabla();
        String[] datos = oraclebd.getAsientoCreado(transaccion_p);
        AsientoLbl.setText(datos[0]);
        EstadoLbl.setText(datos[1]);
        setSelectedValue(EmpresaCB, Integer.valueOf(datos[2]));
        NumeroTxt.setText(datos[3]);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            FechaDC.setDate(formato.parse(datos[4]));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (datos[5].equals("N")) {
            TipoCB.setSelectedIndex(0);
        } else {
            TipoCB.setSelectedIndex(1);
        }
        setSelectedValue(OrigenCB, Integer.valueOf(datos[6]));
        DescripcionTxt.setText(datos[7]);

        for (int i = 0; i < 2; i++) {
            total[i] = 0;
        }
        oraclebd.getDetalleAsiento(modelo, Integer.valueOf(datos[0]), total);
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        DebitoLbl.setText(String.valueOf(formatoNumero.format(total[0])));
        CreditoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
        DiferenciaLbl.setText(String.valueOf(formatoNumero.format(total[0] - total[1])));
    }

    public void setSelectedValue(JComboBox comboBox, int value) {
        Item item;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            item = (Item) comboBox.getItemAt(i);
            if (item.getId() == value) {
                comboBox.setSelectedIndex(i);
            }
        }
    }

    public void incluirDetalle() throws MyException {
        String numero, detalle;
        if (NumeroLinTxt.getText().equals("")) {
            numero = NumeroTxt.getText();
        } else {
            numero = NumeroLinTxt.getText();
        }
        if (DetalleLinTxt.getText().equals("")) {
            detalle = DescripcionTxt.getText();
        } else {
            detalle = DetalleLinTxt.getText();
        }
        try {
            crearTrans();
            if (CuentaTxt.getText().isEmpty()) {
                throw new MyException("Cuenta Invalida");
            }
            if (ValorTxt.getText().isEmpty() || !NumberUtils.isNumber(ValorTxt.getText())) {
                throw new MyException("Valor Invalido");
            }
            Double debito, credito;
            if (TipoLinCB.getSelectedItem().toString().equals("Debito")) {
                debito = Double.parseDouble(ValorTxt.getText());
                credito = 0.0;
            } else {
                credito = Double.parseDouble(ValorTxt.getText());
                debito = 0.0;
            }
            int id = oraclebd.incluirDetalleASI(Integer.parseInt(AsientoLbl.getText()), idCta, debito, credito,
                    FechaLinDC.getDate(), numero, idTerc, detalle, idProy
            );
            if (id > -1) {
                configurarTabla();
                for (int i = 0; i < 2; i++) {
                    total[i] = 0;
                }
                //Totales
                oraclebd.getDetalleAsiento(modelo, Integer.parseInt(AsientoLbl.getText()), total);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                DebitoLbl.setText(String.valueOf(formatoNumero.format(total[0])));
                CreditoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
                DiferenciaLbl.setText(String.valueOf(formatoNumero.format(total[0] - total[1])));

                CuentaTxt.setText("");
                idCta = -1;
                DescripcionLbl.setText("");
                ValorTxt.setText("");
            }
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Crear y Configurar la Tabla.
    public void configurarTabla() {
        modelo = new DefaultTableModel();
        for (int i = 0; i < 11; i++) {
            modelo.addColumn(AsientosTbl.getColumnName(i));
        }
        AsientosTbl.setModel(modelo);
        TableCellRenderer render = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                JLabel lbl = new JLabel(value == null ? "" : formatoNumero.format(Double.parseDouble(value.toString())));
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                lbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
                return lbl;
            }
        };
        AsientosTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Id
        AsientosTbl.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        AsientosTbl.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        AsientosTbl.getColumn(AsientosTbl.getColumnName(0)).setPreferredWidth(0);
        //CuentaId
        AsientosTbl.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        AsientosTbl.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        AsientosTbl.getColumn(AsientosTbl.getColumnName(1)).setPreferredWidth(0);
        //Cuenta
        AsientosTbl.getColumn(AsientosTbl.getColumnName(2)).setPreferredWidth(100);
        //Descripcion
        AsientosTbl.getColumn(AsientosTbl.getColumnName(3)).setPreferredWidth(200);
        //Debito
        AsientosTbl.getColumn(AsientosTbl.getColumnName(4)).setPreferredWidth(80);
        //Credito
        AsientosTbl.getColumn(AsientosTbl.getColumnName(5)).setPreferredWidth(80);
        //Fecha
        AsientosTbl.getColumn(AsientosTbl.getColumnName(6)).setPreferredWidth(80);
        //Numero
        AsientosTbl.getColumn(AsientosTbl.getColumnName(7)).setPreferredWidth(80);
        //Detalle
        AsientosTbl.getColumn(AsientosTbl.getColumnName(8)).setPreferredWidth(200);
        //Proyecto
        AsientosTbl.getColumn(AsientosTbl.getColumnName(9)).setPreferredWidth(200);
        //Tercero
        AsientosTbl.getColumn(AsientosTbl.getColumnName(10)).setPreferredWidth(200);
    }

    public void crearTrans() throws MyException {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String empresa = String.valueOf(item.getId());
        item = (Item) OrigenCB.getSelectedItem();
        String origen = String.valueOf(item.getId());
        item = (Item) PeriodoCB.getSelectedItem();
        String periodo = String.valueOf(item.getId());
        String tipo;

        int respta = oraclebd.validaperiodo(empresa, periodo, FechaDC.getDate());

        if (respta == 0) {
            throw new MyException("Periodo Invalido");
        }

        if (TipoCB.getSelectedItem().toString().equals("Normal")) {
            tipo = "N";
        } else {
            tipo = "C";
        }

        if (StringUtils.isEmpty(empresa)) {
            throw new MyException("Empresa Invalida");
        }

        if (StringUtils.isEmpty(origen)) {
            throw new MyException("Origen Invalido");
        }

        if (AsientoLbl.getText().equals("0")) {
            int id = oraclebd.crearAsiento(Integer.valueOf(Integer.valueOf(empresa)), NumeroTxt.getText(),
                    FechaDC.getDate(), tipo, origen, DescripcionTxt.getText(), usuario_g);
            if (id == -1) {
                JOptionPane.showMessageDialog(null, "Se produjo un error al insertar, intente nuevamente.", "Error de Inserción", JOptionPane.ERROR_MESSAGE);
            } else {
                AsientoLbl.setText(String.valueOf(id));
                CuentaTxt.requestFocus();
            }
        } else {
            oraclebd.modificarAsiento(Integer.valueOf(AsientoLbl.getText()), Integer.valueOf(empresa), NumeroTxt.getText(),
                    FechaDC.getDate(), tipo, origen, DescripcionTxt.getText(), usuario_g);
        }
    }

    public void autorizar() {
        oraclebd.autorizarAsiento(Integer.valueOf(AsientoLbl.getText()));
        limpiar();
    }

    public void mayorizar() {
        oraclebd.mayorizarAsiento(Integer.valueOf(AsientoLbl.getText()));
        limpiar();
    }

    public void reversar() {
        JOptionPane mensaje = new JOptionPane();
        int id_v = oraclebd.reversarASI(Integer.valueOf(AsientoLbl.getText()), usuario_g);
        mensaje.showMessageDialog(null, "Asiento nuevo: " + String.valueOf(id_v), "Reversar Asiento", JOptionPane.INFORMATION_MESSAGE);
    }

    public void duplicar() {
        JOptionPane mensaje = new JOptionPane();
        int id_v = oraclebd.duplicarASI(Integer.valueOf(AsientoLbl.getText()), usuario_g);
        mensaje.showMessageDialog(null, "Asiento nuevo: " + String.valueOf(id_v), "Duplicar Asiento", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiar() {
        AsientoLbl.setText("0");
        EstadoLbl.setText("");
        //EmpresaCB.setSelectedItem(EmpresaCB.getItemAt(0));
        //PeriodoCB.setSelectedItem(PeriodoCB.getItemAt(0));
        NumeroTxt.setText("0");
        Date date = new Date();
        FechaDC.setDate(date);
        TipoCB.setSelectedItem(TipoCB.getItemAt(0));
        OrigenCB.setSelectedItem(OrigenCB.getItemAt(0));
        DescripcionTxt.setText("");
        //Detalle
        CuentaTxt.setText("");
        DescripcionLbl.setText("");
        TipoLinCB.setSelectedItem(TipoLinCB.getItemAt(0));
        ValorTxt.setText("");
        DetalleLinTxt.setText("");
        FechaLinDC.setDate(date);
        NumeroLinTxt.setText("");
        ProyectoTxt.setText("");
        ProyectoLbl.setText("");
        TerceroTxt.setText("");
        TerceroLbl.setText("");

        DiferenciaLbl.setText("0");
        configurarTabla();
        for (int i = 0; i < 2; i++) {
            total[i] = 0;
        }
        EmpresaCB.requestFocus();
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
            java.util.logging.Logger.getLogger(Asientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Asientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Asientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asientos(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AsientoLbl;
    private javax.swing.JTable AsientosTbl;
    private javax.swing.JButton AutorizarBtn;
    private javax.swing.JButton BuscarArtBtn;
    private javax.swing.JButton BuscarProBtn;
    private javax.swing.JButton BuscarTerBtn;
    private javax.swing.JButton BuscarTransBtn;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JLabel CreditoLbl;
    private javax.swing.JTextField CuentaTxt;
    private javax.swing.JLabel DebitoLbl;
    private javax.swing.JLabel DescripcionLbl;
    private javax.swing.JTextField DescripcionTxt;
    private javax.swing.JTextField DetalleLinTxt;
    private javax.swing.JLabel DiferenciaLbl;
    private javax.swing.JButton DuplicarBtn;
    private javax.swing.JComboBox EmpresaCB;
    private javax.swing.JLabel EstadoLbl;
    private com.toedter.calendar.JDateChooser FechaDC;
    private com.toedter.calendar.JDateChooser FechaLinDC;
    private javax.swing.JButton IncluirBtn;
    private javax.swing.JButton LimpiarBtn;
    private javax.swing.JButton MayorizarBtn;
    private javax.swing.JButton MenuBtn;
    private javax.swing.JTextField NumeroLinTxt;
    private javax.swing.JTextField NumeroTxt;
    private javax.swing.JComboBox OrigenCB;
    private javax.swing.JComboBox PeriodoCB;
    private javax.swing.JLabel ProyectoLbl;
    private javax.swing.JTextField ProyectoTxt;
    private javax.swing.JButton ReversarBtn;
    private javax.swing.JLabel TerceroLbl;
    private javax.swing.JTextField TerceroTxt;
    private javax.swing.JComboBox TipoCB;
    private javax.swing.JComboBox TipoLinCB;
    private javax.swing.JTextField ValorTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
