/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PointFree<T>
/*    */ {
/*    */   private volatile boolean initialized;
/*    */   @Nullable
/*    */   private Function<DynamicOps<?>, T> value;
/*    */   
/*    */   public Function<DynamicOps<?>, T> evalCached() {
/* 20 */     if (!this.initialized) {
/* 21 */       synchronized (this) {
/* 22 */         if (!this.initialized) {
/* 23 */           this.value = eval();
/* 24 */           this.initialized = true;
/*    */         } 
/*    */       } 
/*    */     }
/* 28 */     return this.value;
/*    */   }
/*    */   
/*    */   public abstract Function<DynamicOps<?>, T> eval();
/*    */   
/*    */   Optional<? extends PointFree<T>> all(PointFreeRule rule, Type<T> type) {
/* 34 */     return Optional.of(this);
/*    */   }
/*    */   
/*    */   Optional<? extends PointFree<T>> one(PointFreeRule rule, Type<T> type) {
/* 38 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 43 */     return toString(0);
/*    */   }
/*    */   
/*    */   public static String indent(int level) {
/* 47 */     return StringUtils.repeat("  ", level);
/*    */   }
/*    */   
/*    */   public abstract String toString(int paramInt);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\PointFree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */