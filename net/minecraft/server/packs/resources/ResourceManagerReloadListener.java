/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public interface ResourceManagerReloadListener
/*    */   extends PreparableReloadListener
/*    */ {
/*    */   default CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier debug1, ResourceManager debug2, ProfilerFiller debug3, ProfilerFiller debug4, Executor debug5, Executor debug6) {
/* 12 */     return debug1.<Unit>wait(Unit.INSTANCE).thenRunAsync(() -> { debug1.startTick(); debug1.push("listener"); onResourceManagerReload(debug2); debug1.pop(); debug1.endTick(); }debug6);
/*    */   }
/*    */   
/*    */   void onResourceManagerReload(ResourceManager paramResourceManager);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\ResourceManagerReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */