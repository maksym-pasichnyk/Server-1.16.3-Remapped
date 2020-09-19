/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RuinedPortalConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.RuinedPortalPiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ public class RuinedPortalFeature
/*     */   extends StructureFeature<RuinedPortalConfiguration> {
/*  34 */   private static final String[] STRUCTURE_LOCATION_PORTALS = new String[] { "ruined_portal/portal_1", "ruined_portal/portal_2", "ruined_portal/portal_3", "ruined_portal/portal_4", "ruined_portal/portal_5", "ruined_portal/portal_6", "ruined_portal/portal_7", "ruined_portal/portal_8", "ruined_portal/portal_9", "ruined_portal/portal_10" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final String[] STRUCTURE_LOCATION_GIANT_PORTALS = new String[] { "ruined_portal/giant_portal_1", "ruined_portal/giant_portal_2", "ruined_portal/giant_portal_3" };
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
/*     */   public RuinedPortalFeature(Codec<RuinedPortalConfiguration> debug1) {
/*  52 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureFeature.StructureStartFactory<RuinedPortalConfiguration> getStartFactory() {
/*  57 */     return FeatureStart::new;
/*     */   }
/*     */   
/*     */   public static class FeatureStart extends StructureStart<RuinedPortalConfiguration> {
/*     */     protected FeatureStart(StructureFeature<RuinedPortalConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/*  62 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     }
/*     */     
/*     */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, RuinedPortalConfiguration debug7) {
/*     */       RuinedPortalPiece.VerticalPlacement debug8;
/*     */       ResourceLocation debug10;
/*  68 */       RuinedPortalPiece.Properties debug9 = new RuinedPortalPiece.Properties();
/*     */       
/*  70 */       if (debug7.portalType == RuinedPortalFeature.Type.DESERT) {
/*  71 */         debug8 = RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED;
/*  72 */         debug9.airPocket = false;
/*  73 */         debug9.mossiness = 0.0F;
/*  74 */       } else if (debug7.portalType == RuinedPortalFeature.Type.JUNGLE) {
/*  75 */         debug8 = RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE;
/*  76 */         debug9.airPocket = (this.random.nextFloat() < 0.5F);
/*  77 */         debug9.mossiness = 0.8F;
/*  78 */         debug9.overgrown = true;
/*  79 */         debug9.vines = true;
/*  80 */       } else if (debug7.portalType == RuinedPortalFeature.Type.SWAMP) {
/*  81 */         debug8 = RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR;
/*  82 */         debug9.airPocket = false;
/*  83 */         debug9.mossiness = 0.5F;
/*  84 */         debug9.vines = true;
/*  85 */       } else if (debug7.portalType == RuinedPortalFeature.Type.MOUNTAIN) {
/*  86 */         boolean bool = (this.random.nextFloat() < 0.5F);
/*  87 */         debug8 = bool ? RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN : RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE;
/*  88 */         debug9.airPocket = (bool || this.random.nextFloat() < 0.5F);
/*  89 */       } else if (debug7.portalType == RuinedPortalFeature.Type.OCEAN) {
/*  90 */         debug8 = RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR;
/*  91 */         debug9.airPocket = false;
/*  92 */         debug9.mossiness = 0.8F;
/*  93 */       } else if (debug7.portalType == RuinedPortalFeature.Type.NETHER) {
/*  94 */         debug8 = RuinedPortalPiece.VerticalPlacement.IN_NETHER;
/*  95 */         debug9.airPocket = (this.random.nextFloat() < 0.5F);
/*  96 */         debug9.mossiness = 0.0F;
/*  97 */         debug9.replaceWithBlackstone = true;
/*     */       } else {
/*  99 */         boolean bool = (this.random.nextFloat() < 0.5F);
/* 100 */         debug8 = bool ? RuinedPortalPiece.VerticalPlacement.UNDERGROUND : RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE;
/* 101 */         debug9.airPocket = (bool || this.random.nextFloat() < 0.5F);
/*     */       } 
/*     */ 
/*     */       
/* 105 */       if (this.random.nextFloat() < 0.05F) {
/* 106 */         debug10 = new ResourceLocation(RuinedPortalFeature.STRUCTURE_LOCATION_GIANT_PORTALS[this.random.nextInt(RuinedPortalFeature.STRUCTURE_LOCATION_GIANT_PORTALS.length)]);
/*     */       } else {
/* 108 */         debug10 = new ResourceLocation(RuinedPortalFeature.STRUCTURE_LOCATION_PORTALS[this.random.nextInt(RuinedPortalFeature.STRUCTURE_LOCATION_PORTALS.length)]);
/*     */       } 
/*     */       
/* 111 */       StructureTemplate debug11 = debug3.getOrCreate(debug10);
/* 112 */       Rotation debug12 = (Rotation)Util.getRandom((Object[])Rotation.values(), (Random)this.random);
/* 113 */       Mirror debug13 = (this.random.nextFloat() < 0.5F) ? Mirror.NONE : Mirror.FRONT_BACK;
/* 114 */       BlockPos debug14 = new BlockPos(debug11.getSize().getX() / 2, 0, debug11.getSize().getZ() / 2);
/*     */       
/* 116 */       BlockPos debug15 = (new ChunkPos(debug4, debug5)).getWorldPosition();
/* 117 */       BoundingBox debug16 = debug11.getBoundingBox(debug15, debug12, debug14, debug13);
/* 118 */       Vec3i debug17 = debug16.getCenter();
/* 119 */       int debug18 = debug17.getX();
/* 120 */       int debug19 = debug17.getZ();
/* 121 */       int debug20 = debug2.getBaseHeight(debug18, debug19, RuinedPortalPiece.getHeightMapType(debug8)) - 1;
/* 122 */       int debug21 = RuinedPortalFeature.findSuitableY((Random)this.random, debug2, debug8, debug9.airPocket, debug20, debug16.getYSpan(), debug16);
/*     */       
/* 124 */       BlockPos debug22 = new BlockPos(debug15.getX(), debug21, debug15.getZ());
/*     */       
/* 126 */       if (debug7.portalType == RuinedPortalFeature.Type.MOUNTAIN || debug7.portalType == RuinedPortalFeature.Type.OCEAN || debug7.portalType == RuinedPortalFeature.Type.STANDARD) {
/* 127 */         debug9.cold = RuinedPortalFeature.isCold(debug22, debug6);
/*     */       }
/*     */       
/* 130 */       this.pieces.add(new RuinedPortalPiece(debug22, debug8, debug9, debug10, debug11, debug12, debug13, debug14));
/* 131 */       calculateBoundingBox();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isCold(BlockPos debug0, Biome debug1) {
/* 136 */     return (debug1.getTemperature(debug0) < 0.15F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int findSuitableY(Random debug0, ChunkGenerator debug1, RuinedPortalPiece.VerticalPlacement debug2, boolean debug3, int debug4, int debug5, BoundingBox debug6) {
/*     */     int debug7;
/* 143 */     if (debug2 == RuinedPortalPiece.VerticalPlacement.IN_NETHER) {
/* 144 */       if (debug3) {
/*     */         
/* 146 */         debug7 = randomIntInclusive(debug0, 32, 100);
/*     */       }
/* 148 */       else if (debug0.nextFloat() < 0.5F) {
/*     */         
/* 150 */         debug7 = randomIntInclusive(debug0, 27, 29);
/*     */       } else {
/*     */         
/* 153 */         debug7 = randomIntInclusive(debug0, 29, 100);
/*     */       }
/*     */     
/* 156 */     } else if (debug2 == RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN) {
/* 157 */       int debug8 = debug4 - debug5;
/* 158 */       debug7 = getRandomWithinInterval(debug0, 70, debug8);
/* 159 */     } else if (debug2 == RuinedPortalPiece.VerticalPlacement.UNDERGROUND) {
/* 160 */       int debug8 = debug4 - debug5;
/* 161 */       debug7 = getRandomWithinInterval(debug0, 15, debug8);
/* 162 */     } else if (debug2 == RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED) {
/* 163 */       debug7 = debug4 - debug5 + randomIntInclusive(debug0, 2, 8);
/*     */     } else {
/* 165 */       debug7 = debug4;
/*     */     } 
/*     */     
/* 168 */     ImmutableList immutableList = ImmutableList.of(new BlockPos(debug6.x0, 0, debug6.z0), new BlockPos(debug6.x1, 0, debug6.z0), new BlockPos(debug6.x0, 0, debug6.z1), new BlockPos(debug6.x1, 0, debug6.z1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     List<BlockGetter> debug9 = (List<BlockGetter>)immutableList.stream().map(debug1 -> debug0.getBaseColumn(debug1.getX(), debug1.getZ())).collect(Collectors.toList());
/*     */     
/* 177 */     Heightmap.Types debug10 = (debug2 == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
/*     */     
/* 179 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/* 180 */     int debug12 = debug7;
/*     */ 
/*     */ 
/*     */     
/* 184 */     label39: while (debug12 > 15) {
/* 185 */       int debug13 = 0;
/* 186 */       debug11.set(0, debug12, 0);
/* 187 */       for (BlockGetter debug15 : debug9) {
/*     */         
/* 189 */         BlockState debug16 = debug15.getBlockState((BlockPos)debug11);
/*     */         
/* 191 */         debug13++;
/* 192 */         if (debug16 != null && debug10.isOpaque().test(debug16) && debug13 == 3) {
/*     */           break label39;
/*     */         }
/*     */       } 
/*     */       
/* 197 */       debug12--;
/*     */     } 
/* 199 */     return debug12;
/*     */   }
/*     */   
/*     */   private static int randomIntInclusive(Random debug0, int debug1, int debug2) {
/* 203 */     return debug0.nextInt(debug2 - debug1 + 1) + debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getRandomWithinInterval(Random debug0, int debug1, int debug2) {
/* 208 */     if (debug1 < debug2) {
/* 209 */       return randomIntInclusive(debug0, debug1, debug2);
/*     */     }
/* 211 */     return debug2;
/*     */   }
/*     */   
/*     */   public enum Type
/*     */     implements StringRepresentable {
/* 216 */     STANDARD("standard"),
/* 217 */     DESERT("desert"),
/* 218 */     JUNGLE("jungle"),
/* 219 */     SWAMP("swamp"),
/* 220 */     MOUNTAIN("mountain"),
/* 221 */     OCEAN("ocean"),
/* 222 */     NETHER("nether");
/*     */     
/* 224 */     public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName); private static final Map<String, Type> BY_NAME;
/*     */     static {
/* 226 */       BY_NAME = (Map<String, Type>)Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::getName, debug0 -> debug0));
/*     */     }
/*     */     private final String name;
/*     */     Type(String debug3) {
/* 230 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 234 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Type byName(String debug0) {
/* 238 */       return BY_NAME.get(debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 243 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RuinedPortalFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */