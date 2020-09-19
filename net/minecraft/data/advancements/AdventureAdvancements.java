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
/*     */ import net.minecraft.advancements.critereon.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.critereon.DamagePredicate;
/*     */ import net.minecraft.advancements.critereon.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.critereon.DistancePredicate;
/*     */ import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.critereon.EntityPredicate;
/*     */ import net.minecraft.advancements.critereon.KilledByCrossbowTrigger;
/*     */ import net.minecraft.advancements.critereon.KilledTrigger;
/*     */ import net.minecraft.advancements.critereon.LocationPredicate;
/*     */ import net.minecraft.advancements.critereon.LocationTrigger;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.critereon.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.TradeTrigger;
/*     */ import net.minecraft.advancements.critereon.UsedTotemTrigger;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdventureAdvancements
/*     */   implements Consumer<Consumer<Advancement>>
/*     */ {
/*  50 */   private static final List<ResourceKey<Biome>> EXPLORABLE_BIOMES = (List<ResourceKey<Biome>>)ImmutableList.of(Biomes.BIRCH_FOREST_HILLS, Biomes.RIVER, Biomes.SWAMP, Biomes.DESERT, Biomes.WOODED_HILLS, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.BADLANDS, Biomes.FOREST, Biomes.STONE_SHORE, Biomes.SNOWY_TUNDRA, Biomes.TAIGA_HILLS, (Object[])new ResourceKey[] { Biomes.SNOWY_MOUNTAINS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.SAVANNA, Biomes.PLAINS, Biomes.FROZEN_RIVER, Biomes.GIANT_TREE_TAIGA, Biomes.SNOWY_BEACH, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.MUSHROOM_FIELD_SHORE, Biomes.MOUNTAINS, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.BEACH, Biomes.SAVANNA_PLATEAU, Biomes.SNOWY_TAIGA_HILLS, Biomes.BADLANDS_PLATEAU, Biomes.DARK_FOREST, Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.MUSHROOM_FIELDS, Biomes.WOODED_MOUNTAINS, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE_HILLS });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[] { EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WITHER, EntityType.ZOGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(Consumer<Advancement> debug1) {
/* 140 */     Advancement debug2 = Advancement.Builder.advancement().display((ItemLike)Items.MAP, (Component)new TranslatableComponent("advancements.adventure.root.title"), (Component)new TranslatableComponent("advancements.adventure.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"), FrameType.TASK, false, false, false).requirements(RequirementsStrategy.OR).addCriterion("killed_something", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity()).addCriterion("killed_by_something", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.entityKilledPlayer()).save(debug1, "adventure/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     Advancement debug3 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Blocks.RED_BED, (Component)new TranslatableComponent("advancements.adventure.sleep_in_bed.title"), (Component)new TranslatableComponent("advancements.adventure.sleep_in_bed.description"), null, FrameType.TASK, true, true, false).addCriterion("slept_in_bed", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.sleptInBed()).save(debug1, "adventure/sleep_in_bed");
/*     */     
/* 148 */     addBiomes(Advancement.Builder.advancement(), EXPLORABLE_BIOMES)
/* 149 */       .parent(debug3)
/* 150 */       .display((ItemLike)Items.DIAMOND_BOOTS, (Component)new TranslatableComponent("advancements.adventure.adventuring_time.title"), (Component)new TranslatableComponent("advancements.adventure.adventuring_time.description"), null, FrameType.CHALLENGE, true, true, false)
/* 151 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 152 */       .save(debug1, "adventure/adventuring_time");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     Advancement debug4 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.EMERALD, (Component)new TranslatableComponent("advancements.adventure.trade.title"), (Component)new TranslatableComponent("advancements.adventure.trade.description"), null, FrameType.TASK, true, true, false).addCriterion("traded", (CriterionTriggerInstance)TradeTrigger.TriggerInstance.tradedWithVillager()).save(debug1, "adventure/trade");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     Advancement debug5 = addMobsToKill(Advancement.Builder.advancement()).parent(debug2).display((ItemLike)Items.IRON_SWORD, (Component)new TranslatableComponent("advancements.adventure.kill_a_mob.title"), (Component)new TranslatableComponent("advancements.adventure.kill_a_mob.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).save(debug1, "adventure/kill_a_mob");
/*     */     
/* 166 */     addMobsToKill(Advancement.Builder.advancement())
/* 167 */       .parent(debug5)
/* 168 */       .display((ItemLike)Items.DIAMOND_SWORD, (Component)new TranslatableComponent("advancements.adventure.kill_all_mobs.title"), (Component)new TranslatableComponent("advancements.adventure.kill_all_mobs.description"), null, FrameType.CHALLENGE, true, true, false)
/* 169 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 170 */       .save(debug1, "adventure/kill_all_mobs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     Advancement debug6 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.BOW, (Component)new TranslatableComponent("advancements.adventure.shoot_arrow.title"), (Component)new TranslatableComponent("advancements.adventure.shoot_arrow.description"), null, FrameType.TASK, true, true, false).addCriterion("shot_arrow", (CriterionTriggerInstance)PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(Boolean.valueOf(true)).direct(EntityPredicate.Builder.entity().of((Tag)EntityTypeTags.ARROWS))))).save(debug1, "adventure/shoot_arrow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     Advancement debug7 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.TRIDENT, (Component)new TranslatableComponent("advancements.adventure.throw_trident.title"), (Component)new TranslatableComponent("advancements.adventure.throw_trident.description"), null, FrameType.TASK, true, true, false).addCriterion("shot_trident", (CriterionTriggerInstance)PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(Boolean.valueOf(true)).direct(EntityPredicate.Builder.entity().of(EntityType.TRIDENT))))).save(debug1, "adventure/throw_trident");
/*     */     
/* 184 */     Advancement.Builder.advancement()
/* 185 */       .parent(debug7)
/* 186 */       .display((ItemLike)Items.TRIDENT, (Component)new TranslatableComponent("advancements.adventure.very_very_frightening.title"), (Component)new TranslatableComponent("advancements.adventure.very_very_frightening.description"), null, FrameType.TASK, true, true, false)
/* 187 */       .addCriterion("struck_villager", (CriterionTriggerInstance)ChanneledLightningTrigger.TriggerInstance.channeledLightning(new EntityPredicate[] { EntityPredicate.Builder.entity().of(EntityType.VILLAGER).build()
/* 188 */           })).save(debug1, "adventure/very_very_frightening");
/*     */     
/* 190 */     Advancement.Builder.advancement()
/* 191 */       .parent(debug4)
/* 192 */       .display((ItemLike)Blocks.CARVED_PUMPKIN, (Component)new TranslatableComponent("advancements.adventure.summon_iron_golem.title"), (Component)new TranslatableComponent("advancements.adventure.summon_iron_golem.description"), null, FrameType.GOAL, true, true, false)
/* 193 */       .addCriterion("summoned_golem", (CriterionTriggerInstance)SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.IRON_GOLEM)))
/* 194 */       .save(debug1, "adventure/summon_iron_golem");
/*     */     
/* 196 */     Advancement.Builder.advancement()
/* 197 */       .parent(debug6)
/* 198 */       .display((ItemLike)Items.ARROW, (Component)new TranslatableComponent("advancements.adventure.sniper_duel.title"), (Component)new TranslatableComponent("advancements.adventure.sniper_duel.description"), null, FrameType.CHALLENGE, true, true, false)
/* 199 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 200 */       .addCriterion("killed_skeleton", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.SKELETON).distance(DistancePredicate.horizontal(MinMaxBounds.Floats.atLeast(50.0F))), DamageSourcePredicate.Builder.damageType().isProjectile(Boolean.valueOf(true))))
/* 201 */       .save(debug1, "adventure/sniper_duel");
/*     */     
/* 203 */     Advancement.Builder.advancement()
/* 204 */       .parent(debug5)
/* 205 */       .display((ItemLike)Items.TOTEM_OF_UNDYING, (Component)new TranslatableComponent("advancements.adventure.totem_of_undying.title"), (Component)new TranslatableComponent("advancements.adventure.totem_of_undying.description"), null, FrameType.GOAL, true, true, false)
/* 206 */       .addCriterion("used_totem", (CriterionTriggerInstance)UsedTotemTrigger.TriggerInstance.usedTotem((ItemLike)Items.TOTEM_OF_UNDYING))
/* 207 */       .save(debug1, "adventure/totem_of_undying");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Advancement debug8 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.CROSSBOW, (Component)new TranslatableComponent("advancements.adventure.ol_betsy.title"), (Component)new TranslatableComponent("advancements.adventure.ol_betsy.description"), null, FrameType.TASK, true, true, false).addCriterion("shot_crossbow", (CriterionTriggerInstance)ShotCrossbowTrigger.TriggerInstance.shotCrossbow((ItemLike)Items.CROSSBOW)).save(debug1, "adventure/ol_betsy");
/*     */     
/* 215 */     Advancement.Builder.advancement()
/* 216 */       .parent(debug8)
/* 217 */       .display((ItemLike)Items.CROSSBOW, (Component)new TranslatableComponent("advancements.adventure.whos_the_pillager_now.title"), (Component)new TranslatableComponent("advancements.adventure.whos_the_pillager_now.description"), null, FrameType.TASK, true, true, false)
/* 218 */       .addCriterion("kill_pillager", (CriterionTriggerInstance)KilledByCrossbowTrigger.TriggerInstance.crossbowKilled(new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of(EntityType.PILLAGER)
/* 219 */           })).save(debug1, "adventure/whos_the_pillager_now");
/*     */     
/* 221 */     Advancement.Builder.advancement()
/* 222 */       .parent(debug8)
/* 223 */       .display((ItemLike)Items.CROSSBOW, (Component)new TranslatableComponent("advancements.adventure.two_birds_one_arrow.title"), (Component)new TranslatableComponent("advancements.adventure.two_birds_one_arrow.description"), null, FrameType.CHALLENGE, true, true, false)
/* 224 */       .rewards(AdvancementRewards.Builder.experience(65))
/* 225 */       .addCriterion("two_birds", (CriterionTriggerInstance)KilledByCrossbowTrigger.TriggerInstance.crossbowKilled(new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of(EntityType.PHANTOM), EntityPredicate.Builder.entity().of(EntityType.PHANTOM)
/* 226 */           })).save(debug1, "adventure/two_birds_one_arrow");
/*     */     
/* 228 */     Advancement.Builder.advancement()
/* 229 */       .parent(debug8)
/* 230 */       .display((ItemLike)Items.CROSSBOW, (Component)new TranslatableComponent("advancements.adventure.arbalistic.title"), (Component)new TranslatableComponent("advancements.adventure.arbalistic.description"), null, FrameType.CHALLENGE, true, true, true)
/* 231 */       .rewards(AdvancementRewards.Builder.experience(85))
/* 232 */       .addCriterion("arbalistic", (CriterionTriggerInstance)KilledByCrossbowTrigger.TriggerInstance.crossbowKilled(MinMaxBounds.Ints.exactly(5)))
/* 233 */       .save(debug1, "adventure/arbalistic");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     Advancement debug9 = Advancement.Builder.advancement().parent(debug2).display(Raid.getLeaderBannerInstance(), (Component)new TranslatableComponent("advancements.adventure.voluntary_exile.title"), (Component)new TranslatableComponent("advancements.adventure.voluntary_exile.description"), null, FrameType.TASK, true, true, true).addCriterion("voluntary_exile", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((Tag)EntityTypeTags.RAIDERS).equipment(EntityEquipmentPredicate.CAPTAIN))).save(debug1, "adventure/voluntary_exile");
/*     */     
/* 241 */     Advancement.Builder.advancement()
/* 242 */       .parent(debug9)
/* 243 */       .display(Raid.getLeaderBannerInstance(), (Component)new TranslatableComponent("advancements.adventure.hero_of_the_village.title"), (Component)new TranslatableComponent("advancements.adventure.hero_of_the_village.description"), null, FrameType.CHALLENGE, true, true, true)
/* 244 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 245 */       .addCriterion("hero_of_the_village", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.raidWon())
/* 246 */       .save(debug1, "adventure/hero_of_the_village");
/*     */     
/* 248 */     Advancement.Builder.advancement()
/* 249 */       .parent(debug2)
/* 250 */       .display((ItemLike)Blocks.HONEY_BLOCK.asItem(), (Component)new TranslatableComponent("advancements.adventure.honey_block_slide.title"), (Component)new TranslatableComponent("advancements.adventure.honey_block_slide.description"), null, FrameType.TASK, true, true, false)
/* 251 */       .addCriterion("honey_block_slide", (CriterionTriggerInstance)SlideDownBlockTrigger.TriggerInstance.slidesDownBlock(Blocks.HONEY_BLOCK))
/* 252 */       .save(debug1, "adventure/honey_block_slide");
/*     */     
/* 254 */     Advancement.Builder.advancement()
/* 255 */       .parent(debug6)
/* 256 */       .display((ItemLike)Blocks.TARGET.asItem(), (Component)new TranslatableComponent("advancements.adventure.bullseye.title"), (Component)new TranslatableComponent("advancements.adventure.bullseye.description"), null, FrameType.CHALLENGE, true, true, false)
/* 257 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 258 */       .addCriterion("bullseye", (CriterionTriggerInstance)TargetBlockTrigger.TriggerInstance.targetHit(MinMaxBounds.Ints.exactly(15), EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().distance(DistancePredicate.horizontal(MinMaxBounds.Floats.atLeast(30.0F))).build())))
/* 259 */       .save(debug1, "adventure/bullseye");
/*     */   }
/*     */   
/*     */   private Advancement.Builder addMobsToKill(Advancement.Builder debug1) {
/* 263 */     for (EntityType<?> debug5 : MOBS_TO_KILL) {
/* 264 */       debug1.addCriterion(Registry.ENTITY_TYPE.getKey(debug5).toString(), (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(debug5)));
/*     */     }
/* 266 */     return debug1;
/*     */   }
/*     */   
/*     */   protected static Advancement.Builder addBiomes(Advancement.Builder debug0, List<ResourceKey<Biome>> debug1) {
/* 270 */     for (ResourceKey<Biome> debug3 : debug1) {
/* 271 */       debug0.addCriterion(debug3.location().toString(), (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(debug3)));
/*     */     }
/* 273 */     return debug0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\AdventureAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */