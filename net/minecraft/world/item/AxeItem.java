/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class AxeItem extends DiggerItem {
/* 22 */   private static final Set<Material> DIGGABLE_MATERIALS = Sets.newHashSet((Object[])new Material[] { Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   private static final Set<Block> OTHER_DIGGABLE_BLOCKS = Sets.newHashSet((Object[])new Block[] { Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON, Blocks.WARPED_BUTTON });
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
/* 44 */   protected static final Map<Block, Block> STRIPABLES = (Map<Block, Block>)(new ImmutableMap.Builder())
/* 45 */     .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
/* 46 */     .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
/* 47 */     .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
/* 48 */     .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
/* 49 */     .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
/* 50 */     .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
/* 51 */     .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
/* 52 */     .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
/* 53 */     .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
/* 54 */     .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
/* 55 */     .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
/* 56 */     .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
/* 57 */     .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
/* 58 */     .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
/* 59 */     .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
/* 60 */     .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
/* 61 */     .build();
/*    */   
/*    */   protected AxeItem(Tier debug1, float debug2, float debug3, Item.Properties debug4) {
/* 64 */     super(debug2, debug3, debug1, OTHER_DIGGABLE_BLOCKS, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 69 */     Material debug3 = debug2.getMaterial();
/* 70 */     if (DIGGABLE_MATERIALS.contains(debug3)) {
/* 71 */       return this.speed;
/*    */     }
/* 73 */     return super.getDestroySpeed(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 78 */     Level debug2 = debug1.getLevel();
/* 79 */     BlockPos debug3 = debug1.getClickedPos();
/* 80 */     BlockState debug4 = debug2.getBlockState(debug3);
/* 81 */     Block debug5 = STRIPABLES.get(debug4.getBlock());
/*    */     
/* 83 */     if (debug5 != null) {
/* 84 */       Player debug6 = debug1.getPlayer();
/* 85 */       debug2.playSound(debug6, debug3, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */       
/* 87 */       if (!debug2.isClientSide) {
/* 88 */         debug2.setBlock(debug3, (BlockState)debug5.defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, debug4.getValue((Property)RotatedPillarBlock.AXIS)), 11);
/*    */         
/* 90 */         if (debug6 != null) {
/* 91 */           debug1.getItemInHand().hurtAndBreak(1, debug6, debug1 -> debug1.broadcastBreakEvent(debug0.getHand()));
/*    */         }
/*    */       } 
/* 94 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */     } 
/*    */     
/* 97 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\AxeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */