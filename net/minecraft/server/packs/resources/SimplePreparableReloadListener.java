/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public abstract class SimplePreparableReloadListener<T>
/*    */   implements PreparableReloadListener
/*    */ {
/*    */   public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier debug1, ResourceManager debug2, ProfilerFiller debug3, ProfilerFiller debug4, Executor debug5, Executor debug6) {
/* 11 */     return CompletableFuture.supplyAsync(() -> prepare(debug1, debug2), debug5)
/* 12 */       .thenCompose(debug1::wait)
/* 13 */       .thenAcceptAsync(debug3 -> apply((T)debug3, debug1, debug2), debug6);
/*    */   }
/*    */   
/*    */   protected abstract T prepare(ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */   
/*    */   protected abstract void apply(T paramT, ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\SimplePreparableReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */