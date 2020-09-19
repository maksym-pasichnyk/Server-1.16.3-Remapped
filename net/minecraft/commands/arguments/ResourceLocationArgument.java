/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ResourceLocationArgument implements ArgumentType<ResourceLocation> {
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ADVANCEMENT;
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" }); private static final DynamicCommandExceptionType ERROR_UNKNOWN_RECIPE; static {
/* 24 */     ERROR_UNKNOWN_ADVANCEMENT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("advancement.advancementNotFound", new Object[] { debug0 }));
/* 25 */     ERROR_UNKNOWN_RECIPE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("recipe.notFound", new Object[] { debug0 }));
/* 26 */     ERROR_UNKNOWN_PREDICATE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("predicate.unknown", new Object[] { debug0 }));
/* 27 */     ERROR_UNKNOWN_ATTRIBUTE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("attribute.unknown", new Object[] { debug0 }));
/*    */   }
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_PREDICATE;
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ATTRIBUTE;
/*    */   
/*    */   public static ResourceLocationArgument id() {
/* 33 */     return new ResourceLocationArgument();
/*    */   }
/*    */   
/*    */   public static Advancement getAdvancement(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 37 */     ResourceLocation debug2 = (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/* 38 */     Advancement debug3 = ((CommandSourceStack)debug0.getSource()).getServer().getAdvancements().getAdvancement(debug2);
/* 39 */     if (debug3 == null) {
/* 40 */       throw ERROR_UNKNOWN_ADVANCEMENT.create(debug2);
/*    */     }
/* 42 */     return debug3;
/*    */   }
/*    */   
/*    */   public static Recipe<?> getRecipe(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 46 */     RecipeManager debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getRecipeManager();
/* 47 */     ResourceLocation debug3 = (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/*    */     
/* 49 */     return (Recipe)debug2.byKey(debug3).orElseThrow(() -> ERROR_UNKNOWN_RECIPE.create(debug0));
/*    */   }
/*    */   
/*    */   public static LootItemCondition getPredicate(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 53 */     ResourceLocation debug2 = (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/*    */     
/* 55 */     PredicateManager debug3 = ((CommandSourceStack)debug0.getSource()).getServer().getPredicateManager();
/* 56 */     LootItemCondition debug4 = debug3.get(debug2);
/* 57 */     if (debug4 == null) {
/* 58 */       throw ERROR_UNKNOWN_PREDICATE.create(debug2);
/*    */     }
/* 60 */     return debug4;
/*    */   }
/*    */   
/*    */   public static Attribute getAttribute(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 64 */     ResourceLocation debug2 = (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/* 65 */     return (Attribute)Registry.ATTRIBUTE.getOptional(debug2).orElseThrow(() -> ERROR_UNKNOWN_ATTRIBUTE.create(debug0));
/*    */   }
/*    */   
/*    */   public static ResourceLocation getId(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 69 */     return (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation parse(StringReader debug1) throws CommandSyntaxException {
/* 74 */     return ResourceLocation.read(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 79 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ResourceLocationArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */