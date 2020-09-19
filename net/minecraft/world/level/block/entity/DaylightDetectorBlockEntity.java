/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.DaylightDetectorBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DaylightDetectorBlockEntity
/*    */   extends BlockEntity implements TickableBlockEntity {
/*    */   public DaylightDetectorBlockEntity() {
/* 10 */     super(BlockEntityType.DAYLIGHT_DETECTOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 15 */     if (this.level != null && !this.level.isClientSide && this.level.getGameTime() % 20L == 0L) {
/* 16 */       BlockState debug1 = getBlockState();
/* 17 */       Block debug2 = debug1.getBlock();
/* 18 */       if (debug2 instanceof DaylightDetectorBlock)
/* 19 */         DaylightDetectorBlock.updateSignalStrength(debug1, this.level, this.worldPosition); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\DaylightDetectorBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */