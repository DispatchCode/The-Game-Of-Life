class Configuration implements ConfigurationInterface 
{
  protected boolean[][] matrix;
  
  Configuration(int d) 
  {
    matrix = new boolean[d][d];
  }
  
  public boolean[][] loadConfiguration() 
  {
    return matrix;
  }
}