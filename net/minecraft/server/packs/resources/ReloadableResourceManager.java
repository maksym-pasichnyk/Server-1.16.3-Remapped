/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public interface ReloadableResourceManager
/*    */   extends ResourceManager, AutoCloseable {
/*    */   default CompletableFuture<Unit> reload(Executor debug1, Executor debug2, List<PackResources> debug3, CompletableFuture<Unit> debug4) {
/* 12 */     return createFullReload(debug1, debug2, debug4, debug3).done();
/*    */   }
/*    */   
/*    */   ReloadInstance createFullReload(Executor paramExecutor1, Executor paramExecutor2, CompletableFuture<Unit> paramCompletableFuture, List<PackResources> paramList);
/*    */   
/*    */   void registerReloadListener(PreparableReloadListener paramPreparableReloadListener);
/*    */   
/*    */   void close();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\ReloadableResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */