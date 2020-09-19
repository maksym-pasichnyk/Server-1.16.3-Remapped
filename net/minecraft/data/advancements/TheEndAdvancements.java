/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.advancements.FrameType;
/*    */ import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
/*    */ import net.minecraft.advancements.critereon.DistancePredicate;
/*    */ import net.minecraft.advancements.critereon.EnterBlockTrigger;
/*    */ import net.minecraft.advancements.critereon.EntityPredicate;
/*    */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*    */ import net.minecraft.advancements.critereon.KilledTrigger;
/*    */ import net.minecraft.advancements.critereon.LevitationTrigger;
/*    */ import net.minecraft.advancements.critereon.LocationPredicate;
/*    */ import net.minecraft.advancements.critereon.LocationTrigger;
/*    */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*    */ import net.minecraft.advancements.critereon.SummonedEntityTrigger;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TheEndAdvancements
/*    */   implements Consumer<Consumer<Advancement>>
/*    */ {
/*    */   public void accept(Consumer<Advancement> debug1) {
/* 36 */     Advancement debug2 = Advancement.Builder.advancement().display((ItemLike)Blocks.END_STONE, (Component)new TranslatableComponent("advancements.end.root.title"), (Component)new TranslatableComponent("advancements.end.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/end.png"), FrameType.TASK, false, false, false).addCriterion("entered_end", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(debug1, "end/root");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     Advancement debug3 = Advancement.Builder.advancement().parent(debug2).display((ItemLike)Blocks.DRAGON_HEAD, (Component)new TranslatableComponent("advancements.end.kill_dragon.title"), (Component)new TranslatableComponent("advancements.end.kill_dragon.description"), null, FrameType.TASK, true, true, false).addCriterion("killed_dragon", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.ENDER_DRAGON))).save(debug1, "end/kill_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     Advancement debug4 = Advancement.Builder.advancement().parent(debug3).display((ItemLike)Items.ENDER_PEARL, (Component)new TranslatableComponent("advancements.end.enter_end_gateway.title"), (Component)new TranslatableComponent("advancements.end.enter_end_gateway.description"), null, FrameType.TASK, true, true, false).addCriterion("entered_end_gateway", (CriterionTriggerInstance)EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY)).save(debug1, "end/enter_end_gateway");
/*    */     
/* 50 */     Advancement.Builder.advancement()
/* 51 */       .parent(debug3)
/* 52 */       .display((ItemLike)Items.END_CRYSTAL, (Component)new TranslatableComponent("advancements.end.respawn_dragon.title"), (Component)new TranslatableComponent("advancements.end.respawn_dragon.description"), null, FrameType.GOAL, true, true, false)
/* 53 */       .addCriterion("summoned_dragon", (CriterionTriggerInstance)SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.ENDER_DRAGON)))
/* 54 */       .save(debug1, "end/respawn_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     Advancement debug5 = Advancement.Builder.advancement().parent(debug4).display((ItemLike)Blocks.PURPUR_BLOCK, (Component)new TranslatableComponent("advancements.end.find_end_city.title"), (Component)new TranslatableComponent("advancements.end.find_end_city.description"), null, FrameType.TASK, true, true, false).addCriterion("in_city", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.END_CITY))).save(debug1, "end/find_end_city");
/*    */     
/* 62 */     Advancement.Builder.advancement()
/* 63 */       .parent(debug3)
/* 64 */       .display((ItemLike)Items.DRAGON_BREATH, (Component)new TranslatableComponent("advancements.end.dragon_breath.title"), (Component)new TranslatableComponent("advancements.end.dragon_breath.description"), null, FrameType.GOAL, true, true, false)
/* 65 */       .addCriterion("dragon_breath", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DRAGON_BREATH
/* 66 */           })).save(debug1, "end/dragon_breath");
/*    */     
/* 68 */     Advancement.Builder.advancement()
/* 69 */       .parent(debug5)
/* 70 */       .display((ItemLike)Items.SHULKER_SHELL, (Component)new TranslatableComponent("advancements.end.levitate.title"), (Component)new TranslatableComponent("advancements.end.levitate.description"), null, FrameType.CHALLENGE, true, true, false)
/* 71 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 72 */       .addCriterion("levitated", (CriterionTriggerInstance)LevitationTrigger.TriggerInstance.levitated(DistancePredicate.vertical(MinMaxBounds.Floats.atLeast(50.0F))))
/* 73 */       .save(debug1, "end/levitate");
/*    */     
/* 75 */     Advancement.Builder.advancement()
/* 76 */       .parent(debug5)
/* 77 */       .display((ItemLike)Items.ELYTRA, (Component)new TranslatableComponent("advancements.end.elytra.title"), (Component)new TranslatableComponent("advancements.end.elytra.description"), null, FrameType.GOAL, true, true, false)
/* 78 */       .addCriterion("elytra", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ELYTRA
/* 79 */           })).save(debug1, "end/elytra");
/*    */     
/* 81 */     Advancement.Builder.advancement()
/* 82 */       .parent(debug3)
/* 83 */       .display((ItemLike)Blocks.DRAGON_EGG, (Component)new TranslatableComponent("advancements.end.dragon_egg.title"), (Component)new TranslatableComponent("advancements.end.dragon_egg.description"), null, FrameType.GOAL, true, true, false)
/* 84 */       .addCriterion("dragon_egg", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.DRAGON_EGG
/* 85 */           })).save(debug1, "end/dragon_egg");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\TheEndAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */