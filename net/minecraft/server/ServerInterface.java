package net.minecraft.server;

import net.minecraft.server.dedicated.DedicatedServerProperties;

public interface ServerInterface {
  DedicatedServerProperties getProperties();
  
  String getServerIp();
  
  int getServerPort();
  
  String getServerName();
  
  String getServerVersion();
  
  int getPlayerCount();
  
  int getMaxPlayers();
  
  String[] getPlayerNames();
  
  String getLevelIdName();
  
  String getPluginNames();
  
  String runCommand(String paramString);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */