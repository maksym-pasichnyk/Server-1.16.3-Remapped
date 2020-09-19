/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class FlyNodeEvaluator
/*     */   extends WalkNodeEvaluator
/*     */ {
/*     */   public void prepare(PathNavigationRegion debug1, Mob debug2) {
/*  22 */     super.prepare(debug1, debug2);
/*  23 */     this.oldWaterCost = debug2.getPathfindingMalus(BlockPathTypes.WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  28 */     this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
/*  29 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*     */     int debug1;
/*  36 */     if (canFloat() && this.mob.isInWater()) {
/*  37 */       debug1 = Mth.floor(this.mob.getY());
/*  38 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.mob.getX(), debug1, this.mob.getZ());
/*  39 */       Block block = this.level.getBlockState((BlockPos)mutableBlockPos).getBlock();
/*  40 */       while (block == Blocks.WATER) {
/*  41 */         debug1++;
/*  42 */         mutableBlockPos.set(this.mob.getX(), debug1, this.mob.getZ());
/*  43 */         block = this.level.getBlockState((BlockPos)mutableBlockPos).getBlock();
/*     */       } 
/*     */     } else {
/*  46 */       debug1 = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } 
/*     */     
/*  49 */     BlockPos debug2 = this.mob.blockPosition();
/*  50 */     BlockPathTypes debug3 = getBlockPathType(this.mob, debug2.getX(), debug1, debug2.getZ());
/*     */     
/*  52 */     if (this.mob.getPathfindingMalus(debug3) < 0.0F) {
/*  53 */       Set<BlockPos> debug4 = Sets.newHashSet();
/*  54 */       debug4.add(new BlockPos((this.mob.getBoundingBox()).minX, debug1, (this.mob.getBoundingBox()).minZ));
/*  55 */       debug4.add(new BlockPos((this.mob.getBoundingBox()).minX, debug1, (this.mob.getBoundingBox()).maxZ));
/*  56 */       debug4.add(new BlockPos((this.mob.getBoundingBox()).maxX, debug1, (this.mob.getBoundingBox()).minZ));
/*  57 */       debug4.add(new BlockPos((this.mob.getBoundingBox()).maxX, debug1, (this.mob.getBoundingBox()).maxZ));
/*     */       
/*  59 */       for (BlockPos debug6 : debug4) {
/*  60 */         BlockPathTypes debug7 = getBlockPathType(this.mob, debug6);
/*  61 */         if (this.mob.getPathfindingMalus(debug7) >= 0.0F) {
/*  62 */           return super.getNode(debug6.getX(), debug6.getY(), debug6.getZ());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return super.getNode(debug2.getX(), debug1, debug2.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getGoal(double debug1, double debug3, double debug5) {
/*  72 */     return new Target(super.getNode(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] debug1, Node debug2) {
/*  77 */     int debug3 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     Node debug4 = getNode(debug2.x, debug2.y, debug2.z + 1);
/*  83 */     if (isOpen(debug4)) {
/*  84 */       debug1[debug3++] = debug4;
/*     */     }
/*     */     
/*  87 */     Node debug5 = getNode(debug2.x - 1, debug2.y, debug2.z);
/*  88 */     if (isOpen(debug5)) {
/*  89 */       debug1[debug3++] = debug5;
/*     */     }
/*     */     
/*  92 */     Node debug6 = getNode(debug2.x + 1, debug2.y, debug2.z);
/*  93 */     if (isOpen(debug6)) {
/*  94 */       debug1[debug3++] = debug6;
/*     */     }
/*     */     
/*  97 */     Node debug7 = getNode(debug2.x, debug2.y, debug2.z - 1);
/*  98 */     if (isOpen(debug7)) {
/*  99 */       debug1[debug3++] = debug7;
/*     */     }
/*     */     
/* 102 */     Node debug8 = getNode(debug2.x, debug2.y + 1, debug2.z);
/* 103 */     if (isOpen(debug8)) {
/* 104 */       debug1[debug3++] = debug8;
/*     */     }
/*     */     
/* 107 */     Node debug9 = getNode(debug2.x, debug2.y - 1, debug2.z);
/* 108 */     if (isOpen(debug9)) {
/* 109 */       debug1[debug3++] = debug9;
/*     */     }
/*     */     
/* 112 */     Node debug10 = getNode(debug2.x, debug2.y + 1, debug2.z + 1);
/* 113 */     if (isOpen(debug10) && hasMalus(debug4) && hasMalus(debug8)) {
/* 114 */       debug1[debug3++] = debug10;
/*     */     }
/*     */     
/* 117 */     Node debug11 = getNode(debug2.x - 1, debug2.y + 1, debug2.z);
/* 118 */     if (isOpen(debug11) && hasMalus(debug5) && hasMalus(debug8)) {
/* 119 */       debug1[debug3++] = debug11;
/*     */     }
/*     */     
/* 122 */     Node debug12 = getNode(debug2.x + 1, debug2.y + 1, debug2.z);
/* 123 */     if (isOpen(debug12) && hasMalus(debug6) && hasMalus(debug8)) {
/* 124 */       debug1[debug3++] = debug12;
/*     */     }
/*     */     
/* 127 */     Node debug13 = getNode(debug2.x, debug2.y + 1, debug2.z - 1);
/* 128 */     if (isOpen(debug13) && hasMalus(debug7) && hasMalus(debug8)) {
/* 129 */       debug1[debug3++] = debug13;
/*     */     }
/*     */     
/* 132 */     Node debug14 = getNode(debug2.x, debug2.y - 1, debug2.z + 1);
/* 133 */     if (isOpen(debug14) && hasMalus(debug4) && hasMalus(debug9)) {
/* 134 */       debug1[debug3++] = debug14;
/*     */     }
/*     */     
/* 137 */     Node debug15 = getNode(debug2.x - 1, debug2.y - 1, debug2.z);
/* 138 */     if (isOpen(debug15) && hasMalus(debug5) && hasMalus(debug9)) {
/* 139 */       debug1[debug3++] = debug15;
/*     */     }
/*     */     
/* 142 */     Node debug16 = getNode(debug2.x + 1, debug2.y - 1, debug2.z);
/* 143 */     if (isOpen(debug16) && hasMalus(debug6) && hasMalus(debug9)) {
/* 144 */       debug1[debug3++] = debug16;
/*     */     }
/*     */     
/* 147 */     Node debug17 = getNode(debug2.x, debug2.y - 1, debug2.z - 1);
/* 148 */     if (isOpen(debug17) && hasMalus(debug7) && hasMalus(debug9)) {
/* 149 */       debug1[debug3++] = debug17;
/*     */     }
/*     */     
/* 152 */     Node debug18 = getNode(debug2.x + 1, debug2.y, debug2.z - 1);
/* 153 */     if (isOpen(debug18) && hasMalus(debug7) && hasMalus(debug6)) {
/* 154 */       debug1[debug3++] = debug18;
/*     */     }
/*     */     
/* 157 */     Node debug19 = getNode(debug2.x + 1, debug2.y, debug2.z + 1);
/* 158 */     if (isOpen(debug19) && hasMalus(debug4) && hasMalus(debug6)) {
/* 159 */       debug1[debug3++] = debug19;
/*     */     }
/*     */     
/* 162 */     Node debug20 = getNode(debug2.x - 1, debug2.y, debug2.z - 1);
/* 163 */     if (isOpen(debug20) && hasMalus(debug7) && hasMalus(debug5)) {
/* 164 */       debug1[debug3++] = debug20;
/*     */     }
/*     */     
/* 167 */     Node debug21 = getNode(debug2.x - 1, debug2.y, debug2.z + 1);
/* 168 */     if (isOpen(debug21) && hasMalus(debug4) && hasMalus(debug5)) {
/* 169 */       debug1[debug3++] = debug21;
/*     */     }
/*     */     
/* 172 */     Node debug22 = getNode(debug2.x + 1, debug2.y + 1, debug2.z - 1);
/* 173 */     if (isOpen(debug22) && hasMalus(debug18) && hasMalus(debug7) && hasMalus(debug6) && hasMalus(debug8) && hasMalus(debug13) && hasMalus(debug12)) {
/* 174 */       debug1[debug3++] = debug22;
/*     */     }
/*     */     
/* 177 */     Node debug23 = getNode(debug2.x + 1, debug2.y + 1, debug2.z + 1);
/* 178 */     if (isOpen(debug23) && hasMalus(debug19) && hasMalus(debug4) && hasMalus(debug6) && hasMalus(debug8) && hasMalus(debug10) && hasMalus(debug12)) {
/* 179 */       debug1[debug3++] = debug23;
/*     */     }
/*     */     
/* 182 */     Node debug24 = getNode(debug2.x - 1, debug2.y + 1, debug2.z - 1);
/* 183 */     if (isOpen(debug24) && hasMalus(debug20) && hasMalus(debug7) && (hasMalus(debug5) & hasMalus(debug8)) != 0 && hasMalus(debug13) && hasMalus(debug11)) {
/* 184 */       debug1[debug3++] = debug24;
/*     */     }
/*     */     
/* 187 */     Node debug25 = getNode(debug2.x - 1, debug2.y + 1, debug2.z + 1);
/* 188 */     if (isOpen(debug25) && hasMalus(debug21) && hasMalus(debug4) && (hasMalus(debug5) & hasMalus(debug8)) != 0 && hasMalus(debug10) && hasMalus(debug11)) {
/* 189 */       debug1[debug3++] = debug25;
/*     */     }
/*     */     
/* 192 */     Node debug26 = getNode(debug2.x + 1, debug2.y - 1, debug2.z - 1);
/* 193 */     if (isOpen(debug26) && hasMalus(debug18) && hasMalus(debug7) && hasMalus(debug6) && hasMalus(debug9) && hasMalus(debug17) && hasMalus(debug16)) {
/* 194 */       debug1[debug3++] = debug26;
/*     */     }
/*     */     
/* 197 */     Node debug27 = getNode(debug2.x + 1, debug2.y - 1, debug2.z + 1);
/* 198 */     if (isOpen(debug27) && hasMalus(debug19) && hasMalus(debug4) && hasMalus(debug6) && hasMalus(debug9) && hasMalus(debug14) && hasMalus(debug16)) {
/* 199 */       debug1[debug3++] = debug27;
/*     */     }
/*     */     
/* 202 */     Node debug28 = getNode(debug2.x - 1, debug2.y - 1, debug2.z - 1);
/* 203 */     if (isOpen(debug28) && hasMalus(debug20) && hasMalus(debug7) && hasMalus(debug5) && hasMalus(debug9) && hasMalus(debug17) && hasMalus(debug15)) {
/* 204 */       debug1[debug3++] = debug28;
/*     */     }
/*     */     
/* 207 */     Node debug29 = getNode(debug2.x - 1, debug2.y - 1, debug2.z + 1);
/* 208 */     if (isOpen(debug29) && hasMalus(debug21) && hasMalus(debug4) && hasMalus(debug5) && hasMalus(debug9) && hasMalus(debug14) && hasMalus(debug15)) {
/* 209 */       debug1[debug3++] = debug29;
/*     */     }
/*     */     
/* 212 */     return debug3;
/*     */   }
/*     */   
/*     */   private boolean hasMalus(@Nullable Node debug1) {
/* 216 */     return (debug1 != null && debug1.costMalus >= 0.0F);
/*     */   }
/*     */   
/*     */   private boolean isOpen(@Nullable Node debug1) {
/* 220 */     return (debug1 != null && !debug1.closed);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Node getNode(int debug1, int debug2, int debug3) {
/* 226 */     Node debug4 = null;
/*     */     
/* 228 */     BlockPathTypes debug5 = getBlockPathType(this.mob, debug1, debug2, debug3);
/*     */     
/* 230 */     float debug6 = this.mob.getPathfindingMalus(debug5);
/*     */     
/* 232 */     if (debug6 >= 0.0F) {
/* 233 */       debug4 = super.getNode(debug1, debug2, debug3);
/* 234 */       debug4.type = debug5;
/* 235 */       debug4.costMalus = Math.max(debug4.costMalus, debug6);
/*     */       
/* 237 */       if (debug5 == BlockPathTypes.WALKABLE) {
/* 238 */         debug4.costMalus++;
/*     */       }
/*     */     } 
/*     */     
/* 242 */     if (debug5 == BlockPathTypes.OPEN || debug5 == BlockPathTypes.WALKABLE) {
/* 243 */       return debug4;
/*     */     }
/*     */     
/* 246 */     return debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4, Mob debug5, int debug6, int debug7, int debug8, boolean debug9, boolean debug10) {
/* 252 */     EnumSet<BlockPathTypes> debug11 = EnumSet.noneOf(BlockPathTypes.class);
/* 253 */     BlockPathTypes debug12 = BlockPathTypes.BLOCKED;
/*     */     
/* 255 */     BlockPos debug13 = debug5.blockPosition();
/*     */     
/* 257 */     debug12 = getBlockPathTypes(debug1, debug2, debug3, debug4, debug6, debug7, debug8, debug9, debug10, debug11, debug12, debug13);
/*     */     
/* 259 */     if (debug11.contains(BlockPathTypes.FENCE)) {
/* 260 */       return BlockPathTypes.FENCE;
/*     */     }
/*     */     
/* 263 */     BlockPathTypes debug14 = BlockPathTypes.BLOCKED;
/* 264 */     for (BlockPathTypes debug16 : debug11) {
/*     */       
/* 266 */       if (debug5.getPathfindingMalus(debug16) < 0.0F) {
/* 267 */         return debug16;
/*     */       }
/*     */ 
/*     */       
/* 271 */       if (debug5.getPathfindingMalus(debug16) >= debug5.getPathfindingMalus(debug14)) {
/* 272 */         debug14 = debug16;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 277 */     if (debug12 == BlockPathTypes.OPEN && debug5.getPathfindingMalus(debug14) == 0.0F) {
/* 278 */       return BlockPathTypes.OPEN;
/*     */     }
/*     */     
/* 281 */     return debug14;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4) {
/* 286 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 287 */     BlockPathTypes debug6 = getBlockPathTypeRaw(debug1, (BlockPos)debug5.set(debug2, debug3, debug4));
/*     */     
/* 289 */     if (debug6 == BlockPathTypes.OPEN && debug3 >= 1) {
/* 290 */       BlockState debug7 = debug1.getBlockState((BlockPos)debug5.set(debug2, debug3 - 1, debug4));
/* 291 */       BlockPathTypes debug8 = getBlockPathTypeRaw(debug1, (BlockPos)debug5.set(debug2, debug3 - 1, debug4));
/*     */       
/* 293 */       if (debug8 == BlockPathTypes.DAMAGE_FIRE || debug7.is(Blocks.MAGMA_BLOCK) || debug8 == BlockPathTypes.LAVA || debug7.is((Tag)BlockTags.CAMPFIRES)) {
/* 294 */         debug6 = BlockPathTypes.DAMAGE_FIRE;
/* 295 */       } else if (debug8 == BlockPathTypes.DAMAGE_CACTUS) {
/* 296 */         debug6 = BlockPathTypes.DAMAGE_CACTUS;
/* 297 */       } else if (debug8 == BlockPathTypes.DAMAGE_OTHER) {
/* 298 */         debug6 = BlockPathTypes.DAMAGE_OTHER;
/* 299 */       } else if (debug8 == BlockPathTypes.COCOA) {
/* 300 */         debug6 = BlockPathTypes.COCOA;
/* 301 */       } else if (debug8 == BlockPathTypes.FENCE) {
/* 302 */         debug6 = BlockPathTypes.FENCE;
/*     */       } else {
/* 304 */         debug6 = (debug8 == BlockPathTypes.WALKABLE || debug8 == BlockPathTypes.OPEN || debug8 == BlockPathTypes.WATER) ? BlockPathTypes.OPEN : BlockPathTypes.WALKABLE;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 309 */     if (debug6 == BlockPathTypes.WALKABLE || debug6 == BlockPathTypes.OPEN) {
/* 310 */       debug6 = checkNeighbourBlocks(debug1, debug5.set(debug2, debug3, debug4), debug6);
/*     */     }
/*     */     
/* 313 */     return debug6;
/*     */   }
/*     */   
/*     */   private BlockPathTypes getBlockPathType(Mob debug1, BlockPos debug2) {
/* 317 */     return getBlockPathType(debug1, debug2.getX(), debug2.getY(), debug2.getZ());
/*     */   }
/*     */   
/*     */   private BlockPathTypes getBlockPathType(Mob debug1, int debug2, int debug3, int debug4) {
/* 321 */     return getBlockPathType((BlockGetter)this.level, debug2, debug3, debug4, debug1, this.entityWidth, this.entityHeight, this.entityDepth, canOpenDoors(), canPassDoors());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\FlyNodeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */