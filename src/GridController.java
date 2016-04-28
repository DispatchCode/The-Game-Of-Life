/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: Gestisce il dialogo tra Model e View 
 * License   : GNU/GPL
 */


import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenuItem;
import java.awt.*;

class GridController implements Runnable, ActionListener, MouseListener 
{
  //
  // -----------------------------------------------------------------------------
  private final Object LOCK = new Object();
  private GridModel gm;
  private GridGUI gg;
  private boolean state,restart, userChoice, snapGeneration;
  private Thread t;
  // ------------------------------------------------------------------------------
  
  //
  // ------------------------------------------------------------------------------
  GridController(GridModel gm, GridGUI gg) 
  {
    this.gm = gm;
    this.gg = gg;
    
    gg.init();
    gg.makeToolbar(this);
    gg.createPanel(this, gm.getSize());
    gg.makeMenu(this);
    gg.showFrame();
    
    createThread();
  }
  // ------------------------------------------------------------------------------
  
  // Gestisce la pressione sul bottone Start/Stop
  // ------------------------------------------------------------------------------
  public void actionPerformed(ActionEvent ae) 
  {
    JButton btn;
    JRadioButtonMenuItem radio;      
    
    if(ae.getSource() instanceof JButton) 
    {
      btn = (JButton) ae.getSource();
      String text = btn.getActionCommand();
      
      if(text.equals("Start")) 
      {
        synchronized(LOCK) 
        {
          state = true;
          restart = false;
          gg.removeListener(this);
          if(t.getState() != Thread.State.WAITING) t.start();
          gg.changeButtonState("Start");
          LOCK.notify();
        }
      }
      else if(text.equals("Stop")) 
      {
        state = false;
        gg.changeButtonState("Stop");
      }
      // Genera un nuovo mondo
      else if(text.equals("Restart")) 
      {
        state = false;
        restart = true;
        gm.createEmptyWorld();
        gg.createPanel(this,gm.getSize());
        gg.setGrid(gm.getState());
        gg.resetStartButton();
        createThread();
      }
      btn = null;
    }
    else if(ae.getSource() instanceof JRadioButtonMenuItem) 
    {
      
      radio = (JRadioButtonMenuItem) ae.getSource();
      String text = radio.getText();
      
      if(text.equals("Circle") || text.equals("Square")) {
        gg.selectElement(text);
        return;
      }
      
      gm.loadConfiguration(radio.getActionCommand());
      gg.setGrid(gm.getState());
      userChoice = true;
      return;
      
    }
    // Change color
    else if(ae.getSource() instanceof JMenuItem) 
    {
      JMenuItem item = (JMenuItem) ae.getSource();
      String text = item.getText();
      
      // Cambia i colori di default delle celle vive e morte
      if(text.equals("Alive")) 
      {
        gg.changeAliveColorCell();
      }
      else if(text.equals("Death")) 
      {
        gg.changeDeathColorCell();
      }
      else if(text.equals("Information...")) 
      {
       gg.showMessage("\tDeveloped by Marco 'RootkitNeo' C.\n\nThe Game Of Life.\n\nVersion: 2.0", "Information");
       return;
       
      }
      
      gg.setGrid(gm.getState());
      item = null;
      text = null;
      return;
    } 
    // JRadioButton
    else if(ae.getSource() instanceof JCheckBox) 
    {
      JCheckBox snap = (JCheckBox) ae.getSource();
      snapGeneration = snap.isSelected();
      
      snap = null;
      return;
    } 
    
    // Se viene premuto senza aver selezionato caselle, genera la griglia random
    if(!userChoice) gg.setGrid(gm.randomPopulation()); 
  }
  // ----------------------------------------------------------------------------
  
  // Crea un nuovo thread
  // ---------------------------------------------------------------------
  private void createThread() 
  {
    t = null;
    t = new Thread(this);
  }
  // ---------------------------------------------------------------------
  
  
  // Punto di ingresso del Thread (avvio dell'applicazione)
  // ----------------------------------------------------------------------
  public void run() 
  {
    // Se viene premuto restart (restart=true), interrompo esco dal ciclo
    while(!restart) 
    {
      // Utilizzo il lock implicito degli oggetti per sospendere un thread
      synchronized(LOCK) 
      {
        while(!state) 
        {
          try 
          {
            LOCK.wait();
          } catch(InterruptedException e) {e.printStackTrace();}
        }
        
        // Ottengo il mondo e lo passo alla grafica
        gg.setGrid(gm.getState());
        gm.nextStep(); // Calcola il mondo al tempo tn
        
        // Se il mondo corrente ed il successivo sono identici e' inutile continuare
        if(gm.isGridEquals()) 
        {
            gg.showMessage("I mondi sono uguali. Non cambierebbero a seguito di ulteriori iterazioni.", "Informazione!");
          return;
        }
        
        // Copio il nuovo mondo nel vecchio
        gm.copy();
        
        try 
        {
          Thread.sleep(1000);
        } catch(InterruptedException e) {e.printStackTrace();}
      }
      
      // Se flaggato permette di ottenere uno screenshot di ogni generazione
      if(snapGeneration) gg.makeSnapshots();
      
    }
  }
  // ----------------------------------------------------------------------
  
  // Ascolto la pressione del mouse su una cella. Questa operazione
  // fornisce uno stato iniziale per il nuovo mondo (crea la popolazione)
  // ----------------------------------------------------------------------
  public void mousePressed(MouseEvent me) 
  {
    Cell cell = (Cell) me.getSource();
    
    // Se la cella e' viva (gia' stata clickata)
    // allora la segno come "morta"
    if(gm.isAlive(cell.indexX(), cell.indexY())) 
    {
      gm.dieCell(cell.indexX(), cell.indexY());
      cell.changeColor(ColorCell.deathColorCell());
    }
    // Se la cella e' morta viene settata su viva
    else 
    {
      gm.liveCell(cell.indexX(), cell.indexY());
      cell.changeColor(ColorCell.aliveColorCell());
    }
    
    cell.repaint();
    cell = null;
    userChoice = true;  // Garantisce l'esistenza di una popolazione iniziale
  }
  // ---------------------------------------------------------------------
  
  // Non utilizzati
  public void mouseClicked(MouseEvent me) {}
  public void mouseEntered(MouseEvent me) {}
  public void mouseReleased(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}
  // ----------------------------------------------------------------------
}