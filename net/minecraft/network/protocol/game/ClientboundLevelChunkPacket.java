/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.LongArrayTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.chunk.ChunkBiomeContainer;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ public class ClientboundLevelChunkPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   private int x;
/*     */   private int z;
/*     */   private int availableSections;
/*     */   private CompoundTag heightmaps;
/*     */   @Nullable
/*     */   private int[] biomes;
/*     */   private byte[] buffer;
/*     */   private List<CompoundTag> blockEntitiesTags;
/*     */   private boolean fullChunk;
/*     */   
/*     */   public ClientboundLevelChunkPacket() {}
/*     */   
/*     */   public ClientboundLevelChunkPacket(LevelChunk debug1, int debug2) {
/*  41 */     ChunkPos debug3 = debug1.getPos();
/*  42 */     this.x = debug3.x;
/*  43 */     this.z = debug3.z;
/*  44 */     this.fullChunk = (debug2 == 65535);
/*     */     
/*  46 */     this.heightmaps = new CompoundTag();
/*  47 */     for (Map.Entry<Heightmap.Types, Heightmap> debug5 : (Iterable<Map.Entry<Heightmap.Types, Heightmap>>)debug1.getHeightmaps()) {
/*  48 */       if (!((Heightmap.Types)debug5.getKey()).sendToClient()) {
/*     */         continue;
/*     */       }
/*  51 */       this.heightmaps.put(((Heightmap.Types)debug5.getKey()).getSerializationKey(), (Tag)new LongArrayTag(((Heightmap)debug5.getValue()).getRawData()));
/*     */     } 
/*     */     
/*  54 */     if (this.fullChunk) {
/*  55 */       this.biomes = debug1.getBiomes().writeBiomes();
/*     */     }
/*     */     
/*  58 */     this.buffer = new byte[calculateChunkSize(debug1, debug2)];
/*  59 */     this.availableSections = extractChunkData(new FriendlyByteBuf(getWriteBuffer()), debug1, debug2);
/*     */     
/*  61 */     this.blockEntitiesTags = Lists.newArrayList();
/*  62 */     for (Map.Entry<BlockPos, BlockEntity> debug5 : (Iterable<Map.Entry<BlockPos, BlockEntity>>)debug1.getBlockEntities().entrySet()) {
/*  63 */       BlockPos debug6 = debug5.getKey();
/*  64 */       BlockEntity debug7 = debug5.getValue();
/*     */       
/*  66 */       int debug8 = debug6.getY() >> 4;
/*  67 */       if (!isFullChunk() && (debug2 & 1 << debug8) == 0) {
/*     */         continue;
/*     */       }
/*     */       
/*  71 */       CompoundTag debug9 = debug7.getUpdateTag();
/*  72 */       this.blockEntitiesTags.add(debug9);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  78 */     this.x = debug1.readInt();
/*  79 */     this.z = debug1.readInt();
/*  80 */     this.fullChunk = debug1.readBoolean();
/*  81 */     this.availableSections = debug1.readVarInt();
/*  82 */     this.heightmaps = debug1.readNbt();
/*     */     
/*  84 */     if (this.fullChunk) {
/*  85 */       this.biomes = debug1.readVarIntArray(ChunkBiomeContainer.BIOMES_SIZE);
/*     */     }
/*  87 */     int debug2 = debug1.readVarInt();
/*  88 */     if (debug2 > 2097152) {
/*  89 */       throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
/*     */     }
/*     */     
/*  92 */     this.buffer = new byte[debug2];
/*  93 */     debug1.readBytes(this.buffer);
/*     */     
/*  95 */     int debug3 = debug1.readVarInt();
/*  96 */     this.blockEntitiesTags = Lists.newArrayList();
/*  97 */     for (int debug4 = 0; debug4 < debug3; debug4++) {
/*  98 */       this.blockEntitiesTags.add(debug1.readNbt());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 104 */     debug1.writeInt(this.x);
/* 105 */     debug1.writeInt(this.z);
/* 106 */     debug1.writeBoolean(this.fullChunk);
/* 107 */     debug1.writeVarInt(this.availableSections);
/* 108 */     debug1.writeNbt(this.heightmaps);
/* 109 */     if (this.biomes != null) {
/* 110 */       debug1.writeVarIntArray(this.biomes);
/*     */     }
/* 112 */     debug1.writeVarInt(this.buffer.length);
/* 113 */     debug1.writeBytes(this.buffer);
/*     */     
/* 115 */     debug1.writeVarInt(this.blockEntitiesTags.size());
/* 116 */     for (CompoundTag debug3 : this.blockEntitiesTags) {
/* 117 */       debug1.writeNbt(debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 123 */     debug1.handleLevelChunk(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf getWriteBuffer() {
/* 131 */     ByteBuf debug1 = Unpooled.wrappedBuffer(this.buffer);
/* 132 */     debug1.writerIndex(0);
/* 133 */     return debug1;
/*     */   }
/*     */   
/*     */   public int extractChunkData(FriendlyByteBuf debug1, LevelChunk debug2, int debug3) {
/* 137 */     int debug4 = 0;
/*     */     
/* 139 */     LevelChunkSection[] debug5 = debug2.getSections();
/* 140 */     for (int debug6 = 0, debug7 = debug5.length; debug6 < debug7; debug6++) {
/* 141 */       LevelChunkSection debug8 = debug5[debug6];
/*     */       
/* 143 */       if (debug8 != LevelChunk.EMPTY_SECTION && (!isFullChunk() || !debug8.isEmpty()) && (debug3 & 1 << debug6) != 0) {
/*     */ 
/*     */ 
/*     */         
/* 147 */         debug4 |= 1 << debug6;
/*     */         
/* 149 */         debug8.write(debug1);
/*     */       } 
/*     */     } 
/* 152 */     return debug4;
/*     */   }
/*     */   
/*     */   protected int calculateChunkSize(LevelChunk debug1, int debug2) {
/* 156 */     int debug3 = 0;
/*     */     
/* 158 */     LevelChunkSection[] debug4 = debug1.getSections();
/* 159 */     for (int debug5 = 0, debug6 = debug4.length; debug5 < debug6; debug5++) {
/* 160 */       LevelChunkSection debug7 = debug4[debug5];
/*     */       
/* 162 */       if (debug7 != LevelChunk.EMPTY_SECTION && (!isFullChunk() || !debug7.isEmpty()) && (debug2 & 1 << debug5) != 0)
/*     */       {
/*     */ 
/*     */         
/* 166 */         debug3 += debug7.getSerializedSize();
/*     */       }
/*     */     } 
/* 169 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullChunk() {
/* 185 */     return this.fullChunk;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */