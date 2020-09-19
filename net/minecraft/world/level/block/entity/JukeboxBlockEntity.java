/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.world.Clearable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class JukeboxBlockEntity extends BlockEntity implements Clearable {
/* 10 */   private ItemStack record = ItemStack.EMPTY;
/*    */   
/*    */   public JukeboxBlockEntity() {
/* 13 */     super(BlockEntityType.JUKEBOX);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BlockState debug1, CompoundTag debug2) {
/* 18 */     super.load(debug1, debug2);
/*    */     
/* 20 */     if (debug2.contains("RecordItem", 10)) {
/* 21 */       setRecord(ItemStack.of(debug2.getCompound("RecordItem")));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 27 */     super.save(debug1);
/*    */     
/* 29 */     if (!getRecord().isEmpty()) {
/* 30 */       debug1.put("RecordItem", (Tag)getRecord().save(new CompoundTag()));
/*    */     }
/*    */     
/* 33 */     return debug1;
/*    */   }
/*    */   
/*    */   public ItemStack getRecord() {
/* 37 */     return this.record;
/*    */   }
/*    */   
/*    */   public void setRecord(ItemStack debug1) {
/* 41 */     this.record = debug1;
/* 42 */     setChanged();
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearContent() {
/* 47 */     setRecord(ItemStack.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\JukeboxBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */