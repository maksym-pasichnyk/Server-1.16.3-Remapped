/*    */ package net.minecraft.core;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class BlockSourceImpl
/*    */   implements BlockSource
/*    */ {
/*    */   private final ServerLevel level;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public BlockSourceImpl(ServerLevel debug1, BlockPos debug2) {
/* 14 */     this.level = debug1;
/* 15 */     this.pos = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerLevel getLevel() {
/* 20 */     return this.level;
/*    */   }
/*    */ 
/*    */   
/*    */   public double x() {
/* 25 */     return this.pos.getX() + 0.5D;
/*    */   }
/*    */ 
/*    */   
/*    */   public double y() {
/* 30 */     return this.pos.getY() + 0.5D;
/*    */   }
/*    */ 
/*    */   
/*    */   public double z() {
/* 35 */     return this.pos.getZ() + 0.5D;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPos() {
/* 40 */     return this.pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState() {
/* 45 */     return this.level.getBlockState(this.pos);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends net.minecraft.world.level.block.entity.BlockEntity> T getEntity() {
/* 57 */     return (T)this.level.getBlockEntity(this.pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\BlockSourceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */