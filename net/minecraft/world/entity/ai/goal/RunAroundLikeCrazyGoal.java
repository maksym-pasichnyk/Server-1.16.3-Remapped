/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RunAroundLikeCrazyGoal
/*    */   extends Goal {
/*    */   private final AbstractHorse horse;
/*    */   private final double speedModifier;
/*    */   private double posX;
/*    */   private double posY;
/*    */   private double posZ;
/*    */   
/*    */   public RunAroundLikeCrazyGoal(AbstractHorse debug1, double debug2) {
/* 20 */     this.horse = debug1;
/* 21 */     this.speedModifier = debug2;
/* 22 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     if (this.horse.isTamed() || !this.horse.isVehicle()) {
/* 28 */       return false;
/*    */     }
/* 30 */     Vec3 debug1 = RandomPos.getPos((PathfinderMob)this.horse, 5, 4);
/* 31 */     if (debug1 == null) {
/* 32 */       return false;
/*    */     }
/* 34 */     this.posX = debug1.x;
/* 35 */     this.posY = debug1.y;
/* 36 */     this.posZ = debug1.z;
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 42 */     this.horse.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 47 */     return (!this.horse.isTamed() && !this.horse.getNavigation().isDone() && this.horse.isVehicle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 52 */     if (!this.horse.isTamed() && this.horse.getRandom().nextInt(50) == 0) {
/* 53 */       Entity debug1 = this.horse.getPassengers().get(0);
/* 54 */       if (debug1 == null) {
/*    */         return;
/*    */       }
/*    */       
/* 58 */       if (debug1 instanceof Player) {
/* 59 */         int debug2 = this.horse.getTemper();
/* 60 */         int debug3 = this.horse.getMaxTemper();
/* 61 */         if (debug3 > 0 && this.horse.getRandom().nextInt(debug3) < debug2) {
/* 62 */           this.horse.tameWithName((Player)debug1);
/*    */           return;
/*    */         } 
/* 65 */         this.horse.modifyTemper(5);
/*    */       } 
/*    */       
/* 68 */       this.horse.ejectPassengers();
/* 69 */       this.horse.makeMad();
/* 70 */       this.horse.level.broadcastEntityEvent((Entity)this.horse, (byte)6);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RunAroundLikeCrazyGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */