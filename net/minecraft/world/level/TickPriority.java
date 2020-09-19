/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public enum TickPriority {
/*  4 */   EXTREMELY_HIGH(-3),
/*  5 */   VERY_HIGH(-2),
/*  6 */   HIGH(-1),
/*  7 */   NORMAL(0),
/*  8 */   LOW(1),
/*  9 */   VERY_LOW(2),
/* 10 */   EXTREMELY_LOW(3);
/*    */   
/*    */   private final int value;
/*    */ 
/*    */   
/*    */   TickPriority(int debug3) {
/* 16 */     this.value = debug3;
/*    */   }
/*    */   
/*    */   public static TickPriority byValue(int debug0) {
/* 20 */     for (TickPriority debug4 : values()) {
/* 21 */       if (debug4.value == debug0) {
/* 22 */         return debug4;
/*    */       }
/*    */     } 
/* 25 */     if (debug0 < EXTREMELY_HIGH.value) {
/* 26 */       return EXTREMELY_HIGH;
/*    */     }
/* 28 */     return EXTREMELY_LOW;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 32 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\TickPriority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */