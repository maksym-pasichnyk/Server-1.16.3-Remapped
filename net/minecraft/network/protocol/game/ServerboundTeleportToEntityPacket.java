/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundTeleportToEntityPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private UUID uuid;
/*    */   
/*    */   public ServerboundTeleportToEntityPacket() {}
/*    */   
/*    */   public ServerboundTeleportToEntityPacket(UUID debug1) {
/* 19 */     this.uuid = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.uuid = debug1.readUUID();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     debug1.writeUUID(this.uuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 34 */     debug1.handleTeleportToEntityPacket(this);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Entity getEntity(ServerLevel debug1) {
/* 39 */     return debug1.getEntity(this.uuid);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundTeleportToEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */