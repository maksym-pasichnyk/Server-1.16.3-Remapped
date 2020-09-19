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
/*    */ public final class AttributeKey<T>
/*    */   extends AbstractConstant<AttributeKey<T>>
/*    */ {
/* 27 */   private static final ConstantPool<AttributeKey<Object>> pool = new ConstantPool<AttributeKey<Object>>()
/*    */     {
/*    */       protected AttributeKey<Object> newConstant(int id, String name) {
/* 30 */         return new AttributeKey(id, name);
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> AttributeKey<T> valueOf(String name) {
/* 39 */     return (AttributeKey<T>)pool.valueOf(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean exists(String name) {
/* 46 */     return pool.exists(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> AttributeKey<T> newInstance(String name) {
/* 55 */     return (AttributeKey<T>)pool.newInstance(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
/* 60 */     return (AttributeKey<T>)pool.valueOf(firstNameComponent, secondNameComponent);
/*    */   }
/*    */   
/*    */   private AttributeKey(int id, String name) {
/* 64 */     super(id, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\AttributeKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */