/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetExperiencePacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private float experienceProgress;
/*    */   private int totalExperience;
/*    */   private int experienceLevel;
/*    */   
/*    */   public ClientboundSetExperiencePacket() {}
/*    */   
/*    */   public ClientboundSetExperiencePacket(float debug1, int debug2, int debug3) {
/* 17 */     this.experienceProgress = debug1;
/* 18 */     this.totalExperience = debug2;
/* 19 */     this.experienceLevel = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.experienceProgress = debug1.readFloat();
/* 25 */     this.experienceLevel = debug1.readVarInt();
/* 26 */     this.totalExperience = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeFloat(this.experienceProgress);
/* 32 */     debug1.writeVarInt(this.experienceLevel);
/* 33 */     debug1.writeVarInt(this.totalExperience);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 38 */     debug1.handleSetExperience(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetExperiencePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */