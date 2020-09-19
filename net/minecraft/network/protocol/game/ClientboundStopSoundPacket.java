/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundStopSoundPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private ResourceLocation name;
/*    */   private SoundSource source;
/*    */   
/*    */   public ClientboundStopSoundPacket() {}
/*    */   
/*    */   public ClientboundStopSoundPacket(@Nullable ResourceLocation debug1, @Nullable SoundSource debug2) {
/* 22 */     this.name = debug1;
/* 23 */     this.source = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     int debug2 = debug1.readByte();
/* 29 */     if ((debug2 & 0x1) > 0) {
/* 30 */       this.source = (SoundSource)debug1.readEnum(SoundSource.class);
/*    */     }
/* 32 */     if ((debug2 & 0x2) > 0) {
/* 33 */       this.name = debug1.readResourceLocation();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 39 */     if (this.source != null) {
/* 40 */       if (this.name != null) {
/* 41 */         debug1.writeByte(3);
/* 42 */         debug1.writeEnum((Enum)this.source);
/* 43 */         debug1.writeResourceLocation(this.name);
/*    */       } else {
/* 45 */         debug1.writeByte(1);
/* 46 */         debug1.writeEnum((Enum)this.source);
/*    */       }
/*    */     
/* 49 */     } else if (this.name != null) {
/* 50 */       debug1.writeByte(2);
/* 51 */       debug1.writeResourceLocation(this.name);
/*    */     } else {
/* 53 */       debug1.writeByte(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 70 */     debug1.handleStopSoundEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundStopSoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */