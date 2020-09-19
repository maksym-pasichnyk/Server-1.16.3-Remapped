/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.FurnaceMenu;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/*    */   public FurnaceBlockEntity() {
/* 12 */     super(BlockEntityType.FURNACE, RecipeType.SMELTING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 17 */     return (Component)new TranslatableComponent("container.furnace");
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 22 */     return (AbstractContainerMenu)new FurnaceMenu(debug1, debug2, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\FurnaceBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */