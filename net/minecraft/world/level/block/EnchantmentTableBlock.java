/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.EnchantmentMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnchantmentTableBlock extends BaseEntityBlock {
/*  30 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
/*     */   
/*     */   protected EnchantmentTableBlock(BlockBehaviour.Properties debug1) {
/*  33 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  38 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  43 */     return SHAPE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  74 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  79 */     return (BlockEntity)new EnchantmentTableBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  84 */     if (debug2.isClientSide) {
/*  85 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  88 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/*  89 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/*  95 */     BlockEntity debug4 = debug2.getBlockEntity(debug3);
/*  96 */     if (debug4 instanceof EnchantmentTableBlockEntity) {
/*  97 */       Component debug5 = ((Nameable)debug4).getDisplayName();
/*     */       
/*  99 */       return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new EnchantmentMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), debug5);
/*     */     } 
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 106 */     if (debug5.hasCustomHoverName()) {
/* 107 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 108 */       if (debug6 instanceof EnchantmentTableBlockEntity) {
/* 109 */         ((EnchantmentTableBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EnchantmentTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */