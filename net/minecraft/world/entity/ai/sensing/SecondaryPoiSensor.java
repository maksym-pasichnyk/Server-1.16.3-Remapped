/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SecondaryPoiSensor
/*    */   extends Sensor<Villager>
/*    */ {
/*    */   public SecondaryPoiSensor() {
/* 21 */     super(40);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, Villager debug2) {
/* 26 */     ResourceKey<Level> debug3 = debug1.dimension();
/* 27 */     BlockPos debug4 = debug2.blockPosition();
/* 28 */     List<GlobalPos> debug5 = Lists.newArrayList();
/*    */     
/* 30 */     int debug6 = 4;
/* 31 */     for (int i = -4; i <= 4; i++) {
/* 32 */       for (int debug8 = -2; debug8 <= 2; debug8++) {
/* 33 */         for (int debug9 = -4; debug9 <= 4; debug9++) {
/* 34 */           BlockPos debug10 = debug4.offset(i, debug8, debug9);
/* 35 */           if (debug2.getVillagerData().getProfession().getSecondaryPoi().contains(debug1.getBlockState(debug10).getBlock())) {
/* 36 */             debug5.add(GlobalPos.of(debug3, debug10));
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     Brain<?> debug7 = debug2.getBrain();
/* 43 */     if (!debug5.isEmpty()) {
/* 44 */       debug7.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, debug5);
/*    */     } else {
/* 46 */       debug7.eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 52 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\SecondaryPoiSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */