/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: Main, punto di ingresso
 * License   : GNU/GPL
 */


// Main del programma
class GameOfLife 
{
  public static void main(String[] args) 
  {
    GridModel gm = new GridModel(20); // Matrix dimension
    GridGUI   gg = new GridGUI();
    GridController gc = new GridController(gm,gg);
  }
}