/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityTracker
/*    */   implements PositionTracker {
/*    */   private final Entity entity;
/*    */   private final boolean trackEyeHeight;
/*    */   
/*    */   public EntityTracker(Entity debug1, boolean debug2) {
/* 17 */     this.entity = debug1;
/* 18 */     this.trackEyeHeight = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 currentPosition() {
/* 23 */     return this.trackEyeHeight ? this.entity.position().add(0.0D, this.entity.getEyeHeight(), 0.0D) : this.entity.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 28 */     return this.entity.blockPosition();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity debug1) {
/* 33 */     if (this.entity instanceof LivingEntity) {
/* 34 */       Optional<List<LivingEntity>> debug2 = debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
/* 35 */       return (this.entity.isAlive() && debug2.isPresent() && ((List)debug2.get()).contains(this.entity));
/*    */     } 
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "EntityTracker for " + this.entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */