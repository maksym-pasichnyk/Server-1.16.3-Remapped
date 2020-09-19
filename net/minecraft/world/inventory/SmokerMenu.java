/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class SmokerMenu extends AbstractFurnaceMenu {
/*    */   public SmokerMenu(int debug1, Inventory debug2) {
/*  9 */     super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, debug1, debug2);
/*    */   }
/*    */   
/*    */   public SmokerMenu(int debug1, Inventory debug2, Container debug3, ContainerData debug4) {
/* 13 */     super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\SmokerMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */