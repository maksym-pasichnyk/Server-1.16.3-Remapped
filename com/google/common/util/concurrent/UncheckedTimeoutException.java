/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ public class UncheckedTimeoutException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public UncheckedTimeoutException() {}
/*    */   
/*    */   public UncheckedTimeoutException(@Nullable String message) {
/* 31 */     super(message);
/*    */   }
/*    */   
/*    */   public UncheckedTimeoutException(@Nullable Throwable cause) {
/* 35 */     super(cause);
/*    */   }
/*    */   
/*    */   public UncheckedTimeoutException(@Nullable String message, @Nullable Throwable cause) {
/* 39 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\UncheckedTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */