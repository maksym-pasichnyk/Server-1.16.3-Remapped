/*     */ package net.minecraft.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerPlaceRecipe<C extends Container>
/*     */   implements PlaceRecipe<Integer> {
/*  25 */   protected static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  27 */   protected final StackedContents stackedContents = new StackedContents();
/*     */   
/*     */   protected Inventory inventory;
/*     */   protected RecipeBookMenu<C> menu;
/*     */   
/*     */   public ServerPlaceRecipe(RecipeBookMenu<C> debug1) {
/*  33 */     this.menu = debug1;
/*     */   }
/*     */   
/*     */   public void recipeClicked(ServerPlayer debug1, @Nullable Recipe<C> debug2, boolean debug3) {
/*  37 */     if (debug2 == null || !debug1.getRecipeBook().contains(debug2)) {
/*     */       return;
/*     */     }
/*     */     
/*  41 */     this.inventory = debug1.inventory;
/*     */ 
/*     */     
/*  44 */     if (!testClearGrid() && !debug1.isCreative()) {
/*     */       return;
/*     */     }
/*     */     
/*  48 */     this.stackedContents.clear();
/*  49 */     debug1.inventory.fillStackedContents(this.stackedContents);
/*  50 */     this.menu.fillCraftSlotsStackedContents(this.stackedContents);
/*     */     
/*  52 */     if (this.stackedContents.canCraft(debug2, null)) {
/*  53 */       handleRecipeClicked(debug2, debug3);
/*     */     } else {
/*  55 */       clearGrid();
/*  56 */       debug1.connection.send((Packet)new ClientboundPlaceGhostRecipePacket(debug1.containerMenu.containerId, debug2));
/*     */     } 
/*     */     
/*  59 */     debug1.inventory.setChanged();
/*     */   }
/*     */   
/*     */   protected void clearGrid() {
/*  63 */     for (int debug1 = 0; debug1 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; debug1++) {
/*  64 */       if (debug1 != this.menu.getResultSlotIndex() || (!(this.menu instanceof net.minecraft.world.inventory.CraftingMenu) && !(this.menu instanceof net.minecraft.world.inventory.InventoryMenu)))
/*     */       {
/*     */         
/*  67 */         moveItemToInventory(debug1);
/*     */       }
/*     */     } 
/*  70 */     this.menu.clearCraftingContent();
/*     */   }
/*     */   
/*     */   protected void moveItemToInventory(int debug1) {
/*  74 */     ItemStack debug2 = this.menu.getSlot(debug1).getItem();
/*  75 */     if (debug2.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     while (debug2.getCount() > 0) {
/*  80 */       int debug3 = this.inventory.getSlotWithRemainingSpace(debug2);
/*  81 */       if (debug3 == -1) {
/*  82 */         debug3 = this.inventory.getFreeSlot();
/*     */       }
/*  84 */       ItemStack debug4 = debug2.copy();
/*  85 */       debug4.setCount(1);
/*     */       
/*  87 */       if (!this.inventory.add(debug3, debug4)) {
/*  88 */         LOGGER.error("Can't find any space for item in the inventory");
/*     */       }
/*     */       
/*  91 */       this.menu.getSlot(debug1).remove(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void handleRecipeClicked(Recipe<C> debug1, boolean debug2) {
/*  96 */     boolean debug3 = this.menu.recipeMatches(debug1);
/*  97 */     int debug4 = this.stackedContents.getBiggestCraftableStack(debug1, null);
/*     */ 
/*     */     
/* 100 */     if (debug3) {
/* 101 */       for (int i = 0; i < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; i++) {
/* 102 */         if (i != this.menu.getResultSlotIndex()) {
/*     */ 
/*     */           
/* 105 */           ItemStack debug6 = this.menu.getSlot(i).getItem();
/* 106 */           if (!debug6.isEmpty() && Math.min(debug4, debug6.getMaxStackSize()) < debug6.getCount() + 1) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 112 */     int debug5 = getStackSize(debug2, debug4, debug3);
/* 113 */     IntArrayList intArrayList = new IntArrayList();
/* 114 */     if (this.stackedContents.canCraft(debug1, (IntList)intArrayList, debug5)) {
/*     */       
/* 116 */       int debug7 = debug5;
/* 117 */       for (IntListIterator<Integer> intListIterator = intArrayList.iterator(); intListIterator.hasNext(); ) { int debug9 = ((Integer)intListIterator.next()).intValue();
/* 118 */         int debug10 = StackedContents.fromStackingIndex(debug9).getMaxStackSize();
/* 119 */         if (debug10 < debug7) {
/* 120 */           debug7 = debug10;
/*     */         } }
/*     */       
/* 123 */       debug5 = debug7;
/*     */ 
/*     */       
/* 126 */       if (this.stackedContents.canCraft(debug1, (IntList)intArrayList, debug5)) {
/* 127 */         clearGrid();
/* 128 */         placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), debug1, (Iterator<Integer>)intArrayList.iterator(), debug5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addItemToSlot(Iterator<Integer> debug1, int debug2, int debug3, int debug4, int debug5) {
/* 135 */     Slot debug6 = this.menu.getSlot(debug2);
/* 136 */     ItemStack debug7 = StackedContents.fromStackingIndex(((Integer)debug1.next()).intValue());
/* 137 */     if (!debug7.isEmpty()) {
/* 138 */       for (int debug8 = 0; debug8 < debug3; debug8++) {
/* 139 */         moveItemToGrid(debug6, debug7);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected int getStackSize(boolean debug1, int debug2, boolean debug3) {
/* 145 */     int debug4 = 1;
/* 146 */     if (debug1) {
/* 147 */       debug4 = debug2;
/* 148 */     } else if (debug3) {
/* 149 */       debug4 = 64;
/* 150 */       for (int debug5 = 0; debug5 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; debug5++) {
/* 151 */         if (debug5 != this.menu.getResultSlotIndex()) {
/*     */ 
/*     */ 
/*     */           
/* 155 */           ItemStack debug6 = this.menu.getSlot(debug5).getItem();
/* 156 */           if (!debug6.isEmpty() && debug4 > debug6.getCount()) {
/* 157 */             debug4 = debug6.getCount();
/*     */           }
/*     */         } 
/*     */       } 
/* 161 */       if (debug4 < 64) {
/* 162 */         debug4++;
/*     */       }
/*     */     } 
/*     */     
/* 166 */     return debug4;
/*     */   }
/*     */   
/*     */   protected void moveItemToGrid(Slot debug1, ItemStack debug2) {
/* 170 */     int debug3 = this.inventory.findSlotMatchingUnusedItem(debug2);
/* 171 */     if (debug3 == -1) {
/*     */       return;
/*     */     }
/* 174 */     ItemStack debug4 = this.inventory.getItem(debug3).copy();
/*     */     
/* 176 */     if (debug4.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 180 */     if (debug4.getCount() > 1) {
/* 181 */       this.inventory.removeItem(debug3, 1);
/*     */     } else {
/* 183 */       this.inventory.removeItemNoUpdate(debug3);
/*     */     } 
/* 185 */     debug4.setCount(1);
/*     */     
/* 187 */     if (debug1.getItem().isEmpty()) {
/* 188 */       debug1.set(debug4);
/*     */     } else {
/* 190 */       debug1.getItem().grow(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean testClearGrid() {
/* 195 */     List<ItemStack> debug1 = Lists.newArrayList();
/* 196 */     int debug2 = getAmountOfFreeSlotsInInventory();
/*     */     
/* 198 */     for (int debug3 = 0; debug3 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; debug3++) {
/* 199 */       if (debug3 != this.menu.getResultSlotIndex()) {
/*     */ 
/*     */ 
/*     */         
/* 203 */         ItemStack debug4 = this.menu.getSlot(debug3).getItem().copy();
/* 204 */         if (!debug4.isEmpty()) {
/*     */ 
/*     */ 
/*     */           
/* 208 */           int debug5 = this.inventory.getSlotWithRemainingSpace(debug4);
/* 209 */           if (debug5 == -1 && debug1.size() <= debug2) {
/* 210 */             for (ItemStack debug7 : debug1) {
/* 211 */               if (debug7.sameItem(debug4) && debug7.getCount() != debug7.getMaxStackSize() && debug7.getCount() + debug4.getCount() <= debug7.getMaxStackSize()) {
/* 212 */                 debug7.grow(debug4.getCount());
/* 213 */                 debug4.setCount(0);
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 218 */             if (!debug4.isEmpty()) {
/* 219 */               if (debug1.size() < debug2) {
/* 220 */                 debug1.add(debug4);
/*     */               } else {
/* 222 */                 return false;
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/*     */           }
/* 229 */           else if (debug5 == -1) {
/* 230 */             return false;
/*     */           } 
/*     */         } 
/*     */       } 
/* 234 */     }  return true;
/*     */   }
/*     */   
/*     */   private int getAmountOfFreeSlotsInInventory() {
/* 238 */     int debug1 = 0;
/* 239 */     for (ItemStack debug3 : this.inventory.items) {
/* 240 */       if (debug3.isEmpty()) {
/* 241 */         debug1++;
/*     */       }
/*     */     } 
/* 244 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\recipebook\ServerPlaceRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */