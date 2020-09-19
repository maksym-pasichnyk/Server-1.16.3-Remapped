/*     */ package net.minecraft.server.commands;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TagCommand {
/*  25 */   private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.tag.add.failed"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_REMOVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.tag.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  29 */     debug0.register(
/*  30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tag")
/*  31 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  32 */         .then((
/*  33 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  34 */           .then(
/*  35 */             Commands.literal("add")
/*  36 */             .then(
/*  37 */               Commands.argument("name", (ArgumentType)StringArgumentType.word())
/*  38 */               .executes(debug0 -> addTag((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), StringArgumentType.getString(debug0, "name"))))))
/*     */ 
/*     */           
/*  41 */           .then(
/*  42 */             Commands.literal("remove")
/*  43 */             .then(
/*  44 */               Commands.argument("name", (ArgumentType)StringArgumentType.word())
/*  45 */               .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(getTags(EntityArgument.getEntities(debug0, "targets")), debug1))
/*  46 */               .executes(debug0 -> removeTag((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), StringArgumentType.getString(debug0, "name"))))))
/*     */ 
/*     */           
/*  49 */           .then(
/*  50 */             Commands.literal("list")
/*  51 */             .executes(debug0 -> listTags((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<String> getTags(Collection<? extends Entity> debug0) {
/*  58 */     Set<String> debug1 = Sets.newHashSet();
/*  59 */     for (Entity debug3 : debug0) {
/*  60 */       debug1.addAll(debug3.getTags());
/*     */     }
/*  62 */     return debug1;
/*     */   }
/*     */   
/*     */   private static int addTag(CommandSourceStack debug0, Collection<? extends Entity> debug1, String debug2) throws CommandSyntaxException {
/*  66 */     int debug3 = 0;
/*     */     
/*  68 */     for (Entity debug5 : debug1) {
/*  69 */       if (debug5.addTag(debug2)) {
/*  70 */         debug3++;
/*     */       }
/*     */     } 
/*     */     
/*  74 */     if (debug3 == 0) {
/*  75 */       throw ERROR_ADD_FAILED.create();
/*     */     }
/*     */     
/*  78 */     if (debug1.size() == 1) {
/*  79 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.add.success.single", new Object[] { debug2, ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  81 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.add.success.multiple", new Object[] { debug2, Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/*  84 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int removeTag(CommandSourceStack debug0, Collection<? extends Entity> debug1, String debug2) throws CommandSyntaxException {
/*  88 */     int debug3 = 0;
/*     */     
/*  90 */     for (Entity debug5 : debug1) {
/*  91 */       if (debug5.removeTag(debug2)) {
/*  92 */         debug3++;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     if (debug3 == 0) {
/*  97 */       throw ERROR_REMOVE_FAILED.create();
/*     */     }
/*     */     
/* 100 */     if (debug1.size() == 1) {
/* 101 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.remove.success.single", new Object[] { debug2, ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 103 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.remove.success.multiple", new Object[] { debug2, Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 106 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int listTags(CommandSourceStack debug0, Collection<? extends Entity> debug1) {
/* 110 */     Set<String> debug2 = Sets.newHashSet();
/*     */     
/* 112 */     for (Entity debug4 : debug1) {
/* 113 */       debug2.addAll(debug4.getTags());
/*     */     }
/*     */     
/* 116 */     if (debug1.size() == 1) {
/* 117 */       Entity debug3 = debug1.iterator().next();
/*     */       
/* 119 */       if (debug2.isEmpty()) {
/* 120 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.list.single.empty", new Object[] { debug3.getDisplayName() }), false);
/*     */       } else {
/* 122 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.list.single.success", new Object[] { debug3.getDisplayName(), Integer.valueOf(debug2.size()), ComponentUtils.formatList(debug2) }), false);
/*     */       }
/*     */     
/* 125 */     } else if (debug2.isEmpty()) {
/* 126 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.list.multiple.empty", new Object[] { Integer.valueOf(debug1.size()) }), false);
/*     */     } else {
/* 128 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.tag.list.multiple.success", new Object[] { Integer.valueOf(debug1.size()), Integer.valueOf(debug2.size()), ComponentUtils.formatList(debug2) }), false);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return debug2.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TagCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */