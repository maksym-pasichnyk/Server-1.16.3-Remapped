/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*    */ import java.io.IOException;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*    */ 
/*    */ public class ClientboundSectionBlocksUpdatePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private SectionPos sectionPos;
/*    */   private short[] positions;
/*    */   private BlockState[] states;
/*    */   private boolean suppressLightUpdates;
/*    */   
/*    */   public ClientboundSectionBlocksUpdatePacket() {}
/*    */   
/*    */   public ClientboundSectionBlocksUpdatePacket(SectionPos debug1, ShortSet debug2, LevelChunkSection debug3, boolean debug4) {
/* 27 */     this.sectionPos = debug1;
/* 28 */     this.suppressLightUpdates = debug4;
/* 29 */     initFields(debug2.size());
/*    */     
/* 31 */     int debug5 = 0;
/* 32 */     for (ShortIterator<Short> shortIterator = debug2.iterator(); shortIterator.hasNext(); ) { short debug7 = ((Short)shortIterator.next()).shortValue();
/* 33 */       this.positions[debug5] = debug7;
/* 34 */       this.states[debug5] = debug3.getBlockState(SectionPos.sectionRelativeX(debug7), SectionPos.sectionRelativeY(debug7), SectionPos.sectionRelativeZ(debug7));
/* 35 */       debug5++; }
/*    */   
/*    */   }
/*    */   
/*    */   private void initFields(int debug1) {
/* 40 */     this.positions = new short[debug1];
/* 41 */     this.states = new BlockState[debug1];
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 46 */     this.sectionPos = SectionPos.of(debug1.readLong());
/* 47 */     this.suppressLightUpdates = debug1.readBoolean();
/* 48 */     int debug2 = debug1.readVarInt();
/* 49 */     initFields(debug2);
/*    */     
/* 51 */     for (int debug3 = 0; debug3 < this.positions.length; debug3++) {
/* 52 */       long debug4 = debug1.readVarLong();
/* 53 */       this.positions[debug3] = (short)(int)(debug4 & 0xFFFL);
/* 54 */       this.states[debug3] = (BlockState)Block.BLOCK_STATE_REGISTRY.byId((int)(debug4 >>> 12L));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 60 */     debug1.writeLong(this.sectionPos.asLong());
/* 61 */     debug1.writeBoolean(this.suppressLightUpdates);
/* 62 */     debug1.writeVarInt(this.positions.length);
/*    */     
/* 64 */     for (int debug2 = 0; debug2 < this.positions.length; debug2++) {
/* 65 */       debug1.writeVarLong((Block.getId(this.states[debug2]) << 12 | this.positions[debug2]));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 71 */     debug1.handleChunkBlocksUpdate(this);
/*    */   }
/*    */   
/*    */   public void runUpdates(BiConsumer<BlockPos, BlockState> debug1) {
/* 75 */     BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/* 76 */     for (int debug3 = 0; debug3 < this.positions.length; debug3++) {
/* 77 */       short debug4 = this.positions[debug3];
/* 78 */       debug2.set(this.sectionPos.relativeToBlockX(debug4), this.sectionPos.relativeToBlockY(debug4), this.sectionPos.relativeToBlockZ(debug4));
/* 79 */       debug1.accept(debug2, this.states[debug3]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSectionBlocksUpdatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */