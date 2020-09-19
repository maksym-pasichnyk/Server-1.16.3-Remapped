/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.IntSupplier;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ public class ContinuousProfiler {
/*    */   private final LongSupplier realTime;
/*    */   private final IntSupplier tickCount;
/*  9 */   private ProfileCollector profiler = InactiveProfiler.INSTANCE;
/*    */   
/*    */   public ContinuousProfiler(LongSupplier debug1, IntSupplier debug2) {
/* 12 */     this.realTime = debug1;
/* 13 */     this.tickCount = debug2;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 17 */     return (this.profiler != InactiveProfiler.INSTANCE);
/*    */   }
/*    */   
/*    */   public void disable() {
/* 21 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public void enable() {
/* 25 */     this.profiler = new ActiveProfiler(this.realTime, this.tickCount, true);
/*    */   }
/*    */   
/*    */   public ProfilerFiller getFiller() {
/* 29 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public ProfileResults getResults() {
/* 33 */     return this.profiler.getResults();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ContinuousProfiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */