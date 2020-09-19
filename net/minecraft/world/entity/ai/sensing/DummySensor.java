/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DummySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {}
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 17 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\DummySensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */