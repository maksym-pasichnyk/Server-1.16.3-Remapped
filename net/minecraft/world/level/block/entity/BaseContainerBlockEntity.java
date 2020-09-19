/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.LockCode;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.Nameable;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BaseContainerBlockEntity
/*    */   extends BlockEntity
/*    */   implements Container, MenuProvider, Nameable {
/* 21 */   private LockCode lockKey = LockCode.NO_LOCK;
/*    */   private Component name;
/*    */   
/*    */   protected BaseContainerBlockEntity(BlockEntityType<?> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BlockState debug1, CompoundTag debug2) {
/* 30 */     super.load(debug1, debug2);
/*    */     
/* 32 */     this.lockKey = LockCode.fromTag(debug2);
/*    */     
/* 34 */     if (debug2.contains("CustomName", 8)) {
/* 35 */       this.name = (Component)Component.Serializer.fromJson(debug2.getString("CustomName"));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 41 */     super.save(debug1);
/* 42 */     this.lockKey.addToTag(debug1);
/*    */     
/* 44 */     if (this.name != null) {
/* 45 */       debug1.putString("CustomName", Component.Serializer.toJson(this.name));
/*    */     }
/*    */     
/* 48 */     return debug1;
/*    */   }
/*    */   
/*    */   public void setCustomName(Component debug1) {
/* 52 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName() {
/* 57 */     if (this.name != null) {
/* 58 */       return this.name;
/*    */     }
/* 60 */     return getDefaultName();
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 65 */     return getName();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Component getCustomName() {
/* 71 */     return this.name;
/*    */   }
/*    */   
/*    */   protected abstract Component getDefaultName();
/*    */   
/*    */   public boolean canOpen(Player debug1) {
/* 77 */     return canUnlock(debug1, this.lockKey, getDisplayName());
/*    */   }
/*    */   
/*    */   public static boolean canUnlock(Player debug0, LockCode debug1, Component debug2) {
/* 81 */     if (debug0.isSpectator() || debug1.unlocksWith(debug0.getMainHandItem())) {
/* 82 */       return true;
/*    */     }
/*    */     
/* 85 */     debug0.displayClientMessage((Component)new TranslatableComponent("container.isLocked", new Object[] { debug2 }), true);
/* 86 */     debug0.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 93 */     if (canOpen(debug3)) {
/* 94 */       return createMenu(debug1, debug2);
/*    */     }
/*    */     
/* 97 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BaseContainerBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */