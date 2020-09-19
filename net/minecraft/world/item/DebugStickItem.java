/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class DebugStickItem
/*     */   extends Item {
/*     */   public DebugStickItem(Item.Properties debug1) {
/*  27 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFoil(ItemStack debug1) {
/*  32 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackBlock(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/*  37 */     if (!debug2.isClientSide) {
/*  38 */       handleInteraction(debug4, debug1, (LevelAccessor)debug2, debug3, false, debug4.getItemInHand(InteractionHand.MAIN_HAND));
/*     */     }
/*     */     
/*  41 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  46 */     Player debug2 = debug1.getPlayer();
/*  47 */     Level debug3 = debug1.getLevel();
/*     */     
/*  49 */     if (!debug3.isClientSide && debug2 != null) {
/*  50 */       BlockPos debug4 = debug1.getClickedPos();
/*  51 */       handleInteraction(debug2, debug3.getBlockState(debug4), (LevelAccessor)debug3, debug4, true, debug1.getItemInHand());
/*     */     } 
/*     */     
/*  54 */     return InteractionResult.sidedSuccess(debug3.isClientSide);
/*     */   }
/*     */   
/*     */   private void handleInteraction(Player debug1, BlockState debug2, LevelAccessor debug3, BlockPos debug4, boolean debug5, ItemStack debug6) {
/*  58 */     if (!debug1.canUseGameMasterBlocks()) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     Block debug7 = debug2.getBlock();
/*  63 */     StateDefinition<Block, BlockState> debug8 = debug7.getStateDefinition();
/*  64 */     Collection<Property<?>> debug9 = debug8.getProperties();
/*     */     
/*  66 */     String debug10 = Registry.BLOCK.getKey(debug7).toString();
/*  67 */     if (debug9.isEmpty()) {
/*  68 */       message(debug1, (Component)new TranslatableComponent(getDescriptionId() + ".empty", new Object[] { debug10 }));
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     CompoundTag debug11 = debug6.getOrCreateTagElement("DebugProperty");
/*  73 */     String debug12 = debug11.getString(debug10);
/*     */     
/*  75 */     Property<?> debug13 = debug8.getProperty(debug12);
/*  76 */     if (debug5) {
/*  77 */       if (debug13 == null) {
/*  78 */         debug13 = debug9.iterator().next();
/*     */       }
/*     */       
/*  81 */       BlockState debug14 = cycleState(debug2, debug13, debug1.isSecondaryUseActive());
/*  82 */       debug3.setBlock(debug4, debug14, 18);
/*  83 */       message(debug1, (Component)new TranslatableComponent(getDescriptionId() + ".update", new Object[] { debug13.getName(), getNameHelper(debug14, debug13) }));
/*     */     } else {
/*  85 */       debug13 = getRelative((Iterable)debug9, debug13, debug1.isSecondaryUseActive());
/*     */       
/*  87 */       String debug14 = debug13.getName();
/*  88 */       debug11.putString(debug10, debug14);
/*  89 */       message(debug1, (Component)new TranslatableComponent(getDescriptionId() + ".select", new Object[] { debug14, getNameHelper(debug2, debug13) }));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> BlockState cycleState(BlockState debug0, Property<T> debug1, boolean debug2) {
/*  94 */     return (BlockState)debug0.setValue(debug1, getRelative(debug1.getPossibleValues(), debug0.getValue(debug1), debug2));
/*     */   }
/*     */   
/*     */   private static <T> T getRelative(Iterable<T> debug0, @Nullable T debug1, boolean debug2) {
/*  98 */     return debug2 ? (T)Util.findPreviousInIterable(debug0, debug1) : (T)Util.findNextInIterable(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static void message(Player debug0, Component debug1) {
/* 102 */     ((ServerPlayer)debug0).sendMessage(debug1, ChatType.GAME_INFO, Util.NIL_UUID);
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> String getNameHelper(BlockState debug0, Property<T> debug1) {
/* 106 */     return debug1.getName(debug0.getValue(debug1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DebugStickItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */