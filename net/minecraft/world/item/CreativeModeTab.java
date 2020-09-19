/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CreativeModeTab
/*     */ {
/*  15 */   public static final CreativeModeTab[] TABS = new CreativeModeTab[12];
/*  16 */   public static final CreativeModeTab TAB_BUILDING_BLOCKS = (new CreativeModeTab(0, "buildingBlocks")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*  21 */     }).setRecipeFolderName("building_blocks");
/*  22 */   public static final CreativeModeTab TAB_DECORATIONS = new CreativeModeTab(1, "decorations")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  28 */   public static final CreativeModeTab TAB_REDSTONE = new CreativeModeTab(2, "redstone")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  34 */   public static final CreativeModeTab TAB_TRANSPORTATION = new CreativeModeTab(3, "transportation")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  40 */   public static final CreativeModeTab TAB_MISC = new CreativeModeTab(6, "misc")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  46 */   public static final CreativeModeTab TAB_SEARCH = (new CreativeModeTab(5, "search")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*  51 */     }).setBackgroundSuffix("item_search.png");
/*     */   
/*  53 */   public static final CreativeModeTab TAB_FOOD = new CreativeModeTab(7, "food")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  59 */   public static final CreativeModeTab TAB_TOOLS = (new CreativeModeTab(8, "tools")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*  64 */     }).setEnchantmentCategories(new EnchantmentCategory[] { EnchantmentCategory.VANISHABLE, EnchantmentCategory.DIGGER, EnchantmentCategory.FISHING_ROD, EnchantmentCategory.BREAKABLE });
/*  65 */   public static final CreativeModeTab TAB_COMBAT = (new CreativeModeTab(9, "combat")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*  70 */     }).setEnchantmentCategories(new EnchantmentCategory[] { EnchantmentCategory.VANISHABLE, EnchantmentCategory.ARMOR, EnchantmentCategory.ARMOR_FEET, EnchantmentCategory.ARMOR_HEAD, EnchantmentCategory.ARMOR_LEGS, EnchantmentCategory.ARMOR_CHEST, EnchantmentCategory.BOW, EnchantmentCategory.WEAPON, EnchantmentCategory.WEARABLE, EnchantmentCategory.BREAKABLE, 
/*  71 */         EnchantmentCategory.TRIDENT, EnchantmentCategory.CROSSBOW }); public static final CreativeModeTab TAB_BREWING = new CreativeModeTab(10, "brewing")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  77 */   public static final CreativeModeTab TAB_MATERIALS = TAB_MISC;
/*  78 */   public static final CreativeModeTab TAB_HOTBAR = new CreativeModeTab(4, "hotbar")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final CreativeModeTab TAB_INVENTORY = (new CreativeModeTab(11, "inventory")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*  99 */     }).setBackgroundSuffix("inventory.png").hideScroll().hideTitle();
/*     */   
/*     */   private final int id;
/*     */   private final String langId;
/*     */   private final Component displayName;
/*     */   private String recipeFolderName;
/* 105 */   private String backgroundSuffix = "items.png";
/*     */   private boolean canScroll = true;
/*     */   private boolean showTitle = true;
/* 108 */   private EnchantmentCategory[] enchantmentCategories = new EnchantmentCategory[0];
/*     */   private ItemStack iconItemStack;
/*     */   
/*     */   public CreativeModeTab(int debug1, String debug2) {
/* 112 */     this.id = debug1;
/* 113 */     this.langId = debug2;
/* 114 */     this.displayName = (Component)new TranslatableComponent("itemGroup." + debug2);
/* 115 */     this.iconItemStack = ItemStack.EMPTY;
/*     */     
/* 117 */     TABS[debug1] = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecipeFolderName() {
/* 125 */     return (this.recipeFolderName == null) ? this.langId : this.recipeFolderName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreativeModeTab setBackgroundSuffix(String debug1) {
/* 146 */     this.backgroundSuffix = debug1;
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public CreativeModeTab setRecipeFolderName(String debug1) {
/* 151 */     this.recipeFolderName = debug1;
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreativeModeTab hideTitle() {
/* 160 */     this.showTitle = false;
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreativeModeTab hideScroll() {
/* 169 */     this.canScroll = false;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnchantmentCategory[] getEnchantmentCategories() {
/* 186 */     return this.enchantmentCategories;
/*     */   }
/*     */   
/*     */   public CreativeModeTab setEnchantmentCategories(EnchantmentCategory... debug1) {
/* 190 */     this.enchantmentCategories = debug1;
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public boolean hasEnchantmentCategory(@Nullable EnchantmentCategory debug1) {
/* 195 */     if (debug1 != null) {
/* 196 */       for (EnchantmentCategory debug5 : this.enchantmentCategories) {
/* 197 */         if (debug5 == debug1) {
/* 198 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 203 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\CreativeModeTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */