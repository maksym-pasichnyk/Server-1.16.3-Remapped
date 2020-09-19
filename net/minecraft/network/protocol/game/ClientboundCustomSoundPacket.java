/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundCustomSoundPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private ResourceLocation name;
/*    */   private SoundSource source;
/*    */   private int x;
/* 17 */   private int y = Integer.MAX_VALUE;
/*    */   
/*    */   private int z;
/*    */   
/*    */   private float volume;
/*    */   
/*    */   private float pitch;
/*    */   
/*    */   public ClientboundCustomSoundPacket(ResourceLocation debug1, SoundSource debug2, Vec3 debug3, float debug4, float debug5) {
/* 26 */     this.name = debug1;
/* 27 */     this.source = debug2;
/* 28 */     this.x = (int)(debug3.x * 8.0D);
/* 29 */     this.y = (int)(debug3.y * 8.0D);
/* 30 */     this.z = (int)(debug3.z * 8.0D);
/* 31 */     this.volume = debug4;
/* 32 */     this.pitch = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 37 */     this.name = debug1.readResourceLocation();
/* 38 */     this.source = (SoundSource)debug1.readEnum(SoundSource.class);
/* 39 */     this.x = debug1.readInt();
/* 40 */     this.y = debug1.readInt();
/* 41 */     this.z = debug1.readInt();
/* 42 */     this.volume = debug1.readFloat();
/* 43 */     this.pitch = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 48 */     debug1.writeResourceLocation(this.name);
/* 49 */     debug1.writeEnum((Enum)this.source);
/* 50 */     debug1.writeInt(this.x);
/* 51 */     debug1.writeInt(this.y);
/* 52 */     debug1.writeInt(this.z);
/* 53 */     debug1.writeFloat(this.volume);
/* 54 */     debug1.writeFloat(this.pitch);
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
/* 87 */     debug1.handleCustomSoundEvent(this);
/*    */   }
/*    */   
/*    */   public ClientboundCustomSoundPacket() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCustomSoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */