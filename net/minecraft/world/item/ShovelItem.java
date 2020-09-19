/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ShovelItem extends DiggerItem {
/*  24 */   private static final Set<Block> DIGGABLES = Sets.newHashSet((Object[])new Block[] { Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.SOUL_SOIL });
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
/*  58 */   protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.GRASS_PATH
/*  59 */         .defaultBlockState()));
/*     */ 
/*     */   
/*     */   public ShovelItem(Tier debug1, float debug2, float debug3, Item.Properties debug4) {
/*  63 */     super(debug2, debug3, debug1, DIGGABLES, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCorrectToolForDrops(BlockState debug1) {
/*  68 */     return (debug1.is(Blocks.SNOW) || debug1.is(Blocks.SNOW_BLOCK));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  73 */     Level debug2 = debug1.getLevel();
/*  74 */     BlockPos debug3 = debug1.getClickedPos();
/*     */     
/*  76 */     BlockState debug4 = debug2.getBlockState(debug3);
/*  77 */     if (debug1.getClickedFace() != Direction.DOWN) {
/*  78 */       Player debug5 = debug1.getPlayer();
/*  79 */       BlockState debug6 = FLATTENABLES.get(debug4.getBlock());
/*  80 */       BlockState debug7 = null;
/*     */       
/*  82 */       if (debug6 != null && debug2.getBlockState(debug3.above()).isAir()) {
/*  83 */         debug2.playSound(debug5, debug3, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  84 */         debug7 = debug6;
/*  85 */       } else if (debug4.getBlock() instanceof CampfireBlock && ((Boolean)debug4.getValue((Property)CampfireBlock.LIT)).booleanValue()) {
/*  86 */         if (!debug2.isClientSide()) {
/*  87 */           debug2.levelEvent(null, 1009, debug3, 0);
/*     */         }
/*  89 */         CampfireBlock.dowse((LevelAccessor)debug2, debug3, debug4);
/*  90 */         debug7 = (BlockState)debug4.setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false));
/*     */       } 
/*     */       
/*  93 */       if (debug7 != null) {
/*  94 */         if (!debug2.isClientSide) {
/*  95 */           debug2.setBlock(debug3, debug7, 11);
/*  96 */           if (debug5 != null) {
/*  97 */             debug1.getItemInHand().hurtAndBreak(1, debug5, debug1 -> debug1.broadcastBreakEvent(debug0.getHand()));
/*     */           }
/*     */         } 
/* 100 */         return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */       } 
/* 102 */       return InteractionResult.PASS;
/*     */     } 
/*     */     
/* 105 */     return InteractionResult.PASS;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ShovelItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */