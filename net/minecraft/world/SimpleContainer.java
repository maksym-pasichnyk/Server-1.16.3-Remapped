/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class SimpleContainer implements Container, StackedContentsCompatible {
/*     */   private final int size;
/*     */   private final NonNullList<ItemStack> items;
/*     */   private List<ContainerListener> listeners;
/*     */   
/*     */   public SimpleContainer(int debug1) {
/*  22 */     this.size = debug1;
/*  23 */     this.items = NonNullList.withSize(debug1, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleContainer(ItemStack... debug1) {
/*  27 */     this.size = debug1.length;
/*  28 */     this.items = NonNullList.of(ItemStack.EMPTY, (Object[])debug1);
/*     */   }
/*     */   
/*     */   public void addListener(ContainerListener debug1) {
/*  32 */     if (this.listeners == null) {
/*  33 */       this.listeners = Lists.newArrayList();
/*     */     }
/*  35 */     this.listeners.add(debug1);
/*     */   }
/*     */   
/*     */   public void removeListener(ContainerListener debug1) {
/*  39 */     this.listeners.remove(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*  44 */     if (debug1 < 0 || debug1 >= this.items.size()) {
/*  45 */       return ItemStack.EMPTY;
/*     */     }
/*  47 */     return (ItemStack)this.items.get(debug1);
/*     */   }
/*     */   
/*     */   public List<ItemStack> removeAllItems() {
/*  51 */     List<ItemStack> debug1 = (List<ItemStack>)this.items.stream().filter(debug0 -> !debug0.isEmpty()).collect(Collectors.toList());
/*  52 */     clearContent();
/*  53 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*  58 */     ItemStack debug3 = ContainerHelper.removeItem((List<ItemStack>)this.items, debug1, debug2);
/*  59 */     if (!debug3.isEmpty()) {
/*  60 */       setChanged();
/*     */     }
/*  62 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemType(Item debug1, int debug2) {
/*  70 */     ItemStack debug3 = new ItemStack((ItemLike)debug1, 0);
/*     */     
/*  72 */     for (int debug4 = this.size - 1; debug4 >= 0; debug4--) {
/*  73 */       ItemStack debug5 = getItem(debug4);
/*  74 */       if (debug5.getItem().equals(debug1)) {
/*  75 */         int debug6 = debug2 - debug3.getCount();
/*  76 */         ItemStack debug7 = debug5.split(debug6);
/*  77 */         debug3.grow(debug7.getCount());
/*  78 */         if (debug3.getCount() == debug2) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*  83 */     if (!debug3.isEmpty()) {
/*  84 */       setChanged();
/*     */     }
/*  86 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack addItem(ItemStack debug1) {
/*  94 */     ItemStack debug2 = debug1.copy();
/*     */     
/*  96 */     moveItemToOccupiedSlotsWithSameType(debug2);
/*  97 */     if (debug2.isEmpty()) {
/*  98 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 101 */     moveItemToEmptySlots(debug2);
/* 102 */     if (debug2.isEmpty()) {
/* 103 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 106 */     return debug2;
/*     */   }
/*     */   
/*     */   public boolean canAddItem(ItemStack debug1) {
/* 110 */     boolean debug2 = false;
/* 111 */     for (ItemStack debug4 : this.items) {
/* 112 */       if (debug4.isEmpty() || (isSameItem(debug4, debug1) && debug4.getCount() < debug4.getMaxStackSize())) {
/* 113 */         debug2 = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 117 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/* 122 */     ItemStack debug2 = (ItemStack)this.items.get(debug1);
/* 123 */     if (debug2.isEmpty()) {
/* 124 */       return ItemStack.EMPTY;
/*     */     }
/* 126 */     this.items.set(debug1, ItemStack.EMPTY);
/* 127 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 132 */     this.items.set(debug1, debug2);
/* 133 */     if (!debug2.isEmpty() && debug2.getCount() > getMaxStackSize()) {
/* 134 */       debug2.setCount(getMaxStackSize());
/*     */     }
/* 136 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 141 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 146 */     for (ItemStack debug2 : this.items) {
/* 147 */       if (!debug2.isEmpty()) {
/* 148 */         return false;
/*     */       }
/*     */     } 
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 156 */     if (this.listeners != null) {
/* 157 */       for (ContainerListener debug2 : this.listeners) {
/* 158 */         debug2.containerChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 170 */     this.items.clear();
/* 171 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedContents debug1) {
/* 176 */     for (ItemStack debug3 : this.items) {
/* 177 */       debug1.accountStack(debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     return ((List)this.items.stream()
/* 184 */       .filter(debug0 -> !debug0.isEmpty())
/* 185 */       .collect(Collectors.toList()))
/* 186 */       .toString();
/*     */   }
/*     */   
/*     */   private void moveItemToEmptySlots(ItemStack debug1) {
/* 190 */     for (int debug2 = 0; debug2 < this.size; debug2++) {
/* 191 */       ItemStack debug3 = getItem(debug2);
/* 192 */       if (debug3.isEmpty()) {
/* 193 */         setItem(debug2, debug1.copy());
/* 194 */         debug1.setCount(0);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveItemToOccupiedSlotsWithSameType(ItemStack debug1) {
/* 201 */     for (int debug2 = 0; debug2 < this.size; debug2++) {
/* 202 */       ItemStack debug3 = getItem(debug2);
/* 203 */       if (isSameItem(debug3, debug1)) {
/* 204 */         moveItemsBetweenStacks(debug1, debug3);
/* 205 */         if (debug1.isEmpty()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSameItem(ItemStack debug1, ItemStack debug2) {
/* 213 */     return (debug1.getItem() == debug2.getItem() && ItemStack.tagMatches(debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveItemsBetweenStacks(ItemStack debug1, ItemStack debug2) {
/* 220 */     int debug3 = Math.min(getMaxStackSize(), debug2.getMaxStackSize());
/* 221 */     int debug4 = Math.min(debug1.getCount(), debug3 - debug2.getCount());
/* 222 */     if (debug4 > 0) {
/* 223 */       debug2.grow(debug4);
/* 224 */       debug1.shrink(debug4);
/* 225 */       setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fromTag(ListTag debug1) {
/* 230 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 231 */       ItemStack debug3 = ItemStack.of(debug1.getCompound(debug2));
/* 232 */       if (!debug3.isEmpty()) {
/* 233 */         addItem(debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public ListTag createTag() {
/* 239 */     ListTag debug1 = new ListTag();
/* 240 */     for (int debug2 = 0; debug2 < getContainerSize(); debug2++) {
/* 241 */       ItemStack debug3 = getItem(debug2);
/* 242 */       if (!debug3.isEmpty()) {
/* 243 */         debug1.add(debug3.save(new CompoundTag()));
/*     */       }
/*     */     } 
/* 246 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\SimpleContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */