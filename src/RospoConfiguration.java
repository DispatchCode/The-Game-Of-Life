class RospoConfiguration extends Configuration implements ConfigurationInterface 
{

  RospoConfiguration(int d) 
  {
    super(d);
  }

  
  public boolean[][] loadConfiguration() 
  {
     int d = matrix.length;
     int r = (d/2)-3;
     int c = (d/2)-3;
     
     matrix[r][c]     = true;
     matrix[r][c+1]   = true;
     matrix[r][c+2]   = true;
     
     matrix[r+1][c-1]     = true;
     matrix[r+1][c]   = true;
     matrix[r+1][c+1]   = true;
     
     return matrix;
   }
 }