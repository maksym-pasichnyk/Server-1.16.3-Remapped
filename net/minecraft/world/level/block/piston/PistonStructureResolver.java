/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PistonStructureResolver
/*     */ {
/*     */   private final Level level;
/*     */   private final BlockPos pistonPos;
/*     */   private final boolean extending;
/*     */   private final BlockPos startPos;
/*     */   private final Direction pushDirection;
/*  22 */   private final List<BlockPos> toPush = Lists.newArrayList();
/*  23 */   private final List<BlockPos> toDestroy = Lists.newArrayList();
/*     */   private final Direction pistonDirection;
/*     */   
/*     */   public PistonStructureResolver(Level debug1, BlockPos debug2, Direction debug3, boolean debug4) {
/*  27 */     this.level = debug1;
/*  28 */     this.pistonPos = debug2;
/*  29 */     this.pistonDirection = debug3;
/*  30 */     this.extending = debug4;
/*     */     
/*  32 */     if (debug4) {
/*  33 */       this.pushDirection = debug3;
/*  34 */       this.startPos = debug2.relative(debug3);
/*     */     } else {
/*  36 */       this.pushDirection = debug3.getOpposite();
/*  37 */       this.startPos = debug2.relative(debug3, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean resolve() {
/*  42 */     this.toPush.clear();
/*  43 */     this.toDestroy.clear();
/*     */     
/*  45 */     BlockState debug1 = this.level.getBlockState(this.startPos);
/*     */     
/*  47 */     if (!PistonBaseBlock.isPushable(debug1, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
/*  48 */       if (this.extending && debug1.getPistonPushReaction() == PushReaction.DESTROY) {
/*  49 */         this.toDestroy.add(this.startPos);
/*  50 */         return true;
/*     */       } 
/*     */       
/*  53 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  57 */     if (!addBlockLine(this.startPos, this.pushDirection))
/*     */     {
/*  59 */       return false;
/*     */     }
/*     */     
/*  62 */     for (int debug2 = 0; debug2 < this.toPush.size(); debug2++) {
/*  63 */       BlockPos debug3 = this.toPush.get(debug2);
/*     */ 
/*     */       
/*  66 */       if (isSticky(this.level.getBlockState(debug3).getBlock()) && 
/*  67 */         !addBranchingBlocks(debug3))
/*     */       {
/*  69 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSticky(Block debug0) {
/*  78 */     return (debug0 == Blocks.SLIME_BLOCK || debug0 == Blocks.HONEY_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canStickToEachOther(Block debug0, Block debug1) {
/*  83 */     if (debug0 == Blocks.HONEY_BLOCK && debug1 == Blocks.SLIME_BLOCK) {
/*  84 */       return false;
/*     */     }
/*  86 */     if (debug0 == Blocks.SLIME_BLOCK && debug1 == Blocks.HONEY_BLOCK) {
/*  87 */       return false;
/*     */     }
/*  89 */     return (isSticky(debug0) || isSticky(debug1));
/*     */   }
/*     */   
/*     */   private boolean addBlockLine(BlockPos debug1, Direction debug2) {
/*  93 */     BlockState debug3 = this.level.getBlockState(debug1);
/*  94 */     Block debug4 = debug3.getBlock();
/*  95 */     if (debug3.isAir())
/*     */     {
/*  97 */       return true; } 
/*  98 */     if (!PistonBaseBlock.isPushable(debug3, this.level, debug1, this.pushDirection, false, debug2))
/*     */     {
/* 100 */       return true; } 
/* 101 */     if (debug1.equals(this.pistonPos))
/*     */     {
/* 103 */       return true; } 
/* 104 */     if (this.toPush.contains(debug1))
/*     */     {
/* 106 */       return true;
/*     */     }
/*     */     
/* 109 */     int debug5 = 1;
/* 110 */     if (debug5 + this.toPush.size() > 12)
/*     */     {
/* 112 */       return false;
/*     */     }
/*     */     
/* 115 */     while (isSticky(debug4)) {
/* 116 */       BlockPos blockPos = debug1.relative(this.pushDirection.getOpposite(), debug5);
/* 117 */       Block block = debug4;
/* 118 */       debug3 = this.level.getBlockState(blockPos);
/* 119 */       debug4 = debug3.getBlock();
/*     */       
/* 121 */       if (debug3.isAir() || !canStickToEachOther(block, debug4) || !PistonBaseBlock.isPushable(debug3, this.level, blockPos, this.pushDirection, false, this.pushDirection.getOpposite()) || blockPos.equals(this.pistonPos)) {
/*     */         break;
/*     */       }
/* 124 */       debug5++;
/* 125 */       if (debug5 + this.toPush.size() > 12) {
/* 126 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 131 */     int debug6 = 0;
/*     */     
/*     */     int debug7;
/* 134 */     for (debug7 = debug5 - 1; debug7 >= 0; debug7--) {
/* 135 */       this.toPush.add(debug1.relative(this.pushDirection.getOpposite(), debug7));
/* 136 */       debug6++;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     for (debug7 = 1;; debug7++) {
/* 141 */       BlockPos debug8 = debug1.relative(this.pushDirection, debug7);
/*     */       
/* 143 */       int debug9 = this.toPush.indexOf(debug8);
/* 144 */       if (debug9 > -1) {
/*     */         
/* 146 */         reorderListAtCollision(debug6, debug9);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 151 */         for (int debug10 = 0; debug10 <= debug9 + debug6; debug10++) {
/* 152 */           BlockPos debug11 = this.toPush.get(debug10);
/* 153 */           if (isSticky(this.level.getBlockState(debug11).getBlock()) && 
/* 154 */             !addBranchingBlocks(debug11)) {
/* 155 */             return false;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 160 */         return true;
/*     */       } 
/*     */       
/* 163 */       debug3 = this.level.getBlockState(debug8);
/*     */       
/* 165 */       if (debug3.isAir())
/*     */       {
/* 167 */         return true;
/*     */       }
/*     */       
/* 170 */       if (!PistonBaseBlock.isPushable(debug3, this.level, debug8, this.pushDirection, true, this.pushDirection) || debug8.equals(this.pistonPos))
/*     */       {
/* 172 */         return false;
/*     */       }
/*     */       
/* 175 */       if (debug3.getPistonPushReaction() == PushReaction.DESTROY) {
/* 176 */         this.toDestroy.add(debug8);
/* 177 */         return true;
/*     */       } 
/*     */       
/* 180 */       if (this.toPush.size() >= 12) {
/* 181 */         return false;
/*     */       }
/*     */       
/* 184 */       this.toPush.add(debug8);
/* 185 */       debug6++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reorderListAtCollision(int debug1, int debug2) {
/* 190 */     List<BlockPos> debug3 = Lists.newArrayList();
/* 191 */     List<BlockPos> debug4 = Lists.newArrayList();
/* 192 */     List<BlockPos> debug5 = Lists.newArrayList();
/*     */     
/* 194 */     debug3.addAll(this.toPush.subList(0, debug2));
/* 195 */     debug4.addAll(this.toPush.subList(this.toPush.size() - debug1, this.toPush.size()));
/* 196 */     debug5.addAll(this.toPush.subList(debug2, this.toPush.size() - debug1));
/*     */     
/* 198 */     this.toPush.clear();
/* 199 */     this.toPush.addAll(debug3);
/* 200 */     this.toPush.addAll(debug4);
/* 201 */     this.toPush.addAll(debug5);
/*     */   }
/*     */   
/*     */   private boolean addBranchingBlocks(BlockPos debug1) {
/* 205 */     BlockState debug2 = this.level.getBlockState(debug1);
/* 206 */     for (Direction debug6 : Direction.values()) {
/* 207 */       if (debug6.getAxis() != this.pushDirection.getAxis()) {
/* 208 */         BlockPos debug7 = debug1.relative(debug6);
/* 209 */         BlockState debug8 = this.level.getBlockState(debug7);
/* 210 */         if (canStickToEachOther(debug8.getBlock(), debug2.getBlock()))
/*     */         {
/*     */           
/* 213 */           if (!addBlockLine(debug7, debug6)) {
/* 214 */             return false;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/* 219 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BlockPos> getToPush() {
/* 227 */     return this.toPush;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getToDestroy() {
/* 231 */     return this.toDestroy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\PistonStructureResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */