/*    */ package net.minecraft.world;
/*    */ 
/*    */ public class InteractionResultHolder<T> {
/*    */   private final InteractionResult result;
/*    */   private final T object;
/*    */   
/*    */   public InteractionResultHolder(InteractionResult debug1, T debug2) {
/*  8 */     this.result = debug1;
/*  9 */     this.object = debug2;
/*    */   }
/*    */   
/*    */   public InteractionResult getResult() {
/* 13 */     return this.result;
/*    */   }
/*    */   
/*    */   public T getObject() {
/* 17 */     return this.object;
/*    */   }
/*    */   
/*    */   public static <T> InteractionResultHolder<T> success(T debug0) {
/* 21 */     return new InteractionResultHolder<>(InteractionResult.SUCCESS, debug0);
/*    */   }
/*    */   
/*    */   public static <T> InteractionResultHolder<T> consume(T debug0) {
/* 25 */     return new InteractionResultHolder<>(InteractionResult.CONSUME, debug0);
/*    */   }
/*    */   
/*    */   public static <T> InteractionResultHolder<T> pass(T debug0) {
/* 29 */     return new InteractionResultHolder<>(InteractionResult.PASS, debug0);
/*    */   }
/*    */   
/*    */   public static <T> InteractionResultHolder<T> fail(T debug0) {
/* 33 */     return new InteractionResultHolder<>(InteractionResult.FAIL, debug0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> InteractionResultHolder<T> sidedSuccess(T debug0, boolean debug1) {
/* 41 */     return debug1 ? success(debug0) : consume(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\InteractionResultHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */