/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class DoNothing
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   public DoNothing(int debug1, int debug2) {
/* 14 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 19 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\DoNothing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */