/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class PlayerSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 33 */     List<Player> debug3 = (List<Player>)debug1.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(debug1 -> debug0.closerThan((Entity)debug1, 16.0D)).sorted(Comparator.comparingDouble(debug2::distanceToSqr)).collect(Collectors.toList());
/*    */     
/* 35 */     Brain<?> debug4 = debug2.getBrain();
/* 36 */     debug4.setMemory(MemoryModuleType.NEAREST_PLAYERS, debug3);
/*    */ 
/*    */     
/* 39 */     List<Player> debug5 = (List<Player>)debug3.stream().filter(debug1 -> isEntityTargetable(debug0, (LivingEntity)debug1)).collect(Collectors.toList());
/* 40 */     debug4.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, debug5.isEmpty() ? null : debug5.get(0));
/* 41 */     Optional<Player> debug6 = debug5.stream().filter(EntitySelector.ATTACK_ALLOWED).findFirst();
/* 42 */     debug4.setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, debug6);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\PlayerSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */