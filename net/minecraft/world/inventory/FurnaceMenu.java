/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class FurnaceMenu extends AbstractFurnaceMenu {
/*    */   public FurnaceMenu(int debug1, Inventory debug2) {
/*  9 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, debug1, debug2);
/*    */   }
/*    */   
/*    */   public FurnaceMenu(int debug1, Inventory debug2, Container debug3, ContainerData debug4) {
/* 13 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\FurnaceMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */