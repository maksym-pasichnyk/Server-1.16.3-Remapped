/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ 
/*     */ public class BlockItem
/*     */   extends Item
/*     */ {
/*     */   @Deprecated
/*     */   private final Block block;
/*     */   
/*     */   public BlockItem(Block debug1, Item.Properties debug2) {
/*  37 */     super(debug2);
/*  38 */     this.block = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  43 */     InteractionResult debug2 = place(new BlockPlaceContext(debug1));
/*     */     
/*  45 */     if (!debug2.consumesAction() && 
/*  46 */       isEdible()) {
/*  47 */       return use(debug1.getLevel(), debug1.getPlayer(), debug1.getHand()).getResult();
/*     */     }
/*     */     
/*  50 */     return debug2;
/*     */   }
/*     */   
/*     */   public InteractionResult place(BlockPlaceContext debug1) {
/*  54 */     if (!debug1.canPlace()) {
/*  55 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  58 */     BlockPlaceContext debug2 = updatePlacementContext(debug1);
/*  59 */     if (debug2 == null) {
/*  60 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  63 */     BlockState debug3 = getPlacementState(debug2);
/*  64 */     if (debug3 == null) {
/*  65 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  68 */     if (!placeBlock(debug2, debug3)) {
/*  69 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  72 */     BlockPos debug4 = debug2.getClickedPos();
/*  73 */     Level debug5 = debug2.getLevel();
/*  74 */     Player debug6 = debug2.getPlayer();
/*  75 */     ItemStack debug7 = debug2.getItemInHand();
/*     */ 
/*     */     
/*  78 */     BlockState debug8 = debug5.getBlockState(debug4);
/*  79 */     Block debug9 = debug8.getBlock();
/*  80 */     if (debug9 == debug3.getBlock()) {
/*  81 */       debug8 = updateBlockStateFromTag(debug4, debug5, debug7, debug8);
/*  82 */       updateCustomBlockEntityTag(debug4, debug5, debug6, debug7, debug8);
/*  83 */       debug9.setPlacedBy(debug5, debug4, debug8, (LivingEntity)debug6, debug7);
/*  84 */       if (debug6 instanceof ServerPlayer) {
/*  85 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)debug6, debug4, debug7);
/*     */       }
/*     */     } 
/*  88 */     SoundType debug10 = debug8.getSoundType();
/*  89 */     debug5.playSound(debug6, debug4, getPlaceSound(debug8), SoundSource.BLOCKS, (debug10.getVolume() + 1.0F) / 2.0F, debug10.getPitch() * 0.8F);
/*  90 */     if (debug6 == null || !debug6.abilities.instabuild) {
/*  91 */       debug7.shrink(1);
/*     */     }
/*  93 */     return InteractionResult.sidedSuccess(debug5.isClientSide);
/*     */   }
/*     */   
/*     */   protected SoundEvent getPlaceSound(BlockState debug1) {
/*  97 */     return debug1.getSoundType().getPlaceSound();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext debug1) {
/* 102 */     return debug1;
/*     */   }
/*     */   
/*     */   protected boolean updateCustomBlockEntityTag(BlockPos debug1, Level debug2, @Nullable Player debug3, ItemStack debug4, BlockState debug5) {
/* 106 */     return updateCustomBlockEntityTag(debug2, debug3, debug1, debug4);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected BlockState getPlacementState(BlockPlaceContext debug1) {
/* 111 */     BlockState debug2 = getBlock().getStateForPlacement(debug1);
/* 112 */     return (debug2 != null && canPlace(debug1, debug2)) ? debug2 : null;
/*     */   }
/*     */   
/*     */   private BlockState updateBlockStateFromTag(BlockPos debug1, Level debug2, ItemStack debug3, BlockState debug4) {
/* 116 */     BlockState debug5 = debug4;
/* 117 */     CompoundTag debug6 = debug3.getTag();
/* 118 */     if (debug6 != null) {
/* 119 */       CompoundTag debug7 = debug6.getCompound("BlockStateTag");
/* 120 */       StateDefinition<Block, BlockState> debug8 = debug5.getBlock().getStateDefinition();
/* 121 */       for (String debug10 : debug7.getAllKeys()) {
/* 122 */         Property<?> debug11 = debug8.getProperty(debug10);
/* 123 */         if (debug11 != null) {
/* 124 */           String debug12 = debug7.get(debug10).getAsString();
/* 125 */           debug5 = updateState(debug5, debug11, debug12);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     if (debug5 != debug4) {
/* 131 */       debug2.setBlock(debug1, debug5, 2);
/*     */     }
/* 133 */     return debug5;
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> BlockState updateState(BlockState debug0, Property<T> debug1, String debug2) {
/* 137 */     return debug1.getValue(debug2).map(debug2 -> (BlockState)debug0.setValue(debug1, debug2)).orElse(debug0);
/*     */   }
/*     */   
/*     */   protected boolean canPlace(BlockPlaceContext debug1, BlockState debug2) {
/* 141 */     Player debug3 = debug1.getPlayer();
/* 142 */     CollisionContext debug4 = (debug3 == null) ? CollisionContext.empty() : CollisionContext.of((Entity)debug3);
/* 143 */     return ((!mustSurvive() || debug2.canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) && debug1.getLevel().isUnobstructed(debug2, debug1.getClickedPos(), debug4));
/*     */   }
/*     */   
/*     */   protected boolean mustSurvive() {
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean placeBlock(BlockPlaceContext debug1, BlockState debug2) {
/* 151 */     return debug1.getLevel().setBlock(debug1.getClickedPos(), debug2, 11);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean updateCustomBlockEntityTag(Level debug0, @Nullable Player debug1, BlockPos debug2, ItemStack debug3) {
/* 156 */     MinecraftServer debug4 = debug0.getServer();
/* 157 */     if (debug4 == null) {
/* 158 */       return false;
/*     */     }
/*     */     
/* 161 */     CompoundTag debug5 = debug3.getTagElement("BlockEntityTag");
/* 162 */     if (debug5 != null) {
/* 163 */       BlockEntity debug6 = debug0.getBlockEntity(debug2);
/*     */       
/* 165 */       if (debug6 != null) {
/* 166 */         if (!debug0.isClientSide && debug6.onlyOpCanSetNbt() && (debug1 == null || !debug1.canUseGameMasterBlocks())) {
/* 167 */           return false;
/*     */         }
/* 169 */         CompoundTag debug7 = debug6.save(new CompoundTag());
/* 170 */         CompoundTag debug8 = debug7.copy();
/*     */         
/* 172 */         debug7.merge(debug5);
/* 173 */         debug7.putInt("x", debug2.getX());
/* 174 */         debug7.putInt("y", debug2.getY());
/* 175 */         debug7.putInt("z", debug2.getZ());
/*     */         
/* 177 */         if (!debug7.equals(debug8)) {
/* 178 */           debug6.load(debug0.getBlockState(debug2), debug7);
/* 179 */           debug6.setChanged();
/* 180 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/* 189 */     return getBlock().getDescriptionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 194 */     if (allowdedIn(debug1)) {
/* 195 */       getBlock().fillItemCategory(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Block getBlock() {
/* 206 */     return this.block;
/*     */   }
/*     */   
/*     */   public void registerBlocks(Map<Block, Item> debug1, Item debug2) {
/* 210 */     debug1.put(getBlock(), debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */