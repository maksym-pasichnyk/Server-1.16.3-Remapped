/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.monster.Drowned;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class OceanRuinPieces {
/*  43 */   private static final ResourceLocation[] WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/warm_1"), new ResourceLocation("underwater_ruin/warm_2"), new ResourceLocation("underwater_ruin/warm_3"), new ResourceLocation("underwater_ruin/warm_4"), new ResourceLocation("underwater_ruin/warm_5"), new ResourceLocation("underwater_ruin/warm_6"), new ResourceLocation("underwater_ruin/warm_7"), new ResourceLocation("underwater_ruin/warm_8") };
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
/*  54 */   private static final ResourceLocation[] RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/brick_1"), new ResourceLocation("underwater_ruin/brick_2"), new ResourceLocation("underwater_ruin/brick_3"), new ResourceLocation("underwater_ruin/brick_4"), new ResourceLocation("underwater_ruin/brick_5"), new ResourceLocation("underwater_ruin/brick_6"), new ResourceLocation("underwater_ruin/brick_7"), new ResourceLocation("underwater_ruin/brick_8") };
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
/*  65 */   private static final ResourceLocation[] RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/cracked_1"), new ResourceLocation("underwater_ruin/cracked_2"), new ResourceLocation("underwater_ruin/cracked_3"), new ResourceLocation("underwater_ruin/cracked_4"), new ResourceLocation("underwater_ruin/cracked_5"), new ResourceLocation("underwater_ruin/cracked_6"), new ResourceLocation("underwater_ruin/cracked_7"), new ResourceLocation("underwater_ruin/cracked_8") };
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
/*  76 */   private static final ResourceLocation[] RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/mossy_1"), new ResourceLocation("underwater_ruin/mossy_2"), new ResourceLocation("underwater_ruin/mossy_3"), new ResourceLocation("underwater_ruin/mossy_4"), new ResourceLocation("underwater_ruin/mossy_5"), new ResourceLocation("underwater_ruin/mossy_6"), new ResourceLocation("underwater_ruin/mossy_7"), new ResourceLocation("underwater_ruin/mossy_8") };
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
/*  87 */   private static final ResourceLocation[] BIG_RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_brick_1"), new ResourceLocation("underwater_ruin/big_brick_2"), new ResourceLocation("underwater_ruin/big_brick_3"), new ResourceLocation("underwater_ruin/big_brick_8") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final ResourceLocation[] BIG_RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_mossy_1"), new ResourceLocation("underwater_ruin/big_mossy_2"), new ResourceLocation("underwater_ruin/big_mossy_3"), new ResourceLocation("underwater_ruin/big_mossy_8") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private static final ResourceLocation[] BIG_RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_cracked_1"), new ResourceLocation("underwater_ruin/big_cracked_2"), new ResourceLocation("underwater_ruin/big_cracked_3"), new ResourceLocation("underwater_ruin/big_cracked_8") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final ResourceLocation[] BIG_WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_warm_4"), new ResourceLocation("underwater_ruin/big_warm_5"), new ResourceLocation("underwater_ruin/big_warm_6"), new ResourceLocation("underwater_ruin/big_warm_7") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ResourceLocation getSmallWarmRuin(Random debug0) {
/* 116 */     return (ResourceLocation)Util.getRandom((Object[])WARM_RUINS, debug0);
/*     */   }
/*     */   
/*     */   private static ResourceLocation getBigWarmRuin(Random debug0) {
/* 120 */     return (ResourceLocation)Util.getRandom((Object[])BIG_WARM_RUINS, debug0);
/*     */   }
/*     */   
/*     */   public static void addPieces(StructureManager debug0, BlockPos debug1, Rotation debug2, List<StructurePiece> debug3, Random debug4, OceanRuinConfiguration debug5) {
/* 124 */     boolean debug6 = (debug4.nextFloat() <= debug5.largeProbability);
/* 125 */     float debug7 = debug6 ? 0.9F : 0.8F;
/*     */     
/* 127 */     addPiece(debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     
/* 129 */     if (debug6 && debug4.nextFloat() <= debug5.clusterProbability) {
/* 130 */       addClusterRuins(debug0, debug4, debug2, debug1, debug5, debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addClusterRuins(StructureManager debug0, Random debug1, Rotation debug2, BlockPos debug3, OceanRuinConfiguration debug4, List<StructurePiece> debug5) {
/* 135 */     int debug6 = debug3.getX();
/* 136 */     int debug7 = debug3.getZ();
/* 137 */     BlockPos debug8 = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, debug2, BlockPos.ZERO).offset(debug6, 0, debug7);
/* 138 */     BoundingBox debug9 = BoundingBox.createProper(debug6, 0, debug7, debug8.getX(), 0, debug8.getZ());
/* 139 */     BlockPos debug10 = new BlockPos(Math.min(debug6, debug8.getX()), 0, Math.min(debug7, debug8.getZ()));
/* 140 */     List<BlockPos> debug11 = allPositions(debug1, debug10.getX(), debug10.getZ());
/* 141 */     int debug12 = Mth.nextInt(debug1, 4, 8);
/*     */     
/* 143 */     for (int debug13 = 0; debug13 < debug12; debug13++) {
/* 144 */       if (!debug11.isEmpty()) {
/* 145 */         int debug14 = debug1.nextInt(debug11.size());
/* 146 */         BlockPos debug15 = debug11.remove(debug14);
/* 147 */         int debug16 = debug15.getX();
/* 148 */         int debug17 = debug15.getZ();
/* 149 */         Rotation debug18 = Rotation.getRandom(debug1);
/* 150 */         BlockPos debug19 = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, debug18, BlockPos.ZERO).offset(debug16, 0, debug17);
/* 151 */         BoundingBox debug20 = BoundingBox.createProper(debug16, 0, debug17, debug19.getX(), 0, debug19.getZ());
/* 152 */         if (!debug20.intersects(debug9))
/*     */         {
/*     */ 
/*     */           
/* 156 */           addPiece(debug0, debug15, debug18, debug5, debug1, debug4, false, 0.8F); } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<BlockPos> allPositions(Random debug0, int debug1, int debug2) {
/* 162 */     List<BlockPos> debug3 = Lists.newArrayList();
/* 163 */     debug3.add(new BlockPos(debug1 - 16 + Mth.nextInt(debug0, 1, 8), 90, debug2 + 16 + Mth.nextInt(debug0, 1, 7)));
/* 164 */     debug3.add(new BlockPos(debug1 - 16 + Mth.nextInt(debug0, 1, 8), 90, debug2 + Mth.nextInt(debug0, 1, 7)));
/* 165 */     debug3.add(new BlockPos(debug1 - 16 + Mth.nextInt(debug0, 1, 8), 90, debug2 - 16 + Mth.nextInt(debug0, 4, 8)));
/* 166 */     debug3.add(new BlockPos(debug1 + Mth.nextInt(debug0, 1, 7), 90, debug2 + 16 + Mth.nextInt(debug0, 1, 7)));
/* 167 */     debug3.add(new BlockPos(debug1 + Mth.nextInt(debug0, 1, 7), 90, debug2 - 16 + Mth.nextInt(debug0, 4, 6)));
/* 168 */     debug3.add(new BlockPos(debug1 + 16 + Mth.nextInt(debug0, 1, 7), 90, debug2 + 16 + Mth.nextInt(debug0, 3, 8)));
/* 169 */     debug3.add(new BlockPos(debug1 + 16 + Mth.nextInt(debug0, 1, 7), 90, debug2 + Mth.nextInt(debug0, 1, 7)));
/* 170 */     debug3.add(new BlockPos(debug1 + 16 + Mth.nextInt(debug0, 1, 7), 90, debug2 - 16 + Mth.nextInt(debug0, 4, 8)));
/*     */     
/* 172 */     return debug3;
/*     */   }
/*     */   
/*     */   private static void addPiece(StructureManager debug0, BlockPos debug1, Rotation debug2, List<StructurePiece> debug3, Random debug4, OceanRuinConfiguration debug5, boolean debug6, float debug7) {
/* 176 */     if (debug5.biomeTemp == OceanRuinFeature.Type.WARM) {
/* 177 */       ResourceLocation debug8 = debug6 ? getBigWarmRuin(debug4) : getSmallWarmRuin(debug4);
/* 178 */       debug3.add(new OceanRuinPiece(debug0, debug8, debug1, debug2, debug7, debug5.biomeTemp, debug6));
/* 179 */     } else if (debug5.biomeTemp == OceanRuinFeature.Type.COLD) {
/* 180 */       ResourceLocation[] debug8 = debug6 ? BIG_RUINS_BRICK : RUINS_BRICK;
/* 181 */       ResourceLocation[] debug9 = debug6 ? BIG_RUINS_CRACKED : RUINS_CRACKED;
/* 182 */       ResourceLocation[] debug10 = debug6 ? BIG_RUINS_MOSSY : RUINS_MOSSY;
/*     */       
/* 184 */       int debug11 = debug4.nextInt(debug8.length);
/* 185 */       debug3.add(new OceanRuinPiece(debug0, debug8[debug11], debug1, debug2, debug7, debug5.biomeTemp, debug6));
/* 186 */       debug3.add(new OceanRuinPiece(debug0, debug9[debug11], debug1, debug2, 0.7F, debug5.biomeTemp, debug6));
/* 187 */       debug3.add(new OceanRuinPiece(debug0, debug10[debug11], debug1, debug2, 0.5F, debug5.biomeTemp, debug6));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class OceanRuinPiece extends TemplateStructurePiece {
/*     */     private final OceanRuinFeature.Type biomeType;
/*     */     private final float integrity;
/*     */     private final ResourceLocation templateLocation;
/*     */     private final Rotation rotation;
/*     */     private final boolean isLarge;
/*     */     
/*     */     public OceanRuinPiece(StructureManager debug1, ResourceLocation debug2, BlockPos debug3, Rotation debug4, float debug5, OceanRuinFeature.Type debug6, boolean debug7) {
/* 199 */       super(StructurePieceType.OCEAN_RUIN, 0);
/*     */       
/* 201 */       this.templateLocation = debug2;
/* 202 */       this.templatePosition = debug3;
/* 203 */       this.rotation = debug4;
/* 204 */       this.integrity = debug5;
/* 205 */       this.biomeType = debug6;
/* 206 */       this.isLarge = debug7;
/*     */       
/* 208 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     public OceanRuinPiece(StructureManager debug1, CompoundTag debug2) {
/* 212 */       super(StructurePieceType.OCEAN_RUIN, debug2);
/* 213 */       this.templateLocation = new ResourceLocation(debug2.getString("Template"));
/* 214 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/* 215 */       this.integrity = debug2.getFloat("Integrity");
/* 216 */       this.biomeType = OceanRuinFeature.Type.valueOf(debug2.getString("BiomeType"));
/* 217 */       this.isLarge = debug2.getBoolean("IsLarge");
/* 218 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     private void loadTemplate(StructureManager debug1) {
/* 222 */       StructureTemplate debug2 = debug1.getOrCreate(this.templateLocation);
/* 223 */       StructurePlaceSettings debug3 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 224 */       setup(debug2, this.templatePosition, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 229 */       super.addAdditionalSaveData(debug1);
/* 230 */       debug1.putString("Template", this.templateLocation.toString());
/* 231 */       debug1.putString("Rot", this.rotation.name());
/* 232 */       debug1.putFloat("Integrity", this.integrity);
/* 233 */       debug1.putString("BiomeType", this.biomeType.toString());
/* 234 */       debug1.putBoolean("IsLarge", this.isLarge);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {
/* 239 */       if ("chest".equals(debug1)) {
/* 240 */         debug3.setBlock(debug2, (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.WATERLOGGED, Boolean.valueOf(debug3.getFluidState(debug2).is((Tag)FluidTags.WATER))), 2);
/*     */         
/* 242 */         BlockEntity debug6 = debug3.getBlockEntity(debug2);
/* 243 */         if (debug6 instanceof ChestBlockEntity) {
/* 244 */           ((ChestBlockEntity)debug6).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, debug4.nextLong());
/*     */         }
/*     */       }
/* 247 */       else if ("drowned".equals(debug1)) {
/* 248 */         Drowned debug6 = (Drowned)EntityType.DROWNED.create((Level)debug3.getLevel());
/* 249 */         debug6.setPersistenceRequired();
/* 250 */         debug6.moveTo(debug2, 0.0F, 0.0F);
/* 251 */         debug6.finalizeSpawn(debug3, debug3.getCurrentDifficultyAt(debug2), MobSpawnType.STRUCTURE, null, null);
/* 252 */         debug3.addFreshEntityWithPassengers((Entity)debug6);
/* 253 */         if (debug2.getY() > debug3.getSeaLevel()) {
/* 254 */           debug3.setBlock(debug2, Blocks.AIR.defaultBlockState(), 2);
/*     */         } else {
/* 256 */           debug3.setBlock(debug2, Blocks.WATER.defaultBlockState(), 2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 263 */       this.placeSettings.clearProcessors().addProcessor((StructureProcessor)new BlockRotProcessor(this.integrity)).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 264 */       int debug8 = debug1.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
/* 265 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), debug8, this.templatePosition.getZ());
/* 266 */       BlockPos debug9 = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.rotation, BlockPos.ZERO).offset((Vec3i)this.templatePosition);
/* 267 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), getHeight(this.templatePosition, (BlockGetter)debug1, debug9), this.templatePosition.getZ());
/*     */       
/* 269 */       return super.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     }
/*     */     
/*     */     private int getHeight(BlockPos debug1, BlockGetter debug2, BlockPos debug3) {
/* 273 */       int debug4 = debug1.getY();
/* 274 */       int debug5 = 512;
/* 275 */       int debug6 = debug4 - 1;
/* 276 */       int debug7 = 0;
/* 277 */       for (BlockPos debug9 : BlockPos.betweenClosed(debug1, debug3)) {
/* 278 */         int debug10 = debug9.getX();
/* 279 */         int debug11 = debug9.getZ();
/* 280 */         int debug12 = debug1.getY() - 1;
/* 281 */         BlockPos.MutableBlockPos debug13 = new BlockPos.MutableBlockPos(debug10, debug12, debug11);
/* 282 */         BlockState debug14 = debug2.getBlockState((BlockPos)debug13);
/* 283 */         FluidState debug15 = debug2.getFluidState((BlockPos)debug13);
/* 284 */         while ((debug14.isAir() || debug15.is((Tag)FluidTags.WATER) || debug14.getBlock().is((Tag)BlockTags.ICE)) && debug12 > 1) {
/* 285 */           debug12--;
/* 286 */           debug13.set(debug10, debug12, debug11);
/* 287 */           debug14 = debug2.getBlockState((BlockPos)debug13);
/* 288 */           debug15 = debug2.getFluidState((BlockPos)debug13);
/*     */         } 
/*     */         
/* 291 */         debug5 = Math.min(debug5, debug12);
/* 292 */         if (debug12 < debug6 - 2) {
/* 293 */           debug7++;
/*     */         }
/*     */       } 
/*     */       
/* 297 */       int debug8 = Math.abs(debug1.getX() - debug3.getX());
/* 298 */       if (debug6 - debug5 > 2 && debug7 > debug8 - 2) {
/* 299 */         debug4 = debug5 + 1;
/*     */       }
/*     */       
/* 302 */       return debug4;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\OceanRuinPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */