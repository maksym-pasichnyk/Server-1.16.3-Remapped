/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.SerializableUUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundGameProfilePacket
/*    */   implements Packet<ClientLoginPacketListener>
/*    */ {
/*    */   private GameProfile gameProfile;
/*    */   
/*    */   public ClientboundGameProfilePacket() {}
/*    */   
/*    */   public ClientboundGameProfilePacket(GameProfile debug1) {
/* 19 */     this.gameProfile = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     int[] debug2 = new int[4];
/* 25 */     for (int i = 0; i < debug2.length; i++) {
/* 26 */       debug2[i] = debug1.readInt();
/*    */     }
/* 28 */     UUID debug3 = SerializableUUID.uuidFromIntArray(debug2);
/* 29 */     String debug4 = debug1.readUtf(16);
/* 30 */     this.gameProfile = new GameProfile(debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     for (int debug5 : SerializableUUID.uuidToIntArray(this.gameProfile.getId())) {
/* 36 */       debug1.writeInt(debug5);
/*    */     }
/* 38 */     debug1.writeUtf(this.gameProfile.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener debug1) {
/* 43 */     debug1.handleGameProfile(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundGameProfilePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */