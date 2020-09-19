/*      */ package net.minecraft.world.item;
/*      */ 
/*      */ import com.google.common.collect.HashMultimap;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.gson.JsonParseException;
/*      */ import com.mojang.brigadier.StringReader;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.DecimalFormatSymbols;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.ComponentUtils;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.TextComponent;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.TagContainer;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.InteractionResultHolder;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.decoration.ItemFrame;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.context.UseOnContext;
/*      */ import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ItemStack
/*      */ {
/*      */   public static final Codec<ItemStack> CODEC;
/*      */   
/*      */   static {
/*   74 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Registry.ITEM.fieldOf("id").forGetter(()), (App)Codec.INT.fieldOf("Count").forGetter(()), (App)CompoundTag.CODEC.optionalFieldOf("tag").forGetter(())).apply((Applicative)debug0, ItemStack::new));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   80 */   private static final Logger LOGGER = LogManager.getLogger();
/*   81 */   public static final ItemStack EMPTY = new ItemStack((Item)null);
/*      */   static {
/*   83 */     ATTRIBUTE_MODIFIER_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("#.##"), debug0 -> debug0.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum TooltipPart
/*      */   {
/*  105 */     ENCHANTMENTS,
/*  106 */     MODIFIERS,
/*  107 */     UNBREAKABLE,
/*  108 */     CAN_DESTROY,
/*  109 */     CAN_PLACE,
/*  110 */     ADDITIONAL,
/*  111 */     DYE;
/*      */     
/*      */     TooltipPart() {
/*  114 */       this.mask = 1 << ordinal();
/*      */     }
/*      */     public int getMask() {
/*  117 */       return this.mask;
/*      */     }
/*      */ 
/*      */     
/*      */     private int mask;
/*      */   }
/*  123 */   private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(Boolean.valueOf(true));
/*      */   private int count;
/*      */   private int popTime;
/*      */   @Deprecated
/*      */   private final Item item;
/*      */   private CompoundTag tag;
/*      */   private boolean emptyCacheFlag;
/*      */   private Entity entityRepresentation;
/*      */   private BlockInWorld cachedBreakBlock;
/*      */   private boolean cachedBreakBlockResult;
/*      */   private BlockInWorld cachedPlaceBlock;
/*      */   private boolean cachedPlaceBlockResult;
/*      */   
/*      */   public ItemStack(ItemLike debug1) {
/*  137 */     this(debug1, 1);
/*      */   }
/*      */   
/*      */   private ItemStack(ItemLike debug1, int debug2, Optional<CompoundTag> debug3) {
/*  141 */     this(debug1, debug2);
/*  142 */     debug3.ifPresent(this::setTag);
/*      */   }
/*      */   
/*      */   public ItemStack(ItemLike debug1, int debug2) {
/*  146 */     this.item = (debug1 == null) ? null : debug1.asItem();
/*  147 */     this.count = debug2;
/*      */     
/*  149 */     if (this.item != null && this.item.canBeDepleted()) {
/*  150 */       setDamageValue(getDamageValue());
/*      */     }
/*      */     
/*  153 */     updateEmptyCacheFlag();
/*      */   }
/*      */   
/*      */   private void updateEmptyCacheFlag() {
/*  157 */     this.emptyCacheFlag = false;
/*  158 */     this.emptyCacheFlag = isEmpty();
/*      */   }
/*      */   
/*      */   private ItemStack(CompoundTag debug1) {
/*  162 */     this.item = (Item)Registry.ITEM.get(new ResourceLocation(debug1.getString("id")));
/*  163 */     this.count = debug1.getByte("Count");
/*      */     
/*  165 */     if (debug1.contains("tag", 10)) {
/*  166 */       this.tag = debug1.getCompound("tag");
/*  167 */       getItem().verifyTagAfterLoad(debug1);
/*      */     } 
/*      */     
/*  170 */     if (getItem().canBeDepleted()) {
/*  171 */       setDamageValue(getDamageValue());
/*      */     }
/*      */     
/*  174 */     updateEmptyCacheFlag();
/*      */   }
/*      */   
/*      */   public static ItemStack of(CompoundTag debug0) {
/*      */     try {
/*  179 */       return new ItemStack(debug0);
/*  180 */     } catch (RuntimeException debug1) {
/*  181 */       LOGGER.debug("Tried to load invalid item: {}", debug0, debug1);
/*  182 */       return EMPTY;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  187 */     if (this == EMPTY) {
/*  188 */       return true;
/*      */     }
/*      */     
/*  191 */     if (getItem() == null || getItem() == Items.AIR) {
/*  192 */       return true;
/*      */     }
/*  194 */     if (this.count <= 0) {
/*  195 */       return true;
/*      */     }
/*  197 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack split(int debug1) {
/*  205 */     int debug2 = Math.min(debug1, this.count);
/*      */     
/*  207 */     ItemStack debug3 = copy();
/*  208 */     debug3.setCount(debug2);
/*  209 */     shrink(debug2);
/*      */     
/*  211 */     return debug3;
/*      */   }
/*      */   
/*      */   public Item getItem() {
/*  215 */     return this.emptyCacheFlag ? Items.AIR : this.item;
/*      */   }
/*      */   
/*      */   public InteractionResult useOn(UseOnContext debug1) {
/*  219 */     Player debug2 = debug1.getPlayer();
/*  220 */     BlockPos debug3 = debug1.getClickedPos();
/*  221 */     BlockInWorld debug4 = new BlockInWorld((LevelReader)debug1.getLevel(), debug3, false);
/*  222 */     if (debug2 != null && !debug2.abilities.mayBuild && !hasAdventureModePlaceTagForBlock(debug1.getLevel().getTagManager(), debug4)) {
/*  223 */       return InteractionResult.PASS;
/*      */     }
/*      */     
/*  226 */     Item debug5 = getItem();
/*  227 */     InteractionResult debug6 = debug5.useOn(debug1);
/*  228 */     if (debug2 != null && debug6.consumesAction()) {
/*  229 */       debug2.awardStat(Stats.ITEM_USED.get(debug5));
/*      */     }
/*  231 */     return debug6;
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockState debug1) {
/*  235 */     return getItem().getDestroySpeed(this, debug1);
/*      */   }
/*      */   
/*      */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  239 */     return getItem().use(debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   public ItemStack finishUsingItem(Level debug1, LivingEntity debug2) {
/*  243 */     return getItem().finishUsingItem(this, debug1, debug2);
/*      */   }
/*      */   
/*      */   public CompoundTag save(CompoundTag debug1) {
/*  247 */     ResourceLocation debug2 = Registry.ITEM.getKey(getItem());
/*  248 */     debug1.putString("id", (debug2 == null) ? "minecraft:air" : debug2.toString());
/*  249 */     debug1.putByte("Count", (byte)this.count);
/*  250 */     if (this.tag != null) {
/*  251 */       debug1.put("tag", (Tag)this.tag.copy());
/*      */     }
/*  253 */     return debug1;
/*      */   }
/*      */   
/*      */   public int getMaxStackSize() {
/*  257 */     return getItem().getMaxStackSize();
/*      */   }
/*      */   
/*      */   public boolean isStackable() {
/*  261 */     return (getMaxStackSize() > 1 && (!isDamageableItem() || !isDamaged()));
/*      */   }
/*      */   
/*      */   public boolean isDamageableItem() {
/*  265 */     if (this.emptyCacheFlag || getItem().getMaxDamage() <= 0) {
/*  266 */       return false;
/*      */     }
/*  268 */     CompoundTag debug1 = getTag();
/*  269 */     return (debug1 == null || !debug1.getBoolean("Unbreakable"));
/*      */   }
/*      */   
/*      */   public boolean isDamaged() {
/*  273 */     return (isDamageableItem() && getDamageValue() > 0);
/*      */   }
/*      */   
/*      */   public int getDamageValue() {
/*  277 */     return (this.tag == null) ? 0 : this.tag.getInt("Damage");
/*      */   }
/*      */   
/*      */   public void setDamageValue(int debug1) {
/*  281 */     getOrCreateTag().putInt("Damage", Math.max(0, debug1));
/*      */   }
/*      */   
/*      */   public int getMaxDamage() {
/*  285 */     return getItem().getMaxDamage();
/*      */   }
/*      */   
/*      */   public boolean hurt(int debug1, Random debug2, @Nullable ServerPlayer debug3) {
/*  289 */     if (!isDamageableItem()) {
/*  290 */       return false;
/*      */     }
/*      */     
/*  293 */     if (debug1 > 0) {
/*  294 */       int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, this);
/*      */       
/*  296 */       int debug5 = 0;
/*  297 */       for (int debug6 = 0; i > 0 && debug6 < debug1; debug6++) {
/*  298 */         if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(this, i, debug2)) {
/*  299 */           debug5++;
/*      */         }
/*      */       } 
/*  302 */       debug1 -= debug5;
/*      */       
/*  304 */       if (debug1 <= 0) {
/*  305 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  309 */     if (debug3 != null && debug1 != 0) {
/*  310 */       CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(debug3, this, getDamageValue() + debug1);
/*      */     }
/*      */     
/*  313 */     int debug4 = getDamageValue() + debug1;
/*      */     
/*  315 */     setDamageValue(debug4);
/*      */     
/*  317 */     return (debug4 >= getMaxDamage());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends LivingEntity> void hurtAndBreak(int debug1, T debug2, Consumer<T> debug3) {
/*  323 */     if (((LivingEntity)debug2).level.isClientSide || (debug2 instanceof Player && ((Player)debug2).abilities.instabuild)) {
/*      */       return;
/*      */     }
/*  326 */     if (!isDamageableItem()) {
/*      */       return;
/*      */     }
/*      */     
/*  330 */     if (hurt(debug1, debug2.getRandom(), (debug2 instanceof ServerPlayer) ? (ServerPlayer)debug2 : null)) {
/*  331 */       debug3.accept(debug2);
/*      */       
/*  333 */       Item debug4 = getItem();
/*  334 */       shrink(1);
/*  335 */       if (debug2 instanceof Player) {
/*  336 */         ((Player)debug2).awardStat(Stats.ITEM_BROKEN.get(debug4));
/*      */       }
/*      */ 
/*      */       
/*  340 */       setDamageValue(0);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void hurtEnemy(LivingEntity debug1, Player debug2) {
/*  345 */     Item debug3 = getItem();
/*  346 */     if (debug3.hurtEnemy(this, debug1, (LivingEntity)debug2)) {
/*  347 */       debug2.awardStat(Stats.ITEM_USED.get(debug3));
/*      */     }
/*      */   }
/*      */   
/*      */   public void mineBlock(Level debug1, BlockState debug2, BlockPos debug3, Player debug4) {
/*  352 */     Item debug5 = getItem();
/*  353 */     if (debug5.mineBlock(this, debug1, debug2, debug3, (LivingEntity)debug4)) {
/*  354 */       debug4.awardStat(Stats.ITEM_USED.get(debug5));
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isCorrectToolForDrops(BlockState debug1) {
/*  359 */     return getItem().isCorrectToolForDrops(debug1);
/*      */   }
/*      */   
/*      */   public InteractionResult interactLivingEntity(Player debug1, LivingEntity debug2, InteractionHand debug3) {
/*  363 */     return getItem().interactLivingEntity(this, debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   public ItemStack copy() {
/*  367 */     if (isEmpty()) {
/*  368 */       return EMPTY;
/*      */     }
/*  370 */     ItemStack debug1 = new ItemStack(getItem(), this.count);
/*  371 */     debug1.setPopTime(getPopTime());
/*  372 */     if (this.tag != null) {
/*  373 */       debug1.tag = this.tag.copy();
/*      */     }
/*  375 */     return debug1;
/*      */   }
/*      */   
/*      */   public static boolean tagMatches(ItemStack debug0, ItemStack debug1) {
/*  379 */     if (debug0.isEmpty() && debug1.isEmpty()) {
/*  380 */       return true;
/*      */     }
/*  382 */     if (debug0.isEmpty() || debug1.isEmpty()) {
/*  383 */       return false;
/*      */     }
/*      */     
/*  386 */     if (debug0.tag == null && debug1.tag != null) {
/*  387 */       return false;
/*      */     }
/*  389 */     if (debug0.tag != null && !debug0.tag.equals(debug1.tag)) {
/*  390 */       return false;
/*      */     }
/*  392 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean matches(ItemStack debug0, ItemStack debug1) {
/*  396 */     if (debug0.isEmpty() && debug1.isEmpty()) {
/*  397 */       return true;
/*      */     }
/*  399 */     if (debug0.isEmpty() || debug1.isEmpty()) {
/*  400 */       return false;
/*      */     }
/*  402 */     return debug0.matches(debug1);
/*      */   }
/*      */   
/*      */   private boolean matches(ItemStack debug1) {
/*  406 */     if (this.count != debug1.count) {
/*  407 */       return false;
/*      */     }
/*  409 */     if (getItem() != debug1.getItem()) {
/*  410 */       return false;
/*      */     }
/*  412 */     if (this.tag == null && debug1.tag != null) {
/*  413 */       return false;
/*      */     }
/*  415 */     if (this.tag != null && !this.tag.equals(debug1.tag)) {
/*  416 */       return false;
/*      */     }
/*  418 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean isSame(ItemStack debug0, ItemStack debug1) {
/*  422 */     if (debug0 == debug1) {
/*  423 */       return true;
/*      */     }
/*  425 */     if (!debug0.isEmpty() && !debug1.isEmpty()) {
/*  426 */       return debug0.sameItem(debug1);
/*      */     }
/*  428 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isSameIgnoreDurability(ItemStack debug0, ItemStack debug1) {
/*  432 */     if (debug0 == debug1) {
/*  433 */       return true;
/*      */     }
/*  435 */     if (!debug0.isEmpty() && !debug1.isEmpty()) {
/*  436 */       return debug0.sameItemStackIgnoreDurability(debug1);
/*      */     }
/*  438 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean sameItem(ItemStack debug1) {
/*  449 */     return (!debug1.isEmpty() && getItem() == debug1.getItem());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean sameItemStackIgnoreDurability(ItemStack debug1) {
/*  461 */     if (isDamageableItem()) {
/*  462 */       return (!debug1.isEmpty() && getItem() == debug1.getItem());
/*      */     }
/*  464 */     return sameItem(debug1);
/*      */   }
/*      */   
/*      */   public String getDescriptionId() {
/*  468 */     return getItem().getDescriptionId(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  473 */     return this.count + " " + getItem();
/*      */   }
/*      */   
/*      */   public void inventoryTick(Level debug1, Entity debug2, int debug3, boolean debug4) {
/*  477 */     if (this.popTime > 0) {
/*  478 */       this.popTime--;
/*      */     }
/*  480 */     if (getItem() != null) {
/*  481 */       getItem().inventoryTick(this, debug1, debug2, debug3, debug4);
/*      */     }
/*      */   }
/*      */   
/*      */   public void onCraftedBy(Level debug1, Player debug2, int debug3) {
/*  486 */     debug2.awardStat(Stats.ITEM_CRAFTED.get(getItem()), debug3);
/*  487 */     getItem().onCraftedBy(this, debug1, debug2);
/*      */   }
/*      */   
/*      */   public int getUseDuration() {
/*  491 */     return getItem().getUseDuration(this);
/*      */   }
/*      */   
/*      */   public UseAnim getUseAnimation() {
/*  495 */     return getItem().getUseAnimation(this);
/*      */   }
/*      */   
/*      */   public void releaseUsing(Level debug1, LivingEntity debug2, int debug3) {
/*  499 */     getItem().releaseUsing(this, debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   public boolean useOnRelease() {
/*  503 */     return getItem().useOnRelease(this);
/*      */   }
/*      */   
/*      */   public boolean hasTag() {
/*  507 */     return (!this.emptyCacheFlag && this.tag != null && !this.tag.isEmpty());
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public CompoundTag getTag() {
/*  512 */     return this.tag;
/*      */   }
/*      */   
/*      */   public CompoundTag getOrCreateTag() {
/*  516 */     if (this.tag == null) {
/*  517 */       setTag(new CompoundTag());
/*      */     }
/*      */     
/*  520 */     return this.tag;
/*      */   }
/*      */   
/*      */   public CompoundTag getOrCreateTagElement(String debug1) {
/*  524 */     if (this.tag == null || !this.tag.contains(debug1, 10)) {
/*  525 */       CompoundTag debug2 = new CompoundTag();
/*  526 */       addTagElement(debug1, (Tag)debug2);
/*  527 */       return debug2;
/*      */     } 
/*  529 */     return this.tag.getCompound(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public CompoundTag getTagElement(String debug1) {
/*  534 */     if (this.tag == null || !this.tag.contains(debug1, 10)) {
/*  535 */       return null;
/*      */     }
/*  537 */     return this.tag.getCompound(debug1);
/*      */   }
/*      */   
/*      */   public void removeTagKey(String debug1) {
/*  541 */     if (this.tag != null && this.tag.contains(debug1)) {
/*  542 */       this.tag.remove(debug1);
/*  543 */       if (this.tag.isEmpty()) {
/*  544 */         this.tag = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public ListTag getEnchantmentTags() {
/*  550 */     if (this.tag != null) {
/*  551 */       return this.tag.getList("Enchantments", 10);
/*      */     }
/*  553 */     return new ListTag();
/*      */   }
/*      */   
/*      */   public void setTag(@Nullable CompoundTag debug1) {
/*  557 */     this.tag = debug1;
/*      */     
/*  559 */     if (getItem().canBeDepleted()) {
/*  560 */       setDamageValue(getDamageValue());
/*      */     }
/*      */   }
/*      */   
/*      */   public Component getHoverName() {
/*  565 */     CompoundTag debug1 = getTagElement("display");
/*  566 */     if (debug1 != null && 
/*  567 */       debug1.contains("Name", 8)) {
/*      */       try {
/*  569 */         MutableComponent mutableComponent = Component.Serializer.fromJson(debug1.getString("Name"));
/*  570 */         if (mutableComponent != null) {
/*  571 */           return (Component)mutableComponent;
/*      */         }
/*  573 */         debug1.remove("Name");
/*      */       }
/*  575 */       catch (JsonParseException debug2) {
/*  576 */         debug1.remove("Name");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  581 */     return getItem().getName(this);
/*      */   }
/*      */   
/*      */   public ItemStack setHoverName(@Nullable Component debug1) {
/*  585 */     CompoundTag debug2 = getOrCreateTagElement("display");
/*  586 */     if (debug1 != null) {
/*  587 */       debug2.putString("Name", Component.Serializer.toJson(debug1));
/*      */     } else {
/*  589 */       debug2.remove("Name");
/*      */     } 
/*  591 */     return this;
/*      */   }
/*      */   
/*      */   public void resetHoverName() {
/*  595 */     CompoundTag debug1 = getTagElement("display");
/*  596 */     if (debug1 != null) {
/*  597 */       debug1.remove("Name");
/*      */       
/*  599 */       if (debug1.isEmpty()) {
/*  600 */         removeTagKey("display");
/*      */       }
/*      */     } 
/*      */     
/*  604 */     if (this.tag != null && this.tag.isEmpty()) {
/*  605 */       this.tag = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasCustomHoverName() {
/*  610 */     CompoundTag debug1 = getTagElement("display");
/*  611 */     return (debug1 != null && debug1.contains("Name", 8));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void hideTooltipPart(TooltipPart debug1) {
/*  782 */     CompoundTag debug2 = getOrCreateTag();
/*  783 */     debug2.putInt("HideFlags", debug2.getInt("HideFlags") | debug1.getMask());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFoil() {
/*  822 */     return getItem().isFoil(this);
/*      */   }
/*      */   
/*      */   public Rarity getRarity() {
/*  826 */     return getItem().getRarity(this);
/*      */   }
/*      */   
/*      */   public boolean isEnchantable() {
/*  830 */     if (!getItem().isEnchantable(this)) {
/*  831 */       return false;
/*      */     }
/*  833 */     if (isEnchanted()) {
/*  834 */       return false;
/*      */     }
/*  836 */     return true;
/*      */   }
/*      */   
/*      */   public void enchant(Enchantment debug1, int debug2) {
/*  840 */     getOrCreateTag();
/*  841 */     if (!this.tag.contains("Enchantments", 9)) {
/*  842 */       this.tag.put("Enchantments", (Tag)new ListTag());
/*      */     }
/*  844 */     ListTag debug3 = this.tag.getList("Enchantments", 10);
/*  845 */     CompoundTag debug4 = new CompoundTag();
/*  846 */     debug4.putString("id", String.valueOf(Registry.ENCHANTMENT.getKey(debug1)));
/*  847 */     debug4.putShort("lvl", (short)(byte)debug2);
/*  848 */     debug3.add(debug4);
/*      */   }
/*      */   
/*      */   public boolean isEnchanted() {
/*  852 */     if (this.tag != null && this.tag.contains("Enchantments", 9)) {
/*  853 */       return !this.tag.getList("Enchantments", 10).isEmpty();
/*      */     }
/*  855 */     return false;
/*      */   }
/*      */   
/*      */   public void addTagElement(String debug1, Tag debug2) {
/*  859 */     getOrCreateTag().put(debug1, debug2);
/*      */   }
/*      */   
/*      */   public boolean isFramed() {
/*  863 */     return this.entityRepresentation instanceof ItemFrame;
/*      */   }
/*      */   
/*      */   public void setEntityRepresentation(@Nullable Entity debug1) {
/*  867 */     this.entityRepresentation = debug1;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemFrame getFrame() {
/*  872 */     return (this.entityRepresentation instanceof ItemFrame) ? (ItemFrame)getEntityRepresentation() : null;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity getEntityRepresentation() {
/*  877 */     return !this.emptyCacheFlag ? this.entityRepresentation : null;
/*      */   }
/*      */   
/*      */   public int getBaseRepairCost() {
/*  881 */     if (hasTag() && this.tag.contains("RepairCost", 3)) {
/*  882 */       return this.tag.getInt("RepairCost");
/*      */     }
/*  884 */     return 0;
/*      */   }
/*      */   
/*      */   public void setRepairCost(int debug1) {
/*  888 */     getOrCreateTag().putInt("RepairCost", debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot debug1) {
/*      */     Multimap<Attribute, AttributeModifier> debug2;
/*  894 */     if (hasTag() && this.tag.contains("AttributeModifiers", 9))
/*  895 */     { HashMultimap hashMultimap = HashMultimap.create();
/*  896 */       ListTag debug3 = this.tag.getList("AttributeModifiers", 10);
/*      */       
/*  898 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/*  899 */         CompoundTag debug5 = debug3.getCompound(debug4);
/*  900 */         if (!debug5.contains("Slot", 8) || 
/*  901 */           debug5.getString("Slot").equals(debug1.getName())) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  906 */           Optional<Attribute> debug6 = Registry.ATTRIBUTE.getOptional(ResourceLocation.tryParse(debug5.getString("AttributeName")));
/*  907 */           if (debug6.isPresent()) {
/*      */ 
/*      */ 
/*      */             
/*  911 */             AttributeModifier debug7 = AttributeModifier.load(debug5);
/*  912 */             if (debug7 != null)
/*      */             {
/*      */ 
/*      */               
/*  916 */               if (debug7.getId().getLeastSignificantBits() != 0L && debug7.getId().getMostSignificantBits() != 0L)
/*  917 */                 hashMultimap.put(debug6.get(), debug7);  } 
/*      */           } 
/*      */         } 
/*      */       }  }
/*  921 */     else { debug2 = getItem().getDefaultAttributeModifiers(debug1); }
/*      */ 
/*      */     
/*  924 */     return debug2;
/*      */   }
/*      */   
/*      */   public void addAttributeModifier(Attribute debug1, AttributeModifier debug2, @Nullable EquipmentSlot debug3) {
/*  928 */     getOrCreateTag();
/*  929 */     if (!this.tag.contains("AttributeModifiers", 9)) {
/*  930 */       this.tag.put("AttributeModifiers", (Tag)new ListTag());
/*      */     }
/*  932 */     ListTag debug4 = this.tag.getList("AttributeModifiers", 10);
/*  933 */     CompoundTag debug5 = debug2.save();
/*  934 */     debug5.putString("AttributeName", Registry.ATTRIBUTE.getKey(debug1).toString());
/*  935 */     if (debug3 != null) {
/*  936 */       debug5.putString("Slot", debug3.getName());
/*      */     }
/*  938 */     debug4.add(debug5);
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/*  942 */     MutableComponent debug1 = (new TextComponent("")).append(getHoverName());
/*  943 */     if (hasCustomHoverName()) {
/*  944 */       debug1.withStyle(ChatFormatting.ITALIC);
/*      */     }
/*      */     
/*  947 */     MutableComponent debug2 = ComponentUtils.wrapInSquareBrackets((Component)debug1);
/*      */     
/*  949 */     if (!this.emptyCacheFlag) {
/*  950 */       debug2.withStyle((getRarity()).color).withStyle(debug1 -> debug1.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(this))));
/*      */     }
/*      */     
/*  953 */     return (Component)debug2;
/*      */   }
/*      */   
/*      */   private static boolean areSameBlocks(BlockInWorld debug0, @Nullable BlockInWorld debug1) {
/*  957 */     if (debug1 == null || debug0.getState() != debug1.getState()) {
/*  958 */       return false;
/*      */     }
/*  960 */     if (debug0.getEntity() == null && debug1.getEntity() == null) {
/*  961 */       return true;
/*      */     }
/*  963 */     if (debug0.getEntity() == null || debug1.getEntity() == null) {
/*  964 */       return false;
/*      */     }
/*  966 */     return Objects.equals(debug0.getEntity().save(new CompoundTag()), debug1.getEntity().save(new CompoundTag()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAdventureModeBreakTagForBlock(TagContainer debug1, BlockInWorld debug2) {
/*  974 */     if (areSameBlocks(debug2, this.cachedBreakBlock)) {
/*  975 */       return this.cachedBreakBlockResult;
/*      */     }
/*      */     
/*  978 */     this.cachedBreakBlock = debug2;
/*      */     
/*  980 */     if (hasTag() && this.tag.contains("CanDestroy", 9)) {
/*  981 */       ListTag debug3 = this.tag.getList("CanDestroy", 8);
/*  982 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/*  983 */         String debug5 = debug3.getString(debug4);
/*      */         try {
/*  985 */           Predicate<BlockInWorld> debug6 = BlockPredicateArgument.blockPredicate().parse(new StringReader(debug5)).create(debug1);
/*  986 */           if (debug6.test(debug2)) {
/*  987 */             this.cachedBreakBlockResult = true;
/*  988 */             return true;
/*      */           } 
/*  990 */         } catch (CommandSyntaxException commandSyntaxException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  995 */     this.cachedBreakBlockResult = false;
/*  996 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAdventureModePlaceTagForBlock(TagContainer debug1, BlockInWorld debug2) {
/* 1004 */     if (areSameBlocks(debug2, this.cachedPlaceBlock)) {
/* 1005 */       return this.cachedPlaceBlockResult;
/*      */     }
/*      */     
/* 1008 */     this.cachedPlaceBlock = debug2;
/*      */     
/* 1010 */     if (hasTag() && this.tag.contains("CanPlaceOn", 9)) {
/* 1011 */       ListTag debug3 = this.tag.getList("CanPlaceOn", 8);
/* 1012 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 1013 */         String debug5 = debug3.getString(debug4);
/*      */         try {
/* 1015 */           Predicate<BlockInWorld> debug6 = BlockPredicateArgument.blockPredicate().parse(new StringReader(debug5)).create(debug1);
/* 1016 */           if (debug6.test(debug2)) {
/* 1017 */             this.cachedPlaceBlockResult = true;
/* 1018 */             return true;
/*      */           } 
/* 1020 */         } catch (CommandSyntaxException commandSyntaxException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1025 */     this.cachedPlaceBlockResult = false;
/* 1026 */     return false;
/*      */   }
/*      */   
/*      */   public int getPopTime() {
/* 1030 */     return this.popTime;
/*      */   }
/*      */   
/*      */   public void setPopTime(int debug1) {
/* 1034 */     this.popTime = debug1;
/*      */   }
/*      */   
/*      */   public int getCount() {
/* 1038 */     return this.emptyCacheFlag ? 0 : this.count;
/*      */   }
/*      */   
/*      */   public void setCount(int debug1) {
/* 1042 */     this.count = debug1;
/*      */     
/* 1044 */     updateEmptyCacheFlag();
/*      */   }
/*      */   
/*      */   public void grow(int debug1) {
/* 1048 */     setCount(this.count + debug1);
/*      */   }
/*      */   
/*      */   public void shrink(int debug1) {
/* 1052 */     grow(-debug1);
/*      */   }
/*      */   
/*      */   public void onUseTick(Level debug1, LivingEntity debug2, int debug3) {
/* 1056 */     getItem().onUseTick(debug1, debug2, this, debug3);
/*      */   }
/*      */   
/*      */   public boolean isEdible() {
/* 1060 */     return getItem().isEdible();
/*      */   }
/*      */   
/*      */   public SoundEvent getDrinkingSound() {
/* 1064 */     return getItem().getDrinkingSound();
/*      */   }
/*      */   
/*      */   public SoundEvent getEatingSound() {
/* 1068 */     return getItem().getEatingSound();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */