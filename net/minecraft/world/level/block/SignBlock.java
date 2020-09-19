/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class SignBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/* 26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 28 */   protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
/*    */   private final WoodType type;
/*    */   
/*    */   protected SignBlock(BlockBehaviour.Properties debug1, WoodType debug2) {
/* 32 */     super(debug1);
/* 33 */     this.type = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 38 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 39 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 42 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 47 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPossibleToRespawnInThis() {
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 57 */     return (BlockEntity)new SignBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 62 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/* 63 */     boolean debug8 = (debug7.getItem() instanceof DyeItem && debug4.abilities.mayBuild);
/*    */     
/* 65 */     if (debug2.isClientSide) {
/* 66 */       return debug8 ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
/*    */     }
/*    */     
/* 69 */     BlockEntity debug9 = debug2.getBlockEntity(debug3);
/* 70 */     if (debug9 instanceof SignBlockEntity) {
/* 71 */       SignBlockEntity debug10 = (SignBlockEntity)debug9;
/* 72 */       if (debug8) {
/* 73 */         boolean debug11 = debug10.setColor(((DyeItem)debug7.getItem()).getDyeColor());
/* 74 */         if (debug11 && !debug4.isCreative()) {
/* 75 */           debug7.shrink(1);
/*    */         }
/*    */       } 
/*    */       
/* 79 */       return debug10.executeClickCommands(debug4) ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*    */     } 
/*    */     
/* 82 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 87 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 88 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 90 */     return super.getFluidState(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SignBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */