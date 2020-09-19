/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LecternMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private final Container lectern;
/*    */   private final ContainerData lecternData;
/*    */   
/*    */   public LecternMenu(int debug1) {
/* 22 */     this(debug1, (Container)new SimpleContainer(1), new SimpleContainerData(1));
/*    */   }
/*    */   
/*    */   public LecternMenu(int debug1, Container debug2, ContainerData debug3) {
/* 26 */     super(MenuType.LECTERN, debug1);
/* 27 */     checkContainerSize(debug2, 1);
/* 28 */     checkContainerDataCount(debug3, 1);
/* 29 */     this.lectern = debug2;
/* 30 */     this.lecternData = debug3;
/* 31 */     addSlot(new Slot(debug2, 0, 0, 0)
/*    */         {
/*    */           public void setChanged() {
/* 34 */             super.setChanged();
/* 35 */             LecternMenu.this.slotsChanged(this.container);
/*    */           }
/*    */         });
/*    */     
/* 39 */     addDataSlots(debug3);
/*    */   }
/*    */   public boolean clickMenuButton(Player debug1, int debug2) {
/*    */     int i;
/*    */     ItemStack debug3;
/* 44 */     if (debug2 >= 100) {
/* 45 */       int j = debug2 - 100;
/* 46 */       setData(0, j);
/* 47 */       return true;
/*    */     } 
/*    */     
/* 50 */     switch (debug2) {
/*    */       case 2:
/* 52 */         i = this.lecternData.get(0);
/* 53 */         setData(0, i + 1);
/* 54 */         return true;
/*    */       
/*    */       case 1:
/* 57 */         i = this.lecternData.get(0);
/* 58 */         setData(0, i - 1);
/* 59 */         return true;
/*    */       
/*    */       case 3:
/* 62 */         if (!debug1.mayBuild()) {
/* 63 */           return false;
/*    */         }
/* 65 */         debug3 = this.lectern.removeItemNoUpdate(0);
/* 66 */         this.lectern.setChanged();
/* 67 */         if (!debug1.inventory.add(debug3)) {
/* 68 */           debug1.drop(debug3, false);
/*    */         }
/* 70 */         return true;
/*    */     } 
/*    */     
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(int debug1, int debug2) {
/* 78 */     super.setData(debug1, debug2);
/* 79 */     broadcastChanges();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 84 */     return this.lectern.stillValid(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\LecternMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */