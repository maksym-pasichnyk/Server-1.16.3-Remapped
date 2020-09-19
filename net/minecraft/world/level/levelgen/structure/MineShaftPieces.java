/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.vehicle.MinecartChest;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.RailBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ public class MineShaftPieces
/*     */ {
/*     */   static abstract class MineShaftPiece
/*     */     extends StructurePiece
/*     */   {
/*     */     protected MineshaftFeature.Type type;
/*     */     
/*     */     public MineShaftPiece(StructurePieceType debug1, int debug2, MineshaftFeature.Type debug3) {
/*  47 */       super(debug1, debug2);
/*  48 */       this.type = debug3;
/*     */     }
/*     */     
/*     */     public MineShaftPiece(StructurePieceType debug1, CompoundTag debug2) {
/*  52 */       super(debug1, debug2);
/*  53 */       this.type = MineshaftFeature.Type.byId(debug2.getInt("MST"));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  58 */       debug1.putInt("MST", this.type.ordinal());
/*     */     }
/*     */     
/*     */     protected BlockState getPlanksBlock() {
/*  62 */       switch (this.type)
/*     */       
/*     */       { default:
/*  65 */           return Blocks.OAK_PLANKS.defaultBlockState();
/*     */         case SOUTH:
/*  67 */           break; }  return Blocks.DARK_OAK_PLANKS.defaultBlockState();
/*     */     }
/*     */ 
/*     */     
/*     */     protected BlockState getFenceBlock() {
/*  72 */       switch (this.type)
/*     */       
/*     */       { default:
/*  75 */           return Blocks.OAK_FENCE.defaultBlockState();
/*     */         case SOUTH:
/*  77 */           break; }  return Blocks.DARK_OAK_FENCE.defaultBlockState();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isSupportingBox(BlockGetter debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6) {
/*  82 */       for (int debug7 = debug3; debug7 <= debug4; debug7++) {
/*  83 */         if (getBlock(debug1, debug7, debug5 + 1, debug6, debug2).isAir()) {
/*  84 */           return false;
/*     */         }
/*     */       } 
/*  87 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static MineShaftPiece createRandomShaftPiece(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, @Nullable Direction debug5, int debug6, MineshaftFeature.Type debug7) {
/*  92 */     int debug8 = debug1.nextInt(100);
/*  93 */     if (debug8 >= 80) {
/*  94 */       BoundingBox debug9 = MineShaftCrossing.findCrossing(debug0, debug1, debug2, debug3, debug4, debug5);
/*  95 */       if (debug9 != null) {
/*  96 */         return new MineShaftCrossing(debug6, debug9, debug5, debug7);
/*     */       }
/*  98 */     } else if (debug8 >= 70) {
/*  99 */       BoundingBox debug9 = MineShaftStairs.findStairs(debug0, debug1, debug2, debug3, debug4, debug5);
/* 100 */       if (debug9 != null) {
/* 101 */         return new MineShaftStairs(debug6, debug9, debug5, debug7);
/*     */       }
/*     */     } else {
/* 104 */       BoundingBox debug9 = MineShaftCorridor.findCorridorSize(debug0, debug1, debug2, debug3, debug4, debug5);
/* 105 */       if (debug9 != null) {
/* 106 */         return new MineShaftCorridor(debug6, debug1, debug9, debug5, debug7);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   private static MineShaftPiece generateAndAddPiece(StructurePiece debug0, List<StructurePiece> debug1, Random debug2, int debug3, int debug4, int debug5, Direction debug6, int debug7) {
/* 114 */     if (debug7 > 8) {
/* 115 */       return null;
/*     */     }
/* 117 */     if (Math.abs(debug3 - (debug0.getBoundingBox()).x0) > 80 || Math.abs(debug5 - (debug0.getBoundingBox()).z0) > 80) {
/* 118 */       return null;
/*     */     }
/*     */     
/* 121 */     MineshaftFeature.Type debug8 = ((MineShaftPiece)debug0).type;
/* 122 */     MineShaftPiece debug9 = createRandomShaftPiece(debug1, debug2, debug3, debug4, debug5, debug6, debug7 + 1, debug8);
/* 123 */     if (debug9 != null) {
/* 124 */       debug1.add(debug9);
/* 125 */       debug9.addChildren(debug0, debug1, debug2);
/*     */     } 
/* 127 */     return debug9;
/*     */   }
/*     */   
/*     */   public static class MineShaftRoom extends MineShaftPiece {
/* 131 */     private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */     
/*     */     public MineShaftRoom(int debug1, Random debug2, int debug3, int debug4, MineshaftFeature.Type debug5) {
/* 134 */       super(StructurePieceType.MINE_SHAFT_ROOM, debug1, debug5);
/* 135 */       this.type = debug5;
/*     */       
/* 137 */       this.boundingBox = new BoundingBox(debug3, 50, debug4, debug3 + 7 + debug2.nextInt(6), 54 + debug2.nextInt(6), debug4 + 7 + debug2.nextInt(6));
/*     */     }
/*     */     
/*     */     public MineShaftRoom(StructureManager debug1, CompoundTag debug2) {
/* 141 */       super(StructurePieceType.MINE_SHAFT_ROOM, debug2);
/* 142 */       ListTag debug3 = debug2.getList("Entrances", 11);
/* 143 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 144 */         this.childEntranceBoxes.add(new BoundingBox(debug3.getIntArray(debug4)));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 150 */       int debug4 = getGenDepth();
/*     */ 
/*     */ 
/*     */       
/* 154 */       int debug6 = this.boundingBox.getYSpan() - 3 - 1;
/* 155 */       if (debug6 <= 0) {
/* 156 */         debug6 = 1;
/*     */       }
/*     */ 
/*     */       
/* 160 */       int debug5 = 0;
/* 161 */       while (debug5 < this.boundingBox.getXSpan()) {
/* 162 */         debug5 += debug3.nextInt(this.boundingBox.getXSpan());
/* 163 */         if (debug5 + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 166 */         MineShaftPieces.MineShaftPiece debug7 = MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug3.nextInt(debug6) + 1, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/* 167 */         if (debug7 != null) {
/* 168 */           BoundingBox debug8 = debug7.getBoundingBox();
/* 169 */           this.childEntranceBoxes.add(new BoundingBox(debug8.x0, debug8.y0, this.boundingBox.z0, debug8.x1, debug8.y1, this.boundingBox.z0 + 1));
/*     */         } 
/* 171 */         debug5 += 4;
/*     */       } 
/*     */       
/* 174 */       debug5 = 0;
/* 175 */       while (debug5 < this.boundingBox.getXSpan()) {
/* 176 */         debug5 += debug3.nextInt(this.boundingBox.getXSpan());
/* 177 */         if (debug5 + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 180 */         MineShaftPieces.MineShaftPiece debug7 = MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + debug5, this.boundingBox.y0 + debug3.nextInt(debug6) + 1, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/* 181 */         if (debug7 != null) {
/* 182 */           BoundingBox debug8 = debug7.getBoundingBox();
/* 183 */           this.childEntranceBoxes.add(new BoundingBox(debug8.x0, debug8.y0, this.boundingBox.z1 - 1, debug8.x1, debug8.y1, this.boundingBox.z1));
/*     */         } 
/* 185 */         debug5 += 4;
/*     */       } 
/*     */       
/* 188 */       debug5 = 0;
/* 189 */       while (debug5 < this.boundingBox.getZSpan()) {
/* 190 */         debug5 += debug3.nextInt(this.boundingBox.getZSpan());
/* 191 */         if (debug5 + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 194 */         MineShaftPieces.MineShaftPiece debug7 = MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + debug3.nextInt(debug6) + 1, this.boundingBox.z0 + debug5, Direction.WEST, debug4);
/* 195 */         if (debug7 != null) {
/* 196 */           BoundingBox debug8 = debug7.getBoundingBox();
/* 197 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.x0, debug8.y0, debug8.z0, this.boundingBox.x0 + 1, debug8.y1, debug8.z1));
/*     */         } 
/* 199 */         debug5 += 4;
/*     */       } 
/*     */       
/* 202 */       debug5 = 0;
/* 203 */       while (debug5 < this.boundingBox.getZSpan()) {
/* 204 */         debug5 += debug3.nextInt(this.boundingBox.getZSpan());
/* 205 */         if (debug5 + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 208 */         StructurePiece debug7 = MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + debug3.nextInt(debug6) + 1, this.boundingBox.z0 + debug5, Direction.EAST, debug4);
/* 209 */         if (debug7 != null) {
/* 210 */           BoundingBox debug8 = debug7.getBoundingBox();
/* 211 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.x1 - 1, debug8.y0, debug8.z0, this.boundingBox.x1, debug8.y1, debug8.z1));
/*     */         } 
/* 213 */         debug5 += 4;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 219 */       if (edgesLiquid((BlockGetter)debug1, debug5)) {
/* 220 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 224 */       generateBox(debug1, debug5, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y0, this.boundingBox.z1, Blocks.DIRT.defaultBlockState(), CAVE_AIR, true);
/*     */ 
/*     */       
/* 227 */       generateBox(debug1, debug5, this.boundingBox.x0, this.boundingBox.y0 + 1, this.boundingBox.z0, this.boundingBox.x1, Math.min(this.boundingBox.y0 + 3, this.boundingBox.y1), this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
/* 228 */       for (BoundingBox debug9 : this.childEntranceBoxes) {
/* 229 */         generateBox(debug1, debug5, debug9.x0, debug9.y1 - 2, debug9.z0, debug9.x1, debug9.y1, debug9.z1, CAVE_AIR, CAVE_AIR, false);
/*     */       }
/* 231 */       generateUpperHalfSphere(debug1, debug5, this.boundingBox.x0, this.boundingBox.y0 + 4, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, false);
/*     */       
/* 233 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void move(int debug1, int debug2, int debug3) {
/* 238 */       super.move(debug1, debug2, debug3);
/* 239 */       for (BoundingBox debug5 : this.childEntranceBoxes) {
/* 240 */         debug5.move(debug1, debug2, debug3);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 246 */       super.addAdditionalSaveData(debug1);
/* 247 */       ListTag debug2 = new ListTag();
/* 248 */       for (BoundingBox debug4 : this.childEntranceBoxes) {
/* 249 */         debug2.add(debug4.createTag());
/*     */       }
/* 251 */       debug1.put("Entrances", (Tag)debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCorridor extends MineShaftPiece {
/*     */     private final boolean hasRails;
/*     */     private final boolean spiderCorridor;
/*     */     private boolean hasPlacedSpider;
/*     */     private final int numSections;
/*     */     
/*     */     public MineShaftCorridor(StructureManager debug1, CompoundTag debug2) {
/* 262 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, debug2);
/*     */       
/* 264 */       this.hasRails = debug2.getBoolean("hr");
/* 265 */       this.spiderCorridor = debug2.getBoolean("sc");
/* 266 */       this.hasPlacedSpider = debug2.getBoolean("hps");
/* 267 */       this.numSections = debug2.getInt("Num");
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 272 */       super.addAdditionalSaveData(debug1);
/* 273 */       debug1.putBoolean("hr", this.hasRails);
/* 274 */       debug1.putBoolean("sc", this.spiderCorridor);
/* 275 */       debug1.putBoolean("hps", this.hasPlacedSpider);
/* 276 */       debug1.putInt("Num", this.numSections);
/*     */     }
/*     */     
/*     */     public MineShaftCorridor(int debug1, Random debug2, BoundingBox debug3, Direction debug4, MineshaftFeature.Type debug5) {
/* 280 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, debug1, debug5);
/* 281 */       setOrientation(debug4);
/* 282 */       this.boundingBox = debug3;
/* 283 */       this.hasRails = (debug2.nextInt(3) == 0);
/* 284 */       this.spiderCorridor = (!this.hasRails && debug2.nextInt(23) == 0);
/*     */       
/* 286 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 287 */         this.numSections = debug3.getZSpan() / 5;
/*     */       } else {
/* 289 */         this.numSections = debug3.getXSpan() / 5;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static BoundingBox findCorridorSize(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5) {
/* 294 */       BoundingBox debug6 = new BoundingBox(debug2, debug3, debug4, debug2, debug3 + 3 - 1, debug4);
/*     */       
/* 296 */       int debug7 = debug1.nextInt(3) + 2;
/* 297 */       while (debug7 > 0) {
/* 298 */         int debug8 = debug7 * 5;
/*     */         
/* 300 */         switch (debug5) {
/*     */           
/*     */           default:
/* 303 */             debug6.x1 = debug2 + 3 - 1;
/* 304 */             debug6.z0 = debug4 - debug8 - 1;
/*     */             break;
/*     */           case SOUTH:
/* 307 */             debug6.x1 = debug2 + 3 - 1;
/* 308 */             debug6.z1 = debug4 + debug8 - 1;
/*     */             break;
/*     */           case WEST:
/* 311 */             debug6.x0 = debug2 - debug8 - 1;
/* 312 */             debug6.z1 = debug4 + 3 - 1;
/*     */             break;
/*     */           case EAST:
/* 315 */             debug6.x1 = debug2 + debug8 - 1;
/* 316 */             debug6.z1 = debug4 + 3 - 1;
/*     */             break;
/*     */         } 
/*     */         
/* 320 */         if (StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 321 */           debug7--;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 327 */       if (debug7 > 0) {
/* 328 */         return debug6;
/*     */       }
/*     */       
/* 331 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 336 */       int debug4 = getGenDepth();
/* 337 */       int debug5 = debug3.nextInt(4);
/* 338 */       Direction debug6 = getOrientation();
/* 339 */       if (debug6 != null) {
/* 340 */         switch (debug6) {
/*     */           
/*     */           default:
/* 343 */             if (debug5 <= 1) {
/* 344 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0 - 1, debug6, debug4); break;
/* 345 */             }  if (debug5 == 2) {
/* 346 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0, Direction.WEST, debug4); break;
/*     */             } 
/* 348 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0, Direction.EAST, debug4);
/*     */             break;
/*     */           
/*     */           case SOUTH:
/* 352 */             if (debug5 <= 1) {
/* 353 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z1 + 1, debug6, debug4); break;
/* 354 */             }  if (debug5 == 2) {
/* 355 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z1 - 3, Direction.WEST, debug4); break;
/*     */             } 
/* 357 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z1 - 3, Direction.EAST, debug4);
/*     */             break;
/*     */           
/*     */           case WEST:
/* 361 */             if (debug5 <= 1) {
/* 362 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0, debug6, debug4); break;
/* 363 */             }  if (debug5 == 2) {
/* 364 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, debug4); break;
/*     */             } 
/* 366 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/*     */             break;
/*     */           
/*     */           case EAST:
/* 370 */             if (debug5 <= 1) {
/* 371 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0, debug6, debug4); break;
/* 372 */             }  if (debug5 == 2) {
/* 373 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, debug4); break;
/*     */             } 
/* 375 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + debug3.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 382 */       if (debug4 < 8) {
/* 383 */         if (debug6 == Direction.NORTH || debug6 == Direction.SOUTH) {
/* 384 */           for (int debug7 = this.boundingBox.z0 + 3; debug7 + 3 <= this.boundingBox.z1; debug7 += 5) {
/* 385 */             int debug8 = debug3.nextInt(5);
/* 386 */             if (debug8 == 0) {
/* 387 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0, debug7, Direction.WEST, debug4 + 1);
/* 388 */             } else if (debug8 == 1) {
/* 389 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0, debug7, Direction.EAST, debug4 + 1);
/*     */             } 
/*     */           } 
/*     */         } else {
/* 393 */           for (int debug7 = this.boundingBox.x0 + 3; debug7 + 3 <= this.boundingBox.x1; debug7 += 5) {
/* 394 */             int debug8 = debug3.nextInt(5);
/* 395 */             if (debug8 == 0) {
/* 396 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, debug7, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, debug4 + 1);
/* 397 */             } else if (debug8 == 1) {
/* 398 */               MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, debug7, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, debug4 + 1);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean createChest(WorldGenLevel debug1, BoundingBox debug2, Random debug3, int debug4, int debug5, int debug6, ResourceLocation debug7) {
/* 407 */       BlockPos debug8 = new BlockPos(getWorldX(debug4, debug6), getWorldY(debug5), getWorldZ(debug4, debug6));
/* 408 */       if (debug2.isInside((Vec3i)debug8) && 
/* 409 */         debug1.getBlockState(debug8).isAir() && !debug1.getBlockState(debug8.below()).isAir()) {
/* 410 */         BlockState debug9 = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, debug3.nextBoolean() ? (Comparable)RailShape.NORTH_SOUTH : (Comparable)RailShape.EAST_WEST);
/* 411 */         placeBlock(debug1, debug9, debug4, debug5, debug6, debug2);
/* 412 */         MinecartChest debug10 = new MinecartChest((Level)debug1.getLevel(), debug8.getX() + 0.5D, debug8.getY() + 0.5D, debug8.getZ() + 0.5D);
/* 413 */         debug10.setLootTable(debug7, debug3.nextLong());
/* 414 */         debug1.addFreshEntity((Entity)debug10);
/* 415 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 419 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 424 */       if (edgesLiquid((BlockGetter)debug1, debug5)) {
/* 425 */         return false;
/*     */       }
/*     */       
/* 428 */       int debug8 = 0;
/* 429 */       int debug9 = 2;
/* 430 */       int debug10 = 0;
/* 431 */       int debug11 = 2;
/* 432 */       int debug12 = this.numSections * 5 - 1;
/*     */       
/* 434 */       BlockState debug13 = getPlanksBlock();
/*     */ 
/*     */       
/* 437 */       generateBox(debug1, debug5, 0, 0, 0, 2, 1, debug12, CAVE_AIR, CAVE_AIR, false);
/* 438 */       generateMaybeBox(debug1, debug5, debug4, 0.8F, 0, 2, 0, 2, 2, debug12, CAVE_AIR, CAVE_AIR, false, false);
/*     */       
/* 440 */       if (this.spiderCorridor) {
/* 441 */         generateMaybeBox(debug1, debug5, debug4, 0.6F, 0, 0, 0, 2, 1, debug12, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */       }
/*     */       
/*     */       int debug14;
/* 445 */       for (debug14 = 0; debug14 < this.numSections; debug14++) {
/* 446 */         int debug15 = 2 + debug14 * 5;
/*     */         
/* 448 */         placeSupport(debug1, debug5, 0, 0, debug15, 2, 2, debug4);
/*     */         
/* 450 */         placeCobWeb(debug1, debug5, debug4, 0.1F, 0, 2, debug15 - 1);
/* 451 */         placeCobWeb(debug1, debug5, debug4, 0.1F, 2, 2, debug15 - 1);
/* 452 */         placeCobWeb(debug1, debug5, debug4, 0.1F, 0, 2, debug15 + 1);
/* 453 */         placeCobWeb(debug1, debug5, debug4, 0.1F, 2, 2, debug15 + 1);
/* 454 */         placeCobWeb(debug1, debug5, debug4, 0.05F, 0, 2, debug15 - 2);
/* 455 */         placeCobWeb(debug1, debug5, debug4, 0.05F, 2, 2, debug15 - 2);
/* 456 */         placeCobWeb(debug1, debug5, debug4, 0.05F, 0, 2, debug15 + 2);
/* 457 */         placeCobWeb(debug1, debug5, debug4, 0.05F, 2, 2, debug15 + 2);
/*     */         
/* 459 */         if (debug4.nextInt(100) == 0) {
/* 460 */           createChest(debug1, debug5, debug4, 2, 0, debug15 - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 462 */         if (debug4.nextInt(100) == 0) {
/* 463 */           createChest(debug1, debug5, debug4, 0, 0, debug15 + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 465 */         if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 466 */           int debug16 = getWorldY(0);
/* 467 */           int debug17 = debug15 - 1 + debug4.nextInt(3);
/* 468 */           int debug18 = getWorldX(1, debug17);
/* 469 */           int debug19 = getWorldZ(1, debug17);
/* 470 */           BlockPos debug20 = new BlockPos(debug18, debug16, debug19);
/*     */           
/* 472 */           if (debug5.isInside((Vec3i)debug20) && isInterior((LevelReader)debug1, 1, 0, debug17, debug5)) {
/* 473 */             this.hasPlacedSpider = true;
/* 474 */             debug1.setBlock(debug20, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */             
/* 476 */             BlockEntity debug21 = debug1.getBlockEntity(debug20);
/* 477 */             if (debug21 instanceof SpawnerBlockEntity) {
/* 478 */               ((SpawnerBlockEntity)debug21).getSpawner().setEntityId(EntityType.CAVE_SPIDER);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 485 */       for (debug14 = 0; debug14 <= 2; debug14++) {
/* 486 */         for (int debug15 = 0; debug15 <= debug12; debug15++) {
/* 487 */           int debug16 = -1;
/* 488 */           BlockState debug17 = getBlock((BlockGetter)debug1, debug14, -1, debug15, debug5);
/* 489 */           if (debug17.isAir() && isInterior((LevelReader)debug1, debug14, -1, debug15, debug5)) {
/* 490 */             int debug18 = -1;
/* 491 */             placeBlock(debug1, debug13, debug14, -1, debug15, debug5);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 496 */       if (this.hasRails) {
/* 497 */         BlockState blockState = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/* 498 */         for (int debug15 = 0; debug15 <= debug12; debug15++) {
/* 499 */           BlockState debug16 = getBlock((BlockGetter)debug1, 1, -1, debug15, debug5);
/* 500 */           if (!debug16.isAir() && debug16.isSolidRender((BlockGetter)debug1, new BlockPos(getWorldX(1, debug15), getWorldY(-1), getWorldZ(1, debug15)))) {
/* 501 */             float debug17 = isInterior((LevelReader)debug1, 1, 0, debug15, debug5) ? 0.7F : 0.9F;
/* 502 */             maybeGenerateBlock(debug1, debug5, debug4, debug17, 1, 0, debug15, blockState);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 507 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     private void placeSupport(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, Random debug8) {
/* 512 */       if (!isSupportingBox((BlockGetter)debug1, debug2, debug3, debug7, debug6, debug5)) {
/*     */         return;
/*     */       }
/*     */       
/* 516 */       BlockState debug9 = getPlanksBlock();
/* 517 */       BlockState debug10 = getFenceBlock();
/*     */       
/* 519 */       generateBox(debug1, debug2, debug3, debug4, debug5, debug3, debug6 - 1, debug5, (BlockState)debug10.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 520 */       generateBox(debug1, debug2, debug7, debug4, debug5, debug7, debug6 - 1, debug5, (BlockState)debug10.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 521 */       if (debug8.nextInt(4) == 0) {
/* 522 */         generateBox(debug1, debug2, debug3, debug6, debug5, debug3, debug6, debug5, debug9, CAVE_AIR, false);
/* 523 */         generateBox(debug1, debug2, debug7, debug6, debug5, debug7, debug6, debug5, debug9, CAVE_AIR, false);
/*     */       } else {
/* 525 */         generateBox(debug1, debug2, debug3, debug6, debug5, debug7, debug6, debug5, debug9, CAVE_AIR, false);
/* 526 */         maybeGenerateBlock(debug1, debug2, debug8, 0.05F, debug3 + 1, debug6, debug5 - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH));
/* 527 */         maybeGenerateBlock(debug1, debug2, debug8, 0.05F, debug3 + 1, debug6, debug5 + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeCobWeb(WorldGenLevel debug1, BoundingBox debug2, Random debug3, float debug4, int debug5, int debug6, int debug7) {
/* 532 */       if (isInterior((LevelReader)debug1, debug5, debug6, debug7, debug2))
/* 533 */         maybeGenerateBlock(debug1, debug2, debug3, debug4, debug5, debug6, debug7, Blocks.COBWEB.defaultBlockState()); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCrossing
/*     */     extends MineShaftPiece {
/*     */     private final Direction direction;
/*     */     private final boolean isTwoFloored;
/*     */     
/*     */     public MineShaftCrossing(StructureManager debug1, CompoundTag debug2) {
/* 543 */       super(StructurePieceType.MINE_SHAFT_CROSSING, debug2);
/* 544 */       this.isTwoFloored = debug2.getBoolean("tf");
/* 545 */       this.direction = Direction.from2DDataValue(debug2.getInt("D"));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/* 550 */       super.addAdditionalSaveData(debug1);
/* 551 */       debug1.putBoolean("tf", this.isTwoFloored);
/* 552 */       debug1.putInt("D", this.direction.get2DDataValue());
/*     */     }
/*     */     
/*     */     public MineShaftCrossing(int debug1, BoundingBox debug2, @Nullable Direction debug3, MineshaftFeature.Type debug4) {
/* 556 */       super(StructurePieceType.MINE_SHAFT_CROSSING, debug1, debug4);
/*     */       
/* 558 */       this.direction = debug3;
/* 559 */       this.boundingBox = debug2;
/* 560 */       this.isTwoFloored = (debug2.getYSpan() > 3);
/*     */     }
/*     */     
/*     */     public static BoundingBox findCrossing(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5) {
/* 564 */       BoundingBox debug6 = new BoundingBox(debug2, debug3, debug4, debug2, debug3 + 3 - 1, debug4);
/*     */       
/* 566 */       if (debug1.nextInt(4) == 0) {
/* 567 */         debug6.y1 += 4;
/*     */       }
/*     */       
/* 570 */       switch (debug5) {
/*     */         
/*     */         default:
/* 573 */           debug6.x0 = debug2 - 1;
/* 574 */           debug6.x1 = debug2 + 3;
/* 575 */           debug6.z0 = debug4 - 4;
/*     */           break;
/*     */         case SOUTH:
/* 578 */           debug6.x0 = debug2 - 1;
/* 579 */           debug6.x1 = debug2 + 3;
/* 580 */           debug6.z1 = debug4 + 3 + 1;
/*     */           break;
/*     */         case WEST:
/* 583 */           debug6.x0 = debug2 - 4;
/* 584 */           debug6.z0 = debug4 - 1;
/* 585 */           debug6.z1 = debug4 + 3;
/*     */           break;
/*     */         case EAST:
/* 588 */           debug6.x1 = debug2 + 3 + 1;
/* 589 */           debug6.z0 = debug4 - 1;
/* 590 */           debug6.z1 = debug4 + 3;
/*     */           break;
/*     */       } 
/*     */       
/* 594 */       if (StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 595 */         return null;
/*     */       }
/*     */       
/* 598 */       return debug6;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 603 */       int debug4 = getGenDepth();
/*     */ 
/*     */       
/* 606 */       switch (this.direction) {
/*     */         
/*     */         default:
/* 609 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/* 610 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, debug4);
/* 611 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, debug4);
/*     */           break;
/*     */         case SOUTH:
/* 614 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/* 615 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, debug4);
/* 616 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, debug4);
/*     */           break;
/*     */         case WEST:
/* 619 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/* 620 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/* 621 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, debug4);
/*     */           break;
/*     */         case EAST:
/* 624 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/* 625 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/* 626 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, debug4);
/*     */           break;
/*     */       } 
/*     */       
/* 630 */       if (this.isTwoFloored) {
/* 631 */         if (debug3.nextBoolean()) {
/* 632 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/*     */         }
/* 634 */         if (debug3.nextBoolean()) {
/* 635 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.WEST, debug4);
/*     */         }
/* 637 */         if (debug3.nextBoolean()) {
/* 638 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.EAST, debug4);
/*     */         }
/* 640 */         if (debug3.nextBoolean()) {
/* 641 */           MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 648 */       if (edgesLiquid((BlockGetter)debug1, debug5)) {
/* 649 */         return false;
/*     */       }
/*     */       
/* 652 */       BlockState debug8 = getPlanksBlock();
/*     */ 
/*     */       
/* 655 */       if (this.isTwoFloored) {
/* 656 */         generateBox(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
/* 657 */         generateBox(debug1, debug5, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
/* 658 */         generateBox(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y1 - 2, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
/* 659 */         generateBox(debug1, debug5, this.boundingBox.x0, this.boundingBox.y1 - 2, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
/* 660 */         generateBox(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3, this.boundingBox.z0 + 1, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } else {
/* 662 */         generateBox(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
/* 663 */         generateBox(debug1, debug5, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } 
/*     */ 
/*     */       
/* 667 */       placeSupportPillar(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
/* 668 */       placeSupportPillar(debug1, debug5, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);
/* 669 */       placeSupportPillar(debug1, debug5, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
/* 670 */       placeSupportPillar(debug1, debug5, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);
/*     */ 
/*     */ 
/*     */       
/* 674 */       for (int debug9 = this.boundingBox.x0; debug9 <= this.boundingBox.x1; debug9++) {
/* 675 */         for (int debug10 = this.boundingBox.z0; debug10 <= this.boundingBox.z1; debug10++) {
/* 676 */           if (getBlock((BlockGetter)debug1, debug9, this.boundingBox.y0 - 1, debug10, debug5).isAir() && isInterior((LevelReader)debug1, debug9, this.boundingBox.y0 - 1, debug10, debug5)) {
/* 677 */             placeBlock(debug1, debug8, debug9, this.boundingBox.y0 - 1, debug10, debug5);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 682 */       return true;
/*     */     }
/*     */     
/*     */     private void placeSupportPillar(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6) {
/* 686 */       if (!getBlock((BlockGetter)debug1, debug3, debug6 + 1, debug5, debug2).isAir())
/* 687 */         generateBox(debug1, debug2, debug3, debug4, debug5, debug3, debug6, debug5, getPlanksBlock(), CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftStairs
/*     */     extends MineShaftPiece {
/*     */     public MineShaftStairs(int debug1, BoundingBox debug2, Direction debug3, MineshaftFeature.Type debug4) {
/* 694 */       super(StructurePieceType.MINE_SHAFT_STAIRS, debug1, debug4);
/* 695 */       setOrientation(debug3);
/* 696 */       this.boundingBox = debug2;
/*     */     }
/*     */     
/*     */     public MineShaftStairs(StructureManager debug1, CompoundTag debug2) {
/* 700 */       super(StructurePieceType.MINE_SHAFT_STAIRS, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public static BoundingBox findStairs(List<StructurePiece> debug0, Random debug1, int debug2, int debug3, int debug4, Direction debug5) {
/* 705 */       BoundingBox debug6 = new BoundingBox(debug2, debug3 - 5, debug4, debug2, debug3 + 3 - 1, debug4);
/*     */       
/* 707 */       switch (debug5) {
/*     */         
/*     */         default:
/* 710 */           debug6.x1 = debug2 + 3 - 1;
/* 711 */           debug6.z0 = debug4 - 8;
/*     */           break;
/*     */         case SOUTH:
/* 714 */           debug6.x1 = debug2 + 3 - 1;
/* 715 */           debug6.z1 = debug4 + 8;
/*     */           break;
/*     */         case WEST:
/* 718 */           debug6.x0 = debug2 - 8;
/* 719 */           debug6.z1 = debug4 + 3 - 1;
/*     */           break;
/*     */         case EAST:
/* 722 */           debug6.x1 = debug2 + 8;
/* 723 */           debug6.z1 = debug4 + 3 - 1;
/*     */           break;
/*     */       } 
/*     */       
/* 727 */       if (StructurePiece.findCollisionPiece(debug0, debug6) != null) {
/* 728 */         return null;
/*     */       }
/*     */       
/* 731 */       return debug6;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {
/* 736 */       int debug4 = getGenDepth();
/*     */ 
/*     */       
/* 739 */       Direction debug5 = getOrientation();
/* 740 */       if (debug5 != null) {
/* 741 */         switch (debug5) {
/*     */           
/*     */           default:
/* 744 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, debug4);
/*     */             return;
/*     */           case SOUTH:
/* 747 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, debug4);
/*     */             return;
/*     */           case WEST:
/* 750 */             MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0, Direction.WEST, debug4); return;
/*     */           case EAST:
/*     */             break;
/* 753 */         }  MineShaftPieces.generateAndAddPiece(debug1, debug2, debug3, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0, Direction.EAST, debug4);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 761 */       if (edgesLiquid((BlockGetter)debug1, debug5)) {
/* 762 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 766 */       generateBox(debug1, debug5, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 768 */       generateBox(debug1, debug5, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 770 */       for (int debug8 = 0; debug8 < 5; debug8++) {
/* 771 */         generateBox(debug1, debug5, 0, 5 - debug8 - ((debug8 < 4) ? 1 : 0), 2 + debug8, 2, 7 - debug8, 2 + debug8, CAVE_AIR, CAVE_AIR, false);
/*     */       }
/*     */       
/* 774 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\MineShaftPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */