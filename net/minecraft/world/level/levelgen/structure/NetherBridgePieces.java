/*      */ package net.minecraft.world.level.levelgen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureFeatureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NetherBridgePieces
/*      */ {
/*      */   static class PieceWeight
/*      */   {
/*      */     public final Class<? extends NetherBridgePieces.NetherBridgePiece> pieceClass;
/*      */     public final int weight;
/*      */     public int placeCount;
/*      */     public final int maxPlaceCount;
/*      */     public final boolean allowInRow;
/*      */     
/*      */     public PieceWeight(Class<? extends NetherBridgePieces.NetherBridgePiece> debug1, int debug2, int debug3, boolean debug4) {
/*   42 */       this.pieceClass = debug1;
/*   43 */       this.weight = debug2;
/*   44 */       this.maxPlaceCount = debug3;
/*   45 */       this.allowInRow = debug4;
/*      */     }
/*      */     
/*      */     public PieceWeight(Class<? extends NetherBridgePieces.NetherBridgePiece> debug1, int debug2, int debug3) {
/*   49 */       this(debug1, debug2, debug3, false);
/*      */     }
/*      */     
/*      */     public boolean doPlace(int debug1) {
/*   53 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/*   57 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */   }
/*      */   
/*   61 */   private static final PieceWeight[] BRIDGE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)BridgeStraight.class, 30, 0, true), new PieceWeight((Class)BridgeCrossing.class, 10, 4), new PieceWeight((Class)RoomCrossing.class, 10, 4), new PieceWeight((Class)StairsRoom.class, 10, 3), new PieceWeight((Class)MonsterThrone.class, 5, 2), new PieceWeight((Class)CastleEntrance.class, 5, 1) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   69 */   private static final PieceWeight[] CASTLE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)CastleSmallCorridorPiece.class, 25, 0, true), new PieceWeight((Class)CastleSmallCorridorCrossingPiece.class, 15, 5), new PieceWeight((Class)CastleSmallCorridorRightTurnPiece.class, 5, 10), new PieceWeight((Class)CastleSmallCorridorLeftTurnPiece.class, 5, 10), new PieceWeight((Class)CastleCorridorStairsPiece.class, 10, 3, true), new PieceWeight((Class)CastleCorridorTBalconyPiece.class, 7, 2), new PieceWeight((Class)CastleStalkRoom.class, 5, 2) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static NetherBridgePiece findAndCreateBridgePieceFactory(PieceWeight debug0, List<StructurePiece> debug1, Random debug2, int debug3, int debug4, int debug5, Direction debug6, int debug7) {
/*   80 */     Class<? extends NetherBridgePiece> debug8 = debug0.pieceClass;
/*   81 */     NetherBridgePiece debug9 = null;
/*      */     
/*   83 */     if (debug8 == BridgeStraight.class) {
/*   84 */       debug9 = BridgeStraight.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*   85 */     } else if (debug8 == BridgeCrossing.class) {
/*   86 */       debug9 = BridgeCrossing.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*   87 */     } else if (debug8 == RoomCrossing.class) {
/*   88 */       debug9 = RoomCrossing.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*   89 */     } else if (debug8 == StairsRoom.class) {
/*   90 */       debug9 = StairsRoom.createPiece(debug1, debug3, debug4, debug5, debug7, debug6);
/*   91 */     } else if (debug8 == MonsterThrone.class) {
/*   92 */       debug9 = MonsterThrone.createPiece(debug1, debug3, debug4, debug5, debug7, debug6);
/*   93 */     } else if (debug8 == CastleEntrance.class) {
/*   94 */       debug9 = CastleEntrance.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*   95 */     } else if (debug8 == CastleSmallCorridorPiece.class) {
/*   96 */       debug9 = CastleSmallCorridorPiece.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*   97 */     } else if (debug8 == CastleSmallCorridorRightTurnPiece.class) {
/*   98 */       debug9 = CastleSmallCorridorRightTurnPiece.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*   99 */     } else if (debug8 == CastleSmallCorridorLeftTurnPiece.class) {
/*  100 */       debug9 = CastleSmallCorridorLeftTurnPiece.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  101 */     } else if (debug8 == CastleCorridorStairsPiece.class) {
/*  102 */       debug9 = CastleCorridorStairsPiece.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*  103 */     } else if (debug8 == CastleCorridorTBalconyPiece.class) {
/*  104 */       debug9 = CastleCorridorTBalconyPiece.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*  105 */     } else if (debug8 == CastleSmallCorridorCrossingPiece.class) {
/*  106 */       debug9 = CastleSmallCorridorCrossingPiece.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*  107 */     } else if (debug8 == CastleStalkRoom.class) {
/*  108 */       debug9 = CastleStalkRoom.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*      */     } 
/*  110 */     return debug9;
/*      */   }
/*      */   
/*      */   static abstract class NetherBridgePiece extends StructurePiece {
/*      */     protected NetherBridgePiece(StructurePieceType debug1, int debug2) {
/*  115 */       super(debug1, debug2);
/*      */     }
/*      */     
/*      */     public NetherBridgePiece(StructurePieceType debug1, CompoundTag debug2) {
/*  119 */       super(debug1, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {}
/*      */ 
/*      */     
/*      */     private int updatePieceWeight(List<NetherBridgePieces.PieceWeight> debug1) {
/*  127 */       boolean debug2 = false;
/*  128 */       int debug3 = 0;
/*  129 */       for (NetherBridgePieces.PieceWeight debug5 : debug1) {
/*  130 */         if (debug5.maxPlaceCount > 0 && debug5.placeCount < debug5.maxPlaceCount) {
/*  131 */           debug2 = true;
/*      */         }
/*  133 */         debug3 += debug5.weight;
/*      */       } 
/*  135 */       return debug2 ? debug3 : -1;
/*      */     }
/*      */     
/*      */     private NetherBridgePiece generatePiece(NetherBridgePieces.StartPiece debug1, List<NetherBridgePieces.PieceWeight> debug2, List<StructurePiece> debug3, Random debug4, int debug5, int debug6, int debug7, Direction debug8, int debug9) {
/*  139 */       int debug10 = updatePieceWeight(debug2);
/*  140 */       boolean debug11 = (debug10 > 0 && debug9 <= 30);
/*      */       
/*  142 */       int debug12 = 0;
/*  143 */       while (debug12 < 5 && debug11) {
/*  144 */         debug12++;
/*      */         
/*  146 */         int debug13 = debug4.nextInt(debug10);
/*  147 */         for (NetherBridgePieces.PieceWeight debug15 : debug2) {
/*  148 */           debug13 -= debug15.weight;
/*  149 */           if (debug13 < 0) {
/*  150 */             if (!debug15.doPlace(debug9) || (debug15 == debug1.previousPiece && !debug15.allowInRow)) {
/*      */               break;
/*      */             }
/*      */             
/*  154 */             NetherBridgePiece debug16 = NetherBridgePieces.findAndCreateBridgePieceFactory(debug15, debug3, debug4, debug5, debug6, debug7, debug8, debug9);
/*  155 */             if (debug16 != null) {
/*  156 */               debug15.placeCount++;
/*  157 */               debug1.previousPiece = debug15;
/*      */               
/*  159 */               if (!debug15.isValid()) {
/*  160 */                 debug2.remove(debug15);
/*      */               }
/*  162 */               return debug16;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  167 */       return NetherBridgePieces.BridgeEndFiller.createPiece(debug3, debug4, debug5, debug6, debug7, debug8, debug9);
/*      */     }
/*      */     
/*      */     private StructurePiece generateAndAddPiece(NetherBridgePieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5, int debug6, @Nullable Direction debug7, int debug8, boolean debug9) {
/*  171 */       if (Math.abs(debug4 - (debug1.getBoundingBox()).x0) > 112 || Math.abs(debug6 - (debug1.getBoundingBox()).z0) > 112) {
/*  172 */         return NetherBridgePieces.BridgeEndFiller.createPiece(debug2, debug3, debug4, debug5, debug6, debug7, debug8);
/*      */       }
/*  174 */       List<NetherBridgePieces.PieceWeight> debug10 = debug1.availableBridgePieces;
/*  175 */       if (debug9) {
/*  176 */         debug10 = debug1.availableCastlePieces;
/*      */       }
/*  178 */       StructurePiece debug11 = generatePiece(debug1, debug10, debug2, debug3, debug4, debug5, debug6, debug7, debug8 + 1);
/*  179 */       if (debug11 != null) {
/*  180 */         debug2.add(debug11);
/*  181 */         debug1.pendingChildren.add(debug11);
/*      */       } 
/*  183 */       return debug11;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateChildForward(NetherBridgePieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5, boolean debug6) {
/*  188 */       Direction debug7 = getOrientation();
/*  189 */       if (debug7 != null) {
/*  190 */         switch (debug7) {
/*      */           case NORTH:
/*  192 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug4, this.boundingBox.y0 + debug5, this.boundingBox.z0 - 1, debug7, getGenDepth(), debug6);
/*      */           case SOUTH:
/*  194 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug4, this.boundingBox.y0 + debug5, this.boundingBox.z1 + 1, debug7, getGenDepth(), debug6);
/*      */           case WEST:
/*  196 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug5, this.boundingBox.z0 + debug4, debug7, getGenDepth(), debug6);
/*      */           case EAST:
/*  198 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug5, this.boundingBox.z0 + debug4, debug7, getGenDepth(), debug6);
/*      */         } 
/*      */       }
/*  201 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateChildLeft(NetherBridgePieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5, boolean debug6) {
/*  206 */       Direction debug7 = getOrientation();
/*  207 */       if (debug7 != null) {
/*  208 */         switch (debug7) {
/*      */           case NORTH:
/*  210 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.WEST, getGenDepth(), debug6);
/*      */           case SOUTH:
/*  212 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.WEST, getGenDepth(), debug6);
/*      */           case WEST:
/*  214 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z0 - 1, Direction.NORTH, getGenDepth(), debug6);
/*      */           case EAST:
/*  216 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z0 - 1, Direction.NORTH, getGenDepth(), debug6);
/*      */         } 
/*      */       }
/*  219 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateChildRight(NetherBridgePieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5, boolean debug6) {
/*  224 */       Direction debug7 = getOrientation();
/*  225 */       if (debug7 != null) {
/*  226 */         switch (debug7) {
/*      */           case NORTH:
/*  228 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.EAST, getGenDepth(), debug6);
/*      */           case SOUTH:
/*  230 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.EAST, getGenDepth(), debug6);
/*      */           case WEST:
/*  232 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z1 + 1, Direction.SOUTH, getGenDepth(), debug6);
/*      */           case EAST:
/*  234 */             return generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z1 + 1, Direction.SOUTH, getGenDepth(), debug6);
/*      */         } 
/*      */       }
/*  237 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean isOkBox(BoundingBox debug0) {
/*  241 */       return (debug0 != null && debug0.y0 > 10);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class StartPiece
/*      */     extends BridgeCrossing
/*      */   {
/*      */     public NetherBridgePieces.PieceWeight previousPiece;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<NetherBridgePieces.PieceWeight> availableBridgePieces;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<NetherBridgePieces.PieceWeight> availableCastlePieces;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  273 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(Random debug1, int debug2, int debug3) {
/*  276 */       super(debug1, debug2, debug3);
/*      */       
/*  278 */       this.availableBridgePieces = Lists.newArrayList();
/*  279 */       for (NetherBridgePieces.PieceWeight debug7 : NetherBridgePieces.BRIDGE_PIECE_WEIGHTS) {
/*  280 */         debug7.placeCount = 0;
/*  281 */         this.availableBridgePieces.add(debug7);
/*      */       } 
/*      */       
/*  284 */       this.availableCastlePieces = Lists.newArrayList();
/*  285 */       for (NetherBridgePieces.PieceWeight debug7 : NetherBridgePieces.CASTLE_PIECE_WEIGHTS) {
/*  286 */         debug7.placeCount = 0;
/*  287 */         this.availableCastlePieces.add(debug7);
/*      */       } 
/*      */     }
/*      */     
/*      */     public StartPiece(StructureManager debug1, CompoundTag debug2) {
/*  292 */       super(StructurePieceType.NETHER_FORTRESS_START, debug2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BridgeStraight
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public BridgeStraight(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  302 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, debug1);
/*      */       
/*  304 */       setOrientation(debug4);
/*  305 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public BridgeStraight(StructureManager debug1, CompoundTag debug2) {
/*  309 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  314 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 1, 3, false);
/*      */     }
/*      */     
/*      */     public static BridgeStraight createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  318 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -3, 0, 5, 10, 19, debug5);
/*      */       
/*  320 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  321 */         return null;
/*      */       }
/*      */       
/*  324 */       return new BridgeStraight(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  330 */       generateBox(debug1, debug5, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  332 */       generateBox(debug1, debug5, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  335 */       generateBox(debug1, debug5, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  336 */       generateBox(debug1, debug5, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  339 */       generateBox(debug1, debug5, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  340 */       generateBox(debug1, debug5, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  341 */       generateBox(debug1, debug5, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  342 */       generateBox(debug1, debug5, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  344 */       for (int i = 0; i <= 4; i++) {
/*  345 */         for (int j = 0; j <= 2; j++) {
/*  346 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, -1, j, debug5);
/*  347 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, -1, 18 - j, debug5);
/*      */         } 
/*      */       } 
/*      */       
/*  351 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  352 */       BlockState debug9 = (BlockState)debug8.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  353 */       BlockState debug10 = (BlockState)debug8.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  354 */       generateBox(debug1, debug5, 0, 1, 1, 0, 4, 1, debug9, debug9, false);
/*  355 */       generateBox(debug1, debug5, 0, 3, 4, 0, 4, 4, debug9, debug9, false);
/*  356 */       generateBox(debug1, debug5, 0, 3, 14, 0, 4, 14, debug9, debug9, false);
/*  357 */       generateBox(debug1, debug5, 0, 1, 17, 0, 4, 17, debug9, debug9, false);
/*  358 */       generateBox(debug1, debug5, 4, 1, 1, 4, 4, 1, debug10, debug10, false);
/*  359 */       generateBox(debug1, debug5, 4, 3, 4, 4, 4, 4, debug10, debug10, false);
/*  360 */       generateBox(debug1, debug5, 4, 3, 14, 4, 4, 14, debug10, debug10, false);
/*  361 */       generateBox(debug1, debug5, 4, 1, 17, 4, 4, 17, debug10, debug10, false);
/*      */       
/*  363 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class BridgeEndFiller
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private final int selfSeed;
/*      */ 
/*      */     
/*      */     public BridgeEndFiller(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  375 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, debug1);
/*      */       
/*  377 */       setOrientation(debug4);
/*  378 */       this.boundingBox = debug3;
/*  379 */       this.selfSeed = debug2.nextInt();
/*      */     }
/*      */     
/*      */     public BridgeEndFiller(StructureManager debug1, CompoundTag debug2) {
/*  383 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, debug2);
/*  384 */       this.selfSeed = debug2.getInt("Seed");
/*      */     }
/*      */     
/*      */     public static BridgeEndFiller createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  388 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -3, 0, 5, 10, 8, debug5);
/*      */       
/*  390 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  391 */         return null;
/*      */       }
/*      */       
/*  394 */       return new BridgeEndFiller(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  399 */       super.addAdditionalSaveData(debug1);
/*      */       
/*  401 */       debug1.putInt("Seed", this.selfSeed);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  406 */       Random debug8 = new Random(this.selfSeed);
/*      */       
/*      */       int debug9;
/*  409 */       for (debug9 = 0; debug9 <= 4; debug9++) {
/*  410 */         for (int debug10 = 3; debug10 <= 4; debug10++) {
/*  411 */           int debug11 = debug8.nextInt(8);
/*  412 */           generateBox(debug1, debug5, debug9, debug10, 0, debug9, debug10, debug11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  418 */       debug9 = debug8.nextInt(8);
/*  419 */       generateBox(debug1, debug5, 0, 5, 0, 0, 5, debug9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  422 */       debug9 = debug8.nextInt(8);
/*  423 */       generateBox(debug1, debug5, 4, 5, 0, 4, 5, debug9, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */ 
/*      */       
/*  427 */       for (debug9 = 0; debug9 <= 4; debug9++) {
/*  428 */         int debug10 = debug8.nextInt(5);
/*  429 */         generateBox(debug1, debug5, debug9, 2, 0, debug9, 2, debug10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  431 */       for (debug9 = 0; debug9 <= 4; debug9++) {
/*  432 */         for (int debug10 = 0; debug10 <= 1; debug10++) {
/*  433 */           int debug11 = debug8.nextInt(3);
/*  434 */           generateBox(debug1, debug5, debug9, debug10, 0, debug9, debug10, debug11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */       
/*  438 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BridgeCrossing
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public BridgeCrossing(int debug1, BoundingBox debug2, Direction debug3) {
/*  448 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, debug1);
/*      */       
/*  450 */       setOrientation(debug3);
/*  451 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(Random debug1, int debug2, int debug3) {
/*  455 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0);
/*      */       
/*  457 */       setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(debug1));
/*      */       
/*  459 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/*  460 */         this.boundingBox = new BoundingBox(debug2, 64, debug3, debug2 + 19 - 1, 73, debug3 + 19 - 1);
/*      */       } else {
/*  462 */         this.boundingBox = new BoundingBox(debug2, 64, debug3, debug2 + 19 - 1, 73, debug3 + 19 - 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(StructurePieceType debug1, CompoundTag debug2) {
/*  467 */       super(debug1, debug2);
/*      */     }
/*      */     
/*      */     public BridgeCrossing(StructureManager debug1, CompoundTag debug2) {
/*  471 */       this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  476 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 8, 3, false);
/*  477 */       generateChildLeft((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 3, 8, false);
/*  478 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 3, 8, false);
/*      */     }
/*      */     
/*      */     public static BridgeCrossing createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/*  482 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -8, -3, 0, 19, 10, 19, debug4);
/*      */       
/*  484 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/*  485 */         return null;
/*      */       }
/*      */       
/*  488 */       return new BridgeCrossing(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  494 */       generateBox(debug1, debug5, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  495 */       generateBox(debug1, debug5, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  497 */       generateBox(debug1, debug5, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  498 */       generateBox(debug1, debug5, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/*  500 */       generateBox(debug1, debug5, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  501 */       generateBox(debug1, debug5, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  502 */       generateBox(debug1, debug5, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  503 */       generateBox(debug1, debug5, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  504 */       generateBox(debug1, debug5, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  505 */       generateBox(debug1, debug5, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  506 */       generateBox(debug1, debug5, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  507 */       generateBox(debug1, debug5, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  510 */       generateBox(debug1, debug5, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  511 */       generateBox(debug1, debug5, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  512 */       generateBox(debug1, debug5, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  513 */       generateBox(debug1, debug5, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false); int debug8;
/*  514 */       for (debug8 = 7; debug8 <= 11; debug8++) {
/*  515 */         for (int debug9 = 0; debug9 <= 2; debug9++) {
/*  516 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug8, -1, debug9, debug5);
/*  517 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug8, -1, 18 - debug9, debug5);
/*      */         } 
/*      */       } 
/*      */       
/*  521 */       generateBox(debug1, debug5, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  522 */       generateBox(debug1, debug5, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  523 */       generateBox(debug1, debug5, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  524 */       generateBox(debug1, debug5, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  525 */       for (debug8 = 0; debug8 <= 2; debug8++) {
/*  526 */         for (int debug9 = 7; debug9 <= 11; debug9++) {
/*  527 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug8, -1, debug9, debug5);
/*  528 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - debug8, -1, debug9, debug5);
/*      */         } 
/*      */       } 
/*      */       
/*  532 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RoomCrossing
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public RoomCrossing(int debug1, BoundingBox debug2, Direction debug3) {
/*  542 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, debug1);
/*      */       
/*  544 */       setOrientation(debug3);
/*  545 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public RoomCrossing(StructureManager debug1, CompoundTag debug2) {
/*  549 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  554 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 2, 0, false);
/*  555 */       generateChildLeft((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 2, false);
/*  556 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 2, false);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/*  560 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -2, 0, 0, 7, 9, 7, debug4);
/*      */       
/*  562 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/*  563 */         return null;
/*      */       }
/*      */       
/*  566 */       return new RoomCrossing(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  572 */       generateBox(debug1, debug5, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  574 */       generateBox(debug1, debug5, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  577 */       generateBox(debug1, debug5, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  578 */       generateBox(debug1, debug5, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  579 */       generateBox(debug1, debug5, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  580 */       generateBox(debug1, debug5, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  581 */       generateBox(debug1, debug5, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  582 */       generateBox(debug1, debug5, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  583 */       generateBox(debug1, debug5, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  584 */       generateBox(debug1, debug5, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  587 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  588 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  590 */       generateBox(debug1, debug5, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  591 */       generateBox(debug1, debug5, 2, 5, 0, 4, 5, 0, debug8, debug8, false);
/*  592 */       generateBox(debug1, debug5, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  593 */       generateBox(debug1, debug5, 2, 5, 6, 4, 5, 6, debug8, debug8, false);
/*  594 */       generateBox(debug1, debug5, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  595 */       generateBox(debug1, debug5, 0, 5, 2, 0, 5, 4, debug9, debug9, false);
/*  596 */       generateBox(debug1, debug5, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  597 */       generateBox(debug1, debug5, 6, 5, 2, 6, 5, 4, debug9, debug9, false);
/*      */ 
/*      */       
/*  600 */       for (int debug10 = 0; debug10 <= 6; debug10++) {
/*  601 */         for (int debug11 = 0; debug11 <= 6; debug11++) {
/*  602 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug10, -1, debug11, debug5);
/*      */         }
/*      */       } 
/*      */       
/*  606 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class StairsRoom
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public StairsRoom(int debug1, BoundingBox debug2, Direction debug3) {
/*  616 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, debug1);
/*      */       
/*  618 */       setOrientation(debug3);
/*  619 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public StairsRoom(StructureManager debug1, CompoundTag debug2) {
/*  623 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  628 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 6, 2, false);
/*      */     }
/*      */     
/*      */     public static StairsRoom createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, int debug4, Direction debug5) {
/*  632 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -2, 0, 0, 7, 11, 7, debug5);
/*      */       
/*  634 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/*  635 */         return null;
/*      */       }
/*      */       
/*  638 */       return new StairsRoom(debug4, debug6, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  644 */       generateBox(debug1, debug5, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  646 */       generateBox(debug1, debug5, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  649 */       generateBox(debug1, debug5, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  650 */       generateBox(debug1, debug5, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  651 */       generateBox(debug1, debug5, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  652 */       generateBox(debug1, debug5, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  653 */       generateBox(debug1, debug5, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  656 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  657 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  659 */       generateBox(debug1, debug5, 0, 3, 2, 0, 5, 4, debug9, debug9, false);
/*  660 */       generateBox(debug1, debug5, 6, 3, 2, 6, 5, 2, debug9, debug9, false);
/*  661 */       generateBox(debug1, debug5, 6, 3, 4, 6, 5, 4, debug9, debug9, false);
/*      */ 
/*      */       
/*  664 */       placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, debug5);
/*  665 */       generateBox(debug1, debug5, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  666 */       generateBox(debug1, debug5, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  667 */       generateBox(debug1, debug5, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  668 */       generateBox(debug1, debug5, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  671 */       generateBox(debug1, debug5, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  672 */       generateBox(debug1, debug5, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  675 */       generateBox(debug1, debug5, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  676 */       generateBox(debug1, debug5, 2, 5, 0, 4, 5, 0, debug8, debug8, false);
/*      */       
/*  678 */       for (int debug10 = 0; debug10 <= 6; debug10++) {
/*  679 */         for (int debug11 = 0; debug11 <= 6; debug11++) {
/*  680 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug10, -1, debug11, debug5);
/*      */         }
/*      */       } 
/*      */       
/*  684 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class MonsterThrone
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private boolean hasPlacedSpawner;
/*      */ 
/*      */     
/*      */     public MonsterThrone(int debug1, BoundingBox debug2, Direction debug3) {
/*  696 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, debug1);
/*      */       
/*  698 */       setOrientation(debug3);
/*  699 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public MonsterThrone(StructureManager debug1, CompoundTag debug2) {
/*  703 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, debug2);
/*  704 */       this.hasPlacedSpawner = debug2.getBoolean("Mob");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  709 */       super.addAdditionalSaveData(debug1);
/*      */       
/*  711 */       debug1.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */     
/*      */     public static MonsterThrone createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, int debug4, Direction debug5) {
/*  715 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -2, 0, 0, 7, 8, 9, debug5);
/*      */       
/*  717 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/*  718 */         return null;
/*      */       }
/*      */       
/*  721 */       return new MonsterThrone(debug4, debug6, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  727 */       generateBox(debug1, debug5, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  730 */       generateBox(debug1, debug5, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  731 */       generateBox(debug1, debug5, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  732 */       generateBox(debug1, debug5, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  733 */       generateBox(debug1, debug5, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  736 */       generateBox(debug1, debug5, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  737 */       generateBox(debug1, debug5, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  738 */       generateBox(debug1, debug5, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  739 */       generateBox(debug1, debug5, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  740 */       generateBox(debug1, debug5, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  741 */       generateBox(debug1, debug5, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  742 */       generateBox(debug1, debug5, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  744 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  745 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  747 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 1, 6, 3, debug5);
/*  748 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 5, 6, 3, debug5);
/*      */       
/*  750 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 0, 6, 3, debug5);
/*  751 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 6, 3, debug5);
/*      */       
/*  753 */       generateBox(debug1, debug5, 0, 6, 4, 0, 6, 7, debug9, debug9, false);
/*  754 */       generateBox(debug1, debug5, 6, 6, 4, 6, 6, 7, debug9, debug9, false);
/*      */       
/*  756 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 6, 8, debug5);
/*  757 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 6, 8, debug5);
/*      */       
/*  759 */       generateBox(debug1, debug5, 1, 6, 8, 5, 6, 8, debug8, debug8, false);
/*      */       
/*  761 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 1, 7, 8, debug5);
/*  762 */       generateBox(debug1, debug5, 2, 7, 8, 4, 7, 8, debug8, debug8, false);
/*  763 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 5, 7, 8, debug5);
/*      */       
/*  765 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 2, 8, 8, debug5);
/*  766 */       placeBlock(debug1, debug8, 3, 8, 8, debug5);
/*  767 */       placeBlock(debug1, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 4, 8, 8, debug5);
/*      */       
/*  769 */       if (!this.hasPlacedSpawner) {
/*  770 */         BlockPos blockPos = new BlockPos(getWorldX(3, 5), getWorldY(5), getWorldZ(3, 5));
/*  771 */         if (debug5.isInside((Vec3i)blockPos)) {
/*  772 */           this.hasPlacedSpawner = true;
/*  773 */           debug1.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*      */           
/*  775 */           BlockEntity debug11 = debug1.getBlockEntity(blockPos);
/*  776 */           if (debug11 instanceof SpawnerBlockEntity) {
/*  777 */             ((SpawnerBlockEntity)debug11).getSpawner().setEntityId(EntityType.BLAZE);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  783 */       for (int debug10 = 0; debug10 <= 6; debug10++) {
/*  784 */         for (int debug11 = 0; debug11 <= 6; debug11++) {
/*  785 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug10, -1, debug11, debug5);
/*      */         }
/*      */       } 
/*      */       
/*  789 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleEntrance
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleEntrance(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  799 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, debug1);
/*      */       
/*  801 */       setOrientation(debug4);
/*  802 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public CastleEntrance(StructureManager debug1, CompoundTag debug2) {
/*  806 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  811 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 5, 3, true);
/*      */     }
/*      */     
/*      */     public static CastleEntrance createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  815 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -5, -3, 0, 13, 14, 13, debug5);
/*      */       
/*  817 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  818 */         return null;
/*      */       }
/*      */       
/*  821 */       return new CastleEntrance(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  827 */       generateBox(debug1, debug5, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  829 */       generateBox(debug1, debug5, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  832 */       generateBox(debug1, debug5, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  833 */       generateBox(debug1, debug5, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  834 */       generateBox(debug1, debug5, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  835 */       generateBox(debug1, debug5, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  836 */       generateBox(debug1, debug5, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  837 */       generateBox(debug1, debug5, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  838 */       generateBox(debug1, debug5, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  839 */       generateBox(debug1, debug5, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  842 */       generateBox(debug1, debug5, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  845 */       generateBox(debug1, debug5, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
/*      */       
/*  847 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  848 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*      */       int i;
/*  851 */       for (i = 1; i <= 11; i += 2) {
/*  852 */         generateBox(debug1, debug5, i, 10, 0, i, 11, 0, debug8, debug8, false);
/*  853 */         generateBox(debug1, debug5, i, 10, 12, i, 11, 12, debug8, debug8, false);
/*  854 */         generateBox(debug1, debug5, 0, 10, i, 0, 11, i, debug9, debug9, false);
/*  855 */         generateBox(debug1, debug5, 12, 10, i, 12, 11, i, debug9, debug9, false);
/*  856 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, debug5);
/*  857 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, debug5);
/*  858 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, debug5);
/*  859 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, debug5);
/*  860 */         if (i != 11) {
/*  861 */           placeBlock(debug1, debug8, i + 1, 13, 0, debug5);
/*  862 */           placeBlock(debug1, debug8, i + 1, 13, 12, debug5);
/*  863 */           placeBlock(debug1, debug9, 0, 13, i + 1, debug5);
/*  864 */           placeBlock(debug1, debug9, 12, 13, i + 1, debug5);
/*      */         } 
/*      */       } 
/*  867 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, debug5);
/*  868 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, debug5);
/*  869 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, debug5);
/*  870 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, debug5);
/*      */ 
/*      */       
/*  873 */       for (i = 3; i <= 9; i += 2) {
/*  874 */         generateBox(debug1, debug5, 1, 7, i, 1, 8, i, (BlockState)debug9.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), (BlockState)debug9.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), false);
/*  875 */         generateBox(debug1, debug5, 11, 7, i, 11, 8, i, (BlockState)debug9.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), (BlockState)debug9.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), false);
/*      */       } 
/*      */ 
/*      */       
/*  879 */       generateBox(debug1, debug5, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  880 */       generateBox(debug1, debug5, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  882 */       generateBox(debug1, debug5, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  883 */       generateBox(debug1, debug5, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  884 */       generateBox(debug1, debug5, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  885 */       generateBox(debug1, debug5, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  887 */       for (i = 4; i <= 8; i++) {
/*  888 */         for (int debug11 = 0; debug11 <= 2; debug11++) {
/*  889 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, -1, debug11, debug5);
/*  890 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, -1, 12 - debug11, debug5);
/*      */         } 
/*      */       } 
/*  893 */       for (i = 0; i <= 2; i++) {
/*  894 */         for (int debug11 = 4; debug11 <= 8; debug11++) {
/*  895 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, -1, debug11, debug5);
/*  896 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - i, -1, debug11, debug5);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  901 */       generateBox(debug1, debug5, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  902 */       generateBox(debug1, debug5, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  903 */       placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, debug5);
/*  904 */       placeBlock(debug1, Blocks.LAVA.defaultBlockState(), 6, 5, 6, debug5);
/*      */       
/*  906 */       BlockPos debug10 = new BlockPos(getWorldX(6, 6), getWorldY(5), getWorldZ(6, 6));
/*  907 */       if (debug5.isInside((Vec3i)debug10)) {
/*  908 */         debug1.getLiquidTicks().scheduleTick(debug10, Fluids.LAVA, 0);
/*      */       }
/*      */       
/*  911 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleStalkRoom
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleStalkRoom(int debug1, BoundingBox debug2, Direction debug3) {
/*  921 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, debug1);
/*      */       
/*  923 */       setOrientation(debug3);
/*  924 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public CastleStalkRoom(StructureManager debug1, CompoundTag debug2) {
/*  928 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  933 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 5, 3, true);
/*  934 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 5, 11, true);
/*      */     }
/*      */     
/*      */     public static CastleStalkRoom createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/*  938 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -5, -3, 0, 13, 14, 13, debug4);
/*      */       
/*  940 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/*  941 */         return null;
/*      */       }
/*      */       
/*  944 */       return new CastleStalkRoom(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  950 */       generateBox(debug1, debug5, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  952 */       generateBox(debug1, debug5, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  955 */       generateBox(debug1, debug5, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  956 */       generateBox(debug1, debug5, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  957 */       generateBox(debug1, debug5, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  958 */       generateBox(debug1, debug5, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  959 */       generateBox(debug1, debug5, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  960 */       generateBox(debug1, debug5, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  961 */       generateBox(debug1, debug5, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  962 */       generateBox(debug1, debug5, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  965 */       generateBox(debug1, debug5, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  967 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  968 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  969 */       BlockState debug10 = (BlockState)debug9.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  970 */       BlockState debug11 = (BlockState)debug9.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */       
/*      */       int i;
/*  973 */       for (i = 1; i <= 11; i += 2) {
/*  974 */         generateBox(debug1, debug5, i, 10, 0, i, 11, 0, debug8, debug8, false);
/*  975 */         generateBox(debug1, debug5, i, 10, 12, i, 11, 12, debug8, debug8, false);
/*  976 */         generateBox(debug1, debug5, 0, 10, i, 0, 11, i, debug9, debug9, false);
/*  977 */         generateBox(debug1, debug5, 12, 10, i, 12, 11, i, debug9, debug9, false);
/*  978 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, debug5);
/*  979 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, debug5);
/*  980 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, debug5);
/*  981 */         placeBlock(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, debug5);
/*  982 */         if (i != 11) {
/*  983 */           placeBlock(debug1, debug8, i + 1, 13, 0, debug5);
/*  984 */           placeBlock(debug1, debug8, i + 1, 13, 12, debug5);
/*  985 */           placeBlock(debug1, debug9, 0, 13, i + 1, debug5);
/*  986 */           placeBlock(debug1, debug9, 12, 13, i + 1, debug5);
/*      */         } 
/*      */       } 
/*  989 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, debug5);
/*  990 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, debug5);
/*  991 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, debug5);
/*  992 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, debug5);
/*      */ 
/*      */       
/*  995 */       for (i = 3; i <= 9; i += 2) {
/*  996 */         generateBox(debug1, debug5, 1, 7, i, 1, 8, i, debug10, debug10, false);
/*  997 */         generateBox(debug1, debug5, 11, 7, i, 11, 8, i, debug11, debug11, false);
/*      */       } 
/*      */ 
/*      */       
/* 1001 */       BlockState debug12 = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH); int j;
/* 1002 */       for (j = 0; j <= 6; j++) {
/* 1003 */         int k = j + 4;
/* 1004 */         for (int m = 5; m <= 7; m++) {
/* 1005 */           placeBlock(debug1, debug12, m, 5 + j, k, debug5);
/*      */         }
/* 1007 */         if (k >= 5 && k <= 8) {
/* 1008 */           generateBox(debug1, debug5, 5, 5, k, 7, j + 4, k, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1009 */         } else if (k >= 9 && k <= 10) {
/* 1010 */           generateBox(debug1, debug5, 5, 8, k, 7, j + 4, k, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/* 1012 */         if (j >= 1) {
/* 1013 */           generateBox(debug1, debug5, 5, 6 + j, k, 7, 9 + j, k, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */         }
/*      */       } 
/* 1016 */       for (j = 5; j <= 7; j++) {
/* 1017 */         placeBlock(debug1, debug12, j, 12, 11, debug5);
/*      */       }
/* 1019 */       generateBox(debug1, debug5, 5, 6, 7, 5, 7, 7, debug11, debug11, false);
/* 1020 */       generateBox(debug1, debug5, 7, 6, 7, 7, 7, 7, debug10, debug10, false);
/* 1021 */       generateBox(debug1, debug5, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1024 */       generateBox(debug1, debug5, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1025 */       generateBox(debug1, debug5, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1026 */       generateBox(debug1, debug5, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1027 */       generateBox(debug1, debug5, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1028 */       generateBox(debug1, debug5, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1029 */       generateBox(debug1, debug5, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1030 */       BlockState debug13 = (BlockState)debug12.setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/* 1031 */       BlockState debug14 = (BlockState)debug12.setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/* 1032 */       placeBlock(debug1, debug14, 4, 5, 2, debug5);
/* 1033 */       placeBlock(debug1, debug14, 4, 5, 3, debug5);
/* 1034 */       placeBlock(debug1, debug14, 4, 5, 9, debug5);
/* 1035 */       placeBlock(debug1, debug14, 4, 5, 10, debug5);
/* 1036 */       placeBlock(debug1, debug13, 8, 5, 2, debug5);
/* 1037 */       placeBlock(debug1, debug13, 8, 5, 3, debug5);
/* 1038 */       placeBlock(debug1, debug13, 8, 5, 9, debug5);
/* 1039 */       placeBlock(debug1, debug13, 8, 5, 10, debug5);
/*      */ 
/*      */       
/* 1042 */       generateBox(debug1, debug5, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1043 */       generateBox(debug1, debug5, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1044 */       generateBox(debug1, debug5, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/* 1045 */       generateBox(debug1, debug5, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1048 */       generateBox(debug1, debug5, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1049 */       generateBox(debug1, debug5, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1051 */       generateBox(debug1, debug5, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1052 */       generateBox(debug1, debug5, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1053 */       generateBox(debug1, debug5, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1054 */       generateBox(debug1, debug5, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       int debug15;
/* 1056 */       for (debug15 = 4; debug15 <= 8; debug15++) {
/* 1057 */         for (int debug16 = 0; debug16 <= 2; debug16++) {
/* 1058 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug15, -1, debug16, debug5);
/* 1059 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug15, -1, 12 - debug16, debug5);
/*      */         } 
/*      */       } 
/* 1062 */       for (debug15 = 0; debug15 <= 2; debug15++) {
/* 1063 */         for (int debug16 = 4; debug16 <= 8; debug16++) {
/* 1064 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug15, -1, debug16, debug5);
/* 1065 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - debug15, -1, debug16, debug5);
/*      */         } 
/*      */       } 
/*      */       
/* 1069 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleSmallCorridorPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleSmallCorridorPiece(int debug1, BoundingBox debug2, Direction debug3) {
/* 1079 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, debug1);
/*      */       
/* 1081 */       setOrientation(debug3);
/* 1082 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorPiece(StructureManager debug1, CompoundTag debug2) {
/* 1086 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1091 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorPiece createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/* 1095 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -1, 0, 0, 5, 7, 5, debug4);
/*      */       
/* 1097 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 1098 */         return null;
/*      */       }
/*      */       
/* 1101 */       return new CastleSmallCorridorPiece(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1107 */       generateBox(debug1, debug5, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1109 */       generateBox(debug1, debug5, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1111 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1114 */       generateBox(debug1, debug5, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1115 */       generateBox(debug1, debug5, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1116 */       generateBox(debug1, debug5, 0, 3, 1, 0, 4, 1, debug8, debug8, false);
/* 1117 */       generateBox(debug1, debug5, 0, 3, 3, 0, 4, 3, debug8, debug8, false);
/* 1118 */       generateBox(debug1, debug5, 4, 3, 1, 4, 4, 1, debug8, debug8, false);
/* 1119 */       generateBox(debug1, debug5, 4, 3, 3, 4, 4, 3, debug8, debug8, false);
/*      */ 
/*      */       
/* 1122 */       generateBox(debug1, debug5, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1125 */       for (int debug9 = 0; debug9 <= 4; debug9++) {
/* 1126 */         for (int debug10 = 0; debug10 <= 4; debug10++) {
/* 1127 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug9, -1, debug10, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1131 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleSmallCorridorCrossingPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleSmallCorridorCrossingPiece(int debug1, BoundingBox debug2, Direction debug3) {
/* 1141 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, debug1);
/*      */       
/* 1143 */       setOrientation(debug3);
/* 1144 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(StructureManager debug1, CompoundTag debug2) {
/* 1148 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1153 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 1, 0, true);
/* 1154 */       generateChildLeft((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 1, true);
/* 1155 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorCrossingPiece createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/* 1159 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -1, 0, 0, 5, 7, 5, debug4);
/*      */       
/* 1161 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 1162 */         return null;
/*      */       }
/*      */       
/* 1165 */       return new CastleSmallCorridorCrossingPiece(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1171 */       generateBox(debug1, debug5, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1173 */       generateBox(debug1, debug5, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1176 */       generateBox(debug1, debug5, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1177 */       generateBox(debug1, debug5, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1178 */       generateBox(debug1, debug5, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1179 */       generateBox(debug1, debug5, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1182 */       generateBox(debug1, debug5, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1185 */       for (int debug8 = 0; debug8 <= 4; debug8++) {
/* 1186 */         for (int debug9 = 0; debug9 <= 4; debug9++) {
/* 1187 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug8, -1, debug9, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1191 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class CastleSmallCorridorRightTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private boolean isNeedingChest;
/*      */ 
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/* 1203 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, debug1);
/*      */       
/* 1205 */       setOrientation(debug4);
/* 1206 */       this.boundingBox = debug3;
/*      */       
/* 1208 */       this.isNeedingChest = (debug2.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(StructureManager debug1, CompoundTag debug2) {
/* 1212 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, debug2);
/* 1213 */       this.isNeedingChest = debug2.getBoolean("Chest");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 1218 */       super.addAdditionalSaveData(debug1);
/*      */       
/* 1220 */       debug1.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1225 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorRightTurnPiece createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/* 1229 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, 0, 0, 5, 7, 5, debug5);
/*      */       
/* 1231 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/* 1232 */         return null;
/*      */       }
/*      */       
/* 1235 */       return new CastleSmallCorridorRightTurnPiece(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1241 */       generateBox(debug1, debug5, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1243 */       generateBox(debug1, debug5, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1245 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1246 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1249 */       generateBox(debug1, debug5, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1250 */       generateBox(debug1, debug5, 0, 3, 1, 0, 4, 1, debug9, debug9, false);
/* 1251 */       generateBox(debug1, debug5, 0, 3, 3, 0, 4, 3, debug9, debug9, false);
/*      */       
/* 1253 */       generateBox(debug1, debug5, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1255 */       generateBox(debug1, debug5, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1256 */       generateBox(debug1, debug5, 1, 3, 4, 1, 4, 4, debug8, debug8, false);
/* 1257 */       generateBox(debug1, debug5, 3, 3, 4, 3, 4, 4, debug8, debug8, false);
/*      */       
/* 1259 */       if (this.isNeedingChest && 
/* 1260 */         debug5.isInside((Vec3i)new BlockPos(getWorldX(1, 3), getWorldY(2), getWorldZ(1, 3)))) {
/* 1261 */         this.isNeedingChest = false;
/* 1262 */         createChest(debug1, debug5, debug4, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1267 */       generateBox(debug1, debug5, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1270 */       for (int debug10 = 0; debug10 <= 4; debug10++) {
/* 1271 */         for (int debug11 = 0; debug11 <= 4; debug11++) {
/* 1272 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug10, -1, debug11, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1276 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class CastleSmallCorridorLeftTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private boolean isNeedingChest;
/*      */ 
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/* 1288 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, debug1);
/*      */       
/* 1290 */       setOrientation(debug4);
/* 1291 */       this.boundingBox = debug3;
/*      */       
/* 1293 */       this.isNeedingChest = (debug2.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(StructureManager debug1, CompoundTag debug2) {
/* 1297 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, debug2);
/* 1298 */       this.isNeedingChest = debug2.getBoolean("Chest");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 1303 */       super.addAdditionalSaveData(debug1);
/*      */       
/* 1305 */       debug1.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1310 */       generateChildLeft((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorLeftTurnPiece createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/* 1314 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, 0, 0, 5, 7, 5, debug5);
/*      */       
/* 1316 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/* 1317 */         return null;
/*      */       }
/*      */       
/* 1320 */       return new CastleSmallCorridorLeftTurnPiece(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1326 */       generateBox(debug1, debug5, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1328 */       generateBox(debug1, debug5, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1330 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1331 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1334 */       generateBox(debug1, debug5, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1335 */       generateBox(debug1, debug5, 4, 3, 1, 4, 4, 1, debug9, debug9, false);
/* 1336 */       generateBox(debug1, debug5, 4, 3, 3, 4, 4, 3, debug9, debug9, false);
/*      */       
/* 1338 */       generateBox(debug1, debug5, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1340 */       generateBox(debug1, debug5, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1341 */       generateBox(debug1, debug5, 1, 3, 4, 1, 4, 4, debug8, debug8, false);
/* 1342 */       generateBox(debug1, debug5, 3, 3, 4, 3, 4, 4, debug8, debug8, false);
/*      */       
/* 1344 */       if (this.isNeedingChest && 
/* 1345 */         debug5.isInside((Vec3i)new BlockPos(getWorldX(3, 3), getWorldY(2), getWorldZ(3, 3)))) {
/* 1346 */         this.isNeedingChest = false;
/* 1347 */         createChest(debug1, debug5, debug4, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1352 */       generateBox(debug1, debug5, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1355 */       for (int debug10 = 0; debug10 <= 4; debug10++) {
/* 1356 */         for (int debug11 = 0; debug11 <= 4; debug11++) {
/* 1357 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug10, -1, debug11, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1361 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleCorridorStairsPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleCorridorStairsPiece(int debug1, BoundingBox debug2, Direction debug3) {
/* 1371 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, debug1);
/*      */       
/* 1373 */       setOrientation(debug3);
/* 1374 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public CastleCorridorStairsPiece(StructureManager debug1, CompoundTag debug2) {
/* 1378 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1383 */       generateChildForward((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleCorridorStairsPiece createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/* 1387 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -1, -7, 0, 5, 14, 10, debug4);
/*      */       
/* 1389 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 1390 */         return null;
/*      */       }
/*      */       
/* 1393 */       return new CastleCorridorStairsPiece(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1399 */       BlockState debug8 = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 1400 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/* 1402 */       for (int debug10 = 0; debug10 <= 9; debug10++) {
/* 1403 */         int debug11 = Math.max(1, 7 - debug10);
/* 1404 */         int debug12 = Math.min(Math.max(debug11 + 5, 14 - debug10), 13);
/* 1405 */         int debug13 = debug10;
/*      */ 
/*      */         
/* 1408 */         generateBox(debug1, debug5, 0, 0, debug13, 4, debug11, debug13, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1410 */         generateBox(debug1, debug5, 1, debug11 + 1, debug13, 3, debug12 - 1, debug13, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1411 */         if (debug10 <= 6) {
/* 1412 */           placeBlock(debug1, debug8, 1, debug11 + 1, debug13, debug5);
/* 1413 */           placeBlock(debug1, debug8, 2, debug11 + 1, debug13, debug5);
/* 1414 */           placeBlock(debug1, debug8, 3, debug11 + 1, debug13, debug5);
/*      */         } 
/*      */         
/* 1417 */         generateBox(debug1, debug5, 0, debug12, debug13, 4, debug12, debug13, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1419 */         generateBox(debug1, debug5, 0, debug11 + 1, debug13, 0, debug12 - 1, debug13, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1420 */         generateBox(debug1, debug5, 4, debug11 + 1, debug13, 4, debug12 - 1, debug13, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1421 */         if ((debug10 & 0x1) == 0) {
/* 1422 */           generateBox(debug1, debug5, 0, debug11 + 2, debug13, 0, debug11 + 3, debug13, debug9, debug9, false);
/* 1423 */           generateBox(debug1, debug5, 4, debug11 + 2, debug13, 4, debug11 + 3, debug13, debug9, debug9, false);
/*      */         } 
/*      */ 
/*      */         
/* 1427 */         for (int debug14 = 0; debug14 <= 4; debug14++) {
/* 1428 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug14, -1, debug13, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1432 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CastleCorridorTBalconyPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     public CastleCorridorTBalconyPiece(int debug1, BoundingBox debug2, Direction debug3) {
/* 1442 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, debug1);
/*      */       
/* 1444 */       setOrientation(debug3);
/* 1445 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public CastleCorridorTBalconyPiece(StructureManager debug1, CompoundTag debug2) {
/* 1449 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1454 */       int debug4 = 1;
/*      */       
/* 1456 */       Direction debug5 = getOrientation();
/* 1457 */       if (debug5 == Direction.WEST || debug5 == Direction.NORTH) {
/* 1458 */         debug4 = 5;
/*      */       }
/*      */       
/* 1461 */       generateChildLeft((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, debug4, (debug3.nextInt(8) > 0));
/* 1462 */       generateChildRight((NetherBridgePieces.StartPiece)debug1, debug2, debug3, 0, debug4, (debug3.nextInt(8) > 0));
/*      */     }
/*      */     
/*      */     public static CastleCorridorTBalconyPiece createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/* 1466 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -3, 0, 0, 9, 7, 9, debug4);
/*      */       
/* 1468 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 1469 */         return null;
/*      */       }
/*      */       
/* 1472 */       return new CastleCorridorTBalconyPiece(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1477 */       BlockState debug8 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1478 */       BlockState debug9 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1481 */       generateBox(debug1, debug5, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1483 */       generateBox(debug1, debug5, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1485 */       generateBox(debug1, debug5, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1488 */       generateBox(debug1, debug5, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1489 */       generateBox(debug1, debug5, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1490 */       generateBox(debug1, debug5, 1, 3, 0, 1, 4, 0, debug9, debug9, false);
/* 1491 */       generateBox(debug1, debug5, 7, 3, 0, 7, 4, 0, debug9, debug9, false);
/*      */ 
/*      */       
/* 1494 */       generateBox(debug1, debug5, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1495 */       generateBox(debug1, debug5, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1496 */       generateBox(debug1, debug5, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1499 */       generateBox(debug1, debug5, 1, 3, 8, 7, 3, 8, debug9, debug9, false);
/* 1500 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 3, 8, debug5);
/* 1501 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 8, 3, 8, debug5);
/* 1502 */       generateBox(debug1, debug5, 0, 3, 6, 0, 3, 7, debug8, debug8, false);
/* 1503 */       generateBox(debug1, debug5, 8, 3, 6, 8, 3, 7, debug8, debug8, false);
/*      */ 
/*      */       
/* 1506 */       generateBox(debug1, debug5, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1507 */       generateBox(debug1, debug5, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1508 */       generateBox(debug1, debug5, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1509 */       generateBox(debug1, debug5, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1510 */       generateBox(debug1, debug5, 1, 4, 5, 1, 5, 5, debug9, debug9, false);
/* 1511 */       generateBox(debug1, debug5, 7, 4, 5, 7, 5, 5, debug9, debug9, false);
/*      */ 
/*      */       
/* 1514 */       for (int debug10 = 0; debug10 <= 5; debug10++) {
/* 1515 */         for (int debug11 = 0; debug11 <= 8; debug11++) {
/* 1516 */           fillColumnDown(debug1, Blocks.NETHER_BRICKS.defaultBlockState(), debug11, -1, debug10, debug5);
/*      */         }
/*      */       } 
/*      */       
/* 1520 */       return true;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\NetherBridgePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */