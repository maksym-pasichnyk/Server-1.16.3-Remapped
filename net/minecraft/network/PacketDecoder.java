/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ 
/*    */ public class PacketDecoder
/*    */   extends ByteToMessageDecoder {
/* 17 */   private static final Logger LOGGER = LogManager.getLogger();
/* 18 */   private static final Marker MARKER = MarkerManager.getMarker("PACKET_RECEIVED", Connection.PACKET_MARKER);
/*    */   
/*    */   private final PacketFlow flow;
/*    */   
/*    */   public PacketDecoder(PacketFlow debug1) {
/* 23 */     this.flow = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext debug1, ByteBuf debug2, List<Object> debug3) throws Exception {
/* 28 */     if (debug2.readableBytes() == 0) {
/*    */       return;
/*    */     }
/*    */     
/* 32 */     FriendlyByteBuf debug4 = new FriendlyByteBuf(debug2);
/* 33 */     int debug5 = debug4.readVarInt();
/* 34 */     Packet<?> debug6 = ((ConnectionProtocol)debug1.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).createPacket(this.flow, debug5);
/*    */     
/* 36 */     if (debug6 == null) {
/* 37 */       throw new IOException("Bad packet id " + debug5);
/*    */     }
/*    */     
/* 40 */     debug6.read(debug4);
/* 41 */     if (debug4.readableBytes() > 0) {
/* 42 */       throw new IOException("Packet " + ((ConnectionProtocol)debug1.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).getId() + "/" + debug5 + " (" + debug6.getClass().getSimpleName() + ") was larger than I expected, found " + debug4.readableBytes() + " bytes extra whilst reading packet " + debug5);
/*    */     }
/* 44 */     debug3.add(debug6);
/*    */ 
/*    */     
/* 47 */     if (LOGGER.isDebugEnabled())
/* 48 */       LOGGER.debug(MARKER, " IN: [{}:{}] {}", debug1.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get(), Integer.valueOf(debug5), debug6.getClass().getName()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\PacketDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */