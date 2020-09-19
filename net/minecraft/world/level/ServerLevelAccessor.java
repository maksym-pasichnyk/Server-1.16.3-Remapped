/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public interface ServerLevelAccessor
/*    */   extends LevelAccessor {
/*    */   ServerLevel getLevel();
/*    */   
/*    */   default void addFreshEntityWithPassengers(Entity debug1) {
/* 11 */     debug1.getSelfAndPassengers().forEach(this::addFreshEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ServerLevelAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */