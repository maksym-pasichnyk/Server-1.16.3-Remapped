/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class Swim extends Behavior<Mob> {
/*    */   public Swim(float debug1) {
/* 15 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/* 16 */     this.chance = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Mob debug2) {
/* 21 */     return ((debug2.isInWater() && debug2.getFluidHeight((Tag)FluidTags.WATER) > debug2.getFluidJumpThreshold()) || debug2.isInLava());
/*    */   }
/*    */   private final float chance;
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Mob debug2, long debug3) {
/* 26 */     return checkExtraStartConditions(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Mob debug2, long debug3) {
/* 31 */     if (debug2.getRandom().nextFloat() < this.chance)
/* 32 */       debug2.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Swim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */