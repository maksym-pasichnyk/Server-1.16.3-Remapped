/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnchantedBookItem
/*    */   extends Item
/*    */ {
/*    */   public EnchantedBookItem(Item.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFoil(ItemStack debug1) {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnchantable(ItemStack debug1) {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   public static ListTag getEnchantments(ItemStack debug0) {
/* 35 */     CompoundTag debug1 = debug0.getTag();
/* 36 */     if (debug1 != null) {
/* 37 */       return debug1.getList("StoredEnchantments", 10);
/*    */     }
/*    */     
/* 40 */     return new ListTag();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addEnchantment(ItemStack debug0, EnchantmentInstance debug1) {
/* 50 */     ListTag debug2 = getEnchantments(debug0);
/* 51 */     boolean debug3 = true;
/*    */     
/* 53 */     ResourceLocation debug4 = Registry.ENCHANTMENT.getKey(debug1.enchantment);
/* 54 */     for (int debug5 = 0; debug5 < debug2.size(); debug5++) {
/* 55 */       CompoundTag debug6 = debug2.getCompound(debug5);
/*    */       
/* 57 */       ResourceLocation debug7 = ResourceLocation.tryParse(debug6.getString("id"));
/* 58 */       if (debug7 != null && debug7.equals(debug4)) {
/* 59 */         if (debug6.getInt("lvl") < debug1.level) {
/* 60 */           debug6.putShort("lvl", (short)debug1.level);
/*    */         }
/*    */         
/* 63 */         debug3 = false;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 68 */     if (debug3) {
/* 69 */       CompoundTag compoundTag = new CompoundTag();
/*    */       
/* 71 */       compoundTag.putString("id", String.valueOf(debug4));
/* 72 */       compoundTag.putShort("lvl", (short)debug1.level);
/*    */       
/* 74 */       debug2.add(compoundTag);
/*    */     } 
/*    */     
/* 77 */     debug0.getOrCreateTag().put("StoredEnchantments", (Tag)debug2);
/*    */   }
/*    */   
/*    */   public static ItemStack createForEnchantment(EnchantmentInstance debug0) {
/* 81 */     ItemStack debug1 = new ItemStack(Items.ENCHANTED_BOOK);
/* 82 */     addEnchantment(debug1, debug0);
/* 83 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 88 */     if (debug1 == CreativeModeTab.TAB_SEARCH) {
/* 89 */       for (Enchantment debug4 : Registry.ENCHANTMENT) {
/* 90 */         if (debug4.category != null) {
/* 91 */           for (int debug5 = debug4.getMinLevel(); debug5 <= debug4.getMaxLevel(); debug5++) {
/* 92 */             debug2.add(createForEnchantment(new EnchantmentInstance(debug4, debug5)));
/*    */           }
/*    */         }
/*    */       } 
/* 96 */     } else if ((debug1.getEnchantmentCategories()).length != 0) {
/* 97 */       for (Enchantment debug4 : Registry.ENCHANTMENT) {
/* 98 */         if (debug1.hasEnchantmentCategory(debug4.category))
/* 99 */           debug2.add(createForEnchantment(new EnchantmentInstance(debug4, debug4.getMaxLevel()))); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\EnchantedBookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */