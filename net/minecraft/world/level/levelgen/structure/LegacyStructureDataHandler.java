/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ 
/*     */ public class LegacyStructureDataHandler {
/*     */   private static final Map<String, String> CURRENT_TO_LEGACY_MAP;
/*     */   private static final Map<String, String> LEGACY_TO_CURRENT_MAP;
/*     */   private final boolean hasLegacyData;
/*     */   
/*     */   static {
/*  27 */     CURRENT_TO_LEGACY_MAP = (Map<String, String>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("Village", "Village");
/*     */           
/*     */           debug0.put("Mineshaft", "Mineshaft");
/*     */           
/*     */           debug0.put("Mansion", "Mansion");
/*     */           debug0.put("Igloo", "Temple");
/*     */           debug0.put("Desert_Pyramid", "Temple");
/*     */           debug0.put("Jungle_Pyramid", "Temple");
/*     */           debug0.put("Swamp_Hut", "Temple");
/*     */           debug0.put("Stronghold", "Stronghold");
/*     */           debug0.put("Monument", "Monument");
/*     */           debug0.put("Fortress", "Fortress");
/*     */           debug0.put("EndCity", "EndCity");
/*     */         });
/*  42 */     LEGACY_TO_CURRENT_MAP = (Map<String, String>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("Iglu", "Igloo");
/*     */           debug0.put("TeDP", "Desert_Pyramid");
/*     */           debug0.put("TeJP", "Jungle_Pyramid");
/*     */           debug0.put("TeSH", "Swamp_Hut");
/*     */         });
/*     */   } private final Map<String, Long2ObjectMap<CompoundTag>> dataMap; private final Map<String, StructureFeatureIndexSavedData> indexMap; private final List<String> legacyKeys; private final List<String> currentKeys;
/*     */   public LegacyStructureDataHandler(@Nullable DimensionDataStorage debug1, List<String> debug2, List<String> debug3) {
/*     */     int i;
/*  51 */     this.dataMap = Maps.newHashMap();
/*  52 */     this.indexMap = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     this.legacyKeys = debug2;
/*  58 */     this.currentKeys = debug3;
/*  59 */     populateCaches(debug1);
/*     */ 
/*     */     
/*  62 */     boolean debug4 = false;
/*  63 */     for (String debug6 : this.currentKeys) {
/*  64 */       i = debug4 | ((this.dataMap.get(debug6) != null) ? 1 : 0);
/*     */     }
/*  66 */     this.hasLegacyData = i;
/*     */   }
/*     */   
/*     */   public void removeIndex(long debug1) {
/*  70 */     for (String debug4 : this.legacyKeys) {
/*  71 */       StructureFeatureIndexSavedData debug5 = this.indexMap.get(debug4);
/*  72 */       if (debug5 != null && debug5.hasUnhandledIndex(debug1)) {
/*  73 */         debug5.removeIndex(debug1);
/*  74 */         debug5.setDirty();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompoundTag updateFromLegacy(CompoundTag debug1) {
/*  80 */     CompoundTag debug2 = debug1.getCompound("Level");
/*     */     
/*  82 */     ChunkPos debug3 = new ChunkPos(debug2.getInt("xPos"), debug2.getInt("zPos"));
/*     */     
/*  84 */     if (isUnhandledStructureStart(debug3.x, debug3.z)) {
/*  85 */       debug1 = updateStructureStart(debug1, debug3);
/*     */     }
/*     */     
/*  88 */     CompoundTag debug4 = debug2.getCompound("Structures");
/*  89 */     CompoundTag debug5 = debug4.getCompound("References");
/*     */     
/*  91 */     for (String debug7 : this.currentKeys) {
/*  92 */       StructureFeature<?> debug8 = (StructureFeature)StructureFeature.STRUCTURES_REGISTRY.get(debug7.toLowerCase(Locale.ROOT));
/*     */       
/*  94 */       if (debug5.contains(debug7, 12) || debug8 == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  98 */       int debug9 = 8;
/*  99 */       LongArrayList longArrayList = new LongArrayList();
/*     */       
/* 101 */       for (int debug11 = debug3.x - 8; debug11 <= debug3.x + 8; debug11++) {
/* 102 */         for (int debug12 = debug3.z - 8; debug12 <= debug3.z + 8; debug12++) {
/* 103 */           if (hasLegacyStart(debug11, debug12, debug7)) {
/* 104 */             longArrayList.add(ChunkPos.asLong(debug11, debug12));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 109 */       debug5.putLongArray(debug7, (List)longArrayList);
/*     */     } 
/*     */     
/* 112 */     debug4.put("References", (Tag)debug5);
/* 113 */     debug2.put("Structures", (Tag)debug4);
/* 114 */     debug1.put("Level", (Tag)debug2);
/*     */     
/* 116 */     return debug1;
/*     */   }
/*     */   
/*     */   private boolean hasLegacyStart(int debug1, int debug2, String debug3) {
/* 120 */     if (!this.hasLegacyData) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     if (this.dataMap.get(debug3) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(debug3))).hasStartIndex(ChunkPos.asLong(debug1, debug2))) {
/* 125 */       return true;
/*     */     }
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isUnhandledStructureStart(int debug1, int debug2) {
/* 132 */     if (!this.hasLegacyData) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     for (String debug4 : this.currentKeys) {
/* 137 */       if (this.dataMap.get(debug4) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(debug4))).hasUnhandledIndex(ChunkPos.asLong(debug1, debug2))) {
/* 138 */         return true;
/*     */       }
/*     */     } 
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   private CompoundTag updateStructureStart(CompoundTag debug1, ChunkPos debug2) {
/* 145 */     CompoundTag debug3 = debug1.getCompound("Level");
/* 146 */     CompoundTag debug4 = debug3.getCompound("Structures");
/* 147 */     CompoundTag debug5 = debug4.getCompound("Starts");
/*     */     
/* 149 */     for (String debug7 : this.currentKeys) {
/* 150 */       Long2ObjectMap<CompoundTag> debug8 = this.dataMap.get(debug7);
/* 151 */       if (debug8 == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 155 */       long debug9 = debug2.toLong();
/*     */       
/* 157 */       if (!((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(debug7))).hasUnhandledIndex(debug9)) {
/*     */         continue;
/*     */       }
/*     */       
/* 161 */       CompoundTag debug11 = (CompoundTag)debug8.get(debug9);
/* 162 */       if (debug11 == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 166 */       debug5.put(debug7, (Tag)debug11);
/*     */     } 
/*     */     
/* 169 */     debug4.put("Starts", (Tag)debug5);
/* 170 */     debug3.put("Structures", (Tag)debug4);
/* 171 */     debug1.put("Level", (Tag)debug3);
/*     */     
/* 173 */     return debug1;
/*     */   }
/*     */   
/*     */   private void populateCaches(@Nullable DimensionDataStorage debug1) {
/* 177 */     if (debug1 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     for (String debug3 : this.legacyKeys) {
/* 182 */       CompoundTag debug4 = new CompoundTag();
/*     */       try {
/* 184 */         debug4 = debug1.readTagFromDisk(debug3, 1493).getCompound("data").getCompound("Features");
/* 185 */         if (debug4.isEmpty()) {
/*     */           continue;
/*     */         }
/* 188 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 191 */       for (String str1 : debug4.getAllKeys()) {
/* 192 */         CompoundTag debug7 = debug4.getCompound(str1);
/* 193 */         long debug8 = ChunkPos.asLong(debug7.getInt("ChunkX"), debug7.getInt("ChunkZ"));
/*     */ 
/*     */         
/* 196 */         ListTag debug10 = debug7.getList("Children", 10);
/* 197 */         if (!debug10.isEmpty()) {
/* 198 */           String str2 = debug10.getCompound(0).getString("id");
/* 199 */           String debug12 = LEGACY_TO_CURRENT_MAP.get(str2);
/* 200 */           if (debug12 != null) {
/* 201 */             debug7.putString("id", debug12);
/*     */           }
/*     */         } 
/*     */         
/* 205 */         String debug11 = debug7.getString("id");
/*     */         
/* 207 */         ((Long2ObjectMap)this.dataMap.computeIfAbsent(debug11, debug0 -> new Long2ObjectOpenHashMap())).put(debug8, debug7);
/*     */       } 
/*     */       
/* 210 */       String debug5 = debug3 + "_index";
/* 211 */       StructureFeatureIndexSavedData debug6 = (StructureFeatureIndexSavedData)debug1.computeIfAbsent(() -> new StructureFeatureIndexSavedData(debug0), debug5);
/*     */       
/* 213 */       if (debug6.getAll().isEmpty()) {
/*     */         
/* 215 */         StructureFeatureIndexSavedData debug7 = new StructureFeatureIndexSavedData(debug5);
/* 216 */         this.indexMap.put(debug3, debug7);
/* 217 */         for (String debug9 : debug4.getAllKeys()) {
/* 218 */           CompoundTag debug10 = debug4.getCompound(debug9);
/* 219 */           debug7.addIndex(ChunkPos.asLong(debug10.getInt("ChunkX"), debug10.getInt("ChunkZ")));
/*     */         } 
/* 221 */         debug7.setDirty(); continue;
/*     */       } 
/* 223 */       this.indexMap.put(debug3, debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static LegacyStructureDataHandler getLegacyStructureHandler(ResourceKey<Level> debug0, @Nullable DimensionDataStorage debug1) {
/* 229 */     if (debug0 == Level.OVERWORLD) {
/* 230 */       return new LegacyStructureDataHandler(debug1, 
/*     */           
/* 232 */           (List<String>)ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 240 */           (List<String>)ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"));
/*     */     }
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
/* 252 */     if (debug0 == Level.NETHER) {
/* 253 */       ImmutableList immutableList = ImmutableList.of("Fortress");
/* 254 */       return new LegacyStructureDataHandler(debug1, (List<String>)immutableList, (List<String>)immutableList);
/* 255 */     }  if (debug0 == Level.END) {
/* 256 */       ImmutableList immutableList = ImmutableList.of("EndCity");
/* 257 */       return new LegacyStructureDataHandler(debug1, (List<String>)immutableList, (List<String>)immutableList);
/*     */     } 
/* 259 */     throw new RuntimeException(String.format("Unknown dimension type : %s", new Object[] { debug0 }));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\LegacyStructureDataHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */