/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: La singola cella che compone la griglia
 * License   : GNU/GPL
 */

import java.awt.*;
import javax.swing.*;


class Cell extends JLabel 
{
  //
  // --------------------------------------------------
  private int x, y;
  private Color color;
  private static int type;
  // --------------------------------------------------
  
  // 
  // --------------------------------------------------
  Cell(int x, int y) 
  {
    this.x = x;
    this.y = y;
    color = ColorCell.deathColorCell();
  }
  // --------------------------------------------------
  
  int indexX() 
  {
    return x;
  }
  int indexY() 
  {
    return y;
  }
  
  void changeColor(Color color) 
  {
    this.color = color;
    repaint(); // Al momento del cambio aggiorna subito il colore
  }
  
  void updateType(int type) {
    this.type = type;
    repaint();
  }
  
  @Override
  public void paintComponent(Graphics g) 
  {
    super.paintComponent(g);
    g.setPaintMode();
    g.setColor(color);
    if(type == 0) g.fillOval(5,5,20,20);
    else g.fillRect(5,5,20,20);
  }
}