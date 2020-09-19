/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class RepeaterBlock
/*    */   extends DiodeBlock
/*    */ {
/* 24 */   public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
/* 25 */   public static final IntegerProperty DELAY = BlockStateProperties.DELAY;
/*    */   
/*    */   protected RepeaterBlock(BlockBehaviour.Properties debug1) {
/* 28 */     super(debug1);
/* 29 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)DELAY, Integer.valueOf(1))).setValue((Property)LOCKED, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 34 */     if (!debug4.abilities.mayBuild) {
/* 35 */       return InteractionResult.PASS;
/*    */     }
/*    */     
/* 38 */     debug2.setBlock(debug3, (BlockState)debug1.cycle((Property)DELAY), 3);
/* 39 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getDelay(BlockState debug1) {
/* 44 */     return ((Integer)debug1.getValue((Property)DELAY)).intValue() * 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 49 */     BlockState debug2 = super.getStateForPlacement(debug1);
/* 50 */     return (BlockState)debug2.setValue((Property)LOCKED, Boolean.valueOf(isLocked((LevelReader)debug1.getLevel(), debug1.getClickedPos(), debug2)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 56 */     if (!debug4.isClientSide() && debug2.getAxis() != ((Direction)debug1.getValue((Property)FACING)).getAxis()) {
/* 57 */       return (BlockState)debug1.setValue((Property)LOCKED, Boolean.valueOf(isLocked((LevelReader)debug4, debug5, debug1)));
/*    */     }
/* 59 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLocked(LevelReader debug1, BlockPos debug2, BlockState debug3) {
/* 64 */     return (getAlternateSignal(debug1, debug2, debug3) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isAlternateInput(BlockState debug1) {
/* 69 */     return isDiode(debug1);
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
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 97 */     debug1.add(new Property[] { (Property)FACING, (Property)DELAY, (Property)LOCKED, (Property)POWERED });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RepeaterBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */