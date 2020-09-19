/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.ShulkerSharedHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShulkerBoxBlock
/*     */   extends BaseEntityBlock
/*     */ {
/*  50 */   public static final EnumProperty<Direction> FACING = (EnumProperty<Direction>)DirectionalBlock.FACING;
/*     */   
/*  52 */   public static final ResourceLocation CONTENTS = new ResourceLocation("contents");
/*     */   
/*     */   @Nullable
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlock(@Nullable DyeColor debug1, BlockBehaviour.Properties debug2) {
/*  58 */     super(debug2);
/*  59 */     this.color = debug1;
/*  60 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  65 */     return (BlockEntity)new ShulkerBoxBlockEntity(this.color);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  70 */     return RenderShape.ENTITYBLOCK_ANIMATED;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  75 */     if (debug2.isClientSide) {
/*  76 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  79 */     if (debug4.isSpectator()) {
/*  80 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  83 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  84 */     if (debug7 instanceof ShulkerBoxBlockEntity) {
/*  85 */       boolean debug9; ShulkerBoxBlockEntity debug8 = (ShulkerBoxBlockEntity)debug7;
/*     */       
/*  87 */       if (debug8.getAnimationStatus() == ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
/*  88 */         Direction debug10 = (Direction)debug1.getValue((Property)FACING);
/*  89 */         debug9 = debug2.noCollision(ShulkerSharedHelper.openBoundingBox(debug3, debug10));
/*     */       } else {
/*  91 */         debug9 = true;
/*     */       } 
/*  93 */       if (debug9) {
/*  94 */         debug4.openMenu((MenuProvider)debug8);
/*  95 */         debug4.awardStat(Stats.OPEN_SHULKER_BOX);
/*  96 */         PiglinAi.angerNearbyPiglins(debug4, true);
/*     */       } 
/*  98 */       return InteractionResult.CONSUME;
/*     */     } 
/* 100 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 105 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getClickedFace());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 110 */     debug1.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 115 */     BlockEntity debug5 = debug1.getBlockEntity(debug2);
/* 116 */     if (debug5 instanceof ShulkerBoxBlockEntity) {
/* 117 */       ShulkerBoxBlockEntity debug6 = (ShulkerBoxBlockEntity)debug5;
/* 118 */       if (!debug1.isClientSide && debug4.isCreative() && !debug6.isEmpty()) {
/*     */         
/* 120 */         ItemStack debug7 = getColoredItemStack(getColor());
/* 121 */         CompoundTag debug8 = debug6.saveToTag(new CompoundTag());
/* 122 */         if (!debug8.isEmpty()) {
/* 123 */           debug7.addTagElement("BlockEntityTag", (Tag)debug8);
/*     */         }
/*     */         
/* 126 */         if (debug6.hasCustomName()) {
/* 127 */           debug7.setHoverName(debug6.getCustomName());
/*     */         }
/*     */         
/* 130 */         ItemEntity debug9 = new ItemEntity(debug1, debug2.getX() + 0.5D, debug2.getY() + 0.5D, debug2.getZ() + 0.5D, debug7);
/* 131 */         debug9.setDefaultPickUpDelay();
/* 132 */         debug1.addFreshEntity((Entity)debug9);
/*     */       } else {
/* 134 */         debug6.unpackLootTable(debug4);
/*     */       } 
/*     */     } 
/* 137 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/* 142 */     BlockEntity debug3 = (BlockEntity)debug2.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/*     */     
/* 144 */     if (debug3 instanceof ShulkerBoxBlockEntity) {
/* 145 */       ShulkerBoxBlockEntity debug4 = (ShulkerBoxBlockEntity)debug3;
/* 146 */       debug2 = debug2.withDynamicDrop(CONTENTS, (debug1, debug2) -> {
/*     */             for (int debug3 = 0; debug3 < debug0.getContainerSize(); debug3++) {
/*     */               debug2.accept(debug0.getItem(debug3));
/*     */             }
/*     */           });
/*     */     } 
/* 152 */     return super.getDrops(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 157 */     if (debug5.hasCustomHoverName()) {
/* 158 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 159 */       if (debug6 instanceof ShulkerBoxBlockEntity) {
/* 160 */         ((ShulkerBoxBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 167 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 170 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/*     */     
/* 172 */     if (debug6 instanceof ShulkerBoxBlockEntity) {
/* 173 */       debug2.updateNeighbourForOutputSignal(debug3, debug1.getBlock());
/*     */     }
/*     */     
/* 176 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
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
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 217 */     return PushReaction.DESTROY;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 222 */     BlockEntity debug5 = debug2.getBlockEntity(debug3);
/* 223 */     if (debug5 instanceof ShulkerBoxBlockEntity) {
/* 224 */       return Shapes.create(((ShulkerBoxBlockEntity)debug5).getBoundingBox(debug1));
/*     */     }
/* 226 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 231 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 236 */     return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)debug2.getBlockEntity(debug3));
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
/*     */   public static Block getBlockByColor(@Nullable DyeColor debug0) {
/* 267 */     if (debug0 == null) {
/* 268 */       return Blocks.SHULKER_BOX;
/*     */     }
/* 270 */     switch (debug0)
/*     */     { case WHITE:
/* 272 */         return Blocks.WHITE_SHULKER_BOX;
/*     */       case ORANGE:
/* 274 */         return Blocks.ORANGE_SHULKER_BOX;
/*     */       case MAGENTA:
/* 276 */         return Blocks.MAGENTA_SHULKER_BOX;
/*     */       case LIGHT_BLUE:
/* 278 */         return Blocks.LIGHT_BLUE_SHULKER_BOX;
/*     */       case YELLOW:
/* 280 */         return Blocks.YELLOW_SHULKER_BOX;
/*     */       case LIME:
/* 282 */         return Blocks.LIME_SHULKER_BOX;
/*     */       case PINK:
/* 284 */         return Blocks.PINK_SHULKER_BOX;
/*     */       case GRAY:
/* 286 */         return Blocks.GRAY_SHULKER_BOX;
/*     */       case LIGHT_GRAY:
/* 288 */         return Blocks.LIGHT_GRAY_SHULKER_BOX;
/*     */       case CYAN:
/* 290 */         return Blocks.CYAN_SHULKER_BOX;
/*     */       
/*     */       default:
/* 293 */         return Blocks.PURPLE_SHULKER_BOX;
/*     */       case BLUE:
/* 295 */         return Blocks.BLUE_SHULKER_BOX;
/*     */       case BROWN:
/* 297 */         return Blocks.BROWN_SHULKER_BOX;
/*     */       case GREEN:
/* 299 */         return Blocks.GREEN_SHULKER_BOX;
/*     */       case RED:
/* 301 */         return Blocks.RED_SHULKER_BOX;
/*     */       case BLACK:
/* 303 */         break; }  return Blocks.BLACK_SHULKER_BOX;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DyeColor getColor() {
/* 309 */     return this.color;
/*     */   }
/*     */   
/*     */   public static ItemStack getColoredItemStack(@Nullable DyeColor debug0) {
/* 313 */     return new ItemStack(getBlockByColor(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 318 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 323 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ShulkerBoxBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */