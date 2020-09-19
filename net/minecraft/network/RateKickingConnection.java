/*    */ package net.minecraft.network;
/*    */ import io.netty.util.concurrent.Future;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class RateKickingConnection extends Connection {
/* 11 */   private static final Logger LOGGER = LogManager.getLogger();
/* 12 */   private static final Component EXCEED_REASON = (Component)new TranslatableComponent("disconnect.exceeded_packet_rate");
/*    */   
/*    */   private final int rateLimitPacketsPerSecond;
/*    */   
/*    */   public RateKickingConnection(int debug1) {
/* 17 */     super(PacketFlow.SERVERBOUND);
/* 18 */     this.rateLimitPacketsPerSecond = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tickSecond() {
/* 23 */     super.tickSecond();
/*    */     
/* 25 */     float debug1 = getAverageReceivedPackets();
/* 26 */     if (debug1 > this.rateLimitPacketsPerSecond) {
/* 27 */       LOGGER.warn("Player exceeded rate-limit (sent {} packets per second)", Float.valueOf(debug1));
/*    */       
/* 29 */       send((Packet<?>)new ClientboundDisconnectPacket(EXCEED_REASON), debug1 -> disconnect(EXCEED_REASON));
/* 30 */       setReadOnly();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\RateKickingConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */