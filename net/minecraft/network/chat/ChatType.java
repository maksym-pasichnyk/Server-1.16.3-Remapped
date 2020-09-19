/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public enum ChatType {
/*  4 */   CHAT((byte)0, false),
/*  5 */   SYSTEM((byte)1, true),
/*  6 */   GAME_INFO((byte)2, true);
/*    */   
/*    */   private final byte index;
/*    */   
/*    */   private final boolean interrupt;
/*    */   
/*    */   ChatType(byte debug3, boolean debug4) {
/* 13 */     this.index = debug3;
/* 14 */     this.interrupt = debug4;
/*    */   }
/*    */   
/*    */   public byte getIndex() {
/* 18 */     return this.index;
/*    */   }
/*    */   
/*    */   public static ChatType getForIndex(byte debug0) {
/* 22 */     for (ChatType debug4 : values()) {
/* 23 */       if (debug0 == debug4.index) {
/* 24 */         return debug4;
/*    */       }
/*    */     } 
/* 27 */     return CHAT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\ChatType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */