/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ 
/*    */ public class PacketEncoder
/*    */   extends MessageToByteEncoder<Packet<?>> {
/* 16 */   private static final Logger LOGGER = LogManager.getLogger();
/* 17 */   private static final Marker MARKER = MarkerManager.getMarker("PACKET_SENT", Connection.PACKET_MARKER);
/*    */   
/*    */   private final PacketFlow flow;
/*    */   
/*    */   public PacketEncoder(PacketFlow debug1) {
/* 22 */     this.flow = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext debug1, Packet<?> debug2, ByteBuf debug3) throws Exception {
/* 27 */     ConnectionProtocol debug4 = (ConnectionProtocol)debug1.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get();
/* 28 */     if (debug4 == null) {
/* 29 */       throw new RuntimeException("ConnectionProtocol unknown: " + debug2);
/*    */     }
/* 31 */     Integer debug5 = debug4.getPacketId(this.flow, debug2);
/*    */ 
/*    */     
/* 34 */     if (LOGGER.isDebugEnabled()) {
/* 35 */       LOGGER.debug(MARKER, "OUT: [{}:{}] {}", debug1.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get(), debug5, debug2.getClass().getName());
/*    */     }
/*    */     
/* 38 */     if (debug5 == null) {
/* 39 */       throw new IOException("Can't serialize unregistered packet");
/*    */     }
/*    */     
/* 42 */     FriendlyByteBuf debug6 = new FriendlyByteBuf(debug3);
/* 43 */     debug6.writeVarInt(debug5.intValue());
/*    */     
/*    */     try {
/* 46 */       debug2.write(debug6);
/* 47 */     } catch (Throwable debug7) {
/* 48 */       LOGGER.error(debug7);
/* 49 */       if (debug2.isSkippable()) {
/* 50 */         throw new SkipPacketException(debug7);
/*    */       }
/* 52 */       throw debug7;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\PacketEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */