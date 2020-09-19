/*     */ package net.minecraft.world.level.levelgen.feature.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class JigsawPlacement
/*     */ {
/*  38 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   static final class PieceState {
/*     */     private final PoolElementStructurePiece piece;
/*     */     private final MutableObject<VoxelShape> free;
/*     */     private final int boundsTop;
/*     */     private final int depth;
/*     */     
/*     */     private PieceState(PoolElementStructurePiece debug1, MutableObject<VoxelShape> debug2, int debug3, int debug4) {
/*  47 */       this.piece = debug1;
/*  48 */       this.free = debug2;
/*  49 */       this.boundsTop = debug3;
/*  50 */       this.depth = debug4;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Placer {
/*     */     private final Registry<StructureTemplatePool> pools;
/*     */     private final int maxDepth;
/*     */     private final JigsawPlacement.PieceFactory factory;
/*     */     private final ChunkGenerator chunkGenerator;
/*     */     private final StructureManager structureManager;
/*     */     private final List<? super PoolElementStructurePiece> pieces;
/*     */     private final Random random;
/*  62 */     private final Deque<JigsawPlacement.PieceState> placing = Queues.newArrayDeque();
/*     */     
/*     */     private Placer(Registry<StructureTemplatePool> debug1, int debug2, JigsawPlacement.PieceFactory debug3, ChunkGenerator debug4, StructureManager debug5, List<? super PoolElementStructurePiece> debug6, Random debug7) {
/*  65 */       this.pools = debug1;
/*  66 */       this.maxDepth = debug2;
/*  67 */       this.factory = debug3;
/*  68 */       this.chunkGenerator = debug4;
/*  69 */       this.structureManager = debug5;
/*  70 */       this.pieces = debug6;
/*  71 */       this.random = debug7;
/*     */     }
/*     */     
/*     */     private void tryPlacingChildren(PoolElementStructurePiece debug1, MutableObject<VoxelShape> debug2, int debug3, int debug4, boolean debug5) {
/*  75 */       StructurePoolElement debug6 = debug1.getElement();
/*  76 */       BlockPos debug7 = debug1.getPosition();
/*  77 */       Rotation debug8 = debug1.getRotation();
/*     */       
/*  79 */       StructureTemplatePool.Projection debug9 = debug6.getProjection();
/*  80 */       boolean debug10 = (debug9 == StructureTemplatePool.Projection.RIGID);
/*     */       
/*  82 */       MutableObject<VoxelShape> debug11 = new MutableObject();
/*     */       
/*  84 */       BoundingBox debug12 = debug1.getBoundingBox();
/*  85 */       int debug13 = debug12.y0;
/*     */       
/*  87 */       for (StructureTemplate.StructureBlockInfo debug15 : debug6.getShuffledJigsawBlocks(this.structureManager, debug7, debug8, this.random)) {
/*  88 */         MutableObject<VoxelShape> debug25; int debug26; Direction debug16 = JigsawBlock.getFrontFacing(debug15.state);
/*     */         
/*  90 */         BlockPos debug17 = debug15.pos;
/*  91 */         BlockPos debug18 = debug17.relative(debug16);
/*     */         
/*  93 */         int debug19 = debug17.getY() - debug13;
/*  94 */         int debug20 = -1;
/*     */         
/*  96 */         ResourceLocation debug21 = new ResourceLocation(debug15.nbt.getString("pool"));
/*  97 */         Optional<StructureTemplatePool> debug22 = this.pools.getOptional(debug21);
/*     */         
/*  99 */         if (!debug22.isPresent() || (((StructureTemplatePool)debug22.get()).size() == 0 && !Objects.equals(debug21, Pools.EMPTY.location()))) {
/* 100 */           JigsawPlacement.LOGGER.warn("Empty or none existent pool: {}", debug21);
/*     */           
/*     */           continue;
/*     */         } 
/* 104 */         ResourceLocation debug23 = ((StructureTemplatePool)debug22.get()).getFallback();
/* 105 */         Optional<StructureTemplatePool> debug24 = this.pools.getOptional(debug23);
/*     */         
/* 107 */         if (!debug24.isPresent() || (((StructureTemplatePool)debug24.get()).size() == 0 && !Objects.equals(debug23, Pools.EMPTY.location()))) {
/* 108 */           JigsawPlacement.LOGGER.warn("Empty or none existent fallback pool: {}", debug23);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 115 */         boolean debug27 = debug12.isInside((Vec3i)debug18);
/* 116 */         if (debug27) {
/* 117 */           debug25 = debug11;
/* 118 */           debug26 = debug13;
/* 119 */           if (debug11.getValue() == null) {
/* 120 */             debug11.setValue(Shapes.create(AABB.of(debug12)));
/*     */           }
/*     */         } else {
/* 123 */           debug25 = debug2;
/* 124 */           debug26 = debug3;
/*     */         } 
/*     */ 
/*     */         
/* 128 */         List<StructurePoolElement> debug28 = Lists.newArrayList();
/* 129 */         if (debug4 != this.maxDepth) {
/* 130 */           debug28.addAll(((StructureTemplatePool)debug22.get()).getShuffledTemplates(this.random));
/*     */         }
/* 132 */         debug28.addAll(((StructureTemplatePool)debug24.get()).getShuffledTemplates(this.random));
/*     */ 
/*     */         
/* 135 */         for (StructurePoolElement debug30 : debug28) {
/* 136 */           if (debug30 == EmptyPoolElement.INSTANCE) {
/*     */             break;
/*     */           }
/*     */           
/* 140 */           for (Rotation debug32 : Rotation.getShuffled(this.random)) {
/* 141 */             int debug35; List<StructureTemplate.StructureBlockInfo> debug33 = debug30.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, debug32, this.random);
/* 142 */             BoundingBox debug34 = debug30.getBoundingBox(this.structureManager, BlockPos.ZERO, debug32);
/*     */ 
/*     */             
/* 145 */             if (!debug5 || debug34.getYSpan() > 16) {
/* 146 */               debug35 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 158 */               debug35 = debug33.stream().mapToInt(debug2 -> { if (!debug1.isInside((Vec3i)debug2.pos.relative(JigsawBlock.getFrontFacing(debug2.state)))) return 0;  ResourceLocation debug3 = new ResourceLocation(debug2.nbt.getString("pool")); Optional<StructureTemplatePool> debug4 = this.pools.getOptional(debug3); Optional<StructureTemplatePool> debug5 = debug4.flatMap(()); int debug6 = ((Integer)debug4.<Integer>map(()).orElse(Integer.valueOf(0))).intValue(); int debug7 = ((Integer)debug5.<Integer>map(()).orElse(Integer.valueOf(0))).intValue(); return Math.max(debug6, debug7); }).max().orElse(0);
/*     */             } 
/*     */             
/* 161 */             for (StructureTemplate.StructureBlockInfo debug37 : debug33) {
/* 162 */               int debug46, debug51, debug53; if (!JigsawBlock.canAttach(debug15, debug37)) {
/*     */                 continue;
/*     */               }
/*     */               
/* 166 */               BlockPos debug38 = debug37.pos;
/*     */               
/* 168 */               BlockPos debug39 = new BlockPos(debug18.getX() - debug38.getX(), debug18.getY() - debug38.getY(), debug18.getZ() - debug38.getZ());
/* 169 */               BoundingBox debug40 = debug30.getBoundingBox(this.structureManager, debug39, debug32);
/* 170 */               int debug41 = debug40.y0;
/*     */               
/* 172 */               StructureTemplatePool.Projection debug42 = debug30.getProjection();
/* 173 */               boolean debug43 = (debug42 == StructureTemplatePool.Projection.RIGID);
/*     */ 
/*     */               
/* 176 */               int debug44 = debug38.getY();
/*     */               
/* 178 */               int debug45 = debug19 - debug44 + JigsawBlock.getFrontFacing(debug15.state).getStepY();
/*     */ 
/*     */               
/* 181 */               if (debug10 && debug43) {
/* 182 */                 debug46 = debug13 + debug45;
/*     */               } else {
/* 184 */                 if (debug20 == -1) {
/* 185 */                   debug20 = this.chunkGenerator.getFirstFreeHeight(debug17.getX(), debug17.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
/*     */                 }
/* 187 */                 debug46 = debug20 - debug44;
/*     */               } 
/*     */               
/* 190 */               int debug47 = debug46 - debug41;
/*     */               
/* 192 */               BoundingBox debug48 = debug40.moved(0, debug47, 0);
/* 193 */               BlockPos debug49 = debug39.offset(0, debug47, 0);
/*     */               
/* 195 */               if (debug35 > 0) {
/* 196 */                 int i = Math.max(debug35 + 1, debug48.y1 - debug48.y0);
/* 197 */                 debug48.y1 = debug48.y0 + i;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 202 */               if (Shapes.joinIsNotEmpty((VoxelShape)debug25.getValue(), Shapes.create(AABB.of(debug48).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
/*     */                 continue;
/*     */               }
/*     */               
/* 206 */               debug25.setValue(Shapes.joinUnoptimized((VoxelShape)debug25.getValue(), Shapes.create(AABB.of(debug48)), BooleanOp.ONLY_FIRST));
/*     */               
/* 208 */               int debug50 = debug1.getGroundLevelDelta();
/*     */               
/* 210 */               if (debug43) {
/*     */                 
/* 212 */                 debug51 = debug50 - debug45;
/*     */               } else {
/* 214 */                 debug51 = debug30.getGroundLevelDelta();
/*     */               } 
/*     */               
/* 217 */               PoolElementStructurePiece debug52 = this.factory.create(this.structureManager, debug30, debug49, debug51, debug32, debug48);
/*     */ 
/*     */               
/* 220 */               if (debug10) {
/* 221 */                 debug53 = debug13 + debug19;
/* 222 */               } else if (debug43) {
/* 223 */                 debug53 = debug46 + debug44;
/*     */               } else {
/* 225 */                 if (debug20 == -1) {
/* 226 */                   debug20 = this.chunkGenerator.getFirstFreeHeight(debug17.getX(), debug17.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
/*     */                 }
/* 228 */                 debug53 = debug20 + debug45 / 2;
/*     */               } 
/*     */               
/* 231 */               debug1.addJunction(new JigsawJunction(debug18
/* 232 */                     .getX(), debug53 - debug19 + debug50, debug18
/*     */                     
/* 234 */                     .getZ(), debug45, debug42));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 239 */               debug52.addJunction(new JigsawJunction(debug17
/* 240 */                     .getX(), debug53 - debug44 + debug51, debug17
/*     */                     
/* 242 */                     .getZ(), -debug45, debug9));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 247 */               this.pieces.add(debug52);
/* 248 */               if (debug4 + 1 <= this.maxDepth) {
/* 249 */                 this.placing.addLast(new JigsawPlacement.PieceState(debug52, debug25, debug26, debug4 + 1));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addPieces(RegistryAccess debug0, JigsawConfiguration debug1, PieceFactory debug2, ChunkGenerator debug3, StructureManager debug4, BlockPos debug5, List<? super PoolElementStructurePiece> debug6, Random debug7, boolean debug8, boolean debug9) {
/*     */     int debug18;
/* 260 */     StructureFeature.bootstrap();
/*     */     
/* 262 */     WritableRegistry<StructureTemplatePool> debug10 = debug0.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
/*     */     
/* 264 */     Rotation debug11 = Rotation.getRandom(debug7);
/* 265 */     StructureTemplatePool debug12 = debug1.startPool().get();
/* 266 */     StructurePoolElement debug13 = debug12.getRandomTemplate(debug7);
/* 267 */     PoolElementStructurePiece debug14 = debug2.create(debug4, debug13, debug5, debug13.getGroundLevelDelta(), debug11, debug13.getBoundingBox(debug4, debug5, debug11));
/* 268 */     BoundingBox debug15 = debug14.getBoundingBox();
/* 269 */     int debug16 = (debug15.x1 + debug15.x0) / 2;
/* 270 */     int debug17 = (debug15.z1 + debug15.z0) / 2;
/*     */ 
/*     */ 
/*     */     
/* 274 */     if (debug9) {
/* 275 */       debug18 = debug5.getY() + debug3.getFirstFreeHeight(debug16, debug17, Heightmap.Types.WORLD_SURFACE_WG);
/*     */     } else {
/* 277 */       debug18 = debug5.getY();
/*     */     } 
/*     */     
/* 280 */     int debug19 = debug15.y0 + debug14.getGroundLevelDelta();
/* 281 */     debug14.move(0, debug18 - debug19, 0);
/*     */     
/* 283 */     debug6.add(debug14);
/* 284 */     if (debug1.maxDepth() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 288 */     int debug20 = 80;
/*     */     
/* 290 */     AABB debug21 = new AABB((debug16 - 80), (debug18 - 80), (debug17 - 80), (debug16 + 80 + 1), (debug18 + 80 + 1), (debug17 + 80 + 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     Placer debug22 = new Placer((Registry)debug10, debug1.maxDepth(), debug2, debug3, debug4, debug6, debug7);
/*     */     
/* 301 */     debug22.placing.addLast(new PieceState(debug14, new MutableObject(Shapes.join(Shapes.create(debug21), Shapes.create(AABB.of(debug15)), BooleanOp.ONLY_FIRST)), debug18 + 80, 0));
/*     */     
/* 303 */     while (!debug22.placing.isEmpty()) {
/* 304 */       PieceState debug23 = debug22.placing.removeFirst();
/* 305 */       debug22.tryPlacingChildren(debug23.piece, debug23.free, debug23.boundsTop, debug23.depth, debug8);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addPieces(RegistryAccess debug0, PoolElementStructurePiece debug1, int debug2, PieceFactory debug3, ChunkGenerator debug4, StructureManager debug5, List<? super PoolElementStructurePiece> debug6, Random debug7) {
/* 310 */     WritableRegistry<StructureTemplatePool> debug8 = debug0.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
/* 311 */     Placer debug9 = new Placer((Registry)debug8, debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     
/* 313 */     debug9.placing.addLast(new PieceState(debug1, new MutableObject(Shapes.INFINITY), 0, 0));
/*     */     
/* 315 */     while (!debug9.placing.isEmpty()) {
/* 316 */       PieceState debug10 = debug9.placing.removeFirst();
/* 317 */       debug9.tryPlacingChildren(debug10.piece, debug10.free, debug10.boundsTop, debug10.depth, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface PieceFactory {
/*     */     PoolElementStructurePiece create(StructureManager param1StructureManager, StructurePoolElement param1StructurePoolElement, BlockPos param1BlockPos, int param1Int, Rotation param1Rotation, BoundingBox param1BoundingBox);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\JigsawPlacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */