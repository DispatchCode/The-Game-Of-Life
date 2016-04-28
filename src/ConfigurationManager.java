class ConfigurationManager 
{
  
  private ConfigurationManager() {}
  
  static boolean[][] loadConfiguration(String str, int d) 
  {
    ConfigurationInterface conf = null;
    
    switch(str) 
    {
      case "Glider":
        conf = new GliderConfiguration(d);
      break;
      case "Lampeggiatore":
        conf = new LampeggiatoreConfiguration(d);
      break;
      case "Rospo":
        conf = new RospoConfiguration(d);
      break;
    }
    
    return conf.loadConfiguration();
  }
}