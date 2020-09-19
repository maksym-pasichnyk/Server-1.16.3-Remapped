/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class BlastFurnaceMenu extends AbstractFurnaceMenu {
/*    */   public BlastFurnaceMenu(int debug1, Inventory debug2) {
/*  9 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, debug1, debug2);
/*    */   }
/*    */   
/*    */   public BlastFurnaceMenu(int debug1, Inventory debug2, Container debug3, ContainerData debug4) {
/* 13 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\BlastFurnaceMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */