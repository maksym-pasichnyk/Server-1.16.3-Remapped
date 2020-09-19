/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkBiomeContainer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.OldDataLayer;
/*     */ 
/*     */ public class OldChunkStorage
/*     */ {
/*     */   public static OldLevelChunk load(CompoundTag debug0) {
/*  18 */     int debug1 = debug0.getInt("xPos");
/*  19 */     int debug2 = debug0.getInt("zPos");
/*     */     
/*  21 */     OldLevelChunk debug3 = new OldLevelChunk(debug1, debug2);
/*  22 */     debug3.blocks = debug0.getByteArray("Blocks");
/*  23 */     debug3.data = new OldDataLayer(debug0.getByteArray("Data"), 7);
/*  24 */     debug3.skyLight = new OldDataLayer(debug0.getByteArray("SkyLight"), 7);
/*  25 */     debug3.blockLight = new OldDataLayer(debug0.getByteArray("BlockLight"), 7);
/*  26 */     debug3.heightmap = debug0.getByteArray("HeightMap");
/*  27 */     debug3.terrainPopulated = debug0.getBoolean("TerrainPopulated");
/*  28 */     debug3.entities = debug0.getList("Entities", 10);
/*  29 */     debug3.blockEntities = debug0.getList("TileEntities", 10);
/*  30 */     debug3.blockTicks = debug0.getList("TileTicks", 10);
/*     */ 
/*     */     
/*     */     try {
/*  34 */       debug3.lastUpdated = debug0.getLong("LastUpdate");
/*  35 */     } catch (ClassCastException debug4) {
/*  36 */       debug3.lastUpdated = debug0.getInt("LastUpdate");
/*     */     } 
/*     */     
/*  39 */     return debug3;
/*     */   }
/*     */   
/*     */   public static void convertToAnvilFormat(RegistryAccess.RegistryHolder debug0, OldLevelChunk debug1, CompoundTag debug2, BiomeSource debug3) {
/*  43 */     debug2.putInt("xPos", debug1.x);
/*  44 */     debug2.putInt("zPos", debug1.z);
/*  45 */     debug2.putLong("LastUpdate", debug1.lastUpdated);
/*  46 */     int[] debug4 = new int[debug1.heightmap.length];
/*  47 */     for (int i = 0; i < debug1.heightmap.length; i++) {
/*  48 */       debug4[i] = debug1.heightmap[i];
/*     */     }
/*  50 */     debug2.putIntArray("HeightMap", debug4);
/*  51 */     debug2.putBoolean("TerrainPopulated", debug1.terrainPopulated);
/*     */     
/*  53 */     ListTag debug5 = new ListTag();
/*  54 */     for (int debug6 = 0; debug6 < 8; debug6++) {
/*     */       
/*  56 */       boolean debug7 = true;
/*  57 */       for (int debug8 = 0; debug8 < 16 && debug7; debug8++) {
/*  58 */         for (int debug9 = 0; debug9 < 16 && debug7; debug9++) {
/*  59 */           for (int debug10 = 0; debug10 < 16; debug10++) {
/*  60 */             int debug11 = debug8 << 11 | debug10 << 7 | debug9 + (debug6 << 4);
/*  61 */             int debug12 = debug1.blocks[debug11];
/*  62 */             if (debug12 != 0) {
/*  63 */               debug7 = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*  70 */       if (!debug7) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  75 */         byte[] arrayOfByte = new byte[4096];
/*  76 */         DataLayer debug9 = new DataLayer();
/*  77 */         DataLayer debug10 = new DataLayer();
/*  78 */         DataLayer debug11 = new DataLayer();
/*     */         
/*  80 */         for (int j = 0; j < 16; j++) {
/*  81 */           for (int debug13 = 0; debug13 < 16; debug13++) {
/*  82 */             for (int debug14 = 0; debug14 < 16; debug14++) {
/*  83 */               int debug15 = j << 11 | debug14 << 7 | debug13 + (debug6 << 4);
/*  84 */               int debug16 = debug1.blocks[debug15];
/*     */               
/*  86 */               arrayOfByte[debug13 << 8 | debug14 << 4 | j] = (byte)(debug16 & 0xFF);
/*  87 */               debug9.set(j, debug13, debug14, debug1.data.get(j, debug13 + (debug6 << 4), debug14));
/*  88 */               debug10.set(j, debug13, debug14, debug1.skyLight.get(j, debug13 + (debug6 << 4), debug14));
/*  89 */               debug11.set(j, debug13, debug14, debug1.blockLight.get(j, debug13 + (debug6 << 4), debug14));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  94 */         CompoundTag debug12 = new CompoundTag();
/*     */         
/*  96 */         debug12.putByte("Y", (byte)(debug6 & 0xFF));
/*  97 */         debug12.putByteArray("Blocks", arrayOfByte);
/*  98 */         debug12.putByteArray("Data", debug9.getData());
/*  99 */         debug12.putByteArray("SkyLight", debug10.getData());
/* 100 */         debug12.putByteArray("BlockLight", debug11.getData());
/*     */         
/* 102 */         debug5.add(debug12);
/*     */       } 
/* 104 */     }  debug2.put("Sections", (Tag)debug5);
/* 105 */     debug2.putIntArray("Biomes", (new ChunkBiomeContainer((IdMap)debug0.registryOrThrow(Registry.BIOME_REGISTRY), new ChunkPos(debug1.x, debug1.z), debug3)).writeBiomes());
/* 106 */     debug2.put("Entities", (Tag)debug1.entities);
/* 107 */     debug2.put("TileEntities", (Tag)debug1.blockEntities);
/* 108 */     if (debug1.blockTicks != null) {
/* 109 */       debug2.put("TileTicks", (Tag)debug1.blockTicks);
/*     */     }
/* 111 */     debug2.putBoolean("convertedFromAlphaFormat", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class OldLevelChunk
/*     */   {
/*     */     public long lastUpdated;
/*     */     public boolean terrainPopulated;
/*     */     public byte[] heightmap;
/*     */     public OldDataLayer blockLight;
/*     */     public OldDataLayer skyLight;
/*     */     public OldDataLayer data;
/*     */     public byte[] blocks;
/*     */     public ListTag entities;
/*     */     public ListTag blockEntities;
/*     */     public ListTag blockTicks;
/*     */     public final int x;
/*     */     public final int z;
/*     */     
/*     */     public OldLevelChunk(int debug1, int debug2) {
/* 131 */       this.x = debug1;
/* 132 */       this.z = debug2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\OldChunkStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */