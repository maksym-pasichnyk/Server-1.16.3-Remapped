/*    */ package io.netty.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BooleanSupplier
/*    */ {
/* 32 */   public static final BooleanSupplier FALSE_SUPPLIER = new BooleanSupplier()
/*    */     {
/*    */       public boolean get() {
/* 35 */         return false;
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final BooleanSupplier TRUE_SUPPLIER = new BooleanSupplier()
/*    */     {
/*    */       public boolean get() {
/* 45 */         return true;
/*    */       }
/*    */     };
/*    */   
/*    */   boolean get() throws Exception;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\BooleanSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */