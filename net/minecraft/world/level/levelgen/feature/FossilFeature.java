/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ public class FossilFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  26 */   private static final ResourceLocation SPINE_1 = new ResourceLocation("fossil/spine_1");
/*  27 */   private static final ResourceLocation SPINE_2 = new ResourceLocation("fossil/spine_2");
/*  28 */   private static final ResourceLocation SPINE_3 = new ResourceLocation("fossil/spine_3");
/*  29 */   private static final ResourceLocation SPINE_4 = new ResourceLocation("fossil/spine_4");
/*     */   
/*  31 */   private static final ResourceLocation SPINE_1_COAL = new ResourceLocation("fossil/spine_1_coal");
/*  32 */   private static final ResourceLocation SPINE_2_COAL = new ResourceLocation("fossil/spine_2_coal");
/*  33 */   private static final ResourceLocation SPINE_3_COAL = new ResourceLocation("fossil/spine_3_coal");
/*  34 */   private static final ResourceLocation SPINE_4_COAL = new ResourceLocation("fossil/spine_4_coal");
/*     */   
/*  36 */   private static final ResourceLocation SKULL_1 = new ResourceLocation("fossil/skull_1");
/*  37 */   private static final ResourceLocation SKULL_2 = new ResourceLocation("fossil/skull_2");
/*  38 */   private static final ResourceLocation SKULL_3 = new ResourceLocation("fossil/skull_3");
/*  39 */   private static final ResourceLocation SKULL_4 = new ResourceLocation("fossil/skull_4");
/*     */   
/*  41 */   private static final ResourceLocation SKULL_1_COAL = new ResourceLocation("fossil/skull_1_coal");
/*  42 */   private static final ResourceLocation SKULL_2_COAL = new ResourceLocation("fossil/skull_2_coal");
/*  43 */   private static final ResourceLocation SKULL_3_COAL = new ResourceLocation("fossil/skull_3_coal");
/*  44 */   private static final ResourceLocation SKULL_4_COAL = new ResourceLocation("fossil/skull_4_coal");
/*     */   
/*  46 */   private static final ResourceLocation[] fossils = new ResourceLocation[] { SPINE_1, SPINE_2, SPINE_3, SPINE_4, SKULL_1, SKULL_2, SKULL_3, SKULL_4 };
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
/*  57 */   private static final ResourceLocation[] fossilsCoal = new ResourceLocation[] { SPINE_1_COAL, SPINE_2_COAL, SPINE_3_COAL, SPINE_4_COAL, SKULL_1_COAL, SKULL_2_COAL, SKULL_3_COAL, SKULL_4_COAL };
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
/*     */   public FossilFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  69 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/*  74 */     Rotation debug6 = Rotation.getRandom(debug3);
/*     */     
/*  76 */     int debug7 = debug3.nextInt(fossils.length);
/*     */ 
/*     */     
/*  79 */     StructureManager debug8 = debug1.getLevel().getServer().getStructureManager();
/*  80 */     StructureTemplate debug9 = debug8.getOrCreate(fossils[debug7]);
/*  81 */     StructureTemplate debug10 = debug8.getOrCreate(fossilsCoal[debug7]);
/*  82 */     ChunkPos debug11 = new ChunkPos(debug4);
/*  83 */     BoundingBox debug12 = new BoundingBox(debug11.getMinBlockX(), 0, debug11.getMinBlockZ(), debug11.getMaxBlockX(), 256, debug11.getMaxBlockZ());
/*  84 */     StructurePlaceSettings debug13 = (new StructurePlaceSettings()).setRotation(debug6).setBoundingBox(debug12).setRandom(debug3).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*     */     
/*  86 */     BlockPos debug14 = debug9.getSize(debug6);
/*  87 */     int debug15 = debug3.nextInt(16 - debug14.getX());
/*  88 */     int debug16 = debug3.nextInt(16 - debug14.getZ());
/*     */     
/*  90 */     int debug17 = 256; int debug18;
/*  91 */     for (debug18 = 0; debug18 < debug14.getX(); debug18++) {
/*  92 */       for (int i = 0; i < debug14.getZ(); i++) {
/*  93 */         debug17 = Math.min(debug17, debug1.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, debug4.getX() + debug18 + debug15, debug4.getZ() + i + debug16));
/*     */       }
/*     */     } 
/*  96 */     debug18 = Math.max(debug17 - 15 - debug3.nextInt(10), 10);
/*     */     
/*  98 */     BlockPos debug19 = debug9.getZeroPositionWithTransform(debug4.offset(debug15, debug18, debug16), Mirror.NONE, debug6);
/*     */     
/* 100 */     BlockRotProcessor debug20 = new BlockRotProcessor(0.9F);
/* 101 */     debug13.clearProcessors().addProcessor((StructureProcessor)debug20);
/* 102 */     debug9.placeInWorld((ServerLevelAccessor)debug1, debug19, debug19, debug13, debug3, 4);
/* 103 */     debug13.popProcessor((StructureProcessor)debug20);
/*     */     
/* 105 */     BlockRotProcessor debug21 = new BlockRotProcessor(0.1F);
/* 106 */     debug13.clearProcessors().addProcessor((StructureProcessor)debug21);
/* 107 */     debug10.placeInWorld((ServerLevelAccessor)debug1, debug19, debug19, debug13, debug3, 4);
/*     */     
/* 109 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FossilFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */