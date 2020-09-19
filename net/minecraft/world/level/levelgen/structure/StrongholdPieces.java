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
/*      */ import net.minecraft.world.level.block.ButtonBlock;
/*      */ import net.minecraft.world.level.block.DoorBlock;
/*      */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.IronBarsBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.SlabBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StrongholdPieces
/*      */ {
/*      */   static class PieceWeight
/*      */   {
/*      */     public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*      */     public final int weight;
/*      */     public int placeCount;
/*      */     public final int maxPlaceCount;
/*      */     
/*      */     public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> debug1, int debug2, int debug3) {
/*   55 */       this.pieceClass = debug1;
/*   56 */       this.weight = debug2;
/*   57 */       this.maxPlaceCount = debug3;
/*      */     }
/*      */     
/*      */     public boolean doPlace(int debug1) {
/*   61 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/*   65 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */   }
/*      */   
/*   69 */   private static final PieceWeight[] STRONGHOLD_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)Straight.class, 40, 0), new PieceWeight((Class)PrisonHall.class, 5, 5), new PieceWeight((Class)LeftTurn.class, 20, 0), new PieceWeight((Class)RightTurn.class, 20, 0), new PieceWeight((Class)RoomCrossing.class, 10, 6), new PieceWeight((Class)StraightStairsDown.class, 5, 5), new PieceWeight((Class)StairsDown.class, 5, 5), new PieceWeight((Class)FiveCrossing.class, 5, 4), new PieceWeight((Class)ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public boolean doPlace(int debug1)
/*      */         {
/*   82 */           return (super.doPlace(debug1) && debug1 > 4);
/*      */         }
/*      */       }, 
/*      */       new PieceWeight(PortalRoom.class, 20, 1)
/*      */       {
/*      */         public boolean doPlace(int debug1) {
/*   88 */           return (super.doPlace(debug1) && debug1 > 5);
/*      */         }
/*      */       } };
/*      */   
/*      */   private static List<PieceWeight> currentPieces;
/*      */   private static Class<? extends StrongholdPiece> imposedPiece;
/*      */   private static int totalWeight;
/*      */   
/*      */   public static void resetPieces() {
/*   97 */     currentPieces = Lists.newArrayList();
/*   98 */     for (PieceWeight debug3 : STRONGHOLD_PIECE_WEIGHTS) {
/*   99 */       debug3.placeCount = 0;
/*  100 */       currentPieces.add(debug3);
/*      */     } 
/*  102 */     imposedPiece = null;
/*      */   }
/*      */   
/*      */   private static boolean updatePieceWeight() {
/*  106 */     boolean debug0 = false;
/*  107 */     totalWeight = 0;
/*  108 */     for (PieceWeight debug2 : currentPieces) {
/*  109 */       if (debug2.maxPlaceCount > 0 && debug2.placeCount < debug2.maxPlaceCount) {
/*  110 */         debug0 = true;
/*      */       }
/*  112 */       totalWeight += debug2.weight;
/*      */     } 
/*  114 */     return debug0;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece findAndCreatePieceFactory(Class<? extends StrongholdPiece> debug0, List<StructurePiece> debug1, Random debug2, int debug3, int debug4, int debug5, @Nullable Direction debug6, int debug7) {
/*  118 */     StrongholdPiece debug8 = null;
/*      */     
/*  120 */     if (debug0 == Straight.class) {
/*  121 */       debug8 = Straight.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  122 */     } else if (debug0 == PrisonHall.class) {
/*  123 */       debug8 = PrisonHall.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  124 */     } else if (debug0 == LeftTurn.class) {
/*  125 */       debug8 = LeftTurn.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  126 */     } else if (debug0 == RightTurn.class) {
/*  127 */       debug8 = RightTurn.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  128 */     } else if (debug0 == RoomCrossing.class) {
/*  129 */       debug8 = RoomCrossing.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  130 */     } else if (debug0 == StraightStairsDown.class) {
/*  131 */       debug8 = StraightStairsDown.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  132 */     } else if (debug0 == StairsDown.class) {
/*  133 */       debug8 = StairsDown.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  134 */     } else if (debug0 == FiveCrossing.class) {
/*  135 */       debug8 = FiveCrossing.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  136 */     } else if (debug0 == ChestCorridor.class) {
/*  137 */       debug8 = ChestCorridor.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  138 */     } else if (debug0 == Library.class) {
/*  139 */       debug8 = Library.createPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  140 */     } else if (debug0 == PortalRoom.class) {
/*  141 */       debug8 = PortalRoom.createPiece(debug1, debug3, debug4, debug5, debug6, debug7);
/*      */     } 
/*      */     
/*  144 */     return debug8;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece generatePieceFromSmallDoor(StartPiece debug0, List<StructurePiece> debug1, Random debug2, int debug3, int debug4, int debug5, Direction debug6, int debug7) {
/*  148 */     if (!updatePieceWeight()) {
/*  149 */       return null;
/*      */     }
/*      */     
/*  152 */     if (imposedPiece != null) {
/*  153 */       StrongholdPiece strongholdPiece = findAndCreatePieceFactory(imposedPiece, debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  154 */       imposedPiece = null;
/*      */       
/*  156 */       if (strongholdPiece != null) {
/*  157 */         return strongholdPiece;
/*      */       }
/*      */     } 
/*      */     
/*  161 */     int debug8 = 0;
/*  162 */     while (debug8 < 5) {
/*  163 */       debug8++;
/*      */       
/*  165 */       int i = debug2.nextInt(totalWeight);
/*  166 */       for (PieceWeight debug11 : currentPieces) {
/*  167 */         i -= debug11.weight;
/*  168 */         if (i < 0) {
/*  169 */           if (!debug11.doPlace(debug7) || debug11 == debug0.previousPiece) {
/*      */             break;
/*      */           }
/*      */           
/*  173 */           StrongholdPiece debug12 = findAndCreatePieceFactory(debug11.pieceClass, debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*  174 */           if (debug12 != null) {
/*  175 */             debug11.placeCount++;
/*  176 */             debug0.previousPiece = debug11;
/*      */             
/*  178 */             if (!debug11.isValid()) {
/*  179 */               currentPieces.remove(debug11);
/*      */             }
/*  181 */             return debug12;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  186 */     BoundingBox debug9 = FillerCorridor.findPieceBox(debug1, debug2, debug3, debug4, debug5, debug6);
/*  187 */     if (debug9 != null && debug9.y0 > 1) {
/*  188 */       return new FillerCorridor(debug7, debug9, debug6);
/*      */     }
/*      */     
/*  191 */     return null;
/*      */   }
/*      */   
/*      */   private static StructurePiece generateAndAddPiece(StartPiece debug0, List<StructurePiece> debug1, Random debug2, int debug3, int debug4, int debug5, @Nullable Direction debug6, int debug7) {
/*  195 */     if (debug7 > 50) {
/*  196 */       return null;
/*      */     }
/*  198 */     if (Math.abs(debug3 - (debug0.getBoundingBox()).x0) > 112 || Math.abs(debug5 - (debug0.getBoundingBox()).z0) > 112) {
/*  199 */       return null;
/*      */     }
/*      */     
/*  202 */     StructurePiece debug8 = generatePieceFromSmallDoor(debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7 + 1);
/*  203 */     if (debug8 != null) {
/*  204 */       debug1.add(debug8);
/*  205 */       debug0.pendingChildren.add(debug8);
/*      */     } 
/*  207 */     return debug8;
/*      */   }
/*      */   
/*      */   static abstract class StrongholdPiece extends StructurePiece {
/*  211 */     protected SmallDoorType entryDoor = SmallDoorType.OPENING;
/*      */     
/*      */     protected StrongholdPiece(StructurePieceType debug1, int debug2) {
/*  214 */       super(debug1, debug2);
/*      */     }
/*      */     
/*      */     public StrongholdPiece(StructurePieceType debug1, CompoundTag debug2) {
/*  218 */       super(debug1, debug2);
/*  219 */       this.entryDoor = SmallDoorType.valueOf(debug2.getString("EntryDoor"));
/*      */     }
/*      */     
/*      */     public enum SmallDoorType {
/*  223 */       OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  228 */       debug1.putString("EntryDoor", this.entryDoor.name());
/*      */     }
/*      */     
/*      */     protected void generateSmallDoor(WorldGenLevel debug1, Random debug2, BoundingBox debug3, SmallDoorType debug4, int debug5, int debug6, int debug7) {
/*  232 */       switch (debug4) {
/*      */         case NORTH:
/*  234 */           generateBox(debug1, debug3, debug5, debug6, debug7, debug5 + 3 - 1, debug6 + 3 - 1, debug7, CAVE_AIR, CAVE_AIR, false);
/*      */           break;
/*      */         case SOUTH:
/*  237 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6, debug7, debug3);
/*  238 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6 + 1, debug7, debug3);
/*  239 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6 + 2, debug7, debug3);
/*  240 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 1, debug6 + 2, debug7, debug3);
/*  241 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6 + 2, debug7, debug3);
/*  242 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6 + 1, debug7, debug3);
/*  243 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6, debug7, debug3);
/*  244 */           placeBlock(debug1, Blocks.OAK_DOOR.defaultBlockState(), debug5 + 1, debug6, debug7, debug3);
/*  245 */           placeBlock(debug1, (BlockState)Blocks.OAK_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), debug5 + 1, debug6 + 1, debug7, debug3);
/*      */           break;
/*      */         case WEST:
/*  248 */           placeBlock(debug1, Blocks.CAVE_AIR.defaultBlockState(), debug5 + 1, debug6, debug7, debug3);
/*  249 */           placeBlock(debug1, Blocks.CAVE_AIR.defaultBlockState(), debug5 + 1, debug6 + 1, debug7, debug3);
/*  250 */           placeBlock(debug1, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), debug5, debug6, debug7, debug3);
/*  251 */           placeBlock(debug1, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), debug5, debug6 + 1, debug7, debug3);
/*  252 */           placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), debug5, debug6 + 2, debug7, debug3);
/*  253 */           placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), debug5 + 1, debug6 + 2, debug7, debug3);
/*  254 */           placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), debug5 + 2, debug6 + 2, debug7, debug3);
/*  255 */           placeBlock(debug1, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), debug5 + 2, debug6 + 1, debug7, debug3);
/*  256 */           placeBlock(debug1, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), debug5 + 2, debug6, debug7, debug3);
/*      */           break;
/*      */         case EAST:
/*  259 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6, debug7, debug3);
/*  260 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6 + 1, debug7, debug3);
/*  261 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5, debug6 + 2, debug7, debug3);
/*  262 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 1, debug6 + 2, debug7, debug3);
/*  263 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6 + 2, debug7, debug3);
/*  264 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6 + 1, debug7, debug3);
/*  265 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), debug5 + 2, debug6, debug7, debug3);
/*  266 */           placeBlock(debug1, Blocks.IRON_DOOR.defaultBlockState(), debug5 + 1, debug6, debug7, debug3);
/*  267 */           placeBlock(debug1, (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), debug5 + 1, debug6 + 1, debug7, debug3);
/*  268 */           placeBlock(debug1, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.NORTH), debug5 + 2, debug6 + 1, debug7 + 1, debug3);
/*  269 */           placeBlock(debug1, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.SOUTH), debug5 + 2, debug6 + 1, debug7 - 1, debug3);
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     protected SmallDoorType randomSmallDoor(Random debug1) {
/*  275 */       int debug2 = debug1.nextInt(5);
/*  276 */       switch (debug2)
/*      */       
/*      */       { 
/*      */         default:
/*  280 */           return SmallDoorType.OPENING;
/*      */         case 2:
/*  282 */           return SmallDoorType.WOOD_DOOR;
/*      */         case 3:
/*  284 */           return SmallDoorType.GRATES;
/*      */         case 4:
/*  286 */           break; }  return SmallDoorType.IRON_DOOR;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5) {
/*  292 */       Direction debug6 = getOrientation();
/*  293 */       if (debug6 != null) {
/*  294 */         switch (debug6) {
/*      */           case NORTH:
/*  296 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug4, this.boundingBox.y0 + debug5, this.boundingBox.z0 - 1, debug6, getGenDepth());
/*      */           case SOUTH:
/*  298 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug4, this.boundingBox.y0 + debug5, this.boundingBox.z1 + 1, debug6, getGenDepth());
/*      */           case WEST:
/*  300 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug5, this.boundingBox.z0 + debug4, debug6, getGenDepth());
/*      */           case EAST:
/*  302 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug5, this.boundingBox.z0 + debug4, debug6, getGenDepth());
/*      */         } 
/*      */       }
/*  305 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5) {
/*  310 */       Direction debug6 = getOrientation();
/*  311 */       if (debug6 != null) {
/*  312 */         switch (debug6) {
/*      */           case NORTH:
/*  314 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.WEST, getGenDepth());
/*      */           case SOUTH:
/*  316 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.WEST, getGenDepth());
/*      */           case WEST:
/*  318 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z0 - 1, Direction.NORTH, getGenDepth());
/*      */           case EAST:
/*  320 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z0 - 1, Direction.NORTH, getGenDepth());
/*      */         } 
/*      */       }
/*  323 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece debug1, List<StructurePiece> debug2, Random debug3, int debug4, int debug5) {
/*  328 */       Direction debug6 = getOrientation();
/*  329 */       if (debug6 != null) {
/*  330 */         switch (debug6) {
/*      */           case NORTH:
/*  332 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.EAST, getGenDepth());
/*      */           case SOUTH:
/*  334 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug4, this.boundingBox.z0 + debug5, Direction.EAST, getGenDepth());
/*      */           case WEST:
/*  336 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z1 + 1, Direction.SOUTH, getGenDepth());
/*      */           case EAST:
/*  338 */             return StrongholdPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug4, this.boundingBox.z1 + 1, Direction.SOUTH, getGenDepth());
/*      */         } 
/*      */       }
/*  341 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean isOkBox(BoundingBox debug0) {
/*  345 */       return (debug0 != null && debug0.y0 > 10);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class FillerCorridor
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private final int steps;
/*      */     
/*      */     public FillerCorridor(int debug1, BoundingBox debug2, Direction debug3) {
/*  356 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, debug1);
/*      */       
/*  358 */       setOrientation(debug3);
/*  359 */       this.boundingBox = debug2;
/*  360 */       this.steps = (debug3 == Direction.NORTH || debug3 == Direction.SOUTH) ? debug2.getZSpan() : debug2.getXSpan();
/*      */     }
/*      */     
/*      */     public FillerCorridor(StructureManager debug1, CompoundTag debug2) {
/*  364 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, debug2);
/*  365 */       this.steps = debug2.getInt("Steps");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  370 */       super.addAdditionalSaveData(debug1);
/*  371 */       debug1.putInt("Steps", this.steps);
/*      */     }
/*      */     
/*      */     public static BoundingBox findPieceBox(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5) {
/*  375 */       int debug6 = 3;
/*      */       
/*  377 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, 4, debug5);
/*      */       
/*  379 */       StructurePiece debug8 = StructurePiece.findCollisionPiece(debug0, debug7);
/*  380 */       if (debug8 == null)
/*      */       {
/*  382 */         return null;
/*      */       }
/*      */       
/*  385 */       if ((debug8.getBoundingBox()).y0 == debug7.y0)
/*      */       {
/*  387 */         for (int debug9 = 3; debug9 >= 1; debug9--) {
/*  388 */           debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, debug9 - 1, debug5);
/*  389 */           if (!debug8.getBoundingBox().intersects(debug7))
/*      */           {
/*      */             
/*  392 */             return BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, debug9, debug5);
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*  397 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  403 */       for (int debug8 = 0; debug8 < this.steps; debug8++) {
/*      */         
/*  405 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, debug8, debug5);
/*  406 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, debug8, debug5);
/*  407 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, debug8, debug5);
/*  408 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, debug8, debug5);
/*  409 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, debug8, debug5);
/*      */         
/*  411 */         for (int debug9 = 1; debug9 <= 3; debug9++) {
/*  412 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 0, debug9, debug8, debug5);
/*  413 */           placeBlock(debug1, Blocks.CAVE_AIR.defaultBlockState(), 1, debug9, debug8, debug5);
/*  414 */           placeBlock(debug1, Blocks.CAVE_AIR.defaultBlockState(), 2, debug9, debug8, debug5);
/*  415 */           placeBlock(debug1, Blocks.CAVE_AIR.defaultBlockState(), 3, debug9, debug8, debug5);
/*  416 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 4, debug9, debug8, debug5);
/*      */         } 
/*      */         
/*  419 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, debug8, debug5);
/*  420 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, debug8, debug5);
/*  421 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, debug8, debug5);
/*  422 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, debug8, debug5);
/*  423 */         placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, debug8, debug5);
/*      */       } 
/*      */       
/*  426 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class StairsDown
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private final boolean isSource;
/*      */ 
/*      */     
/*      */     public StairsDown(StructurePieceType debug1, int debug2, Random debug3, int debug4, int debug5) {
/*  438 */       super(debug1, debug2);
/*      */       
/*  440 */       this.isSource = true;
/*  441 */       setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(debug3));
/*  442 */       this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
/*      */       
/*  444 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/*  445 */         this.boundingBox = new BoundingBox(debug4, 64, debug5, debug4 + 5 - 1, 74, debug5 + 5 - 1);
/*      */       } else {
/*  447 */         this.boundingBox = new BoundingBox(debug4, 64, debug5, debug4 + 5 - 1, 74, debug5 + 5 - 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public StairsDown(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  452 */       super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, debug1);
/*      */       
/*  454 */       this.isSource = false;
/*  455 */       setOrientation(debug4);
/*  456 */       this.entryDoor = randomSmallDoor(debug2);
/*  457 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public StairsDown(StructurePieceType debug1, CompoundTag debug2) {
/*  461 */       super(debug1, debug2);
/*  462 */       this.isSource = debug2.getBoolean("Source");
/*      */     }
/*      */     
/*      */     public StairsDown(StructureManager debug1, CompoundTag debug2) {
/*  466 */       this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  471 */       super.addAdditionalSaveData(debug1);
/*  472 */       debug1.putBoolean("Source", this.isSource);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  477 */       if (this.isSource)
/*      */       {
/*  479 */         StrongholdPieces.imposedPiece = (Class)StrongholdPieces.FiveCrossing.class;
/*      */       }
/*  481 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */     }
/*      */     
/*      */     public static StairsDown createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  485 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -7, 0, 5, 11, 5, debug5);
/*      */       
/*  487 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  488 */         return null;
/*      */       }
/*      */       
/*  491 */       return new StairsDown(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  497 */       generateBox(debug1, debug5, 0, 0, 0, 4, 10, 4, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  499 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 7, 0);
/*      */       
/*  501 */       generateSmallDoor(debug1, debug4, debug5, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
/*      */ 
/*      */       
/*  504 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, debug5);
/*  505 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, debug5);
/*  506 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, debug5);
/*  507 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, debug5);
/*  508 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, debug5);
/*  509 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, debug5);
/*  510 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, debug5);
/*  511 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, debug5);
/*  512 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, debug5);
/*  513 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, debug5);
/*  514 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, debug5);
/*  515 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, debug5);
/*  516 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, debug5);
/*  517 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, debug5);
/*  518 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, debug5);
/*  519 */       placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, debug5);
/*  520 */       placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, debug5);
/*      */       
/*  522 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StartPiece
/*      */     extends StairsDown
/*      */   {
/*      */     public StrongholdPieces.PieceWeight previousPiece;
/*      */     @Nullable
/*      */     public StrongholdPieces.PortalRoom portalRoomPiece;
/*  532 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(Random debug1, int debug2, int debug3) {
/*  535 */       super(StructurePieceType.STRONGHOLD_START, 0, debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public StartPiece(StructureManager debug1, CompoundTag debug2) {
/*  539 */       super(StructurePieceType.STRONGHOLD_START, debug2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Straight
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private final boolean leftChild;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean rightChild;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Straight(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  560 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, debug1);
/*      */       
/*  562 */       setOrientation(debug4);
/*  563 */       this.entryDoor = randomSmallDoor(debug2);
/*  564 */       this.boundingBox = debug3;
/*      */       
/*  566 */       this.leftChild = (debug2.nextInt(2) == 0);
/*  567 */       this.rightChild = (debug2.nextInt(2) == 0);
/*      */     }
/*      */     
/*      */     public Straight(StructureManager debug1, CompoundTag debug2) {
/*  571 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, debug2);
/*  572 */       this.leftChild = debug2.getBoolean("Left");
/*  573 */       this.rightChild = debug2.getBoolean("Right");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  578 */       super.addAdditionalSaveData(debug1);
/*  579 */       debug1.putBoolean("Left", this.leftChild);
/*  580 */       debug1.putBoolean("Right", this.rightChild);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  585 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*  586 */       if (this.leftChild) {
/*  587 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 2);
/*      */       }
/*  589 */       if (this.rightChild) {
/*  590 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 2);
/*      */       }
/*      */     }
/*      */     
/*      */     public static Straight createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  595 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, 7, debug5);
/*      */       
/*  597 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  598 */         return null;
/*      */       }
/*      */       
/*  601 */       return new Straight(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  607 */       generateBox(debug1, debug5, 0, 0, 0, 4, 4, 6, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  609 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 1, 0);
/*      */       
/*  611 */       generateSmallDoor(debug1, debug4, debug5, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*      */       
/*  613 */       BlockState debug8 = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST);
/*  614 */       BlockState debug9 = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST);
/*      */       
/*  616 */       maybeGenerateBlock(debug1, debug5, debug4, 0.1F, 1, 2, 1, debug8);
/*  617 */       maybeGenerateBlock(debug1, debug5, debug4, 0.1F, 3, 2, 1, debug9);
/*  618 */       maybeGenerateBlock(debug1, debug5, debug4, 0.1F, 1, 2, 5, debug8);
/*  619 */       maybeGenerateBlock(debug1, debug5, debug4, 0.1F, 3, 2, 5, debug9);
/*      */       
/*  621 */       if (this.leftChild) {
/*  622 */         generateBox(debug1, debug5, 0, 1, 2, 0, 3, 4, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/*  624 */       if (this.rightChild) {
/*  625 */         generateBox(debug1, debug5, 4, 1, 2, 4, 3, 4, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/*      */       
/*  628 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ChestCorridor
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private boolean hasPlacedChest;
/*      */ 
/*      */     
/*      */     public ChestCorridor(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  640 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, debug1);
/*      */       
/*  642 */       setOrientation(debug4);
/*  643 */       this.entryDoor = randomSmallDoor(debug2);
/*  644 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public ChestCorridor(StructureManager debug1, CompoundTag debug2) {
/*  648 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, debug2);
/*  649 */       this.hasPlacedChest = debug2.getBoolean("Chest");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  654 */       super.addAdditionalSaveData(debug1);
/*  655 */       debug1.putBoolean("Chest", this.hasPlacedChest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  660 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */     }
/*      */     
/*      */     public static ChestCorridor createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  664 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, 7, debug5);
/*      */       
/*  666 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  667 */         return null;
/*      */       }
/*      */       
/*  670 */       return new ChestCorridor(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  676 */       generateBox(debug1, debug5, 0, 0, 0, 4, 4, 6, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  678 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 1, 0);
/*      */       
/*  680 */       generateSmallDoor(debug1, debug4, debug5, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*      */ 
/*      */       
/*  683 */       generateBox(debug1, debug5, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
/*  684 */       placeBlock(debug1, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, debug5);
/*  685 */       placeBlock(debug1, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, debug5);
/*  686 */       placeBlock(debug1, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, debug5);
/*  687 */       placeBlock(debug1, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, debug5);
/*  688 */       for (int debug8 = 2; debug8 <= 4; debug8++) {
/*  689 */         placeBlock(debug1, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, debug8, debug5);
/*      */       }
/*      */       
/*  692 */       if (!this.hasPlacedChest && 
/*  693 */         debug5.isInside((Vec3i)new BlockPos(getWorldX(3, 3), getWorldY(2), getWorldZ(3, 3)))) {
/*  694 */         this.hasPlacedChest = true;
/*  695 */         createChest(debug1, debug5, debug4, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
/*      */       } 
/*      */ 
/*      */       
/*  699 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class StraightStairsDown
/*      */     extends StrongholdPiece
/*      */   {
/*      */     public StraightStairsDown(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  709 */       super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, debug1);
/*      */       
/*  711 */       setOrientation(debug4);
/*  712 */       this.entryDoor = randomSmallDoor(debug2);
/*  713 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public StraightStairsDown(StructureManager debug1, CompoundTag debug2) {
/*  717 */       super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  722 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */     }
/*      */     
/*      */     public static StraightStairsDown createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  726 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -7, 0, 5, 11, 8, debug5);
/*      */       
/*  728 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  729 */         return null;
/*      */       }
/*      */       
/*  732 */       return new StraightStairsDown(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  738 */       generateBox(debug1, debug5, 0, 0, 0, 4, 10, 7, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  740 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 7, 0);
/*      */       
/*  742 */       generateSmallDoor(debug1, debug4, debug5, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 7);
/*      */ 
/*      */       
/*  745 */       BlockState debug8 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*  746 */       for (int debug9 = 0; debug9 < 6; debug9++) {
/*  747 */         placeBlock(debug1, debug8, 1, 6 - debug9, 1 + debug9, debug5);
/*  748 */         placeBlock(debug1, debug8, 2, 6 - debug9, 1 + debug9, debug5);
/*  749 */         placeBlock(debug1, debug8, 3, 6 - debug9, 1 + debug9, debug5);
/*  750 */         if (debug9 < 5) {
/*  751 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - debug9, 1 + debug9, debug5);
/*  752 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - debug9, 1 + debug9, debug5);
/*  753 */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - debug9, 1 + debug9, debug5);
/*      */         } 
/*      */       } 
/*      */       
/*  757 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static abstract class Turn
/*      */     extends StrongholdPiece
/*      */   {
/*      */     protected Turn(StructurePieceType debug1, int debug2) {
/*  767 */       super(debug1, debug2);
/*      */     }
/*      */     
/*      */     public Turn(StructurePieceType debug1, CompoundTag debug2) {
/*  771 */       super(debug1, debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class LeftTurn extends Turn {
/*      */     public LeftTurn(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  777 */       super(StructurePieceType.STRONGHOLD_LEFT_TURN, debug1);
/*      */       
/*  779 */       setOrientation(debug4);
/*  780 */       this.entryDoor = randomSmallDoor(debug2);
/*  781 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public LeftTurn(StructureManager debug1, CompoundTag debug2) {
/*  785 */       super(StructurePieceType.STRONGHOLD_LEFT_TURN, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  790 */       Direction debug4 = getOrientation();
/*  791 */       if (debug4 == Direction.NORTH || debug4 == Direction.EAST) {
/*  792 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */       } else {
/*  794 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static LeftTurn createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  799 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, 5, debug5);
/*      */       
/*  801 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  802 */         return null;
/*      */       }
/*      */       
/*  805 */       return new LeftTurn(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  811 */       generateBox(debug1, debug5, 0, 0, 0, 4, 4, 4, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  813 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 1, 0);
/*      */       
/*  815 */       Direction debug8 = getOrientation();
/*  816 */       if (debug8 == Direction.NORTH || debug8 == Direction.EAST) {
/*  817 */         generateBox(debug1, debug5, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  819 */         generateBox(debug1, debug5, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */       
/*  822 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RightTurn extends Turn {
/*      */     public RightTurn(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  828 */       super(StructurePieceType.STRONGHOLD_RIGHT_TURN, debug1);
/*      */       
/*  830 */       setOrientation(debug4);
/*  831 */       this.entryDoor = randomSmallDoor(debug2);
/*  832 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     public RightTurn(StructureManager debug1, CompoundTag debug2) {
/*  836 */       super(StructurePieceType.STRONGHOLD_RIGHT_TURN, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  841 */       Direction debug4 = getOrientation();
/*  842 */       if (debug4 == Direction.NORTH || debug4 == Direction.EAST) {
/*  843 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */       } else {
/*  845 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static RightTurn createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  850 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 5, 5, 5, debug5);
/*      */       
/*  852 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  853 */         return null;
/*      */       }
/*      */       
/*  856 */       return new RightTurn(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  862 */       generateBox(debug1, debug5, 0, 0, 0, 4, 4, 4, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  864 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 1, 0);
/*      */       
/*  866 */       Direction debug8 = getOrientation();
/*  867 */       if (debug8 == Direction.NORTH || debug8 == Direction.EAST) {
/*  868 */         generateBox(debug1, debug5, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  870 */         generateBox(debug1, debug5, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */       
/*  873 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class RoomCrossing
/*      */     extends StrongholdPiece
/*      */   {
/*      */     protected final int type;
/*      */ 
/*      */     
/*      */     public RoomCrossing(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/*  885 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, debug1);
/*      */       
/*  887 */       setOrientation(debug4);
/*  888 */       this.entryDoor = randomSmallDoor(debug2);
/*  889 */       this.boundingBox = debug3;
/*  890 */       this.type = debug2.nextInt(5);
/*      */     }
/*      */     
/*      */     public RoomCrossing(StructureManager debug1, CompoundTag debug2) {
/*  894 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, debug2);
/*  895 */       this.type = debug2.getInt("Type");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  900 */       super.addAdditionalSaveData(debug1);
/*  901 */       debug1.putInt("Type", this.type);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/*  906 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 4, 1);
/*  907 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 4);
/*  908 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 4);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/*  912 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -4, -1, 0, 11, 7, 11, debug5);
/*      */       
/*  914 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*  915 */         return null;
/*      */       }
/*      */       
/*  918 */       return new RoomCrossing(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  924 */       generateBox(debug1, debug5, 0, 0, 0, 10, 6, 10, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  926 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 4, 1, 0);
/*      */       
/*  928 */       generateBox(debug1, debug5, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*  929 */       generateBox(debug1, debug5, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*  930 */       generateBox(debug1, debug5, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*      */       
/*  932 */       switch (this.type)
/*      */       
/*      */       { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 1009 */           return true;
/*      */         case 0:
/*      */           placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, debug5); placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 4, 3, 5, debug5); placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 6, 3, 5, debug5); placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 5, 3, 4, debug5); placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH), 5, 3, 6, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, debug5); placeBlock(debug1, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, debug5);
/*      */         case 1:
/*      */           for (i = 0; i < 5; i++) { placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + i, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + i, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 3, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 7, debug5); }  placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, debug5); placeBlock(debug1, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, debug5); placeBlock(debug1, Blocks.WATER.defaultBlockState(), 5, 4, 5, debug5);
/*      */         case 2:
/*      */           break; }  int i; for (i = 1; i <= 9; i++) { placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, i, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, i, debug5); }  for (i = 1; i <= 9; i++) { placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), i, 3, 1, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), i, 3, 9, debug5); }
/*      */        placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, debug5); for (i = 1; i <= 3; i++) { placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 4, i, 4, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 6, i, 4, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 4, i, 6, debug5); placeBlock(debug1, Blocks.COBBLESTONE.defaultBlockState(), 6, i, 6, debug5); }
/*      */        placeBlock(debug1, Blocks.TORCH.defaultBlockState(), 5, 3, 5, debug5); for (i = 2; i <= 8; i++) { placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, i, debug5); placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, i, debug5); if (i <= 3 || i >= 7) { placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, i, debug5); placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, i, debug5); placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, i, debug5); }
/*      */          placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, i, debug5); placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, i, debug5); }
/* 1019 */        BlockState debug8 = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.WEST); placeBlock(debug1, debug8, 9, 1, 3, debug5); placeBlock(debug1, debug8, 9, 2, 3, debug5); placeBlock(debug1, debug8, 9, 3, 3, debug5); createChest(debug1, debug5, debug4, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING); } } public static class PrisonHall extends StrongholdPiece { public PrisonHall(int debug1, Random debug2, BoundingBox debug3, Direction debug4) { super(StructurePieceType.STRONGHOLD_PRISON_HALL, debug1);
/*      */       
/* 1021 */       setOrientation(debug4);
/* 1022 */       this.entryDoor = randomSmallDoor(debug2);
/* 1023 */       this.boundingBox = debug3; }
/*      */ 
/*      */     
/*      */     public PrisonHall(StructureManager debug1, CompoundTag debug2) {
/* 1027 */       super(StructurePieceType.STRONGHOLD_PRISON_HALL, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1032 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 1, 1);
/*      */     }
/*      */     
/*      */     public static PrisonHall createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/* 1036 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -1, -1, 0, 9, 5, 11, debug5);
/*      */       
/* 1038 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/* 1039 */         return null;
/*      */       }
/*      */       
/* 1042 */       return new PrisonHall(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1048 */       generateBox(debug1, debug5, 0, 0, 0, 8, 4, 10, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1050 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 1, 1, 0);
/*      */       
/* 1052 */       generateBox(debug1, debug5, 1, 1, 10, 3, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*      */ 
/*      */       
/* 1055 */       generateBox(debug1, debug5, 4, 1, 1, 4, 3, 1, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1056 */       generateBox(debug1, debug5, 4, 1, 3, 4, 3, 3, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1057 */       generateBox(debug1, debug5, 4, 1, 7, 4, 3, 7, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1058 */       generateBox(debug1, debug5, 4, 1, 9, 4, 3, 9, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1061 */       for (int i = 1; i <= 3; i++) {
/* 1062 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, i, 4, debug5);
/* 1063 */         placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 4, i, 5, debug5);
/* 1064 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, i, 6, debug5);
/*      */         
/* 1066 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 5, i, 5, debug5);
/* 1067 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 6, i, 5, debug5);
/* 1068 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 7, i, 5, debug5);
/*      */       } 
/*      */ 
/*      */       
/* 1072 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 2, debug5);
/* 1073 */       placeBlock(debug1, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 8, debug5);
/* 1074 */       BlockState debug8 = (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST);
/* 1075 */       BlockState debug9 = (BlockState)((BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST)).setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 1076 */       placeBlock(debug1, debug8, 4, 1, 2, debug5);
/* 1077 */       placeBlock(debug1, debug9, 4, 2, 2, debug5);
/* 1078 */       placeBlock(debug1, debug8, 4, 1, 8, debug5);
/* 1079 */       placeBlock(debug1, debug9, 4, 2, 8, debug5);
/*      */       
/* 1081 */       return true;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Library
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private final boolean isTall;
/*      */ 
/*      */     
/*      */     public Library(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/* 1094 */       super(StructurePieceType.STRONGHOLD_LIBRARY, debug1);
/*      */       
/* 1096 */       setOrientation(debug4);
/* 1097 */       this.entryDoor = randomSmallDoor(debug2);
/* 1098 */       this.boundingBox = debug3;
/* 1099 */       this.isTall = (debug3.getYSpan() > 6);
/*      */     }
/*      */     
/*      */     public Library(StructureManager debug1, CompoundTag debug2) {
/* 1103 */       super(StructurePieceType.STRONGHOLD_LIBRARY, debug2);
/* 1104 */       this.isTall = debug2.getBoolean("Tall");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 1109 */       super.addAdditionalSaveData(debug1);
/* 1110 */       debug1.putBoolean("Tall", this.isTall);
/*      */     }
/*      */ 
/*      */     
/*      */     public static Library createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/* 1115 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -4, -1, 0, 14, 11, 15, debug5);
/*      */       
/* 1117 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/*      */         
/* 1119 */         debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -4, -1, 0, 14, 6, 15, debug5);
/*      */         
/* 1121 */         if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/* 1122 */           return null;
/*      */         }
/*      */       } 
/*      */       
/* 1126 */       return new Library(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1131 */       int debug8 = 11;
/* 1132 */       if (!this.isTall) {
/* 1133 */         debug8 = 6;
/*      */       }
/*      */ 
/*      */       
/* 1137 */       generateBox(debug1, debug5, 0, 0, 0, 13, debug8 - 1, 14, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1139 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 4, 1, 0);
/*      */ 
/*      */       
/* 1142 */       generateMaybeBox(debug1, debug5, debug4, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/*      */       
/* 1144 */       int debug9 = 1;
/* 1145 */       int debug10 = 12;
/*      */       
/*      */       int debug11;
/* 1148 */       for (debug11 = 1; debug11 <= 13; debug11++) {
/* 1149 */         if ((debug11 - 1) % 4 == 0) {
/* 1150 */           generateBox(debug1, debug5, 1, 1, debug11, 1, 4, debug11, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1151 */           generateBox(debug1, debug5, 12, 1, debug11, 12, 4, debug11, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */           
/* 1153 */           placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 2, 3, debug11, debug5);
/* 1154 */           placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 11, 3, debug11, debug5);
/*      */           
/* 1156 */           if (this.isTall) {
/* 1157 */             generateBox(debug1, debug5, 1, 6, debug11, 1, 9, debug11, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1158 */             generateBox(debug1, debug5, 12, 6, debug11, 12, 9, debug11, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */           } 
/*      */         } else {
/* 1161 */           generateBox(debug1, debug5, 1, 1, debug11, 1, 4, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1162 */           generateBox(debug1, debug5, 12, 1, debug11, 12, 4, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */           
/* 1164 */           if (this.isTall) {
/* 1165 */             generateBox(debug1, debug5, 1, 6, debug11, 1, 9, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1166 */             generateBox(debug1, debug5, 12, 6, debug11, 12, 9, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1172 */       for (debug11 = 3; debug11 < 12; debug11 += 2) {
/* 1173 */         generateBox(debug1, debug5, 3, 1, debug11, 4, 3, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1174 */         generateBox(debug1, debug5, 6, 1, debug11, 7, 3, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1175 */         generateBox(debug1, debug5, 9, 1, debug11, 10, 3, debug11, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */       } 
/*      */       
/* 1178 */       if (this.isTall) {
/*      */         
/* 1180 */         generateBox(debug1, debug5, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1181 */         generateBox(debug1, debug5, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1182 */         generateBox(debug1, debug5, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1183 */         generateBox(debug1, debug5, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         
/* 1185 */         placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, debug5);
/* 1186 */         placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, debug5);
/* 1187 */         placeBlock(debug1, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, debug5);
/*      */         
/* 1189 */         BlockState blockState1 = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1190 */         BlockState debug12 = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */         
/* 1193 */         generateBox(debug1, debug5, 3, 6, 3, 3, 6, 11, debug12, debug12, false);
/* 1194 */         generateBox(debug1, debug5, 10, 6, 3, 10, 6, 9, debug12, debug12, false);
/* 1195 */         generateBox(debug1, debug5, 4, 6, 2, 9, 6, 2, blockState1, blockState1, false);
/* 1196 */         generateBox(debug1, debug5, 4, 6, 12, 7, 6, 12, blockState1, blockState1, false);
/*      */         
/* 1198 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, debug5);
/* 1199 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, debug5);
/* 1200 */         placeBlock(debug1, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, debug5);
/*      */         
/* 1202 */         for (int i = 0; i <= 2; i++) {
/* 1203 */           placeBlock(debug1, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 8 + i, 6, 12 - i, debug5);
/* 1204 */           if (i != 2) {
/* 1205 */             placeBlock(debug1, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 8 + i, 6, 11 - i, debug5);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1210 */         BlockState debug13 = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.SOUTH);
/* 1211 */         placeBlock(debug1, debug13, 10, 1, 13, debug5);
/* 1212 */         placeBlock(debug1, debug13, 10, 2, 13, debug5);
/* 1213 */         placeBlock(debug1, debug13, 10, 3, 13, debug5);
/* 1214 */         placeBlock(debug1, debug13, 10, 4, 13, debug5);
/* 1215 */         placeBlock(debug1, debug13, 10, 5, 13, debug5);
/* 1216 */         placeBlock(debug1, debug13, 10, 6, 13, debug5);
/* 1217 */         placeBlock(debug1, debug13, 10, 7, 13, debug5);
/*      */ 
/*      */         
/* 1220 */         int debug14 = 7;
/* 1221 */         int debug15 = 7;
/* 1222 */         BlockState debug16 = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1223 */         placeBlock(debug1, debug16, 6, 9, 7, debug5);
/* 1224 */         BlockState debug17 = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 1225 */         placeBlock(debug1, debug17, 7, 9, 7, debug5);
/*      */         
/* 1227 */         placeBlock(debug1, debug16, 6, 8, 7, debug5);
/* 1228 */         placeBlock(debug1, debug17, 7, 8, 7, debug5);
/*      */         
/* 1230 */         BlockState debug18 = (BlockState)((BlockState)debug12.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */         
/* 1232 */         placeBlock(debug1, debug18, 6, 7, 7, debug5);
/* 1233 */         placeBlock(debug1, debug18, 7, 7, 7, debug5);
/*      */         
/* 1235 */         placeBlock(debug1, debug16, 5, 7, 7, debug5);
/*      */         
/* 1237 */         placeBlock(debug1, debug17, 8, 7, 7, debug5);
/*      */         
/* 1239 */         placeBlock(debug1, (BlockState)debug16.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, debug5);
/* 1240 */         placeBlock(debug1, (BlockState)debug16.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, debug5);
/*      */         
/* 1242 */         placeBlock(debug1, (BlockState)debug17.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, debug5);
/* 1243 */         placeBlock(debug1, (BlockState)debug17.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, debug5);
/*      */         
/* 1245 */         BlockState debug19 = Blocks.TORCH.defaultBlockState();
/* 1246 */         placeBlock(debug1, debug19, 5, 8, 7, debug5);
/* 1247 */         placeBlock(debug1, debug19, 8, 8, 7, debug5);
/* 1248 */         placeBlock(debug1, debug19, 6, 8, 6, debug5);
/* 1249 */         placeBlock(debug1, debug19, 6, 8, 8, debug5);
/* 1250 */         placeBlock(debug1, debug19, 7, 8, 6, debug5);
/* 1251 */         placeBlock(debug1, debug19, 7, 8, 8, debug5);
/*      */       } 
/*      */ 
/*      */       
/* 1255 */       createChest(debug1, debug5, debug4, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1256 */       if (this.isTall) {
/* 1257 */         placeBlock(debug1, CAVE_AIR, 12, 9, 1, debug5);
/* 1258 */         createChest(debug1, debug5, debug4, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */       } 
/*      */       
/* 1261 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class FiveCrossing
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private final boolean leftLow;
/*      */     
/*      */     private final boolean leftHigh;
/*      */     private final boolean rightLow;
/*      */     private final boolean rightHigh;
/*      */     
/*      */     public FiveCrossing(int debug1, Random debug2, BoundingBox debug3, Direction debug4) {
/* 1276 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, debug1);
/*      */       
/* 1278 */       setOrientation(debug4);
/* 1279 */       this.entryDoor = randomSmallDoor(debug2);
/* 1280 */       this.boundingBox = debug3;
/*      */       
/* 1282 */       this.leftLow = debug2.nextBoolean();
/* 1283 */       this.leftHigh = debug2.nextBoolean();
/* 1284 */       this.rightLow = debug2.nextBoolean();
/* 1285 */       this.rightHigh = (debug2.nextInt(3) > 0);
/*      */     }
/*      */     
/*      */     public FiveCrossing(StructureManager debug1, CompoundTag debug2) {
/* 1289 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, debug2);
/* 1290 */       this.leftLow = debug2.getBoolean("leftLow");
/* 1291 */       this.leftHigh = debug2.getBoolean("leftHigh");
/* 1292 */       this.rightLow = debug2.getBoolean("rightLow");
/* 1293 */       this.rightHigh = debug2.getBoolean("rightHigh");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 1298 */       super.addAdditionalSaveData(debug1);
/* 1299 */       debug1.putBoolean("leftLow", this.leftLow);
/* 1300 */       debug1.putBoolean("leftHigh", this.leftHigh);
/* 1301 */       debug1.putBoolean("rightLow", this.rightLow);
/* 1302 */       debug1.putBoolean("rightHigh", this.rightHigh);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1307 */       int debug4 = 3;
/* 1308 */       int debug5 = 5;
/*      */       
/* 1310 */       Direction debug6 = getOrientation();
/* 1311 */       if (debug6 == Direction.WEST || debug6 == Direction.NORTH) {
/* 1312 */         debug4 = 8 - debug4;
/* 1313 */         debug5 = 8 - debug5;
/*      */       } 
/*      */       
/* 1316 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)debug1, debug2, debug3, 5, 1);
/* 1317 */       if (this.leftLow) {
/* 1318 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, debug4, 1);
/*      */       }
/* 1320 */       if (this.leftHigh) {
/* 1321 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)debug1, debug2, debug3, debug5, 7);
/*      */       }
/* 1323 */       if (this.rightLow) {
/* 1324 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, debug4, 1);
/*      */       }
/* 1326 */       if (this.rightHigh) {
/* 1327 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)debug1, debug2, debug3, debug5, 7);
/*      */       }
/*      */     }
/*      */     
/*      */     public static FiveCrossing createPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5, int debug6) {
/* 1332 */       BoundingBox debug7 = BoundingBox.orientBox(debug2, debug3, debug4, -4, -3, 0, 10, 9, 11, debug5);
/*      */       
/* 1334 */       if (!isOkBox(debug7) || StructurePiece.findCollisionPiece(debug0, debug7) != null) {
/* 1335 */         return null;
/*      */       }
/*      */       
/* 1338 */       return new FiveCrossing(debug6, debug1, debug7, debug5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1344 */       generateBox(debug1, debug5, 0, 0, 0, 9, 8, 10, true, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1346 */       generateSmallDoor(debug1, debug4, debug5, this.entryDoor, 4, 3, 0);
/*      */ 
/*      */       
/* 1349 */       if (this.leftLow) {
/* 1350 */         generateBox(debug1, debug5, 0, 3, 1, 0, 5, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1352 */       if (this.rightLow) {
/* 1353 */         generateBox(debug1, debug5, 9, 3, 1, 9, 5, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1355 */       if (this.leftHigh) {
/* 1356 */         generateBox(debug1, debug5, 0, 5, 7, 0, 7, 9, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1358 */       if (this.rightHigh) {
/* 1359 */         generateBox(debug1, debug5, 9, 5, 7, 9, 7, 9, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1361 */       generateBox(debug1, debug5, 5, 1, 10, 7, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*      */ 
/*      */       
/* 1364 */       generateBox(debug1, debug5, 1, 2, 1, 8, 2, 6, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1366 */       generateBox(debug1, debug5, 4, 1, 5, 4, 4, 9, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1367 */       generateBox(debug1, debug5, 8, 1, 5, 8, 4, 9, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1369 */       generateBox(debug1, debug5, 1, 4, 7, 3, 4, 9, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1372 */       generateBox(debug1, debug5, 1, 3, 5, 3, 3, 6, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1373 */       generateBox(debug1, debug5, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1374 */       generateBox(debug1, debug5, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1377 */       generateBox(debug1, debug5, 5, 1, 7, 7, 1, 8, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1378 */       generateBox(debug1, debug5, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1379 */       generateBox(debug1, debug5, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1382 */       generateBox(debug1, debug5, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1383 */       generateBox(debug1, debug5, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1384 */       generateBox(debug1, debug5, 5, 5, 7, 7, 5, 9, (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), false);
/* 1385 */       placeBlock(debug1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 6, 5, 6, debug5);
/*      */       
/* 1387 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class PortalRoom
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private boolean hasPlacedSpawner;
/*      */ 
/*      */     
/*      */     public PortalRoom(int debug1, BoundingBox debug2, Direction debug3) {
/* 1399 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, debug1);
/*      */       
/* 1401 */       setOrientation(debug3);
/* 1402 */       this.boundingBox = debug2;
/*      */     }
/*      */     
/*      */     public PortalRoom(StructureManager debug1, CompoundTag debug2) {
/* 1406 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, debug2);
/* 1407 */       this.hasPlacedSpawner = debug2.getBoolean("Mob");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 1412 */       super.addAdditionalSaveData(debug1);
/* 1413 */       debug1.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 1418 */       if (debug1 != null) {
/* 1419 */         ((StrongholdPieces.StartPiece)debug1).portalRoomPiece = this;
/*      */       }
/*      */     }
/*      */     
/*      */     public static PortalRoom createPiece(List<StructurePiece> debug0, int debug1, int debug2, int debug3, Direction debug4, int debug5) {
/* 1424 */       BoundingBox debug6 = BoundingBox.orientBox(debug1, debug2, debug3, -4, -1, 0, 11, 8, 16, debug4);
/*      */       
/* 1426 */       if (!isOkBox(debug6) || StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 1427 */         return null;
/*      */       }
/*      */       
/* 1430 */       return new PortalRoom(debug5, debug6, debug4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1436 */       generateBox(debug1, debug5, 0, 0, 0, 10, 7, 15, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1438 */       generateSmallDoor(debug1, debug4, debug5, StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES, 4, 1, 0);
/*      */ 
/*      */       
/* 1441 */       int debug8 = 6;
/* 1442 */       generateBox(debug1, debug5, 1, debug8, 1, 1, debug8, 14, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1443 */       generateBox(debug1, debug5, 9, debug8, 1, 9, debug8, 14, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1444 */       generateBox(debug1, debug5, 2, debug8, 1, 8, debug8, 2, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1445 */       generateBox(debug1, debug5, 2, debug8, 14, 8, debug8, 14, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1448 */       generateBox(debug1, debug5, 1, 1, 1, 2, 1, 4, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1449 */       generateBox(debug1, debug5, 8, 1, 1, 9, 1, 4, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1450 */       generateBox(debug1, debug5, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1451 */       generateBox(debug1, debug5, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1454 */       generateBox(debug1, debug5, 3, 1, 8, 7, 1, 12, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1455 */       generateBox(debug1, debug5, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1458 */       BlockState debug9 = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true));
/* 1459 */       BlockState debug10 = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)); int i;
/* 1460 */       for (i = 3; i < 14; i += 2) {
/* 1461 */         generateBox(debug1, debug5, 0, 3, i, 0, 4, i, debug9, debug9, false);
/* 1462 */         generateBox(debug1, debug5, 10, 3, i, 10, 4, i, debug9, debug9, false);
/*      */       } 
/* 1464 */       for (i = 2; i < 9; i += 2) {
/* 1465 */         generateBox(debug1, debug5, i, 3, 15, i, 4, 15, debug10, debug10, false);
/*      */       }
/*      */ 
/*      */       
/* 1469 */       BlockState debug11 = (BlockState)Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/* 1470 */       generateBox(debug1, debug5, 4, 1, 5, 6, 1, 7, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1471 */       generateBox(debug1, debug5, 4, 2, 6, 6, 2, 7, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1472 */       generateBox(debug1, debug5, 4, 3, 7, 6, 3, 7, false, debug4, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1473 */       for (int j = 4; j <= 6; j++) {
/* 1474 */         placeBlock(debug1, debug11, j, 1, 4, debug5);
/* 1475 */         placeBlock(debug1, debug11, j, 2, 5, debug5);
/* 1476 */         placeBlock(debug1, debug11, j, 3, 6, debug5);
/*      */       } 
/*      */       
/* 1479 */       BlockState debug12 = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.NORTH);
/* 1480 */       BlockState debug13 = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.SOUTH);
/* 1481 */       BlockState debug14 = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.EAST);
/* 1482 */       BlockState debug15 = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.WEST);
/*      */       
/* 1484 */       boolean debug16 = true;
/* 1485 */       boolean[] debug17 = new boolean[12];
/* 1486 */       for (int debug18 = 0; debug18 < debug17.length; debug18++) {
/* 1487 */         debug17[debug18] = (debug4.nextFloat() > 0.9F);
/* 1488 */         debug16 &= debug17[debug18];
/*      */       } 
/*      */       
/* 1491 */       placeBlock(debug1, (BlockState)debug12.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[0])), 4, 3, 8, debug5);
/* 1492 */       placeBlock(debug1, (BlockState)debug12.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[1])), 5, 3, 8, debug5);
/* 1493 */       placeBlock(debug1, (BlockState)debug12.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[2])), 6, 3, 8, debug5);
/* 1494 */       placeBlock(debug1, (BlockState)debug13.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[3])), 4, 3, 12, debug5);
/* 1495 */       placeBlock(debug1, (BlockState)debug13.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[4])), 5, 3, 12, debug5);
/* 1496 */       placeBlock(debug1, (BlockState)debug13.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[5])), 6, 3, 12, debug5);
/* 1497 */       placeBlock(debug1, (BlockState)debug14.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[6])), 3, 3, 9, debug5);
/* 1498 */       placeBlock(debug1, (BlockState)debug14.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[7])), 3, 3, 10, debug5);
/* 1499 */       placeBlock(debug1, (BlockState)debug14.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[8])), 3, 3, 11, debug5);
/* 1500 */       placeBlock(debug1, (BlockState)debug15.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[9])), 7, 3, 9, debug5);
/* 1501 */       placeBlock(debug1, (BlockState)debug15.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[10])), 7, 3, 10, debug5);
/* 1502 */       placeBlock(debug1, (BlockState)debug15.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(debug17[11])), 7, 3, 11, debug5);
/*      */       
/* 1504 */       if (debug16) {
/* 1505 */         BlockState blockState = Blocks.END_PORTAL.defaultBlockState();
/*      */         
/* 1507 */         placeBlock(debug1, blockState, 4, 3, 9, debug5);
/* 1508 */         placeBlock(debug1, blockState, 5, 3, 9, debug5);
/* 1509 */         placeBlock(debug1, blockState, 6, 3, 9, debug5);
/* 1510 */         placeBlock(debug1, blockState, 4, 3, 10, debug5);
/* 1511 */         placeBlock(debug1, blockState, 5, 3, 10, debug5);
/* 1512 */         placeBlock(debug1, blockState, 6, 3, 10, debug5);
/* 1513 */         placeBlock(debug1, blockState, 4, 3, 11, debug5);
/* 1514 */         placeBlock(debug1, blockState, 5, 3, 11, debug5);
/* 1515 */         placeBlock(debug1, blockState, 6, 3, 11, debug5);
/*      */       } 
/*      */       
/* 1518 */       if (!this.hasPlacedSpawner) {
/* 1519 */         debug8 = getWorldY(3);
/* 1520 */         BlockPos blockPos = new BlockPos(getWorldX(5, 6), debug8, getWorldZ(5, 6));
/* 1521 */         if (debug5.isInside((Vec3i)blockPos)) {
/* 1522 */           this.hasPlacedSpawner = true;
/* 1523 */           debug1.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*      */           
/* 1525 */           BlockEntity debug19 = debug1.getBlockEntity(blockPos);
/* 1526 */           if (debug19 instanceof SpawnerBlockEntity) {
/* 1527 */             ((SpawnerBlockEntity)debug19).getSpawner().setEntityId(EntityType.SILVERFISH);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1532 */       return true;
/*      */     } }
/*      */   
/*      */   static class SmoothStoneSelector extends StructurePiece.BlockSelector {
/*      */     private SmoothStoneSelector() {}
/*      */     
/*      */     public void next(Random debug1, int debug2, int debug3, int debug4, boolean debug5) {
/* 1539 */       if (debug5) {
/* 1540 */         float debug6 = debug1.nextFloat();
/* 1541 */         if (debug6 < 0.2F) {
/* 1542 */           this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
/* 1543 */         } else if (debug6 < 0.5F) {
/* 1544 */           this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
/* 1545 */         } else if (debug6 < 0.55F) {
/* 1546 */           this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
/*      */         } else {
/* 1548 */           this.next = Blocks.STONE_BRICKS.defaultBlockState();
/*      */         } 
/*      */       } else {
/* 1551 */         this.next = Blocks.CAVE_AIR.defaultBlockState();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/* 1556 */   private static final SmoothStoneSelector SMOOTH_STONE_SELECTOR = new SmoothStoneSelector();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StrongholdPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */