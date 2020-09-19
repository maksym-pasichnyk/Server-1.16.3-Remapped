/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemInput implements Predicate<ItemStack> {
/*    */   static {
/* 16 */     ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("arguments.item.overstacked", new Object[] { debug0, debug1 }));
/*    */   }
/*    */   private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG; private final Item item;
/*    */   @Nullable
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public ItemInput(Item debug1, @Nullable CompoundTag debug2) {
/* 23 */     this.item = debug1;
/* 24 */     this.tag = debug2;
/*    */   }
/*    */   
/*    */   public Item getItem() {
/* 28 */     return this.item;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(ItemStack debug1) {
/* 38 */     return (debug1.getItem() == this.item && NbtUtils.compareNbt((Tag)this.tag, (Tag)debug1.getTag(), true));
/*    */   }
/*    */   
/*    */   public ItemStack createItemStack(int debug1, boolean debug2) throws CommandSyntaxException {
/* 42 */     ItemStack debug3 = new ItemStack((ItemLike)this.item, debug1);
/* 43 */     if (this.tag != null) {
/* 44 */       debug3.setTag(this.tag);
/*    */     }
/* 46 */     if (debug2 && debug1 > debug3.getMaxStackSize()) {
/* 47 */       throw ERROR_STACK_TOO_BIG.create(Registry.ITEM.getKey(this.item), Integer.valueOf(debug3.getMaxStackSize()));
/*    */     }
/* 49 */     return debug3;
/*    */   }
/*    */   
/*    */   public String serialize() {
/* 53 */     StringBuilder debug1 = new StringBuilder(Registry.ITEM.getId(this.item));
/* 54 */     if (this.tag != null) {
/* 55 */       debug1.append(this.tag);
/*    */     }
/* 57 */     return debug1.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\item\ItemInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */