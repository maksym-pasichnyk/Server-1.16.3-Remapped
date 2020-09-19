/*     */ package net.minecraft.data.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.FrameType;
/*     */ import net.minecraft.advancements.RequirementsStrategy;
/*     */ import net.minecraft.advancements.critereon.BlockPredicate;
/*     */ import net.minecraft.advancements.critereon.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.critereon.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.critereon.DistancePredicate;
/*     */ import net.minecraft.advancements.critereon.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.critereon.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.critereon.EntityPredicate;
/*     */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemPickedUpByEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemPredicate;
/*     */ import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.KilledTrigger;
/*     */ import net.minecraft.advancements.critereon.LocationPredicate;
/*     */ import net.minecraft.advancements.critereon.LocationTrigger;
/*     */ import net.minecraft.advancements.critereon.LootTableTrigger;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.advancements.critereon.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.critereon.NetherTravelTrigger;
/*     */ import net.minecraft.advancements.critereon.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.critereon.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.critereon.SummonedEntityTrigger;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public class NetherAdvancements implements Consumer<Consumer<Advancement>> {
/*  59 */   private static final List<ResourceKey<Biome>> EXPLORABLE_BIOMES = (List<ResourceKey<Biome>>)ImmutableList.of(Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST, Biomes.BASALT_DELTAS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final EntityPredicate.Composite DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE = EntityPredicate.Composite.create(new LootItemCondition[] {
/*  68 */         LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of((ItemLike)Items.GOLDEN_HELMET).build()).build())).invert().build(), 
/*  69 */         LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of((ItemLike)Items.GOLDEN_CHESTPLATE).build()).build())).invert().build(), 
/*  70 */         LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of((ItemLike)Items.GOLDEN_LEGGINGS).build()).build())).invert().build(), 
/*  71 */         LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of((ItemLike)Items.GOLDEN_BOOTS).build()).build())).invert().build()
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(Consumer<Advancement> debug1) {
/*  79 */     Advancement debug2 = Advancement.Builder.advancement().display((ItemLike)Blocks.RED_NETHER_BRICKS, (Component)new TranslatableComponent("advancements.nether.root.title"), (Component)new TranslatableComponent("advancements.nether.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/nether.png"), FrameType.TASK, false, false, false).addCriterion("entered_nether", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(debug1, "nether/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Advancement debug3 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.FIRE_CHARGE, (Component)new TranslatableComponent("advancements.nether.return_to_sender.title"), (Component)new TranslatableComponent("advancements.nether.return_to_sender.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("killed_ghast", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.GHAST), DamageSourcePredicate.Builder.damageType().isProjectile(Boolean.valueOf(true)).direct(EntityPredicate.Builder.entity().of(EntityType.FIREBALL)))).save(debug1, "nether/return_to_sender");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     Advancement debug4 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Blocks.NETHER_BRICKS, (Component)new TranslatableComponent("advancements.nether.find_fortress.title"), (Component)new TranslatableComponent("advancements.nether.find_fortress.description"), null, FrameType.TASK, true, true, false).addCriterion("fortress", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.NETHER_BRIDGE))).save(debug1, "nether/find_fortress");
/*     */     
/*  94 */     Advancement.Builder.advancement()
/*  95 */       .parent(debug2)
/*  96 */       .display((ItemLike)Items.MAP, (Component)new TranslatableComponent("advancements.nether.fast_travel.title"), (Component)new TranslatableComponent("advancements.nether.fast_travel.description"), null, FrameType.CHALLENGE, true, true, false)
/*  97 */       .rewards(AdvancementRewards.Builder.experience(100))
/*  98 */       .addCriterion("travelled", (CriterionTriggerInstance)NetherTravelTrigger.TriggerInstance.travelledThroughNether(DistancePredicate.horizontal(MinMaxBounds.Floats.atLeast(7000.0F))))
/*  99 */       .save(debug1, "nether/fast_travel");
/*     */     
/* 101 */     Advancement.Builder.advancement()
/* 102 */       .parent(debug3)
/* 103 */       .display((ItemLike)Items.GHAST_TEAR, (Component)new TranslatableComponent("advancements.nether.uneasy_alliance.title"), (Component)new TranslatableComponent("advancements.nether.uneasy_alliance.description"), null, FrameType.CHALLENGE, true, true, false)
/* 104 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 105 */       .addCriterion("killed_ghast", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.GHAST).located(LocationPredicate.inDimension(Level.OVERWORLD))))
/* 106 */       .save(debug1, "nether/uneasy_alliance");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     Advancement debug5 = Advancement.Builder.advancement().parent(debug4).display((ItemLike)Blocks.WITHER_SKELETON_SKULL, (Component)new TranslatableComponent("advancements.nether.get_wither_skull.title"), (Component)new TranslatableComponent("advancements.nether.get_wither_skull.description"), null, FrameType.TASK, true, true, false).addCriterion("wither_skull", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.WITHER_SKELETON_SKULL })).save(debug1, "nether/get_wither_skull");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     Advancement debug6 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.NETHER_STAR, (Component)new TranslatableComponent("advancements.nether.summon_wither.title"), (Component)new TranslatableComponent("advancements.nether.summon_wither.description"), null, FrameType.TASK, true, true, false).addCriterion("summoned", (CriterionTriggerInstance)SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.WITHER))).save(debug1, "nether/summon_wither");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     Advancement debug7 = Advancement.Builder.advancement().parent(debug4).display((ItemLike)Items.BLAZE_ROD, (Component)new TranslatableComponent("advancements.nether.obtain_blaze_rod.title"), (Component)new TranslatableComponent("advancements.nether.obtain_blaze_rod.description"), null, FrameType.TASK, true, true, false).addCriterion("blaze_rod", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.BLAZE_ROD })).save(debug1, "nether/obtain_blaze_rod");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     Advancement debug8 = Advancement.Builder.advancement().parent(debug6).display((ItemLike)Blocks.BEACON, (Component)new TranslatableComponent("advancements.nether.create_beacon.title"), (Component)new TranslatableComponent("advancements.nether.create_beacon.description"), null, FrameType.TASK, true, true, false).addCriterion("beacon", (CriterionTriggerInstance)ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.atLeast(1))).save(debug1, "nether/create_beacon");
/*     */     
/* 132 */     Advancement.Builder.advancement()
/* 133 */       .parent(debug8)
/* 134 */       .display((ItemLike)Blocks.BEACON, (Component)new TranslatableComponent("advancements.nether.create_full_beacon.title"), (Component)new TranslatableComponent("advancements.nether.create_full_beacon.description"), null, FrameType.GOAL, true, true, false)
/* 135 */       .addCriterion("beacon", (CriterionTriggerInstance)ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.exactly(4)))
/* 136 */       .save(debug1, "nether/create_full_beacon");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Advancement debug9 = Advancement.Builder.advancement().parent(debug7).display((ItemLike)Items.POTION, (Component)new TranslatableComponent("advancements.nether.brew_potion.title"), (Component)new TranslatableComponent("advancements.nether.brew_potion.description"), null, FrameType.TASK, true, true, false).addCriterion("potion", (CriterionTriggerInstance)BrewedPotionTrigger.TriggerInstance.brewedPotion()).save(debug1, "nether/brew_potion");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Advancement debug10 = Advancement.Builder.advancement().parent(debug9).display((ItemLike)Items.MILK_BUCKET, (Component)new TranslatableComponent("advancements.nether.all_potions.title"), (Component)new TranslatableComponent("advancements.nether.all_potions.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("all_effects", (CriterionTriggerInstance)EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(MobEffects.MOVEMENT_SPEED).and(MobEffects.MOVEMENT_SLOWDOWN).and(MobEffects.DAMAGE_BOOST).and(MobEffects.JUMP).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.SLOW_FALLING).and(MobEffects.DAMAGE_RESISTANCE))).save(debug1, "nether/all_potions");
/*     */     
/* 151 */     Advancement.Builder.advancement()
/* 152 */       .parent(debug10)
/* 153 */       .display((ItemLike)Items.BUCKET, (Component)new TranslatableComponent("advancements.nether.all_effects.title"), (Component)new TranslatableComponent("advancements.nether.all_effects.description"), null, FrameType.CHALLENGE, true, true, true)
/* 154 */       .rewards(AdvancementRewards.Builder.experience(1000))
/* 155 */       .addCriterion("all_effects", (CriterionTriggerInstance)EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(MobEffects.MOVEMENT_SPEED).and(MobEffects.MOVEMENT_SLOWDOWN).and(MobEffects.DAMAGE_BOOST).and(MobEffects.JUMP).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.WITHER).and(MobEffects.DIG_SPEED).and(MobEffects.DIG_SLOWDOWN).and(MobEffects.LEVITATION).and(MobEffects.GLOWING).and(MobEffects.ABSORPTION).and(MobEffects.HUNGER).and(MobEffects.CONFUSION).and(MobEffects.DAMAGE_RESISTANCE).and(MobEffects.SLOW_FALLING).and(MobEffects.CONDUIT_POWER).and(MobEffects.DOLPHINS_GRACE).and(MobEffects.BLINDNESS).and(MobEffects.BAD_OMEN).and(MobEffects.HERO_OF_THE_VILLAGE)))
/* 156 */       .save(debug1, "nether/all_effects");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     Advancement debug11 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.ANCIENT_DEBRIS, (Component)new TranslatableComponent("advancements.nether.obtain_ancient_debris.title"), (Component)new TranslatableComponent("advancements.nether.obtain_ancient_debris.description"), null, FrameType.TASK, true, true, false).addCriterion("ancient_debris", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ANCIENT_DEBRIS })).save(debug1, "nether/obtain_ancient_debris");
/*     */     
/* 164 */     Advancement.Builder.advancement()
/* 165 */       .parent(debug11)
/* 166 */       .display((ItemLike)Items.NETHERITE_CHESTPLATE, (Component)new TranslatableComponent("advancements.nether.netherite_armor.title"), (Component)new TranslatableComponent("advancements.nether.netherite_armor.description"), null, FrameType.CHALLENGE, true, true, false)
/* 167 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 168 */       .addCriterion("netherite_armor", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HELMET, (ItemLike)Items.NETHERITE_CHESTPLATE, (ItemLike)Items.NETHERITE_LEGGINGS, (ItemLike)Items.NETHERITE_BOOTS
/* 169 */           })).save(debug1, "nether/netherite_armor");
/*     */     
/* 171 */     Advancement.Builder.advancement()
/* 172 */       .parent(debug11)
/* 173 */       .display((ItemLike)Items.LODESTONE, (Component)new TranslatableComponent("advancements.nether.use_lodestone.title"), (Component)new TranslatableComponent("advancements.nether.use_lodestone.description"), null, FrameType.TASK, true, true, false)
/* 174 */       .addCriterion("use_lodestone", (CriterionTriggerInstance)ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.LODESTONE).build()), ItemPredicate.Builder.item().of((ItemLike)Items.COMPASS)))
/* 175 */       .save(debug1, "nether/use_lodestone");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     Advancement debug12 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.CRYING_OBSIDIAN, (Component)new TranslatableComponent("advancements.nether.obtain_crying_obsidian.title"), (Component)new TranslatableComponent("advancements.nether.obtain_crying_obsidian.description"), null, FrameType.TASK, true, true, false).addCriterion("crying_obsidian", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.CRYING_OBSIDIAN })).save(debug1, "nether/obtain_crying_obsidian");
/*     */     
/* 183 */     Advancement.Builder.advancement()
/* 184 */       .parent(debug12)
/* 185 */       .display((ItemLike)Items.RESPAWN_ANCHOR, (Component)new TranslatableComponent("advancements.nether.charge_respawn_anchor.title"), (Component)new TranslatableComponent("advancements.nether.charge_respawn_anchor.description"), null, FrameType.TASK, true, true, false)
/* 186 */       .addCriterion("charge_respawn_anchor", (CriterionTriggerInstance)ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.RESPAWN_ANCHOR).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)RespawnAnchorBlock.CHARGE, 4).build()).build()), ItemPredicate.Builder.item().of((ItemLike)Blocks.GLOWSTONE)))
/* 187 */       .save(debug1, "nether/charge_respawn_anchor");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     Advancement debug13 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK, (Component)new TranslatableComponent("advancements.nether.ride_strider.title"), (Component)new TranslatableComponent("advancements.nether.ride_strider.description"), null, FrameType.TASK, true, true, false).addCriterion("used_warped_fungus_on_a_stick", (CriterionTriggerInstance)ItemDurabilityTrigger.TriggerInstance.changedDurability(EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity().of(EntityType.STRIDER).build()).build()), ItemPredicate.Builder.item().of((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK).build(), MinMaxBounds.Ints.ANY)).save(debug1, "nether/ride_strider");
/*     */     
/* 198 */     AdventureAdvancements.addBiomes(Advancement.Builder.advancement(), EXPLORABLE_BIOMES)
/* 199 */       .parent(debug13)
/* 200 */       .display((ItemLike)Items.NETHERITE_BOOTS, (Component)new TranslatableComponent("advancements.nether.explore_nether.title"), (Component)new TranslatableComponent("advancements.nether.explore_nether.description"), null, FrameType.CHALLENGE, true, true, false)
/* 201 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 202 */       .save(debug1, "nether/explore_nether");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     Advancement debug14 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.POLISHED_BLACKSTONE_BRICKS, (Component)new TranslatableComponent("advancements.nether.find_bastion.title"), (Component)new TranslatableComponent("advancements.nether.find_bastion.description"), null, FrameType.TASK, true, true, false).addCriterion("bastion", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.BASTION_REMNANT))).save(debug1, "nether/find_bastion");
/*     */     
/* 210 */     Advancement.Builder.advancement()
/* 211 */       .parent(debug14)
/* 212 */       .display((ItemLike)Blocks.CHEST, (Component)new TranslatableComponent("advancements.nether.loot_bastion.title"), (Component)new TranslatableComponent("advancements.nether.loot_bastion.description"), null, FrameType.TASK, true, true, false)
/* 213 */       .requirements(RequirementsStrategy.OR)
/* 214 */       .addCriterion("loot_bastion_other", (CriterionTriggerInstance)LootTableTrigger.TriggerInstance.lootTableUsed(new ResourceLocation("minecraft:chests/bastion_other")))
/* 215 */       .addCriterion("loot_bastion_treasure", (CriterionTriggerInstance)LootTableTrigger.TriggerInstance.lootTableUsed(new ResourceLocation("minecraft:chests/bastion_treasure")))
/* 216 */       .addCriterion("loot_bastion_hoglin_stable", (CriterionTriggerInstance)LootTableTrigger.TriggerInstance.lootTableUsed(new ResourceLocation("minecraft:chests/bastion_hoglin_stable")))
/* 217 */       .addCriterion("loot_bastion_bridge", (CriterionTriggerInstance)LootTableTrigger.TriggerInstance.lootTableUsed(new ResourceLocation("minecraft:chests/bastion_bridge")))
/* 218 */       .save(debug1, "nether/loot_bastion");
/*     */     
/* 220 */     Advancement.Builder.advancement()
/* 221 */       .parent(debug2)
/* 222 */       .requirements(RequirementsStrategy.OR)
/* 223 */       .display((ItemLike)Items.GOLD_INGOT, (Component)new TranslatableComponent("advancements.nether.distract_piglin.title"), (Component)new TranslatableComponent("advancements.nether.distract_piglin.description"), null, FrameType.TASK, true, true, false)
/* 224 */       .addCriterion("distract_piglin", (CriterionTriggerInstance)ItemPickedUpByEntityTrigger.TriggerInstance.itemPickedUpByEntity(DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE, 
/*     */           
/* 226 */           ItemPredicate.Builder.item().of((Tag)ItemTags.PIGLIN_LOVED), 
/* 227 */           EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)).build()).build())))
/*     */       
/* 229 */       .addCriterion("distract_piglin_directly", (CriterionTriggerInstance)PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE, 
/*     */           
/* 231 */           ItemPredicate.Builder.item().of((ItemLike)PiglinAi.BARTERING_ITEM), 
/* 232 */           EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)).build()).build())))
/*     */       
/* 234 */       .save(debug1, "nether/distract_piglin");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\NetherAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */