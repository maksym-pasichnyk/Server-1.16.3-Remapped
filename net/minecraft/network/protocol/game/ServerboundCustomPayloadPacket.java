/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class ServerboundCustomPayloadPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/* 13 */   public static final ResourceLocation BRAND = new ResourceLocation("brand");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ResourceLocation identifier;
/*    */ 
/*    */ 
/*    */   
/*    */   private FriendlyByteBuf data;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     this.identifier = debug1.readResourceLocation();
/* 29 */     int debug2 = debug1.readableBytes();
/* 30 */     if (debug2 < 0 || debug2 > 32767) {
/* 31 */       throw new IOException("Payload may not be larger than 32767 bytes");
/*    */     }
/* 33 */     this.data = new FriendlyByteBuf(debug1.readBytes(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeResourceLocation(this.identifier);
/* 39 */     debug1.writeBytes((ByteBuf)this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 44 */     debug1.handleCustomPayload(this);
/* 45 */     if (this.data != null)
/* 46 */       this.data.release(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundCustomPayloadPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */