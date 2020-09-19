/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Item
/*     */   implements ItemLike
/*     */ {
/*  46 */   public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
/*     */   
/*  48 */   protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
/*  49 */   protected static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
/*     */   
/*  51 */   protected static final Random random = new Random(); protected final CreativeModeTab category; private final Rarity rarity;
/*     */   private final int maxStackSize;
/*     */   private final int maxDamage;
/*     */   
/*     */   public static int getId(Item debug0) {
/*  56 */     return (debug0 == null) ? 0 : Registry.ITEM.getId(debug0);
/*     */   } private final boolean isFireResistant; private final Item craftingRemainingItem; @Nullable
/*     */   private String descriptionId; @Nullable
/*     */   private final FoodProperties foodProperties; public static Item byId(int debug0) {
/*  60 */     return (Item)Registry.ITEM.byId(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Item byBlock(Block debug0) {
/*  66 */     return BY_BLOCK.getOrDefault(debug0, Items.AIR);
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
/*     */   public Item(Properties debug1) {
/*  85 */     this.category = debug1.category;
/*  86 */     this.rarity = debug1.rarity;
/*  87 */     this.craftingRemainingItem = debug1.craftingRemainingItem;
/*  88 */     this.maxDamage = debug1.maxDamage;
/*  89 */     this.maxStackSize = debug1.maxStackSize;
/*  90 */     this.foodProperties = debug1.foodProperties;
/*  91 */     this.isFireResistant = debug1.isFireResistant;
/*     */   }
/*     */   
/*     */   public static class Properties {
/*  95 */     private int maxStackSize = 64;
/*     */     private int maxDamage;
/*     */     private Item craftingRemainingItem;
/*     */     private CreativeModeTab category;
/*  99 */     private Rarity rarity = Rarity.COMMON;
/*     */     private FoodProperties foodProperties;
/*     */     private boolean isFireResistant;
/*     */     
/*     */     public Properties food(FoodProperties debug1) {
/* 104 */       this.foodProperties = debug1;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Properties stacksTo(int debug1) {
/* 109 */       if (this.maxDamage > 0) {
/* 110 */         throw new RuntimeException("Unable to have damage AND stack.");
/*     */       }
/* 112 */       this.maxStackSize = debug1;
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Properties defaultDurability(int debug1) {
/* 117 */       return (this.maxDamage == 0) ? durability(debug1) : this;
/*     */     }
/*     */     
/*     */     public Properties durability(int debug1) {
/* 121 */       this.maxDamage = debug1;
/* 122 */       this.maxStackSize = 1;
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Properties craftRemainder(Item debug1) {
/* 127 */       this.craftingRemainingItem = debug1;
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public Properties tab(CreativeModeTab debug1) {
/* 132 */       this.category = debug1;
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public Properties rarity(Rarity debug1) {
/* 137 */       this.rarity = debug1;
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public Properties fireResistant() {
/* 142 */       this.isFireResistant = true;
/* 143 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level debug1, LivingEntity debug2, ItemStack debug3, int debug4) {}
/*     */   
/*     */   public boolean verifyTagAfterLoad(CompoundTag debug1) {
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canAttackBlock(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item asItem() {
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/* 164 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 168 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 172 */     if (isEdible()) {
/* 173 */       ItemStack debug4 = debug2.getItemInHand(debug3);
/* 174 */       if (debug2.canEat(getFoodProperties().canAlwaysEat())) {
/* 175 */         debug2.startUsingItem(debug3);
/* 176 */         return InteractionResultHolder.consume(debug4);
/*     */       } 
/* 178 */       return InteractionResultHolder.fail(debug4);
/*     */     } 
/* 180 */     return InteractionResultHolder.pass(debug2.getItemInHand(debug3));
/*     */   }
/*     */   
/*     */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 184 */     if (isEdible()) {
/* 185 */       return debug3.eat(debug2, debug1);
/*     */     }
/* 187 */     return debug1;
/*     */   }
/*     */   
/*     */   public final int getMaxStackSize() {
/* 191 */     return this.maxStackSize;
/*     */   }
/*     */   
/*     */   public final int getMaxDamage() {
/* 195 */     return this.maxDamage;
/*     */   }
/*     */   
/*     */   public boolean canBeDepleted() {
/* 199 */     return (this.maxDamage > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtEnemy(ItemStack debug1, LivingEntity debug2, LivingEntity debug3) {
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mineBlock(ItemStack debug1, Level debug2, BlockState debug3, BlockPos debug4, LivingEntity debug5) {
/* 213 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCorrectToolForDrops(BlockState debug1) {
/* 217 */     return false;
/*     */   }
/*     */   
/*     */   public InteractionResult interactLivingEntity(ItemStack debug1, Player debug2, LivingEntity debug3, InteractionHand debug4) {
/* 221 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 231 */     return Registry.ITEM.getKey(this).getPath();
/*     */   }
/*     */   
/*     */   protected String getOrCreateDescriptionId() {
/* 235 */     if (this.descriptionId == null) {
/* 236 */       this.descriptionId = Util.makeDescriptionId("item", Registry.ITEM.getKey(this));
/*     */     }
/* 238 */     return this.descriptionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/* 245 */     return getOrCreateDescriptionId();
/*     */   }
/*     */   
/*     */   public String getDescriptionId(ItemStack debug1) {
/* 249 */     return getDescriptionId();
/*     */   }
/*     */   
/*     */   public boolean shouldOverrideMultiplayerNbt() {
/* 253 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Item getCraftingRemainingItem() {
/* 259 */     return this.craftingRemainingItem;
/*     */   }
/*     */   
/*     */   public boolean hasCraftingRemainingItem() {
/* 263 */     return (this.craftingRemainingItem != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack debug1, Level debug2, Entity debug3, int debug4, boolean debug5) {}
/*     */ 
/*     */   
/*     */   public void onCraftedBy(ItemStack debug1, Level debug2, Player debug3) {}
/*     */   
/*     */   public boolean isComplex() {
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 277 */     return debug1.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE;
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack debug1) {
/* 281 */     if (debug1.getItem().isEdible()) {
/* 282 */       return getFoodProperties().isFastFood() ? 16 : 32;
/*     */     }
/* 284 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseUsing(ItemStack debug1, Level debug2, LivingEntity debug3, int debug4) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getName(ItemStack debug1) {
/* 294 */     return (Component)new TranslatableComponent(getDescriptionId(debug1));
/*     */   }
/*     */   
/*     */   public boolean isFoil(ItemStack debug1) {
/* 298 */     return debug1.isEnchanted();
/*     */   }
/*     */   
/*     */   public Rarity getRarity(ItemStack debug1) {
/* 302 */     if (!debug1.isEnchanted()) {
/* 303 */       return this.rarity;
/*     */     }
/*     */     
/* 306 */     switch (this.rarity) {
/*     */       case COMMON:
/*     */       case UNCOMMON:
/* 309 */         return Rarity.RARE;
/*     */       
/*     */       case RARE:
/* 312 */         return Rarity.EPIC;
/*     */     } 
/*     */ 
/*     */     
/* 316 */     return this.rarity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnchantable(ItemStack debug1) {
/* 321 */     return (getMaxStackSize() == 1 && canBeDepleted());
/*     */   }
/*     */   
/*     */   protected static BlockHitResult getPlayerPOVHitResult(Level debug0, Player debug1, ClipContext.Fluid debug2) {
/* 325 */     float debug3 = debug1.xRot;
/* 326 */     float debug4 = debug1.yRot;
/* 327 */     Vec3 debug5 = debug1.getEyePosition(1.0F);
/*     */ 
/*     */     
/* 330 */     float debug6 = Mth.cos(-debug4 * 0.017453292F - 3.1415927F);
/* 331 */     float debug7 = Mth.sin(-debug4 * 0.017453292F - 3.1415927F);
/* 332 */     float debug8 = -Mth.cos(-debug3 * 0.017453292F);
/* 333 */     float debug9 = Mth.sin(-debug3 * 0.017453292F);
/*     */     
/* 335 */     float debug10 = debug7 * debug8;
/* 336 */     float debug11 = debug9;
/* 337 */     float debug12 = debug6 * debug8;
/*     */     
/* 339 */     double debug13 = 5.0D;
/* 340 */     Vec3 debug15 = debug5.add(debug10 * 5.0D, debug11 * 5.0D, debug12 * 5.0D);
/*     */     
/* 342 */     return debug0.clip(new ClipContext(debug5, debug15, ClipContext.Block.OUTLINE, debug2, (Entity)debug1));
/*     */   }
/*     */   
/*     */   public int getEnchantmentValue() {
/* 346 */     return 0;
/*     */   }
/*     */   
/*     */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 350 */     if (allowdedIn(debug1)) {
/* 351 */       debug2.add(new ItemStack(this));
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean allowdedIn(CreativeModeTab debug1) {
/* 356 */     CreativeModeTab debug2 = getItemCategory();
/* 357 */     return (debug2 != null && (debug1 == CreativeModeTab.TAB_SEARCH || debug1 == debug2));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public final CreativeModeTab getItemCategory() {
/* 362 */     return this.category;
/*     */   }
/*     */   
/*     */   public boolean isValidRepairItem(ItemStack debug1, ItemStack debug2) {
/* 366 */     return false;
/*     */   }
/*     */   
/*     */   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot debug1) {
/* 370 */     return (Multimap<Attribute, AttributeModifier>)ImmutableMultimap.of();
/*     */   }
/*     */   
/*     */   public boolean useOnRelease(ItemStack debug1) {
/* 374 */     return (debug1.getItem() == Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   public ItemStack getDefaultInstance() {
/* 378 */     return new ItemStack(this);
/*     */   }
/*     */   
/*     */   public boolean is(Tag<Item> debug1) {
/* 382 */     return debug1.contains(this);
/*     */   }
/*     */   
/*     */   public boolean isEdible() {
/* 386 */     return (this.foodProperties != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public FoodProperties getFoodProperties() {
/* 391 */     return this.foodProperties;
/*     */   }
/*     */   
/*     */   public SoundEvent getDrinkingSound() {
/* 395 */     return SoundEvents.GENERIC_DRINK;
/*     */   }
/*     */   
/*     */   public SoundEvent getEatingSound() {
/* 399 */     return SoundEvents.GENERIC_EAT;
/*     */   }
/*     */   
/*     */   public boolean isFireResistant() {
/* 403 */     return this.isFireResistant;
/*     */   }
/*     */   
/*     */   public boolean canBeHurtBy(DamageSource debug1) {
/* 407 */     return (!this.isFireResistant || !debug1.isFire());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */