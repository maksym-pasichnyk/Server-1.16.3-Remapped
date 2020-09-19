/*    */ package com.mojang.bridge.launcher;
/*    */ 
/*    */ import com.mojang.bridge.game.GameSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SessionEventListener
/*    */ {
/* 12 */   public static final SessionEventListener NONE = new SessionEventListener() {
/*    */     
/*    */     };
/*    */   
/*    */   default void onStartGameSession(GameSession session) {}
/*    */   
/*    */   default void onLeaveGameSession(GameSession session) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\bridge\launcher\SessionEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */