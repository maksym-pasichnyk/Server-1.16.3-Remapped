/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestItemSensor
/*    */   extends Sensor<Mob>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, Mob debug2) {
/* 28 */     Brain<?> debug3 = debug2.getBrain();
/*    */     
/* 30 */     List<ItemEntity> debug4 = debug1.getEntitiesOfClass(ItemEntity.class, debug2.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), debug0 -> true);
/* 31 */     debug4.sort(Comparator.comparingDouble(debug2::distanceToSqr));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     Optional<ItemEntity> debug5 = debug4.stream().filter(debug1 -> debug0.wantsToPickUp(debug1.getItem())).filter(debug1 -> debug1.closerThan((Entity)debug0, 9.0D)).filter(debug2::canSee).findFirst();
/* 38 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestItemSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */