/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.FrontAndTop;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class JigsawBlock extends Block implements EntityBlock {
/* 24 */   public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*    */   
/*    */   protected JigsawBlock(BlockBehaviour.Properties debug1) {
/* 27 */     super(debug1);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 33 */     debug1.add(new Property[] { (Property)ORIENTATION });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 38 */     return (BlockState)debug1.setValue((Property)ORIENTATION, (Comparable)debug2.rotation().rotate((FrontAndTop)debug1.getValue((Property)ORIENTATION)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 43 */     return (BlockState)debug1.setValue((Property)ORIENTATION, (Comparable)debug2.rotation().rotate((FrontAndTop)debug1.getValue((Property)ORIENTATION)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 48 */     Direction debug3, debug2 = debug1.getClickedFace();
/*    */     
/* 50 */     if (debug2.getAxis() == Direction.Axis.Y) {
/* 51 */       debug3 = debug1.getHorizontalDirection().getOpposite();
/*    */     } else {
/* 53 */       debug3 = Direction.UP;
/*    */     } 
/*    */     
/* 56 */     return (BlockState)defaultBlockState().setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 62 */     return (BlockEntity)new JigsawBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 67 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/* 68 */     if (debug7 instanceof JigsawBlockEntity && debug4.canUseGameMasterBlocks()) {
/* 69 */       debug4.openJigsawBlock((JigsawBlockEntity)debug7);
/* 70 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */     } 
/*    */ 
/*    */     
/* 74 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static boolean canAttach(StructureTemplate.StructureBlockInfo debug0, StructureTemplate.StructureBlockInfo debug1) {
/* 78 */     Direction debug2 = getFrontFacing(debug0.state);
/* 79 */     Direction debug3 = getFrontFacing(debug1.state);
/* 80 */     Direction debug4 = getTopFacing(debug0.state);
/* 81 */     Direction debug5 = getTopFacing(debug1.state);
/*    */ 
/*    */ 
/*    */     
/* 85 */     JigsawBlockEntity.JointType debug6 = JigsawBlockEntity.JointType.byName(debug0.nbt.getString("joint")).orElseGet(() -> debug0.getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE);
/* 86 */     boolean debug7 = (debug6 == JigsawBlockEntity.JointType.ROLLABLE);
/*    */     
/* 88 */     return (debug2 == debug3.getOpposite() && (debug7 || debug4 == debug5) && debug0.nbt
/*    */       
/* 90 */       .getString("target").equals(debug1.nbt.getString("name")));
/*    */   }
/*    */   
/*    */   public static Direction getFrontFacing(BlockState debug0) {
/* 94 */     return ((FrontAndTop)debug0.getValue((Property)ORIENTATION)).front();
/*    */   }
/*    */   
/*    */   public static Direction getTopFacing(BlockState debug0) {
/* 98 */     return ((FrontAndTop)debug0.getValue((Property)ORIENTATION)).top();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\JigsawBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */