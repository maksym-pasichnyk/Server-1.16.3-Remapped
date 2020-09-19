/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class EnderChestBlockEntity
/*    */   extends BlockEntity
/*    */   implements TickableBlockEntity {
/*    */   public float openness;
/*    */   public float oOpenness;
/*    */   public int openCount;
/*    */   private int tickInterval;
/*    */   
/*    */   public EnderChestBlockEntity() {
/* 17 */     super(BlockEntityType.ENDER_CHEST);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 22 */     if (++this.tickInterval % 20 * 4 == 0) {
/* 23 */       this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
/*    */     }
/*    */     
/* 26 */     this.oOpenness = this.openness;
/*    */     
/* 28 */     int debug1 = this.worldPosition.getX();
/* 29 */     int debug2 = this.worldPosition.getY();
/* 30 */     int debug3 = this.worldPosition.getZ();
/*    */     
/* 32 */     float debug4 = 0.1F;
/* 33 */     if (this.openCount > 0 && this.openness == 0.0F) {
/* 34 */       double debug5 = debug1 + 0.5D;
/* 35 */       double debug7 = debug3 + 0.5D;
/*    */       
/* 37 */       this.level.playSound(null, debug5, debug2 + 0.5D, debug7, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*    */     } 
/* 39 */     if ((this.openCount == 0 && this.openness > 0.0F) || (this.openCount > 0 && this.openness < 1.0F)) {
/* 40 */       float debug5 = this.openness;
/* 41 */       if (this.openCount > 0) {
/* 42 */         this.openness += 0.1F;
/*    */       } else {
/* 44 */         this.openness -= 0.1F;
/*    */       } 
/* 46 */       if (this.openness > 1.0F) {
/* 47 */         this.openness = 1.0F;
/*    */       }
/* 49 */       float debug6 = 0.5F;
/* 50 */       if (this.openness < 0.5F && debug5 >= 0.5F) {
/* 51 */         double debug7 = debug1 + 0.5D;
/* 52 */         double debug9 = debug3 + 0.5D;
/*    */         
/* 54 */         this.level.playSound(null, debug7, debug2 + 0.5D, debug9, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*    */       } 
/* 56 */       if (this.openness < 0.0F) {
/* 57 */         this.openness = 0.0F;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean triggerEvent(int debug1, int debug2) {
/* 64 */     if (debug1 == 1) {
/* 65 */       this.openCount = debug2;
/* 66 */       return true;
/*    */     } 
/* 68 */     return super.triggerEvent(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRemoved() {
/* 73 */     clearCache();
/* 74 */     super.setRemoved();
/*    */   }
/*    */   
/*    */   public void startOpen() {
/* 78 */     this.openCount++;
/* 79 */     this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
/*    */   }
/*    */   
/*    */   public void stopOpen() {
/* 83 */     this.openCount--;
/* 84 */     this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
/*    */   }
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 88 */     if (this.level.getBlockEntity(this.worldPosition) != this) {
/* 89 */       return false;
/*    */     }
/* 91 */     if (debug1.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) > 64.0D) {
/* 92 */       return false;
/*    */     }
/*    */     
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\EnderChestBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */