/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EatBlockGoal
/*    */   extends Goal
/*    */ {
/* 21 */   private static final Predicate<BlockState> IS_TALL_GRASS = (Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.GRASS);
/*    */   
/*    */   private final Mob mob;
/*    */   private final Level level;
/*    */   private int eatAnimationTick;
/*    */   
/*    */   public EatBlockGoal(Mob debug1) {
/* 28 */     this.mob = debug1;
/* 29 */     this.level = debug1.level;
/* 30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 35 */     if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     BlockPos debug1 = this.mob.blockPosition();
/* 40 */     if (IS_TALL_GRASS.test(this.level.getBlockState(debug1))) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (this.level.getBlockState(debug1.below()).is(Blocks.GRASS_BLOCK)) {
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 51 */     this.eatAnimationTick = 40;
/* 52 */     this.level.broadcastEntityEvent((Entity)this.mob, (byte)10);
/* 53 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.eatAnimationTick = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 63 */     return (this.eatAnimationTick > 0);
/*    */   }
/*    */   
/*    */   public int getEatAnimationTick() {
/* 67 */     return this.eatAnimationTick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 72 */     this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
/* 73 */     if (this.eatAnimationTick != 4) {
/*    */       return;
/*    */     }
/*    */     
/* 77 */     BlockPos debug1 = this.mob.blockPosition();
/*    */     
/* 79 */     if (IS_TALL_GRASS.test(this.level.getBlockState(debug1))) {
/* 80 */       if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 81 */         this.level.destroyBlock(debug1, false);
/*    */       }
/* 83 */       this.mob.ate();
/*    */     } else {
/* 85 */       BlockPos debug2 = debug1.below();
/* 86 */       if (this.level.getBlockState(debug2).is(Blocks.GRASS_BLOCK)) {
/* 87 */         if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 88 */           this.level.levelEvent(2001, debug2, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
/* 89 */           this.level.setBlock(debug2, Blocks.DIRT.defaultBlockState(), 2);
/*    */         } 
/* 91 */         this.mob.ate();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\EatBlockGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */