/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.SmokerMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class SmokerBlockEntity extends AbstractFurnaceBlockEntity {
/*    */   public SmokerBlockEntity() {
/* 13 */     super(BlockEntityType.SMOKER, RecipeType.SMOKING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 18 */     return (Component)new TranslatableComponent("container.smoker");
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBurnDuration(ItemStack debug1) {
/* 23 */     return super.getBurnDuration(debug1) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 28 */     return (AbstractContainerMenu)new SmokerMenu(debug1, debug2, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\SmokerBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */