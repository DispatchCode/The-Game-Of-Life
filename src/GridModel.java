/*
 * Author    : Marco 'RootkitNeo' C.
 * Desription: Implementa il Model (inizializza la griglia etc.)
 * License   : GNU/GPL
 */

import java.util.ArrayList;
import java.util.Random;


class GridModel 
{
  //
  // ------------------------------------------------------------------
  private boolean[][] grid1; // riflette lo stato della griglia
  private boolean[][] grid2; // Prossima modifica
  // ------------------------------------------------------------------
  
  //
  // --------------------------------------------------
  GridModel(int d) 
  {
    createEmptyWorld(d);
  }
  // --------------------------------------------------
  
  void createEmptyWorld() 
  {
    createEmptyWorld(getSize());
  }
  
  void createEmptyWorld(int d) 
  {
    grid1 = new boolean[d][d];
  }
  
  
  void liveCell(int x, int y) 
  {
    changeInternalState(x,y,true);
  }
  
  void dieCell(int x, int y) 
  {
    changeInternalState(x,y,false);
  }
  
  boolean isAlive(int x, int y) 
  {
    return grid1[x][y];
  }
  
  
  private void changeInternalState(int x, int y, boolean state) 
  {
    grid1[x][y] = state;
  }
  
  
  // Calcola lo step successivo (grid2) sulla base di quello precedente (grid1)
  // -------------------------------------------------------------------------
  void nextStep() 
  {
    grid2 = null;
    grid2 = new boolean[grid1.length][grid1.length];
  
    for(int i=0; i<grid1.length; i++) 
    {
      for(int j=0; j<grid1[0].length; j++) 
      {
        int neigh = getNeighboards(i,j);
         
         if(grid1[i][j] && neigh < 2) 
         {
           grid2[i][j] = false;
           continue;
         }
         if(grid1[i][j] && (neigh == 2 || neigh == 3)) 
         {
           grid2[i][j] = true;
           continue;
         }
         if(grid1[i][j] && neigh > 3) 
         {
           grid2[i][j] = false;
           continue;
         }
         if(!grid1[i][j] && neigh == 3) 
         {
           grid2[i][j] = true;
           continue;
         }    
      }
    }
  }
  // --------------------------------------------------------------------
  
  // Copia grid2 (stato successivo), in quello precedente
  // che diventera' quello attuale (sara' quello ad essere stampato)
  // --------------------------------------------------------------------
  void copy() 
  {
    for(int i=0; i<grid1.length; i++) 
    {
      for(int j=0; j<grid1[0].length; j++)
      {
        grid1[i][j] = grid2[i][j];
      }
    }
  }
  // --------------------------------------------------------------------
  
  // Se le matrici sono uguali il ciclo termina
  // --------------------------------------------------------------------
  boolean isGridEquals() 
  {
    for(int i=0; i<grid1.length; i++) 
    {
      for(int j=0; j<grid1[0].length; j++) 
      {
        if(grid1[i][j] != grid2[i][j]) return false;
      }
    }
    return true;
  }
  // --------------------------------------------------------------------
  
  // Restituisce lo stato interno del mondo (la griglia)
  // --------------------------------------------------------------------
  boolean[][] getState() 
  {
    return grid1;
  }
  // --------------------------------------------------------------------
  
  
  // Calcola i vicini di una cella
  // --------------------------------------------------------------------
  private int getNeighboards(int x, int y)
  {
    int n = 0;
    
    for(int i = x - 1; i <= (x+1) && i<grid1.length; i++) 
    {
      for(int j = y - 1; j <= (y+1) && j<grid1[0].length; j++) 
      {
        if(i < 0 || j < 0  || (x == i && j == y)) continue;
        
        n += (grid1[i][j]) ? 1 : 0;
      }
    }
    
    return n;
  }
  // --------------------------------------------------------------------
  
  // Se l'utente non seleziona celle, il programma ne popola 100 random
  // (in realta' non verifico se una cella e' gia' stata selezionata)
  // --------------------------------------------------------------------
  boolean[][] randomPopulation() 
  {
    Random rand = new Random();
    
    for(int i=0; i<100; i++) 
    {
      int x = rand.nextInt(20);
      int y = rand.nextInt(20);
      
      liveCell(x,y);
    }
    return grid1;
  }
  // --------------------------------------------------------------------

  // Restituisce la dimensione del mondo
  // --------------------------------------------------------------------
  int getSize() {
    return grid1.length;
  }
  // --------------------------------------------------------------------
  

  // In una prossima versione...
  void loadConfiguration(String sampleName) {
    grid1 = ConfigurationManager.loadConfiguration(sampleName,grid1.length);
  }
  
  
}