/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public interface Clearable {
/*    */   void clearContent();
/*    */   
/*    */   static void tryClear(@Nullable Object debug0) {
/*  9 */     if (debug0 instanceof Clearable)
/* 10 */       ((Clearable)debug0).clearContent(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Clearable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */