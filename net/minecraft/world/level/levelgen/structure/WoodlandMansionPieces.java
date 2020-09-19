/*      */ package net.minecraft.world.level.levelgen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.util.Tuple;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.monster.AbstractIllager;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChestBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class WoodlandMansionPieces {
/*      */   public static class WoodlandMansionPiece
/*      */     extends TemplateStructurePiece {
/*      */     private final String templateName;
/*      */     
/*      */     public WoodlandMansionPiece(StructureManager debug1, String debug2, BlockPos debug3, Rotation debug4) {
/*   39 */       this(debug1, debug2, debug3, debug4, Mirror.NONE);
/*      */     }
/*      */     private final Rotation rotation; private final Mirror mirror;
/*      */     public WoodlandMansionPiece(StructureManager debug1, String debug2, BlockPos debug3, Rotation debug4, Mirror debug5) {
/*   43 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, 0);
/*      */       
/*   45 */       this.templateName = debug2;
/*   46 */       this.templatePosition = debug3;
/*   47 */       this.rotation = debug4;
/*   48 */       this.mirror = debug5;
/*      */       
/*   50 */       loadTemplate(debug1);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureManager debug1, CompoundTag debug2) {
/*   54 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, debug2);
/*      */       
/*   56 */       this.templateName = debug2.getString("Template");
/*   57 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/*   58 */       this.mirror = Mirror.valueOf(debug2.getString("Mi"));
/*      */       
/*   60 */       loadTemplate(debug1);
/*      */     }
/*      */     
/*      */     private void loadTemplate(StructureManager debug1) {
/*   64 */       StructureTemplate debug2 = debug1.getOrCreate(new ResourceLocation("woodland_mansion/" + this.templateName));
/*   65 */       StructurePlaceSettings debug3 = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*      */       
/*   67 */       setup(debug2, this.templatePosition, debug3);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*   72 */       super.addAdditionalSaveData(debug1);
/*      */       
/*   74 */       debug1.putString("Template", this.templateName);
/*   75 */       debug1.putString("Rot", this.placeSettings.getRotation().name());
/*   76 */       debug1.putString("Mi", this.placeSettings.getMirror().name());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {
/*   81 */       if (debug1.startsWith("Chest")) {
/*   82 */         Rotation debug6 = this.placeSettings.getRotation();
/*   83 */         BlockState debug7 = Blocks.CHEST.defaultBlockState();
/*   84 */         if ("ChestWest".equals(debug1)) {
/*   85 */           debug7 = (BlockState)debug7.setValue((Property)ChestBlock.FACING, (Comparable)debug6.rotate(Direction.WEST));
/*   86 */         } else if ("ChestEast".equals(debug1)) {
/*   87 */           debug7 = (BlockState)debug7.setValue((Property)ChestBlock.FACING, (Comparable)debug6.rotate(Direction.EAST));
/*   88 */         } else if ("ChestSouth".equals(debug1)) {
/*   89 */           debug7 = (BlockState)debug7.setValue((Property)ChestBlock.FACING, (Comparable)debug6.rotate(Direction.SOUTH));
/*   90 */         } else if ("ChestNorth".equals(debug1)) {
/*   91 */           debug7 = (BlockState)debug7.setValue((Property)ChestBlock.FACING, (Comparable)debug6.rotate(Direction.NORTH));
/*      */         } 
/*   93 */         createChest(debug3, debug5, debug4, debug2, BuiltInLootTables.WOODLAND_MANSION, debug7);
/*      */       } else {
/*      */         AbstractIllager debug6;
/*   96 */         switch (debug1) {
/*      */           case "Mage":
/*   98 */             debug6 = (AbstractIllager)EntityType.EVOKER.create((Level)debug3.getLevel());
/*      */             break;
/*      */           case "Warrior":
/*  101 */             debug6 = (AbstractIllager)EntityType.VINDICATOR.create((Level)debug3.getLevel());
/*      */             break;
/*      */           
/*      */           default:
/*      */             return;
/*      */         } 
/*  107 */         debug6.setPersistenceRequired();
/*  108 */         debug6.moveTo(debug2, 0.0F, 0.0F);
/*  109 */         debug6.finalizeSpawn(debug3, debug3.getCurrentDifficultyAt(debug6.blockPosition()), MobSpawnType.STRUCTURE, null, null);
/*  110 */         debug3.addFreshEntityWithPassengers((Entity)debug6);
/*  111 */         debug3.setBlock(debug2, Blocks.AIR.defaultBlockState(), 2);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateMansion(StructureManager debug0, BlockPos debug1, Rotation debug2, List<WoodlandMansionPiece> debug3, Random debug4) {
/*  117 */     MansionGrid debug5 = new MansionGrid(debug4);
/*  118 */     MansionPiecePlacer debug6 = new MansionPiecePlacer(debug0, debug4);
/*  119 */     debug6.createMansion(debug1, debug2, debug3, debug5);
/*      */   }
/*      */   
/*      */   static class PlacementData {
/*      */     public Rotation rotation;
/*      */     public BlockPos position;
/*      */     public String wallType;
/*      */     
/*      */     private PlacementData() {} }
/*      */   
/*      */   static class MansionPiecePlacer {
/*      */     private final StructureManager structureManager;
/*      */     private final Random random;
/*      */     private int startX;
/*      */     private int startY;
/*      */     
/*      */     public MansionPiecePlacer(StructureManager debug1, Random debug2) {
/*  136 */       this.structureManager = debug1;
/*  137 */       this.random = debug2;
/*      */     }
/*      */     
/*      */     public void createMansion(BlockPos debug1, Rotation debug2, List<WoodlandMansionPieces.WoodlandMansionPiece> debug3, WoodlandMansionPieces.MansionGrid debug4) {
/*  141 */       WoodlandMansionPieces.PlacementData debug5 = new WoodlandMansionPieces.PlacementData();
/*  142 */       debug5.position = debug1;
/*  143 */       debug5.rotation = debug2;
/*  144 */       debug5.wallType = "wall_flat";
/*      */       
/*  146 */       WoodlandMansionPieces.PlacementData debug6 = new WoodlandMansionPieces.PlacementData();
/*      */ 
/*      */       
/*  149 */       entrance(debug3, debug5);
/*  150 */       debug6.position = debug5.position.above(8);
/*  151 */       debug6.rotation = debug5.rotation;
/*  152 */       debug6.wallType = "wall_window";
/*      */       
/*  154 */       if (!debug3.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  158 */       WoodlandMansionPieces.SimpleGrid debug7 = debug4.baseGrid;
/*  159 */       WoodlandMansionPieces.SimpleGrid debug8 = debug4.thirdFloorGrid;
/*      */       
/*  161 */       this.startX = debug4.entranceX + 1;
/*  162 */       this.startY = debug4.entranceY + 1;
/*  163 */       int debug9 = debug4.entranceX + 1;
/*  164 */       int debug10 = debug4.entranceY;
/*      */       
/*  166 */       traverseOuterWalls(debug3, debug5, debug7, Direction.SOUTH, this.startX, this.startY, debug9, debug10);
/*  167 */       traverseOuterWalls(debug3, debug6, debug7, Direction.SOUTH, this.startX, this.startY, debug9, debug10);
/*      */ 
/*      */       
/*  170 */       WoodlandMansionPieces.PlacementData debug11 = new WoodlandMansionPieces.PlacementData();
/*  171 */       debug11.position = debug5.position.above(19);
/*  172 */       debug11.rotation = debug5.rotation;
/*  173 */       debug11.wallType = "wall_window";
/*      */       
/*  175 */       boolean debug12 = false;
/*  176 */       for (int i = 0; i < debug8.height && !debug12; i++) {
/*  177 */         for (int j = debug8.width - 1; j >= 0 && !debug12; j--) {
/*  178 */           if (WoodlandMansionPieces.MansionGrid.isHouse(debug8, j, i)) {
/*  179 */             debug11.position = debug11.position.relative(debug2.rotate(Direction.SOUTH), 8 + (i - this.startY) * 8);
/*  180 */             debug11.position = debug11.position.relative(debug2.rotate(Direction.EAST), (j - this.startX) * 8);
/*  181 */             traverseWallPiece(debug3, debug11);
/*  182 */             traverseOuterWalls(debug3, debug11, debug8, Direction.SOUTH, j, i, j, i);
/*  183 */             debug12 = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  189 */       createRoof(debug3, debug1.above(16), debug2, debug7, debug8);
/*  190 */       createRoof(debug3, debug1.above(27), debug2, debug8, null);
/*      */       
/*  192 */       if (!debug3.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  196 */       WoodlandMansionPieces.FloorRoomCollection[] debug13 = new WoodlandMansionPieces.FloorRoomCollection[3];
/*  197 */       debug13[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/*  198 */       debug13[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/*  199 */       debug13[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*      */       
/*  201 */       for (int debug14 = 0; debug14 < 3; debug14++) {
/*  202 */         BlockPos debug15 = debug1.above(8 * debug14 + ((debug14 == 2) ? 3 : 0));
/*  203 */         WoodlandMansionPieces.SimpleGrid debug16 = debug4.floorRooms[debug14];
/*  204 */         WoodlandMansionPieces.SimpleGrid debug17 = (debug14 == 2) ? debug8 : debug7;
/*      */ 
/*      */         
/*  207 */         String debug18 = (debug14 == 0) ? "carpet_south_1" : "carpet_south_2";
/*  208 */         String debug19 = (debug14 == 0) ? "carpet_west_1" : "carpet_west_2";
/*  209 */         for (int j = 0; j < debug17.height; j++) {
/*  210 */           for (int k = 0; k < debug17.width; k++) {
/*  211 */             if (debug17.get(k, j) == 1) {
/*  212 */               BlockPos blockPos = debug15.relative(debug2.rotate(Direction.SOUTH), 8 + (j - this.startY) * 8);
/*  213 */               blockPos = blockPos.relative(debug2.rotate(Direction.EAST), (k - this.startX) * 8);
/*  214 */               debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "corridor_floor", blockPos, debug2));
/*      */               
/*  216 */               if (debug17.get(k, j - 1) == 1 || (debug16.get(k, j - 1) & 0x800000) == 8388608) {
/*  217 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "carpet_north", blockPos.relative(debug2.rotate(Direction.EAST), 1).above(), debug2));
/*      */               }
/*  219 */               if (debug17.get(k + 1, j) == 1 || (debug16.get(k + 1, j) & 0x800000) == 8388608) {
/*  220 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "carpet_east", blockPos.relative(debug2.rotate(Direction.SOUTH), 1).relative(debug2.rotate(Direction.EAST), 5).above(), debug2));
/*      */               }
/*  222 */               if (debug17.get(k, j + 1) == 1 || (debug16.get(k, j + 1) & 0x800000) == 8388608) {
/*  223 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug18, blockPos.relative(debug2.rotate(Direction.SOUTH), 5).relative(debug2.rotate(Direction.WEST), 1), debug2));
/*      */               }
/*  225 */               if (debug17.get(k - 1, j) == 1 || (debug16.get(k - 1, j) & 0x800000) == 8388608) {
/*  226 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug19, blockPos.relative(debug2.rotate(Direction.WEST), 1).relative(debug2.rotate(Direction.NORTH), 1), debug2));
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  232 */         String debug20 = (debug14 == 0) ? "indoors_wall_1" : "indoors_wall_2";
/*  233 */         String debug21 = (debug14 == 0) ? "indoors_door_1" : "indoors_door_2";
/*  234 */         List<Direction> debug22 = Lists.newArrayList();
/*  235 */         for (int debug23 = 0; debug23 < debug17.height; debug23++) {
/*  236 */           for (int debug24 = 0; debug24 < debug17.width; debug24++) {
/*  237 */             boolean debug25 = (debug14 == 2 && debug17.get(debug24, debug23) == 3);
/*  238 */             if (debug17.get(debug24, debug23) == 2 || debug25) {
/*  239 */               int debug26 = debug16.get(debug24, debug23);
/*  240 */               int debug27 = debug26 & 0xF0000;
/*  241 */               int debug28 = debug26 & 0xFFFF;
/*      */ 
/*      */               
/*  244 */               debug25 = (debug25 && (debug26 & 0x800000) == 8388608);
/*      */               
/*  246 */               debug22.clear();
/*  247 */               if ((debug26 & 0x200000) == 2097152) {
/*  248 */                 for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  249 */                   if (debug17.get(debug24 + direction.getStepX(), debug23 + direction.getStepZ()) == 1) {
/*  250 */                     debug22.add(direction);
/*      */                   }
/*      */                 } 
/*      */               }
/*  254 */               Direction debug29 = null;
/*  255 */               if (!debug22.isEmpty()) {
/*  256 */                 debug29 = debug22.get(this.random.nextInt(debug22.size()));
/*  257 */               } else if ((debug26 & 0x100000) == 1048576) {
/*      */                 
/*  259 */                 debug29 = Direction.UP;
/*      */               } 
/*      */               
/*  262 */               BlockPos debug30 = debug15.relative(debug2.rotate(Direction.SOUTH), 8 + (debug23 - this.startY) * 8);
/*  263 */               debug30 = debug30.relative(debug2.rotate(Direction.EAST), -1 + (debug24 - this.startX) * 8);
/*      */               
/*  265 */               if (WoodlandMansionPieces.MansionGrid.isHouse(debug17, debug24 - 1, debug23) && !debug4.isRoomId(debug17, debug24 - 1, debug23, debug14, debug28)) {
/*  266 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, (debug29 == Direction.WEST) ? debug21 : debug20, debug30, debug2));
/*      */               }
/*  268 */               if (debug17.get(debug24 + 1, debug23) == 1 && !debug25) {
/*  269 */                 BlockPos debug31 = debug30.relative(debug2.rotate(Direction.EAST), 8);
/*  270 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, (debug29 == Direction.EAST) ? debug21 : debug20, debug31, debug2));
/*      */               } 
/*  272 */               if (WoodlandMansionPieces.MansionGrid.isHouse(debug17, debug24, debug23 + 1) && !debug4.isRoomId(debug17, debug24, debug23 + 1, debug14, debug28)) {
/*  273 */                 BlockPos debug31 = debug30.relative(debug2.rotate(Direction.SOUTH), 7);
/*  274 */                 debug31 = debug31.relative(debug2.rotate(Direction.EAST), 7);
/*  275 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, (debug29 == Direction.SOUTH) ? debug21 : debug20, debug31, debug2.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  277 */               if (debug17.get(debug24, debug23 - 1) == 1 && !debug25) {
/*  278 */                 BlockPos debug31 = debug30.relative(debug2.rotate(Direction.NORTH), 1);
/*  279 */                 debug31 = debug31.relative(debug2.rotate(Direction.EAST), 7);
/*  280 */                 debug3.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, (debug29 == Direction.NORTH) ? debug21 : debug20, debug31, debug2.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  283 */               if (debug27 == 65536) {
/*  284 */                 addRoom1x1(debug3, debug30, debug2, debug29, debug13[debug14]);
/*  285 */               } else if (debug27 == 131072 && debug29 != null) {
/*      */                 
/*  287 */                 Direction debug31 = debug4.get1x2RoomDirection(debug17, debug24, debug23, debug14, debug28);
/*  288 */                 boolean debug32 = ((debug26 & 0x400000) == 4194304);
/*  289 */                 addRoom1x2(debug3, debug30, debug2, debug31, debug29, debug13[debug14], debug32);
/*  290 */               } else if (debug27 == 262144 && debug29 != null && debug29 != Direction.UP) {
/*      */                 
/*  292 */                 Direction debug31 = debug29.getClockWise();
/*  293 */                 if (!debug4.isRoomId(debug17, debug24 + debug31.getStepX(), debug23 + debug31.getStepZ(), debug14, debug28)) {
/*  294 */                   debug31 = debug31.getOpposite();
/*      */                 }
/*  296 */                 addRoom2x2(debug3, debug30, debug2, debug31, debug29, debug13[debug14]);
/*  297 */               } else if (debug27 == 262144 && debug29 == Direction.UP) {
/*  298 */                 addRoom2x2Secret(debug3, debug30, debug2, debug13[debug14]);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, WoodlandMansionPieces.PlacementData debug2, WoodlandMansionPieces.SimpleGrid debug3, Direction debug4, int debug5, int debug6, int debug7, int debug8) {
/*  307 */       int debug9 = debug5;
/*  308 */       int debug10 = debug6;
/*  309 */       Direction debug11 = debug4;
/*      */       
/*      */       do {
/*  312 */         if (!WoodlandMansionPieces.MansionGrid.isHouse(debug3, debug9 + debug4.getStepX(), debug10 + debug4.getStepZ())) {
/*      */           
/*  314 */           traverseTurn(debug1, debug2);
/*  315 */           debug4 = debug4.getClockWise();
/*  316 */           if (debug9 != debug7 || debug10 != debug8 || debug11 != debug4) {
/*  317 */             traverseWallPiece(debug1, debug2);
/*      */           }
/*  319 */         } else if (WoodlandMansionPieces.MansionGrid.isHouse(debug3, debug9 + debug4.getStepX(), debug10 + debug4.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(debug3, debug9 + debug4.getStepX() + debug4.getCounterClockWise().getStepX(), debug10 + debug4.getStepZ() + debug4.getCounterClockWise().getStepZ())) {
/*      */           
/*  321 */           traverseInnerTurn(debug1, debug2);
/*  322 */           debug9 += debug4.getStepX();
/*  323 */           debug10 += debug4.getStepZ();
/*  324 */           debug4 = debug4.getCounterClockWise();
/*      */         } else {
/*  326 */           debug9 += debug4.getStepX();
/*  327 */           debug10 += debug4.getStepZ();
/*  328 */           if (debug9 != debug7 || debug10 != debug8 || debug11 != debug4) {
/*  329 */             traverseWallPiece(debug1, debug2);
/*      */           }
/*      */         } 
/*  332 */       } while (debug9 != debug7 || debug10 != debug8 || debug11 != debug4);
/*      */     }
/*      */     
/*      */     private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, BlockPos debug2, Rotation debug3, WoodlandMansionPieces.SimpleGrid debug4, @Nullable WoodlandMansionPieces.SimpleGrid debug5) {
/*      */       int debug6;
/*  337 */       for (debug6 = 0; debug6 < debug4.height; debug6++) {
/*  338 */         for (int debug7 = 0; debug7 < debug4.width; debug7++) {
/*  339 */           BlockPos debug8 = debug2;
/*  340 */           debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 8 + (debug6 - this.startY) * 8);
/*  341 */           debug8 = debug8.relative(debug3.rotate(Direction.EAST), (debug7 - this.startX) * 8);
/*      */ 
/*      */           
/*  344 */           boolean debug9 = (debug5 != null && WoodlandMansionPieces.MansionGrid.isHouse(debug5, debug7, debug6));
/*      */           
/*  346 */           if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6) && !debug9) {
/*  347 */             debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof", debug8.above(3), debug3));
/*      */             
/*  349 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6)) {
/*  350 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 6);
/*  351 */               debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_front", debug10, debug3));
/*      */             } 
/*  353 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6)) {
/*  354 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 0);
/*  355 */               debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 7);
/*  356 */               debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_front", debug10, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*      */             } 
/*  358 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  359 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.WEST), 1);
/*  360 */               debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_front", debug10, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */             } 
/*  362 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  363 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 6);
/*  364 */               debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 6);
/*  365 */               debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_front", debug10, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  371 */       if (debug5 != null) {
/*  372 */         for (debug6 = 0; debug6 < debug4.height; debug6++) {
/*  373 */           for (int debug7 = 0; debug7 < debug4.width; debug7++) {
/*  374 */             BlockPos debug8 = debug2;
/*  375 */             debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 8 + (debug6 - this.startY) * 8);
/*  376 */             debug8 = debug8.relative(debug3.rotate(Direction.EAST), (debug7 - this.startX) * 8);
/*      */ 
/*      */             
/*  379 */             boolean debug9 = WoodlandMansionPieces.MansionGrid.isHouse(debug5, debug7, debug6);
/*      */             
/*  381 */             if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6) && debug9) {
/*      */               
/*  383 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6)) {
/*  384 */                 BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 7);
/*  385 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall", debug10, debug3));
/*      */               } 
/*  387 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6)) {
/*  388 */                 BlockPos debug10 = debug8.relative(debug3.rotate(Direction.WEST), 1);
/*  389 */                 debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 6);
/*  390 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall", debug10, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*  392 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  393 */                 BlockPos debug10 = debug8.relative(debug3.rotate(Direction.WEST), 0);
/*  394 */                 debug10 = debug10.relative(debug3.rotate(Direction.NORTH), 1);
/*  395 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall", debug10, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  397 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  398 */                 BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 6);
/*  399 */                 debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 7);
/*  400 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall", debug10, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  403 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6)) {
/*  404 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  405 */                   BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 7);
/*  406 */                   debug10 = debug10.relative(debug3.rotate(Direction.NORTH), 2);
/*  407 */                   debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall_corner", debug10, debug3));
/*      */                 } 
/*  409 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  410 */                   BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 8);
/*  411 */                   debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 7);
/*  412 */                   debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall_corner", debug10, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*      */                 } 
/*      */               } 
/*  415 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6)) {
/*  416 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  417 */                   BlockPos debug10 = debug8.relative(debug3.rotate(Direction.WEST), 2);
/*  418 */                   debug10 = debug10.relative(debug3.rotate(Direction.NORTH), 1);
/*  419 */                   debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall_corner", debug10, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */                 } 
/*  421 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  422 */                   BlockPos debug10 = debug8.relative(debug3.rotate(Direction.WEST), 1);
/*  423 */                   debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 8);
/*  424 */                   debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "small_wall_corner", debug10, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  432 */       for (debug6 = 0; debug6 < debug4.height; debug6++) {
/*  433 */         for (int debug7 = 0; debug7 < debug4.width; debug7++) {
/*  434 */           BlockPos debug8 = debug2;
/*  435 */           debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 8 + (debug6 - this.startY) * 8);
/*  436 */           debug8 = debug8.relative(debug3.rotate(Direction.EAST), (debug7 - this.startX) * 8);
/*      */ 
/*      */           
/*  439 */           boolean debug9 = (debug5 != null && WoodlandMansionPieces.MansionGrid.isHouse(debug5, debug7, debug6));
/*      */           
/*  441 */           if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6) && !debug9) {
/*  442 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6)) {
/*  443 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 6);
/*  444 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  445 */                 BlockPos debug11 = debug10.relative(debug3.rotate(Direction.SOUTH), 6);
/*  446 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_corner", debug11, debug3));
/*  447 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6 + 1)) {
/*  448 */                 BlockPos debug11 = debug10.relative(debug3.rotate(Direction.SOUTH), 5);
/*  449 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_inner_corner", debug11, debug3));
/*      */               } 
/*  451 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  452 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_corner", debug10, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  453 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 + 1, debug6 - 1)) {
/*  454 */                 BlockPos debug11 = debug8.relative(debug3.rotate(Direction.EAST), 9);
/*  455 */                 debug11 = debug11.relative(debug3.rotate(Direction.NORTH), 2);
/*  456 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_inner_corner", debug11, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */             } 
/*  459 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6)) {
/*  460 */               BlockPos debug10 = debug8.relative(debug3.rotate(Direction.EAST), 0);
/*  461 */               debug10 = debug10.relative(debug3.rotate(Direction.SOUTH), 0);
/*  462 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 + 1)) {
/*  463 */                 BlockPos debug11 = debug10.relative(debug3.rotate(Direction.SOUTH), 6);
/*  464 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_corner", debug11, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*  465 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6 + 1)) {
/*  466 */                 BlockPos debug11 = debug10.relative(debug3.rotate(Direction.SOUTH), 8);
/*  467 */                 debug11 = debug11.relative(debug3.rotate(Direction.WEST), 3);
/*  468 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_inner_corner", debug11, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  470 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7, debug6 - 1)) {
/*  471 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_corner", debug10, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*  472 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(debug4, debug7 - 1, debug6 - 1)) {
/*  473 */                 BlockPos debug11 = debug10.relative(debug3.rotate(Direction.SOUTH), 1);
/*  474 */                 debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "roof_inner_corner", debug11, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, WoodlandMansionPieces.PlacementData debug2) {
/*  483 */       Direction debug3 = debug2.rotation.rotate(Direction.WEST);
/*  484 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "entrance", debug2.position.relative(debug3, 9), debug2.rotation));
/*  485 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.SOUTH), 16);
/*      */     }
/*      */     
/*      */     private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, WoodlandMansionPieces.PlacementData debug2) {
/*  489 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug2.wallType, debug2.position.relative(debug2.rotation.rotate(Direction.EAST), 7), debug2.rotation));
/*  490 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.SOUTH), 8);
/*      */     }
/*      */     
/*      */     private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, WoodlandMansionPieces.PlacementData debug2) {
/*  494 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.SOUTH), -1);
/*  495 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, "wall_corner", debug2.position, debug2.rotation));
/*  496 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.SOUTH), -7);
/*  497 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.WEST), -6);
/*  498 */       debug2.rotation = debug2.rotation.getRotated(Rotation.CLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, WoodlandMansionPieces.PlacementData debug2) {
/*  502 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.SOUTH), 6);
/*  503 */       debug2.position = debug2.position.relative(debug2.rotation.rotate(Direction.EAST), 8);
/*  504 */       debug2.rotation = debug2.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, BlockPos debug2, Rotation debug3, Direction debug4, WoodlandMansionPieces.FloorRoomCollection debug5) {
/*  508 */       Rotation debug6 = Rotation.NONE;
/*  509 */       String debug7 = debug5.get1x1(this.random);
/*  510 */       if (debug4 != Direction.EAST) {
/*  511 */         if (debug4 == Direction.NORTH) {
/*  512 */           debug6 = debug6.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  513 */         } else if (debug4 == Direction.WEST) {
/*  514 */           debug6 = debug6.getRotated(Rotation.CLOCKWISE_180);
/*  515 */         } else if (debug4 == Direction.SOUTH) {
/*  516 */           debug6 = debug6.getRotated(Rotation.CLOCKWISE_90);
/*      */         } else {
/*      */           
/*  519 */           debug7 = debug5.get1x1Secret(this.random);
/*      */         } 
/*      */       }
/*  522 */       BlockPos debug8 = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, debug6, 7, 7);
/*  523 */       debug6 = debug6.getRotated(debug3);
/*  524 */       debug8 = debug8.rotate(debug3);
/*  525 */       BlockPos debug9 = debug2.offset(debug8.getX(), 0, debug8.getZ());
/*  526 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug7, debug9, debug6));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, BlockPos debug2, Rotation debug3, Direction debug4, Direction debug5, WoodlandMansionPieces.FloorRoomCollection debug6, boolean debug7) {
/*  533 */       if (debug5 == Direction.EAST && debug4 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  536 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  537 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3));
/*  538 */       } else if (debug5 == Direction.EAST && debug4 == Direction.NORTH) {
/*      */ 
/*      */         
/*  541 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  542 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 6);
/*  543 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3, Mirror.LEFT_RIGHT));
/*  544 */       } else if (debug5 == Direction.WEST && debug4 == Direction.NORTH) {
/*      */ 
/*      */         
/*  547 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 7);
/*  548 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 6);
/*  549 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*  550 */       } else if (debug5 == Direction.WEST && debug4 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  553 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 7);
/*  554 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3, Mirror.FRONT_BACK));
/*  555 */       } else if (debug5 == Direction.SOUTH && debug4 == Direction.EAST) {
/*      */ 
/*      */         
/*  558 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  559 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/*  560 */       } else if (debug5 == Direction.SOUTH && debug4 == Direction.WEST) {
/*      */ 
/*      */         
/*  563 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 7);
/*  564 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*  565 */       } else if (debug5 == Direction.NORTH && debug4 == Direction.WEST) {
/*      */ 
/*      */         
/*  568 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 7);
/*  569 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 6);
/*  570 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/*  571 */       } else if (debug5 == Direction.NORTH && debug4 == Direction.EAST) {
/*      */ 
/*      */         
/*  574 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  575 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 6);
/*  576 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2SideEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  577 */       } else if (debug5 == Direction.SOUTH && debug4 == Direction.NORTH) {
/*      */ 
/*      */ 
/*      */         
/*  581 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  582 */         debug8 = debug8.relative(debug3.rotate(Direction.NORTH), 8);
/*  583 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2FrontEntrance(this.random, debug7), debug8, debug3));
/*  584 */       } else if (debug5 == Direction.NORTH && debug4 == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  588 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 7);
/*  589 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 14);
/*  590 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2FrontEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_180)));
/*  591 */       } else if (debug5 == Direction.WEST && debug4 == Direction.EAST) {
/*      */         
/*  593 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 15);
/*  594 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2FrontEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*  595 */       } else if (debug5 == Direction.EAST && debug4 == Direction.WEST) {
/*      */         
/*  597 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.WEST), 7);
/*  598 */         debug8 = debug8.relative(debug3.rotate(Direction.SOUTH), 6);
/*  599 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2FrontEntrance(this.random, debug7), debug8, debug3.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  600 */       } else if (debug5 == Direction.UP && debug4 == Direction.EAST) {
/*      */         
/*  602 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 15);
/*  603 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2Secret(this.random), debug8, debug3.getRotated(Rotation.CLOCKWISE_90)));
/*  604 */       } else if (debug5 == Direction.UP && debug4 == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  608 */         BlockPos debug8 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  609 */         debug8 = debug8.relative(debug3.rotate(Direction.NORTH), 0);
/*  610 */         debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get1x2Secret(this.random), debug8, debug3));
/*      */       } 
/*      */     }
/*      */     
/*      */     private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, BlockPos debug2, Rotation debug3, Direction debug4, Direction debug5, WoodlandMansionPieces.FloorRoomCollection debug6) {
/*  615 */       int debug7 = 0;
/*  616 */       int debug8 = 0;
/*  617 */       Rotation debug9 = debug3;
/*  618 */       Mirror debug10 = Mirror.NONE;
/*      */ 
/*      */ 
/*      */       
/*  622 */       if (debug5 == Direction.EAST && debug4 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  625 */         debug7 = -7;
/*  626 */       } else if (debug5 == Direction.EAST && debug4 == Direction.NORTH) {
/*      */ 
/*      */         
/*  629 */         debug7 = -7;
/*  630 */         debug8 = 6;
/*  631 */         debug10 = Mirror.LEFT_RIGHT;
/*  632 */       } else if (debug5 == Direction.NORTH && debug4 == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  636 */         debug7 = 1;
/*  637 */         debug8 = 14;
/*  638 */         debug9 = debug3.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  639 */       } else if (debug5 == Direction.NORTH && debug4 == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  643 */         debug7 = 7;
/*  644 */         debug8 = 14;
/*  645 */         debug9 = debug3.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  646 */         debug10 = Mirror.LEFT_RIGHT;
/*  647 */       } else if (debug5 == Direction.SOUTH && debug4 == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  651 */         debug7 = 7;
/*  652 */         debug8 = -8;
/*  653 */         debug9 = debug3.getRotated(Rotation.CLOCKWISE_90);
/*  654 */       } else if (debug5 == Direction.SOUTH && debug4 == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  658 */         debug7 = 1;
/*  659 */         debug8 = -8;
/*  660 */         debug9 = debug3.getRotated(Rotation.CLOCKWISE_90);
/*  661 */         debug10 = Mirror.LEFT_RIGHT;
/*  662 */       } else if (debug5 == Direction.WEST && debug4 == Direction.NORTH) {
/*      */ 
/*      */         
/*  665 */         debug7 = 15;
/*  666 */         debug8 = 6;
/*  667 */         debug9 = debug3.getRotated(Rotation.CLOCKWISE_180);
/*  668 */       } else if (debug5 == Direction.WEST && debug4 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  671 */         debug7 = 15;
/*  672 */         debug10 = Mirror.FRONT_BACK;
/*      */       } 
/*      */       
/*  675 */       BlockPos debug11 = debug2.relative(debug3.rotate(Direction.EAST), debug7);
/*  676 */       debug11 = debug11.relative(debug3.rotate(Direction.SOUTH), debug8);
/*  677 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug6.get2x2(this.random), debug11, debug9, debug10));
/*      */     }
/*      */     
/*      */     private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> debug1, BlockPos debug2, Rotation debug3, WoodlandMansionPieces.FloorRoomCollection debug4) {
/*  681 */       BlockPos debug5 = debug2.relative(debug3.rotate(Direction.EAST), 1);
/*  682 */       debug1.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureManager, debug4.get2x2Secret(this.random), debug5, debug3, Mirror.NONE));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MansionGrid
/*      */   {
/*      */     private final Random random;
/*      */ 
/*      */ 
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid baseGrid;
/*      */ 
/*      */ 
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*      */ 
/*      */ 
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int entranceX;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int entranceY;
/*      */ 
/*      */ 
/*      */     
/*      */     public MansionGrid(Random debug1) {
/*  716 */       this.random = debug1;
/*      */       
/*  718 */       int debug2 = 11;
/*  719 */       this.entranceX = 7;
/*  720 */       this.entranceY = 4;
/*      */       
/*  722 */       this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  723 */       this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/*  724 */       this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/*  725 */       this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/*  726 */       this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/*  727 */       this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/*  728 */       this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/*  729 */       this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*      */       
/*  731 */       this.baseGrid.set(0, 0, 11, 1, 5);
/*  732 */       this.baseGrid.set(0, 9, 11, 11, 5);
/*      */       
/*  734 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/*  735 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/*  736 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/*  737 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/*  738 */       while (cleanEdges(this.baseGrid));
/*      */ 
/*      */       
/*  741 */       this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/*  742 */       this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  743 */       this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  744 */       this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  745 */       identifyRooms(this.baseGrid, this.floorRooms[0]);
/*  746 */       identifyRooms(this.baseGrid, this.floorRooms[1]);
/*      */ 
/*      */       
/*  749 */       this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*  750 */       this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*      */       
/*  752 */       this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/*  753 */       setupThirdFloor();
/*  754 */       identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*      */     }
/*      */     
/*      */     public static boolean isHouse(WoodlandMansionPieces.SimpleGrid debug0, int debug1, int debug2) {
/*  758 */       int debug3 = debug0.get(debug1, debug2);
/*  759 */       return (debug3 == 1 || debug3 == 2 || debug3 == 3 || debug3 == 4);
/*      */     }
/*      */     
/*      */     public boolean isRoomId(WoodlandMansionPieces.SimpleGrid debug1, int debug2, int debug3, int debug4, int debug5) {
/*  763 */       return ((this.floorRooms[debug4].get(debug2, debug3) & 0xFFFF) == debug5);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid debug1, int debug2, int debug3, int debug4, int debug5) {
/*  768 */       for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/*  769 */         if (isRoomId(debug1, debug2 + debug7.getStepX(), debug3 + debug7.getStepZ(), debug4, debug5)) {
/*  770 */           return debug7;
/*      */         }
/*      */       } 
/*  773 */       return null;
/*      */     }
/*      */     
/*      */     private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid debug1, int debug2, int debug3, Direction debug4, int debug5) {
/*  777 */       if (debug5 <= 0) {
/*      */         return;
/*      */       }
/*      */       
/*  781 */       debug1.set(debug2, debug3, 1);
/*  782 */       debug1.setif(debug2 + debug4.getStepX(), debug3 + debug4.getStepZ(), 0, 1);
/*      */       
/*  784 */       for (int i = 0; i < 8; i++) {
/*  785 */         Direction direction = Direction.from2DDataValue(this.random.nextInt(4));
/*  786 */         if (direction != debug4.getOpposite())
/*      */         {
/*      */           
/*  789 */           if (direction != Direction.EAST || !this.random.nextBoolean()) {
/*      */ 
/*      */ 
/*      */             
/*  793 */             int debug8 = debug2 + debug4.getStepX();
/*  794 */             int debug9 = debug3 + debug4.getStepZ();
/*  795 */             if (debug1.get(debug8 + direction.getStepX(), debug9 + direction.getStepZ()) == 0 && debug1.get(debug8 + direction.getStepX() * 2, debug9 + direction.getStepZ() * 2) == 0) {
/*  796 */               recursiveCorridor(debug1, debug2 + debug4.getStepX() + direction.getStepX(), debug3 + debug4.getStepZ() + direction.getStepZ(), direction, debug5 - 1); break;
/*      */             } 
/*      */           }  } 
/*      */       } 
/*  800 */       Direction debug6 = debug4.getClockWise();
/*  801 */       Direction debug7 = debug4.getCounterClockWise();
/*  802 */       debug1.setif(debug2 + debug6.getStepX(), debug3 + debug6.getStepZ(), 0, 2);
/*  803 */       debug1.setif(debug2 + debug7.getStepX(), debug3 + debug7.getStepZ(), 0, 2);
/*      */       
/*  805 */       debug1.setif(debug2 + debug4.getStepX() + debug6.getStepX(), debug3 + debug4.getStepZ() + debug6.getStepZ(), 0, 2);
/*  806 */       debug1.setif(debug2 + debug4.getStepX() + debug7.getStepX(), debug3 + debug4.getStepZ() + debug7.getStepZ(), 0, 2);
/*  807 */       debug1.setif(debug2 + debug4.getStepX() * 2, debug3 + debug4.getStepZ() * 2, 0, 2);
/*  808 */       debug1.setif(debug2 + debug6.getStepX() * 2, debug3 + debug6.getStepZ() * 2, 0, 2);
/*  809 */       debug1.setif(debug2 + debug7.getStepX() * 2, debug3 + debug7.getStepZ() * 2, 0, 2);
/*      */     }
/*      */     
/*      */     private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid debug1) {
/*  813 */       boolean debug2 = false;
/*  814 */       for (int debug3 = 0; debug3 < debug1.height; debug3++) {
/*  815 */         for (int debug4 = 0; debug4 < debug1.width; debug4++) {
/*  816 */           if (debug1.get(debug4, debug3) == 0) {
/*  817 */             int debug5 = 0;
/*  818 */             debug5 += isHouse(debug1, debug4 + 1, debug3) ? 1 : 0;
/*  819 */             debug5 += isHouse(debug1, debug4 - 1, debug3) ? 1 : 0;
/*  820 */             debug5 += isHouse(debug1, debug4, debug3 + 1) ? 1 : 0;
/*  821 */             debug5 += isHouse(debug1, debug4, debug3 - 1) ? 1 : 0;
/*      */             
/*  823 */             if (debug5 >= 3) {
/*      */               
/*  825 */               debug1.set(debug4, debug3, 2);
/*  826 */               debug2 = true;
/*  827 */             } else if (debug5 == 2) {
/*      */               
/*  829 */               int debug6 = 0;
/*  830 */               debug6 += isHouse(debug1, debug4 + 1, debug3 + 1) ? 1 : 0;
/*  831 */               debug6 += isHouse(debug1, debug4 - 1, debug3 + 1) ? 1 : 0;
/*  832 */               debug6 += isHouse(debug1, debug4 + 1, debug3 - 1) ? 1 : 0;
/*  833 */               debug6 += isHouse(debug1, debug4 - 1, debug3 - 1) ? 1 : 0;
/*  834 */               if (debug6 <= 1) {
/*  835 */                 debug1.set(debug4, debug3, 2);
/*  836 */                 debug2 = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  842 */       return debug2;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setupThirdFloor() {
/*  847 */       List<Tuple<Integer, Integer>> debug1 = Lists.newArrayList();
/*  848 */       WoodlandMansionPieces.SimpleGrid debug2 = this.floorRooms[1];
/*  849 */       for (int i = 0; i < this.thirdFloorGrid.height; i++) {
/*  850 */         for (int k = 0; k < this.thirdFloorGrid.width; k++) {
/*  851 */           int m = debug2.get(k, i);
/*  852 */           int n = m & 0xF0000;
/*  853 */           if (n == 131072 && (m & 0x200000) == 2097152) {
/*  854 */             debug1.add(new Tuple(Integer.valueOf(k), Integer.valueOf(i)));
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  859 */       if (debug1.isEmpty()) {
/*      */         
/*  861 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*      */         
/*      */         return;
/*      */       } 
/*  865 */       Tuple<Integer, Integer> debug3 = debug1.get(this.random.nextInt(debug1.size()));
/*  866 */       int debug4 = debug2.get(((Integer)debug3.getA()).intValue(), ((Integer)debug3.getB()).intValue());
/*  867 */       debug2.set(((Integer)debug3.getA()).intValue(), ((Integer)debug3.getB()).intValue(), debug4 | 0x400000);
/*  868 */       Direction debug5 = get1x2RoomDirection(this.baseGrid, ((Integer)debug3.getA()).intValue(), ((Integer)debug3.getB()).intValue(), 1, debug4 & 0xFFFF);
/*  869 */       int debug6 = ((Integer)debug3.getA()).intValue() + debug5.getStepX();
/*  870 */       int debug7 = ((Integer)debug3.getB()).intValue() + debug5.getStepZ();
/*      */       
/*  872 */       for (int j = 0; j < this.thirdFloorGrid.height; j++) {
/*  873 */         for (int k = 0; k < this.thirdFloorGrid.width; k++) {
/*  874 */           if (!isHouse(this.baseGrid, k, j)) {
/*  875 */             this.thirdFloorGrid.set(k, j, 5);
/*  876 */           } else if (k == ((Integer)debug3.getA()).intValue() && j == ((Integer)debug3.getB()).intValue()) {
/*  877 */             this.thirdFloorGrid.set(k, j, 3);
/*  878 */           } else if (k == debug6 && j == debug7) {
/*  879 */             this.thirdFloorGrid.set(k, j, 3);
/*  880 */             this.floorRooms[2].set(k, j, 8388608);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  885 */       List<Direction> debug8 = Lists.newArrayList();
/*  886 */       for (Direction debug10 : Direction.Plane.HORIZONTAL) {
/*  887 */         if (this.thirdFloorGrid.get(debug6 + debug10.getStepX(), debug7 + debug10.getStepZ()) == 0) {
/*  888 */           debug8.add(debug10);
/*      */         }
/*      */       } 
/*      */       
/*  892 */       if (debug8.isEmpty()) {
/*      */         
/*  894 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*  895 */         debug2.set(((Integer)debug3.getA()).intValue(), ((Integer)debug3.getB()).intValue(), debug4);
/*      */         return;
/*      */       } 
/*  898 */       Direction debug9 = debug8.get(this.random.nextInt(debug8.size()));
/*  899 */       recursiveCorridor(this.thirdFloorGrid, debug6 + debug9.getStepX(), debug7 + debug9.getStepZ(), debug9, 4);
/*  900 */       while (cleanEdges(this.thirdFloorGrid));
/*      */     }
/*      */ 
/*      */     
/*      */     private void identifyRooms(WoodlandMansionPieces.SimpleGrid debug1, WoodlandMansionPieces.SimpleGrid debug2) {
/*  905 */       List<Tuple<Integer, Integer>> debug3 = Lists.newArrayList(); int debug4;
/*  906 */       for (debug4 = 0; debug4 < debug1.height; debug4++) {
/*  907 */         for (int debug5 = 0; debug5 < debug1.width; debug5++) {
/*  908 */           if (debug1.get(debug5, debug4) == 2) {
/*  909 */             debug3.add(new Tuple(Integer.valueOf(debug5), Integer.valueOf(debug4)));
/*      */           }
/*      */         } 
/*      */       } 
/*  913 */       Collections.shuffle(debug3, this.random);
/*      */       
/*  915 */       debug4 = 10;
/*  916 */       for (Tuple<Integer, Integer> debug6 : debug3) {
/*  917 */         int debug7 = ((Integer)debug6.getA()).intValue();
/*  918 */         int debug8 = ((Integer)debug6.getB()).intValue();
/*      */         
/*  920 */         if (debug2.get(debug7, debug8) == 0) {
/*  921 */           int debug9 = debug7;
/*  922 */           int debug10 = debug7;
/*  923 */           int debug11 = debug8;
/*  924 */           int debug12 = debug8;
/*  925 */           int debug13 = 65536;
/*  926 */           if (debug2.get(debug7 + 1, debug8) == 0 && debug2.get(debug7, debug8 + 1) == 0 && debug2.get(debug7 + 1, debug8 + 1) == 0 && debug1
/*  927 */             .get(debug7 + 1, debug8) == 2 && debug1.get(debug7, debug8 + 1) == 2 && debug1.get(debug7 + 1, debug8 + 1) == 2) {
/*      */             
/*  929 */             debug10++;
/*  930 */             debug12++;
/*  931 */             debug13 = 262144;
/*  932 */           } else if (debug2.get(debug7 - 1, debug8) == 0 && debug2.get(debug7, debug8 + 1) == 0 && debug2.get(debug7 - 1, debug8 + 1) == 0 && debug1
/*  933 */             .get(debug7 - 1, debug8) == 2 && debug1.get(debug7, debug8 + 1) == 2 && debug1.get(debug7 - 1, debug8 + 1) == 2) {
/*      */             
/*  935 */             debug9--;
/*  936 */             debug12++;
/*  937 */             debug13 = 262144;
/*  938 */           } else if (debug2.get(debug7 - 1, debug8) == 0 && debug2.get(debug7, debug8 - 1) == 0 && debug2.get(debug7 - 1, debug8 - 1) == 0 && debug1
/*  939 */             .get(debug7 - 1, debug8) == 2 && debug1.get(debug7, debug8 - 1) == 2 && debug1.get(debug7 - 1, debug8 - 1) == 2) {
/*      */             
/*  941 */             debug9--;
/*  942 */             debug11--;
/*  943 */             debug13 = 262144;
/*  944 */           } else if (debug2.get(debug7 + 1, debug8) == 0 && debug1.get(debug7 + 1, debug8) == 2) {
/*  945 */             debug10++;
/*  946 */             debug13 = 131072;
/*  947 */           } else if (debug2.get(debug7, debug8 + 1) == 0 && debug1.get(debug7, debug8 + 1) == 2) {
/*  948 */             debug12++;
/*  949 */             debug13 = 131072;
/*  950 */           } else if (debug2.get(debug7 - 1, debug8) == 0 && debug1.get(debug7 - 1, debug8) == 2) {
/*  951 */             debug9--;
/*  952 */             debug13 = 131072;
/*  953 */           } else if (debug2.get(debug7, debug8 - 1) == 0 && debug1.get(debug7, debug8 - 1) == 2) {
/*  954 */             debug11--;
/*  955 */             debug13 = 131072;
/*      */           } 
/*      */ 
/*      */           
/*  959 */           int debug14 = this.random.nextBoolean() ? debug9 : debug10;
/*  960 */           int debug15 = this.random.nextBoolean() ? debug11 : debug12;
/*  961 */           int debug16 = 2097152;
/*  962 */           if (!debug1.edgesTo(debug14, debug15, 1)) {
/*  963 */             debug14 = (debug14 == debug9) ? debug10 : debug9;
/*  964 */             debug15 = (debug15 == debug11) ? debug12 : debug11;
/*  965 */             if (!debug1.edgesTo(debug14, debug15, 1)) {
/*  966 */               debug15 = (debug15 == debug11) ? debug12 : debug11;
/*  967 */               if (!debug1.edgesTo(debug14, debug15, 1)) {
/*  968 */                 debug14 = (debug14 == debug9) ? debug10 : debug9;
/*  969 */                 debug15 = (debug15 == debug11) ? debug12 : debug11;
/*  970 */                 if (!debug1.edgesTo(debug14, debug15, 1)) {
/*      */                   
/*  972 */                   debug16 = 0;
/*  973 */                   debug14 = debug9;
/*  974 */                   debug15 = debug11;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  979 */           for (int debug17 = debug11; debug17 <= debug12; debug17++) {
/*  980 */             for (int debug18 = debug9; debug18 <= debug10; debug18++) {
/*  981 */               if (debug18 == debug14 && debug17 == debug15) {
/*  982 */                 debug2.set(debug18, debug17, 0x100000 | debug16 | debug13 | debug4);
/*      */               } else {
/*  984 */                 debug2.set(debug18, debug17, debug13 | debug4);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  989 */           debug4++;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class SimpleGrid
/*      */   {
/*      */     private final int[][] grid;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int width;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int height;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int valueIfOutside;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SimpleGrid(int debug1, int debug2, int debug3) {
/* 1028 */       this.width = debug1;
/* 1029 */       this.height = debug2;
/* 1030 */       this.valueIfOutside = debug3;
/* 1031 */       this.grid = new int[debug1][debug2];
/*      */     }
/*      */     
/*      */     public void set(int debug1, int debug2, int debug3) {
/* 1035 */       if (debug1 >= 0 && debug1 < this.width && debug2 >= 0 && debug2 < this.height) {
/* 1036 */         this.grid[debug1][debug2] = debug3;
/*      */       }
/*      */     }
/*      */     
/*      */     public void set(int debug1, int debug2, int debug3, int debug4, int debug5) {
/* 1041 */       for (int debug6 = debug2; debug6 <= debug4; debug6++) {
/* 1042 */         for (int debug7 = debug1; debug7 <= debug3; debug7++) {
/* 1043 */           set(debug7, debug6, debug5);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public int get(int debug1, int debug2) {
/* 1049 */       if (debug1 >= 0 && debug1 < this.width && debug2 >= 0 && debug2 < this.height) {
/* 1050 */         return this.grid[debug1][debug2];
/*      */       }
/* 1052 */       return this.valueIfOutside;
/*      */     }
/*      */     
/*      */     public void setif(int debug1, int debug2, int debug3, int debug4) {
/* 1056 */       if (get(debug1, debug2) == debug3) {
/* 1057 */         set(debug1, debug2, debug4);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean edgesTo(int debug1, int debug2, int debug3) {
/* 1062 */       return (get(debug1 - 1, debug2) == debug3 || get(debug1 + 1, debug2) == debug3 || get(debug1, debug2 + 1) == debug3 || get(debug1, debug2 - 1) == debug3);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class FloorRoomCollection
/*      */   {
/*      */     private FloorRoomCollection() {}
/*      */ 
/*      */     
/*      */     public abstract String get1x1(Random param1Random);
/*      */ 
/*      */     
/*      */     public abstract String get1x1Secret(Random param1Random);
/*      */ 
/*      */     
/*      */     public abstract String get1x2SideEntrance(Random param1Random, boolean param1Boolean);
/*      */ 
/*      */     
/*      */     public abstract String get1x2FrontEntrance(Random param1Random, boolean param1Boolean);
/*      */ 
/*      */     
/*      */     public abstract String get1x2Secret(Random param1Random);
/*      */ 
/*      */     
/*      */     public abstract String get2x2(Random param1Random);
/*      */ 
/*      */     
/*      */     public abstract String get2x2Secret(Random param1Random);
/*      */   }
/*      */ 
/*      */   
/*      */   static class FirstFloorRoomCollection
/*      */     extends FloorRoomCollection
/*      */   {
/*      */     private FirstFloorRoomCollection() {}
/*      */ 
/*      */     
/*      */     public String get1x1(Random debug1) {
/* 1101 */       return "1x1_a" + (debug1.nextInt(5) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(Random debug1) {
/* 1106 */       return "1x1_as" + (debug1.nextInt(4) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(Random debug1, boolean debug2) {
/* 1111 */       return "1x2_a" + (debug1.nextInt(9) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(Random debug1, boolean debug2) {
/* 1116 */       return "1x2_b" + (debug1.nextInt(5) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(Random debug1) {
/* 1121 */       return "1x2_s" + (debug1.nextInt(2) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(Random debug1) {
/* 1126 */       return "2x2_a" + (debug1.nextInt(4) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(Random debug1) {
/* 1131 */       return "2x2_s1";
/*      */     } }
/*      */   
/*      */   static class SecondFloorRoomCollection extends FloorRoomCollection {
/*      */     private SecondFloorRoomCollection() {}
/*      */     
/*      */     public String get1x1(Random debug1) {
/* 1138 */       return "1x1_b" + (debug1.nextInt(4) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(Random debug1) {
/* 1143 */       return "1x1_as" + (debug1.nextInt(4) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(Random debug1, boolean debug2) {
/* 1148 */       if (debug2) {
/* 1149 */         return "1x2_c_stairs";
/*      */       }
/* 1151 */       return "1x2_c" + (debug1.nextInt(4) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(Random debug1, boolean debug2) {
/* 1156 */       if (debug2) {
/* 1157 */         return "1x2_d_stairs";
/*      */       }
/* 1159 */       return "1x2_d" + (debug1.nextInt(5) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(Random debug1) {
/* 1164 */       return "1x2_se" + (debug1.nextInt(1) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(Random debug1) {
/* 1169 */       return "2x2_b" + (debug1.nextInt(5) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(Random debug1) {
/* 1174 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   static class ThirdFloorRoomCollection extends SecondFloorRoomCollection {
/*      */     private ThirdFloorRoomCollection() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\WoodlandMansionPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */