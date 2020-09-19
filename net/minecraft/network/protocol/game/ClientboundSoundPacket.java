/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ 
/*    */ public class ClientboundSoundPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private SoundEvent sound;
/*    */   private SoundSource source;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   private float volume;
/*    */   private float pitch;
/*    */   
/*    */   public ClientboundSoundPacket() {}
/*    */   
/*    */   public ClientboundSoundPacket(SoundEvent debug1, SoundSource debug2, double debug3, double debug5, double debug7, float debug9, float debug10) {
/* 27 */     Validate.notNull(debug1, "sound", new Object[0]);
/* 28 */     this.sound = debug1;
/* 29 */     this.source = debug2;
/* 30 */     this.x = (int)(debug3 * 8.0D);
/* 31 */     this.y = (int)(debug5 * 8.0D);
/* 32 */     this.z = (int)(debug7 * 8.0D);
/* 33 */     this.volume = debug9;
/* 34 */     this.pitch = debug10;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 39 */     this.sound = (SoundEvent)Registry.SOUND_EVENT.byId(debug1.readVarInt());
/* 40 */     this.source = (SoundSource)debug1.readEnum(SoundSource.class);
/* 41 */     this.x = debug1.readInt();
/* 42 */     this.y = debug1.readInt();
/* 43 */     this.z = debug1.readInt();
/* 44 */     this.volume = debug1.readFloat();
/* 45 */     this.pitch = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 50 */     debug1.writeVarInt(Registry.SOUND_EVENT.getId(this.sound));
/* 51 */     debug1.writeEnum((Enum)this.source);
/* 52 */     debug1.writeInt(this.x);
/* 53 */     debug1.writeInt(this.y);
/* 54 */     debug1.writeInt(this.z);
/* 55 */     debug1.writeFloat(this.volume);
/* 56 */     debug1.writeFloat(this.pitch);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 89 */     debug1.handleSoundEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */