/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class ReactToBell extends Behavior<LivingEntity> {
/*    */   public ReactToBell() {
/* 14 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HEARD_BELL_TIME, MemoryStatus.VALUE_PRESENT));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 19 */     Brain<?> debug5 = debug2.getBrain();
/* 20 */     Raid debug6 = debug1.getRaidAt(debug2.blockPosition());
/*    */ 
/*    */     
/* 23 */     if (debug6 == null)
/* 24 */       debug5.setActiveActivityIfPossible(Activity.HIDE); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ReactToBell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */