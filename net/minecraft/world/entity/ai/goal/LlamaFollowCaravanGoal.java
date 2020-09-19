/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.horse.Llama;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LlamaFollowCaravanGoal
/*     */   extends Goal
/*     */ {
/*     */   public final Llama llama;
/*     */   private double speedModifier;
/*     */   private int distCheckCounter;
/*     */   
/*     */   public LlamaFollowCaravanGoal(Llama debug1, double debug2) {
/*  23 */     this.llama = debug1;
/*  24 */     this.speedModifier = debug2;
/*  25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  30 */     if (this.llama.isLeashed() || this.llama.inCaravan()) {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     List<Entity> debug1 = this.llama.level.getEntities((Entity)this.llama, this.llama.getBoundingBox().inflate(9.0D, 4.0D, 9.0D), debug0 -> {
/*     */           EntityType<?> debug1 = debug0.getType();
/*  36 */           return (debug1 == EntityType.LLAMA || debug1 == EntityType.TRADER_LLAMA);
/*     */         });
/*     */     
/*  39 */     Llama debug2 = null;
/*  40 */     double debug3 = Double.MAX_VALUE;
/*  41 */     for (Entity debug6 : debug1) {
/*  42 */       Llama debug7 = (Llama)debug6;
/*     */       
/*  44 */       if (!debug7.inCaravan() || debug7.hasCaravanTail()) {
/*     */         continue;
/*     */       }
/*     */       
/*  48 */       double debug8 = this.llama.distanceToSqr((Entity)debug7);
/*  49 */       if (debug8 > debug3) {
/*     */         continue;
/*     */       }
/*     */       
/*  53 */       debug3 = debug8;
/*  54 */       debug2 = debug7;
/*     */     } 
/*     */     
/*  57 */     if (debug2 == null)
/*     */     {
/*  59 */       for (Entity debug6 : debug1) {
/*  60 */         Llama debug7 = (Llama)debug6;
/*     */         
/*  62 */         if (!debug7.isLeashed()) {
/*     */           continue;
/*     */         }
/*     */         
/*  66 */         if (debug7.hasCaravanTail()) {
/*     */           continue;
/*     */         }
/*     */         
/*  70 */         double debug8 = this.llama.distanceToSqr((Entity)debug7);
/*  71 */         if (debug8 > debug3) {
/*     */           continue;
/*     */         }
/*     */         
/*  75 */         debug3 = debug8;
/*  76 */         debug2 = debug7;
/*     */       } 
/*     */     }
/*     */     
/*  80 */     if (debug2 == null) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (debug3 < 4.0D) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     if (!debug2.isLeashed() && !firstIsLeashed(debug2, 1)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     this.llama.joinCaravan(debug2);
/*     */     
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  98 */     if (!this.llama.inCaravan() || !this.llama.getCaravanHead().isAlive() || !firstIsLeashed(this.llama, 0)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     double debug1 = this.llama.distanceToSqr((Entity)this.llama.getCaravanHead());
/* 103 */     if (debug1 > 676.0D) {
/* 104 */       if (this.speedModifier <= 3.0D) {
/* 105 */         this.speedModifier *= 1.2D;
/* 106 */         this.distCheckCounter = 40;
/* 107 */         return true;
/*     */       } 
/*     */       
/* 110 */       if (this.distCheckCounter == 0) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/* 114 */     if (this.distCheckCounter > 0) {
/* 115 */       this.distCheckCounter--;
/*     */     }
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 122 */     this.llama.leaveCaravan();
/* 123 */     this.speedModifier = 2.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 128 */     if (!this.llama.inCaravan()) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (this.llama.getLeashHolder() instanceof net.minecraft.world.entity.decoration.LeashFenceKnotEntity) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     Llama debug1 = this.llama.getCaravanHead();
/* 137 */     double debug2 = this.llama.distanceTo((Entity)debug1);
/*     */     
/* 139 */     float debug4 = 2.0F;
/* 140 */     Vec3 debug5 = (new Vec3(debug1.getX() - this.llama.getX(), debug1.getY() - this.llama.getY(), debug1.getZ() - this.llama.getZ())).normalize().scale(Math.max(debug2 - 2.0D, 0.0D));
/* 141 */     this.llama.getNavigation().moveTo(this.llama.getX() + debug5.x, this.llama.getY() + debug5.y, this.llama.getZ() + debug5.z, this.speedModifier);
/*     */   }
/*     */   
/*     */   private boolean firstIsLeashed(Llama debug1, int debug2) {
/* 145 */     if (debug2 > 8) {
/* 146 */       return false;
/*     */     }
/*     */     
/* 149 */     if (debug1.inCaravan()) {
/* 150 */       if (debug1.getCaravanHead().isLeashed()) {
/* 151 */         return true;
/*     */       }
/* 153 */       return firstIsLeashed(debug1.getCaravanHead(), ++debug2);
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\LlamaFollowCaravanGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */