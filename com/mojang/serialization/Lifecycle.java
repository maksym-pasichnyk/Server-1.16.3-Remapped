/*    */ package com.mojang.serialization;
/*    */ 
/*    */ 
/*    */ public class Lifecycle
/*    */ {
/*  6 */   private static final Lifecycle STABLE = new Lifecycle()
/*    */     {
/*    */       public String toString() {
/*  9 */         return "Stable";
/*    */       }
/*    */     };
/* 12 */   private static final Lifecycle EXPERIMENTAL = new Lifecycle()
/*    */     {
/*    */       public String toString() {
/* 15 */         return "Experimental";
/*    */       }
/*    */     };
/*    */   
/*    */   private Lifecycle() {}
/*    */   
/*    */   public static final class Deprecated
/*    */     extends Lifecycle {
/*    */     private final int since;
/*    */     
/*    */     public Deprecated(int since) {
/* 26 */       this.since = since;
/*    */     }
/*    */     
/*    */     public int since() {
/* 30 */       return this.since;
/*    */     }
/*    */   }
/*    */   
/*    */   public static Lifecycle experimental() {
/* 35 */     return EXPERIMENTAL;
/*    */   }
/*    */   
/*    */   public static Lifecycle stable() {
/* 39 */     return STABLE;
/*    */   }
/*    */   
/*    */   public static Lifecycle deprecated(int since) {
/* 43 */     return new Deprecated(since);
/*    */   }
/*    */   
/*    */   public Lifecycle add(Lifecycle other) {
/* 47 */     if (this == EXPERIMENTAL || other == EXPERIMENTAL) {
/* 48 */       return EXPERIMENTAL;
/*    */     }
/* 50 */     if (this instanceof Deprecated) {
/* 51 */       if (other instanceof Deprecated && ((Deprecated)other).since < ((Deprecated)this).since) {
/* 52 */         return other;
/*    */       }
/* 54 */       return this;
/*    */     } 
/* 56 */     if (other instanceof Deprecated) {
/* 57 */       return other;
/*    */     }
/* 59 */     return STABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Lifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */