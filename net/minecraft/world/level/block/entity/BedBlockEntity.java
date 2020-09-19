/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class BedBlockEntity
/*    */   extends BlockEntity {
/*    */   private DyeColor color;
/*    */   
/*    */   public BedBlockEntity() {
/* 11 */     super(BlockEntityType.BED);
/*    */   }
/*    */   
/*    */   public BedBlockEntity(DyeColor debug1) {
/* 15 */     this();
/* 16 */     setColor(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 21 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 11, getUpdateTag());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setColor(DyeColor debug1) {
/* 32 */     this.color = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BedBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */