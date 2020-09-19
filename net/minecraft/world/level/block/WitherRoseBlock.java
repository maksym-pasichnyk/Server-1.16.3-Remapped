/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WitherRoseBlock
/*    */   extends FlowerBlock
/*    */ {
/*    */   public WitherRoseBlock(MobEffect debug1, BlockBehaviour.Properties debug2) {
/* 24 */     super(debug1, 8, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 29 */     return (super.mayPlaceOn(debug1, debug2, debug3) || debug1.is(Blocks.NETHERRACK) || debug1.is(Blocks.SOUL_SAND) || debug1.is(Blocks.SOUL_SOIL));
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 47 */     if (debug2.isClientSide || debug2.getDifficulty() == Difficulty.PEACEFUL) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     if (debug4 instanceof LivingEntity) {
/* 52 */       LivingEntity debug5 = (LivingEntity)debug4;
/* 53 */       if (!debug5.isInvulnerableTo(DamageSource.WITHER))
/* 54 */         debug5.addEffect(new MobEffectInstance(MobEffects.WITHER, 40)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WitherRoseBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */