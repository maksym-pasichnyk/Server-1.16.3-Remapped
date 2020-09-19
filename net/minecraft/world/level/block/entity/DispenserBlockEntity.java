/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.DispenserMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DispenserBlockEntity extends RandomizableContainerBlockEntity {
/* 17 */   private static final Random RANDOM = new Random();
/*    */ 
/*    */   
/* 20 */   private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
/*    */   
/*    */   protected DispenserBlockEntity(BlockEntityType<?> debug1) {
/* 23 */     super(debug1);
/*    */   }
/*    */   
/*    */   public DispenserBlockEntity() {
/* 27 */     this(BlockEntityType.DISPENSER);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getContainerSize() {
/* 32 */     return 9;
/*    */   }
/*    */   
/*    */   public int getRandomSlot() {
/* 36 */     unpackLootTable((Player)null);
/* 37 */     int debug1 = -1;
/* 38 */     int debug2 = 1;
/*    */     
/* 40 */     for (int debug3 = 0; debug3 < this.items.size(); debug3++) {
/* 41 */       if (!((ItemStack)this.items.get(debug3)).isEmpty() && RANDOM.nextInt(debug2++) == 0) {
/* 42 */         debug1 = debug3;
/*    */       }
/*    */     } 
/*    */     
/* 46 */     return debug1;
/*    */   }
/*    */   
/*    */   public int addItem(ItemStack debug1) {
/* 50 */     for (int debug2 = 0; debug2 < this.items.size(); debug2++) {
/* 51 */       if (((ItemStack)this.items.get(debug2)).isEmpty()) {
/* 52 */         setItem(debug2, debug1);
/* 53 */         return debug2;
/*    */       } 
/*    */     } 
/*    */     
/* 57 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 62 */     return (Component)new TranslatableComponent("container.dispenser");
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BlockState debug1, CompoundTag debug2) {
/* 67 */     super.load(debug1, debug2);
/*    */     
/* 69 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 70 */     if (!tryLoadLootTable(debug2)) {
/* 71 */       ContainerHelper.loadAllItems(debug2, this.items);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 77 */     super.save(debug1);
/*    */     
/* 79 */     if (!trySaveLootTable(debug1)) {
/* 80 */       ContainerHelper.saveAllItems(debug1, this.items);
/*    */     }
/* 82 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected NonNullList<ItemStack> getItems() {
/* 87 */     return this.items;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setItems(NonNullList<ItemStack> debug1) {
/* 92 */     this.items = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 97 */     return (AbstractContainerMenu)new DispenserMenu(debug1, debug2, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\DispenserBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */