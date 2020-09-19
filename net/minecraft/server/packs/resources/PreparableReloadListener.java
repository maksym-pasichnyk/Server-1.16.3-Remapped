/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PreparableReloadListener
/*    */ {
/*    */   CompletableFuture<Void> reload(PreparationBarrier paramPreparationBarrier, ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller1, ProfilerFiller paramProfilerFiller2, Executor paramExecutor1, Executor paramExecutor2);
/*    */   
/*    */   default String getName() {
/* 16 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   public static interface PreparationBarrier {
/*    */     <T> CompletableFuture<T> wait(T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\PreparableReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */