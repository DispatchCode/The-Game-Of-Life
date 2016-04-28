/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: Implementa il View
 * License   : GNU/GPL
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import javax.swing.UIManager.*;

import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.*;

class GridGUI {
  //
  // ---------------------------------------------------------------------
  private JFrame frame;
  private Cell[][] cells;      // Rappresenta ogni singola cella
  private JButton pauseStart, restart, changeColor;
  private JMenuBar menuBar;
  private JCheckBox snapshot;
  private JPanel container;
  private int generation;
  private ImageIcon play, pause, refresh;
  
  public final int CIRCLE = 0, SQUARE = 1;
  // ---------------------------------------------------------------------
  
  GridGUI() {}
  
  // Crea la toolbar
  // -----------------------------------------------------------
  void makeToolbar(ActionListener al) 
  {
    play    = getImageIcon("play.png");
    pause   = getImageIcon("pause.png");
    refresh = getImageIcon("refresh.png");
    
    JToolBar toolbar = new JToolBar();
    pauseStart = new JButton(play);
    pauseStart.setActionCommand("Start");
    pauseStart.setToolTipText("Play/Pause");
    pauseStart.addActionListener(al);
    
    restart = new JButton(refresh);
    restart.setActionCommand("Restart");
    restart.setToolTipText("Restart the application.");
    restart.addActionListener(al);
    
    snapshot = new JCheckBox("Take Snapshots");
    snapshot.setToolTipText("Take snapshot every N ms");
    snapshot.addActionListener(al);
    
    
    toolbar.add(pauseStart);
    toolbar.addSeparator();
    toolbar.add(restart);
    toolbar.addSeparator();
    toolbar.add(snapshot);
    
    
    frame.add(toolbar,BorderLayout.NORTH);
  }
  // -----------------------------------------------------------
  
  // Crea il menu
  // -----------------------------------------------------------
  void makeMenu(ActionListener al) 
  {
    menuBar = new JMenuBar();
    
    JMenu menu = new JMenu("Select Example");
    menu.setToolTipText("Select preconfigured automa.");
    ButtonGroup bg = new ButtonGroup();
    
    JRadioButtonMenuItem glider = new JRadioButtonMenuItem("Glider (Aliante)");
    glider.setActionCommand("Glider");
    glider.setMnemonic(KeyEvent.VK_G);
    bg.add(glider);
    menu.add(glider);
    
    JRadioButtonMenuItem barca = new JRadioButtonMenuItem("Barca");
    barca.setEnabled(false);
    barca.setMnemonic(KeyEvent.VK_B);
    bg.add(barca);
    menu.add(barca);
    
    JRadioButtonMenuItem lampeggiatore = new JRadioButtonMenuItem("Lampeggiatore");
    lampeggiatore.setMnemonic(KeyEvent.VK_L);
    bg.add(lampeggiatore);
    menu.add(lampeggiatore);
  
    JRadioButtonMenuItem rospo = new JRadioButtonMenuItem("Rospo");
    rospo.setMnemonic(KeyEvent.VK_R);
    bg.add(rospo);
    menu.add(rospo);
    
    JRadioButtonMenuItem cannone = new JRadioButtonMenuItem("Cannone di Alianti di Gosper");
    cannone.setEnabled(false);
    cannone.setActionCommand("Cannone");
    cannone.setMnemonic(KeyEvent.VK_C);
    bg.add(cannone);
    menu.add(cannone);
    
    
    JMenu changeColor = new JMenu("Choose Color");
    changeColor.setToolTipText("Change color Cell");
    JMenuItem liveCell = new JMenuItem("Alive");
    changeColor.add(liveCell);
    JMenuItem deathCell = new JMenuItem("Death");
    changeColor.add(deathCell);
    
    ButtonGroup bg1 = new ButtonGroup();
    JMenu circleSquare = new JMenu("Circle/Square");
    JRadioButtonMenuItem circle = new JRadioButtonMenuItem("Circle");
    circle.setSelected(true);
    circleSquare.add(circle);
    bg1.add(circle);
    JRadioButtonMenuItem square = new JRadioButtonMenuItem("Square");
    circleSquare.add(square);
    bg1.add(square);
    
    
    JMenu about = new JMenu("About");
    JMenuItem information = new JMenuItem("Information...");
    about.add(information);
    
    glider.addActionListener(al);
    barca.addActionListener(al);
    lampeggiatore.addActionListener(al);
    rospo.addActionListener(al);
    cannone.addActionListener(al);
    
    liveCell.addActionListener(al);
    deathCell.addActionListener(al);
    
    circle.addActionListener(al);
    square.addActionListener(al);
    
    information.addActionListener(al);
    
    
    menuBar.add(menu);
    menuBar.add(changeColor);
    menuBar.add(circleSquare);
    menuBar.add(about);
    
    
    frame.setJMenuBar(menuBar);
  }
  // ----------------------------------------------------------
  
  
  // Crea i pannelli (in realta' JLabel)
  // ----------------------------------------------------------
  void createPanel(MouseListener ml, int d) 
  {
    cells = null;
    cells = new Cell[d][d];
    
    if(container != null) 
    {
      frame.remove(container);
      frame.pack();
    }
    
    container = null;
    container = new JPanel(new GridLayout(d,d));
    
    for(int i=0; i<d; i++) 
    {
      for(int j=0; j<d; j++) 
      {
        Cell cell = new Cell(i,j);
        //cell.updateType(CIRCLE); <-- Default value 0 = CIRCLE
        cell.setOpaque(true);
        cell.setBackground(Color.WHITE);    
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        cell.addMouseListener(ml);
        
        cells[i][j] = cell;
      
        container.add(cells[i][j]);
        
        cells[i][j].repaint();
        cells[i][j].revalidate();
      }
    }
    
    frame.add(container);
    frame.pack();
  }
  // -----------------------------------------------------------
  
  // Rimuove gli ascoltatori
  // viene chiamato alla pressione di Start
  // -----------------------------------------------------------
  void removeListener(MouseListener ml) 
  {
    for(int i=0; i<cells.length; i++) 
    {
      for(int j=0; j<cells.length; j++) 
      {
        cells[i][j].removeMouseListener(ml);
      }
    }
  }
  // -----------------------------------------------------------
  
  // Riceve lo stato interno della griglia ed aggiorna la GUI
  // -----------------------------------------------------------
  void setGrid(boolean[][] state) 
  {
    for(int i=0; i<state.length; i++) 
    {
      for(int j=0; j<state[0].length; j++) 
      {
        if(state[i][j]) 
        {
          cells[i][j].changeColor(ColorCell.aliveColorCell());
        } 
        else 
        {
          cells[i][j].changeColor(ColorCell.deathColorCell());
        }
        cells[i][j].repaint();
      }
    }
    
  }
  // ------------------------------------------------------------
  
  // Viene chiamato quando si vuole uno "screenshot" del pannello della griglia
  // (viene richiamato dal controller ogni N ms se flaggato)
  void makeSnapshots() 
  {
    try 
    {
      BufferedImage im = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_ARGB);
      container.paint(im.getGraphics());
      ImageIO.write(im, "PNG", new File("shot"+generation+".png"));
      im = null;
      
      System.gc();
    } catch(Exception e) {e.printStackTrace();}
    generation++;
  }
  // -------------------------------------------------------------
  
  
  // Modifica le impostazioni di default (i colori) sulle celle vive e morte
  void changeAliveColorCell() 
  {
    Color newColor = JColorChooser.showDialog(
                     frame,
                     "Choose Alive Color Cell",
                     ColorCell.aliveColorCell());
    if(newColor != null) ColorCell.setAliveColor(newColor);
  }
  
  void changeDeathColorCell() 
  {
    Color newColor = JColorChooser.showDialog(
                     frame,
                     "Choose Death Color Cell",
                     ColorCell.deathColorCell());
    if(newColor != null) ColorCell.setDeathColor(newColor);
    
  }
  // -------------------------------------------------------------
  
  // Riporta allo stato iniziale il bottone "Start"
  // -------------------------------------------------------------
  void resetStartButton() 
  {
    System.gc();
    changeButtonState("Stop");
  }
  // -------------------------------------------------------------
  
  void changeButtonState(String str) 
  {
    if(str.equals("Start")) 
    {
      pauseStart.setIcon(pause);
      pauseStart.setActionCommand("Stop");
    } 
    else 
    {
      pauseStart.setIcon(play);
      pauseStart.setActionCommand("Start");
    }
  }
  
  void selectElement(String text) {
    int t = (text.equals("Circle")) ? CIRCLE : SQUARE;
  
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[0].length; j++) {
        cells[i][j].updateType(t);
      }
    }
  }
  
  
  // Mostra il frame
  // -------------------------------------------------------------
  void showFrame() 
  {
    frame.setVisible(true);
  }
  // -------------------------------------------------------------
  
  // Mostra una finestra con un messaggio
  // -------------------------------------------------------------
  void showMessage(String message, String title) 
  {
    JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
  }
  // -------------------------------------------------------------
  
  
  private ImageIcon getImageIcon(String nameImg) {
    Image img = null;
    
    try {
      img = ImageIO.read(getClass().getResource("icon/"+nameImg));
    } catch (IOException ex) { }
    
    return new ImageIcon(img);
  }
  
  
  // Crea un thread e lancia l'applicazione, creando il JFrame
  // -------------------------------------------------------------
  public void init() 
  {
    try 
    {
      SwingUtilities.invokeAndWait(new Runnable() 
      {
        public void run() 
        {
          makeGUI();
        }
      });
    } catch(Exception e) {}
  }
  // -------------------------------------------------------------
  
  // Crea la finestra
  // -------------------------------------------------------------
  private void makeGUI() 
  {
    int w = 600, h = 610;
    frame = new JFrame("The Game Of Life - by Marco C. (DispatchCode)");
    frame.setSize(w,h);
    frame.setMinimumSize(new Dimension(w,h));
    frame.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  // -------------------------------------------------------------
}