/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ComparatorBlockEntity extends BlockEntity {
/*    */   private int output;
/*    */   
/*    */   public ComparatorBlockEntity() {
/* 10 */     super(BlockEntityType.COMPARATOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 15 */     super.save(debug1);
/* 16 */     debug1.putInt("OutputSignal", this.output);
/*    */     
/* 18 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BlockState debug1, CompoundTag debug2) {
/* 23 */     super.load(debug1, debug2);
/* 24 */     this.output = debug2.getInt("OutputSignal");
/*    */   }
/*    */   
/*    */   public int getOutputSignal() {
/* 28 */     return this.output;
/*    */   }
/*    */   
/*    */   public void setOutputSignal(int debug1) {
/* 32 */     this.output = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\ComparatorBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */