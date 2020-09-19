/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public final class BitSetDiscreteVoxelShape
/*     */   extends DiscreteVoxelShape {
/*     */   private final BitSet storage;
/*     */   private int xMin;
/*     */   private int yMin;
/*     */   private int zMin;
/*     */   private int xMax;
/*     */   private int yMax;
/*     */   private int zMax;
/*     */   
/*     */   public BitSetDiscreteVoxelShape(int debug1, int debug2, int debug3) {
/*  17 */     this(debug1, debug2, debug3, debug1, debug2, debug3, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public BitSetDiscreteVoxelShape(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, int debug9) {
/*  21 */     super(debug1, debug2, debug3);
/*  22 */     this.storage = new BitSet(debug1 * debug2 * debug3);
/*  23 */     this.xMin = debug4;
/*  24 */     this.yMin = debug5;
/*  25 */     this.zMin = debug6;
/*  26 */     this.xMax = debug7;
/*  27 */     this.yMax = debug8;
/*  28 */     this.zMax = debug9;
/*     */   }
/*     */   
/*     */   public BitSetDiscreteVoxelShape(DiscreteVoxelShape debug1) {
/*  32 */     super(debug1.xSize, debug1.ySize, debug1.zSize);
/*     */ 
/*     */     
/*  35 */     if (debug1 instanceof BitSetDiscreteVoxelShape) {
/*  36 */       this.storage = (BitSet)((BitSetDiscreteVoxelShape)debug1).storage.clone();
/*     */     } else {
/*  38 */       this.storage = new BitSet(this.xSize * this.ySize * this.zSize);
/*  39 */       for (int debug2 = 0; debug2 < this.xSize; debug2++) {
/*  40 */         for (int debug3 = 0; debug3 < this.ySize; debug3++) {
/*  41 */           for (int debug4 = 0; debug4 < this.zSize; debug4++) {
/*  42 */             if (debug1.isFull(debug2, debug3, debug4)) {
/*  43 */               this.storage.set(getIndex(debug2, debug3, debug4));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     this.xMin = debug1.firstFull(Direction.Axis.X);
/*  51 */     this.yMin = debug1.firstFull(Direction.Axis.Y);
/*  52 */     this.zMin = debug1.firstFull(Direction.Axis.Z);
/*     */     
/*  54 */     this.xMax = debug1.lastFull(Direction.Axis.X);
/*  55 */     this.yMax = debug1.lastFull(Direction.Axis.Y);
/*  56 */     this.zMax = debug1.lastFull(Direction.Axis.Z);
/*     */   }
/*     */   
/*     */   protected int getIndex(int debug1, int debug2, int debug3) {
/*  60 */     return (debug1 * this.ySize + debug2) * this.zSize + debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull(int debug1, int debug2, int debug3) {
/*  65 */     return this.storage.get(getIndex(debug1, debug2, debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFull(int debug1, int debug2, int debug3, boolean debug4, boolean debug5) {
/*  70 */     this.storage.set(getIndex(debug1, debug2, debug3), debug5);
/*  71 */     if (debug4 && debug5) {
/*  72 */       this.xMin = Math.min(this.xMin, debug1);
/*  73 */       this.yMin = Math.min(this.yMin, debug2);
/*  74 */       this.zMin = Math.min(this.zMin, debug3);
/*     */       
/*  76 */       this.xMax = Math.max(this.xMax, debug1 + 1);
/*  77 */       this.yMax = Math.max(this.yMax, debug2 + 1);
/*  78 */       this.zMax = Math.max(this.zMax, debug3 + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  84 */     return this.storage.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int firstFull(Direction.Axis debug1) {
/*  89 */     return debug1.choose(this.xMin, this.yMin, this.zMin);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastFull(Direction.Axis debug1) {
/*  94 */     return debug1.choose(this.xMax, this.yMax, this.zMax);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isZStripFull(int debug1, int debug2, int debug3, int debug4) {
/* 100 */     if (debug3 < 0 || debug4 < 0 || debug1 < 0) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (debug3 >= this.xSize || debug4 >= this.ySize || debug2 > this.zSize) {
/* 104 */       return false;
/*     */     }
/* 106 */     return (this.storage.nextClearBit(getIndex(debug3, debug4, debug1)) >= getIndex(debug3, debug4, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setZStrip(int debug1, int debug2, int debug3, int debug4, boolean debug5) {
/* 112 */     this.storage.set(getIndex(debug3, debug4, debug1), getIndex(debug3, debug4, debug2), debug5);
/*     */   }
/*     */   
/*     */   static BitSetDiscreteVoxelShape join(DiscreteVoxelShape debug0, DiscreteVoxelShape debug1, IndexMerger debug2, IndexMerger debug3, IndexMerger debug4, BooleanOp debug5) {
/* 116 */     BitSetDiscreteVoxelShape debug6 = new BitSetDiscreteVoxelShape(debug2.getList().size() - 1, debug3.getList().size() - 1, debug4.getList().size() - 1);
/* 117 */     int[] debug7 = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     debug2.forMergedIndexes((debug7, debug8, debug9) -> {
/*     */           boolean[] debug10 = { false };
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           boolean debug11 = debug0.forMergedIndexes(());
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (debug10[0]) {
/*     */             debug6[0] = Math.min(debug6[0], debug9);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             debug6[3] = Math.max(debug6[3], debug9);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           return debug11;
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 153 */     debug6.xMin = debug7[0];
/* 154 */     debug6.yMin = debug7[1];
/* 155 */     debug6.zMin = debug7[2];
/* 156 */     debug6.xMax = debug7[3] + 1;
/* 157 */     debug6.yMax = debug7[4] + 1;
/* 158 */     debug6.zMax = debug7[5] + 1;
/* 159 */     return debug6;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\BitSetDiscreteVoxelShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */