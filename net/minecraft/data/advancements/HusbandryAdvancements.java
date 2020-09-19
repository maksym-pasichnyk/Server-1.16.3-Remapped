/*     */ package net.minecraft.data.advancements;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.FrameType;
/*     */ import net.minecraft.advancements.RequirementsStrategy;
/*     */ import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.critereon.BlockPredicate;
/*     */ import net.minecraft.advancements.critereon.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.critereon.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.critereon.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.critereon.EntityPredicate;
/*     */ import net.minecraft.advancements.critereon.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemPredicate;
/*     */ import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.LocationPredicate;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.advancements.critereon.PlacedBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.TameAnimalTrigger;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HusbandryAdvancements
/*     */   implements Consumer<Consumer<Advancement>>
/*     */ {
/*  43 */   private static final EntityType<?>[] BREEDABLE_ANIMALS = new EntityType[] { EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.CHICKEN, EntityType.WOLF, EntityType.OCELOT, EntityType.RABBIT, EntityType.LLAMA, EntityType.CAT, EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.HOGLIN, EntityType.STRIDER };
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
/*  65 */   private static final Item[] FISH = new Item[] { Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final Item[] FISH_BUCKETS = new Item[] { Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final Item[] EDIBLE_ITEMS = new Item[] { Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE };
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
/*     */   public void accept(Consumer<Advancement> debug1) {
/* 126 */     Advancement debug2 = Advancement.Builder.advancement().display((ItemLike)Blocks.HAY_BLOCK, (Component)new TranslatableComponent("advancements.husbandry.root.title"), (Component)new TranslatableComponent("advancements.husbandry.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png"), FrameType.TASK, false, false, false).addCriterion("consumed_item", (CriterionTriggerInstance)ConsumeItemTrigger.TriggerInstance.usedItem()).save(debug1, "husbandry/root");
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
/* 137 */     Advancement debug3 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.WHEAT, (Component)new TranslatableComponent("advancements.husbandry.plant_seed.title"), (Component)new TranslatableComponent("advancements.husbandry.plant_seed.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("wheat", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.WHEAT)).addCriterion("pumpkin_stem", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM)).addCriterion("melon_stem", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM)).addCriterion("beetroots", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS)).addCriterion("nether_wart", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART)).save(debug1, "husbandry/plant_seed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     Advancement debug4 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.WHEAT, (Component)new TranslatableComponent("advancements.husbandry.breed_an_animal.title"), (Component)new TranslatableComponent("advancements.husbandry.breed_an_animal.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("bred", (CriterionTriggerInstance)BredAnimalsTrigger.TriggerInstance.bredAnimals()).save(debug1, "husbandry/breed_an_animal");
/*     */     
/* 146 */     addFood(Advancement.Builder.advancement())
/* 147 */       .parent(debug3)
/* 148 */       .display((ItemLike)Items.APPLE, (Component)new TranslatableComponent("advancements.husbandry.balanced_diet.title"), (Component)new TranslatableComponent("advancements.husbandry.balanced_diet.description"), null, FrameType.CHALLENGE, true, true, false)
/* 149 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 150 */       .save(debug1, "husbandry/balanced_diet");
/*     */     
/* 152 */     Advancement.Builder.advancement()
/* 153 */       .parent(debug3)
/* 154 */       .display((ItemLike)Items.NETHERITE_HOE, (Component)new TranslatableComponent("advancements.husbandry.netherite_hoe.title"), (Component)new TranslatableComponent("advancements.husbandry.netherite_hoe.description"), null, FrameType.CHALLENGE, true, true, false)
/* 155 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 156 */       .addCriterion("netherite_hoe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HOE
/* 157 */           })).save(debug1, "husbandry/obtain_netherite_hoe");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     Advancement debug5 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.LEAD, (Component)new TranslatableComponent("advancements.husbandry.tame_an_animal.title"), (Component)new TranslatableComponent("advancements.husbandry.tame_an_animal.description"), null, FrameType.TASK, true, true, false).addCriterion("tamed_animal", (CriterionTriggerInstance)TameAnimalTrigger.TriggerInstance.tamedAnimal()).save(debug1, "husbandry/tame_an_animal");
/*     */     
/* 165 */     addBreedable(Advancement.Builder.advancement())
/* 166 */       .parent(debug4)
/* 167 */       .display((ItemLike)Items.GOLDEN_CARROT, (Component)new TranslatableComponent("advancements.husbandry.breed_all_animals.title"), (Component)new TranslatableComponent("advancements.husbandry.breed_all_animals.description"), null, FrameType.CHALLENGE, true, true, false)
/* 168 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 169 */       .save(debug1, "husbandry/bred_all_animals");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     Advancement debug6 = addFish(Advancement.Builder.advancement()).parent(debug2).requirements(RequirementsStrategy.OR).display((ItemLike)Items.FISHING_ROD, (Component)new TranslatableComponent("advancements.husbandry.fishy_business.title"), (Component)new TranslatableComponent("advancements.husbandry.fishy_business.description"), null, FrameType.TASK, true, true, false).save(debug1, "husbandry/fishy_business");
/*     */     
/* 177 */     addFishBuckets(Advancement.Builder.advancement())
/* 178 */       .parent(debug6)
/* 179 */       .requirements(RequirementsStrategy.OR)
/* 180 */       .display((ItemLike)Items.PUFFERFISH_BUCKET, (Component)new TranslatableComponent("advancements.husbandry.tactical_fishing.title"), (Component)new TranslatableComponent("advancements.husbandry.tactical_fishing.description"), null, FrameType.TASK, true, true, false)
/* 181 */       .save(debug1, "husbandry/tactical_fishing");
/*     */     
/* 183 */     addCatVariants(Advancement.Builder.advancement())
/* 184 */       .parent(debug5)
/* 185 */       .display((ItemLike)Items.COD, (Component)new TranslatableComponent("advancements.husbandry.complete_catalogue.title"), (Component)new TranslatableComponent("advancements.husbandry.complete_catalogue.description"), null, FrameType.CHALLENGE, true, true, false)
/* 186 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 187 */       .save(debug1, "husbandry/complete_catalogue");
/*     */     
/* 189 */     Advancement.Builder.advancement()
/* 190 */       .parent(debug2)
/* 191 */       .addCriterion("safely_harvest_honey", (CriterionTriggerInstance)ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((Tag)BlockTags.BEEHIVES).build()).setSmokey(Boolean.valueOf(true)), ItemPredicate.Builder.item().of((ItemLike)Items.GLASS_BOTTLE)))
/* 192 */       .display((ItemLike)Items.HONEY_BOTTLE, (Component)new TranslatableComponent("advancements.husbandry.safely_harvest_honey.title"), (Component)new TranslatableComponent("advancements.husbandry.safely_harvest_honey.description"), null, FrameType.TASK, true, true, false)
/* 193 */       .save(debug1, "husbandry/safely_harvest_honey");
/*     */     
/* 195 */     Advancement.Builder.advancement()
/* 196 */       .parent(debug2)
/* 197 */       .addCriterion("silk_touch_nest", (CriterionTriggerInstance)BeeNestDestroyedTrigger.TriggerInstance.destroyedBeeNest(Blocks.BEE_NEST, ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))), MinMaxBounds.Ints.exactly(3)))
/* 198 */       .display((ItemLike)Blocks.BEE_NEST, (Component)new TranslatableComponent("advancements.husbandry.silk_touch_nest.title"), (Component)new TranslatableComponent("advancements.husbandry.silk_touch_nest.description"), null, FrameType.TASK, true, true, false)
/* 199 */       .save(debug1, "husbandry/silk_touch_nest");
/*     */   }
/*     */   
/*     */   private Advancement.Builder addFood(Advancement.Builder debug1) {
/* 203 */     for (Item debug5 : EDIBLE_ITEMS) {
/* 204 */       debug1.addCriterion(Registry.ITEM.getKey(debug5).getPath(), (CriterionTriggerInstance)ConsumeItemTrigger.TriggerInstance.usedItem((ItemLike)debug5));
/*     */     }
/* 206 */     return debug1;
/*     */   }
/*     */   
/*     */   private Advancement.Builder addBreedable(Advancement.Builder debug1) {
/* 210 */     for (EntityType<?> debug5 : BREEDABLE_ANIMALS) {
/* 211 */       debug1.addCriterion(EntityType.getKey(debug5).toString(), (CriterionTriggerInstance)BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(debug5)));
/*     */     }
/* 213 */     debug1.addCriterion(EntityType.getKey(EntityType.TURTLE).toString(), (CriterionTriggerInstance)BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(EntityType.TURTLE).build(), EntityPredicate.Builder.entity().of(EntityType.TURTLE).build(), EntityPredicate.ANY));
/* 214 */     return debug1;
/*     */   }
/*     */   
/*     */   private Advancement.Builder addFishBuckets(Advancement.Builder debug1) {
/* 218 */     for (Item debug5 : FISH_BUCKETS) {
/* 219 */       debug1.addCriterion(Registry.ITEM.getKey(debug5).getPath(), (CriterionTriggerInstance)FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((ItemLike)debug5).build()));
/*     */     }
/* 221 */     return debug1;
/*     */   }
/*     */   
/*     */   private Advancement.Builder addFish(Advancement.Builder debug1) {
/* 225 */     for (Item debug5 : FISH) {
/* 226 */       debug1.addCriterion(Registry.ITEM.getKey(debug5).getPath(), (CriterionTriggerInstance)FishingRodHookedTrigger.TriggerInstance.fishedItem(ItemPredicate.ANY, EntityPredicate.ANY, ItemPredicate.Builder.item().of((ItemLike)debug5).build()));
/*     */     }
/* 228 */     return debug1;
/*     */   }
/*     */   
/*     */   private Advancement.Builder addCatVariants(Advancement.Builder debug1) {
/* 232 */     Cat.TEXTURE_BY_TYPE.forEach((debug1, debug2) -> debug0.addCriterion(debug2.getPath(), (CriterionTriggerInstance)TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(debug2).build())));
/*     */     
/* 234 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\HusbandryAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */