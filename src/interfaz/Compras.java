package interfaz;

//awt
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

//text
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//util
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

//Propios
import hetec.*;
import javax.swing.ImageIcon;

public class Compras extends javax.swing.JFrame {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    int idArt;
    DefaultTableModel modelo;
    double[] total = new double[4]; //Subtotal, Monto, Impuesto, Total

    public Compras(hetec.OracleBD oracle, String usuario) {
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Compras " + usuario_g);
        configurarTabla();
        //Setear fecha actual al calendario.
        FechaDC.setDate(new Date());
        FechaDC.setDateFormatString("dd/MM/yyyy");
        //Llenado de ComboBox Empresas.
        oraclebd.getEmpresas(EmpresaCB);

        /*ESTILO*/
        //POSICION DE PANTALLA
        this.setLocationRelativeTo(null);
        //TAMAñOS DE JPANEL
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDER
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO DE COMBO BOX
        EmpresaCB.setBackground(Color.white);
        DepartamentoCB.setBackground(Color.white);
        LocalizacionCB.setBackground(Color.white);
        TipoTransCB.setBackground(Color.white);
        TipoDescCB.setBackground(Color.white);
        MonedaCB.setBackground(Color.white);
        UnidMedCB.setBackground(Color.white);
        AfectaCB.setBackground(Color.white);
        ComprasTbl.setBackground(Color.white);
        //FONDO TABLA
        ComprasTbl.setOpaque(true);
        ComprasTbl.setFillsViewportHeight(true);
        ComprasTbl.setBackground(Color.white);
        
        /*TERMINA ESTILO*/

        ((JTextField) FechaDC.getDateEditor().getUiComponent()).addKeyListener(
                new KeyListener() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    DetalleTxt.requestFocus();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        }
        );

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
        String key = "Limpiar";
        LimpiarBtn.setAction(limpiarAction);
        limpiarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F4);
        LimpiarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), key);
        LimpiarBtn.getActionMap().put(key, limpiarAction);

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

        Action registrarAction = new AbstractAction("Registrar F8") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "¿desea registrar?", //Mensaje
                        "Registrar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        registrar();
                    }
                }
            }
        };
        key = "Registrar";
        RegistrarBtn.setAction(registrarAction);
        registrarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F8);
        RegistrarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), key);
        RegistrarBtn.getActionMap().put(key, registrarAction);

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

        DocumentoTxt.addFocusListener(resaltatxt);
        DocumentoTxt.setDocument(new JTextFieldLimit(20, true));
        FechaDC.addFocusListener(resaltacb);
        EmpresaCB.addFocusListener(resaltacb);
        LocalizacionCB.addFocusListener(resaltacb);
        DepartamentoCB.addFocusListener(resaltacb);
        MonedaCB.addFocusListener(resaltacb);
        ValorCambioTxt.addFocusListener(resaltatxt);
        ValorCambioTxt.setDocument(new JTextFieldLimit(10, true));
        TipoTransCB.addFocusListener(resaltacb);
        CodigoProvTxt.addFocusListener(resaltatxt);
        CodigoProvTxt.setDocument(new JTextFieldLimit(10, true));
        DetalleTxt.addFocusListener(resaltatxt);
        DetalleTxt.setDocument(new JTextFieldLimit(250, true));
        CodigoArtTxt.addFocusListener(resaltatxt);
        CodigoArtTxt.setDocument(new JTextFieldLimit(45, true));
        CantidadTxt.addFocusListener(resaltatxt);
        CantidadTxt.setDocument(new JTextFieldLimit(20, true));
        CantBonifTxt.addFocusListener(resaltatxt);
        CantBonifTxt.setDocument(new JTextFieldLimit(20, true));
        UnidMedCB.addFocusListener(resaltacb);
        MontUnitTxt.addFocusListener(resaltatxt);
        AfectaCB.addFocusListener(resaltacb);
        TipoDescCB.addFocusListener(resaltacb);
        ValorTxt.addFocusListener(resaltatxt);
        ValorTxt.setDocument(new JTextFieldLimit(20, true));
        limpiar();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ComprasTbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        MenuBtn = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        LimpiarBtn = new javax.swing.JButton();
        RegistrarBtn = new javax.swing.JButton();
        CancelarBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        CodigoArtTxt = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        ArticuloLbl = new javax.swing.JLabel();
        BuscarArtBtn = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        CantidadTxt = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        CantBonifTxt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        UnidMedCB = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        MontUnitTxt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        AfectaCB = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        TipoDescCB = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        ValorTxt = new javax.swing.JTextField();
        IncluirBtn = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        SubLbl = new javax.swing.JLabel();
        PorcentLbl = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        MontoDescLbl = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        ImpLbl = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        TotLbl = new javax.swing.JLabel();
        CrearBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ImpuestoLbl = new javax.swing.JLabel();
        TotalLbl = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        SubtotalLbl = new javax.swing.JLabel();
        DescuentoLbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        TransaccionLbl = new javax.swing.JLabel();
        BuscarTransBtn = new javax.swing.JButton();
        EmpresaCB = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        LocalizacionCB = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        DepartamentoCB = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        TipoTransCB = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        MonedaCB = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        ValorCambioTxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        CodigoProvTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        BuscarProvBtn = new javax.swing.JButton();
        ProveedorLbl = new javax.swing.JLabel();
        DocumentoTxt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        FechaDC = new com.toedter.calendar.JDateChooser();
        DetalleTxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HETEC Compras");
        setMinimumSize(new java.awt.Dimension(1024, 550));
        setSize(new java.awt.Dimension(1024, 550));

        jScrollPane1.setFont(new java.awt.Font("Roboto Condensed", 0, 13)); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1024, 550));

        ComprasTbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ComprasTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Articulo", "Código", "Descripción", "Cantidad", "Cant. Bonif.", "U.M.", "Monto Unit.", "Afecta", "Subtotal", "% Desc.", "Descuento", "Impuesto", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ComprasTbl.setToolTipText("Detalle de documento.");
        ComprasTbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ComprasTbl.setName("detTbl"); // NOI18N
        ComprasTbl.setPreferredSize(new java.awt.Dimension(1000, 0));
        ComprasTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ComprasTblMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(ComprasTbl);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFont(new java.awt.Font("Roboto Condensed", 0, 13)); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(200, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
        jLabel25.setText("COMPRAS");

        MenuBtn.setBackground(new java.awt.Color(255, 255, 255));
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        MenuBtn.setToolTipText("Menu");
        MenuBtn.setBorder(null);
        MenuBtn.setBorderPainted(false);
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

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(118, 20));

        LimpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        LimpiarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setText("Limpiar F4");
        LimpiarBtn.setToolTipText("Limpiar pantalla.");
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

        RegistrarBtn.setBackground(new java.awt.Color(255, 255, 255));
        RegistrarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        RegistrarBtn.setForeground(new java.awt.Color(32, 116, 174));
        RegistrarBtn.setText("Registrar F8");
        RegistrarBtn.setToolTipText("Registrar documento.");
        RegistrarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        RegistrarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        RegistrarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseExited(evt);
            }
        });

        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setText("Salir F2");
        CancelarBtn.setToolTipText("Salir del sistema HETEC transaccional");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(RegistrarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CancelarBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RegistrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("");
        jPanel3.setFont(new java.awt.Font("Roboto Condensed", 0, 13)); // NOI18N
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 200));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 200));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 200));

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel9.setText("Artículo:");
        jLabel9.setToolTipText("Codigo de Articulo.");

        CodigoArtTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CodigoArtTxt.setMaximumSize(null);
        CodigoArtTxt.setPreferredSize(new java.awt.Dimension(100, 22));
        CodigoArtTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoArtTxtKeyPressed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel28.setText("Descripcion:");
        jLabel28.setToolTipText("Nombre de Articulo.");

        ArticuloLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ArticuloLbl.setPreferredSize(new java.awt.Dimension(277, 22));

        BuscarArtBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarArtBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setText("Buscar F6");
        BuscarArtBtn.setToolTipText("Buscar articulo por codigo o nombre.");
        BuscarArtBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarArtBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarArtBtn.setPreferredSize(new java.awt.Dimension(100, 22));
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

        jLabel18.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel18.setText("Cantidad:");
        jLabel18.setToolTipText("Cantidad comprada del articulo sin bonificaciones.");

        CantidadTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CantidadTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        CantidadTxt.setPreferredSize(new java.awt.Dimension(100, 22));
        CantidadTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                CantidadTxtFocusLost(evt);
            }
        });
        CantidadTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CantidadTxtKeyPressed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel19.setText("Cant. Bonif.:");
        jLabel19.setToolTipText("Cantidad de bonificaciones del articulo.");

        CantBonifTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CantBonifTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        CantBonifTxt.setText("0");
        CantBonifTxt.setPreferredSize(new java.awt.Dimension(100, 22));
        CantBonifTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CantBonifTxtKeyPressed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel22.setText("U.M.:");
        jLabel22.setToolTipText("Tipo de unidad de medida en la compra del articulo.");

        UnidMedCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        UnidMedCB.setPreferredSize(new java.awt.Dimension(29, 22));
        UnidMedCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UnidMedCBKeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel23.setText("Monto Unit.:");
        jLabel23.setToolTipText("Monto unitario con base en Unidad de Medida.");

        MontUnitTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        MontUnitTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        MontUnitTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                MontUnitTxtFocusLost(evt);
            }
        });
        MontUnitTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MontUnitTxtKeyPressed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel20.setText("Afecta Precio:");
        jLabel20.setToolTipText("Si el valor es \"si\" la compra del articulo afectara precios. Si se encuentra en \"no\" no se tomara en cuenta para el calculo.");
        jLabel20.setPreferredSize(new java.awt.Dimension(20, 15));

        AfectaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        AfectaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Si", "No" }));
        AfectaCB.setPreferredSize(new java.awt.Dimension(42, 22));
        AfectaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AfectaCBKeyPressed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel21.setText("Tipo Desc.:");
        jLabel21.setToolTipText("Define el tipo de descuento que se aplicara (si existe)");

        TipoDescCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoDescCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Porcentaje", "Monto" }));
        TipoDescCB.setPreferredSize(new java.awt.Dimension(79, 22));
        TipoDescCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TipoDescCBItemStateChanged(evt);
            }
        });
        TipoDescCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoDescCBKeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel24.setText("Valor:");
        jLabel24.setToolTipText("Valor de descuento con base al tipo del mismo.");

        ValorTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ValorTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ValorTxt.setText("0");
        ValorTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ValorTxtFocusLost(evt);
            }
        });
        ValorTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ValorTxtKeyPressed(evt);
            }
        });

        IncluirBtn.setBackground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        IncluirBtn.setForeground(new java.awt.Color(255, 255, 255));
        IncluirBtn.setText("Incluir");
        IncluirBtn.setToolTipText("Agregar linea al total de la factura.");
        IncluirBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        IncluirBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        IncluirBtn.setPreferredSize(new java.awt.Dimension(33, 22));
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

        jLabel26.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel26.setText("Subtotal:");
        jLabel26.setToolTipText("Subtotal de articulo.");

        SubLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        SubLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SubLbl.setText("0");
        SubLbl.setPreferredSize(new java.awt.Dimension(6, 22));

        PorcentLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        PorcentLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PorcentLbl.setText("0");
        PorcentLbl.setPreferredSize(new java.awt.Dimension(6, 22));

        jLabel27.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel27.setText("% Descuento:");
        jLabel27.setToolTipText("Porcentaje de descuento de articulo.");

        jLabel29.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel29.setText("Descuento:");
        jLabel29.setToolTipText("Total descuento de articulo.");

        MontoDescLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        MontoDescLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        MontoDescLbl.setText("0");
        MontoDescLbl.setPreferredSize(new java.awt.Dimension(6, 22));

        jLabel31.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel31.setText("Impuesto:");
        jLabel31.setToolTipText("Total impuesto del articulo.");

        ImpLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ImpLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ImpLbl.setText("0");
        ImpLbl.setPreferredSize(new java.awt.Dimension(6, 22));

        jLabel33.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel33.setText("Total:");
        jLabel33.setToolTipText("Total de linea del articulo.");

        TotLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TotLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotLbl.setText("0");
        TotLbl.setPreferredSize(new java.awt.Dimension(6, 22));

        CrearBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        CrearBtn.setText("Crear");
        CrearBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CrearBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CrearBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CrearBtnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(220, 220, 220)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TotLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGap(18, 18, 18)
                        .addComponent(IncluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CrearBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MontoDescLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(UnidMedCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)
                            .addComponent(CodigoArtTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ImpLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)
                                    .addComponent(MontUnitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                    .addComponent(AfectaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TipoDescCB, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21)))
                            .addComponent(ArticuloLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ValorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(BuscarArtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SubLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(CantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PorcentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(CantBonifTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel28)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel19)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CantBonifTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(CantidadTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ArticuloLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CodigoArtTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BuscarArtBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel26)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(UnidMedCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(MontUnitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(AfectaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ValorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SubLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(PorcentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel31)
                            .addComponent(jLabel33)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(TipoDescCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel21))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TotLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IncluirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CrearBtn))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ImpLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MontoDescLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setFont(new java.awt.Font("Roboto Condensed", 0, 13)); // NOI18N
        jPanel5.setMaximumSize(new java.awt.Dimension(200, 200));
        jPanel5.setMinimumSize(new java.awt.Dimension(200, 200));
        jPanel5.setPreferredSize(new java.awt.Dimension(200, 200));

        jLabel14.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(32, 116, 174));
        jLabel14.setText("Descuento:");
        jLabel14.setToolTipText("Total descuentos del documento.");

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(32, 116, 174));
        jLabel15.setText("Impuesto:");
        jLabel15.setToolTipText("Total impuesto del documento.");

        ImpuestoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        ImpuestoLbl.setForeground(new java.awt.Color(32, 116, 174));
        ImpuestoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ImpuestoLbl.setText("0");

        TotalLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        TotalLbl.setForeground(new java.awt.Color(32, 116, 174));
        TotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotalLbl.setText("0");

        jLabel16.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(32, 116, 174));
        jLabel16.setText("Total:");
        jLabel16.setToolTipText("Total del documento.");

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(32, 116, 174));
        jLabel13.setText("Subtotal:");
        jLabel13.setToolTipText("Subtotal de todo el documento.");

        SubtotalLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        SubtotalLbl.setForeground(new java.awt.Color(32, 116, 174));
        SubtotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SubtotalLbl.setText("0");

        DescuentoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        DescuentoLbl.setForeground(new java.awt.Color(32, 116, 174));
        DescuentoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DescuentoLbl.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DescuentoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(ImpuestoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TotalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SubtotalLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(SubtotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(DescuentoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(ImpuestoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(TotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setText("Transacción:");
        jLabel1.setToolTipText("ID de Base de Datos de la Compra. Generado automaticamente por HETEC Transaccional.");

        TransaccionLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TransaccionLbl.setText("0");
        TransaccionLbl.setPreferredSize(new java.awt.Dimension(7, 22));

        BuscarTransBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarTransBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setText("Buscar (F3)");
        BuscarTransBtn.setToolTipText("Buscar compras realizadas previamente para su registro o modificacion.");
        BuscarTransBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarTransBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarTransBtn.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        BuscarTransBtn.setPreferredSize(new java.awt.Dimension(89, 22));
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

        EmpresaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        EmpresaCB.setBorder(null);
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

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel5.setText("Empresa:");
        jLabel5.setToolTipText("Empresa que hace la compra.");

        LocalizacionCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        LocalizacionCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LocalizacionCBKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel4.setText("Localización:");
        jLabel4.setToolTipText("Bodega fisica o virtual donde se localiza el articulo.");

        DepartamentoCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DepartamentoCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DepartamentoCBKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel6.setText("Departamento:");
        jLabel6.setToolTipText("Departamento de la empresa quien hace la compra. Ej: Computo, Recursos Humanos.");

        TipoTransCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoTransCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoTransCBKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setText("Tipo Transacción:");
        jLabel3.setToolTipText("Tipo de Documento a crear.");

        MonedaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        MonedaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MonedaCBKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel7.setText("Moneda:");
        jLabel7.setToolTipText("Moneda asignada al documento.");

        ValorCambioTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ValorCambioTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ValorCambioTxt.setText("0");
        ValorCambioTxt.setPreferredSize(new java.awt.Dimension(12, 22));
        ValorCambioTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ValorCambioTxtKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel10.setText("Valor de Cambio:");
        jLabel10.setToolTipText("Valor del tipo de cambio de la moneda asignada a colones.");

        CodigoProvTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CodigoProvTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoProvTxtKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel8.setText("Proveedor:");
        jLabel8.setToolTipText("Codigo del proveedor de la compra.");

        jLabel30.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel30.setText("Nombre:");
        jLabel30.setToolTipText("Nombre de proveedor seleccionado. ");

        BuscarProvBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarProvBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarProvBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarProvBtn.setText("Buscar F6");
        BuscarProvBtn.setToolTipText("Buscar proveedores por codigo o nombre.");
        BuscarProvBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarProvBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarProvBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarProvBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarProvBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarProvBtnMouseExited(evt);
            }
        });
        BuscarProvBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarProvBtnKeyPressed(evt);
            }
        });

        ProveedorLbl.setBackground(new java.awt.Color(255, 102, 0));
        ProveedorLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        DocumentoTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DocumentoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DocumentoTxtKeyPressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel12.setText("Documento:");
        jLabel12.setToolTipText("Numero de factura de compra.");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("Fecha Documento:");
        jLabel2.setToolTipText("Fecha del documento de compra.");

        FechaDC.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FechaDC.setPreferredSize(new java.awt.Dimension(87, 22));
        FechaDC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FechaDCKeyPressed(evt);
            }
        });

        DetalleTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DetalleTxt.setText("COMPRA");
        DetalleTxt.setPreferredSize(new java.awt.Dimension(55, 22));
        DetalleTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                DetalleTxtFocusLost(evt);
            }
        });
        DetalleTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetalleTxtKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel11.setText("Detalle:");
        jLabel11.setToolTipText("Observaciones de la Compra.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel1)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(TransaccionLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(BuscarTransBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel5)
                                            .addComponent(EmpresaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(LocalizacionCB, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(DepartamentoCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(TipoTransCB, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(MonedaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(ValorCambioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(CodigoProvTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ProveedorLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(8, 8, 8)
                                                .addComponent(BuscarProvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(jLabel30))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(DocumentoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(FechaDC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(DetalleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                                            .addGap(21, 21, 21)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(BuscarTransBtn, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(EmpresaCB, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(TransaccionLbl, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addComponent(jLabel4)
                                .addComponent(jLabel6)
                                .addComponent(LocalizacionCB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(DepartamentoCB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(TipoTransCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(MonedaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ValorCambioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CodigoProvTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ProveedorLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BuscarProvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DocumentoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FechaDC, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DetalleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BuscarArtBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarArtBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarArt();
        }
    }//GEN-LAST:event_BuscarArtBtnKeyPressed

    private void ValorTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ValorTxtFocusLost
        actualizarLinea();
    }//GEN-LAST:event_ValorTxtFocusLost

    private void CantidadTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CantidadTxtFocusLost
        actualizarLinea();
    }//GEN-LAST:event_CantidadTxtFocusLost

    private void MontUnitTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MontUnitTxtFocusLost
        actualizarLinea();
    }//GEN-LAST:event_MontUnitTxtFocusLost

    private void IncluirBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IncluirBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                incluirDetalle();
            } catch (MyException e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_IncluirBtnKeyPressed

    private void TipoDescCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TipoDescCBItemStateChanged
        actualizarLinea();
    }//GEN-LAST:event_TipoDescCBItemStateChanged

    private void ComprasTblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComprasTblMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            if (ComprasTbl.getSelectedRow() > -1) {
                idArt = Integer.parseInt(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 1).toString());
                CodigoArtTxt.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 2).toString());
                ArticuloLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 3).toString());
                CantidadTxt.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 4).toString());
                CantBonifTxt.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 5).toString());
                UnidMedCB.setSelectedItem(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 6).toString());
                MontUnitTxt.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 7).toString());
                AfectaCB.setSelectedItem(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 8).toString());
                TipoDescCB.setSelectedIndex(0);
                SubLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 9).toString());
                PorcentLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 10).toString());
                MontoDescLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 11).toString());
                ImpLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 12).toString());
                TotLbl.setText(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 13).toString());
                oraclebd.borrarDetalleCOM(Integer.parseInt(ComprasTbl.getValueAt(ComprasTbl.getSelectedRow(), 0).toString()));

                configurarTabla();
                for (int i = 0; i < 4; i++) {
                    total[i] = 0;
                }
                oraclebd.getDetallesComp(modelo, Integer.parseInt(TransaccionLbl.getText()), total);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
                DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
                ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
                TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));
                CodigoArtTxt.requestFocus();
            }
        }
    }//GEN-LAST:event_ComprasTblMousePressed

    private void CodigoArtTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoArtTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (CodigoArtTxt.getText().equals("")) {
                buscarArt();
            } else {
                Item item;
                item = (Item) EmpresaCB.getSelectedItem();
                String selectedItem = String.valueOf(item.getId());
                item = (Item) LocalizacionCB.getSelectedItem();
                String selectedLoc = String.valueOf(item.getId());
                String[] articulo = oraclebd.getArticulo(CodigoArtTxt.getText(), Integer.valueOf(selectedItem), Integer.valueOf(selectedLoc), -1, "COM");
                if (articulo[0] != null) {
                    idArt = Integer.parseInt(articulo[0]);
                    ArticuloLbl.setText(articulo[1]);
                    actualizarLinea();
                    CantidadTxt.requestFocus();
                } else {
                    CodigoArtTxt.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarArt();
        }
    }//GEN-LAST:event_CodigoArtTxtKeyPressed

    private void CantidadTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantidadTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CantBonifTxt.requestFocus();
        }
    }//GEN-LAST:event_CantidadTxtKeyPressed

    private void CantBonifTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantBonifTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            UnidMedCB.requestFocus();
        }
    }//GEN-LAST:event_CantBonifTxtKeyPressed

    private void UnidMedCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UnidMedCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MontUnitTxt.requestFocus();
        }
    }//GEN-LAST:event_UnidMedCBKeyPressed

    private void MontUnitTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MontUnitTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            AfectaCB.requestFocus();
        }
    }//GEN-LAST:event_MontUnitTxtKeyPressed

    private void TipoDescCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoDescCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ValorTxt.requestFocus();
        }
    }//GEN-LAST:event_TipoDescCBKeyPressed

    private void ValorTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ValorTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            IncluirBtn.requestFocus();
        }
    }//GEN-LAST:event_ValorTxtKeyPressed

    private void AfectaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AfectaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TipoDescCB.requestFocus();
        }

    }//GEN-LAST:event_AfectaCBKeyPressed

    private void BuscarArtBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseClicked
        // TODO add your handling code here:
        buscarArt();
    }//GEN-LAST:event_BuscarArtBtnMouseClicked

    private void IncluirBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseClicked
        // TODO add your handling code here:
        try {
            incluirDetalle();
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_IncluirBtnMouseClicked

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

    private void CancelarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Cancelar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                this.dispose();
            }
        }
    }//GEN-LAST:event_CancelarBtnMouseClicked

    private void RegistrarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "¿desea registrar?", //Mensaje
                "Registrar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                registrar();
            }
        }
    }//GEN-LAST:event_RegistrarBtnMouseClicked

    private void MenuBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea continuar?", //Mensaje
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

    private void BuscarArtBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseEntered
        BuscarArtBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_BuscarArtBtnMouseEntered

    private void BuscarArtBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseExited
                BuscarArtBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarArtBtnMouseExited

    private void IncluirBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseEntered
        IncluirBtn.setForeground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_IncluirBtnMouseEntered

    private void IncluirBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseExited
                IncluirBtn.setForeground(new java.awt.Color(255,255,255));
        IncluirBtn.setBackground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_IncluirBtnMouseExited

    private void MenuBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseEntered
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menuhover_icon.png"))); // NOI18N
        jLabel25.setForeground(new java.awt.Color(253, 141, 42));
    }//GEN-LAST:event_MenuBtnMouseEntered

    private void MenuBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseExited
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_MenuBtnMouseExited

    private void LimpiarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseEntered
          LimpiarBtn.setBackground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_LimpiarBtnMouseEntered

    private void LimpiarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseExited
                LimpiarBtn.setBackground(new java.awt.Color(255,255,255));
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_LimpiarBtnMouseExited

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
          CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
                CancelarBtn.setBackground(new java.awt.Color(255,255,255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void RegistrarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseEntered
          RegistrarBtn.setBackground(new java.awt.Color(32, 116, 174));
        RegistrarBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_RegistrarBtnMouseEntered

    private void RegistrarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseExited
                RegistrarBtn.setBackground(new java.awt.Color(255,255,255));
        RegistrarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_RegistrarBtnMouseExited

    private void CrearBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearBtnMouseClicked
        // TODO add your handling code here:
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        CrearArticulo articulosFrm = new CrearArticulo(this, true, oraclebd, item.getId(), usuario_g);
        articulosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                CodigoArtTxt.setText(String.valueOf(articulosFrm.getCodigo()));
                CodigoArtTxt.requestFocus();
            }
        });
        articulosFrm.setVisible(true);
    }//GEN-LAST:event_CrearBtnMouseClicked

    private void CrearBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearBtnMouseEntered
         CrearBtn.setForeground(new java.awt.Color(32, 116, 174));
        CrearBtn.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_CrearBtnMouseEntered

    private void CrearBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearBtnMouseExited
         CrearBtn.setForeground(new java.awt.Color(255,255,255));
        CrearBtn.setBackground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_CrearBtnMouseExited

    private void LocalizacionCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LocalizacionCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DepartamentoCB.requestFocus();
        }
    }//GEN-LAST:event_LocalizacionCBKeyPressed

    private void DetalleTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetalleTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CodigoArtTxt.requestFocus();
        }
    }//GEN-LAST:event_DetalleTxtKeyPressed

    private void DetalleTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DetalleTxtFocusLost
        DetalleTxt.setText(DetalleTxt.getText().toUpperCase());
    }//GEN-LAST:event_DetalleTxtFocusLost

    private void BuscarProvBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarProvBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarProv();
        }
    }//GEN-LAST:event_BuscarProvBtnKeyPressed

    private void BuscarProvBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProvBtnMouseExited
        BuscarProvBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarProvBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarProvBtnMouseExited

    private void BuscarProvBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProvBtnMouseEntered
        BuscarProvBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarProvBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_BuscarProvBtnMouseEntered

    private void BuscarProvBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarProvBtnMouseClicked
        // TODO add your handling code here:
        buscarProv();
    }//GEN-LAST:event_BuscarProvBtnMouseClicked

    private void CodigoProvTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoProvTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (CodigoProvTxt.getText().length() == 0) {
                buscarProv();
            } else {
                String proveedor[] = oraclebd.getProveedor(CodigoProvTxt.getText());
                if (proveedor[0] != null) {
                    ProveedorLbl.setText(proveedor[0]);
                    DocumentoTxt.requestFocus();
                } else {
                    CodigoProvTxt.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarProv();
        }
    }//GEN-LAST:event_CodigoProvTxtKeyPressed

    private void TipoTransCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoTransCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MonedaCB.requestFocus();
        }
    }//GEN-LAST:event_TipoTransCBKeyPressed

    private void ValorCambioTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ValorCambioTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CodigoProvTxt.requestFocus();
        }
    }//GEN-LAST:event_ValorCambioTxtKeyPressed

    private void MonedaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MonedaCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ValorCambioTxt.requestFocus();
        }
    }//GEN-LAST:event_MonedaCBKeyPressed

    private void DepartamentoCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DepartamentoCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TipoTransCB.requestFocus();
        }
    }//GEN-LAST:event_DepartamentoCBKeyPressed

    private void EmpresaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpresaCBKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            LocalizacionCB.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBKeyPressed

    private void EmpresaCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EmpresaCBItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Item item = (Item) EmpresaCB.getSelectedItem();
            oraclebd.getLocalizaciones(LocalizacionCB, item.getId(), usuario_g);
            oraclebd.getDepartamentos(DepartamentoCB, item.getId(), usuario_g);
            oraclebd.getMonedas(MonedaCB, item.getId());
            oraclebd.getinvTipos(TipoTransCB, item.getId());
            oraclebd.getUnidMed(UnidMedCB, item.getId());
            CodigoProvTxt.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBItemStateChanged

    private void FechaDCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaDCKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DetalleTxt.requestFocus();
        }
    }//GEN-LAST:event_FechaDCKeyPressed

    private void DocumentoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DocumentoTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            FechaDC.getDateEditor().getUiComponent().requestFocus();
        }
    }//GEN-LAST:event_DocumentoTxtKeyPressed

    private void BuscarTransBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarTransBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarTrans();
        }
    }//GEN-LAST:event_BuscarTransBtnKeyPressed

    private void BuscarTransBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseExited
        BuscarTransBtn.setBackground(new java.awt.Color(255,255,255));
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarTransBtnMouseExited

    private void BuscarTransBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseEntered
        BuscarTransBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_BuscarTransBtnMouseEntered

    private void BuscarTransBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseClicked
        // TODO add your handling code here:
        buscarTrans();
    }//GEN-LAST:event_BuscarTransBtnMouseClicked

    public class tableModel extends DefaultTableModel {

        tableModel() {

        }

        @Override
        public boolean isCellEditable(int row, int cols) {
            return false;
        }
    }

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
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compras(null, null).setVisible(true);
            }
        });
    }

    //Método para Completar los datos de la Transaccion seleccionada en la Ventana Transacciónes Creadas.
    public void llenarDatos(int transaccion_p) {
        limpiar();
        configurarTabla();
        String[] datos = oraclebd.getCompraCreada(transaccion_p);
        TransaccionLbl.setText(datos[0]);
        DocumentoTxt.setText(datos[1]);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            FechaDC.setDate(formato.parse(datos[2]));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        //EmpresaCB.setSelectedItem(Integer.valueOf(datos[3]));
        setSelectedValue(EmpresaCB, Integer.valueOf(datos[3]));
        //LocalizacionCB.setSelectedItem(Integer.valueOf(datos[4]));
        setSelectedValue(LocalizacionCB, Integer.valueOf(datos[4]));
        //DepartamentoCB.setSelectedItem(Integer.valueOf(datos[5]));
        setSelectedValue(DepartamentoCB, Integer.valueOf(datos[5]));
        //MonedaCB.setSelectedItem(Integer.valueOf(datos[6]));
        setSelectedValue(MonedaCB, Integer.valueOf(datos[6]));
        ValorCambioTxt.setText(datos[7]);
        //TipoTransCB.setSelectedItem(Integer.valueOf(datos[8]));
        setSelectedValue(TipoTransCB, Integer.valueOf(datos[8]));
        CodigoProvTxt.setText(datos[9]);
        ProveedorLbl.setText(datos[10]);
        DetalleTxt.setText(datos[11]);
        for (int i = 0; i < 4; i++) {
            total[i] = 0;
        }
        oraclebd.getDetallesComp(modelo, Integer.valueOf(datos[0]), total);
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
        DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
        ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
        TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));
        EmpresaCB.setEnabled(false);
    }

    //Método del Botón Buscar Transacciónes Creadas.
    public void buscarTrans() {
        BuscarCompras comprasFrm = new BuscarCompras(this, true, oraclebd, usuario_g);
        comprasFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                if (comprasFrm.getId() > -1) {
                    llenarDatos(comprasFrm.getId());
                }
            }
        });
        comprasFrm.setVisible(true);
    }

    //Método del Botón Buscar Proveedores.
    public void buscarProv() {
        Item item = (Item) EmpresaCB.getSelectedItem();
        BuscarProveedores proveedoresFrm = new BuscarProveedores(this, true, oraclebd, item.getId(), usuario_g);
        proveedoresFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                if (proveedoresFrm.getId() > -1) {
                    CodigoProvTxt.setText(String.valueOf(proveedoresFrm.getId()));
                    ProveedorLbl.setText(proveedoresFrm.getNombre());
                    DocumentoTxt.requestFocus();
                } else {
                    CodigoProvTxt.requestFocus();
                }
            }
        });
        proveedoresFrm.setVisible(true);
    }

    //Método del Botón Buscar Artículos.
    public void buscarArt() {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String selectedItem = String.valueOf(item.getId());
        item = (Item) LocalizacionCB.getSelectedItem();
        String selectedLoc = String.valueOf(item.getId());
        BuscarArticulos articulosFrm = new BuscarArticulos(this, true, oraclebd, Integer.valueOf(selectedItem), Integer.valueOf(selectedLoc), -1, "COM", usuario_g);
        articulosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                idArt = articulosFrm.getId();
                if (idArt > -1) {
                    CodigoArtTxt.setText(articulosFrm.getCodigo());
                    ArticuloLbl.setText(articulosFrm.getNombre());
                    CantidadTxt.requestFocus();
                } else {
                    CodigoArtTxt.requestFocus();
                }
            }
        });
        articulosFrm.setVisible(true);
    }

    //Método del Botón Crear Transacciones.
    public void crearTrans() throws MyException {
        try {
            Item item;
            item = (Item) EmpresaCB.getSelectedItem();
            String empresa = String.valueOf(item.getId());
            item = (Item) LocalizacionCB.getSelectedItem();
            String localizacion = String.valueOf(item.getId());
            item = (Item) TipoTransCB.getSelectedItem();
            String tipo = String.valueOf(item.getId());
            item = (Item) DepartamentoCB.getSelectedItem();
            String departamento = String.valueOf(item.getId());
            item = (Item) MonedaCB.getSelectedItem();
            String moneda = String.valueOf(item.getId());

            if (StringUtils.isEmpty(empresa)) {
                throw new MyException("Empresa Invalida");
            }

            if (StringUtils.isEmpty(localizacion)) {
                throw new MyException("Localizacion Invalida");
            }

            if (StringUtils.isEmpty(tipo)) {
                throw new MyException("Tipo de Transaccion Invalido");
            }

            if (StringUtils.isEmpty(departamento)) {
                throw new MyException("Departamento Invalido");
            }

            if (StringUtils.isEmpty(moneda)) {
                throw new MyException("Moneda Invalida");
            }

            if (StringUtils.isEmpty(CodigoProvTxt.getText()) || StringUtils.isEmpty(ProveedorLbl.getText())) {
                throw new MyException("Proveedor Invalido");
            }

            if (StringUtils.isEmpty(ValorCambioTxt.getText()) || !NumberUtils.isNumber(ValorCambioTxt.getText())) {
                throw new MyException("Valor de Cambio Invalido");
            }

            if (StringUtils.isEmpty(DocumentoTxt.getText())) {
                throw new MyException("Documento Invalido");
            }

            if (StringUtils.isEmpty(DetalleTxt.getText())) {
                throw new MyException("Detalle Invalido");
            }

            if (TransaccionLbl.getText().equals("0")) {
                int id = oraclebd.crearCompra(Integer.valueOf(empresa), Integer.valueOf(localizacion),
                        Integer.valueOf(tipo), Integer.valueOf(departamento), Integer.valueOf(moneda),
                        Integer.valueOf(CodigoProvTxt.getText()), 0, 0, 0, DocumentoTxt.getText(),
                        FechaDC.getDate(), "C", Integer.valueOf(ValorCambioTxt.getText()),
                        DetalleTxt.getText(), "INV");
                if (id == -1) {
                    JOptionPane.showMessageDialog(null, "Se produjo un error al insertar, intente nuevamente.", "Error de Inserción", JOptionPane.ERROR_MESSAGE);
                } else {
                    oraclebd.actualizausuario(id, 3, usuario_g);
                    TransaccionLbl.setText(String.valueOf(id));
                    EmpresaCB.setEnabled(false);
                    CodigoArtTxt.requestFocus();
                }
            } else {
                oraclebd.modificarCompra(Integer.valueOf(TransaccionLbl.getText()), Integer.valueOf(empresa), Integer.valueOf(localizacion),
                        Integer.valueOf(tipo), Integer.valueOf(departamento), Integer.valueOf(moneda),
                        Integer.valueOf(CodigoProvTxt.getText()), 0, 0, 0, DocumentoTxt.getText(),
                        FechaDC.getDate(), "C", Integer.valueOf(ValorCambioTxt.getText()),
                        DetalleTxt.getText(), "INV");
            }
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Método del Botón Limpiar.
    public void limpiar() {
        TransaccionLbl.setText("0");
        DocumentoTxt.setText("");
        FechaDC.setDate(new Date());
        EmpresaCB.setSelectedItem(EmpresaCB.getItemAt(0));
        LocalizacionCB.setSelectedItem(LocalizacionCB.getItemAt(0));
        DepartamentoCB.setSelectedItem(DepartamentoCB.getItemAt(0));
        MonedaCB.setSelectedItem(MonedaCB.getItemAt(0));
        ValorCambioTxt.setText("0");
        CodigoProvTxt.setText("");
        ProveedorLbl.setText("");
        DetalleTxt.setText("COMPRA");
        CodigoArtTxt.setText("");
        idArt = -1;
        ArticuloLbl.setText("");
        CantidadTxt.setText("");
        CantBonifTxt.setText("0");
        UnidMedCB.setSelectedItem(UnidMedCB.getItemAt(0));
        MontUnitTxt.setText("");
        AfectaCB.setSelectedItem(AfectaCB.getItemAt(0));
        TipoDescCB.setSelectedItem(TipoDescCB.getItemAt(0));
        ValorTxt.setText("0");
        SubLbl.setText("0");
        PorcentLbl.setText("0");
        MontoDescLbl.setText("0");
        ImpLbl.setText("0");
        TotLbl.setText("0");
        SubtotalLbl.setText("0");
        DescuentoLbl.setText("0");
        ImpuestoLbl.setText("0");
        TotalLbl.setText("0");
        configurarTabla();
        for (int i = 0; i < 4; i++) {
            total[i] = 0;
        }
        CodigoProvTxt.requestFocus();
        EmpresaCB.setEnabled(true);
    }

    //Método para incluir una línea en la factura de compra.
    public void incluirDetalle() throws MyException {
        try {
            crearTrans();
            if (StringUtils.isEmpty(ArticuloLbl.getText())) {
                throw new MyException("Articulo Invalido");
            }
            if (StringUtils.isEmpty(CantidadTxt.getText()) || !NumberUtils.isNumber(CantidadTxt.getText())) {
                throw new MyException("Cantidad Invalida");
            }
            if (StringUtils.isEmpty(CantBonifTxt.getText()) || !NumberUtils.isNumber(CantBonifTxt.getText())) {
                throw new MyException("Cant Bonificada Invalida");
            }
            if (StringUtils.isEmpty(MontUnitTxt.getText()) || !NumberUtils.isNumber(MontUnitTxt.getText())) {
                throw new MyException("Monto Unitario Invalido");
            }
            if (StringUtils.isEmpty(ValorTxt.getText()) || !NumberUtils.isNumber(ValorTxt.getText())) {
                throw new MyException("Descuento Invalido");
            }

            String afecta = "N";
            if (AfectaCB.getSelectedItem().equals("Si")) {
                afecta = "S";
            }
            Item item;
            item = (Item) UnidMedCB.getSelectedItem();
            String selectedItem = String.valueOf(item.getId());
            int id = oraclebd.incluirDetalleCOM(Integer.parseInt(TransaccionLbl.getText()), idArt, Double.parseDouble(CantidadTxt.getText()),
                    Integer.parseInt(CantBonifTxt.getText()), Double.parseDouble(MontUnitTxt.getText()), Double.parseDouble(PorcentLbl.getText()),
                    0, Integer.parseInt(selectedItem), 0, afecta);
            if (id > -1) {
                configurarTabla();
                for (int i = 0; i < 4; i++) {
                    total[i] = 0;
                }
                oraclebd.getDetallesComp(modelo, Integer.parseInt(TransaccionLbl.getText()), total);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
                DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
                ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
                TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));

                CodigoArtTxt.setText("");
                idArt = -1;
                ArticuloLbl.setText("");
                CantidadTxt.setText("");
                CantBonifTxt.setText("0");
                UnidMedCB.setSelectedItem(UnidMedCB.getItemAt(0));
                MontUnitTxt.setText("");
                AfectaCB.setSelectedItem(AfectaCB.getItemAt(0));
                TipoDescCB.setSelectedItem(TipoDescCB.getItemAt(0));
                ValorTxt.setText("0");
                SubLbl.setText("0");
                PorcentLbl.setText("0");
                MontoDescLbl.setText("0");
                ImpLbl.setText("0");
                TotLbl.setText("0");
                CodigoArtTxt.requestFocus();
            }
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Método para actualizar los datos de Subtotal, % Descuento y Descuento de la linea de compra.
    public void actualizarLinea() {
        double[] sub = new double[5]; //Subtotal, Porcentaje, Monto, Impuesto, Total
        if (!ArticuloLbl.getText().isEmpty()) {
            if (!CantidadTxt.getText().isEmpty() && NumberUtils.isNumber(CantidadTxt.getText())) {
                if (!MontUnitTxt.getText().isEmpty() && NumberUtils.isNumber(MontUnitTxt.getText())) {
                    sub[0] = Double.parseDouble(CantidadTxt.getText()) * Double.parseDouble(MontUnitTxt.getText());
                    NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                    SubLbl.setText(String.valueOf(formatoNumero.format(sub[0])));
                    if (!ValorTxt.getText().isEmpty() && NumberUtils.isNumber(ValorTxt.getText())) {
                        if (TipoDescCB.getSelectedItem().equals("Porcentaje")) {
                            sub[1] = Double.parseDouble(ValorTxt.getText());
                            sub[2] = sub[0] * sub[1] / 100;
                            PorcentLbl.setText(String.valueOf(sub[1]));
                            MontoDescLbl.setText(String.valueOf(formatoNumero.format(sub[2])));
                        } else {
                            sub[2] = Double.parseDouble(ValorTxt.getText());
                            sub[1] = 100 * sub[2] / sub[0];
                            MontoDescLbl.setText(String.valueOf(formatoNumero.format(sub[2])));
                            PorcentLbl.setText(String.valueOf(sub[1]));
                        }
                        Item item;
                        item = (Item) EmpresaCB.getSelectedItem();
                        String empresa = String.valueOf(item.getId());
                        sub[3] = (sub[0] - sub[2]) * oraclebd.getImpuesto(Integer.parseInt(empresa), idArt) / 100;
                        ImpLbl.setText(String.valueOf(formatoNumero.format(sub[3])));
                        sub[4] = sub[0] - sub[2] + sub[3];
                        TotLbl.setText(String.valueOf(formatoNumero.format(sub[4])));
                    }
                }
            }
        }
    }

    public void registrar() {
        if (!TransaccionLbl.getText().equals("0")) {
            if (ComprasTbl.getRowCount() > 0) {
                boolean continuar_v = true;
                String proveedor[] = oraclebd.getProveedor(CodigoProvTxt.getText());
                Item item;
                item = (Item) TipoTransCB.getSelectedItem();
                int tipo_v = item.getId();
                String concre_v = oraclebd.getTipoCom(tipo_v);

                //Si el proveedor tiene limite y la factura es de contado, pregunta si 
                //desea continuar
                if (Double.parseDouble(proveedor[1]) > 0 && concre_v.equals("CON")) {
                    int seleccion = JOptionPane.showOptionDialog(
                            this, // Componente padre
                            "El proveedor es de credito pero la compra de contado ¿Continuar?", //Mensaje
                            "Credito/Contado", // Título
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, // null para icono por defecto.
                            new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                            "No");
                    if (seleccion == JOptionPane.NO_OPTION) {
                        continuar_v = false;
                    }
                }
                if (continuar_v == true) {
                    if (oraclebd.registrarCompra(Integer.parseInt(TransaccionLbl.getText()))) {
                        oraclebd.actualizausuario(Integer.parseInt(TransaccionLbl.getText()), 4, usuario_g);
                        JOptionPane.showMessageDialog(null, "Compra registrada exitosamente", "Registro Compra", JOptionPane.INFORMATION_MESSAGE);
                        limpiar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No existen artículos asociados a la Compra para Registrar.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No existe una Compra para Registrar.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Crear y Configurar la Tabla.
    public void configurarTabla() {
        modelo = new DefaultTableModel();
        for (int i = 0; i < 14; i++) {
            modelo.addColumn(ComprasTbl.getColumnName(i));
        }
        ComprasTbl.setModel(modelo);
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
        ComprasTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //Linea
        ComprasTbl.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        ComprasTbl.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(0)).setPreferredWidth(0);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(0)).setResizable(false);
        //Articulo
        ComprasTbl.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        ComprasTbl.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(1)).setPreferredWidth(0);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(1)).setResizable(false);        
        //Codigo
        ComprasTbl.getColumn(ComprasTbl.getColumnName(2)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(2)).setResizable(false);
        //Descripcion
        ComprasTbl.getColumn(ComprasTbl.getColumnName(3)).setPreferredWidth(200);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(3)).setResizable(false);
        //Cantidad
        ComprasTbl.getColumn(ComprasTbl.getColumnName(4)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(4)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(4)).setCellRenderer(render);
        //Bonificacion
        ComprasTbl.getColumn(ComprasTbl.getColumnName(5)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(5)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(5)).setCellRenderer(render);
        //Unidad de Medida
        ComprasTbl.getColumn(ComprasTbl.getColumnName(6)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(6)).setResizable(false);
        //Monto Unitario
        ComprasTbl.getColumn(ComprasTbl.getColumnName(7)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(7)).setResizable(false);        
        ComprasTbl.getColumn(ComprasTbl.getColumnName(7)).setCellRenderer(render);
        //Afecta
        ComprasTbl.getColumn(ComprasTbl.getColumnName(8)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(8)).setResizable(false);
        //Subtotal
        ComprasTbl.getColumn(ComprasTbl.getColumnName(9)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(9)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(9)).setCellRenderer(render);
        //% Descuento
        ComprasTbl.getColumn(ComprasTbl.getColumnName(10)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(10)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(10)).setCellRenderer(render);
        //Descuento
        ComprasTbl.getColumn(ComprasTbl.getColumnName(11)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(11)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(11)).setCellRenderer(render);
        //Impuesto
        ComprasTbl.getColumn(ComprasTbl.getColumnName(12)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(12)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(12)).setCellRenderer(render);
        //Total
        ComprasTbl.getColumn(ComprasTbl.getColumnName(13)).setPreferredWidth(100);
        //ComprasTbl.getColumn(ComprasTbl.getColumnName(13)).setResizable(false);
        ComprasTbl.getColumn(ComprasTbl.getColumnName(13)).setCellRenderer(render);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox AfectaCB;
    private javax.swing.JLabel ArticuloLbl;
    private javax.swing.JButton BuscarArtBtn;
    private javax.swing.JButton BuscarProvBtn;
    private javax.swing.JButton BuscarTransBtn;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JTextField CantBonifTxt;
    private javax.swing.JTextField CantidadTxt;
    private javax.swing.JTextField CodigoArtTxt;
    private javax.swing.JTextField CodigoProvTxt;
    private javax.swing.JTable ComprasTbl;
    private javax.swing.JButton CrearBtn;
    private javax.swing.JComboBox DepartamentoCB;
    private javax.swing.JLabel DescuentoLbl;
    private javax.swing.JTextField DetalleTxt;
    private javax.swing.JTextField DocumentoTxt;
    private javax.swing.JComboBox EmpresaCB;
    private com.toedter.calendar.JDateChooser FechaDC;
    private javax.swing.JLabel ImpLbl;
    private javax.swing.JLabel ImpuestoLbl;
    private javax.swing.JButton IncluirBtn;
    private javax.swing.JButton LimpiarBtn;
    private javax.swing.JComboBox LocalizacionCB;
    private javax.swing.JButton MenuBtn;
    private javax.swing.JComboBox MonedaCB;
    private javax.swing.JTextField MontUnitTxt;
    private javax.swing.JLabel MontoDescLbl;
    private javax.swing.JLabel PorcentLbl;
    private javax.swing.JLabel ProveedorLbl;
    private javax.swing.JButton RegistrarBtn;
    private javax.swing.JLabel SubLbl;
    private javax.swing.JLabel SubtotalLbl;
    private javax.swing.JComboBox TipoDescCB;
    private javax.swing.JComboBox TipoTransCB;
    private javax.swing.JLabel TotLbl;
    private javax.swing.JLabel TotalLbl;
    private javax.swing.JLabel TransaccionLbl;
    private javax.swing.JComboBox UnidMedCB;
    private javax.swing.JTextField ValorCambioTxt;
    private javax.swing.JTextField ValorTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
