/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ClientboundBlockBreakAckPacket
/*    */   implements Packet<ClientGamePacketListener> {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private BlockPos pos;
/*    */   
/*    */   private BlockState state;
/*    */   
/*    */   ServerboundPlayerActionPacket.Action action;
/*    */   
/*    */   private boolean allGood;
/*    */ 
/*    */   
/*    */   public ClientboundBlockBreakAckPacket() {}
/*    */ 
/*    */   
/*    */   public ClientboundBlockBreakAckPacket(BlockPos debug1, BlockState debug2, ServerboundPlayerActionPacket.Action debug3, boolean debug4, String debug5) {
/* 30 */     this.pos = debug1.immutable();
/* 31 */     this.state = debug2;
/* 32 */     this.action = debug3;
/* 33 */     this.allGood = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 38 */     this.pos = debug1.readBlockPos();
/* 39 */     this.state = (BlockState)Block.BLOCK_STATE_REGISTRY.byId(debug1.readVarInt());
/* 40 */     this.action = (ServerboundPlayerActionPacket.Action)debug1.readEnum(ServerboundPlayerActionPacket.Action.class);
/* 41 */     this.allGood = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 46 */     debug1.writeBlockPos(this.pos);
/* 47 */     debug1.writeVarInt(Block.getId(this.state));
/* 48 */     debug1.writeEnum(this.action);
/* 49 */     debug1.writeBoolean(this.allGood);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 54 */     debug1.handleBlockBreakAck(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockBreakAckPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */