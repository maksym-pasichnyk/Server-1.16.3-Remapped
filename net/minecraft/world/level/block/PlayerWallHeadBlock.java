/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class PlayerWallHeadBlock extends WallSkullBlock {
/*    */   protected PlayerWallHeadBlock(BlockBehaviour.Properties debug1) {
/* 15 */     super(SkullBlock.Types.PLAYER, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 20 */     Blocks.PLAYER_HEAD.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/* 25 */     return Blocks.PLAYER_HEAD.getDrops(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PlayerWallHeadBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */