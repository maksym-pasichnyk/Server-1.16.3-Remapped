/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HoeItem
/*    */   extends DiggerItem {
/* 22 */   private static final Set<Block> DIGGABLES = (Set<Block>)ImmutableSet.of(Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.TARGET, Blocks.SHROOMLIGHT, (Object[])new Block[] { Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES });
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
/* 39 */   protected static final Map<Block, BlockState> TILLABLES = Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND
/* 40 */         .defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND
/* 41 */         .defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND
/* 42 */         .defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT
/* 43 */         .defaultBlockState()));
/*    */ 
/*    */   
/*    */   protected HoeItem(Tier debug1, int debug2, float debug3, Item.Properties debug4) {
/* 47 */     super(debug2, debug3, debug1, DIGGABLES, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 52 */     Level debug2 = debug1.getLevel();
/* 53 */     BlockPos debug3 = debug1.getClickedPos();
/*    */     
/* 55 */     if (debug1.getClickedFace() != Direction.DOWN && debug2.getBlockState(debug3.above()).isAir()) {
/* 56 */       BlockState debug4 = TILLABLES.get(debug2.getBlockState(debug3).getBlock());
/*    */       
/* 58 */       if (debug4 != null) {
/* 59 */         Player debug5 = debug1.getPlayer();
/* 60 */         debug2.playSound(debug5, debug3, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */         
/* 62 */         if (!debug2.isClientSide) {
/* 63 */           debug2.setBlock(debug3, debug4, 11);
/* 64 */           if (debug5 != null) {
/* 65 */             debug1.getItemInHand().hurtAndBreak(1, debug5, debug1 -> debug1.broadcastBreakEvent(debug0.getHand()));
/*    */           }
/*    */         } 
/* 68 */         return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\HoeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */