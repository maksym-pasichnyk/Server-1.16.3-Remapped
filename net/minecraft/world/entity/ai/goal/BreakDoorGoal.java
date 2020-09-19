/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BreakDoorGoal
/*    */   extends DoorInteractGoal
/*    */ {
/*    */   private final Predicate<Difficulty> validDifficulties;
/*    */   protected int breakTime;
/* 17 */   protected int lastBreakProgress = -1;
/* 18 */   protected int doorBreakTime = -1;
/*    */   
/*    */   public BreakDoorGoal(Mob debug1, Predicate<Difficulty> debug2) {
/* 21 */     super(debug1);
/* 22 */     this.validDifficulties = debug2;
/*    */   }
/*    */   
/*    */   public BreakDoorGoal(Mob debug1, int debug2, Predicate<Difficulty> debug3) {
/* 26 */     this(debug1, debug3);
/* 27 */     this.doorBreakTime = debug2;
/*    */   }
/*    */   
/*    */   protected int getDoorBreakTime() {
/* 31 */     return Math.max(240, this.doorBreakTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 36 */     if (!super.canUse()) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 40 */       return false;
/*    */     }
/* 42 */     return (isValidDifficulty(this.mob.level.getDifficulty()) && !isOpen());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 47 */     super.start();
/* 48 */     this.breakTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 53 */     return (this.breakTime <= getDoorBreakTime() && !isOpen() && this.doorPos.closerThan((Position)this.mob.position(), 2.0D) && isValidDifficulty(this.mob.level.getDifficulty()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     super.stop();
/* 59 */     this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 64 */     super.tick();
/* 65 */     if (this.mob.getRandom().nextInt(20) == 0) {
/* 66 */       this.mob.level.levelEvent(1019, this.doorPos, 0);
/* 67 */       if (!this.mob.swinging) {
/* 68 */         this.mob.swing(this.mob.getUsedItemHand());
/*    */       }
/*    */     } 
/*    */     
/* 72 */     this.breakTime++;
/*    */     
/* 74 */     int debug1 = (int)(this.breakTime / getDoorBreakTime() * 10.0F);
/* 75 */     if (debug1 != this.lastBreakProgress) {
/* 76 */       this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, debug1);
/* 77 */       this.lastBreakProgress = debug1;
/*    */     } 
/*    */     
/* 80 */     if (this.breakTime == getDoorBreakTime() && isValidDifficulty(this.mob.level.getDifficulty())) {
/* 81 */       this.mob.level.removeBlock(this.doorPos, false);
/* 82 */       this.mob.level.levelEvent(1021, this.doorPos, 0);
/* 83 */       this.mob.level.levelEvent(2001, this.doorPos, Block.getId(this.mob.level.getBlockState(this.doorPos)));
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isValidDifficulty(Difficulty debug1) {
/* 88 */     return this.validDifficulties.test(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreakDoorGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */