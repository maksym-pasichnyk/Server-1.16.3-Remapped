/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public class TranslatableFormatException extends IllegalArgumentException {
/*    */   public TranslatableFormatException(TranslatableComponent debug1, String debug2) {
/*  5 */     super(String.format("Error parsing: %s: %s", new Object[] { debug1, debug2 }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableComponent debug1, int debug2) {
/*  9 */     super(String.format("Invalid index %d requested for %s", new Object[] { Integer.valueOf(debug2), debug1 }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableComponent debug1, Throwable debug2) {
/* 13 */     super(String.format("Error while parsing: %s", new Object[] { debug1 }), debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\TranslatableFormatException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */