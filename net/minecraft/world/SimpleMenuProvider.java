/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MenuConstructor;
/*    */ 
/*    */ public final class SimpleMenuProvider implements MenuProvider {
/*    */   private final Component title;
/*    */   private final MenuConstructor menuConstructor;
/*    */   
/*    */   public SimpleMenuProvider(MenuConstructor debug1, Component debug2) {
/* 14 */     this.menuConstructor = debug1;
/* 15 */     this.title = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 20 */     return this.title;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 25 */     return this.menuConstructor.createMenu(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\SimpleMenuProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */