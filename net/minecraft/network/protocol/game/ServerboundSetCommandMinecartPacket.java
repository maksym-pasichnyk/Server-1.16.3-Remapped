/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
/*    */ import net.minecraft.world.level.BaseCommandBlock;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundSetCommandMinecartPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int entity;
/*    */   private String command;
/*    */   private boolean trackOutput;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.entity = debug1.readVarInt();
/* 30 */     this.command = debug1.readUtf(32767);
/* 31 */     this.trackOutput = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeVarInt(this.entity);
/* 37 */     debug1.writeUtf(this.command);
/* 38 */     debug1.writeBoolean(this.trackOutput);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 43 */     debug1.handleSetCommandMinecart(this);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public BaseCommandBlock getCommandBlock(Level debug1) {
/* 48 */     Entity debug2 = debug1.getEntity(this.entity);
/* 49 */     if (debug2 instanceof MinecartCommandBlock) {
/* 50 */       return ((MinecartCommandBlock)debug2).getCommandBlock();
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommand() {
/* 57 */     return this.command;
/*    */   }
/*    */   
/*    */   public boolean isTrackOutput() {
/* 61 */     return this.trackOutput;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCommandMinecartPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */