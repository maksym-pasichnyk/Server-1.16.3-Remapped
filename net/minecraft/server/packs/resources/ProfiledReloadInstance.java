/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.ActiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfileResults;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ProfiledReloadInstance
/*    */   extends SimpleReloadInstance<ProfiledReloadInstance.State> {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger();
/* 20 */   private final Stopwatch total = Stopwatch.createUnstarted();
/*    */   
/*    */   public ProfiledReloadInstance(ResourceManager debug1, List<PreparableReloadListener> debug2, Executor debug3, Executor debug4, CompletableFuture<Unit> debug5) {
/* 23 */     super(debug3, debug4, debug1, debug2, (debug1, debug2, debug3, debug4, debug5) -> { AtomicLong debug6 = new AtomicLong(); AtomicLong debug7 = new AtomicLong(); ActiveProfiler debug8 = new ActiveProfiler(Util.timeSource, (), false); ActiveProfiler debug9 = new ActiveProfiler(Util.timeSource, (), false); CompletableFuture<Void> debug10 = debug3.reload(debug1, debug2, (ProfilerFiller)debug8, (ProfilerFiller)debug9, (), ()); return debug10.thenApplyAsync((), debug0); }debug5);
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
/* 46 */     this.total.start();
/* 47 */     this.allDone.thenAcceptAsync(this::finish, debug4);
/*    */   }
/*    */   
/*    */   private void finish(List<State> debug1) {
/* 51 */     this.total.stop();
/* 52 */     int debug2 = 0;
/* 53 */     LOGGER.info("Resource reload finished after " + this.total.elapsed(TimeUnit.MILLISECONDS) + " ms");
/* 54 */     for (State debug4 : debug1) {
/* 55 */       ProfileResults debug5 = debug4.preparationResult;
/* 56 */       ProfileResults debug6 = debug4.reloadResult;
/* 57 */       int debug7 = (int)(debug4.preparationNanos.get() / 1000000.0D);
/* 58 */       int debug8 = (int)(debug4.reloadNanos.get() / 1000000.0D);
/* 59 */       int debug9 = debug7 + debug8;
/* 60 */       String debug10 = debug4.name;
/* 61 */       LOGGER.info(debug10 + " took approximately " + debug9 + " ms (" + debug7 + " ms preparing, " + debug8 + " ms applying)");
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
/* 75 */       debug2 += debug8;
/*    */     } 
/*    */     
/* 78 */     LOGGER.info("Total blocking time: " + debug2 + " ms");
/*    */   }
/*    */   
/*    */   public static class State {
/*    */     private final String name;
/*    */     private final ProfileResults preparationResult;
/*    */     private final ProfileResults reloadResult;
/*    */     private final AtomicLong preparationNanos;
/*    */     private final AtomicLong reloadNanos;
/*    */     
/*    */     private State(String debug1, ProfileResults debug2, ProfileResults debug3, AtomicLong debug4, AtomicLong debug5) {
/* 89 */       this.name = debug1;
/* 90 */       this.preparationResult = debug2;
/* 91 */       this.reloadResult = debug3;
/* 92 */       this.preparationNanos = debug4;
/* 93 */       this.reloadNanos = debug5;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\ProfiledReloadInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */