/*      */ package net.minecraft.world.level.levelgen.structure;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.monster.ElderGuardian;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.StructureFeatureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ 
/*      */ public class OceanMonumentPieces {
/*      */   public static abstract class OceanMonumentPiece extends StructurePiece {
/*   31 */     protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
/*   32 */     protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
/*   33 */     protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
/*      */     
/*   35 */     protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
/*      */     
/*   37 */     protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
/*      */ 
/*      */     
/*   40 */     protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
/*   41 */     protected static final Set<Block> FILL_KEEP = (Set<Block>)ImmutableSet.builder()
/*   42 */       .add(Blocks.ICE)
/*   43 */       .add(Blocks.PACKED_ICE)
/*   44 */       .add(Blocks.BLUE_ICE)
/*   45 */       .add(FILL_BLOCK.getBlock())
/*   46 */       .build();
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
/*   57 */     protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
/*   58 */     protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
/*   59 */     protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
/*   60 */     protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
/*      */ 
/*      */ 
/*      */     
/*      */     protected OceanMonumentPieces.RoomDefinition roomDefinition;
/*      */ 
/*      */ 
/*      */     
/*      */     protected static final int getRoomIndex(int debug0, int debug1, int debug2) {
/*   69 */       return debug1 * 25 + debug2 * 5 + debug0;
/*      */     }
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
/*      */     public OceanMonumentPiece(StructurePieceType debug1, int debug2) {
/*   87 */       super(debug1, debug2);
/*      */     }
/*      */     
/*      */     public OceanMonumentPiece(StructurePieceType debug1, Direction debug2, BoundingBox debug3) {
/*   91 */       super(debug1, 1);
/*   92 */       setOrientation(debug2);
/*   93 */       this.boundingBox = debug3;
/*      */     }
/*      */     
/*      */     protected OceanMonumentPiece(StructurePieceType debug1, int debug2, Direction debug3, OceanMonumentPieces.RoomDefinition debug4, int debug5, int debug6, int debug7) {
/*   97 */       super(debug1, debug2);
/*   98 */       setOrientation(debug3);
/*   99 */       this.roomDefinition = debug4;
/*      */       
/*  101 */       int debug8 = debug4.index;
/*  102 */       int debug9 = debug8 % 5;
/*  103 */       int debug10 = debug8 / 5 % 5;
/*  104 */       int debug11 = debug8 / 25;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  109 */       if (debug3 == Direction.NORTH || debug3 == Direction.SOUTH) {
/*  110 */         this.boundingBox = new BoundingBox(0, 0, 0, debug5 * 8 - 1, debug6 * 4 - 1, debug7 * 8 - 1);
/*      */       } else {
/*      */         
/*  113 */         this.boundingBox = new BoundingBox(0, 0, 0, debug7 * 8 - 1, debug6 * 4 - 1, debug5 * 8 - 1);
/*      */       } 
/*      */       
/*  116 */       switch (debug3) {
/*      */         case NORTH:
/*  118 */           this.boundingBox.move(debug9 * 8, debug11 * 4, -(debug10 + debug7) * 8 + 1);
/*      */           return;
/*      */         
/*      */         case SOUTH:
/*  122 */           this.boundingBox.move(debug9 * 8, debug11 * 4, debug10 * 8);
/*      */           return;
/*      */         case WEST:
/*  125 */           this.boundingBox.move(-(debug10 + debug7) * 8 + 1, debug11 * 4, debug9 * 8);
/*      */           return;
/*      */       } 
/*  128 */       this.boundingBox.move(debug10 * 8, debug11 * 4, debug9 * 8);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPiece(StructurePieceType debug1, CompoundTag debug2) {
/*  134 */       super(debug1, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {}
/*      */ 
/*      */     
/*      */     protected void generateWaterBox(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8) {
/*  142 */       for (int debug9 = debug4; debug9 <= debug7; debug9++) {
/*  143 */         for (int debug10 = debug3; debug10 <= debug6; debug10++) {
/*  144 */           for (int debug11 = debug5; debug11 <= debug8; debug11++) {
/*  145 */             BlockState debug12 = getBlock((BlockGetter)debug1, debug10, debug9, debug11, debug2);
/*  146 */             if (!FILL_KEEP.contains(debug12.getBlock())) {
/*  147 */               if (getWorldY(debug9) >= debug1.getSeaLevel() && debug12 != FILL_BLOCK) {
/*  148 */                 placeBlock(debug1, Blocks.AIR.defaultBlockState(), debug10, debug9, debug11, debug2);
/*      */               } else {
/*  150 */                 placeBlock(debug1, FILL_BLOCK, debug10, debug9, debug11, debug2);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateDefaultFloor(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, boolean debug5) {
/*  159 */       if (debug5) {
/*  160 */         generateBox(debug1, debug2, debug3 + 0, 0, debug4 + 0, debug3 + 2, 0, debug4 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  161 */         generateBox(debug1, debug2, debug3 + 5, 0, debug4 + 0, debug3 + 8 - 1, 0, debug4 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  162 */         generateBox(debug1, debug2, debug3 + 3, 0, debug4 + 0, debug3 + 4, 0, debug4 + 2, BASE_GRAY, BASE_GRAY, false);
/*  163 */         generateBox(debug1, debug2, debug3 + 3, 0, debug4 + 5, debug3 + 4, 0, debug4 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  165 */         generateBox(debug1, debug2, debug3 + 3, 0, debug4 + 2, debug3 + 4, 0, debug4 + 2, BASE_LIGHT, BASE_LIGHT, false);
/*  166 */         generateBox(debug1, debug2, debug3 + 3, 0, debug4 + 5, debug3 + 4, 0, debug4 + 5, BASE_LIGHT, BASE_LIGHT, false);
/*  167 */         generateBox(debug1, debug2, debug3 + 2, 0, debug4 + 3, debug3 + 2, 0, debug4 + 4, BASE_LIGHT, BASE_LIGHT, false);
/*  168 */         generateBox(debug1, debug2, debug3 + 5, 0, debug4 + 3, debug3 + 5, 0, debug4 + 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  170 */         generateBox(debug1, debug2, debug3 + 0, 0, debug4 + 0, debug3 + 8 - 1, 0, debug4 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateBoxOnFillOnly(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BlockState debug9) {
/*  175 */       for (int debug10 = debug4; debug10 <= debug7; debug10++) {
/*  176 */         for (int debug11 = debug3; debug11 <= debug6; debug11++) {
/*  177 */           for (int debug12 = debug5; debug12 <= debug8; debug12++) {
/*  178 */             if (getBlock((BlockGetter)debug1, debug11, debug10, debug12, debug2) == FILL_BLOCK)
/*      */             {
/*      */               
/*  181 */               placeBlock(debug1, debug9, debug11, debug10, debug12, debug2); } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected boolean chunkIntersects(BoundingBox debug1, int debug2, int debug3, int debug4, int debug5) {
/*  188 */       int debug6 = getWorldX(debug2, debug3);
/*  189 */       int debug7 = getWorldZ(debug2, debug3);
/*  190 */       int debug8 = getWorldX(debug4, debug5);
/*  191 */       int debug9 = getWorldZ(debug4, debug5);
/*  192 */       return debug1.intersects(Math.min(debug6, debug8), Math.min(debug7, debug9), Math.max(debug6, debug8), Math.max(debug7, debug9));
/*      */     }
/*      */     
/*      */     protected boolean spawnElder(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5) {
/*  196 */       int debug6 = getWorldX(debug3, debug5);
/*  197 */       int debug7 = getWorldY(debug4);
/*  198 */       int debug8 = getWorldZ(debug3, debug5);
/*      */       
/*  200 */       if (debug2.isInside((Vec3i)new BlockPos(debug6, debug7, debug8))) {
/*  201 */         ElderGuardian debug9 = (ElderGuardian)EntityType.ELDER_GUARDIAN.create((Level)debug1.getLevel());
/*  202 */         debug9.heal(debug9.getMaxHealth());
/*  203 */         debug9.moveTo(debug6 + 0.5D, debug7, debug8 + 0.5D, 0.0F, 0.0F);
/*  204 */         debug9.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug9.blockPosition()), MobSpawnType.STRUCTURE, null, null);
/*  205 */         debug1.addFreshEntityWithPassengers((Entity)debug9);
/*  206 */         return true;
/*      */       } 
/*  208 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class MonumentBuilding
/*      */     extends OceanMonumentPiece
/*      */   {
/*      */     private OceanMonumentPieces.RoomDefinition sourceRoom;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private OceanMonumentPieces.RoomDefinition coreRoom;
/*      */ 
/*      */ 
/*      */     
/*  228 */     private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();
/*      */     
/*      */     public MonumentBuilding(Random debug1, int debug2, int debug3, Direction debug4) {
/*  231 */       super(StructurePieceType.OCEAN_MONUMENT_BUILDING, 0);
/*      */       
/*  233 */       setOrientation(debug4);
/*      */       
/*  235 */       Direction debug5 = getOrientation();
/*  236 */       if (debug5.getAxis() == Direction.Axis.Z) {
/*  237 */         this.boundingBox = new BoundingBox(debug2, 39, debug3, debug2 + 58 - 1, 61, debug3 + 58 - 1);
/*      */       } else {
/*  239 */         this.boundingBox = new BoundingBox(debug2, 39, debug3, debug2 + 58 - 1, 61, debug3 + 58 - 1);
/*      */       } 
/*      */       
/*  242 */       List<OceanMonumentPieces.RoomDefinition> debug6 = generateRoomGraph(debug1);
/*      */       
/*  244 */       this.sourceRoom.claimed = true;
/*  245 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(debug5, this.sourceRoom));
/*  246 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(debug5, this.coreRoom));
/*      */       
/*  248 */       List<OceanMonumentPieces.MonumentRoomFitter> debug7 = Lists.newArrayList();
/*  249 */       debug7.add(new OceanMonumentPieces.FitDoubleXYRoom());
/*  250 */       debug7.add(new OceanMonumentPieces.FitDoubleYZRoom());
/*  251 */       debug7.add(new OceanMonumentPieces.FitDoubleZRoom());
/*  252 */       debug7.add(new OceanMonumentPieces.FitDoubleXRoom());
/*  253 */       debug7.add(new OceanMonumentPieces.FitDoubleYRoom());
/*  254 */       debug7.add(new OceanMonumentPieces.FitSimpleTopRoom());
/*  255 */       debug7.add(new OceanMonumentPieces.FitSimpleRoom());
/*      */       
/*  257 */       for (OceanMonumentPieces.RoomDefinition roomDefinition : debug6) {
/*  258 */         if (!roomDefinition.claimed && !roomDefinition.isSpecial())
/*      */         {
/*  260 */           for (OceanMonumentPieces.MonumentRoomFitter monumentRoomFitter : debug7) {
/*  261 */             if (monumentRoomFitter.fits(roomDefinition)) {
/*  262 */               this.childPieces.add(monumentRoomFitter.create(debug5, roomDefinition, debug1));
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  270 */       int debug8 = this.boundingBox.y0;
/*  271 */       int debug9 = getWorldX(9, 22);
/*  272 */       int debug10 = getWorldZ(9, 22);
/*  273 */       for (OceanMonumentPieces.OceanMonumentPiece oceanMonumentPiece : this.childPieces) {
/*  274 */         oceanMonumentPiece.getBoundingBox().move(debug9, debug8, debug10);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  279 */       BoundingBox debug11 = BoundingBox.createProper(getWorldX(1, 1), getWorldY(1), getWorldZ(1, 1), getWorldX(23, 21), getWorldY(8), getWorldZ(23, 21));
/*  280 */       BoundingBox debug12 = BoundingBox.createProper(getWorldX(34, 1), getWorldY(1), getWorldZ(34, 1), getWorldX(56, 21), getWorldY(8), getWorldZ(56, 21));
/*  281 */       BoundingBox debug13 = BoundingBox.createProper(getWorldX(22, 22), getWorldY(13), getWorldZ(22, 22), getWorldX(35, 35), getWorldY(17), getWorldZ(35, 35));
/*      */ 
/*      */       
/*  284 */       int debug14 = debug1.nextInt();
/*  285 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(debug5, debug11, debug14++));
/*  286 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(debug5, debug12, debug14++));
/*      */       
/*  288 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(debug5, debug13));
/*      */     }
/*      */ 
/*      */     
/*      */     public MonumentBuilding(StructureManager debug1, CompoundTag debug2) {
/*  293 */       super(StructurePieceType.OCEAN_MONUMENT_BUILDING, debug2);
/*      */     }
/*      */     
/*      */     private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(Random debug1) {
/*  297 */       OceanMonumentPieces.RoomDefinition[] debug2 = new OceanMonumentPieces.RoomDefinition[75];
/*      */       int i;
/*  299 */       for (i = 0; i < 5; i++) {
/*  300 */         for (int j = 0; j < 4; j++) {
/*  301 */           int k = 0;
/*  302 */           int m = getRoomIndex(i, 0, j);
/*  303 */           debug2[m] = new OceanMonumentPieces.RoomDefinition(m);
/*      */         } 
/*      */       } 
/*  306 */       for (i = 0; i < 5; i++) {
/*  307 */         for (int j = 0; j < 4; j++) {
/*  308 */           int k = 1;
/*  309 */           int m = getRoomIndex(i, 1, j);
/*  310 */           debug2[m] = new OceanMonumentPieces.RoomDefinition(m);
/*      */         } 
/*      */       } 
/*  313 */       for (i = 1; i < 4; i++) {
/*  314 */         for (int j = 0; j < 2; j++) {
/*  315 */           int k = 2;
/*  316 */           int m = getRoomIndex(i, 2, j);
/*  317 */           debug2[m] = new OceanMonumentPieces.RoomDefinition(m);
/*      */         } 
/*      */       } 
/*      */       
/*  321 */       this.sourceRoom = debug2[GRIDROOM_SOURCE_INDEX];
/*      */       
/*  323 */       for (i = 0; i < 5; i++) {
/*  324 */         for (int j = 0; j < 5; j++) {
/*  325 */           for (int k = 0; k < 3; k++) {
/*  326 */             int m = getRoomIndex(i, k, j);
/*  327 */             if (debug2[m] != null)
/*      */             {
/*      */               
/*  330 */               for (Direction debug10 : Direction.values()) {
/*  331 */                 int debug11 = i + debug10.getStepX();
/*  332 */                 int debug12 = k + debug10.getStepY();
/*  333 */                 int debug13 = j + debug10.getStepZ();
/*  334 */                 if (debug11 >= 0 && debug11 < 5 && debug13 >= 0 && debug13 < 5 && debug12 >= 0 && debug12 < 3) {
/*  335 */                   int debug14 = getRoomIndex(debug11, debug12, debug13);
/*  336 */                   if (debug2[debug14] != null)
/*      */                   {
/*      */                     
/*  339 */                     if (debug13 == j) {
/*  340 */                       debug2[m].setConnection(debug10, debug2[debug14]);
/*      */                     } else {
/*  342 */                       debug2[m].setConnection(debug10.getOpposite(), debug2[debug14]);
/*      */                     }  } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  350 */       OceanMonumentPieces.RoomDefinition debug3 = new OceanMonumentPieces.RoomDefinition(1003);
/*  351 */       OceanMonumentPieces.RoomDefinition debug4 = new OceanMonumentPieces.RoomDefinition(1001);
/*  352 */       OceanMonumentPieces.RoomDefinition debug5 = new OceanMonumentPieces.RoomDefinition(1002);
/*  353 */       debug2[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, debug3);
/*  354 */       debug2[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, debug4);
/*  355 */       debug2[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, debug5);
/*  356 */       debug3.claimed = true;
/*  357 */       debug4.claimed = true;
/*  358 */       debug5.claimed = true;
/*  359 */       this.sourceRoom.isSource = true;
/*      */ 
/*      */       
/*  362 */       this.coreRoom = debug2[getRoomIndex(debug1.nextInt(4), 0, 2)];
/*  363 */       this.coreRoom.claimed = true;
/*  364 */       (this.coreRoom.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/*  365 */       (this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  366 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  367 */       (this.coreRoom.connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  368 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  369 */       ((this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  370 */       (((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*      */       
/*  372 */       List<OceanMonumentPieces.RoomDefinition> debug6 = Lists.newArrayList();
/*  373 */       for (OceanMonumentPieces.RoomDefinition debug10 : debug2) {
/*  374 */         if (debug10 != null) {
/*  375 */           debug10.updateOpenings();
/*  376 */           debug6.add(debug10);
/*      */         } 
/*      */       } 
/*  379 */       debug3.updateOpenings();
/*      */       
/*  381 */       Collections.shuffle(debug6, debug1);
/*  382 */       int debug7 = 1;
/*  383 */       for (OceanMonumentPieces.RoomDefinition debug9 : debug6) {
/*      */         
/*  385 */         int debug10 = 0;
/*  386 */         int debug11 = 0;
/*  387 */         while (debug10 < 2 && debug11 < 5) {
/*  388 */           debug11++;
/*      */           
/*  390 */           int debug12 = debug1.nextInt(6);
/*  391 */           if (debug9.hasOpening[debug12]) {
/*  392 */             int debug13 = Direction.from3DDataValue(debug12).getOpposite().get3DDataValue();
/*      */ 
/*      */             
/*  395 */             debug9.hasOpening[debug12] = false;
/*  396 */             (debug9.connections[debug12]).hasOpening[debug13] = false;
/*      */             
/*  398 */             if (debug9.findSource(debug7++) && debug9.connections[debug12].findSource(debug7++)) {
/*  399 */               debug10++;
/*      */               continue;
/*      */             } 
/*  402 */             debug9.hasOpening[debug12] = true;
/*  403 */             (debug9.connections[debug12]).hasOpening[debug13] = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  408 */       debug6.add(debug3);
/*  409 */       debug6.add(debug4);
/*  410 */       debug6.add(debug5);
/*      */       
/*  412 */       return debug6;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  417 */       int debug8 = Math.max(debug1.getSeaLevel(), 64) - this.boundingBox.y0;
/*      */       
/*  419 */       generateWaterBox(debug1, debug5, 0, 0, 0, 58, debug8, 58);
/*      */ 
/*      */       
/*  422 */       generateWing(false, 0, debug1, debug4, debug5);
/*      */ 
/*      */       
/*  425 */       generateWing(true, 33, debug1, debug4, debug5);
/*      */ 
/*      */       
/*  428 */       generateEntranceArchs(debug1, debug4, debug5);
/*      */       
/*  430 */       generateEntranceWall(debug1, debug4, debug5);
/*  431 */       generateRoofPiece(debug1, debug4, debug5);
/*      */       
/*  433 */       generateLowerWall(debug1, debug4, debug5);
/*  434 */       generateMiddleWall(debug1, debug4, debug5);
/*  435 */       generateUpperWall(debug1, debug4, debug5);
/*      */       
/*      */       int debug9;
/*  438 */       for (debug9 = 0; debug9 < 7; debug9++) {
/*  439 */         for (int debug10 = 0; debug10 < 7; ) {
/*  440 */           if (debug10 == 0 && debug9 == 3)
/*      */           {
/*  442 */             debug10 = 6;
/*      */           }
/*      */           
/*  445 */           int debug11 = debug9 * 9;
/*  446 */           int debug12 = debug10 * 9;
/*  447 */           for (int debug13 = 0; debug13 < 4; debug13++) {
/*  448 */             for (int debug14 = 0; debug14 < 4; debug14++) {
/*  449 */               placeBlock(debug1, BASE_LIGHT, debug11 + debug13, 0, debug12 + debug14, debug5);
/*  450 */               fillColumnDown(debug1, BASE_LIGHT, debug11 + debug13, -1, debug12 + debug14, debug5);
/*      */             } 
/*      */           } 
/*      */           
/*  454 */           if (debug9 == 0 || debug9 == 6) {
/*  455 */             debug10++; continue;
/*      */           } 
/*  457 */           debug10 += 6;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  463 */       for (debug9 = 0; debug9 < 5; debug9++) {
/*  464 */         generateWaterBox(debug1, debug5, -1 - debug9, 0 + debug9 * 2, -1 - debug9, -1 - debug9, 23, 58 + debug9);
/*  465 */         generateWaterBox(debug1, debug5, 58 + debug9, 0 + debug9 * 2, -1 - debug9, 58 + debug9, 23, 58 + debug9);
/*  466 */         generateWaterBox(debug1, debug5, 0 - debug9, 0 + debug9 * 2, -1 - debug9, 57 + debug9, 23, -1 - debug9);
/*  467 */         generateWaterBox(debug1, debug5, 0 - debug9, 0 + debug9 * 2, 58 + debug9, 57 + debug9, 23, 58 + debug9);
/*      */       } 
/*      */       
/*  470 */       for (OceanMonumentPieces.OceanMonumentPiece debug10 : this.childPieces) {
/*  471 */         if (debug10.getBoundingBox().intersects(debug5)) {
/*  472 */           debug10.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*      */         }
/*      */       } 
/*      */       
/*  476 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateWing(boolean debug1, int debug2, WorldGenLevel debug3, Random debug4, BoundingBox debug5) {
/*  481 */       int debug6 = 24;
/*  482 */       if (chunkIntersects(debug5, debug2, 0, debug2 + 23, 20)) {
/*  483 */         generateBox(debug3, debug5, debug2 + 0, 0, 0, debug2 + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  485 */         generateWaterBox(debug3, debug5, debug2 + 0, 1, 0, debug2 + 24, 10, 20);
/*      */         int debug7;
/*  487 */         for (debug7 = 0; debug7 < 4; debug7++) {
/*  488 */           generateBox(debug3, debug5, debug2 + debug7, debug7 + 1, debug7, debug2 + debug7, debug7 + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  489 */           generateBox(debug3, debug5, debug2 + debug7 + 7, debug7 + 5, debug7 + 7, debug2 + debug7 + 7, debug7 + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  490 */           generateBox(debug3, debug5, debug2 + 17 - debug7, debug7 + 5, debug7 + 7, debug2 + 17 - debug7, debug7 + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  491 */           generateBox(debug3, debug5, debug2 + 24 - debug7, debug7 + 1, debug7, debug2 + 24 - debug7, debug7 + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */           
/*  493 */           generateBox(debug3, debug5, debug2 + debug7 + 1, debug7 + 1, debug7, debug2 + 23 - debug7, debug7 + 1, debug7, BASE_LIGHT, BASE_LIGHT, false);
/*  494 */           generateBox(debug3, debug5, debug2 + debug7 + 8, debug7 + 5, debug7 + 7, debug2 + 16 - debug7, debug7 + 5, debug7 + 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  496 */         generateBox(debug3, debug5, debug2 + 4, 4, 4, debug2 + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  497 */         generateBox(debug3, debug5, debug2 + 7, 4, 4, debug2 + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
/*  498 */         generateBox(debug3, debug5, debug2 + 18, 4, 4, debug2 + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  499 */         generateBox(debug3, debug5, debug2 + 11, 8, 11, debug2 + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
/*  500 */         placeBlock(debug3, DOT_DECO_DATA, debug2 + 12, 9, 12, debug5);
/*  501 */         placeBlock(debug3, DOT_DECO_DATA, debug2 + 12, 9, 15, debug5);
/*  502 */         placeBlock(debug3, DOT_DECO_DATA, debug2 + 12, 9, 18, debug5);
/*      */         
/*  504 */         debug7 = debug2 + (debug1 ? 19 : 5);
/*  505 */         int debug8 = debug2 + (debug1 ? 5 : 19); int debug9;
/*  506 */         for (debug9 = 20; debug9 >= 5; debug9 -= 3) {
/*  507 */           placeBlock(debug3, DOT_DECO_DATA, debug7, 5, debug9, debug5);
/*      */         }
/*  509 */         for (debug9 = 19; debug9 >= 7; debug9 -= 3) {
/*  510 */           placeBlock(debug3, DOT_DECO_DATA, debug8, 5, debug9, debug5);
/*      */         }
/*  512 */         for (debug9 = 0; debug9 < 4; debug9++) {
/*  513 */           int debug10 = debug1 ? (debug2 + 24 - 17 - debug9 * 3) : (debug2 + 17 - debug9 * 3);
/*  514 */           placeBlock(debug3, DOT_DECO_DATA, debug10, 5, 5, debug5);
/*      */         } 
/*  516 */         placeBlock(debug3, DOT_DECO_DATA, debug8, 5, 5, debug5);
/*      */ 
/*      */         
/*  519 */         generateBox(debug3, debug5, debug2 + 11, 1, 12, debug2 + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
/*  520 */         generateBox(debug3, debug5, debug2 + 12, 1, 11, debug2 + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateEntranceArchs(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  526 */       if (chunkIntersects(debug3, 22, 5, 35, 17)) {
/*      */         
/*  528 */         generateWaterBox(debug1, debug3, 25, 0, 0, 32, 8, 20);
/*      */ 
/*      */         
/*  531 */         for (int debug4 = 0; debug4 < 4; debug4++) {
/*  532 */           generateBox(debug1, debug3, 24, 2, 5 + debug4 * 4, 24, 4, 5 + debug4 * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  533 */           generateBox(debug1, debug3, 22, 4, 5 + debug4 * 4, 23, 4, 5 + debug4 * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  534 */           placeBlock(debug1, BASE_LIGHT, 25, 5, 5 + debug4 * 4, debug3);
/*  535 */           placeBlock(debug1, BASE_LIGHT, 26, 6, 5 + debug4 * 4, debug3);
/*  536 */           placeBlock(debug1, LAMP_BLOCK, 26, 5, 5 + debug4 * 4, debug3);
/*      */           
/*  538 */           generateBox(debug1, debug3, 33, 2, 5 + debug4 * 4, 33, 4, 5 + debug4 * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  539 */           generateBox(debug1, debug3, 34, 4, 5 + debug4 * 4, 35, 4, 5 + debug4 * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  540 */           placeBlock(debug1, BASE_LIGHT, 32, 5, 5 + debug4 * 4, debug3);
/*  541 */           placeBlock(debug1, BASE_LIGHT, 31, 6, 5 + debug4 * 4, debug3);
/*  542 */           placeBlock(debug1, LAMP_BLOCK, 31, 5, 5 + debug4 * 4, debug3);
/*      */           
/*  544 */           generateBox(debug1, debug3, 27, 6, 5 + debug4 * 4, 30, 6, 5 + debug4 * 4, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateEntranceWall(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  552 */       if (chunkIntersects(debug3, 15, 20, 42, 21)) {
/*  553 */         generateBox(debug1, debug3, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  555 */         generateWaterBox(debug1, debug3, 26, 1, 21, 31, 3, 21);
/*      */ 
/*      */ 
/*      */         
/*  559 */         generateBox(debug1, debug3, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
/*  560 */         generateBox(debug1, debug3, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
/*  561 */         generateBox(debug1, debug3, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
/*  562 */         generateBox(debug1, debug3, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
/*  563 */         generateBox(debug1, debug3, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
/*  564 */         generateBox(debug1, debug3, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
/*  565 */         generateBox(debug1, debug3, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
/*  566 */         generateBox(debug1, debug3, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  567 */         generateBox(debug1, debug3, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  568 */         generateBox(debug1, debug3, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*  569 */         generateBox(debug1, debug3, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */         
/*  572 */         generateBox(debug1, debug3, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*  573 */         placeBlock(debug1, BASE_LIGHT, 27, 3, 21, debug3);
/*  574 */         placeBlock(debug1, BASE_LIGHT, 30, 3, 21, debug3);
/*  575 */         placeBlock(debug1, BASE_LIGHT, 26, 2, 21, debug3);
/*  576 */         placeBlock(debug1, BASE_LIGHT, 31, 2, 21, debug3);
/*  577 */         placeBlock(debug1, BASE_LIGHT, 25, 1, 21, debug3);
/*  578 */         placeBlock(debug1, BASE_LIGHT, 32, 1, 21, debug3); int debug4;
/*  579 */         for (debug4 = 0; debug4 < 7; debug4++) {
/*  580 */           placeBlock(debug1, BASE_BLACK, 28 - debug4, 6 + debug4, 21, debug3);
/*  581 */           placeBlock(debug1, BASE_BLACK, 29 + debug4, 6 + debug4, 21, debug3);
/*      */         } 
/*  583 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  584 */           placeBlock(debug1, BASE_BLACK, 28 - debug4, 9 + debug4, 21, debug3);
/*  585 */           placeBlock(debug1, BASE_BLACK, 29 + debug4, 9 + debug4, 21, debug3);
/*      */         } 
/*  587 */         placeBlock(debug1, BASE_BLACK, 28, 12, 21, debug3);
/*  588 */         placeBlock(debug1, BASE_BLACK, 29, 12, 21, debug3);
/*  589 */         for (debug4 = 0; debug4 < 3; debug4++) {
/*  590 */           placeBlock(debug1, BASE_BLACK, 22 - debug4 * 2, 8, 21, debug3);
/*  591 */           placeBlock(debug1, BASE_BLACK, 22 - debug4 * 2, 9, 21, debug3);
/*      */           
/*  593 */           placeBlock(debug1, BASE_BLACK, 35 + debug4 * 2, 8, 21, debug3);
/*  594 */           placeBlock(debug1, BASE_BLACK, 35 + debug4 * 2, 9, 21, debug3);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  599 */         generateWaterBox(debug1, debug3, 15, 13, 21, 42, 15, 21);
/*  600 */         generateWaterBox(debug1, debug3, 15, 1, 21, 15, 6, 21);
/*  601 */         generateWaterBox(debug1, debug3, 16, 1, 21, 16, 5, 21);
/*  602 */         generateWaterBox(debug1, debug3, 17, 1, 21, 20, 4, 21);
/*  603 */         generateWaterBox(debug1, debug3, 21, 1, 21, 21, 3, 21);
/*  604 */         generateWaterBox(debug1, debug3, 22, 1, 21, 22, 2, 21);
/*  605 */         generateWaterBox(debug1, debug3, 23, 1, 21, 24, 1, 21);
/*  606 */         generateWaterBox(debug1, debug3, 42, 1, 21, 42, 6, 21);
/*  607 */         generateWaterBox(debug1, debug3, 41, 1, 21, 41, 5, 21);
/*  608 */         generateWaterBox(debug1, debug3, 37, 1, 21, 40, 4, 21);
/*  609 */         generateWaterBox(debug1, debug3, 36, 1, 21, 36, 3, 21);
/*  610 */         generateWaterBox(debug1, debug3, 33, 1, 21, 34, 1, 21);
/*  611 */         generateWaterBox(debug1, debug3, 35, 1, 21, 35, 2, 21);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateRoofPiece(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  619 */       if (chunkIntersects(debug3, 21, 21, 36, 36)) {
/*  620 */         generateBox(debug1, debug3, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */ 
/*      */         
/*  624 */         generateWaterBox(debug1, debug3, 21, 1, 22, 36, 23, 36);
/*      */ 
/*      */         
/*  627 */         for (int debug4 = 0; debug4 < 4; debug4++) {
/*  628 */           generateBox(debug1, debug3, 21 + debug4, 13 + debug4, 21 + debug4, 36 - debug4, 13 + debug4, 21 + debug4, BASE_LIGHT, BASE_LIGHT, false);
/*  629 */           generateBox(debug1, debug3, 21 + debug4, 13 + debug4, 36 - debug4, 36 - debug4, 13 + debug4, 36 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*  630 */           generateBox(debug1, debug3, 21 + debug4, 13 + debug4, 22 + debug4, 21 + debug4, 13 + debug4, 35 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*  631 */           generateBox(debug1, debug3, 36 - debug4, 13 + debug4, 22 + debug4, 36 - debug4, 13 + debug4, 35 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  633 */         generateBox(debug1, debug3, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
/*  634 */         generateBox(debug1, debug3, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  635 */         generateBox(debug1, debug3, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  636 */         generateBox(debug1, debug3, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*  637 */         generateBox(debug1, debug3, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  639 */         placeBlock(debug1, BASE_LIGHT, 26, 20, 26, debug3);
/*  640 */         placeBlock(debug1, BASE_LIGHT, 27, 21, 27, debug3);
/*  641 */         placeBlock(debug1, LAMP_BLOCK, 27, 20, 27, debug3);
/*  642 */         placeBlock(debug1, BASE_LIGHT, 26, 20, 31, debug3);
/*  643 */         placeBlock(debug1, BASE_LIGHT, 27, 21, 30, debug3);
/*  644 */         placeBlock(debug1, LAMP_BLOCK, 27, 20, 30, debug3);
/*  645 */         placeBlock(debug1, BASE_LIGHT, 31, 20, 31, debug3);
/*  646 */         placeBlock(debug1, BASE_LIGHT, 30, 21, 30, debug3);
/*  647 */         placeBlock(debug1, LAMP_BLOCK, 30, 20, 30, debug3);
/*  648 */         placeBlock(debug1, BASE_LIGHT, 31, 20, 26, debug3);
/*  649 */         placeBlock(debug1, BASE_LIGHT, 30, 21, 27, debug3);
/*  650 */         placeBlock(debug1, LAMP_BLOCK, 30, 20, 27, debug3);
/*      */         
/*  652 */         generateBox(debug1, debug3, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
/*  653 */         generateBox(debug1, debug3, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*  654 */         generateBox(debug1, debug3, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
/*  655 */         generateBox(debug1, debug3, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateLowerWall(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  662 */       if (chunkIntersects(debug3, 0, 21, 6, 58)) {
/*  663 */         generateBox(debug1, debug3, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  665 */         generateWaterBox(debug1, debug3, 0, 1, 21, 6, 7, 57);
/*      */ 
/*      */         
/*  668 */         generateBox(debug1, debug3, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false); int debug4;
/*  669 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  670 */           generateBox(debug1, debug3, debug4, debug4 + 1, 21, debug4, debug4 + 1, 57 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  672 */         for (debug4 = 23; debug4 < 53; debug4 += 3) {
/*  673 */           placeBlock(debug1, DOT_DECO_DATA, 5, 5, debug4, debug3);
/*      */         }
/*  675 */         placeBlock(debug1, DOT_DECO_DATA, 5, 5, 52, debug3);
/*      */         
/*  677 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  678 */           generateBox(debug1, debug3, debug4, debug4 + 1, 21, debug4, debug4 + 1, 57 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */         
/*  681 */         generateBox(debug1, debug3, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  682 */         generateBox(debug1, debug3, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  687 */       if (chunkIntersects(debug3, 51, 21, 58, 58)) {
/*  688 */         generateBox(debug1, debug3, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  690 */         generateWaterBox(debug1, debug3, 51, 1, 21, 57, 7, 57);
/*      */ 
/*      */         
/*  693 */         generateBox(debug1, debug3, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false); int debug4;
/*  694 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  695 */           generateBox(debug1, debug3, 57 - debug4, debug4 + 1, 21, 57 - debug4, debug4 + 1, 57 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  697 */         for (debug4 = 23; debug4 < 53; debug4 += 3) {
/*  698 */           placeBlock(debug1, DOT_DECO_DATA, 52, 5, debug4, debug3);
/*      */         }
/*  700 */         placeBlock(debug1, DOT_DECO_DATA, 52, 5, 52, debug3);
/*      */ 
/*      */         
/*  703 */         generateBox(debug1, debug3, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  704 */         generateBox(debug1, debug3, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  709 */       if (chunkIntersects(debug3, 0, 51, 57, 57)) {
/*  710 */         generateBox(debug1, debug3, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  712 */         generateWaterBox(debug1, debug3, 7, 1, 51, 50, 10, 57);
/*      */ 
/*      */         
/*  715 */         for (int debug4 = 0; debug4 < 4; debug4++) {
/*  716 */           generateBox(debug1, debug3, debug4 + 1, debug4 + 1, 57 - debug4, 56 - debug4, debug4 + 1, 57 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateMiddleWall(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  724 */       if (chunkIntersects(debug3, 7, 21, 13, 50)) {
/*  725 */         generateBox(debug1, debug3, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  727 */         generateWaterBox(debug1, debug3, 7, 1, 21, 13, 10, 50);
/*      */ 
/*      */         
/*  730 */         generateBox(debug1, debug3, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false); int debug4;
/*  731 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  732 */           generateBox(debug1, debug3, debug4 + 7, debug4 + 5, 21, debug4 + 7, debug4 + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  734 */         for (debug4 = 21; debug4 <= 45; debug4 += 3) {
/*  735 */           placeBlock(debug1, DOT_DECO_DATA, 12, 9, debug4, debug3);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  741 */       if (chunkIntersects(debug3, 44, 21, 50, 54)) {
/*  742 */         generateBox(debug1, debug3, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  744 */         generateWaterBox(debug1, debug3, 44, 1, 21, 50, 10, 50);
/*      */ 
/*      */         
/*  747 */         generateBox(debug1, debug3, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false); int debug4;
/*  748 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  749 */           generateBox(debug1, debug3, 50 - debug4, debug4 + 5, 21, 50 - debug4, debug4 + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  751 */         for (debug4 = 21; debug4 <= 45; debug4 += 3) {
/*  752 */           placeBlock(debug1, DOT_DECO_DATA, 45, 9, debug4, debug3);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  758 */       if (chunkIntersects(debug3, 8, 44, 49, 54)) {
/*  759 */         generateBox(debug1, debug3, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  761 */         generateWaterBox(debug1, debug3, 14, 1, 44, 43, 10, 50);
/*      */         
/*      */         int debug4;
/*  764 */         for (debug4 = 12; debug4 <= 45; debug4 += 3) {
/*  765 */           placeBlock(debug1, DOT_DECO_DATA, debug4, 9, 45, debug3);
/*  766 */           placeBlock(debug1, DOT_DECO_DATA, debug4, 9, 52, debug3);
/*  767 */           if (debug4 == 12 || debug4 == 18 || debug4 == 24 || debug4 == 33 || debug4 == 39 || debug4 == 45) {
/*  768 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 9, 47, debug3);
/*  769 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 9, 50, debug3);
/*  770 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 10, 45, debug3);
/*  771 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 10, 46, debug3);
/*  772 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 10, 51, debug3);
/*  773 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 10, 52, debug3);
/*  774 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 11, 47, debug3);
/*  775 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 11, 50, debug3);
/*  776 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 12, 48, debug3);
/*  777 */             placeBlock(debug1, DOT_DECO_DATA, debug4, 12, 49, debug3);
/*      */           } 
/*      */         } 
/*      */         
/*  781 */         for (debug4 = 0; debug4 < 3; debug4++) {
/*  782 */           generateBox(debug1, debug3, 8 + debug4, 5 + debug4, 54, 49 - debug4, 5 + debug4, 54, BASE_GRAY, BASE_GRAY, false);
/*      */         }
/*  784 */         generateBox(debug1, debug3, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
/*  785 */         generateBox(debug1, debug3, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateUpperWall(WorldGenLevel debug1, Random debug2, BoundingBox debug3) {
/*  792 */       if (chunkIntersects(debug3, 14, 21, 20, 43)) {
/*  793 */         generateBox(debug1, debug3, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  795 */         generateWaterBox(debug1, debug3, 14, 1, 22, 20, 14, 43);
/*      */ 
/*      */         
/*  798 */         generateBox(debug1, debug3, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  799 */         generateBox(debug1, debug3, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false); int debug4;
/*  800 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  801 */           generateBox(debug1, debug3, debug4 + 14, debug4 + 9, 21, debug4 + 14, debug4 + 9, 43 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  803 */         for (debug4 = 23; debug4 <= 39; debug4 += 3) {
/*  804 */           placeBlock(debug1, DOT_DECO_DATA, 19, 13, debug4, debug3);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  810 */       if (chunkIntersects(debug3, 37, 21, 43, 43)) {
/*  811 */         generateBox(debug1, debug3, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  813 */         generateWaterBox(debug1, debug3, 37, 1, 22, 43, 14, 43);
/*      */ 
/*      */         
/*  816 */         generateBox(debug1, debug3, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  817 */         generateBox(debug1, debug3, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false); int debug4;
/*  818 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  819 */           generateBox(debug1, debug3, 43 - debug4, debug4 + 9, 21, 43 - debug4, debug4 + 9, 43 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  821 */         for (debug4 = 23; debug4 <= 39; debug4 += 3) {
/*  822 */           placeBlock(debug1, DOT_DECO_DATA, 38, 13, debug4, debug3);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  828 */       if (chunkIntersects(debug3, 15, 37, 42, 43)) {
/*  829 */         generateBox(debug1, debug3, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  831 */         generateWaterBox(debug1, debug3, 21, 1, 37, 36, 14, 43);
/*      */ 
/*      */         
/*  834 */         generateBox(debug1, debug3, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false); int debug4;
/*  835 */         for (debug4 = 0; debug4 < 4; debug4++) {
/*  836 */           generateBox(debug1, debug3, 15 + debug4, debug4 + 9, 43 - debug4, 42 - debug4, debug4 + 9, 43 - debug4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  838 */         for (debug4 = 21; debug4 <= 36; debug4 += 3)
/*  839 */           placeBlock(debug1, DOT_DECO_DATA, debug4, 13, 38, debug3); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentEntryRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentEntryRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/*  847 */       super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, 1, debug1, debug2, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentEntryRoom(StructureManager debug1, CompoundTag debug2) {
/*  851 */       super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, debug2);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  857 */       generateBox(debug1, debug5, 0, 3, 0, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  858 */       generateBox(debug1, debug5, 5, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  859 */       generateBox(debug1, debug5, 0, 2, 0, 1, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  860 */       generateBox(debug1, debug5, 6, 2, 0, 7, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  861 */       generateBox(debug1, debug5, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  862 */       generateBox(debug1, debug5, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  865 */       generateBox(debug1, debug5, 0, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  868 */       generateBox(debug1, debug5, 1, 1, 0, 2, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  869 */       generateBox(debug1, debug5, 5, 1, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  871 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  872 */         generateWaterBox(debug1, debug5, 3, 1, 7, 4, 2, 7);
/*      */       }
/*  874 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  875 */         generateWaterBox(debug1, debug5, 0, 1, 3, 1, 2, 4);
/*      */       }
/*  877 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  878 */         generateWaterBox(debug1, debug5, 6, 1, 3, 7, 2, 4);
/*      */       }
/*      */       
/*  881 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleRoom extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentSimpleRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/*  889 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, debug1, debug2, 1, 1, 1);
/*  890 */       this.mainDesign = debug3.nextInt(3);
/*      */     }
/*      */     
/*      */     public OceanMonumentSimpleRoom(StructureManager debug1, CompoundTag debug2) {
/*  894 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  899 */       if (this.roomDefinition.index / 25 > 0) {
/*  900 */         generateDefaultFloor(debug1, debug5, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/*  902 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/*  903 */         generateBoxOnFillOnly(debug1, debug5, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */       
/*  906 */       boolean debug8 = (this.mainDesign != 0 && debug4.nextBoolean() && !this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()] && !this.roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && this.roomDefinition.countOpenings() > 1);
/*      */       
/*  908 */       if (this.mainDesign == 0) {
/*      */         
/*  910 */         generateBox(debug1, debug5, 0, 1, 0, 2, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  911 */         generateBox(debug1, debug5, 0, 3, 0, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  912 */         generateBox(debug1, debug5, 0, 2, 0, 0, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  913 */         generateBox(debug1, debug5, 1, 2, 0, 2, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  914 */         placeBlock(debug1, LAMP_BLOCK, 1, 2, 1, debug5);
/*      */ 
/*      */         
/*  917 */         generateBox(debug1, debug5, 5, 1, 0, 7, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  918 */         generateBox(debug1, debug5, 5, 3, 0, 7, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  919 */         generateBox(debug1, debug5, 7, 2, 0, 7, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  920 */         generateBox(debug1, debug5, 5, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  921 */         placeBlock(debug1, LAMP_BLOCK, 6, 2, 1, debug5);
/*      */ 
/*      */         
/*  924 */         generateBox(debug1, debug5, 0, 1, 5, 2, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  925 */         generateBox(debug1, debug5, 0, 3, 5, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  926 */         generateBox(debug1, debug5, 0, 2, 5, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  927 */         generateBox(debug1, debug5, 1, 2, 7, 2, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  928 */         placeBlock(debug1, LAMP_BLOCK, 1, 2, 6, debug5);
/*      */ 
/*      */         
/*  931 */         generateBox(debug1, debug5, 5, 1, 5, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  932 */         generateBox(debug1, debug5, 5, 3, 5, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  933 */         generateBox(debug1, debug5, 7, 2, 5, 7, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  934 */         generateBox(debug1, debug5, 5, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  935 */         placeBlock(debug1, LAMP_BLOCK, 6, 2, 6, debug5);
/*      */         
/*  937 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  938 */           generateBox(debug1, debug5, 3, 3, 0, 4, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  940 */           generateBox(debug1, debug5, 3, 3, 0, 4, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  941 */           generateBox(debug1, debug5, 3, 2, 0, 4, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  942 */           generateBox(debug1, debug5, 3, 1, 0, 4, 1, 1, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  944 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  945 */           generateBox(debug1, debug5, 3, 3, 7, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  947 */           generateBox(debug1, debug5, 3, 3, 6, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  948 */           generateBox(debug1, debug5, 3, 2, 7, 4, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  949 */           generateBox(debug1, debug5, 3, 1, 6, 4, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  951 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  952 */           generateBox(debug1, debug5, 0, 3, 3, 0, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  954 */           generateBox(debug1, debug5, 0, 3, 3, 1, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  955 */           generateBox(debug1, debug5, 0, 2, 3, 0, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  956 */           generateBox(debug1, debug5, 0, 1, 3, 1, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  958 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  959 */           generateBox(debug1, debug5, 7, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  961 */           generateBox(debug1, debug5, 6, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  962 */           generateBox(debug1, debug5, 7, 2, 3, 7, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  963 */           generateBox(debug1, debug5, 6, 1, 3, 7, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  965 */       } else if (this.mainDesign == 1) {
/*      */         
/*  967 */         generateBox(debug1, debug5, 2, 1, 2, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  968 */         generateBox(debug1, debug5, 2, 1, 5, 2, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  969 */         generateBox(debug1, debug5, 5, 1, 5, 5, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  970 */         generateBox(debug1, debug5, 5, 1, 2, 5, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  971 */         placeBlock(debug1, LAMP_BLOCK, 2, 2, 2, debug5);
/*  972 */         placeBlock(debug1, LAMP_BLOCK, 2, 2, 5, debug5);
/*  973 */         placeBlock(debug1, LAMP_BLOCK, 5, 2, 5, debug5);
/*  974 */         placeBlock(debug1, LAMP_BLOCK, 5, 2, 2, debug5);
/*      */ 
/*      */         
/*  977 */         generateBox(debug1, debug5, 0, 1, 0, 1, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  978 */         generateBox(debug1, debug5, 0, 1, 1, 0, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  979 */         generateBox(debug1, debug5, 0, 1, 7, 1, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  980 */         generateBox(debug1, debug5, 0, 1, 6, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  981 */         generateBox(debug1, debug5, 6, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  982 */         generateBox(debug1, debug5, 7, 1, 6, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  983 */         generateBox(debug1, debug5, 6, 1, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  984 */         generateBox(debug1, debug5, 7, 1, 1, 7, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  985 */         placeBlock(debug1, BASE_GRAY, 1, 2, 0, debug5);
/*  986 */         placeBlock(debug1, BASE_GRAY, 0, 2, 1, debug5);
/*  987 */         placeBlock(debug1, BASE_GRAY, 1, 2, 7, debug5);
/*  988 */         placeBlock(debug1, BASE_GRAY, 0, 2, 6, debug5);
/*  989 */         placeBlock(debug1, BASE_GRAY, 6, 2, 7, debug5);
/*  990 */         placeBlock(debug1, BASE_GRAY, 7, 2, 6, debug5);
/*  991 */         placeBlock(debug1, BASE_GRAY, 6, 2, 0, debug5);
/*  992 */         placeBlock(debug1, BASE_GRAY, 7, 2, 1, debug5);
/*  993 */         if (!this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  994 */           generateBox(debug1, debug5, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  995 */           generateBox(debug1, debug5, 1, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  996 */           generateBox(debug1, debug5, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  998 */         if (!this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  999 */           generateBox(debug1, debug5, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1000 */           generateBox(debug1, debug5, 1, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1001 */           generateBox(debug1, debug5, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/* 1003 */         if (!this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1004 */           generateBox(debug1, debug5, 0, 3, 1, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1005 */           generateBox(debug1, debug5, 0, 2, 1, 0, 2, 6, BASE_GRAY, BASE_GRAY, false);
/* 1006 */           generateBox(debug1, debug5, 0, 1, 1, 0, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/* 1008 */         if (!this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1009 */           generateBox(debug1, debug5, 7, 3, 1, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1010 */           generateBox(debug1, debug5, 7, 2, 1, 7, 2, 6, BASE_GRAY, BASE_GRAY, false);
/* 1011 */           generateBox(debug1, debug5, 7, 1, 1, 7, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/* 1013 */       } else if (this.mainDesign == 2) {
/* 1014 */         generateBox(debug1, debug5, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1015 */         generateBox(debug1, debug5, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1016 */         generateBox(debug1, debug5, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1017 */         generateBox(debug1, debug5, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1019 */         generateBox(debug1, debug5, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1020 */         generateBox(debug1, debug5, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1021 */         generateBox(debug1, debug5, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1022 */         generateBox(debug1, debug5, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1024 */         generateBox(debug1, debug5, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1025 */         generateBox(debug1, debug5, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1026 */         generateBox(debug1, debug5, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1027 */         generateBox(debug1, debug5, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1029 */         generateBox(debug1, debug5, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1030 */         generateBox(debug1, debug5, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1031 */         generateBox(debug1, debug5, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1032 */         generateBox(debug1, debug5, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1034 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1035 */           generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */         }
/* 1037 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1038 */           generateWaterBox(debug1, debug5, 3, 1, 7, 4, 2, 7);
/*      */         }
/* 1040 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1041 */           generateWaterBox(debug1, debug5, 0, 1, 3, 0, 2, 4);
/*      */         }
/* 1043 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1044 */           generateWaterBox(debug1, debug5, 7, 1, 3, 7, 2, 4);
/*      */         }
/*      */       } 
/* 1047 */       if (debug8) {
/* 1048 */         generateBox(debug1, debug5, 3, 1, 3, 4, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1049 */         generateBox(debug1, debug5, 3, 2, 3, 4, 2, 4, BASE_GRAY, BASE_GRAY, false);
/* 1050 */         generateBox(debug1, debug5, 3, 3, 3, 4, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */       
/* 1053 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleTopRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentSimpleTopRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1059 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, 1, debug1, debug2, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentSimpleTopRoom(StructureManager debug1, CompoundTag debug2) {
/* 1063 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1068 */       if (this.roomDefinition.index / 25 > 0) {
/* 1069 */         generateDefaultFloor(debug1, debug5, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1071 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/* 1072 */         generateBoxOnFillOnly(debug1, debug5, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1077 */       for (int debug8 = 1; debug8 <= 6; debug8++) {
/* 1078 */         for (int debug9 = 1; debug9 <= 6; debug9++) {
/* 1079 */           if (debug4.nextInt(3) != 0) {
/* 1080 */             int debug10 = 2 + ((debug4.nextInt(4) == 0) ? 0 : 1);
/* 1081 */             BlockState debug11 = Blocks.WET_SPONGE.defaultBlockState();
/* 1082 */             generateBox(debug1, debug5, debug8, debug10, debug9, debug8, 3, debug9, debug11, debug11, false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1087 */       generateBox(debug1, debug5, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1088 */       generateBox(debug1, debug5, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1089 */       generateBox(debug1, debug5, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1090 */       generateBox(debug1, debug5, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1092 */       generateBox(debug1, debug5, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1093 */       generateBox(debug1, debug5, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1094 */       generateBox(debug1, debug5, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1095 */       generateBox(debug1, debug5, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1097 */       generateBox(debug1, debug5, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1098 */       generateBox(debug1, debug5, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1099 */       generateBox(debug1, debug5, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1100 */       generateBox(debug1, debug5, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1102 */       generateBox(debug1, debug5, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1103 */       generateBox(debug1, debug5, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1104 */       generateBox(debug1, debug5, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1105 */       generateBox(debug1, debug5, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1107 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1108 */         generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */       }
/*      */ 
/*      */       
/* 1112 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleYRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1118 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, 1, debug1, debug2, 1, 2, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleYRoom(StructureManager debug1, CompoundTag debug2) {
/* 1122 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1127 */       if (this.roomDefinition.index / 25 > 0) {
/* 1128 */         generateDefaultFloor(debug1, debug5, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1130 */       OceanMonumentPieces.RoomDefinition debug8 = this.roomDefinition.connections[Direction.UP.get3DDataValue()];
/* 1131 */       if (debug8.connections[Direction.UP.get3DDataValue()] == null) {
/* 1132 */         generateBoxOnFillOnly(debug1, debug5, 1, 8, 1, 6, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1137 */       generateBox(debug1, debug5, 0, 4, 0, 0, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1138 */       generateBox(debug1, debug5, 7, 4, 0, 7, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1139 */       generateBox(debug1, debug5, 1, 4, 0, 6, 4, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1140 */       generateBox(debug1, debug5, 1, 4, 7, 6, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1142 */       generateBox(debug1, debug5, 2, 4, 1, 2, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1143 */       generateBox(debug1, debug5, 1, 4, 2, 1, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1144 */       generateBox(debug1, debug5, 5, 4, 1, 5, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1145 */       generateBox(debug1, debug5, 6, 4, 2, 6, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1146 */       generateBox(debug1, debug5, 2, 4, 5, 2, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1147 */       generateBox(debug1, debug5, 1, 4, 5, 1, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1148 */       generateBox(debug1, debug5, 5, 4, 5, 5, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1149 */       generateBox(debug1, debug5, 6, 4, 5, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1151 */       OceanMonumentPieces.RoomDefinition debug9 = this.roomDefinition;
/* 1152 */       for (int debug10 = 1; debug10 <= 5; debug10 += 4) {
/* 1153 */         int debug11 = 0;
/* 1154 */         if (debug9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1155 */           generateBox(debug1, debug5, 2, debug10, debug11, 2, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1156 */           generateBox(debug1, debug5, 5, debug10, debug11, 5, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1157 */           generateBox(debug1, debug5, 3, debug10 + 2, debug11, 4, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1159 */           generateBox(debug1, debug5, 0, debug10, debug11, 7, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1160 */           generateBox(debug1, debug5, 0, debug10 + 1, debug11, 7, debug10 + 1, debug11, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1162 */         debug11 = 7;
/* 1163 */         if (debug9.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1164 */           generateBox(debug1, debug5, 2, debug10, debug11, 2, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1165 */           generateBox(debug1, debug5, 5, debug10, debug11, 5, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1166 */           generateBox(debug1, debug5, 3, debug10 + 2, debug11, 4, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1168 */           generateBox(debug1, debug5, 0, debug10, debug11, 7, debug10 + 2, debug11, BASE_LIGHT, BASE_LIGHT, false);
/* 1169 */           generateBox(debug1, debug5, 0, debug10 + 1, debug11, 7, debug10 + 1, debug11, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1171 */         int debug12 = 0;
/* 1172 */         if (debug9.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1173 */           generateBox(debug1, debug5, debug12, debug10, 2, debug12, debug10 + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1174 */           generateBox(debug1, debug5, debug12, debug10, 5, debug12, debug10 + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1175 */           generateBox(debug1, debug5, debug12, debug10 + 2, 3, debug12, debug10 + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1177 */           generateBox(debug1, debug5, debug12, debug10, 0, debug12, debug10 + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1178 */           generateBox(debug1, debug5, debug12, debug10 + 1, 0, debug12, debug10 + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1180 */         debug12 = 7;
/* 1181 */         if (debug9.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1182 */           generateBox(debug1, debug5, debug12, debug10, 2, debug12, debug10 + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1183 */           generateBox(debug1, debug5, debug12, debug10, 5, debug12, debug10 + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1184 */           generateBox(debug1, debug5, debug12, debug10 + 2, 3, debug12, debug10 + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1186 */           generateBox(debug1, debug5, debug12, debug10, 0, debug12, debug10 + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1187 */           generateBox(debug1, debug5, debug12, debug10 + 1, 0, debug12, debug10 + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1189 */         debug9 = debug8;
/*      */       } 
/*      */ 
/*      */       
/* 1193 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleXRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1199 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, 1, debug1, debug2, 2, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleXRoom(StructureManager debug1, CompoundTag debug2) {
/* 1203 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1208 */       OceanMonumentPieces.RoomDefinition debug8 = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1209 */       OceanMonumentPieces.RoomDefinition debug9 = this.roomDefinition;
/* 1210 */       if (this.roomDefinition.index / 25 > 0) {
/* 1211 */         generateDefaultFloor(debug1, debug5, 8, 0, debug8.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1212 */         generateDefaultFloor(debug1, debug5, 0, 0, debug9.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1214 */       if (debug9.connections[Direction.UP.get3DDataValue()] == null) {
/* 1215 */         generateBoxOnFillOnly(debug1, debug5, 1, 4, 1, 7, 4, 6, BASE_GRAY);
/*      */       }
/* 1217 */       if (debug8.connections[Direction.UP.get3DDataValue()] == null) {
/* 1218 */         generateBoxOnFillOnly(debug1, debug5, 8, 4, 1, 14, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1222 */       generateBox(debug1, debug5, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1223 */       generateBox(debug1, debug5, 15, 3, 0, 15, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1224 */       generateBox(debug1, debug5, 1, 3, 0, 15, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1225 */       generateBox(debug1, debug5, 1, 3, 7, 14, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1226 */       generateBox(debug1, debug5, 0, 2, 0, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1227 */       generateBox(debug1, debug5, 15, 2, 0, 15, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1228 */       generateBox(debug1, debug5, 1, 2, 0, 15, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1229 */       generateBox(debug1, debug5, 1, 2, 7, 14, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1230 */       generateBox(debug1, debug5, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1231 */       generateBox(debug1, debug5, 15, 1, 0, 15, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1232 */       generateBox(debug1, debug5, 1, 1, 0, 15, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1233 */       generateBox(debug1, debug5, 1, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1236 */       generateBox(debug1, debug5, 5, 1, 0, 10, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1237 */       generateBox(debug1, debug5, 6, 2, 0, 9, 2, 3, BASE_GRAY, BASE_GRAY, false);
/* 1238 */       generateBox(debug1, debug5, 5, 3, 0, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1240 */       placeBlock(debug1, LAMP_BLOCK, 6, 2, 3, debug5);
/* 1241 */       placeBlock(debug1, LAMP_BLOCK, 9, 2, 3, debug5);
/*      */ 
/*      */       
/* 1244 */       if (debug9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1245 */         generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1247 */       if (debug9.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1248 */         generateWaterBox(debug1, debug5, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1250 */       if (debug9.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1251 */         generateWaterBox(debug1, debug5, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1253 */       if (debug8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1254 */         generateWaterBox(debug1, debug5, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1256 */       if (debug8.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1257 */         generateWaterBox(debug1, debug5, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1259 */       if (debug8.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1260 */         generateWaterBox(debug1, debug5, 15, 1, 3, 15, 2, 4);
/*      */       }
/*      */       
/* 1263 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleZRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleZRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1269 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, 1, debug1, debug2, 1, 1, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleZRoom(StructureManager debug1, CompoundTag debug2) {
/* 1273 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1278 */       OceanMonumentPieces.RoomDefinition debug8 = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1279 */       OceanMonumentPieces.RoomDefinition debug9 = this.roomDefinition;
/* 1280 */       if (this.roomDefinition.index / 25 > 0) {
/* 1281 */         generateDefaultFloor(debug1, debug5, 0, 8, debug8.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1282 */         generateDefaultFloor(debug1, debug5, 0, 0, debug9.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1284 */       if (debug9.connections[Direction.UP.get3DDataValue()] == null) {
/* 1285 */         generateBoxOnFillOnly(debug1, debug5, 1, 4, 1, 6, 4, 7, BASE_GRAY);
/*      */       }
/* 1287 */       if (debug8.connections[Direction.UP.get3DDataValue()] == null) {
/* 1288 */         generateBoxOnFillOnly(debug1, debug5, 1, 4, 8, 6, 4, 14, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1292 */       generateBox(debug1, debug5, 0, 3, 0, 0, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1293 */       generateBox(debug1, debug5, 7, 3, 0, 7, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1294 */       generateBox(debug1, debug5, 1, 3, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1295 */       generateBox(debug1, debug5, 1, 3, 15, 6, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1296 */       generateBox(debug1, debug5, 0, 2, 0, 0, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1297 */       generateBox(debug1, debug5, 7, 2, 0, 7, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1298 */       generateBox(debug1, debug5, 1, 2, 0, 7, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1299 */       generateBox(debug1, debug5, 1, 2, 15, 6, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1300 */       generateBox(debug1, debug5, 0, 1, 0, 0, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1301 */       generateBox(debug1, debug5, 7, 1, 0, 7, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1302 */       generateBox(debug1, debug5, 1, 1, 0, 7, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1303 */       generateBox(debug1, debug5, 1, 1, 15, 6, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1306 */       generateBox(debug1, debug5, 1, 1, 1, 1, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1307 */       generateBox(debug1, debug5, 6, 1, 1, 6, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1308 */       generateBox(debug1, debug5, 1, 3, 1, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1309 */       generateBox(debug1, debug5, 6, 3, 1, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1310 */       generateBox(debug1, debug5, 1, 1, 13, 1, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1311 */       generateBox(debug1, debug5, 6, 1, 13, 6, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1312 */       generateBox(debug1, debug5, 1, 3, 13, 1, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1313 */       generateBox(debug1, debug5, 6, 3, 13, 6, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1316 */       generateBox(debug1, debug5, 2, 1, 6, 2, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1317 */       generateBox(debug1, debug5, 5, 1, 6, 5, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1318 */       generateBox(debug1, debug5, 2, 1, 9, 2, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1319 */       generateBox(debug1, debug5, 5, 1, 9, 5, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1321 */       generateBox(debug1, debug5, 3, 2, 6, 4, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1322 */       generateBox(debug1, debug5, 3, 2, 9, 4, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1323 */       generateBox(debug1, debug5, 2, 2, 7, 2, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/* 1324 */       generateBox(debug1, debug5, 5, 2, 7, 5, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1326 */       placeBlock(debug1, LAMP_BLOCK, 2, 2, 5, debug5);
/* 1327 */       placeBlock(debug1, LAMP_BLOCK, 5, 2, 5, debug5);
/* 1328 */       placeBlock(debug1, LAMP_BLOCK, 2, 2, 10, debug5);
/* 1329 */       placeBlock(debug1, LAMP_BLOCK, 5, 2, 10, debug5);
/* 1330 */       placeBlock(debug1, BASE_LIGHT, 2, 3, 5, debug5);
/* 1331 */       placeBlock(debug1, BASE_LIGHT, 5, 3, 5, debug5);
/* 1332 */       placeBlock(debug1, BASE_LIGHT, 2, 3, 10, debug5);
/* 1333 */       placeBlock(debug1, BASE_LIGHT, 5, 3, 10, debug5);
/*      */ 
/*      */       
/* 1336 */       if (debug9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1337 */         generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1339 */       if (debug9.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1340 */         generateWaterBox(debug1, debug5, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1342 */       if (debug9.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1343 */         generateWaterBox(debug1, debug5, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1345 */       if (debug8.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1346 */         generateWaterBox(debug1, debug5, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1348 */       if (debug8.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1349 */         generateWaterBox(debug1, debug5, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1351 */       if (debug8.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1352 */         generateWaterBox(debug1, debug5, 7, 1, 11, 7, 2, 12);
/*      */       }
/*      */       
/* 1355 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXYRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleXYRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1361 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, 1, debug1, debug2, 2, 2, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleXYRoom(StructureManager debug1, CompoundTag debug2) {
/* 1365 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1370 */       OceanMonumentPieces.RoomDefinition debug8 = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1371 */       OceanMonumentPieces.RoomDefinition debug9 = this.roomDefinition;
/* 1372 */       OceanMonumentPieces.RoomDefinition debug10 = debug9.connections[Direction.UP.get3DDataValue()];
/* 1373 */       OceanMonumentPieces.RoomDefinition debug11 = debug8.connections[Direction.UP.get3DDataValue()];
/*      */       
/* 1375 */       if (this.roomDefinition.index / 25 > 0) {
/* 1376 */         generateDefaultFloor(debug1, debug5, 8, 0, debug8.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1377 */         generateDefaultFloor(debug1, debug5, 0, 0, debug9.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1379 */       if (debug10.connections[Direction.UP.get3DDataValue()] == null) {
/* 1380 */         generateBoxOnFillOnly(debug1, debug5, 1, 8, 1, 7, 8, 6, BASE_GRAY);
/*      */       }
/* 1382 */       if (debug11.connections[Direction.UP.get3DDataValue()] == null) {
/* 1383 */         generateBoxOnFillOnly(debug1, debug5, 8, 8, 1, 14, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1387 */       for (int debug12 = 1; debug12 <= 7; debug12++) {
/* 1388 */         BlockState debug13 = BASE_LIGHT;
/* 1389 */         if (debug12 == 2 || debug12 == 6) {
/* 1390 */           debug13 = BASE_GRAY;
/*      */         }
/* 1392 */         generateBox(debug1, debug5, 0, debug12, 0, 0, debug12, 7, debug13, debug13, false);
/* 1393 */         generateBox(debug1, debug5, 15, debug12, 0, 15, debug12, 7, debug13, debug13, false);
/* 1394 */         generateBox(debug1, debug5, 1, debug12, 0, 15, debug12, 0, debug13, debug13, false);
/* 1395 */         generateBox(debug1, debug5, 1, debug12, 7, 14, debug12, 7, debug13, debug13, false);
/*      */       } 
/*      */ 
/*      */       
/* 1399 */       generateBox(debug1, debug5, 2, 1, 3, 2, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1400 */       generateBox(debug1, debug5, 3, 1, 2, 4, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1401 */       generateBox(debug1, debug5, 3, 1, 5, 4, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1402 */       generateBox(debug1, debug5, 13, 1, 3, 13, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1403 */       generateBox(debug1, debug5, 11, 1, 2, 12, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1404 */       generateBox(debug1, debug5, 11, 1, 5, 12, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1406 */       generateBox(debug1, debug5, 5, 1, 3, 5, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1407 */       generateBox(debug1, debug5, 10, 1, 3, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1409 */       generateBox(debug1, debug5, 5, 7, 2, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1410 */       generateBox(debug1, debug5, 5, 5, 2, 5, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1411 */       generateBox(debug1, debug5, 10, 5, 2, 10, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1412 */       generateBox(debug1, debug5, 5, 5, 5, 5, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1413 */       generateBox(debug1, debug5, 10, 5, 5, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1414 */       placeBlock(debug1, BASE_LIGHT, 6, 6, 2, debug5);
/* 1415 */       placeBlock(debug1, BASE_LIGHT, 9, 6, 2, debug5);
/* 1416 */       placeBlock(debug1, BASE_LIGHT, 6, 6, 5, debug5);
/* 1417 */       placeBlock(debug1, BASE_LIGHT, 9, 6, 5, debug5);
/*      */       
/* 1419 */       generateBox(debug1, debug5, 5, 4, 3, 6, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1420 */       generateBox(debug1, debug5, 9, 4, 3, 10, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1421 */       placeBlock(debug1, LAMP_BLOCK, 5, 4, 2, debug5);
/* 1422 */       placeBlock(debug1, LAMP_BLOCK, 5, 4, 5, debug5);
/* 1423 */       placeBlock(debug1, LAMP_BLOCK, 10, 4, 2, debug5);
/* 1424 */       placeBlock(debug1, LAMP_BLOCK, 10, 4, 5, debug5);
/*      */ 
/*      */       
/* 1427 */       if (debug9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1428 */         generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1430 */       if (debug9.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1431 */         generateWaterBox(debug1, debug5, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1433 */       if (debug9.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1434 */         generateWaterBox(debug1, debug5, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1436 */       if (debug8.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1437 */         generateWaterBox(debug1, debug5, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1439 */       if (debug8.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1440 */         generateWaterBox(debug1, debug5, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1442 */       if (debug8.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1443 */         generateWaterBox(debug1, debug5, 15, 1, 3, 15, 2, 4);
/*      */       }
/* 1445 */       if (debug10.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1446 */         generateWaterBox(debug1, debug5, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1448 */       if (debug10.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1449 */         generateWaterBox(debug1, debug5, 3, 5, 7, 4, 6, 7);
/*      */       }
/* 1451 */       if (debug10.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1452 */         generateWaterBox(debug1, debug5, 0, 5, 3, 0, 6, 4);
/*      */       }
/* 1454 */       if (debug11.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1455 */         generateWaterBox(debug1, debug5, 11, 5, 0, 12, 6, 0);
/*      */       }
/* 1457 */       if (debug11.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1458 */         generateWaterBox(debug1, debug5, 11, 5, 7, 12, 6, 7);
/*      */       }
/* 1460 */       if (debug11.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1461 */         generateWaterBox(debug1, debug5, 15, 5, 3, 15, 6, 4);
/*      */       }
/*      */       
/* 1464 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYZRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleYZRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1470 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, 1, debug1, debug2, 1, 2, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleYZRoom(StructureManager debug1, CompoundTag debug2) {
/* 1474 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1479 */       OceanMonumentPieces.RoomDefinition debug8 = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1480 */       OceanMonumentPieces.RoomDefinition debug9 = this.roomDefinition;
/* 1481 */       OceanMonumentPieces.RoomDefinition debug10 = debug8.connections[Direction.UP.get3DDataValue()];
/* 1482 */       OceanMonumentPieces.RoomDefinition debug11 = debug9.connections[Direction.UP.get3DDataValue()];
/* 1483 */       if (this.roomDefinition.index / 25 > 0) {
/* 1484 */         generateDefaultFloor(debug1, debug5, 0, 8, debug8.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1485 */         generateDefaultFloor(debug1, debug5, 0, 0, debug9.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1487 */       if (debug11.connections[Direction.UP.get3DDataValue()] == null) {
/* 1488 */         generateBoxOnFillOnly(debug1, debug5, 1, 8, 1, 6, 8, 7, BASE_GRAY);
/*      */       }
/* 1490 */       if (debug10.connections[Direction.UP.get3DDataValue()] == null) {
/* 1491 */         generateBoxOnFillOnly(debug1, debug5, 1, 8, 8, 6, 8, 14, BASE_GRAY);
/*      */       }
/*      */       
/*      */       int debug12;
/* 1495 */       for (debug12 = 1; debug12 <= 7; debug12++) {
/* 1496 */         BlockState debug13 = BASE_LIGHT;
/* 1497 */         if (debug12 == 2 || debug12 == 6) {
/* 1498 */           debug13 = BASE_GRAY;
/*      */         }
/* 1500 */         generateBox(debug1, debug5, 0, debug12, 0, 0, debug12, 15, debug13, debug13, false);
/* 1501 */         generateBox(debug1, debug5, 7, debug12, 0, 7, debug12, 15, debug13, debug13, false);
/* 1502 */         generateBox(debug1, debug5, 1, debug12, 0, 6, debug12, 0, debug13, debug13, false);
/* 1503 */         generateBox(debug1, debug5, 1, debug12, 15, 6, debug12, 15, debug13, debug13, false);
/*      */       } 
/*      */ 
/*      */       
/* 1507 */       for (debug12 = 1; debug12 <= 7; debug12++) {
/* 1508 */         BlockState debug13 = BASE_BLACK;
/* 1509 */         if (debug12 == 2 || debug12 == 6) {
/* 1510 */           debug13 = LAMP_BLOCK;
/*      */         }
/* 1512 */         generateBox(debug1, debug5, 3, debug12, 7, 4, debug12, 8, debug13, debug13, false);
/*      */       } 
/*      */ 
/*      */       
/* 1516 */       if (debug9.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1517 */         generateWaterBox(debug1, debug5, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1519 */       if (debug9.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1520 */         generateWaterBox(debug1, debug5, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1522 */       if (debug9.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1523 */         generateWaterBox(debug1, debug5, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1525 */       if (debug8.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1526 */         generateWaterBox(debug1, debug5, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1528 */       if (debug8.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1529 */         generateWaterBox(debug1, debug5, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1531 */       if (debug8.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1532 */         generateWaterBox(debug1, debug5, 7, 1, 11, 7, 2, 12);
/*      */       }
/*      */       
/* 1535 */       if (debug11.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1536 */         generateWaterBox(debug1, debug5, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1538 */       if (debug11.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1539 */         generateWaterBox(debug1, debug5, 7, 5, 3, 7, 6, 4);
/* 1540 */         generateBox(debug1, debug5, 5, 4, 2, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1541 */         generateBox(debug1, debug5, 6, 1, 2, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1542 */         generateBox(debug1, debug5, 6, 1, 5, 6, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1544 */       if (debug11.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1545 */         generateWaterBox(debug1, debug5, 0, 5, 3, 0, 6, 4);
/* 1546 */         generateBox(debug1, debug5, 1, 4, 2, 2, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1547 */         generateBox(debug1, debug5, 1, 1, 2, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1548 */         generateBox(debug1, debug5, 1, 1, 5, 1, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1550 */       if (debug10.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1551 */         generateWaterBox(debug1, debug5, 3, 5, 15, 4, 6, 15);
/*      */       }
/* 1553 */       if (debug10.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1554 */         generateWaterBox(debug1, debug5, 0, 5, 11, 0, 6, 12);
/* 1555 */         generateBox(debug1, debug5, 1, 4, 10, 2, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1556 */         generateBox(debug1, debug5, 1, 1, 10, 1, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1557 */         generateBox(debug1, debug5, 1, 1, 13, 1, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1559 */       if (debug10.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1560 */         generateWaterBox(debug1, debug5, 7, 5, 11, 7, 6, 12);
/* 1561 */         generateBox(debug1, debug5, 5, 4, 10, 6, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1562 */         generateBox(debug1, debug5, 6, 1, 10, 6, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1563 */         generateBox(debug1, debug5, 6, 1, 13, 6, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */       
/* 1566 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentCoreRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentCoreRoom(Direction debug1, OceanMonumentPieces.RoomDefinition debug2) {
/* 1572 */       super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, 1, debug1, debug2, 2, 2, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentCoreRoom(StructureManager debug1, CompoundTag debug2) {
/* 1576 */       super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1581 */       generateBoxOnFillOnly(debug1, debug5, 1, 8, 0, 14, 8, 14, BASE_GRAY);
/*      */ 
/*      */ 
/*      */       
/* 1585 */       int debug8 = 7;
/* 1586 */       BlockState debug9 = BASE_LIGHT;
/* 1587 */       generateBox(debug1, debug5, 0, 7, 0, 0, 7, 15, debug9, debug9, false);
/* 1588 */       generateBox(debug1, debug5, 15, 7, 0, 15, 7, 15, debug9, debug9, false);
/* 1589 */       generateBox(debug1, debug5, 1, 7, 0, 15, 7, 0, debug9, debug9, false);
/* 1590 */       generateBox(debug1, debug5, 1, 7, 15, 14, 7, 15, debug9, debug9, false);
/*      */       
/* 1592 */       for (debug8 = 1; debug8 <= 6; debug8++) {
/* 1593 */         debug9 = BASE_LIGHT;
/* 1594 */         if (debug8 == 2 || debug8 == 6) {
/* 1595 */           debug9 = BASE_GRAY;
/*      */         }
/*      */         
/* 1598 */         for (int debug10 = 0; debug10 <= 15; debug10 += 15) {
/* 1599 */           generateBox(debug1, debug5, debug10, debug8, 0, debug10, debug8, 1, debug9, debug9, false);
/* 1600 */           generateBox(debug1, debug5, debug10, debug8, 6, debug10, debug8, 9, debug9, debug9, false);
/* 1601 */           generateBox(debug1, debug5, debug10, debug8, 14, debug10, debug8, 15, debug9, debug9, false);
/*      */         } 
/* 1603 */         generateBox(debug1, debug5, 1, debug8, 0, 1, debug8, 0, debug9, debug9, false);
/* 1604 */         generateBox(debug1, debug5, 6, debug8, 0, 9, debug8, 0, debug9, debug9, false);
/* 1605 */         generateBox(debug1, debug5, 14, debug8, 0, 14, debug8, 0, debug9, debug9, false);
/*      */         
/* 1607 */         generateBox(debug1, debug5, 1, debug8, 15, 14, debug8, 15, debug9, debug9, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1612 */       generateBox(debug1, debug5, 6, 3, 6, 9, 6, 9, BASE_BLACK, BASE_BLACK, false);
/* 1613 */       generateBox(debug1, debug5, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
/* 1614 */       for (debug8 = 3; debug8 <= 6; debug8 += 3) {
/* 1615 */         for (int i = 6; i <= 9; i += 3) {
/* 1616 */           placeBlock(debug1, LAMP_BLOCK, i, debug8, 6, debug5);
/* 1617 */           placeBlock(debug1, LAMP_BLOCK, i, debug8, 9, debug5);
/*      */         } 
/*      */       } 
/*      */       
/* 1621 */       generateBox(debug1, debug5, 5, 1, 6, 5, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1622 */       generateBox(debug1, debug5, 5, 1, 9, 5, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1623 */       generateBox(debug1, debug5, 10, 1, 6, 10, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1624 */       generateBox(debug1, debug5, 10, 1, 9, 10, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1625 */       generateBox(debug1, debug5, 6, 1, 5, 6, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1626 */       generateBox(debug1, debug5, 9, 1, 5, 9, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1627 */       generateBox(debug1, debug5, 6, 1, 10, 6, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1628 */       generateBox(debug1, debug5, 9, 1, 10, 9, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1630 */       generateBox(debug1, debug5, 5, 2, 5, 5, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1631 */       generateBox(debug1, debug5, 5, 2, 10, 5, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1632 */       generateBox(debug1, debug5, 10, 2, 5, 10, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1633 */       generateBox(debug1, debug5, 10, 2, 10, 10, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1635 */       generateBox(debug1, debug5, 5, 7, 1, 5, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1636 */       generateBox(debug1, debug5, 10, 7, 1, 10, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1637 */       generateBox(debug1, debug5, 5, 7, 9, 5, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1638 */       generateBox(debug1, debug5, 10, 7, 9, 10, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1640 */       generateBox(debug1, debug5, 1, 7, 5, 6, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1641 */       generateBox(debug1, debug5, 1, 7, 10, 6, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1642 */       generateBox(debug1, debug5, 9, 7, 5, 14, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1643 */       generateBox(debug1, debug5, 9, 7, 10, 14, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1646 */       generateBox(debug1, debug5, 2, 1, 2, 2, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1647 */       generateBox(debug1, debug5, 3, 1, 2, 3, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1648 */       generateBox(debug1, debug5, 13, 1, 2, 13, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1649 */       generateBox(debug1, debug5, 12, 1, 2, 12, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1650 */       generateBox(debug1, debug5, 2, 1, 12, 2, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1651 */       generateBox(debug1, debug5, 3, 1, 13, 3, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1652 */       generateBox(debug1, debug5, 13, 1, 12, 13, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1653 */       generateBox(debug1, debug5, 12, 1, 13, 12, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1655 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentWingRoom extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentWingRoom(Direction debug1, BoundingBox debug2, int debug3) {
/* 1663 */       super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, debug1, debug2);
/* 1664 */       this.mainDesign = debug3 & 0x1;
/*      */     }
/*      */     
/*      */     public OceanMonumentWingRoom(StructureManager debug1, CompoundTag debug2) {
/* 1668 */       super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1673 */       if (this.mainDesign == 0) {
/* 1674 */         int debug8; for (debug8 = 0; debug8 < 4; debug8++) {
/* 1675 */           generateBox(debug1, debug5, 10 - debug8, 3 - debug8, 20 - debug8, 12 + debug8, 3 - debug8, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1677 */         generateBox(debug1, debug5, 7, 0, 6, 15, 0, 16, BASE_LIGHT, BASE_LIGHT, false);
/* 1678 */         generateBox(debug1, debug5, 6, 0, 6, 6, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1679 */         generateBox(debug1, debug5, 16, 0, 6, 16, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1680 */         generateBox(debug1, debug5, 7, 1, 7, 7, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1681 */         generateBox(debug1, debug5, 15, 1, 7, 15, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1683 */         generateBox(debug1, debug5, 7, 1, 6, 9, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1684 */         generateBox(debug1, debug5, 13, 1, 6, 15, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1685 */         generateBox(debug1, debug5, 8, 1, 7, 9, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1686 */         generateBox(debug1, debug5, 13, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1687 */         generateBox(debug1, debug5, 9, 0, 5, 13, 0, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1689 */         generateBox(debug1, debug5, 10, 0, 7, 12, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1690 */         generateBox(debug1, debug5, 8, 0, 10, 8, 0, 12, BASE_BLACK, BASE_BLACK, false);
/* 1691 */         generateBox(debug1, debug5, 14, 0, 10, 14, 0, 12, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1693 */         for (debug8 = 18; debug8 >= 7; debug8 -= 3) {
/* 1694 */           placeBlock(debug1, LAMP_BLOCK, 6, 3, debug8, debug5);
/* 1695 */           placeBlock(debug1, LAMP_BLOCK, 16, 3, debug8, debug5);
/*      */         } 
/* 1697 */         placeBlock(debug1, LAMP_BLOCK, 10, 0, 10, debug5);
/* 1698 */         placeBlock(debug1, LAMP_BLOCK, 12, 0, 10, debug5);
/* 1699 */         placeBlock(debug1, LAMP_BLOCK, 10, 0, 12, debug5);
/* 1700 */         placeBlock(debug1, LAMP_BLOCK, 12, 0, 12, debug5);
/*      */         
/* 1702 */         placeBlock(debug1, LAMP_BLOCK, 8, 3, 6, debug5);
/* 1703 */         placeBlock(debug1, LAMP_BLOCK, 14, 3, 6, debug5);
/*      */ 
/*      */         
/* 1706 */         placeBlock(debug1, BASE_LIGHT, 4, 2, 4, debug5);
/* 1707 */         placeBlock(debug1, LAMP_BLOCK, 4, 1, 4, debug5);
/* 1708 */         placeBlock(debug1, BASE_LIGHT, 4, 0, 4, debug5);
/*      */         
/* 1710 */         placeBlock(debug1, BASE_LIGHT, 18, 2, 4, debug5);
/* 1711 */         placeBlock(debug1, LAMP_BLOCK, 18, 1, 4, debug5);
/* 1712 */         placeBlock(debug1, BASE_LIGHT, 18, 0, 4, debug5);
/*      */         
/* 1714 */         placeBlock(debug1, BASE_LIGHT, 4, 2, 18, debug5);
/* 1715 */         placeBlock(debug1, LAMP_BLOCK, 4, 1, 18, debug5);
/* 1716 */         placeBlock(debug1, BASE_LIGHT, 4, 0, 18, debug5);
/*      */         
/* 1718 */         placeBlock(debug1, BASE_LIGHT, 18, 2, 18, debug5);
/* 1719 */         placeBlock(debug1, LAMP_BLOCK, 18, 1, 18, debug5);
/* 1720 */         placeBlock(debug1, BASE_LIGHT, 18, 0, 18, debug5);
/*      */ 
/*      */         
/* 1723 */         placeBlock(debug1, BASE_LIGHT, 9, 7, 20, debug5);
/* 1724 */         placeBlock(debug1, BASE_LIGHT, 13, 7, 20, debug5);
/* 1725 */         generateBox(debug1, debug5, 6, 0, 21, 7, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 1726 */         generateBox(debug1, debug5, 15, 0, 21, 16, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1728 */         spawnElder(debug1, debug5, 11, 2, 16);
/* 1729 */       } else if (this.mainDesign == 1) {
/* 1730 */         generateBox(debug1, debug5, 9, 3, 18, 13, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1731 */         generateBox(debug1, debug5, 9, 0, 18, 9, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1732 */         generateBox(debug1, debug5, 13, 0, 18, 13, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1733 */         int debug8 = 9;
/* 1734 */         int debug9 = 20;
/* 1735 */         int debug10 = 5; int debug11;
/* 1736 */         for (debug11 = 0; debug11 < 2; debug11++) {
/* 1737 */           placeBlock(debug1, BASE_LIGHT, debug8, 6, 20, debug5);
/* 1738 */           placeBlock(debug1, LAMP_BLOCK, debug8, 5, 20, debug5);
/* 1739 */           placeBlock(debug1, BASE_LIGHT, debug8, 4, 20, debug5);
/* 1740 */           debug8 = 13;
/*      */         } 
/*      */         
/* 1743 */         generateBox(debug1, debug5, 7, 3, 7, 15, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1744 */         debug8 = 10;
/* 1745 */         for (debug11 = 0; debug11 < 2; debug11++) {
/* 1746 */           generateBox(debug1, debug5, debug8, 0, 10, debug8, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1747 */           generateBox(debug1, debug5, debug8, 0, 12, debug8, 6, 12, BASE_LIGHT, BASE_LIGHT, false);
/* 1748 */           placeBlock(debug1, LAMP_BLOCK, debug8, 0, 10, debug5);
/* 1749 */           placeBlock(debug1, LAMP_BLOCK, debug8, 0, 12, debug5);
/* 1750 */           placeBlock(debug1, LAMP_BLOCK, debug8, 4, 10, debug5);
/* 1751 */           placeBlock(debug1, LAMP_BLOCK, debug8, 4, 12, debug5);
/* 1752 */           debug8 = 12;
/*      */         } 
/* 1754 */         debug8 = 8;
/* 1755 */         for (debug11 = 0; debug11 < 2; debug11++) {
/* 1756 */           generateBox(debug1, debug5, debug8, 0, 7, debug8, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1757 */           generateBox(debug1, debug5, debug8, 0, 14, debug8, 2, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1758 */           debug8 = 14;
/*      */         } 
/* 1760 */         generateBox(debug1, debug5, 8, 3, 8, 8, 3, 13, BASE_BLACK, BASE_BLACK, false);
/* 1761 */         generateBox(debug1, debug5, 14, 3, 8, 14, 3, 13, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1763 */         spawnElder(debug1, debug5, 11, 5, 13);
/*      */       } 
/*      */       
/* 1766 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentPenthouse extends OceanMonumentPiece {
/*      */     public OceanMonumentPenthouse(Direction debug1, BoundingBox debug2) {
/* 1772 */       super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, debug1, debug2);
/*      */     }
/*      */     
/*      */     public OceanMonumentPenthouse(StructureManager debug1, CompoundTag debug2) {
/* 1776 */       super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 1781 */       generateBox(debug1, debug5, 2, -1, 2, 11, -1, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1782 */       generateBox(debug1, debug5, 0, -1, 0, 1, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1783 */       generateBox(debug1, debug5, 12, -1, 0, 13, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1784 */       generateBox(debug1, debug5, 2, -1, 0, 11, -1, 1, BASE_GRAY, BASE_GRAY, false);
/* 1785 */       generateBox(debug1, debug5, 2, -1, 12, 11, -1, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       
/* 1787 */       generateBox(debug1, debug5, 0, 0, 0, 0, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1788 */       generateBox(debug1, debug5, 13, 0, 0, 13, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1789 */       generateBox(debug1, debug5, 1, 0, 0, 12, 0, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1790 */       generateBox(debug1, debug5, 1, 0, 13, 12, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       int debug8;
/* 1792 */       for (debug8 = 2; debug8 <= 11; debug8 += 3) {
/* 1793 */         placeBlock(debug1, LAMP_BLOCK, 0, 0, debug8, debug5);
/* 1794 */         placeBlock(debug1, LAMP_BLOCK, 13, 0, debug8, debug5);
/* 1795 */         placeBlock(debug1, LAMP_BLOCK, debug8, 0, 0, debug5);
/*      */       } 
/*      */       
/* 1798 */       generateBox(debug1, debug5, 2, 0, 3, 4, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1799 */       generateBox(debug1, debug5, 9, 0, 3, 11, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1800 */       generateBox(debug1, debug5, 4, 0, 9, 9, 0, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1801 */       placeBlock(debug1, BASE_LIGHT, 5, 0, 8, debug5);
/* 1802 */       placeBlock(debug1, BASE_LIGHT, 8, 0, 8, debug5);
/* 1803 */       placeBlock(debug1, BASE_LIGHT, 10, 0, 10, debug5);
/* 1804 */       placeBlock(debug1, BASE_LIGHT, 3, 0, 10, debug5);
/* 1805 */       generateBox(debug1, debug5, 3, 0, 3, 3, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1806 */       generateBox(debug1, debug5, 10, 0, 3, 10, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1807 */       generateBox(debug1, debug5, 6, 0, 10, 7, 0, 10, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1809 */       debug8 = 3;
/* 1810 */       for (int debug9 = 0; debug9 < 2; debug9++) {
/* 1811 */         for (int debug10 = 2; debug10 <= 8; debug10 += 3) {
/* 1812 */           generateBox(debug1, debug5, debug8, 0, debug10, debug8, 2, debug10, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1814 */         debug8 = 10;
/*      */       } 
/* 1816 */       generateBox(debug1, debug5, 5, 0, 10, 5, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1817 */       generateBox(debug1, debug5, 8, 0, 10, 8, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1819 */       generateBox(debug1, debug5, 6, -1, 7, 7, -1, 8, BASE_BLACK, BASE_BLACK, false);
/*      */ 
/*      */       
/* 1822 */       generateWaterBox(debug1, debug5, 6, -1, 3, 7, -1, 4);
/*      */       
/* 1824 */       spawnElder(debug1, debug5, 6, 1, 6);
/*      */       
/* 1826 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   static class RoomDefinition {
/*      */     private final int index;
/* 1832 */     private final RoomDefinition[] connections = new RoomDefinition[6];
/* 1833 */     private final boolean[] hasOpening = new boolean[6];
/*      */     private boolean claimed;
/*      */     private boolean isSource;
/*      */     private int scanIndex;
/*      */     
/*      */     public RoomDefinition(int debug1) {
/* 1839 */       this.index = debug1;
/*      */     }
/*      */     
/*      */     public void setConnection(Direction debug1, RoomDefinition debug2) {
/* 1843 */       this.connections[debug1.get3DDataValue()] = debug2;
/* 1844 */       debug2.connections[debug1.getOpposite().get3DDataValue()] = this;
/*      */     }
/*      */     
/*      */     public void updateOpenings() {
/* 1848 */       for (int debug1 = 0; debug1 < 6; debug1++) {
/* 1849 */         this.hasOpening[debug1] = (this.connections[debug1] != null);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean findSource(int debug1) {
/* 1854 */       if (this.isSource) {
/* 1855 */         return true;
/*      */       }
/* 1857 */       this.scanIndex = debug1;
/* 1858 */       for (int debug2 = 0; debug2 < 6; debug2++) {
/* 1859 */         if (this.connections[debug2] != null && this.hasOpening[debug2] && 
/* 1860 */           (this.connections[debug2]).scanIndex != debug1 && this.connections[debug2].findSource(debug1)) {
/* 1861 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1865 */       return false;
/*      */     }
/*      */     
/*      */     public boolean isSpecial() {
/* 1869 */       return (this.index >= 75);
/*      */     }
/*      */     
/*      */     public int countOpenings() {
/* 1873 */       int debug1 = 0;
/* 1874 */       for (int debug2 = 0; debug2 < 6; debug2++) {
/* 1875 */         if (this.hasOpening[debug2]) {
/* 1876 */           debug1++;
/*      */         }
/*      */       } 
/* 1879 */       return debug1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class FitSimpleRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/*      */     private FitSimpleRoom() {}
/*      */ 
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1892 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1897 */       debug2.claimed = true;
/* 1898 */       return new OceanMonumentPieces.OceanMonumentSimpleRoom(debug1, debug2, debug3);
/*      */     }
/*      */   }
/*      */   
/*      */   static class FitSimpleTopRoom implements MonumentRoomFitter { private FitSimpleTopRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1905 */       return (!debug1.hasOpening[Direction.WEST.get3DDataValue()] && !debug1.hasOpening[Direction.EAST.get3DDataValue()] && !debug1.hasOpening[Direction.NORTH.get3DDataValue()] && !debug1.hasOpening[Direction.SOUTH.get3DDataValue()] && !debug1.hasOpening[Direction.UP.get3DDataValue()]);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1910 */       debug2.claimed = true;
/* 1911 */       return new OceanMonumentPieces.OceanMonumentSimpleTopRoom(debug1, debug2);
/*      */     } }
/*      */   
/*      */   static class FitDoubleYRoom implements MonumentRoomFitter {
/*      */     private FitDoubleYRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1918 */       return (debug1.hasOpening[Direction.UP.get3DDataValue()] && !(debug1.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1923 */       debug2.claimed = true;
/* 1924 */       (debug2.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1925 */       return new OceanMonumentPieces.OceanMonumentDoubleYRoom(debug1, debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   static class FitDoubleXRoom implements MonumentRoomFitter { private FitDoubleXRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1932 */       return (debug1.hasOpening[Direction.EAST.get3DDataValue()] && !(debug1.connections[Direction.EAST.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1937 */       debug2.claimed = true;
/* 1938 */       (debug2.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1939 */       return new OceanMonumentPieces.OceanMonumentDoubleXRoom(debug1, debug2);
/*      */     } }
/*      */   
/*      */   static class FitDoubleZRoom implements MonumentRoomFitter {
/*      */     private FitDoubleZRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1946 */       return (debug1.hasOpening[Direction.NORTH.get3DDataValue()] && !(debug1.connections[Direction.NORTH.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1951 */       OceanMonumentPieces.RoomDefinition debug4 = debug2;
/* 1952 */       if (!debug2.hasOpening[Direction.NORTH.get3DDataValue()] || (debug2.connections[Direction.NORTH.get3DDataValue()]).claimed) {
/* 1953 */         debug4 = debug2.connections[Direction.SOUTH.get3DDataValue()];
/*      */       }
/* 1955 */       debug4.claimed = true;
/* 1956 */       (debug4.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1957 */       return new OceanMonumentPieces.OceanMonumentDoubleZRoom(debug1, debug4);
/*      */     }
/*      */   }
/*      */   
/*      */   static class FitDoubleXYRoom implements MonumentRoomFitter { private FitDoubleXYRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1964 */       if (debug1.hasOpening[Direction.EAST.get3DDataValue()] && !(debug1.connections[Direction.EAST.get3DDataValue()]).claimed && 
/* 1965 */         debug1.hasOpening[Direction.UP.get3DDataValue()] && !(debug1.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1966 */         OceanMonumentPieces.RoomDefinition debug2 = debug1.connections[Direction.EAST.get3DDataValue()];
/*      */         
/* 1968 */         return (debug2.hasOpening[Direction.UP.get3DDataValue()] && !(debug2.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1971 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1976 */       debug2.claimed = true;
/* 1977 */       (debug2.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1978 */       (debug2.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1979 */       ((debug2.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1980 */       return new OceanMonumentPieces.OceanMonumentDoubleXYRoom(debug1, debug2);
/*      */     } }
/*      */   
/*      */   static class FitDoubleYZRoom implements MonumentRoomFitter {
/*      */     private FitDoubleYZRoom() {}
/*      */     
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition debug1) {
/* 1987 */       if (debug1.hasOpening[Direction.NORTH.get3DDataValue()] && !(debug1.connections[Direction.NORTH.get3DDataValue()]).claimed && 
/* 1988 */         debug1.hasOpening[Direction.UP.get3DDataValue()] && !(debug1.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1989 */         OceanMonumentPieces.RoomDefinition debug2 = debug1.connections[Direction.NORTH.get3DDataValue()];
/*      */         
/* 1991 */         return (debug2.hasOpening[Direction.UP.get3DDataValue()] && !(debug2.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1994 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction debug1, OceanMonumentPieces.RoomDefinition debug2, Random debug3) {
/* 1999 */       debug2.claimed = true;
/* 2000 */       (debug2.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 2001 */       (debug2.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 2002 */       ((debug2.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 2003 */       return new OceanMonumentPieces.OceanMonumentDoubleYZRoom(debug1, debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   static interface MonumentRoomFitter {
/*      */     boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition);
/*      */     
/*      */     OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, Random param1Random);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\OceanMonumentPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */