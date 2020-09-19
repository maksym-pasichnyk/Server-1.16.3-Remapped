/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.function.LongSupplier;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleTickProfiler
/*    */ {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final LongSupplier realTime;
/*    */   
/*    */   private final long saveThreshold;
/*    */   
/*    */   private int tick;
/*    */   
/*    */   private final File location;
/*    */   
/*    */   private ProfileCollector profiler;
/*    */ 
/*    */   
/*    */   public ProfilerFiller startTick() {
/* 29 */     this.profiler = new ActiveProfiler(this.realTime, () -> this.tick, false);
/* 30 */     this.tick++;
/* 31 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public void endTick() {
/* 35 */     if (this.profiler == InactiveProfiler.INSTANCE) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     ProfileResults debug1 = this.profiler.getResults();
/* 40 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */     
/* 42 */     if (debug1.getNanoDuration() >= this.saveThreshold) {
/* 43 */       File debug2 = new File(this.location, "tick-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
/* 44 */       debug1.saveResults(debug2);
/* 45 */       LOGGER.info("Recorded long tick -- wrote info to: {}", debug2.getAbsolutePath());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static SingleTickProfiler createTickProfiler(String debug0) {
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public static ProfilerFiller decorateFiller(ProfilerFiller debug0, @Nullable SingleTickProfiler debug1) {
/* 58 */     if (debug1 != null) {
/* 59 */       return ProfilerFiller.tee(debug1.startTick(), debug0);
/*    */     }
/* 61 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\SingleTickProfiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */