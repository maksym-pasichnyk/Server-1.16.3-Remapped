/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.InactiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleReloadInstance<S>
/*    */   implements ReloadInstance
/*    */ {
/*    */   protected final ResourceManager resourceManager;
/* 21 */   protected final CompletableFuture<Unit> allPreparations = new CompletableFuture<>();
/*    */   
/*    */   protected final CompletableFuture<List<S>> allDone;
/*    */   
/*    */   private final Set<PreparableReloadListener> preparingListeners;
/*    */   private final int listenerCount;
/*    */   private int startedReloads;
/*    */   private int finishedReloads;
/* 29 */   private final AtomicInteger startedTaskCounter = new AtomicInteger();
/* 30 */   private final AtomicInteger doneTaskCounter = new AtomicInteger();
/*    */   
/*    */   public static SimpleReloadInstance<Void> of(ResourceManager debug0, List<PreparableReloadListener> debug1, Executor debug2, Executor debug3, CompletableFuture<Unit> debug4) {
/* 33 */     return new SimpleReloadInstance<>(debug2, debug3, debug0, debug1, (debug1, debug2, debug3, debug4, debug5) -> debug3.reload(debug1, debug2, (ProfilerFiller)InactiveProfiler.INSTANCE, (ProfilerFiller)InactiveProfiler.INSTANCE, debug0, debug5), debug4);
/*    */   }
/*    */   
/*    */   protected SimpleReloadInstance(Executor debug1, final Executor mainThreadExecutor, ResourceManager debug3, List<PreparableReloadListener> debug4, StateFactory<S> debug5, CompletableFuture<Unit> debug6) {
/* 37 */     this.resourceManager = debug3;
/* 38 */     this.listenerCount = debug4.size();
/* 39 */     this.startedTaskCounter.incrementAndGet();
/* 40 */     debug6.thenRun(this.doneTaskCounter::incrementAndGet);
/* 41 */     List<CompletableFuture<S>> debug7 = Lists.newArrayList();
/* 42 */     CompletableFuture<?> debug8 = debug6;
/* 43 */     this.preparingListeners = Sets.newHashSet(debug4);
/* 44 */     for (PreparableReloadListener debug10 : debug4) {
/* 45 */       final CompletableFuture<?> previousTask = debug8;
/* 46 */       CompletableFuture<S> debug12 = debug5.create(new PreparableReloadListener.PreparationBarrier()
/*    */           {
/*    */             public <T> CompletableFuture<T> wait(T debug1)
/*    */             {
/* 50 */               mainThreadExecutor.execute(() -> {
/*    */                     SimpleReloadInstance.this.preparingListeners.remove(debug1);
/*    */                     if (SimpleReloadInstance.this.preparingListeners.isEmpty()) {
/*    */                       SimpleReloadInstance.this.allPreparations.complete(Unit.INSTANCE);
/*    */                     }
/*    */                   });
/* 56 */               return SimpleReloadInstance.this.allPreparations.thenCombine(previousTask, (debug1, debug2) -> debug0);
/*    */             }
/*    */           }debug3, debug10, debug2 -> {
/*    */             this.startedTaskCounter.incrementAndGet();
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             debug1.execute(());
/*    */           }debug2 -> {
/*    */             this.startedReloads++;
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             debug1.execute(());
/*    */           });
/*    */ 
/*    */ 
/*    */       
/* 76 */       debug7.add(debug12);
/* 77 */       debug8 = debug12;
/*    */     } 
/* 79 */     this.allDone = Util.sequence(debug7);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Unit> done() {
/* 84 */     return this.allDone.thenApply(debug0 -> Unit.INSTANCE);
/*    */   }
/*    */   
/*    */   public static interface StateFactory<S> {
/*    */     CompletableFuture<S> create(PreparableReloadListener.PreparationBarrier param1PreparationBarrier, ResourceManager param1ResourceManager, PreparableReloadListener param1PreparableReloadListener, Executor param1Executor1, Executor param1Executor2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\SimpleReloadInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */