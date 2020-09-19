/*    */ package net.minecraft.util;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class ExceptionCollector<T extends Throwable> {
/*    */   @Nullable
/*    */   private T result;
/*    */   
/*    */   public void add(T debug1) {
/* 10 */     if (this.result == null) {
/* 11 */       this.result = debug1;
/*    */     } else {
/* 13 */       this.result.addSuppressed((Throwable)debug1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void throwIfPresent() throws T {
/* 18 */     if (this.result != null)
/* 19 */       throw this.result; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\ExceptionCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */