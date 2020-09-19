/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class PressurePlateBlock extends BasePressurePlateBlock {
/*    */   private final Sensitivity sensitivity;
/* 21 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   
/*    */   public enum Sensitivity {
/* 24 */     EVERYTHING, MOBS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected PressurePlateBlock(Sensitivity debug1, BlockBehaviour.Properties debug2) {
/* 30 */     super(debug2);
/* 31 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false)));
/* 32 */     this.sensitivity = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalForState(BlockState debug1) {
/* 37 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState setSignalForState(BlockState debug1, int debug2) {
/* 42 */     return (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf((debug2 > 0)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playOnSound(LevelAccessor debug1, BlockPos debug2) {
/* 47 */     if (this.material == Material.WOOD || this.material == Material.NETHER_WOOD) {
/* 48 */       debug1.playSound(null, debug2, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.8F);
/*    */     } else {
/* 50 */       debug1.playSound(null, debug2, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playOffSound(LevelAccessor debug1, BlockPos debug2) {
/* 56 */     if (this.material == Material.WOOD || this.material == Material.NETHER_WOOD) {
/* 57 */       debug1.playSound(null, debug2, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.7F);
/*    */     } else {
/* 59 */       debug1.playSound(null, debug2, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.5F);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getSignalStrength(Level debug1, BlockPos debug2) {
/*    */     List<? extends Entity> debug4;
/* 65 */     AABB debug3 = TOUCH_AABB.move(debug2);
/*    */ 
/*    */     
/* 68 */     switch (this.sensitivity) {
/*    */       case EVERYTHING:
/* 70 */         debug4 = debug1.getEntities(null, debug3);
/*    */         break;
/*    */       case MOBS:
/* 73 */         debug4 = debug1.getEntitiesOfClass(LivingEntity.class, debug3);
/*    */         break;
/*    */       default:
/* 76 */         return 0;
/*    */     } 
/*    */     
/* 79 */     if (!debug4.isEmpty()) {
/* 80 */       for (Entity debug6 : debug4) {
/* 81 */         if (!debug6.isIgnoringBlockTriggers()) {
/* 82 */           return 15;
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 87 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 92 */     debug1.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PressurePlateBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */