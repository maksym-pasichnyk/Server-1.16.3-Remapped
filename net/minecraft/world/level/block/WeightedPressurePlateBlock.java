/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeightedPressurePlateBlock extends BasePressurePlateBlock {
/* 18 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*    */   
/*    */   private final int maxWeight;
/*    */   
/*    */   protected WeightedPressurePlateBlock(int debug1, BlockBehaviour.Properties debug2) {
/* 23 */     super(debug2);
/* 24 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0)));
/* 25 */     this.maxWeight = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalStrength(Level debug1, BlockPos debug2) {
/* 30 */     int debug3 = Math.min(debug1.getEntitiesOfClass(Entity.class, TOUCH_AABB.move(debug2)).size(), this.maxWeight);
/* 31 */     if (debug3 > 0) {
/* 32 */       float debug4 = Math.min(this.maxWeight, debug3) / this.maxWeight;
/* 33 */       return Mth.ceil(debug4 * 15.0F);
/*    */     } 
/*    */     
/* 36 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playOnSound(LevelAccessor debug1, BlockPos debug2) {
/* 41 */     debug1.playSound(null, debug2, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.90000004F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playOffSound(LevelAccessor debug1, BlockPos debug2) {
/* 46 */     debug1.playSound(null, debug2, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.75F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalForState(BlockState debug1) {
/* 51 */     return ((Integer)debug1.getValue((Property)POWER)).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState setSignalForState(BlockState debug1, int debug2) {
/* 56 */     return (BlockState)debug1.setValue((Property)POWER, Integer.valueOf(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getPressedTime() {
/* 61 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 66 */     debug1.add(new Property[] { (Property)POWER });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WeightedPressurePlateBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */