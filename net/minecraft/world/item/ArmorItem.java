/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.BlockSource;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ArmorItem extends Item implements Wearable {
/*  27 */   private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[] {
/*  28 */       UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), 
/*  29 */       UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), 
/*  30 */       UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), 
/*  31 */       UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
/*     */     };
/*     */   
/*  34 */   public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = (DispenseItemBehavior)new DefaultDispenseItemBehavior()
/*     */     {
/*     */       protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/*  37 */         return ArmorItem.dispenseArmor(debug1, debug2) ? debug2 : super.execute(debug1, debug2);
/*     */       }
/*     */     };
/*     */   protected final EquipmentSlot slot;
/*     */   public static boolean dispenseArmor(BlockSource debug0, ItemStack debug1) {
/*  42 */     BlockPos debug2 = debug0.getPos().relative((Direction)debug0.getBlockState().getValue((Property)DispenserBlock.FACING));
/*     */     
/*  44 */     List<LivingEntity> debug3 = debug0.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(debug2), EntitySelector.NO_SPECTATORS.and((Predicate)new EntitySelector.MobCanWearArmorEntitySelector(debug1)));
/*     */     
/*  46 */     if (debug3.isEmpty()) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     LivingEntity debug4 = debug3.get(0);
/*  51 */     EquipmentSlot debug5 = Mob.getEquipmentSlotForItem(debug1);
/*     */     
/*  53 */     ItemStack debug6 = debug1.split(1);
/*  54 */     debug4.setItemSlot(debug5, debug6);
/*  55 */     if (debug4 instanceof Mob) {
/*  56 */       ((Mob)debug4).setDropChance(debug5, 2.0F);
/*  57 */       ((Mob)debug4).setPersistenceRequired();
/*     */     } 
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int defense;
/*     */   
/*     */   private final float toughness;
/*     */   protected final float knockbackResistance;
/*     */   protected final ArmorMaterial material;
/*     */   private final Multimap<Attribute, AttributeModifier> defaultModifiers;
/*     */   
/*     */   public ArmorItem(ArmorMaterial debug1, EquipmentSlot debug2, Item.Properties debug3) {
/*  71 */     super(debug3.defaultDurability(debug1.getDurabilityForSlot(debug2)));
/*  72 */     this.material = debug1;
/*  73 */     this.slot = debug2;
/*  74 */     this.defense = debug1.getDefenseForSlot(debug2);
/*  75 */     this.toughness = debug1.getToughness();
/*  76 */     this.knockbackResistance = debug1.getKnockbackResistance();
/*     */     
/*  78 */     DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
/*     */     
/*  80 */     ImmutableMultimap.Builder<Attribute, AttributeModifier> debug4 = ImmutableMultimap.builder();
/*     */ 
/*     */     
/*  83 */     UUID debug5 = ARMOR_MODIFIER_UUID_PER_SLOT[debug2.getIndex()];
/*  84 */     debug4.put(Attributes.ARMOR, new AttributeModifier(debug5, "Armor modifier", this.defense, AttributeModifier.Operation.ADDITION));
/*  85 */     debug4.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(debug5, "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));
/*     */     
/*  87 */     if (debug1 == ArmorMaterials.NETHERITE) {
/*  88 */       debug4.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(debug5, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION));
/*     */     }
/*     */     
/*  91 */     this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)debug4.build();
/*     */   }
/*     */   
/*     */   public EquipmentSlot getSlot() {
/*  95 */     return this.slot;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEnchantmentValue() {
/* 100 */     return this.material.getEnchantmentValue();
/*     */   }
/*     */   
/*     */   public ArmorMaterial getMaterial() {
/* 104 */     return this.material;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidRepairItem(ItemStack debug1, ItemStack debug2) {
/* 109 */     return (this.material.getRepairIngredient().test(debug2) || super.isValidRepairItem(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 114 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 115 */     EquipmentSlot debug5 = Mob.getEquipmentSlotForItem(debug4);
/* 116 */     ItemStack debug6 = debug2.getItemBySlot(debug5);
/*     */     
/* 118 */     if (debug6.isEmpty()) {
/* 119 */       debug2.setItemSlot(debug5, debug4.copy());
/* 120 */       debug4.setCount(0);
/* 121 */       return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*     */     } 
/*     */     
/* 124 */     return InteractionResultHolder.fail(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot debug1) {
/* 129 */     if (debug1 == this.slot) {
/* 130 */       return this.defaultModifiers;
/*     */     }
/* 132 */     return super.getDefaultAttributeModifiers(debug1);
/*     */   }
/*     */   
/*     */   public int getDefense() {
/* 136 */     return this.defense;
/*     */   }
/*     */   
/*     */   public float getToughness() {
/* 140 */     return this.toughness;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ArmorItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */