/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.server.RunningOnDifferentThreadException;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.thread.BlockableEventLoop;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class PacketUtils {
/* 11 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> debug0, T debug1, ServerLevel debug2) throws RunningOnDifferentThreadException {
/* 14 */     ensureRunningOnSameThread(debug0, debug1, (BlockableEventLoop<?>)debug2.getServer());
/*    */   }
/*    */   
/*    */   public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> debug0, T debug1, BlockableEventLoop<?> debug2) throws RunningOnDifferentThreadException {
/* 18 */     if (!debug2.isSameThread()) {
/* 19 */       debug2.execute(() -> {
/*    */             if (debug0.getConnection().isConnected()) {
/*    */               debug1.handle(debug0);
/*    */             } else {
/*    */               LOGGER.debug("Ignoring packet due to disconnection: " + debug1);
/*    */             } 
/*    */           });
/* 26 */       throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\PacketUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */