/*     */ package net.minecraft.data.advancements;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.FrameType;
/*     */ import net.minecraft.advancements.RequirementsStrategy;
/*     */ import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.critereon.DamagePredicate;
/*     */ import net.minecraft.advancements.critereon.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.critereon.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemPredicate;
/*     */ import net.minecraft.advancements.critereon.LocationPredicate;
/*     */ import net.minecraft.advancements.critereon.LocationTrigger;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StoryAdvancements
/*     */   implements Consumer<Consumer<Advancement>>
/*     */ {
/*     */   public void accept(Consumer<Advancement> debug1) {
/*  35 */     Advancement debug2 = Advancement.Builder.advancement().display((ItemLike)Blocks.GRASS_BLOCK, (Component)new TranslatableComponent("advancements.story.root.title"), (Component)new TranslatableComponent("advancements.story.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, false, false, false).addCriterion("crafting_table", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.CRAFTING_TABLE })).save(debug1, "story/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     Advancement debug3 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Items.WOODEN_PICKAXE, (Component)new TranslatableComponent("advancements.story.mine_stone.title"), (Component)new TranslatableComponent("advancements.story.mine_stone.description"), null, FrameType.TASK, true, true, false).addCriterion("get_stone", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate[] { ItemPredicate.Builder.item().of((Tag)ItemTags.STONE_TOOL_MATERIALS).build() })).save(debug1, "story/mine_stone");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     Advancement debug4 = Advancement.Builder.advancement().parent(debug3).display((ItemLike)Items.STONE_PICKAXE, (Component)new TranslatableComponent("advancements.story.upgrade_tools.title"), (Component)new TranslatableComponent("advancements.story.upgrade_tools.description"), null, FrameType.TASK, true, true, false).addCriterion("stone_pickaxe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.STONE_PICKAXE })).save(debug1, "story/upgrade_tools");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     Advancement debug5 = Advancement.Builder.advancement().parent(debug4).display((ItemLike)Items.IRON_INGOT, (Component)new TranslatableComponent("advancements.story.smelt_iron.title"), (Component)new TranslatableComponent("advancements.story.smelt_iron.description"), null, FrameType.TASK, true, true, false).addCriterion("iron", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_INGOT })).save(debug1, "story/smelt_iron");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Advancement debug6 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.IRON_PICKAXE, (Component)new TranslatableComponent("advancements.story.iron_tools.title"), (Component)new TranslatableComponent("advancements.story.iron_tools.description"), null, FrameType.TASK, true, true, false).addCriterion("iron_pickaxe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE })).save(debug1, "story/iron_tools");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Advancement debug7 = Advancement.Builder.advancement().parent(debug6).display((ItemLike)Items.DIAMOND, (Component)new TranslatableComponent("advancements.story.mine_diamond.title"), (Component)new TranslatableComponent("advancements.story.mine_diamond.description"), null, FrameType.TASK, true, true, false).addCriterion("diamond", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND })).save(debug1, "story/mine_diamond");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     Advancement debug8 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.LAVA_BUCKET, (Component)new TranslatableComponent("advancements.story.lava_bucket.title"), (Component)new TranslatableComponent("advancements.story.lava_bucket.description"), null, FrameType.TASK, true, true, false).addCriterion("lava_bucket", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.LAVA_BUCKET })).save(debug1, "story/lava_bucket");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     Advancement debug9 = Advancement.Builder.advancement().parent(debug5).display((ItemLike)Items.IRON_CHESTPLATE, (Component)new TranslatableComponent("advancements.story.obtain_armor.title"), (Component)new TranslatableComponent("advancements.story.obtain_armor.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("iron_helmet", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_HELMET })).addCriterion("iron_chestplate", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_CHESTPLATE })).addCriterion("iron_leggings", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_LEGGINGS })).addCriterion("iron_boots", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_BOOTS })).save(debug1, "story/obtain_armor");
/*     */     
/*  83 */     Advancement.Builder.advancement()
/*  84 */       .parent(debug7)
/*  85 */       .display((ItemLike)Items.ENCHANTED_BOOK, (Component)new TranslatableComponent("advancements.story.enchant_item.title"), (Component)new TranslatableComponent("advancements.story.enchant_item.description"), null, FrameType.TASK, true, true, false)
/*  86 */       .addCriterion("enchanted_item", (CriterionTriggerInstance)EnchantedItemTrigger.TriggerInstance.enchantedItem())
/*  87 */       .save(debug1, "story/enchant_item");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     Advancement debug10 = Advancement.Builder.advancement().parent(debug8).display((ItemLike)Blocks.OBSIDIAN, (Component)new TranslatableComponent("advancements.story.form_obsidian.title"), (Component)new TranslatableComponent("advancements.story.form_obsidian.description"), null, FrameType.TASK, true, true, false).addCriterion("obsidian", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.OBSIDIAN })).save(debug1, "story/form_obsidian");
/*     */     
/*  95 */     Advancement.Builder.advancement()
/*  96 */       .parent(debug9)
/*  97 */       .display((ItemLike)Items.SHIELD, (Component)new TranslatableComponent("advancements.story.deflect_arrow.title"), (Component)new TranslatableComponent("advancements.story.deflect_arrow.description"), null, FrameType.TASK, true, true, false)
/*  98 */       .addCriterion("deflected_projectile", (CriterionTriggerInstance)EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(Boolean.valueOf(true))).blocked(Boolean.valueOf(true))))
/*  99 */       .save(debug1, "story/deflect_arrow");
/*     */     
/* 101 */     Advancement.Builder.advancement()
/* 102 */       .parent(debug7)
/* 103 */       .display((ItemLike)Items.DIAMOND_CHESTPLATE, (Component)new TranslatableComponent("advancements.story.shiny_gear.title"), (Component)new TranslatableComponent("advancements.story.shiny_gear.description"), null, FrameType.TASK, true, true, false)
/* 104 */       .requirements(RequirementsStrategy.OR)
/* 105 */       .addCriterion("diamond_helmet", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_HELMET
/* 106 */           })).addCriterion("diamond_chestplate", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_CHESTPLATE
/* 107 */           })).addCriterion("diamond_leggings", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_LEGGINGS
/* 108 */           })).addCriterion("diamond_boots", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_BOOTS
/* 109 */           })).save(debug1, "story/shiny_gear");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     Advancement debug11 = Advancement.Builder.advancement().parent(debug10).display((ItemLike)Items.FLINT_AND_STEEL, (Component)new TranslatableComponent("advancements.story.enter_the_nether.title"), (Component)new TranslatableComponent("advancements.story.enter_the_nether.description"), null, FrameType.TASK, true, true, false).addCriterion("entered_nether", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(debug1, "story/enter_the_nether");
/*     */     
/* 117 */     Advancement.Builder.advancement()
/* 118 */       .parent(debug11)
/* 119 */       .display((ItemLike)Items.GOLDEN_APPLE, (Component)new TranslatableComponent("advancements.story.cure_zombie_villager.title"), (Component)new TranslatableComponent("advancements.story.cure_zombie_villager.description"), null, FrameType.GOAL, true, true, false)
/* 120 */       .addCriterion("cured_zombie", (CriterionTriggerInstance)CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
/* 121 */       .save(debug1, "story/cure_zombie_villager");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     Advancement debug12 = Advancement.Builder.advancement().parent(debug11).display((ItemLike)Items.ENDER_EYE, (Component)new TranslatableComponent("advancements.story.follow_ender_eye.title"), (Component)new TranslatableComponent("advancements.story.follow_ender_eye.description"), null, FrameType.TASK, true, true, false).addCriterion("in_stronghold", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.STRONGHOLD))).save(debug1, "story/follow_ender_eye");
/*     */     
/* 129 */     Advancement.Builder.advancement()
/* 130 */       .parent(debug12)
/* 131 */       .display((ItemLike)Blocks.END_STONE, (Component)new TranslatableComponent("advancements.story.enter_the_end.title"), (Component)new TranslatableComponent("advancements.story.enter_the_end.description"), null, FrameType.TASK, true, true, false)
/* 132 */       .addCriterion("entered_end", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
/* 133 */       .save(debug1, "story/enter_the_end");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\StoryAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */