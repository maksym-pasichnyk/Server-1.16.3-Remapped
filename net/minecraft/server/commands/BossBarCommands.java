/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.bossevents.CustomBossEvent;
/*     */ import net.minecraft.server.bossevents.CustomBossEvents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class BossBarCommands {
/*     */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
/*     */   private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST;
/*     */   
/*     */   static {
/*  39 */     ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.bossbar.create.failed", new Object[] { debug0 }));
/*  40 */     ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.bossbar.unknown", new Object[] { debug0 }));
/*  41 */   } private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.players.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.name.unchanged"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.color.unchanged"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.style.unchanged"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.value.unchanged"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.max.unchanged"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.visibility.unchanged.hidden")); public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR;
/*  48 */   private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.visibility.unchanged.visible")); static {
/*  49 */     SUGGEST_BOSS_BAR = ((debug0, debug1) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)debug0.getSource()).getServer().getCustomBossEvents().getIds(), debug1));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  52 */     debug0.register(
/*  53 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bossbar")
/*  54 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  55 */         .then(
/*  56 */           Commands.literal("add")
/*  57 */           .then(
/*  58 */             Commands.argument("id", (ArgumentType)ResourceLocationArgument.id())
/*  59 */             .then(
/*  60 */               Commands.argument("name", (ArgumentType)ComponentArgument.textComponent())
/*  61 */               .executes(debug0 -> createBar((CommandSourceStack)debug0.getSource(), ResourceLocationArgument.getId(debug0, "id"), ComponentArgument.getComponent(debug0, "name")))))))
/*     */ 
/*     */ 
/*     */         
/*  65 */         .then(
/*  66 */           Commands.literal("remove")
/*  67 */           .then(
/*  68 */             Commands.argument("id", (ArgumentType)ResourceLocationArgument.id())
/*  69 */             .suggests(SUGGEST_BOSS_BAR)
/*  70 */             .executes(debug0 -> removeBar((CommandSourceStack)debug0.getSource(), getBossBar(debug0))))))
/*     */ 
/*     */         
/*  73 */         .then(
/*  74 */           Commands.literal("list")
/*  75 */           .executes(debug0 -> listBars((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  77 */         .then(
/*  78 */           Commands.literal("set")
/*  79 */           .then((
/*  80 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)ResourceLocationArgument.id())
/*  81 */             .suggests(SUGGEST_BOSS_BAR)
/*  82 */             .then(
/*  83 */               Commands.literal("name")
/*  84 */               .then(
/*  85 */                 Commands.argument("name", (ArgumentType)ComponentArgument.textComponent())
/*  86 */                 .executes(debug0 -> setName((CommandSourceStack)debug0.getSource(), getBossBar(debug0), ComponentArgument.getComponent(debug0, "name"))))))
/*     */ 
/*     */             
/*  89 */             .then((
/*  90 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  91 */               .then(
/*  92 */                 Commands.literal("pink")
/*  93 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.PINK))))
/*     */               
/*  95 */               .then(
/*  96 */                 Commands.literal("blue")
/*  97 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.BLUE))))
/*     */               
/*  99 */               .then(
/* 100 */                 Commands.literal("red")
/* 101 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.RED))))
/*     */               
/* 103 */               .then(
/* 104 */                 Commands.literal("green")
/* 105 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.GREEN))))
/*     */               
/* 107 */               .then(
/* 108 */                 Commands.literal("yellow")
/* 109 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.YELLOW))))
/*     */               
/* 111 */               .then(
/* 112 */                 Commands.literal("purple")
/* 113 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.PURPLE))))
/*     */               
/* 115 */               .then(
/* 116 */                 Commands.literal("white")
/* 117 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarColor.WHITE)))))
/*     */ 
/*     */             
/* 120 */             .then((
/* 121 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("style")
/* 122 */               .then(
/* 123 */                 Commands.literal("progress")
/* 124 */                 .executes(debug0 -> setStyle((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarOverlay.PROGRESS))))
/*     */               
/* 126 */               .then(
/* 127 */                 Commands.literal("notched_6")
/* 128 */                 .executes(debug0 -> setStyle((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarOverlay.NOTCHED_6))))
/*     */               
/* 130 */               .then(
/* 131 */                 Commands.literal("notched_10")
/* 132 */                 .executes(debug0 -> setStyle((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarOverlay.NOTCHED_10))))
/*     */               
/* 134 */               .then(
/* 135 */                 Commands.literal("notched_12")
/* 136 */                 .executes(debug0 -> setStyle((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarOverlay.NOTCHED_12))))
/*     */               
/* 138 */               .then(
/* 139 */                 Commands.literal("notched_20")
/* 140 */                 .executes(debug0 -> setStyle((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BossEvent.BossBarOverlay.NOTCHED_20)))))
/*     */ 
/*     */             
/* 143 */             .then(
/* 144 */               Commands.literal("value")
/* 145 */               .then(
/* 146 */                 Commands.argument("value", (ArgumentType)IntegerArgumentType.integer(0))
/* 147 */                 .executes(debug0 -> setValue((CommandSourceStack)debug0.getSource(), getBossBar(debug0), IntegerArgumentType.getInteger(debug0, "value"))))))
/*     */ 
/*     */             
/* 150 */             .then(
/* 151 */               Commands.literal("max")
/* 152 */               .then(
/* 153 */                 Commands.argument("max", (ArgumentType)IntegerArgumentType.integer(1))
/* 154 */                 .executes(debug0 -> setMax((CommandSourceStack)debug0.getSource(), getBossBar(debug0), IntegerArgumentType.getInteger(debug0, "max"))))))
/*     */ 
/*     */             
/* 157 */             .then(
/* 158 */               Commands.literal("visible")
/* 159 */               .then(
/* 160 */                 Commands.argument("visible", (ArgumentType)BoolArgumentType.bool())
/* 161 */                 .executes(debug0 -> setVisible((CommandSourceStack)debug0.getSource(), getBossBar(debug0), BoolArgumentType.getBool(debug0, "visible"))))))
/*     */ 
/*     */             
/* 164 */             .then((
/* 165 */               (LiteralArgumentBuilder)Commands.literal("players")
/* 166 */               .executes(debug0 -> setPlayers((CommandSourceStack)debug0.getSource(), getBossBar(debug0), Collections.emptyList())))
/* 167 */               .then(
/* 168 */                 Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 169 */                 .executes(debug0 -> setPlayers((CommandSourceStack)debug0.getSource(), getBossBar(debug0), EntityArgument.getOptionalPlayers(debug0, "targets"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         .then(
/* 175 */           Commands.literal("get")
/* 176 */           .then((
/* 177 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)ResourceLocationArgument.id())
/* 178 */             .suggests(SUGGEST_BOSS_BAR)
/* 179 */             .then(
/* 180 */               Commands.literal("value")
/* 181 */               .executes(debug0 -> getValue((CommandSourceStack)debug0.getSource(), getBossBar(debug0)))))
/*     */             
/* 183 */             .then(
/* 184 */               Commands.literal("max")
/* 185 */               .executes(debug0 -> getMax((CommandSourceStack)debug0.getSource(), getBossBar(debug0)))))
/*     */             
/* 187 */             .then(
/* 188 */               Commands.literal("visible")
/* 189 */               .executes(debug0 -> getVisible((CommandSourceStack)debug0.getSource(), getBossBar(debug0)))))
/*     */             
/* 191 */             .then(
/* 192 */               Commands.literal("players")
/* 193 */               .executes(debug0 -> getPlayers((CommandSourceStack)debug0.getSource(), getBossBar(debug0)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getValue(CommandSourceStack debug0, CustomBossEvent debug1) {
/* 201 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.value", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug1.getValue()) }), true);
/* 202 */     return debug1.getValue();
/*     */   }
/*     */   
/*     */   private static int getMax(CommandSourceStack debug0, CustomBossEvent debug1) {
/* 206 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.max", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug1.getMax()) }), true);
/* 207 */     return debug1.getMax();
/*     */   }
/*     */   
/*     */   private static int getVisible(CommandSourceStack debug0, CustomBossEvent debug1) {
/* 211 */     if (debug1.isVisible()) {
/* 212 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.visible.visible", new Object[] { debug1.getDisplayName() }), true);
/* 213 */       return 1;
/*     */     } 
/* 215 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.visible.hidden", new Object[] { debug1.getDisplayName() }), true);
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getPlayers(CommandSourceStack debug0, CustomBossEvent debug1) {
/* 221 */     if (debug1.getPlayers().isEmpty()) {
/* 222 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.players.none", new Object[] { debug1.getDisplayName() }), true);
/*     */     } else {
/* 224 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.get.players.some", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug1.getPlayers().size()), ComponentUtils.formatList(debug1.getPlayers(), Player::getDisplayName) }), true);
/*     */     } 
/* 226 */     return debug1.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int setVisible(CommandSourceStack debug0, CustomBossEvent debug1, boolean debug2) throws CommandSyntaxException {
/* 230 */     if (debug1.isVisible() == debug2) {
/* 231 */       if (debug2) {
/* 232 */         throw ERROR_ALREADY_VISIBLE.create();
/*     */       }
/* 234 */       throw ERROR_ALREADY_HIDDEN.create();
/*     */     } 
/*     */     
/* 237 */     debug1.setVisible(debug2);
/* 238 */     if (debug2) {
/* 239 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.visible.success.visible", new Object[] { debug1.getDisplayName() }), true);
/*     */     } else {
/* 241 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.visible.success.hidden", new Object[] { debug1.getDisplayName() }), true);
/*     */     } 
/* 243 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack debug0, CustomBossEvent debug1, int debug2) throws CommandSyntaxException {
/* 247 */     if (debug1.getValue() == debug2) {
/* 248 */       throw ERROR_NO_VALUE_CHANGE.create();
/*     */     }
/* 250 */     debug1.setValue(debug2);
/* 251 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.value.success", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug2) }), true);
/* 252 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int setMax(CommandSourceStack debug0, CustomBossEvent debug1, int debug2) throws CommandSyntaxException {
/* 256 */     if (debug1.getMax() == debug2) {
/* 257 */       throw ERROR_NO_MAX_CHANGE.create();
/*     */     }
/* 259 */     debug1.setMax(debug2);
/* 260 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.max.success", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug2) }), true);
/* 261 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack debug0, CustomBossEvent debug1, BossEvent.BossBarColor debug2) throws CommandSyntaxException {
/* 265 */     if (debug1.getColor().equals(debug2)) {
/* 266 */       throw ERROR_NO_COLOR_CHANGE.create();
/*     */     }
/* 268 */     debug1.setColor(debug2);
/* 269 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.color.success", new Object[] { debug1.getDisplayName() }), true);
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setStyle(CommandSourceStack debug0, CustomBossEvent debug1, BossEvent.BossBarOverlay debug2) throws CommandSyntaxException {
/* 274 */     if (debug1.getOverlay().equals(debug2)) {
/* 275 */       throw ERROR_NO_STYLE_CHANGE.create();
/*     */     }
/* 277 */     debug1.setOverlay(debug2);
/* 278 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.style.success", new Object[] { debug1.getDisplayName() }), true);
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setName(CommandSourceStack debug0, CustomBossEvent debug1, Component debug2) throws CommandSyntaxException {
/* 283 */     MutableComponent mutableComponent = ComponentUtils.updateForEntity(debug0, debug2, null, 0);
/* 284 */     if (debug1.getName().equals(mutableComponent)) {
/* 285 */       throw ERROR_NO_NAME_CHANGE.create();
/*     */     }
/* 287 */     debug1.setName((Component)mutableComponent);
/* 288 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.name.success", new Object[] { debug1.getDisplayName() }), true);
/* 289 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setPlayers(CommandSourceStack debug0, CustomBossEvent debug1, Collection<ServerPlayer> debug2) throws CommandSyntaxException {
/* 293 */     boolean debug3 = debug1.setPlayers(debug2);
/* 294 */     if (!debug3) {
/* 295 */       throw ERROR_NO_PLAYER_CHANGE.create();
/*     */     }
/* 297 */     if (debug1.getPlayers().isEmpty()) {
/* 298 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.players.success.none", new Object[] { debug1.getDisplayName() }), true);
/*     */     } else {
/* 300 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.set.players.success.some", new Object[] { debug1.getDisplayName(), Integer.valueOf(debug2.size()), ComponentUtils.formatList(debug2, Player::getDisplayName) }), true);
/*     */     } 
/* 302 */     return debug1.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int listBars(CommandSourceStack debug0) {
/* 306 */     Collection<CustomBossEvent> debug1 = debug0.getServer().getCustomBossEvents().getEvents();
/* 307 */     if (debug1.isEmpty()) {
/* 308 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.list.bars.none"), false);
/*     */     } else {
/* 310 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.list.bars.some", new Object[] { Integer.valueOf(debug1.size()), ComponentUtils.formatList(debug1, CustomBossEvent::getDisplayName) }), false);
/*     */     } 
/* 312 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int createBar(CommandSourceStack debug0, ResourceLocation debug1, Component debug2) throws CommandSyntaxException {
/* 316 */     CustomBossEvents debug3 = debug0.getServer().getCustomBossEvents();
/* 317 */     if (debug3.get(debug1) != null) {
/* 318 */       throw ERROR_ALREADY_EXISTS.create(debug1.toString());
/*     */     }
/* 320 */     CustomBossEvent debug4 = debug3.create(debug1, (Component)ComponentUtils.updateForEntity(debug0, debug2, null, 0));
/* 321 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.create.success", new Object[] { debug4.getDisplayName() }), true);
/* 322 */     return debug3.getEvents().size();
/*     */   }
/*     */   
/*     */   private static int removeBar(CommandSourceStack debug0, CustomBossEvent debug1) {
/* 326 */     CustomBossEvents debug2 = debug0.getServer().getCustomBossEvents();
/* 327 */     debug1.removeAllPlayers();
/* 328 */     debug2.remove(debug1);
/* 329 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.bossbar.remove.success", new Object[] { debug1.getDisplayName() }), true);
/* 330 */     return debug2.getEvents().size();
/*     */   }
/*     */   
/*     */   public static CustomBossEvent getBossBar(CommandContext<CommandSourceStack> debug0) throws CommandSyntaxException {
/* 334 */     ResourceLocation debug1 = ResourceLocationArgument.getId(debug0, "id");
/* 335 */     CustomBossEvent debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getCustomBossEvents().get(debug1);
/* 336 */     if (debug2 == null) {
/* 337 */       throw ERROR_DOESNT_EXIST.create(debug1.toString());
/*     */     }
/* 339 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\BossBarCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */