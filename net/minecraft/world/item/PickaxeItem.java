/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class PickaxeItem
/*     */   extends DiggerItem {
/*  12 */   private static final Set<Block> DIGGABLES = (Set<Block>)ImmutableSet.of(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, (Object[])new Block[] { Blocks.POWERED_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_HEAD });
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
/*     */   protected PickaxeItem(Tier debug1, int debug2, float debug3, Item.Properties debug4) {
/* 100 */     super(debug2, debug3, debug1, DIGGABLES, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCorrectToolForDrops(BlockState debug1) {
/* 105 */     int debug2 = getTier().getLevel();
/* 106 */     if (debug1.is(Blocks.OBSIDIAN) || debug1
/* 107 */       .is(Blocks.CRYING_OBSIDIAN) || debug1
/* 108 */       .is(Blocks.NETHERITE_BLOCK) || debug1
/* 109 */       .is(Blocks.RESPAWN_ANCHOR) || debug1
/* 110 */       .is(Blocks.ANCIENT_DEBRIS))
/*     */     {
/* 112 */       return (debug2 >= 3);
/*     */     }
/* 114 */     if (debug1.is(Blocks.DIAMOND_BLOCK) || debug1
/* 115 */       .is(Blocks.DIAMOND_ORE) || debug1
/* 116 */       .is(Blocks.EMERALD_ORE) || debug1
/* 117 */       .is(Blocks.EMERALD_BLOCK) || debug1
/* 118 */       .is(Blocks.GOLD_BLOCK) || debug1
/* 119 */       .is(Blocks.GOLD_ORE) || debug1
/* 120 */       .is(Blocks.REDSTONE_ORE))
/*     */     {
/* 122 */       return (debug2 >= 2);
/*     */     }
/* 124 */     if (debug1.is(Blocks.IRON_BLOCK) || debug1
/* 125 */       .is(Blocks.IRON_ORE) || debug1
/* 126 */       .is(Blocks.LAPIS_BLOCK) || debug1
/* 127 */       .is(Blocks.LAPIS_ORE))
/*     */     {
/* 129 */       return (debug2 >= 1);
/*     */     }
/*     */     
/* 132 */     Material debug3 = debug1.getMaterial();
/* 133 */     return (debug3 == Material.STONE || debug3 == Material.METAL || debug3 == Material.HEAVY_METAL || debug1
/*     */ 
/*     */       
/* 136 */       .is(Blocks.NETHER_GOLD_ORE));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 141 */     Material debug3 = debug2.getMaterial();
/* 142 */     if (debug3 == Material.METAL || debug3 == Material.HEAVY_METAL || debug3 == Material.STONE) {
/* 143 */       return this.speed;
/*     */     }
/* 145 */     return super.getDestroySpeed(debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\PickaxeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */