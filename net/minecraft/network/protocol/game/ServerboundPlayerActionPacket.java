/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
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
/*    */ public class ServerboundPlayerActionPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private Direction direction;
/*    */   private Action action;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.action = (Action)debug1.readEnum(Action.class);
/* 27 */     this.pos = debug1.readBlockPos();
/* 28 */     this.direction = Direction.from3DDataValue(debug1.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeEnum(this.action);
/* 34 */     debug1.writeBlockPos(this.pos);
/* 35 */     debug1.writeByte(this.direction.get3DDataValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 40 */     debug1.handlePlayerAction(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 44 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 48 */     return this.direction;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 52 */     return this.action;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 56 */     START_DESTROY_BLOCK,
/* 57 */     ABORT_DESTROY_BLOCK,
/* 58 */     STOP_DESTROY_BLOCK,
/* 59 */     DROP_ALL_ITEMS,
/* 60 */     DROP_ITEM,
/* 61 */     RELEASE_USE_ITEM,
/* 62 */     SWAP_ITEM_WITH_OFFHAND;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerActionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */