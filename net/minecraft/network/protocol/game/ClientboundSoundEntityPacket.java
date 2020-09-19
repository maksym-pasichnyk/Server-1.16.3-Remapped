/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class ClientboundSoundEntityPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private SoundEvent sound;
/*    */   private SoundSource source;
/*    */   private int id;
/*    */   private float volume;
/*    */   private float pitch;
/*    */   
/*    */   public ClientboundSoundEntityPacket() {}
/*    */   
/*    */   public ClientboundSoundEntityPacket(SoundEvent debug1, SoundSource debug2, Entity debug3, float debug4, float debug5) {
/* 24 */     Validate.notNull(debug1, "sound", new Object[0]);
/* 25 */     this.sound = debug1;
/* 26 */     this.source = debug2;
/* 27 */     this.id = debug3.getId();
/* 28 */     this.volume = debug4;
/* 29 */     this.pitch = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 34 */     this.sound = (SoundEvent)Registry.SOUND_EVENT.byId(debug1.readVarInt());
/* 35 */     this.source = (SoundSource)debug1.readEnum(SoundSource.class);
/* 36 */     this.id = debug1.readVarInt();
/* 37 */     this.volume = debug1.readFloat();
/* 38 */     this.pitch = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 43 */     debug1.writeVarInt(Registry.SOUND_EVENT.getId(this.sound));
/* 44 */     debug1.writeEnum((Enum)this.source);
/* 45 */     debug1.writeVarInt(this.id);
/* 46 */     debug1.writeFloat(this.volume);
/* 47 */     debug1.writeFloat(this.pitch);
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
/* 72 */     debug1.handleSoundEntityEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSoundEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */