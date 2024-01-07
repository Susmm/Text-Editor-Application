/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Myeditor;

//import java.awt.Event;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author susmit
 */
/*class MyCheckBoxMenuItemPainter extends AbstractRegionPainter {
  static final int CHECKICON_ENABLED_SELECTED   = 6;
  static final int CHECKICON_SELECTED_MOUSEOVER = 7;
  static final int CHECKICON_ENABLED            = 8;
  static final int CHECKICON_MOUSEOVER          = 9;
  private int state;
  private PaintContext ctx;
  public MyCheckBoxMenuItemPainter(int state) {
    super();
    this.state = state;
    this.ctx = new AbstractRegionPainter.PaintContext(
      new Insets(5, 5, 5, 5), new Dimension(9, 10), false, null, 1.0, 1.0);
  }
  @Override
  protected void doPaint(Graphics2D g, JComponent c,
               int width, int height, Object[] eckey) {
    switch(state) {
      case CHECKICON_ENABLED:
        paintcheckIconEnabled(g);              break;
      case CHECKICON_MOUSEOVER:
        paintcheckIconMouseOver(g);            break;
      case CHECKICON_ENABLED_SELECTED:
        paintcheckIconEnabledAndSelected(g);   break;
      case CHECKICON_SELECTED_MOUSEOVER:
        paintcheckIconSelectedAndMouseOver(g); break;
    }
  }
  @Override
  protected final PaintContext getPaintContext() {
    return ctx;
  }
  private void paintcheckIconEnabled(Graphics2D g) {
      g.setPaint(Color.GREEN);
      g.drawOval( 0, 0, 10, 10 );
  }
  private void paintcheckIconMouseOver(Graphics2D g) {
      g.setPaint(Color.PINK);
      g.drawOval( 0, 0, 10, 10 );
  }
  private void paintcheckIconEnabledAndSelected(Graphics2D g) {
    g.setPaint(Color.ORANGE);
    g.fillOval( 0, 0, 10, 10 );
  }
  private void paintcheckIconSelectedAndMouseOver(Graphics2D g) {
    g.setPaint(Color.CYAN);
    g.fillOval( 0, 0, 10, 10 );
  }
}*/
class FillPainter implements Painter<JComponent> {

    private final Color color;

    FillPainter(Color c) {
        color = c;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }
}

class MyScrollBarUI extends BasicScrollBarUI {

    private final java.awt.Dimension d = new java.awt.Dimension();

    @Override
    protected javax.swing.JButton createDecreaseButton(int orientation) {
        return new javax.swing.JButton() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                return d;
            }
        };
    }

    @Override
    protected javax.swing.JButton createIncreaseButton(int orientation) {
        return new javax.swing.JButton() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                return d;
            }
        };
    }

    @Override
    protected void paintTrack(java.awt.Graphics g, JComponent c, java.awt.Rectangle r) {
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = null;
        JScrollBar sb = (JScrollBar) c;
        if (!sb.isEnabled() || r.width > r.height) {
            return;
        } else if (isDragging) {
            color = Color.DARK_GRAY;
        } else if (isThumbRollover()) {
            color = Color.LIGHT_GRAY;
        } else {
            color = Color.GRAY;
        }
        g2.setPaint(color);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
        g2.setPaint(Color.WHITE);
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}
class ChangeListener implements javax.swing.event.DocumentListener {
            // implement the methods
            javax.swing.JButton[] buttons;
            
            public ChangeListener(javax.swing.JButton[] buttons){ this.buttons=buttons; }
            
            public void update(DocumentEvent e) {
                // Your code here
                //jButton5.setEnabled(false);
                for(javax.swing.JButton button : buttons) button.setEnabled(false);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }
}
public class TextEditor extends javax.swing.JFrame {

    /**
     * Creates new form TextEditor
     */
    UndoManager undoManager = new UndoManager();
    private String content = "";
    private java.awt.Font font = null;
    private int bg = -1, fg = -1, lastfg, lastbg;

    private final Color[] color = {Color.red, Color.blue, Color.green, Color.yellow, Color.black, Color.white, new Color(0xFF00FF), new Color(0x00FFFF), new Color(0xFF781F), Color.gray, new Color(0x393939), new Color(0xFFC0CB), new Color(0xFF1493)};

    private boolean custom = true, canceled = false;
    private Color fgcol = null, customfg = null, bgcol = null;
    private int[] findin={-1,-1};

    java.awt.event.ActionListener okActionListener = null, cancelActionListener = null;
    javax.swing.JDialog dialog = null;
    javax.swing.ImageIcon help=new javax.swing.ImageIcon(getClass().getResource("/help2.png"));
    
    public TextEditor(String content) {
        this.content = content;
        /*try {
                   //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }*/

 /*UIManager.put("MenuBar.background", Color.RED);
                UIManager.put("Menu.background", Color.GREEN);
                UIManager.put("MenuItem.background", Color.MAGENTA);*/
        initComponents();
        //jLabel13.setIcon(new javax.swing.ImageIcon(help.getImage().getScaledInstance(18,18,java.awt.Image.SCALE_SMOOTH)));
        /*javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
        this.setIconImage(help.getImage());     //Doesn't work on ubuntu
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("/help3.png"));*/
        fg = jComboBox4.getSelectedIndex();
        bg = jComboBox5.getSelectedIndex();
        lastfg = fg;
        lastbg = bg;

        jColorChooser1.getSelectionModel().addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent arg0) {
                Color color = jColorChooser1.getColor();
                //System.out.println(color);
                jTextField3.setForeground(color);
            }
        });
        cancelActionListener = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField3.setForeground(fgcol);
                canceled = true; //jComboBox4.setSelectedIndex(lastfg);
                dialog.dispose();
            }
        };
        okActionListener = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField3.setForeground(jColorChooser1.getColor());
                dialog.dispose();
            }
        };
        jTextArea1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, InputEvent.ALT_DOWN_MASK, false),
                "doSomething");
        jTextArea1.getActionMap().put("doSomething",
                new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("ALT"); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                jMenu1.setMnemonic(KeyEvent.VK_F);
                jMenu2.setMnemonic(KeyEvent.VK_E);
                jMenu3.setMnemonic(KeyEvent.VK_M);
            }
        });
        jTextArea1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true),
                "release");
        jTextArea1.getActionMap().put("release",
                new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("DOWN"); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                jMenu1.setMnemonic(0);
                jMenu2.setMnemonic(0);
                jMenu3.setMnemonic(0);
            }
        });
        //UIManager.put("MenuBar.selectionBackground",new Color(245,29,29));

        //UIManager.put("MenuItem.selectionForeground",Color.white);
        //jMenu1.setBackground(Color.red);
        //jMenu1.setOpaque(true);
        //jMenuBar1.setBackground(Color.blue);
        //jMenuBar1.setOpaque(true);
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        //
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);

        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);

        Document document = jTextArea1.getDocument();

        document.addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        // Map undo action
        jTextArea1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(undoKeyStroke, "undoKeyStroke");
        jTextArea1.getActionMap().put("undoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                } catch (CannotUndoException cue) {
                }
            }
        });
        // Map redo action
        jTextArea1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(redoKeyStroke, "redoKeyStroke");
        jTextArea1.getActionMap().put("redoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                } catch (CannotRedoException cre) {
                }
            }
        });

        customScrollPane(jScrollPane1, false);
        customScrollPane(jScrollPane11, true);
        customScrollPane(jScrollPane10, true);
        customScrollPane(jScrollPane12, true);
        customScrollPane(jComboBox4, true);
        customScrollPane(jComboBox5, true);
        //jScrollPane1.getHorizontalScrollBar().setUI(scroll);
        jTextField1.getDocument().addDocumentListener(new ChangeListener(new javax.swing.JButton[]{jButton5}));
        jTextField4.getDocument().addDocumentListener(new ChangeListener(new javax.swing.JButton[]{jButton8,jButton10,jButton11}));
        //jTextField5.getDocument().addDocumentListener(new ChangeListener(new javax.swing.JButton[]{jButton10,jButton11}));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog(this,true);
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList9 = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList10 = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList11 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jColorChooser1 = new javax.swing.JColorChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList7 = new javax.swing.JList<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList8 = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox<>();
        jDialog2 = new javax.swing.JDialog(this,false);
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jDialog3 = new javax.swing.JDialog(this,false);
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem(new javax.swing.text.DefaultEditorKit.CutAction());
        jMenuItem11 = new javax.swing.JMenuItem(new javax.swing.text.DefaultEditorKit.CopyAction());
        jMenuItem13 = new javax.swing.JMenuItem(new javax.swing.text.DefaultEditorKit.PasteAction());
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle("Fonts & Styles");
        jDialog1.setResizable(false);
        jDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                jDialog1WindowClosed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(244, 244, 244));

        jPanel4.setBackground(new java.awt.Color(244, 244, 244));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        jList9.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jList9.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList9.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList9ValueChanged(evt);
            }
        });
        jScrollPane10.setViewportView(jList9);
        populateFonts();

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Plain");
        jLabel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), null));
        jLabel5.setMaximumSize(new java.awt.Dimension(40, 18));
        jLabel5.setMinimumSize(new java.awt.Dimension(40, 18));
        jLabel5.setOpaque(true);
        jLabel5.setPreferredSize(new java.awt.Dimension(40, 18));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Lato", 0, 16)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField3.setText("A quick brown fox jumps over the lazy dog");
        jTextField3.setFocusable(false);
        jTextField3.setHighlighter(null);

        jLabel6.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Preview");

        jSpinner3.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(16, 3, null, 1));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jList10.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jList10.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "3","5","8","10","12","14","18","24","36","48" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList10.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList10ValueChanged(evt);
            }
        });
        jScrollPane11.setViewportView(jList10);

        jList11.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jList11.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "<html>Plain</html>", "<html><b>Bold</b></html>", "<html><i>Italic</i></html>", "<html><b><i>Bold Italic</i></b></html>" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList11.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList11ValueChanged(evt);
            }
        });
        jScrollPane12.setViewportView(jList11);

        jLabel1.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Font:");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Lato");
        jLabel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), null));
        jLabel3.setOpaque(true);

        jComboBox4.setFont(new java.awt.Font("Nimbus Sans", 0, 12)); // NOI18N
        jComboBox4.setMaximumRowCount(7);
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<html><span style=\"color:red\">\u2B1B&ensp;Red</span></html>","<html><span style=\"color:blue\">\u2B1B&ensp;Blue</span></html>","<html><span style=\"color:green\">\u2B1B&ensp;Green</span></html>","<html><span style=\"color:yellow\">\u2B1B&ensp;Yellow</span></html>","<html><span style=\"color:black\">\u2B1B&ensp;Black</span></html>","<html><span style=\"color:white\">\u2B1B&ensp;White</span></html>","<html><span style=\"color:#FF00FF\">\u2B1B&ensp;Magenta</span></html>","<html><span style=\"color:#00FFFF\">\u2B1B&ensp;Cyan</span></html>","<html><span style=\"color:orange\">\u2B1B&ensp;Orange</span></html>","<html><span style=\"color:gray\">\u2B1B&ensp;Gray</span></html>","<html><span style=\"color:#393939\">\u2B1B&ensp;D.Gray</span></html>","<html><span style=\"color:#FFC0CB\">\u2B1B&ensp;Pink</span></html>","<html><span style=\"color:#FF1493\">\u2B1B&ensp;D.Pink</span></html>","<html><span style=\"background-image: linear-gradient(to right, red, orange, yellow, green, blue, indigo, violet);-webkit-background-clip: text;color: red;\">\u2B1B&ensp;</span><span style=\"color:orange\">C</span><span style=\"color:yellow\">u</span><span style=\"color:green\">s</span><span style=\"color:blue\">t</span><span style=\"color:#4b0082\">o</span><span style=\"color:#9400d3\">m</span></html>" }));
        jComboBox4.setSelectedIndex(4);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });
        jComboBox4.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBox4PopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jComboBox5.setFont(new java.awt.Font("Nimbus Sans", 0, 12)); // NOI18N
        jComboBox5.setMaximumRowCount(5);
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<html><span style=\"color:red\">\u2B1B&ensp;Red</span></html>","<html><span style=\"color:blue\">\u2B1B&ensp;Blue</span></html>","<html><span style=\"color:green\">\u2B1B&ensp;Green</span></html>","<html><span style=\"color:yellow\">\u2B1B&ensp;Yellow</span></html>","<html><span style=\"color:black\">\u2B1B&ensp;Black</span></html>","<html><span style=\"color:white\">\u2B1B&ensp;White</span></html>","<html><span style=\"color:#FF00FF\">\u2B1B&ensp;Magenta</span></html>","<html><span style=\"color:#00FFFF\">\u2B1B&ensp;Cyan</span></html>","<html><span style=\"color:orange\">\u2B1B&ensp;Orange</span></html>","<html><span style=\"color:gray\">\u2B1B&ensp;Gray</span></html>","<html><span style=\"color:#393939\">\u2B1B&ensp;D.Gray</span></html>","<html><span style=\"color:#FFC0CB\">\u2B1B&ensp;Pink</span></html>","<html><span style=\"color:#FF1493\">\u2B1B&ensp;D.Pink</span></html>","<html><span style=\"color: red;\">\u2B1B&ensp;</span><span style=\"color:orange\">C</span><span style=\"color:yellow\">u</span><span style=\"color:green\">s</span><span style=\"color:blue\">t</span><span style=\"color:#4b0082\">o</span><span style=\"color:#9400d3\">m</span></html>" }));
        jComboBox5.setSelectedIndex(5);
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Nimbus Sans", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("FG:");

        jLabel8.setFont(new java.awt.Font("Nimbus Sans", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("BG:");

        jLabel9.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Style:");

        jLabel10.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Size:");

        jLabel11.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Color:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jSpinner3, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(16, 16, 16))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jButton1.setFont(new java.awt.Font("Liberation Sans", 0, 13)); // NOI18N
        jButton1.setText("  OK  ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Liberation Sans", 0, 13)); // NOI18N
        jButton2.setText("Apply");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Liberation Sans", 0, 13)); // NOI18N
        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenuItem15.setText("jMenuItem15");
        jPopupMenu1.add(jMenuItem15);

        jList6.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jList6.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "3","5","8","10","12","14","18","24","36","48" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(jList6);

        jList7.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jList7.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Plain", "Bold", "Italic", "Bold Italic" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(jList7);

        jList8.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(jList8);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Nimbus Sans", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Plain");
        jLabel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), null));
        jLabel4.setMaximumSize(new java.awt.Dimension(40, 18));
        jLabel4.setMinimumSize(new java.awt.Dimension(40, 18));
        jLabel4.setOpaque(true);
        jLabel4.setPreferredSize(new java.awt.Dimension(40, 18));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField2.setText("A quick brown fox jumps over the lazy dog");

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Preview");

        jSpinner2.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<html><b>Item 1</b></html>", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                        .addGap(16, 16, 16)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jDialog2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog2.setTitle("Find");
        //\u2013\u2A00
        jDialog2.setIconImage(null);
        jDialog2.setResizable(false);

        jLabel13.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 0, 255));
        jLabel13.setText("Find:    ");

        jCheckBox1.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Case-sensitive");

        jCheckBox2.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Word wrap");

        jButton4.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton4.setText("Find");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton5.setText("Find Next");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox2))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(36, 36, 36)
                        .addComponent(jTextField1))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(20, 20, 20))
        );

        jDialog2.getContentPane().setBackground(new Color(244,244,244));

        jDialog3.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog3.setTitle("Find & Replace");
        jDialog3.setResizable(false);

        jLabel14.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 255));
        jLabel14.setText("Find:    ");

        jLabel15.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 255));
        jLabel15.setText("Replace:");

        jCheckBox3.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Case-sensitive");

        jCheckBox4.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Word wrap");

        jButton7.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton7.setText("Find");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton8.setText("Find Next");
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton10.setText("Replace");
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton11.setText("Replace All");
        jButton11.setEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        jButton12.setText("Cancel");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addComponent(jCheckBox3)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(37, 37, 37)
                        .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField5)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                        .addContainerGap(32, Short.MAX_VALUE))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jDialog3Layout.createSequentialGroup()
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog3Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(110, 110, 110)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4))
                .addGap(18, 18, 18)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jDialog3.getContentPane().setBackground(new Color(244,244,244));

        jLabel12.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        jLabel12.setText("Replace?");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel12)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Untitled");
        setIconImage(help.getImage());
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Lato", 0, 16)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText(content);
        jTextArea1.setSelectionColor(new java.awt.Color(255, 51, 0));
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);
        //jTextArea1.setFont(new java.awt.Font(jTextArea1.getFont().getName(), 0, 15));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );

        jMenuBar1.setToolTipText("Path: ~/NetBeansProjects/Text_Editor/src/Myeditor");
        jMenuBar1.setPreferredSize(new java.awt.Dimension(138, 23));

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setText("New");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Open");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItem3.setText("Close");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setText("Undo");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setText("Redo");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem12.setText("Cut");
        jMenu2.add(jMenuItem12);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem11.setText("Copy");
        jMenu2.add(jMenuItem11);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem13.setText("Paste");
        jMenu2.add(jMenuItem13);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem10.setText("Find");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem16.setText("Replace");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem14.setText("Delete");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setText("Zoom in");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem9.setText("Zoom out");
        jMenu2.add(jMenuItem9);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Format");
        jMenu3.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N

        jMenuItem7.setText("Font");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Line Wrap");
        jMenu3.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void populateFonts() {
        String[] font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        DefaultListModel listModel1 = new DefaultListModel();
        for (String f : font) {
            listModel1.addElement(f);
        }
        jList9.setModel(listModel1);
    }

    private void customScrollPane(javax.swing.JComponent jcomp, boolean opaque) {
        javax.swing.JScrollPane scrollpane = null;
        if (jcomp instanceof javax.swing.JComboBox) {
            //System.out.println("yes");
            javax.swing.JComboBox jc = (javax.swing.JComboBox<String>) jcomp;
            Object comp = jc.getUI().getAccessibleChild(jc, 0);

            if (comp instanceof javax.swing.JPopupMenu) {
                javax.swing.JPopupMenu popup = (javax.swing.JPopupMenu) comp;
                scrollpane = (javax.swing.JScrollPane) popup.getComponent(0);
            }
        } else {
            scrollpane = (javax.swing.JScrollPane) jcomp;
        }
        scrollpane.setComponentZOrder(scrollpane.getVerticalScrollBar(), 0);
        scrollpane.setComponentZOrder(scrollpane.getViewport(), 1);
        scrollpane.getVerticalScrollBar().setOpaque(opaque);

        scrollpane.setLayout(new javax.swing.ScrollPaneLayout() {
            @Override
            public void layoutContainer(java.awt.Container parent) {
                javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) parent;

                java.awt.Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                java.awt.Insets parentInsets = parent.getInsets();
                availR.x = parentInsets.left;
                availR.y = parentInsets.top;
                availR.width -= parentInsets.left + parentInsets.right;
                availR.height -= parentInsets.top + parentInsets.bottom;

                java.awt.Rectangle vsbR = new java.awt.Rectangle();
                vsbR.width = 12;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
        MyScrollBarUI scroll = new MyScrollBarUI();
        scrollpane.getVerticalScrollBar().setUI(scroll);
    }
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser filechooser = new javax.swing.JFileChooser(new File("."));
        int response = filechooser.showSaveDialog(null);
        if (response == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter fileOut = null;
            file = new File(filechooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new PrintWriter(file);
                fileOut.println(jTextArea1.getText());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                fileOut.close();
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser filechooser = new javax.swing.JFileChooser(new File("."));
        javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("Text Files(.txt)", "txt");
        //filechooser.setFileFilter(filter);
        int response = filechooser.showOpenDialog(null);
        if (response == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file = new File(filechooser.getSelectedFile().getAbsolutePath());
            Scanner fileIn = null;
            try {
                fileIn = new Scanner(file);
                jTextArea1.setText("");
                StringBuffer text = new StringBuffer("");
                if (file.isFile()) {
                    if (fileIn.hasNextLine()) {
                        String line = fileIn.nextLine();
                        text.append(line);
                    }
                    while (fileIn.hasNextLine()) {
                        String line = "\n" + fileIn.nextLine();
                        text.append(line);
                        //System.out.print(text);
                    }
                    //System.out.println(text);
                    jTextArea1.append(new String(text));
                    this.setTitle(filechooser.getSelectedFile().getName());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                fileIn.close();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        try {
            undoManager.redo();
        } catch (CannotRedoException cre) {
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        try {
            undoManager.undo();
        } catch (CannotUndoException cue) {
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        TextEditor E = new TextEditor("");
        E.setVisible(true); //E.jTextArea1.setText("abc");
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jTextArea1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusGained
        // TODO add your handling code here:
        jMenu1.setMnemonic(0);
        jMenu2.setMnemonic(0);
        jMenu3.setMnemonic(0);
    }//GEN-LAST:event_jTextArea1FocusGained

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        jMenu1.setMnemonic(0);
        jMenu2.setMnemonic(0);
        jMenu3.setMnemonic(0);
    }//GEN-LAST:event_formWindowLostFocus

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        jTextArea1.replaceSelection(null);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        jDialog1.setSize(573, 484);
        //jLabel5.setSize(30, 18);
        jDialog1.setLocationRelativeTo(this);
        jDialog1.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jList10ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList10ValueChanged
        // TODO add your handling code here
        if (!jList10.isSelectionEmpty()) {
            jSpinner3.setValue(Integer.valueOf(jList10.getSelectedValue()));
        }
    }//GEN-LAST:event_jList10ValueChanged

    private void jList9ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList9ValueChanged
        // TODO add your handling code here:
        if (jList9.isSelectionEmpty()) {
            return;
        }
        jLabel3.setText(jList9.getSelectedValue());
        java.awt.Font f = jTextField3.getFont();
        jTextField3.setFont(new java.awt.Font(jLabel3.getText(), f.getStyle(), f.getSize()));
    }//GEN-LAST:event_jList9ValueChanged

    private void jList11ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList11ValueChanged
        // TODO add your handling code here:
        if (jList11.isSelectionEmpty()) {
            return;
        }
        jLabel5.setText(jList11.getSelectedValue());
        java.awt.Font f = jTextField3.getFont();
        jTextField3.setFont(new java.awt.Font(f.getFamily(), jList11.getSelectedIndex(), f.getSize()));
    }//GEN-LAST:event_jList11ValueChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        // TODO add your handling code here:
        int val = (Integer) jSpinner3.getValue();
        java.awt.Font f = jTextField3.getFont();
        jTextField3.setFont(new java.awt.Font(f.getFamily(), f.getStyle(), val));
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (font != null) {
            jTextArea1.setFont(font);
            if (fg < color.length) {
                jTextArea1.setForeground(color[fg]);
            } else {
                jTextArea1.setForeground(customfg);
            }
            jTextArea1.setBackground(color[bg]);
        }
        font = null;
        java.awt.Font f = jTextArea1.getFont();
        jTextField3.setFont(f);
        jTextField3.setForeground(jTextArea1.getForeground());
        jTextField3.setBackground(jTextArea1.getBackground());
        jLabel3.setText(f.getFamily());
        jLabel5.setText(jList11.getModel().getElementAt(f.getStyle()));
        jSpinner3.setValue(f.getSize());
        //if(fg<color.length) 
        jList9.clearSelection();
        jList10.clearSelection();
        jList11.clearSelection();
        jDialog1.dispose();
        jComboBox4.setSelectedIndex(fg);
        jComboBox5.setSelectedIndex(bg);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (font == null) {
            font = jTextArea1.getFont(); //bg=jTextArea1.getBackground(); fg=jTextArea1.getForeground();
            if (fg == color.length) {
                customfg = jTextArea1.getForeground();
            }
        }
        jTextArea1.setFont(jTextField3.getFont());
        jTextArea1.setForeground(jTextField3.getForeground());
        jTextArea1.setBackground(jTextField3.getBackground());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        font = null;
        fg = jComboBox4.getSelectedIndex();
        bg = jComboBox5.getSelectedIndex();
        lastfg = fg;
        lastbg = bg;
        jTextArea1.setFont(jTextField3.getFont());
        jTextArea1.setForeground(jTextField3.getForeground());
        jTextArea1.setBackground(jTextField3.getBackground());
        //fgcol=jTextField3.getForeground();
        jList9.clearSelection();
        jList10.clearSelection();
        jList11.clearSelection();
        jDialog1.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jDialog1WindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowClosed
        // TODO add your handling code here:
        if (font != null) {
            jTextArea1.setFont(font);
            jTextArea1.setForeground(color[fg]);
            jTextArea1.setBackground(color[bg]);
        }
        font = null;
        java.awt.Font f = jTextArea1.getFont();
        jTextField3.setFont(f);
        jTextField3.setForeground(jTextArea1.getForeground());
        jTextField3.setBackground(jTextArea1.getBackground());
        jLabel3.setText(f.getFamily());
        jLabel5.setText(jList11.getModel().getElementAt(f.getStyle()));
        jSpinner3.setValue(f.getSize());
        jComboBox4.setSelectedIndex(fg);
        jComboBox5.setSelectedIndex(bg);
        jList9.clearSelection();
        jList10.clearSelection();
        jList11.clearSelection();
        jDialog1.dispose();
    }//GEN-LAST:event_jDialog1WindowClosed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        int idx = jComboBox4.getSelectedIndex();
        if (idx < color.length) {
            jTextField3.setForeground(color[idx]);
            lastfg = idx;
        } else if (jDialog1.isVisible()) {
            Color c = null;
            if (custom) {
                //c=javax.swing.JColorChooser.showDialog(this, "Choose a color", Color.BLACK);
                //Color c=jColorChooser1.getColor();
                //jTextField3.setForeground(c);
                fgcol = jTextField3.getForeground();
                dialog = javax.swing.JColorChooser.createDialog(null, "Color Chooser",
                        true, jColorChooser1, okActionListener, cancelActionListener);
                dialog.setVisible(true);
            } else {
                if (canceled) {
                    jComboBox4.setSelectedIndex(lastfg);
                }
                canceled = false;
            }
            custom = !custom;
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        int idx = jComboBox5.getSelectedIndex();
        if (idx < color.length) {
            jTextField3.setBackground(color[idx]);
        }
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox4PopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox4PopupMenuCanceled
        // TODO add your handling code here:
        //if(jComboBox4.getSelectedIndex()==color.length) System.out.println("yes");
    }//GEN-LAST:event_jComboBox4PopupMenuCanceled

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        jDialog2.setSize(460, 250);
        //jLabel5.setSize(30, 18);
        jDialog2.setLocationRelativeTo(this);
        jDialog2.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
        jDialog3.setSize(460, 300);
        //jLabel5.setSize(30, 18);
        jDialog3.setLocationRelativeTo(this);
        jDialog3.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed
    private boolean findActionPerformed(javax.swing.JTextField textField,int in){
        String pat=textField.getText();
        if(pat.isEmpty()) return false;
        String txt=jTextArea1.getText(); findin[in]=txt.indexOf(pat);
        if(findin[in]==-1) return false;
        jTextArea1.grabFocus(); //jTextArea1.setFocusable(false); 
        jTextArea1.setSelectionStart(findin[in]); jTextArea1.setSelectionEnd(findin[in]+pat.length());
        return true;
    }
    private void findNextActionPerformed(javax.swing.JTextField textField,int in){
        if(findin[in]==-1) return;
        String pat=textField.getText(),txt=jTextArea1.getText();
        findin[in]=txt.indexOf(pat,findin[in]+pat.length());
        if(findin[in]==-1) return;
        jTextArea1.setSelectionStart(findin[in]); jTextArea1.setSelectionEnd(findin[in]+pat.length());
    }
    private void cancelActionPerformed(javax.swing.JDialog jDialog){
        jTextArea1.setSelectionStart(0); jTextArea1.setSelectionEnd(0); jDialog.dispose();
    }
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(findActionPerformed(jTextField1,0)) jButton5.setEnabled(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if(jButton5.isEnabled()) 
            findNextActionPerformed(jTextField1,0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        cancelActionPerformed(jDialog2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if(findActionPerformed(jTextField4,1)){ 
            jButton8.setEnabled(true); jButton10.setEnabled(true); jButton11.setEnabled(true);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if(jButton8.isEnabled()) findNextActionPerformed(jTextField4,1);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        jLabel12.setText("Replace \""+jTextField4.getText()+"\" with \""+jTextField5.getText()+"\" ?");
        int rc=javax.swing.JOptionPane.showConfirmDialog(jDialog3,jPanel5,"", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE,new javax.swing.ImageIcon(help.getImage().getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        jLabel12.setText("Replace all \""+jTextField4.getText()+"\" with \""+jTextField5.getText()+"\" ?");
        int rc=javax.swing.JOptionPane.showConfirmDialog(jDialog3,jPanel5,"", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE,new javax.swing.ImageIcon(help.getImage().getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        cancelActionPerformed(jDialog3);
    }//GEN-LAST:event_jButton12ActionPerformed

    /**
     * @param args the command line argument s
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
                    UIManager.getLookAndFeelDefaults().put("MenuBar:Menu[Selected].backgroundPainter",
                            new FillPainter(Color.BLUE));
                    //UIManager.getLookAndFeelDefaults().put("MenuBar:Menu[Selected].textForeground", Color.WHITE);
                    UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].backgroundPainter", new FillPainter(Color.BLUE));
                    UIManager.getLookAndFeelDefaults().put("MenuItem:MenuItemAccelerator.textForeground", Color.GRAY);
                    UIManager.getLookAndFeelDefaults().put("MenuItem.font", new java.awt.Font("Liberation Sans", 0, 14));
                    UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem.font", new java.awt.Font("Liberation Sans", 0, 14));
                    
                    //UIManager.getLookAndFeelDefaults().put("MenuItem.backgroundPainter",new FillPainter(new Color(250,248,248)));
                    
                    UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[MouseOver].backgroundPainter", new FillPainter(Color.BLUE));
                    UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[MouseOver+Selected].backgroundPainter", new FillPainter(Color.BLUE));
                    /*UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[Enabled].checkIconPainter",
               new MyCheckBoxMenuItemPainter(
                   MyCheckBoxMenuItemPainter.CHECKICON_ENABLED));*/
 /*UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[MouseOver].checkIconPainter",
               new MyCheckBoxMenuItemPainter(
                   MyCheckBoxMenuItemPainter.CHECKICON_MOUSEOVER));*/
 /*UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[Enabled+Selected].checkIconPainter",
               new MyCheckBoxMenuItemPainter(
                   MyCheckBoxMenuItemPainter.CHECKICON_ENABLED_SELECTED));*/
 /*UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem[MouseOver+Selected].checkIconPainter",
               new MyCheckBoxMenuItemPainter(
                   MyCheckBoxMenuItemPainter.CHECKICON_SELECTED_MOUSEOVER));*/
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".background", new Color(228, 228, 228));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", new Color(0x4682B4)); //new Color(153,153,255));
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TextEditor("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList10;
    private javax.swing.JList<String> jList11;
    private javax.swing.JList<String> jList6;
    private javax.swing.JList<String> jList7;
    private javax.swing.JList<String> jList8;
    private javax.swing.JList<String> jList9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
