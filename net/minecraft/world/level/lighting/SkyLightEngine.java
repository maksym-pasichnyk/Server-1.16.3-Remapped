/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public final class SkyLightEngine extends LayerLightEngine<SkyLightSectionStorage.SkyDataLayerStorageMap, SkyLightSectionStorage> {
/*  15 */   private static final Direction[] DIRECTIONS = Direction.values();
/*  16 */   private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
/*     */   
/*     */   public SkyLightEngine(LightChunkGetter debug1) {
/*  19 */     super(debug1, LightLayer.SKY, new SkyLightSectionStorage(debug1));
/*     */   }
/*     */   
/*     */   protected int computeLevelFromNeighbor(long debug1, long debug3, int debug5) {
/*     */     Direction debug18;
/*  24 */     if (debug3 == Long.MAX_VALUE) {
/*  25 */       return 15;
/*     */     }
/*  27 */     if (debug1 == Long.MAX_VALUE) {
/*  28 */       if (this.storage.hasLightSource(debug3)) {
/*     */         
/*  30 */         debug5 = 0;
/*     */       } else {
/*  32 */         return 15;
/*     */       } 
/*     */     }
/*  35 */     if (debug5 >= 15) {
/*  36 */       return debug5;
/*     */     }
/*     */     
/*  39 */     MutableInt debug6 = new MutableInt();
/*  40 */     BlockState debug7 = getStateAndOpacity(debug3, debug6);
/*     */     
/*  42 */     if (debug6.getValue().intValue() >= 15) {
/*  43 */       return 15;
/*     */     }
/*     */     
/*  46 */     int debug8 = BlockPos.getX(debug1);
/*  47 */     int debug9 = BlockPos.getY(debug1);
/*  48 */     int debug10 = BlockPos.getZ(debug1);
/*     */     
/*  50 */     int debug11 = BlockPos.getX(debug3);
/*  51 */     int debug12 = BlockPos.getY(debug3);
/*  52 */     int debug13 = BlockPos.getZ(debug3);
/*     */     
/*  54 */     boolean debug14 = (debug8 == debug11 && debug10 == debug13);
/*     */     
/*  56 */     int debug15 = Integer.signum(debug11 - debug8);
/*  57 */     int debug16 = Integer.signum(debug12 - debug9);
/*  58 */     int debug17 = Integer.signum(debug13 - debug10);
/*     */ 
/*     */     
/*  61 */     if (debug1 == Long.MAX_VALUE) {
/*  62 */       debug18 = Direction.DOWN;
/*     */     } else {
/*  64 */       debug18 = Direction.fromNormal(debug15, debug16, debug17);
/*     */     } 
/*     */     
/*  67 */     BlockState debug19 = getStateAndOpacity(debug1, (MutableInt)null);
/*     */     
/*  69 */     if (debug18 != null) {
/*  70 */       VoxelShape voxelShape1 = getShape(debug19, debug1, debug18);
/*  71 */       VoxelShape debug21 = getShape(debug7, debug3, debug18.getOpposite());
/*     */       
/*  73 */       if (Shapes.faceShapeOccludes(voxelShape1, debug21)) {
/*  74 */         return 15;
/*     */       }
/*     */     } else {
/*     */       
/*  78 */       VoxelShape voxelShape1 = getShape(debug19, debug1, Direction.DOWN);
/*  79 */       if (Shapes.faceShapeOccludes(voxelShape1, Shapes.empty())) {
/*  80 */         return 15;
/*     */       }
/*     */       
/*  83 */       int debug21 = debug14 ? -1 : 0;
/*  84 */       Direction debug22 = Direction.fromNormal(debug15, debug21, debug17);
/*  85 */       if (debug22 == null)
/*     */       {
/*  87 */         return 15;
/*     */       }
/*  89 */       VoxelShape debug23 = getShape(debug7, debug3, debug22.getOpposite());
/*  90 */       if (Shapes.faceShapeOccludes(Shapes.empty(), debug23)) {
/*  91 */         return 15;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  96 */     boolean debug20 = (debug1 == Long.MAX_VALUE || (debug14 && debug9 > debug12));
/*  97 */     if (debug20 && debug5 == 0 && debug6.getValue().intValue() == 0) {
/*  98 */       return 0;
/*     */     }
/* 100 */     return debug5 + Math.max(1, debug6.getValue().intValue());
/*     */   }
/*     */   
/*     */   protected void checkNeighborsAfterUpdate(long debug1, int debug3, boolean debug4) {
/*     */     int debug10;
/* 105 */     long debug5 = SectionPos.blockToSection(debug1);
/* 106 */     int debug7 = BlockPos.getY(debug1);
/* 107 */     int debug8 = SectionPos.sectionRelative(debug7);
/* 108 */     int debug9 = SectionPos.blockToSectionCoord(debug7);
/*     */     
/* 110 */     if (debug8 != 0) {
/* 111 */       debug10 = 0;
/*     */     } else {
/* 113 */       int i = 0;
/* 114 */       while (!this.storage.storingLightForSection(SectionPos.offset(debug5, 0, -i - 1, 0)) && this.storage.hasSectionsBelow(debug9 - i - 1)) {
/* 115 */         i++;
/*     */       }
/* 117 */       debug10 = i;
/*     */     } 
/*     */     
/* 120 */     long debug11 = BlockPos.offset(debug1, 0, -1 - debug10 * 16, 0);
/* 121 */     long debug13 = SectionPos.blockToSection(debug11);
/* 122 */     if (debug5 == debug13 || this.storage.storingLightForSection(debug13)) {
/* 123 */       checkNeighbor(debug1, debug11, debug3, debug4);
/*     */     }
/*     */     
/* 126 */     long debug15 = BlockPos.offset(debug1, Direction.UP);
/* 127 */     long debug17 = SectionPos.blockToSection(debug15);
/* 128 */     if (debug5 == debug17 || this.storage.storingLightForSection(debug17)) {
/* 129 */       checkNeighbor(debug1, debug15, debug3, debug4);
/*     */     }
/*     */     
/* 132 */     for (Direction debug22 : HORIZONTALS) {
/* 133 */       int debug23 = 0;
/*     */       do {
/* 135 */         long debug24 = BlockPos.offset(debug1, debug22.getStepX(), -debug23, debug22.getStepZ());
/* 136 */         long debug26 = SectionPos.blockToSection(debug24);
/*     */         
/* 138 */         if (debug5 == debug26) {
/* 139 */           checkNeighbor(debug1, debug24, debug3, debug4);
/*     */           
/*     */           break;
/*     */         } 
/* 143 */         if (!this.storage.storingLightForSection(debug26))
/* 144 */           continue;  checkNeighbor(debug1, debug24, debug3, debug4);
/*     */         
/* 146 */         ++debug23;
/* 147 */       } while (debug23 <= debug10 * 16);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getComputedLevel(long debug1, long debug3, int debug5) {
/* 153 */     int debug6 = debug5;
/* 154 */     if (Long.MAX_VALUE != debug3) {
/* 155 */       int i = computeLevelFromNeighbor(Long.MAX_VALUE, debug1, 0);
/* 156 */       if (debug6 > i) {
/* 157 */         debug6 = i;
/*     */       }
/* 159 */       if (debug6 == 0) {
/* 160 */         return debug6;
/*     */       }
/*     */     } 
/* 163 */     long debug7 = SectionPos.blockToSection(debug1);
/* 164 */     DataLayer debug9 = this.storage.getDataLayer(debug7, true);
/* 165 */     for (Direction debug13 : DIRECTIONS) {
/* 166 */       DataLayer debug18; long debug14 = BlockPos.offset(debug1, debug13);
/* 167 */       long debug16 = SectionPos.blockToSection(debug14);
/*     */       
/* 169 */       if (debug7 == debug16) {
/* 170 */         debug18 = debug9;
/*     */       } else {
/* 172 */         debug18 = this.storage.getDataLayer(debug16, true);
/*     */       } 
/* 174 */       if (debug18 != null) {
/* 175 */         if (debug14 != debug3) {
/* 176 */           int debug19 = computeLevelFromNeighbor(debug14, debug1, getLevel(debug18, debug14));
/* 177 */           if (debug6 > debug19) {
/* 178 */             debug6 = debug19;
/*     */           }
/* 180 */           if (debug6 == 0) {
/* 181 */             return debug6;
/*     */           }
/*     */         } 
/* 184 */       } else if (debug13 != Direction.DOWN) {
/*     */         
/* 186 */         debug14 = BlockPos.getFlatIndex(debug14);
/* 187 */         while (!this.storage.storingLightForSection(debug16) && !this.storage.isAboveData(debug16)) {
/* 188 */           debug16 = SectionPos.offset(debug16, Direction.UP);
/* 189 */           debug14 = BlockPos.offset(debug14, 0, 16, 0);
/*     */         } 
/* 191 */         DataLayer debug19 = this.storage.getDataLayer(debug16, true);
/* 192 */         if (debug14 != debug3) {
/*     */           int debug20;
/* 194 */           if (debug19 != null) {
/* 195 */             debug20 = computeLevelFromNeighbor(debug14, debug1, getLevel(debug19, debug14));
/*     */           } else {
/* 197 */             debug20 = this.storage.lightOnInSection(debug16) ? 0 : 15;
/*     */           } 
/* 199 */           if (debug6 > debug20) {
/* 200 */             debug6 = debug20;
/*     */           }
/* 202 */           if (debug6 == 0) {
/* 203 */             return debug6;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 208 */     return debug6;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkNode(long debug1) {
/* 214 */     this.storage.runAllUpdates();
/* 215 */     long debug3 = SectionPos.blockToSection(debug1);
/* 216 */     if (this.storage.storingLightForSection(debug3)) {
/* 217 */       super.checkNode(debug1);
/*     */     } else {
/* 219 */       debug1 = BlockPos.getFlatIndex(debug1);
/* 220 */       while (!this.storage.storingLightForSection(debug3) && !this.storage.isAboveData(debug3)) {
/* 221 */         debug3 = SectionPos.offset(debug3, Direction.UP);
/* 222 */         debug1 = BlockPos.offset(debug1, 0, 16, 0);
/*     */       } 
/* 224 */       if (this.storage.storingLightForSection(debug3))
/* 225 */         super.checkNode(debug1); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\SkyLightEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */