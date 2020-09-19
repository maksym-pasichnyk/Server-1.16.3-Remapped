/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.commands.CommandRuntimeException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ public class AdvancementCommands {
/*     */   static {
/*  28 */     SUGGEST_ADVANCEMENTS = ((debug0, debug1) -> {
/*     */         Collection<Advancement> debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getAdvancements().getAllAdvancements();
/*     */         return SharedSuggestionProvider.suggestResource(debug2.stream().map(Advancement::getId), debug1);
/*     */       });
/*     */   } private static final SuggestionProvider<CommandSourceStack> SUGGEST_ADVANCEMENTS;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  34 */     debug0.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("advancement")
/*  36 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  37 */         .then(
/*  38 */           Commands.literal("grant")
/*  39 */           .then((
/*  40 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  41 */             .then(
/*  42 */               Commands.literal("only")
/*  43 */               .then((
/*  44 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/*  45 */                 .suggests(SUGGEST_ADVANCEMENTS)
/*  46 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.ONLY))))
/*  47 */                 .then(
/*  48 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  49 */                   .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(ResourceLocationArgument.getAdvancement(debug0, "advancement").getCriteria().keySet(), debug1))
/*  50 */                   .executes(debug0 -> performCriterion((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, ResourceLocationArgument.getAdvancement(debug0, "advancement"), StringArgumentType.getString(debug0, "criterion")))))))
/*     */ 
/*     */ 
/*     */             
/*  54 */             .then(
/*  55 */               Commands.literal("from")
/*  56 */               .then(
/*  57 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/*  58 */                 .suggests(SUGGEST_ADVANCEMENTS)
/*  59 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.FROM))))))
/*     */ 
/*     */             
/*  62 */             .then(
/*  63 */               Commands.literal("until")
/*  64 */               .then(
/*  65 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/*  66 */                 .suggests(SUGGEST_ADVANCEMENTS)
/*  67 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.UNTIL))))))
/*     */ 
/*     */             
/*  70 */             .then(
/*  71 */               Commands.literal("through")
/*  72 */               .then(
/*  73 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/*  74 */                 .suggests(SUGGEST_ADVANCEMENTS)
/*  75 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.THROUGH))))))
/*     */ 
/*     */             
/*  78 */             .then(
/*  79 */               Commands.literal("everything")
/*  80 */               .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.GRANT, ((CommandSourceStack)debug0.getSource()).getServer().getAdvancements().getAllAdvancements()))))))
/*     */ 
/*     */ 
/*     */         
/*  84 */         .then(
/*  85 */           Commands.literal("revoke")
/*  86 */           .then((
/*  87 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  88 */             .then(
/*  89 */               Commands.literal("only")
/*  90 */               .then((
/*  91 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/*  92 */                 .suggests(SUGGEST_ADVANCEMENTS)
/*  93 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.ONLY))))
/*  94 */                 .then(
/*  95 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  96 */                   .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(ResourceLocationArgument.getAdvancement(debug0, "advancement").getCriteria().keySet(), debug1))
/*  97 */                   .executes(debug0 -> performCriterion((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, ResourceLocationArgument.getAdvancement(debug0, "advancement"), StringArgumentType.getString(debug0, "criterion")))))))
/*     */ 
/*     */ 
/*     */             
/* 101 */             .then(
/* 102 */               Commands.literal("from")
/* 103 */               .then(
/* 104 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/* 105 */                 .suggests(SUGGEST_ADVANCEMENTS)
/* 106 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.FROM))))))
/*     */ 
/*     */             
/* 109 */             .then(
/* 110 */               Commands.literal("until")
/* 111 */               .then(
/* 112 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/* 113 */                 .suggests(SUGGEST_ADVANCEMENTS)
/* 114 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.UNTIL))))))
/*     */ 
/*     */             
/* 117 */             .then(
/* 118 */               Commands.literal("through")
/* 119 */               .then(
/* 120 */                 Commands.argument("advancement", (ArgumentType)ResourceLocationArgument.id())
/* 121 */                 .suggests(SUGGEST_ADVANCEMENTS)
/* 122 */                 .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(debug0, "advancement"), Mode.THROUGH))))))
/*     */ 
/*     */             
/* 125 */             .then(
/* 126 */               Commands.literal("everything")
/* 127 */               .executes(debug0 -> perform((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), Action.REVOKE, ((CommandSourceStack)debug0.getSource()).getServer().getAdvancements().getAllAdvancements()))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int perform(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Action debug2, Collection<Advancement> debug3) {
/* 135 */     int debug4 = 0;
/* 136 */     for (ServerPlayer debug6 : debug1) {
/* 137 */       debug4 += debug2.perform(debug6, debug3);
/*     */     }
/*     */     
/* 140 */     if (debug4 == 0) {
/* 141 */       if (debug3.size() == 1) {
/* 142 */         if (debug1.size() == 1) {
/* 143 */           throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".one.to.one.failure", new Object[] { ((Advancement)debug3.iterator().next()).getChatComponent(), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }));
/*     */         }
/* 145 */         throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".one.to.many.failure", new Object[] { ((Advancement)debug3.iterator().next()).getChatComponent(), Integer.valueOf(debug1.size()) }));
/*     */       } 
/*     */       
/* 148 */       if (debug1.size() == 1) {
/* 149 */         throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".many.to.one.failure", new Object[] { Integer.valueOf(debug3.size()), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }));
/*     */       }
/* 151 */       throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".many.to.many.failure", new Object[] { Integer.valueOf(debug3.size()), Integer.valueOf(debug1.size()) }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 156 */     if (debug3.size() == 1) {
/* 157 */       if (debug1.size() == 1) {
/* 158 */         debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".one.to.one.success", new Object[] { ((Advancement)debug3.iterator().next()).getChatComponent(), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */       } else {
/* 160 */         debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".one.to.many.success", new Object[] { ((Advancement)debug3.iterator().next()).getChatComponent(), Integer.valueOf(debug1.size()) }), true);
/*     */       }
/*     */     
/* 163 */     } else if (debug1.size() == 1) {
/* 164 */       debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".many.to.one.success", new Object[] { Integer.valueOf(debug3.size()), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 166 */       debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".many.to.many.success", new Object[] { Integer.valueOf(debug3.size()), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */ 
/*     */     
/* 170 */     return debug4;
/*     */   }
/*     */   
/*     */   private static int performCriterion(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Action debug2, Advancement debug3, String debug4) {
/* 174 */     int debug5 = 0;
/*     */     
/* 176 */     if (!debug3.getCriteria().containsKey(debug4)) {
/* 177 */       throw new CommandRuntimeException(new TranslatableComponent("commands.advancement.criterionNotFound", new Object[] { debug3.getChatComponent(), debug4 }));
/*     */     }
/*     */     
/* 180 */     for (ServerPlayer debug7 : debug1) {
/* 181 */       if (debug2.performCriterion(debug7, debug3, debug4)) {
/* 182 */         debug5++;
/*     */       }
/*     */     } 
/*     */     
/* 186 */     if (debug5 == 0) {
/* 187 */       if (debug1.size() == 1) {
/* 188 */         throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".criterion.to.one.failure", new Object[] { debug4, debug3.getChatComponent(), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }));
/*     */       }
/* 190 */       throw new CommandRuntimeException(new TranslatableComponent(debug2.getKey() + ".criterion.to.many.failure", new Object[] { debug4, debug3.getChatComponent(), Integer.valueOf(debug1.size()) }));
/*     */     } 
/*     */ 
/*     */     
/* 194 */     if (debug1.size() == 1) {
/* 195 */       debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".criterion.to.one.success", new Object[] { debug4, debug3.getChatComponent(), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 197 */       debug0.sendSuccess((Component)new TranslatableComponent(debug2.getKey() + ".criterion.to.many.success", new Object[] { debug4, debug3.getChatComponent(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 200 */     return debug5;
/*     */   }
/*     */   
/*     */   private static List<Advancement> getAdvancements(Advancement debug0, Mode debug1) {
/* 204 */     List<Advancement> debug2 = Lists.newArrayList();
/* 205 */     if (debug1.parents) {
/* 206 */       Advancement debug3 = debug0.getParent();
/* 207 */       while (debug3 != null) {
/* 208 */         debug2.add(debug3);
/* 209 */         debug3 = debug3.getParent();
/*     */       } 
/*     */     } 
/* 212 */     debug2.add(debug0);
/* 213 */     if (debug1.children) {
/* 214 */       addChildren(debug0, debug2);
/*     */     }
/* 216 */     return debug2;
/*     */   }
/*     */   
/*     */   private static void addChildren(Advancement debug0, List<Advancement> debug1) {
/* 220 */     for (Advancement debug3 : debug0.getChildren()) {
/* 221 */       debug1.add(debug3);
/* 222 */       addChildren(debug3, debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   enum Action {
/* 227 */     GRANT("grant")
/*     */     {
/*     */       protected boolean perform(ServerPlayer debug1, Advancement debug2) {
/* 230 */         AdvancementProgress debug3 = debug1.getAdvancements().getOrStartProgress(debug2);
/* 231 */         if (debug3.isDone()) {
/* 232 */           return false;
/*     */         }
/* 234 */         for (String debug5 : debug3.getRemainingCriteria()) {
/* 235 */           debug1.getAdvancements().award(debug2, debug5);
/*     */         }
/* 237 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer debug1, Advancement debug2, String debug3) {
/* 242 */         return debug1.getAdvancements().award(debug2, debug3);
/*     */       }
/*     */     },
/* 245 */     REVOKE("revoke")
/*     */     {
/*     */       protected boolean perform(ServerPlayer debug1, Advancement debug2) {
/* 248 */         AdvancementProgress debug3 = debug1.getAdvancements().getOrStartProgress(debug2);
/* 249 */         if (!debug3.hasProgress()) {
/* 250 */           return false;
/*     */         }
/* 252 */         for (String debug5 : debug3.getCompletedCriteria()) {
/* 253 */           debug1.getAdvancements().revoke(debug2, debug5);
/*     */         }
/* 255 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer debug1, Advancement debug2, String debug3) {
/* 260 */         return debug1.getAdvancements().revoke(debug2, debug3);
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     private final String key;
/*     */     
/*     */     Action(String debug3) {
/* 268 */       this.key = "commands.advancement." + debug3;
/*     */     }
/*     */     
/*     */     public int perform(ServerPlayer debug1, Iterable<Advancement> debug2) {
/* 272 */       int debug3 = 0;
/* 273 */       for (Advancement debug5 : debug2) {
/* 274 */         if (perform(debug1, debug5)) {
/* 275 */           debug3++;
/*     */         }
/*     */       } 
/* 278 */       return debug3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getKey() {
/* 286 */       return this.key;
/*     */     }
/*     */     protected abstract boolean perform(ServerPlayer param1ServerPlayer, Advancement param1Advancement);
/*     */     protected abstract boolean performCriterion(ServerPlayer param1ServerPlayer, Advancement param1Advancement, String param1String); }
/*     */   
/* 291 */   enum Mode { ONLY(false, false),
/* 292 */     THROUGH(true, true),
/* 293 */     FROM(false, true),
/* 294 */     UNTIL(true, false),
/* 295 */     EVERYTHING(true, true);
/*     */     
/*     */     private final boolean parents;
/*     */     
/*     */     private final boolean children;
/*     */     
/*     */     Mode(boolean debug3, boolean debug4) {
/* 302 */       this.parents = debug3;
/* 303 */       this.children = debug4;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\AdvancementCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */