package com.mojang.bridge.game;

import java.util.UUID;

public interface GameSession {
  int getPlayerCount();
  
  boolean isRemoteServer();
  
  String getDifficulty();
  
  String getGameMode();
  
  UUID getSessionId();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\bridge\game\GameSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */