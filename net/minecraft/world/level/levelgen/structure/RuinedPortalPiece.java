/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlackstoneReplaceProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RuinedPortalPiece extends TemplateStructurePiece {
/*  51 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ResourceLocation templateLocation;
/*     */   
/*     */   private final Rotation rotation;
/*     */   
/*     */   private final Mirror mirror;
/*     */   private final VerticalPlacement verticalPlacement;
/*     */   private final Properties properties;
/*     */   
/*     */   public static class Properties
/*     */   {
/*     */     public static final Codec<Properties> CODEC;
/*     */     public boolean cold;
/*     */     
/*     */     static {
/*  67 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.BOOL.fieldOf("cold").forGetter(()), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(()), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(()), (App)Codec.BOOL.fieldOf("overgrown").forGetter(()), (App)Codec.BOOL.fieldOf("vines").forGetter(()), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(())).apply((Applicative)debug0, Properties::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     public float mossiness = 0.2F;
/*     */     
/*     */     public boolean airPocket;
/*     */     
/*     */     public boolean overgrown;
/*     */     
/*     */     public boolean vines;
/*     */     public boolean replaceWithBlackstone;
/*     */     
/*     */     public <T> Properties(boolean debug1, float debug2, boolean debug3, boolean debug4, boolean debug5, boolean debug6) {
/*  87 */       this.cold = debug1;
/*  88 */       this.mossiness = debug2;
/*  89 */       this.airPocket = debug3;
/*  90 */       this.overgrown = debug4;
/*  91 */       this.vines = debug5;
/*  92 */       this.replaceWithBlackstone = debug6;
/*     */     }
/*     */     public Properties() {} }
/*     */   
/*     */   public RuinedPortalPiece(BlockPos debug1, VerticalPlacement debug2, Properties debug3, ResourceLocation debug4, StructureTemplate debug5, Rotation debug6, Mirror debug7, BlockPos debug8) {
/*  97 */     super(StructurePieceType.RUINED_PORTAL, 0);
/*     */     
/*  99 */     this.templatePosition = debug1;
/* 100 */     this.templateLocation = debug4;
/* 101 */     this.rotation = debug6;
/* 102 */     this.mirror = debug7;
/*     */     
/* 104 */     this.verticalPlacement = debug2;
/* 105 */     this.properties = debug3;
/*     */     
/* 107 */     loadTemplate(debug5, debug8);
/*     */   }
/*     */   
/*     */   public RuinedPortalPiece(StructureManager debug1, CompoundTag debug2) {
/* 111 */     super(StructurePieceType.RUINED_PORTAL, debug2);
/* 112 */     this.templateLocation = new ResourceLocation(debug2.getString("Template"));
/* 113 */     this.rotation = Rotation.valueOf(debug2.getString("Rotation"));
/* 114 */     this.mirror = Mirror.valueOf(debug2.getString("Mirror"));
/* 115 */     this.verticalPlacement = VerticalPlacement.byName(debug2.getString("VerticalPlacement"));
/* 116 */     this.properties = (Properties)Properties.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug2.get("Properties"))).getOrThrow(true, LOGGER::error);
/*     */     
/* 118 */     StructureTemplate debug3 = debug1.getOrCreate(this.templateLocation);
/* 119 */     loadTemplate(debug3, new BlockPos(debug3.getSize().getX() / 2, 0, debug3.getSize().getZ() / 2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 124 */     super.addAdditionalSaveData(debug1);
/* 125 */     debug1.putString("Template", this.templateLocation.toString());
/* 126 */     debug1.putString("Rotation", this.rotation.name());
/* 127 */     debug1.putString("Mirror", this.mirror.name());
/* 128 */     debug1.putString("VerticalPlacement", this.verticalPlacement.getName());
/* 129 */     Properties.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.properties).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("Properties", debug1));
/*     */   }
/*     */   
/*     */   private void loadTemplate(StructureTemplate debug1, BlockPos debug2) {
/* 133 */     BlockIgnoreProcessor debug3 = this.properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/*     */     
/* 135 */     List<ProcessorRule> debug4 = Lists.newArrayList();
/* 136 */     debug4.add(getBlockReplaceRule(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
/* 137 */     debug4.add(getLavaProcessorRule());
/* 138 */     if (!this.properties.cold) {
/* 139 */       debug4.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07F, Blocks.MAGMA_BLOCK));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     StructurePlaceSettings debug5 = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(this.mirror).setRotationPivot(debug2).addProcessor((StructureProcessor)debug3).addProcessor((StructureProcessor)new RuleProcessor(debug4)).addProcessor((StructureProcessor)new BlockAgeProcessor(this.properties.mossiness)).addProcessor((StructureProcessor)new LavaSubmergedBlockProcessor());
/*     */     
/* 151 */     if (this.properties.replaceWithBlackstone) {
/* 152 */       debug5.addProcessor((StructureProcessor)BlackstoneReplaceProcessor.INSTANCE);
/*     */     }
/* 154 */     setup(debug1, this.templatePosition, debug5);
/*     */   }
/*     */   
/*     */   private ProcessorRule getLavaProcessorRule() {
/* 158 */     if (this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR)
/* 159 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.MAGMA_BLOCK); 
/* 160 */     if (this.properties.cold) {
/* 161 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK);
/*     */     }
/* 163 */     return getBlockReplaceRule(Blocks.LAVA, 0.2F, Blocks.MAGMA_BLOCK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 170 */     if (!debug5.isInside((Vec3i)this.templatePosition))
/*     */     {
/* 172 */       return true;
/*     */     }
/* 174 */     debug5.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
/*     */     
/* 176 */     boolean debug8 = super.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     
/* 178 */     spreadNetherrack(debug4, (LevelAccessor)debug1);
/* 179 */     addNetherrackDripColumnsBelowPortal(debug4, (LevelAccessor)debug1);
/*     */     
/* 181 */     if (this.properties.vines || this.properties.overgrown) {
/* 182 */       BlockPos.betweenClosedStream(getBoundingBox()).forEach(debug3 -> {
/*     */             if (this.properties.vines) {
/*     */               maybeAddVines(debug1, (LevelAccessor)debug2, debug3);
/*     */             }
/*     */             
/*     */             if (this.properties.overgrown) {
/*     */               maybeAddLeavesAbove(debug1, (LevelAccessor)debug2, debug3);
/*     */             }
/*     */           });
/*     */     }
/* 192 */     return debug8;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {}
/*     */ 
/*     */   
/*     */   private void maybeAddVines(Random debug1, LevelAccessor debug2, BlockPos debug3) {
/* 201 */     BlockState debug4 = debug2.getBlockState(debug3);
/* 202 */     if (debug4.isAir() || debug4.is(Blocks.VINE)) {
/*     */       return;
/*     */     }
/*     */     
/* 206 */     Direction debug5 = Direction.Plane.HORIZONTAL.getRandomDirection(debug1);
/* 207 */     BlockPos debug6 = debug3.relative(debug5);
/* 208 */     BlockState debug7 = debug2.getBlockState(debug6);
/* 209 */     if (!debug7.isAir()) {
/*     */       return;
/*     */     }
/* 212 */     if (!Block.isFaceFull(debug4.getCollisionShape((BlockGetter)debug2, debug3), debug5)) {
/*     */       return;
/*     */     }
/* 215 */     BooleanProperty debug8 = VineBlock.getPropertyForFace(debug5.getOpposite());
/* 216 */     debug2.setBlock(debug6, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)debug8, Boolean.valueOf(true)), 3);
/*     */   }
/*     */   
/*     */   private void maybeAddLeavesAbove(Random debug1, LevelAccessor debug2, BlockPos debug3) {
/* 220 */     if (debug1.nextFloat() < 0.5F && debug2.getBlockState(debug3).is(Blocks.NETHERRACK) && debug2.getBlockState(debug3.above()).isAir()) {
/* 221 */       debug2.setBlock(debug3.above(), (BlockState)Blocks.JUNGLE_LEAVES.defaultBlockState().setValue((Property)LeavesBlock.PERSISTENT, Boolean.valueOf(true)), 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumnsBelowPortal(Random debug1, LevelAccessor debug2) {
/* 226 */     for (int debug3 = this.boundingBox.x0 + 1; debug3 < this.boundingBox.x1; debug3++) {
/* 227 */       for (int debug4 = this.boundingBox.z0 + 1; debug4 < this.boundingBox.z1; debug4++) {
/* 228 */         BlockPos debug5 = new BlockPos(debug3, this.boundingBox.y0, debug4);
/* 229 */         if (debug2.getBlockState(debug5).is(Blocks.NETHERRACK)) {
/* 230 */           addNetherrackDripColumn(debug1, debug2, debug5.below());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumn(Random debug1, LevelAccessor debug2, BlockPos debug3) {
/* 237 */     BlockPos.MutableBlockPos debug4 = debug3.mutable();
/* 238 */     placeNetherrackOrMagma(debug1, debug2, (BlockPos)debug4);
/* 239 */     int debug5 = 8;
/* 240 */     while (debug5 > 0 && debug1.nextFloat() < 0.5F) {
/* 241 */       debug4.move(Direction.DOWN);
/* 242 */       debug5--;
/* 243 */       placeNetherrackOrMagma(debug1, debug2, (BlockPos)debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spreadNetherrack(Random debug1, LevelAccessor debug2) {
/* 248 */     boolean debug3 = (this.verticalPlacement == VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR);
/*     */     
/* 250 */     Vec3i debug4 = this.boundingBox.getCenter();
/* 251 */     int debug5 = debug4.getX();
/* 252 */     int debug6 = debug4.getZ();
/*     */     
/* 254 */     float[] debug7 = { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 0.7F, 0.6F, 0.4F, 0.2F };
/* 255 */     int debug8 = debug7.length;
/* 256 */     int debug9 = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
/* 257 */     int debug10 = debug1.nextInt(Math.max(1, 8 - debug9 / 2));
/* 258 */     int debug11 = 3;
/* 259 */     BlockPos.MutableBlockPos debug12 = BlockPos.ZERO.mutable();
/* 260 */     for (int debug13 = debug5 - debug8; debug13 <= debug5 + debug8; debug13++) {
/* 261 */       for (int debug14 = debug6 - debug8; debug14 <= debug6 + debug8; debug14++) {
/* 262 */         int debug15 = Math.abs(debug13 - debug5) + Math.abs(debug14 - debug6);
/* 263 */         int debug16 = Math.max(0, debug15 + debug10);
/* 264 */         if (debug16 < debug8) {
/*     */ 
/*     */           
/* 267 */           float debug17 = debug7[debug16];
/* 268 */           if (debug1.nextDouble() < debug17) {
/* 269 */             int debug18 = getSurfaceY(debug2, debug13, debug14, this.verticalPlacement);
/* 270 */             int debug19 = debug3 ? debug18 : Math.min(this.boundingBox.y0, debug18);
/* 271 */             debug12.set(debug13, debug19, debug14);
/* 272 */             if (Math.abs(debug19 - this.boundingBox.y0) <= 3 && canBlockBeReplacedByNetherrackOrMagma(debug2, (BlockPos)debug12)) {
/* 273 */               placeNetherrackOrMagma(debug1, debug2, (BlockPos)debug12);
/* 274 */               if (this.properties.overgrown) {
/* 275 */                 maybeAddLeavesAbove(debug1, debug2, (BlockPos)debug12);
/*     */               }
/* 277 */               addNetherrackDripColumn(debug1, debug2, debug12.below());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean canBlockBeReplacedByNetherrackOrMagma(LevelAccessor debug1, BlockPos debug2) {
/* 285 */     BlockState debug3 = debug1.getBlockState(debug2);
/* 286 */     return (!debug3.is(Blocks.AIR) && 
/* 287 */       !debug3.is(Blocks.OBSIDIAN) && 
/* 288 */       !debug3.is(Blocks.CHEST) && (this.verticalPlacement == VerticalPlacement.IN_NETHER || 
/* 289 */       !debug3.is(Blocks.LAVA)));
/*     */   }
/*     */   
/*     */   private void placeNetherrackOrMagma(Random debug1, LevelAccessor debug2, BlockPos debug3) {
/* 293 */     if (!this.properties.cold && debug1.nextFloat() < 0.07F) {
/* 294 */       debug2.setBlock(debug3, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
/*     */     } else {
/* 296 */       debug2.setBlock(debug3, Blocks.NETHERRACK.defaultBlockState(), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getSurfaceY(LevelAccessor debug0, int debug1, int debug2, VerticalPlacement debug3) {
/* 301 */     return debug0.getHeight(getHeightMapType(debug3), debug1, debug2) - 1;
/*     */   }
/*     */   
/*     */   public static Heightmap.Types getHeightMapType(VerticalPlacement debug0) {
/* 305 */     return (debug0 == VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block debug0, float debug1, Block debug2) {
/* 309 */     return new ProcessorRule((RuleTest)new RandomBlockMatchTest(debug0, debug1), (RuleTest)AlwaysTrueTest.INSTANCE, debug2.defaultBlockState());
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block debug0, Block debug1) {
/* 313 */     return new ProcessorRule((RuleTest)new BlockMatchTest(debug0), (RuleTest)AlwaysTrueTest.INSTANCE, debug1.defaultBlockState());
/*     */   }
/*     */   
/*     */   public enum VerticalPlacement {
/* 317 */     ON_LAND_SURFACE("on_land_surface"),
/* 318 */     PARTLY_BURIED("partly_buried"),
/* 319 */     ON_OCEAN_FLOOR("on_ocean_floor"),
/* 320 */     IN_MOUNTAIN("in_mountain"),
/* 321 */     UNDERGROUND("underground"),
/* 322 */     IN_NETHER("in_nether"); private static final Map<String, VerticalPlacement> BY_NAME; private final String name;
/*     */     
/*     */     static {
/* 325 */       BY_NAME = (Map<String, VerticalPlacement>)Arrays.<VerticalPlacement>stream(values()).collect(Collectors.toMap(VerticalPlacement::getName, debug0 -> debug0));
/*     */     }
/*     */     
/*     */     VerticalPlacement(String debug3) {
/* 329 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 333 */       return this.name;
/*     */     }
/*     */     
/*     */     public static VerticalPlacement byName(String debug0) {
/* 337 */       return BY_NAME.get(debug0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\RuinedPortalPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */