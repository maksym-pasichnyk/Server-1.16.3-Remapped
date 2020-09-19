/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class InfestedBlock extends Block {
/*    */   private final Block hostBlock;
/* 21 */   private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();
/*    */   
/*    */   public InfestedBlock(Block debug1, BlockBehaviour.Properties debug2) {
/* 24 */     super(debug2);
/* 25 */     this.hostBlock = debug1;
/* 26 */     BLOCK_BY_HOST_BLOCK.put(debug1, this);
/*    */   }
/*    */   
/*    */   public Block getHostBlock() {
/* 30 */     return this.hostBlock;
/*    */   }
/*    */   
/*    */   public static boolean isCompatibleHostBlock(BlockState debug0) {
/* 34 */     return BLOCK_BY_HOST_BLOCK.containsKey(debug0.getBlock());
/*    */   }
/*    */   
/*    */   private void spawnInfestation(ServerLevel debug1, BlockPos debug2) {
/* 38 */     Silverfish debug3 = (Silverfish)EntityType.SILVERFISH.create((Level)debug1);
/* 39 */     debug3.moveTo(debug2.getX() + 0.5D, debug2.getY(), debug2.getZ() + 0.5D, 0.0F, 0.0F);
/* 40 */     debug1.addFreshEntity((Entity)debug3);
/*    */     
/* 42 */     debug3.spawnAnim();
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAfterBreak(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {
/* 47 */     super.spawnAfterBreak(debug1, debug2, debug3, debug4);
/*    */     
/* 49 */     if (debug2.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && 
/* 50 */       EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, debug4) == 0) {
/* 51 */       spawnInfestation(debug2, debug3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void wasExploded(Level debug1, BlockPos debug2, Explosion debug3) {
/* 57 */     if (debug1 instanceof ServerLevel) {
/* 58 */       spawnInfestation((ServerLevel)debug1, debug2);
/*    */     }
/*    */   }
/*    */   
/*    */   public static BlockState stateByHostBlock(Block debug0) {
/* 63 */     return ((Block)BLOCK_BY_HOST_BLOCK.get(debug0)).defaultBlockState();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\InfestedBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */