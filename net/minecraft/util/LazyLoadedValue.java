/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class LazyLoadedValue<T> {
/*    */   private Supplier<T> factory;
/*    */   private T value;
/*    */   
/*    */   public LazyLoadedValue(Supplier<T> debug1) {
/* 10 */     this.factory = debug1;
/*    */   }
/*    */   
/*    */   public T get() {
/* 14 */     Supplier<T> debug1 = this.factory;
/* 15 */     if (debug1 != null) {
/* 16 */       this.value = debug1.get();
/* 17 */       this.factory = null;
/*    */     } 
/*    */     
/* 20 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\LazyLoadedValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */