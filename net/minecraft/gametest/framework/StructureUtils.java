/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureUtils
/*     */ {
/*  48 */   public static String testStructuresDir = "gameteststructures";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rotation getRotationForRotationSteps(int debug0) {
/*  54 */     switch (debug0) { case 0:
/*  55 */         return Rotation.NONE;
/*  56 */       case 1: return Rotation.CLOCKWISE_90;
/*  57 */       case 2: return Rotation.CLOCKWISE_180;
/*  58 */       case 3: return Rotation.COUNTERCLOCKWISE_90; }
/*  59 */      throw new IllegalArgumentException("rotationSteps must be a value from 0-3. Got value " + debug0);
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
/*     */   public static AABB getStructureBounds(StructureBlockEntity debug0) {
/*  74 */     BlockPos debug1 = debug0.getBlockPos();
/*  75 */     BlockPos debug2 = debug1.offset((Vec3i)debug0.getStructureSize().offset(-1, -1, -1));
/*  76 */     BlockPos debug3 = StructureTemplate.transform(debug2, Mirror.NONE, debug0.getRotation(), debug1);
/*     */     
/*  78 */     return new AABB(debug1, debug3);
/*     */   }
/*     */   
/*     */   public static BoundingBox getStructureBoundingBox(StructureBlockEntity debug0) {
/*  82 */     BlockPos debug1 = debug0.getBlockPos();
/*  83 */     BlockPos debug2 = debug1.offset((Vec3i)debug0.getStructureSize().offset(-1, -1, -1));
/*  84 */     BlockPos debug3 = StructureTemplate.transform(debug2, Mirror.NONE, debug0.getRotation(), debug1);
/*     */     
/*  86 */     return new BoundingBox((Vec3i)debug1, (Vec3i)debug3);
/*     */   }
/*     */   
/*     */   public static void addCommandBlockAndButtonToStartTest(BlockPos debug0, BlockPos debug1, Rotation debug2, ServerLevel debug3) {
/*  90 */     BlockPos debug4 = StructureTemplate.transform(debug0.offset((Vec3i)debug1), Mirror.NONE, debug2, debug0);
/*  91 */     debug3.setBlockAndUpdate(debug4, Blocks.COMMAND_BLOCK.defaultBlockState());
/*  92 */     CommandBlockEntity debug5 = (CommandBlockEntity)debug3.getBlockEntity(debug4);
/*  93 */     debug5.getCommandBlock().setCommand("test runthis");
/*     */     
/*  95 */     BlockPos debug6 = StructureTemplate.transform(debug4.offset(0, 0, -1), Mirror.NONE, debug2, debug4);
/*     */     
/*  97 */     debug3.setBlockAndUpdate(debug6, Blocks.STONE_BUTTON.defaultBlockState().rotate(debug2));
/*     */   }
/*     */   
/*     */   public static void createNewEmptyStructureBlock(String debug0, BlockPos debug1, BlockPos debug2, Rotation debug3, ServerLevel debug4) {
/* 101 */     BoundingBox debug5 = getStructureBoundingBox(debug1, debug2, debug3);
/* 102 */     clearSpaceForStructure(debug5, debug1.getY(), debug4);
/*     */     
/* 104 */     debug4.setBlockAndUpdate(debug1, Blocks.STRUCTURE_BLOCK.defaultBlockState());
/*     */     
/* 106 */     StructureBlockEntity debug6 = (StructureBlockEntity)debug4.getBlockEntity(debug1);
/* 107 */     debug6.setIgnoreEntities(false);
/* 108 */     debug6.setStructureName(new ResourceLocation(debug0));
/* 109 */     debug6.setStructureSize(debug2);
/* 110 */     debug6.setMode(StructureMode.SAVE);
/* 111 */     debug6.setShowBoundingBox(true);
/*     */   }
/*     */   
/*     */   public static StructureBlockEntity spawnStructure(String debug0, BlockPos debug1, Rotation debug2, int debug3, ServerLevel debug4, boolean debug5) {
/* 115 */     BlockPos debug8, debug6 = getStructureTemplate(debug0, debug4).getSize();
/* 116 */     BoundingBox debug7 = getStructureBoundingBox(debug1, debug6, debug2);
/*     */ 
/*     */     
/* 119 */     if (debug2 == Rotation.NONE) {
/* 120 */       debug8 = debug1;
/* 121 */     } else if (debug2 == Rotation.CLOCKWISE_90) {
/* 122 */       debug8 = debug1.offset(debug6.getZ() - 1, 0, 0);
/* 123 */     } else if (debug2 == Rotation.CLOCKWISE_180) {
/* 124 */       debug8 = debug1.offset(debug6.getX() - 1, 0, debug6.getZ() - 1);
/* 125 */     } else if (debug2 == Rotation.COUNTERCLOCKWISE_90) {
/* 126 */       debug8 = debug1.offset(0, 0, debug6.getX() - 1);
/*     */     } else {
/* 128 */       throw new IllegalArgumentException("Invalid rotation: " + debug2);
/*     */     } 
/*     */     
/* 131 */     forceLoadChunks(debug1, debug4);
/*     */     
/* 133 */     clearSpaceForStructure(debug7, debug1.getY(), debug4);
/* 134 */     StructureBlockEntity debug9 = createStructureBlock(debug0, debug8, debug2, debug4, debug5);
/* 135 */     debug4.getBlockTicks().fetchTicksInArea(debug7, true, false);
/* 136 */     debug4.clearBlockEvents(debug7);
/* 137 */     return debug9;
/*     */   }
/*     */   
/*     */   private static void forceLoadChunks(BlockPos debug0, ServerLevel debug1) {
/* 141 */     ChunkPos debug2 = new ChunkPos(debug0);
/*     */ 
/*     */     
/* 144 */     for (int debug3 = -1; debug3 < 4; debug3++) {
/* 145 */       for (int debug4 = -1; debug4 < 4; debug4++) {
/* 146 */         int debug5 = debug2.x + debug3;
/* 147 */         int debug6 = debug2.z + debug4;
/* 148 */         debug1.setChunkForced(debug5, debug6, true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearSpaceForStructure(BoundingBox debug0, int debug1, ServerLevel debug2) {
/* 155 */     BoundingBox debug3 = new BoundingBox(debug0.x0 - 2, debug0.y0 - 3, debug0.z0 - 3, debug0.x1 + 3, debug0.y1 + 20, debug0.z1 + 3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     BlockPos.betweenClosedStream(debug3).forEach(debug2 -> clearBlock(debug0, debug2, debug1));
/* 165 */     debug2.getBlockTicks().fetchTicksInArea(debug3, true, false);
/* 166 */     debug2.clearBlockEvents(debug3);
/* 167 */     AABB debug4 = new AABB(debug3.x0, debug3.y0, debug3.z0, debug3.x1, debug3.y1, debug3.z1);
/* 168 */     List<Entity> debug5 = debug2.getEntitiesOfClass(Entity.class, debug4, debug0 -> !(debug0 instanceof net.minecraft.world.entity.player.Player));
/* 169 */     debug5.forEach(Entity::remove);
/*     */   }
/*     */   
/*     */   public static BoundingBox getStructureBoundingBox(BlockPos debug0, BlockPos debug1, Rotation debug2) {
/* 173 */     BlockPos debug3 = debug0.offset((Vec3i)debug1).offset(-1, -1, -1);
/* 174 */     BlockPos debug4 = StructureTemplate.transform(debug3, Mirror.NONE, debug2, debug0);
/* 175 */     BoundingBox debug5 = BoundingBox.createProper(debug0.getX(), debug0.getY(), debug0.getZ(), debug4.getX(), debug4.getY(), debug4.getZ());
/*     */     
/* 177 */     int debug6 = Math.min(debug5.x0, debug5.x1);
/* 178 */     int debug7 = Math.min(debug5.z0, debug5.z1);
/*     */ 
/*     */     
/* 181 */     BlockPos debug8 = new BlockPos(debug0.getX() - debug6, 0, debug0.getZ() - debug7);
/* 182 */     debug5.move((Vec3i)debug8);
/* 183 */     return debug5;
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findStructureBlockContainingPos(BlockPos debug0, int debug1, ServerLevel debug2) {
/* 187 */     return findStructureBlocks(debug0, debug1, debug2).stream()
/* 188 */       .filter(debug2 -> doesStructureContain(debug2, debug0, debug1))
/* 189 */       .findFirst();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BlockPos findNearestStructureBlock(BlockPos debug0, int debug1, ServerLevel debug2) {
/* 194 */     Comparator<BlockPos> debug3 = Comparator.comparingInt(debug1 -> debug1.distManhattan((Vec3i)debug0));
/*     */     
/* 196 */     Collection<BlockPos> debug4 = findStructureBlocks(debug0, debug1, debug2);
/* 197 */     Optional<BlockPos> debug5 = debug4.stream().min(debug3);
/* 198 */     return debug5.orElse(null);
/*     */   }
/*     */   
/*     */   public static Collection<BlockPos> findStructureBlocks(BlockPos debug0, int debug1, ServerLevel debug2) {
/* 202 */     Collection<BlockPos> debug3 = Lists.newArrayList();
/*     */     
/* 204 */     AABB debug4 = new AABB(debug0);
/* 205 */     debug4 = debug4.inflate(debug1);
/* 206 */     for (int debug5 = (int)debug4.minX; debug5 <= (int)debug4.maxX; debug5++) {
/* 207 */       for (int debug6 = (int)debug4.minY; debug6 <= (int)debug4.maxY; debug6++) {
/* 208 */         for (int debug7 = (int)debug4.minZ; debug7 <= (int)debug4.maxZ; debug7++) {
/* 209 */           BlockPos debug8 = new BlockPos(debug5, debug6, debug7);
/* 210 */           BlockState debug9 = debug2.getBlockState(debug8);
/* 211 */           if (debug9.is(Blocks.STRUCTURE_BLOCK)) {
/* 212 */             debug3.add(debug8);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     return debug3;
/*     */   }
/*     */   
/*     */   private static StructureTemplate getStructureTemplate(String debug0, ServerLevel debug1) {
/* 221 */     StructureManager debug2 = debug1.getStructureManager();
/*     */ 
/*     */     
/* 224 */     StructureTemplate debug3 = debug2.get(new ResourceLocation(debug0));
/* 225 */     if (debug3 != null) {
/* 226 */       return debug3;
/*     */     }
/*     */ 
/*     */     
/* 230 */     String debug4 = debug0 + ".snbt";
/* 231 */     Path debug5 = Paths.get(testStructuresDir, new String[] { debug4 });
/* 232 */     CompoundTag debug6 = tryLoadStructure(debug5);
/* 233 */     if (debug6 == null) {
/* 234 */       throw new RuntimeException("Could not find structure file " + debug5 + ", and the structure is not available in the world structures either.");
/*     */     }
/*     */     
/* 237 */     return debug2.readStructure(debug6);
/*     */   }
/*     */   
/*     */   private static StructureBlockEntity createStructureBlock(String debug0, BlockPos debug1, Rotation debug2, ServerLevel debug3, boolean debug4) {
/* 241 */     debug3.setBlockAndUpdate(debug1, Blocks.STRUCTURE_BLOCK.defaultBlockState());
/*     */     
/* 243 */     StructureBlockEntity debug5 = (StructureBlockEntity)debug3.getBlockEntity(debug1);
/* 244 */     debug5.setMode(StructureMode.LOAD);
/* 245 */     debug5.setRotation(debug2);
/* 246 */     debug5.setIgnoreEntities(false);
/* 247 */     debug5.setStructureName(new ResourceLocation(debug0));
/*     */     
/* 249 */     debug5.loadStructure(debug3, debug4);
/* 250 */     if (debug5.getStructureSize() != BlockPos.ZERO) {
/* 251 */       return debug5;
/*     */     }
/* 253 */     StructureTemplate debug6 = getStructureTemplate(debug0, debug3);
/* 254 */     debug5.loadStructure(debug3, debug4, debug6);
/* 255 */     if (debug5.getStructureSize() == BlockPos.ZERO) {
/* 256 */       throw new RuntimeException("Failed to load structure " + debug0);
/*     */     }
/* 258 */     return debug5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static CompoundTag tryLoadStructure(Path debug0) {
/*     */     try {
/* 266 */       BufferedReader debug1 = Files.newBufferedReader(debug0);
/* 267 */       String debug2 = IOUtils.toString(debug1);
/* 268 */       return TagParser.parseTag(debug2);
/* 269 */     } catch (IOException debug1) {
/* 270 */       return null;
/* 271 */     } catch (CommandSyntaxException debug1) {
/* 272 */       throw new RuntimeException("Error while trying to load structure " + debug0, debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void clearBlock(int debug0, BlockPos debug1, ServerLevel debug2) {
/* 277 */     BlockState debug3 = null;
/*     */     
/* 279 */     FlatLevelGeneratorSettings debug4 = FlatLevelGeneratorSettings.getDefault((Registry)debug2.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
/* 280 */     if (debug4 instanceof FlatLevelGeneratorSettings) {
/* 281 */       BlockState[] arrayOfBlockState = debug4.getLayers();
/* 282 */       if (debug1.getY() < debug0 && debug1.getY() <= arrayOfBlockState.length) {
/* 283 */         debug3 = arrayOfBlockState[debug1.getY() - 1];
/*     */       }
/*     */     }
/* 286 */     else if (debug1.getY() == debug0 - 1) {
/* 287 */       debug3 = debug2.getBiome(debug1).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
/* 288 */     } else if (debug1.getY() < debug0 - 1) {
/* 289 */       debug3 = debug2.getBiome(debug1).getGenerationSettings().getSurfaceBuilderConfig().getUnderMaterial();
/*     */     } 
/*     */     
/* 292 */     if (debug3 == null) {
/* 293 */       debug3 = Blocks.AIR.defaultBlockState();
/*     */     }
/* 295 */     BlockInput debug5 = new BlockInput(debug3, Collections.emptySet(), null);
/* 296 */     debug5.place(debug2, debug1, 2);
/* 297 */     debug2.blockUpdated(debug1, debug3.getBlock());
/*     */   }
/*     */   
/*     */   private static boolean doesStructureContain(BlockPos debug0, BlockPos debug1, ServerLevel debug2) {
/* 301 */     StructureBlockEntity debug3 = (StructureBlockEntity)debug2.getBlockEntity(debug0);
/* 302 */     AABB debug4 = getStructureBounds(debug3).inflate(1.0D);
/* 303 */     return debug4.contains(Vec3.atCenterOf((Vec3i)debug1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\StructureUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */