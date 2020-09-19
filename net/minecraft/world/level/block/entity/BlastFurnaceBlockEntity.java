/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.BlastFurnaceMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class BlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/*    */   public BlastFurnaceBlockEntity() {
/* 13 */     super(BlockEntityType.BLAST_FURNACE, RecipeType.BLASTING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 18 */     return (Component)new TranslatableComponent("container.blast_furnace");
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBurnDuration(ItemStack debug1) {
/* 23 */     return super.getBurnDuration(debug1) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 28 */     return (AbstractContainerMenu)new BlastFurnaceMenu(debug1, debug2, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BlastFurnaceBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */