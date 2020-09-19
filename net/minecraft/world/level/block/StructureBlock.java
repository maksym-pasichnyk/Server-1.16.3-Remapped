/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class StructureBlock extends BaseEntityBlock {
/*  25 */   public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
/*     */   
/*     */   protected StructureBlock(BlockBehaviour.Properties debug1) {
/*  28 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  33 */     return (BlockEntity)new StructureBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  38 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  39 */     if (debug7 instanceof StructureBlockEntity) {
/*  40 */       return ((StructureBlockEntity)debug7).usedBy(debug4) ? InteractionResult.sidedSuccess(debug2.isClientSide) : InteractionResult.PASS;
/*     */     }
/*     */     
/*  43 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/*  48 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*  51 */     if (debug4 != null) {
/*  52 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*  53 */       if (debug6 instanceof StructureBlockEntity) {
/*  54 */         ((StructureBlockEntity)debug6).createdBy(debug4);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  61 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  66 */     return (BlockState)defaultBlockState().setValue((Property)MODE, (Comparable)StructureMode.DATA);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  71 */     debug1.add(new Property[] { (Property)MODE });
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  76 */     if (!(debug2 instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  81 */     if (!(debug7 instanceof StructureBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     StructureBlockEntity debug8 = (StructureBlockEntity)debug7;
/*     */     
/*  87 */     boolean debug9 = debug2.hasNeighborSignal(debug3);
/*  88 */     boolean debug10 = debug8.isPowered();
/*     */     
/*  90 */     if (debug9 && !debug10) {
/*  91 */       debug8.setPowered(true);
/*  92 */       trigger((ServerLevel)debug2, debug8);
/*  93 */     } else if (!debug9 && debug10) {
/*  94 */       debug8.setPowered(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void trigger(ServerLevel debug1, StructureBlockEntity debug2) {
/*  99 */     switch (debug2.getMode()) {
/*     */       case SAVE:
/* 101 */         debug2.saveStructure(false);
/*     */         break;
/*     */       case LOAD:
/* 104 */         debug2.loadStructure(debug1, false);
/*     */         break;
/*     */       case CORNER:
/* 107 */         debug2.unloadStructure();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StructureBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */