/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MoveControl
/*     */ {
/*     */   protected final Mob mob;
/*     */   protected double wantedX;
/*     */   protected double wantedY;
/*     */   protected double wantedZ;
/*     */   protected double speedModifier;
/*     */   protected float strafeForwards;
/*     */   protected float strafeRight;
/*  29 */   protected Operation operation = Operation.WAIT;
/*     */   
/*     */   public MoveControl(Mob debug1) {
/*  32 */     this.mob = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasWanted() {
/*  36 */     return (this.operation == Operation.MOVE_TO);
/*     */   }
/*     */   
/*     */   public double getSpeedModifier() {
/*  40 */     return this.speedModifier;
/*     */   }
/*     */   
/*     */   public void setWantedPosition(double debug1, double debug3, double debug5, double debug7) {
/*  44 */     this.wantedX = debug1;
/*  45 */     this.wantedY = debug3;
/*  46 */     this.wantedZ = debug5;
/*  47 */     this.speedModifier = debug7;
/*  48 */     if (this.operation != Operation.JUMPING) {
/*  49 */       this.operation = Operation.MOVE_TO;
/*     */     }
/*     */   }
/*     */   
/*     */   public void strafe(float debug1, float debug2) {
/*  54 */     this.operation = Operation.STRAFE;
/*  55 */     this.strafeForwards = debug1;
/*  56 */     this.strafeRight = debug2;
/*  57 */     this.speedModifier = 0.25D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  71 */     if (this.operation == Operation.STRAFE) {
/*  72 */       float debug1 = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
/*  73 */       float debug2 = (float)this.speedModifier * debug1;
/*     */       
/*  75 */       float debug3 = this.strafeForwards;
/*  76 */       float debug4 = this.strafeRight;
/*  77 */       float debug5 = Mth.sqrt(debug3 * debug3 + debug4 * debug4);
/*  78 */       if (debug5 < 1.0F) {
/*  79 */         debug5 = 1.0F;
/*     */       }
/*  81 */       debug5 = debug2 / debug5;
/*  82 */       debug3 *= debug5;
/*  83 */       debug4 *= debug5;
/*     */       
/*  85 */       float debug6 = Mth.sin(this.mob.yRot * 0.017453292F);
/*  86 */       float debug7 = Mth.cos(this.mob.yRot * 0.017453292F);
/*  87 */       float debug8 = debug3 * debug7 - debug4 * debug6;
/*  88 */       float debug9 = debug4 * debug7 + debug3 * debug6;
/*     */       
/*  90 */       if (!isWalkable(debug8, debug9)) {
/*     */         
/*  92 */         this.strafeForwards = 1.0F;
/*  93 */         this.strafeRight = 0.0F;
/*     */       } 
/*     */       
/*  96 */       this.mob.setSpeed(debug2);
/*  97 */       this.mob.setZza(this.strafeForwards);
/*  98 */       this.mob.setXxa(this.strafeRight);
/*     */       
/* 100 */       this.operation = Operation.WAIT;
/* 101 */     } else if (this.operation == Operation.MOVE_TO) {
/* 102 */       this.operation = Operation.WAIT;
/*     */       
/* 104 */       double debug1 = this.wantedX - this.mob.getX();
/* 105 */       double debug3 = this.wantedZ - this.mob.getZ();
/* 106 */       double debug5 = this.wantedY - this.mob.getY();
/* 107 */       double debug7 = debug1 * debug1 + debug5 * debug5 + debug3 * debug3;
/* 108 */       if (debug7 < 2.500000277905201E-7D) {
/* 109 */         this.mob.setZza(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 113 */       float debug9 = (float)(Mth.atan2(debug3, debug1) * 57.2957763671875D) - 90.0F;
/*     */       
/* 115 */       this.mob.yRot = rotlerp(this.mob.yRot, debug9, 90.0F);
/* 116 */       this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       
/* 118 */       BlockPos debug10 = this.mob.blockPosition();
/* 119 */       BlockState debug11 = this.mob.level.getBlockState(debug10);
/* 120 */       Block debug12 = debug11.getBlock();
/* 121 */       VoxelShape debug13 = debug11.getCollisionShape((BlockGetter)this.mob.level, debug10);
/* 122 */       if ((debug5 > this.mob.maxUpStep && debug1 * debug1 + debug3 * debug3 < Math.max(1.0F, this.mob.getBbWidth())) || (
/* 123 */         !debug13.isEmpty() && this.mob.getY() < debug13.max(Direction.Axis.Y) + debug10.getY() && !debug12.is((Tag)BlockTags.DOORS) && !debug12.is((Tag)BlockTags.FENCES))) {
/*     */         
/* 125 */         this.mob.getJumpControl().jump();
/* 126 */         this.operation = Operation.JUMPING;
/*     */       } 
/* 128 */     } else if (this.operation == Operation.JUMPING) {
/* 129 */       this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 130 */       if (this.mob.isOnGround()) {
/* 131 */         this.operation = Operation.WAIT;
/*     */       }
/*     */     } else {
/* 134 */       this.mob.setZza(0.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isWalkable(float debug1, float debug2) {
/* 139 */     PathNavigation debug3 = this.mob.getNavigation();
/* 140 */     if (debug3 != null) {
/* 141 */       NodeEvaluator debug4 = debug3.getNodeEvaluator();
/* 142 */       if (debug4 != null && debug4.getBlockPathType((BlockGetter)this.mob.level, Mth.floor(this.mob.getX() + debug1), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + debug2)) != BlockPathTypes.WALKABLE) {
/* 143 */         return false;
/*     */       }
/*     */     } 
/* 146 */     return true;
/*     */   }
/*     */   
/*     */   protected float rotlerp(float debug1, float debug2, float debug3) {
/* 150 */     float debug4 = Mth.wrapDegrees(debug2 - debug1);
/* 151 */     if (debug4 > debug3) {
/* 152 */       debug4 = debug3;
/*     */     }
/* 154 */     if (debug4 < -debug3) {
/* 155 */       debug4 = -debug3;
/*     */     }
/* 157 */     float debug5 = debug1 + debug4;
/* 158 */     if (debug5 < 0.0F) {
/* 159 */       debug5 += 360.0F;
/* 160 */     } else if (debug5 > 360.0F) {
/* 161 */       debug5 -= 360.0F;
/*     */     } 
/* 163 */     return debug5;
/*     */   }
/*     */   
/*     */   public double getWantedX() {
/* 167 */     return this.wantedX;
/*     */   }
/*     */   
/*     */   public double getWantedY() {
/* 171 */     return this.wantedY;
/*     */   }
/*     */   
/*     */   public double getWantedZ() {
/* 175 */     return this.wantedZ;
/*     */   }
/*     */   
/*     */   public enum Operation {
/* 179 */     WAIT,
/* 180 */     MOVE_TO,
/* 181 */     STRAFE,
/* 182 */     JUMPING;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\MoveControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */