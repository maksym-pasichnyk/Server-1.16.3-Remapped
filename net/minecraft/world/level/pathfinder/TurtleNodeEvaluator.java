/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TurtleNodeEvaluator
/*     */   extends WalkNodeEvaluator {
/*     */   private float oldWalkableCost;
/*     */   private float oldWaterBorderCost;
/*     */   
/*     */   public void prepare(PathNavigationRegion debug1, Mob debug2) {
/*  24 */     super.prepare(debug1, debug2);
/*  25 */     debug2.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*  26 */     this.oldWalkableCost = debug2.getPathfindingMalus(BlockPathTypes.WALKABLE);
/*  27 */     debug2.setPathfindingMalus(BlockPathTypes.WALKABLE, 6.0F);
/*  28 */     this.oldWaterBorderCost = debug2.getPathfindingMalus(BlockPathTypes.WATER_BORDER);
/*  29 */     debug2.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  34 */     this.mob.setPathfindingMalus(BlockPathTypes.WALKABLE, this.oldWalkableCost);
/*  35 */     this.mob.setPathfindingMalus(BlockPathTypes.WATER_BORDER, this.oldWaterBorderCost);
/*  36 */     super.done();
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  41 */     return getNode(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getGoal(double debug1, double debug3, double debug5) {
/*  47 */     return new Target(getNode(Mth.floor(debug1), Mth.floor(debug3 + 0.5D), Mth.floor(debug5)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] debug1, Node debug2) {
/*  53 */     int debug3 = 0;
/*  54 */     int debug4 = 1;
/*     */     
/*  56 */     BlockPos debug5 = new BlockPos(debug2.x, debug2.y, debug2.z);
/*  57 */     double debug6 = inWaterDependentPosHeight(debug5);
/*     */     
/*  59 */     Node debug8 = getAcceptedNode(debug2.x, debug2.y, debug2.z + 1, 1, debug6);
/*  60 */     Node debug9 = getAcceptedNode(debug2.x - 1, debug2.y, debug2.z, 1, debug6);
/*  61 */     Node debug10 = getAcceptedNode(debug2.x + 1, debug2.y, debug2.z, 1, debug6);
/*  62 */     Node debug11 = getAcceptedNode(debug2.x, debug2.y, debug2.z - 1, 1, debug6);
/*  63 */     Node debug12 = getAcceptedNode(debug2.x, debug2.y + 1, debug2.z, 0, debug6);
/*  64 */     Node debug13 = getAcceptedNode(debug2.x, debug2.y - 1, debug2.z, 1, debug6);
/*     */     
/*  66 */     if (debug8 != null && !debug8.closed) {
/*  67 */       debug1[debug3++] = debug8;
/*     */     }
/*  69 */     if (debug9 != null && !debug9.closed) {
/*  70 */       debug1[debug3++] = debug9;
/*     */     }
/*  72 */     if (debug10 != null && !debug10.closed) {
/*  73 */       debug1[debug3++] = debug10;
/*     */     }
/*  75 */     if (debug11 != null && !debug11.closed) {
/*  76 */       debug1[debug3++] = debug11;
/*     */     }
/*  78 */     if (debug12 != null && !debug12.closed) {
/*  79 */       debug1[debug3++] = debug12;
/*     */     }
/*  81 */     if (debug13 != null && !debug13.closed) {
/*  82 */       debug1[debug3++] = debug13;
/*     */     }
/*     */     
/*  85 */     boolean debug14 = (debug11 == null || debug11.type == BlockPathTypes.OPEN || debug11.costMalus != 0.0F);
/*  86 */     boolean debug15 = (debug8 == null || debug8.type == BlockPathTypes.OPEN || debug8.costMalus != 0.0F);
/*  87 */     boolean debug16 = (debug10 == null || debug10.type == BlockPathTypes.OPEN || debug10.costMalus != 0.0F);
/*  88 */     boolean debug17 = (debug9 == null || debug9.type == BlockPathTypes.OPEN || debug9.costMalus != 0.0F);
/*     */     
/*  90 */     if (debug14 && debug17) {
/*  91 */       Node debug18 = getAcceptedNode(debug2.x - 1, debug2.y, debug2.z - 1, 1, debug6);
/*  92 */       if (debug18 != null && !debug18.closed) {
/*  93 */         debug1[debug3++] = debug18;
/*     */       }
/*     */     } 
/*  96 */     if (debug14 && debug16) {
/*  97 */       Node debug18 = getAcceptedNode(debug2.x + 1, debug2.y, debug2.z - 1, 1, debug6);
/*  98 */       if (debug18 != null && !debug18.closed) {
/*  99 */         debug1[debug3++] = debug18;
/*     */       }
/*     */     } 
/* 102 */     if (debug15 && debug17) {
/* 103 */       Node debug18 = getAcceptedNode(debug2.x - 1, debug2.y, debug2.z + 1, 1, debug6);
/* 104 */       if (debug18 != null && !debug18.closed) {
/* 105 */         debug1[debug3++] = debug18;
/*     */       }
/*     */     } 
/* 108 */     if (debug15 && debug16) {
/* 109 */       Node debug18 = getAcceptedNode(debug2.x + 1, debug2.y, debug2.z + 1, 1, debug6);
/* 110 */       if (debug18 != null && !debug18.closed) {
/* 111 */         debug1[debug3++] = debug18;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     return debug3;
/*     */   }
/*     */   
/*     */   private double inWaterDependentPosHeight(BlockPos debug1) {
/* 119 */     if (!this.mob.isInWater()) {
/* 120 */       BlockPos debug2 = debug1.below();
/* 121 */       VoxelShape debug3 = this.level.getBlockState(debug2).getCollisionShape((BlockGetter)this.level, debug2);
/* 122 */       return debug2.getY() + (debug3.isEmpty() ? 0.0D : debug3.max(Direction.Axis.Y));
/*     */     } 
/* 124 */     return debug1.getY() + 0.5D;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Node getAcceptedNode(int debug1, int debug2, int debug3, int debug4, double debug5) {
/* 129 */     Node debug7 = null;
/*     */     
/* 131 */     BlockPos debug8 = new BlockPos(debug1, debug2, debug3);
/* 132 */     double debug9 = inWaterDependentPosHeight(debug8);
/*     */ 
/*     */     
/* 135 */     if (debug9 - debug5 > 1.125D) {
/* 136 */       return null;
/*     */     }
/*     */     
/* 139 */     BlockPathTypes debug11 = getBlockPathType((BlockGetter)this.level, debug1, debug2, debug3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
/*     */     
/* 141 */     float debug12 = this.mob.getPathfindingMalus(debug11);
/* 142 */     double debug13 = this.mob.getBbWidth() / 2.0D;
/*     */     
/* 144 */     if (debug12 >= 0.0F) {
/* 145 */       debug7 = getNode(debug1, debug2, debug3);
/* 146 */       debug7.type = debug11;
/* 147 */       debug7.costMalus = Math.max(debug7.costMalus, debug12);
/*     */     } 
/*     */     
/* 150 */     if (debug11 == BlockPathTypes.WATER || debug11 == BlockPathTypes.WALKABLE) {
/*     */       
/* 152 */       if (debug2 < this.mob.level.getSeaLevel() - 10 && debug7 != null) {
/* 153 */         debug7.costMalus++;
/*     */       }
/*     */       
/* 156 */       return debug7;
/*     */     } 
/*     */     
/* 159 */     if (debug7 == null && debug4 > 0 && debug11 != BlockPathTypes.FENCE && debug11 != BlockPathTypes.UNPASSABLE_RAIL && debug11 != BlockPathTypes.TRAPDOOR) {
/* 160 */       debug7 = getAcceptedNode(debug1, debug2 + 1, debug3, debug4 - 1, debug5);
/*     */     }
/*     */     
/* 163 */     if (debug11 == BlockPathTypes.OPEN) {
/*     */       
/* 165 */       AABB debug15 = new AABB(debug1 - debug13 + 0.5D, debug2 + 0.001D, debug3 - debug13 + 0.5D, debug1 + debug13 + 0.5D, (debug2 + this.mob.getBbHeight()), debug3 + debug13 + 0.5D);
/* 166 */       if (!this.mob.level.noCollision((Entity)this.mob, debug15)) {
/* 167 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 171 */       BlockPathTypes debug16 = getBlockPathType((BlockGetter)this.level, debug1, debug2 - 1, debug3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
/* 172 */       if (debug16 == BlockPathTypes.BLOCKED) {
/* 173 */         debug7 = getNode(debug1, debug2, debug3);
/* 174 */         debug7.type = BlockPathTypes.WALKABLE;
/* 175 */         debug7.costMalus = Math.max(debug7.costMalus, debug12);
/* 176 */         return debug7;
/*     */       } 
/* 178 */       if (debug16 == BlockPathTypes.WATER) {
/* 179 */         debug7 = getNode(debug1, debug2, debug3);
/* 180 */         debug7.type = BlockPathTypes.WATER;
/* 181 */         debug7.costMalus = Math.max(debug7.costMalus, debug12);
/* 182 */         return debug7;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 187 */       int debug17 = 0;
/* 188 */       while (debug2 > 0 && debug11 == BlockPathTypes.OPEN) {
/* 189 */         debug2--;
/*     */         
/* 191 */         if (debug17++ >= this.mob.getMaxFallDistance()) {
/* 192 */           return null;
/*     */         }
/*     */         
/* 195 */         debug11 = getBlockPathType((BlockGetter)this.level, debug1, debug2, debug3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
/* 196 */         debug12 = this.mob.getPathfindingMalus(debug11);
/*     */         
/* 198 */         if (debug11 != BlockPathTypes.OPEN && debug12 >= 0.0F) {
/* 199 */           debug7 = getNode(debug1, debug2, debug3);
/* 200 */           debug7.type = debug11;
/* 201 */           debug7.costMalus = Math.max(debug7.costMalus, debug12); break;
/*     */         } 
/* 203 */         if (debug12 < 0.0F) {
/* 204 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     return debug7;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPathTypes evaluateBlockPathType(BlockGetter debug1, boolean debug2, boolean debug3, BlockPos debug4, BlockPathTypes debug5) {
/* 214 */     if (debug5 == BlockPathTypes.RAIL && !(debug1.getBlockState(debug4).getBlock() instanceof net.minecraft.world.level.block.BaseRailBlock) && !(debug1.getBlockState(debug4.below()).getBlock() instanceof net.minecraft.world.level.block.BaseRailBlock)) {
/* 215 */       debug5 = BlockPathTypes.UNPASSABLE_RAIL;
/*     */     }
/* 217 */     if (debug5 == BlockPathTypes.DOOR_OPEN || debug5 == BlockPathTypes.DOOR_WOOD_CLOSED || debug5 == BlockPathTypes.DOOR_IRON_CLOSED) {
/* 218 */       debug5 = BlockPathTypes.BLOCKED;
/*     */     }
/* 220 */     if (debug5 == BlockPathTypes.LEAVES) {
/* 221 */       debug5 = BlockPathTypes.BLOCKED;
/*     */     }
/* 223 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4) {
/* 228 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 229 */     BlockPathTypes debug6 = getBlockPathTypeRaw(debug1, (BlockPos)debug5.set(debug2, debug3, debug4));
/*     */     
/* 231 */     if (debug6 == BlockPathTypes.WATER) {
/* 232 */       for (Direction debug10 : Direction.values()) {
/* 233 */         BlockPathTypes debug11 = getBlockPathTypeRaw(debug1, (BlockPos)debug5.set(debug2, debug3, debug4).move(debug10));
/* 234 */         if (debug11 == BlockPathTypes.BLOCKED) {
/* 235 */           return BlockPathTypes.WATER_BORDER;
/*     */         }
/*     */       } 
/*     */       
/* 239 */       return BlockPathTypes.WATER;
/*     */     } 
/*     */     
/* 242 */     if (debug6 == BlockPathTypes.OPEN && debug3 >= 1) {
/* 243 */       BlockState debug7 = debug1.getBlockState(new BlockPos(debug2, debug3 - 1, debug4));
/* 244 */       BlockPathTypes debug8 = getBlockPathTypeRaw(debug1, (BlockPos)debug5.set(debug2, debug3 - 1, debug4));
/* 245 */       if (debug8 == BlockPathTypes.WALKABLE || debug8 == BlockPathTypes.OPEN || debug8 == BlockPathTypes.LAVA) {
/* 246 */         debug6 = BlockPathTypes.OPEN;
/*     */       } else {
/* 248 */         debug6 = BlockPathTypes.WALKABLE;
/*     */       } 
/*     */       
/* 251 */       if (debug8 == BlockPathTypes.DAMAGE_FIRE || debug7.is(Blocks.MAGMA_BLOCK) || debug7.is((Tag)BlockTags.CAMPFIRES)) {
/* 252 */         debug6 = BlockPathTypes.DAMAGE_FIRE;
/*     */       }
/*     */       
/* 255 */       if (debug8 == BlockPathTypes.DAMAGE_CACTUS) {
/* 256 */         debug6 = BlockPathTypes.DAMAGE_CACTUS;
/*     */       }
/*     */       
/* 259 */       if (debug8 == BlockPathTypes.DAMAGE_OTHER) {
/* 260 */         debug6 = BlockPathTypes.DAMAGE_OTHER;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 265 */     if (debug6 == BlockPathTypes.WALKABLE) {
/* 266 */       debug6 = checkNeighbourBlocks(debug1, debug5.set(debug2, debug3, debug4), debug6);
/*     */     }
/*     */     
/* 269 */     return debug6;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\TurtleNodeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */