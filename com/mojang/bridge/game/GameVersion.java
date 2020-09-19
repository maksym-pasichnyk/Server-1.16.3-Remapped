package com.mojang.bridge.game;

import java.util.Date;

public interface GameVersion {
  String getId();
  
  String getName();
  
  String getReleaseTarget();
  
  int getWorldVersion();
  
  int getProtocolVersion();
  
  int getPackVersion();
  
  Date getBuildTime();
  
  boolean isStable();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\bridge\game\GameVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */