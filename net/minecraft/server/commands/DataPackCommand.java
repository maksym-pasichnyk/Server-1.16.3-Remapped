/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ 
/*     */ public class DataPackCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_PACK;
/*     */   private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_ENABLED;
/*     */   
/*     */   static {
/*  28 */     ERROR_UNKNOWN_PACK = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.datapack.unknown", new Object[] { debug0 }));
/*  29 */     ERROR_PACK_ALREADY_ENABLED = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.datapack.enable.failed", new Object[] { debug0 }));
/*  30 */     ERROR_PACK_ALREADY_DISABLED = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.datapack.disable.failed", new Object[] { debug0 }));
/*     */     
/*  32 */     SELECTED_PACKS = ((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getPackRepository().getSelectedIds().stream().map(StringArgumentType::escapeIfRequired), debug1));
/*  33 */     UNSELECTED_PACKS = ((debug0, debug1) -> {
/*     */         PackRepository debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getPackRepository();
/*     */         Collection<String> debug3 = debug2.getSelectedIds();
/*     */         return SharedSuggestionProvider.suggest(debug2.getAvailableIds().stream().filter(()).map(StringArgumentType::escapeIfRequired), debug1);
/*     */       });
/*     */   } private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_DISABLED; private static final SuggestionProvider<CommandSourceStack> SELECTED_PACKS; private static final SuggestionProvider<CommandSourceStack> UNSELECTED_PACKS;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  40 */     debug0.register(
/*  41 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("datapack")
/*  42 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  43 */         .then(
/*  44 */           Commands.literal("enable")
/*  45 */           .then((
/*  46 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", (ArgumentType)StringArgumentType.string())
/*  47 */             .suggests(UNSELECTED_PACKS)
/*  48 */             .executes(debug0 -> enablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", true), ())))
/*  49 */             .then(
/*  50 */               Commands.literal("after")
/*  51 */               .then(
/*  52 */                 Commands.argument("existing", (ArgumentType)StringArgumentType.string())
/*  53 */                 .suggests(SELECTED_PACKS)
/*  54 */                 .executes(debug0 -> enablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", true), ())))))
/*     */ 
/*     */             
/*  57 */             .then(
/*  58 */               Commands.literal("before")
/*  59 */               .then(
/*  60 */                 Commands.argument("existing", (ArgumentType)StringArgumentType.string())
/*  61 */                 .suggests(SELECTED_PACKS)
/*  62 */                 .executes(debug0 -> enablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", true), ())))))
/*     */ 
/*     */             
/*  65 */             .then(
/*  66 */               Commands.literal("last")
/*  67 */               .executes(debug0 -> enablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", true), List::add))))
/*     */             
/*  69 */             .then(
/*  70 */               Commands.literal("first")
/*  71 */               .executes(debug0 -> enablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", true), ()))))))
/*     */ 
/*     */ 
/*     */         
/*  75 */         .then(
/*  76 */           Commands.literal("disable")
/*  77 */           .then(
/*  78 */             Commands.argument("name", (ArgumentType)StringArgumentType.string())
/*  79 */             .suggests(SELECTED_PACKS)
/*  80 */             .executes(debug0 -> disablePack((CommandSourceStack)debug0.getSource(), getPack(debug0, "name", false))))))
/*     */ 
/*     */         
/*  83 */         .then((
/*  84 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/*  85 */           .executes(debug0 -> listPacks((CommandSourceStack)debug0.getSource())))
/*  86 */           .then(
/*  87 */             Commands.literal("available")
/*  88 */             .executes(debug0 -> listAvailablePacks((CommandSourceStack)debug0.getSource()))))
/*     */           
/*  90 */           .then(
/*  91 */             Commands.literal("enabled")
/*  92 */             .executes(debug0 -> listEnabledPacks((CommandSourceStack)debug0.getSource())))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int enablePack(CommandSourceStack debug0, Pack debug1, Inserter debug2) throws CommandSyntaxException {
/*  99 */     PackRepository debug3 = debug0.getServer().getPackRepository();
/*     */     
/* 101 */     List<Pack> debug4 = Lists.newArrayList(debug3.getSelectedPacks());
/* 102 */     debug2.apply(debug4, debug1);
/*     */     
/* 104 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.modify.enable", new Object[] { debug1.getChatLink(true) }), true);
/* 105 */     ReloadCommand.reloadPacks((Collection<String>)debug4.stream().map(Pack::getId).collect(Collectors.toList()), debug0);
/* 106 */     return debug4.size();
/*     */   }
/*     */   
/*     */   private static int disablePack(CommandSourceStack debug0, Pack debug1) {
/* 110 */     PackRepository debug2 = debug0.getServer().getPackRepository();
/*     */     
/* 112 */     List<Pack> debug3 = Lists.newArrayList(debug2.getSelectedPacks());
/* 113 */     debug3.remove(debug1);
/*     */     
/* 115 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.modify.disable", new Object[] { debug1.getChatLink(true) }), true);
/* 116 */     ReloadCommand.reloadPacks((Collection<String>)debug3.stream().map(Pack::getId).collect(Collectors.toList()), debug0);
/* 117 */     return debug3.size();
/*     */   }
/*     */   
/*     */   private static int listPacks(CommandSourceStack debug0) {
/* 121 */     return listEnabledPacks(debug0) + listAvailablePacks(debug0);
/*     */   }
/*     */   
/*     */   private static int listAvailablePacks(CommandSourceStack debug0) {
/* 125 */     PackRepository debug1 = debug0.getServer().getPackRepository();
/* 126 */     debug1.reload();
/*     */     
/* 128 */     Collection<? extends Pack> debug2 = debug1.getSelectedPacks();
/* 129 */     Collection<? extends Pack> debug3 = debug1.getAvailablePacks();
/* 130 */     List<Pack> debug4 = (List<Pack>)debug3.stream().filter(debug1 -> !debug0.contains(debug1)).collect(Collectors.toList());
/* 131 */     if (debug4.isEmpty()) {
/* 132 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.list.available.none"), false);
/*     */     } else {
/* 134 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.list.available.success", new Object[] { Integer.valueOf(debug4.size()), ComponentUtils.formatList(debug4, debug0 -> debug0.getChatLink(false)) }), false);
/*     */     } 
/*     */     
/* 137 */     return debug4.size();
/*     */   }
/*     */   
/*     */   private static int listEnabledPacks(CommandSourceStack debug0) {
/* 141 */     PackRepository debug1 = debug0.getServer().getPackRepository();
/* 142 */     debug1.reload();
/*     */     
/* 144 */     Collection<? extends Pack> debug2 = debug1.getSelectedPacks();
/* 145 */     if (debug2.isEmpty()) {
/* 146 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.list.enabled.none"), false);
/*     */     } else {
/* 148 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.datapack.list.enabled.success", new Object[] { Integer.valueOf(debug2.size()), ComponentUtils.formatList(debug2, debug0 -> debug0.getChatLink(true)) }), false);
/*     */     } 
/*     */     
/* 151 */     return debug2.size();
/*     */   }
/*     */   
/*     */   private static Pack getPack(CommandContext<CommandSourceStack> debug0, String debug1, boolean debug2) throws CommandSyntaxException {
/* 155 */     String debug3 = StringArgumentType.getString(debug0, debug1);
/* 156 */     PackRepository debug4 = ((CommandSourceStack)debug0.getSource()).getServer().getPackRepository();
/* 157 */     Pack debug5 = debug4.getPack(debug3);
/* 158 */     if (debug5 == null) {
/* 159 */       throw ERROR_UNKNOWN_PACK.create(debug3);
/*     */     }
/* 161 */     boolean debug6 = debug4.getSelectedPacks().contains(debug5);
/* 162 */     if (debug2 && debug6) {
/* 163 */       throw ERROR_PACK_ALREADY_ENABLED.create(debug3);
/*     */     }
/* 165 */     if (!debug2 && !debug6) {
/* 166 */       throw ERROR_PACK_ALREADY_DISABLED.create(debug3);
/*     */     }
/* 168 */     return debug5;
/*     */   }
/*     */   
/*     */   static interface Inserter {
/*     */     void apply(List<Pack> param1List, Pack param1Pack) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\DataPackCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */