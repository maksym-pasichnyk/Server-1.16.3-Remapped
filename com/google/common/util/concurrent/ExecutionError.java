/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible
/*    */ public class ExecutionError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected ExecutionError() {}
/*    */   
/*    */   protected ExecutionError(@Nullable String message) {
/* 41 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExecutionError(@Nullable String message, @Nullable Error cause) {
/* 48 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExecutionError(@Nullable Error cause) {
/* 55 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ExecutionError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */