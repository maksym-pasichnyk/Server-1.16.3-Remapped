/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundJigsawGeneratePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private int levels;
/*    */   private boolean keepJigsaws;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.pos = debug1.readBlockPos();
/* 26 */     this.levels = debug1.readVarInt();
/* 27 */     this.keepJigsaws = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeBlockPos(this.pos);
/* 33 */     debug1.writeVarInt(this.levels);
/* 34 */     debug1.writeBoolean(this.keepJigsaws);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 39 */     debug1.handleJigsawGenerate(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 43 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int levels() {
/* 47 */     return this.levels;
/*    */   }
/*    */   
/*    */   public boolean keepJigsaws() {
/* 51 */     return this.keepJigsaws;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundJigsawGeneratePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */