/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.RecordItem;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JukeboxBlock extends BaseEntityBlock {
/*  29 */   public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;
/*     */   
/*     */   protected JukeboxBlock(BlockBehaviour.Properties debug1) {
/*  32 */     super(debug1);
/*  33 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_RECORD, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/*  38 */     super.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*  39 */     CompoundTag debug6 = debug5.getOrCreateTag();
/*  40 */     if (debug6.contains("BlockEntityTag")) {
/*  41 */       CompoundTag debug7 = debug6.getCompound("BlockEntityTag");
/*  42 */       if (debug7.contains("RecordItem")) {
/*  43 */         debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)HAS_RECORD, Boolean.valueOf(true)), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  50 */     if (((Boolean)debug1.getValue((Property)HAS_RECORD)).booleanValue()) {
/*  51 */       dropRecording(debug2, debug3);
/*     */       
/*  53 */       debug1 = (BlockState)debug1.setValue((Property)HAS_RECORD, Boolean.valueOf(false));
/*  54 */       debug2.setBlock(debug3, debug1, 2);
/*     */       
/*  56 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/*  59 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public void setRecord(LevelAccessor debug1, BlockPos debug2, BlockState debug3, ItemStack debug4) {
/*  63 */     BlockEntity debug5 = debug1.getBlockEntity(debug2);
/*  64 */     if (!(debug5 instanceof JukeboxBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     ((JukeboxBlockEntity)debug5).setRecord(debug4.copy());
/*  69 */     debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)HAS_RECORD, Boolean.valueOf(true)), 2);
/*     */   }
/*     */   
/*     */   private void dropRecording(Level debug1, BlockPos debug2) {
/*  73 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     BlockEntity debug3 = debug1.getBlockEntity(debug2);
/*  78 */     if (!(debug3 instanceof JukeboxBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     JukeboxBlockEntity debug4 = (JukeboxBlockEntity)debug3;
/*  83 */     ItemStack debug5 = debug4.getRecord();
/*  84 */     if (debug5.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     debug1.levelEvent(1010, debug2, 0);
/*  89 */     debug4.clearContent();
/*     */     
/*  91 */     float debug6 = 0.7F;
/*  92 */     double debug7 = (debug1.random.nextFloat() * 0.7F) + 0.15000000596046448D;
/*  93 */     double debug9 = (debug1.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
/*  94 */     double debug11 = (debug1.random.nextFloat() * 0.7F) + 0.15000000596046448D;
/*     */     
/*  96 */     ItemStack debug13 = debug5.copy();
/*     */     
/*  98 */     ItemEntity debug14 = new ItemEntity(debug1, debug2.getX() + debug7, debug2.getY() + debug9, debug2.getZ() + debug11, debug13);
/*  99 */     debug14.setDefaultPickUpDelay();
/* 100 */     debug1.addFreshEntity((Entity)debug14);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 105 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 108 */     dropRecording(debug2, debug3);
/* 109 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 114 */     return (BlockEntity)new JukeboxBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 124 */     BlockEntity debug4 = debug2.getBlockEntity(debug3);
/* 125 */     if (debug4 instanceof JukeboxBlockEntity) {
/* 126 */       Item debug5 = ((JukeboxBlockEntity)debug4).getRecord().getItem();
/* 127 */       if (debug5 instanceof RecordItem) {
/* 128 */         return ((RecordItem)debug5).getAnalogOutput();
/*     */       }
/*     */     } 
/*     */     
/* 132 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 137 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 142 */     debug1.add(new Property[] { (Property)HAS_RECORD });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\JukeboxBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */