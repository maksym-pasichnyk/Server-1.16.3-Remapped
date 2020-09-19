package com.mojang.bridge.game;

import com.mojang.bridge.launcher.SessionEventListener;

public interface RunningGame {
  GameVersion getVersion();
  
  Language getSelectedLanguage();
  
  GameSession getCurrentSession();
  
  PerformanceMetrics getPerformanceMetrics();
  
  void setSessionEventListener(SessionEventListener paramSessionEventListener);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\bridge\game\RunningGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */