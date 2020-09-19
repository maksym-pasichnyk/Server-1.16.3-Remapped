/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.LecternMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.WrittenBookItem;
/*     */ import net.minecraft.world.level.block.LecternBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LecternBlockEntity
/*     */   extends BlockEntity
/*     */   implements Clearable, MenuProvider
/*     */ {
/*  40 */   private final Container bookAccess = new Container()
/*     */     {
/*     */       public int getContainerSize() {
/*  43 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/*  48 */         return LecternBlockEntity.this.book.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack getItem(int debug1) {
/*  53 */         return (debug1 == 0) ? LecternBlockEntity.this.book : ItemStack.EMPTY;
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack removeItem(int debug1, int debug2) {
/*  58 */         if (debug1 == 0) {
/*  59 */           ItemStack debug3 = LecternBlockEntity.this.book.split(debug2);
/*  60 */           if (LecternBlockEntity.this.book.isEmpty()) {
/*  61 */             LecternBlockEntity.this.onBookItemRemove();
/*     */           }
/*  63 */           return debug3;
/*     */         } 
/*  65 */         return ItemStack.EMPTY;
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack removeItemNoUpdate(int debug1) {
/*  70 */         if (debug1 == 0) {
/*  71 */           ItemStack debug2 = LecternBlockEntity.this.book;
/*  72 */           LecternBlockEntity.this.book = ItemStack.EMPTY;
/*  73 */           LecternBlockEntity.this.onBookItemRemove();
/*  74 */           return debug2;
/*     */         } 
/*  76 */         return ItemStack.EMPTY;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void setItem(int debug1, ItemStack debug2) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public int getMaxStackSize() {
/*  86 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public void setChanged() {
/*  91 */         LecternBlockEntity.this.setChanged();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean stillValid(Player debug1) {
/*  96 */         if (LecternBlockEntity.this.level.getBlockEntity(LecternBlockEntity.this.worldPosition) != LecternBlockEntity.this) {
/*  97 */           return false;
/*     */         }
/*  99 */         if (debug1.distanceToSqr(LecternBlockEntity.this.worldPosition.getX() + 0.5D, LecternBlockEntity.this.worldPosition.getY() + 0.5D, LecternBlockEntity.this.worldPosition.getZ() + 0.5D) > 64.0D) {
/* 100 */           return false;
/*     */         }
/* 102 */         return LecternBlockEntity.this.hasBook();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean canPlaceItem(int debug1, ItemStack debug2) {
/* 107 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public void clearContent() {}
/*     */     };
/*     */ 
/*     */   
/* 115 */   private final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int debug1) {
/* 118 */         return (debug1 == 0) ? LecternBlockEntity.this.page : 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int debug1, int debug2) {
/* 123 */         if (debug1 == 0) {
/* 124 */           LecternBlockEntity.this.setPage(debug2);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public int getCount() {
/* 130 */         return 1;
/*     */       }
/*     */     };
/*     */   
/* 134 */   private ItemStack book = ItemStack.EMPTY;
/*     */   private int page;
/*     */   private int pageCount;
/*     */   
/*     */   public LecternBlockEntity() {
/* 139 */     super(BlockEntityType.LECTERN);
/*     */   }
/*     */   
/*     */   public ItemStack getBook() {
/* 143 */     return this.book;
/*     */   }
/*     */   
/*     */   public boolean hasBook() {
/* 147 */     Item debug1 = this.book.getItem();
/* 148 */     return (debug1 == Items.WRITABLE_BOOK || debug1 == Items.WRITTEN_BOOK);
/*     */   }
/*     */   
/*     */   public void setBook(ItemStack debug1) {
/* 152 */     setBook(debug1, (Player)null);
/*     */   }
/*     */   
/*     */   private void onBookItemRemove() {
/* 156 */     this.page = 0;
/* 157 */     this.pageCount = 0;
/* 158 */     LecternBlock.resetBookState(getLevel(), getBlockPos(), getBlockState(), false);
/*     */   }
/*     */   
/*     */   public void setBook(ItemStack debug1, @Nullable Player debug2) {
/* 162 */     this.book = resolveBook(debug1, debug2);
/* 163 */     this.page = 0;
/* 164 */     this.pageCount = WrittenBookItem.getPageCount(this.book);
/* 165 */     setChanged();
/*     */   }
/*     */   
/*     */   private void setPage(int debug1) {
/* 169 */     int debug2 = Mth.clamp(debug1, 0, this.pageCount - 1);
/* 170 */     if (debug2 != this.page) {
/* 171 */       this.page = debug2;
/* 172 */       setChanged();
/* 173 */       LecternBlock.signalPageChange(getLevel(), getBlockPos(), getBlockState());
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getPage() {
/* 178 */     return this.page;
/*     */   }
/*     */   
/*     */   public int getRedstoneSignal() {
/* 182 */     float debug1 = (this.pageCount > 1) ? (getPage() / (this.pageCount - 1.0F)) : 1.0F;
/* 183 */     return Mth.floor(debug1 * 14.0F) + (hasBook() ? 1 : 0);
/*     */   }
/*     */   
/*     */   private ItemStack resolveBook(ItemStack debug1, @Nullable Player debug2) {
/* 187 */     if (this.level instanceof ServerLevel && debug1.getItem() == Items.WRITTEN_BOOK) {
/* 188 */       WrittenBookItem.resolveBookComponents(debug1, createCommandSourceStack(debug2), debug2);
/*     */     }
/* 190 */     return debug1;
/*     */   }
/*     */   
/*     */   private CommandSourceStack createCommandSourceStack(@Nullable Player debug1) {
/*     */     String debug2;
/*     */     Component debug3;
/* 196 */     if (debug1 == null) {
/* 197 */       debug2 = "Lectern";
/* 198 */       TextComponent textComponent = new TextComponent("Lectern");
/*     */     } else {
/* 200 */       debug2 = debug1.getName().getString();
/* 201 */       debug3 = debug1.getDisplayName();
/*     */     } 
/* 203 */     Vec3 debug4 = Vec3.atCenterOf((Vec3i)this.worldPosition);
/* 204 */     return new CommandSourceStack(CommandSource.NULL, debug4, Vec2.ZERO, (ServerLevel)this.level, 2, debug2, debug3, this.level.getServer(), (Entity)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 209 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 214 */     super.load(debug1, debug2);
/*     */     
/* 216 */     if (debug2.contains("Book", 10)) {
/* 217 */       this.book = resolveBook(ItemStack.of(debug2.getCompound("Book")), (Player)null);
/*     */     } else {
/* 219 */       this.book = ItemStack.EMPTY;
/*     */     } 
/*     */     
/* 222 */     this.pageCount = WrittenBookItem.getPageCount(this.book);
/* 223 */     this.page = Mth.clamp(debug2.getInt("Page"), 0, this.pageCount - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 228 */     super.save(debug1);
/*     */     
/* 230 */     if (!getBook().isEmpty()) {
/* 231 */       debug1.put("Book", (Tag)getBook().save(new CompoundTag()));
/* 232 */       debug1.putInt("Page", this.page);
/*     */     } 
/*     */     
/* 235 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 240 */     setBook(ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 245 */     return (AbstractContainerMenu)new LecternMenu(debug1, this.bookAccess, this.dataAccess);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getDisplayName() {
/* 250 */     return (Component)new TranslatableComponent("container.lectern");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */