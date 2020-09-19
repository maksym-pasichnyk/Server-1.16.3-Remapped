/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ public class RecipeCommand {
/*  24 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.recipe.give.failed"));
/*  25 */   private static final SimpleCommandExceptionType ERROR_TAKE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.recipe.take.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  28 */     debug0.register(
/*  29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("recipe")
/*  30 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  31 */         .then(
/*  32 */           Commands.literal("give")
/*  33 */           .then((
/*  34 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  35 */             .then(
/*  36 */               Commands.argument("recipe", (ArgumentType)ResourceLocationArgument.id())
/*  37 */               .suggests(SuggestionProviders.ALL_RECIPES)
/*  38 */               .executes(debug0 -> giveRecipes((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Collections.singleton(ResourceLocationArgument.getRecipe(debug0, "recipe"))))))
/*     */             
/*  40 */             .then(
/*  41 */               Commands.literal("*")
/*  42 */               .executes(debug0 -> giveRecipes((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getServer().getRecipeManager().getRecipes()))))))
/*     */ 
/*     */ 
/*     */         
/*  46 */         .then(
/*  47 */           Commands.literal("take")
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  50 */             .then(
/*  51 */               Commands.argument("recipe", (ArgumentType)ResourceLocationArgument.id())
/*  52 */               .suggests(SuggestionProviders.ALL_RECIPES)
/*  53 */               .executes(debug0 -> takeRecipes((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Collections.singleton(ResourceLocationArgument.getRecipe(debug0, "recipe"))))))
/*     */             
/*  55 */             .then(
/*  56 */               Commands.literal("*")
/*  57 */               .executes(debug0 -> takeRecipes((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getServer().getRecipeManager().getRecipes()))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int giveRecipes(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Collection<Recipe<?>> debug2) throws CommandSyntaxException {
/*  65 */     int debug3 = 0;
/*     */     
/*  67 */     for (ServerPlayer debug5 : debug1) {
/*  68 */       debug3 += debug5.awardRecipes(debug2);
/*     */     }
/*     */     
/*  71 */     if (debug3 == 0) {
/*  72 */       throw ERROR_GIVE_FAILED.create();
/*     */     }
/*     */     
/*  75 */     if (debug1.size() == 1) {
/*  76 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.recipe.give.success.single", new Object[] { Integer.valueOf(debug2.size()), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  78 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.recipe.give.success.multiple", new Object[] { Integer.valueOf(debug2.size()), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/*  81 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int takeRecipes(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Collection<Recipe<?>> debug2) throws CommandSyntaxException {
/*  85 */     int debug3 = 0;
/*     */     
/*  87 */     for (ServerPlayer debug5 : debug1) {
/*  88 */       debug3 += debug5.resetRecipes(debug2);
/*     */     }
/*     */     
/*  91 */     if (debug3 == 0) {
/*  92 */       throw ERROR_TAKE_FAILED.create();
/*     */     }
/*     */     
/*  95 */     if (debug1.size() == 1) {
/*  96 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.recipe.take.success.single", new Object[] { Integer.valueOf(debug2.size()), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  98 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.recipe.take.success.multiple", new Object[] { Integer.valueOf(debug2.size()), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 101 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\RecipeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */