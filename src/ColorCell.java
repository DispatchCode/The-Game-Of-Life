/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: Colore in base allo stato della cella, 
 *           : con possibilita' di modifica.
 * License   : GNU/GPL
 */
 

import java.awt.Color;


enum ColorCell 
{
  ALIVE(Color.BLACK), // Colori di default
  DEATH(Color.WHITE);
  private Color color;
  
  private ColorCell(Color color) 
  {
    this.color = color;
  }
  
  static Color aliveColorCell() 
  {
    return ALIVE.color;
  }
  
  static Color deathColorCell() 
  {
    return DEATH.color;
  }
  
  static void setAliveColor(Color color) 
  {
    ALIVE.color = color;
  }
  
  static void setDeathColor(Color color) 
  {
    DEATH.color = color;
  }
}