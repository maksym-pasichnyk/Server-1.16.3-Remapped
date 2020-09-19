/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ClientboundBlockUpdatePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private BlockState blockState;
/*    */   
/*    */   public ClientboundBlockUpdatePacket() {}
/*    */   
/*    */   public ClientboundBlockUpdatePacket(BlockPos debug1, BlockState debug2) {
/* 21 */     this.pos = debug1;
/* 22 */     this.blockState = debug2;
/*    */   }
/*    */   
/*    */   public ClientboundBlockUpdatePacket(BlockGetter debug1, BlockPos debug2) {
/* 26 */     this(debug2, debug1.getBlockState(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 31 */     this.pos = debug1.readBlockPos();
/* 32 */     this.blockState = (BlockState)Block.BLOCK_STATE_REGISTRY.byId(debug1.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 37 */     debug1.writeBlockPos(this.pos);
/* 38 */     debug1.writeVarInt(Block.getId(this.blockState));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 43 */     debug1.handleBlockUpdate(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockUpdatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */