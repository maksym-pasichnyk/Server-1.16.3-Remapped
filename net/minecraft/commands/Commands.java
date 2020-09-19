/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypes;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.gametest.framework.TestCommand;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.server.commands.AdvancementCommands;
/*     */ import net.minecraft.server.commands.AttributeCommand;
/*     */ import net.minecraft.server.commands.BanIpCommands;
/*     */ import net.minecraft.server.commands.BanListCommands;
/*     */ import net.minecraft.server.commands.BanPlayerCommands;
/*     */ import net.minecraft.server.commands.BossBarCommands;
/*     */ import net.minecraft.server.commands.ClearInventoryCommands;
/*     */ import net.minecraft.server.commands.CloneCommands;
/*     */ import net.minecraft.server.commands.DataPackCommand;
/*     */ import net.minecraft.server.commands.DeOpCommands;
/*     */ import net.minecraft.server.commands.DebugCommand;
/*     */ import net.minecraft.server.commands.DefaultGameModeCommands;
/*     */ import net.minecraft.server.commands.DifficultyCommand;
/*     */ import net.minecraft.server.commands.EffectCommands;
/*     */ import net.minecraft.server.commands.EmoteCommands;
/*     */ import net.minecraft.server.commands.EnchantCommand;
/*     */ import net.minecraft.server.commands.ExecuteCommand;
/*     */ import net.minecraft.server.commands.ExperienceCommand;
/*     */ import net.minecraft.server.commands.FillCommand;
/*     */ import net.minecraft.server.commands.ForceLoadCommand;
/*     */ import net.minecraft.server.commands.FunctionCommand;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.server.commands.GameRuleCommand;
/*     */ import net.minecraft.server.commands.GiveCommand;
/*     */ import net.minecraft.server.commands.HelpCommand;
/*     */ import net.minecraft.server.commands.KickCommand;
/*     */ import net.minecraft.server.commands.KillCommand;
/*     */ import net.minecraft.server.commands.ListPlayersCommand;
/*     */ import net.minecraft.server.commands.LocateBiomeCommand;
/*     */ import net.minecraft.server.commands.LocateCommand;
/*     */ import net.minecraft.server.commands.LootCommand;
/*     */ import net.minecraft.server.commands.MsgCommand;
/*     */ import net.minecraft.server.commands.OpCommand;
/*     */ import net.minecraft.server.commands.PardonCommand;
/*     */ import net.minecraft.server.commands.PardonIpCommand;
/*     */ import net.minecraft.server.commands.ParticleCommand;
/*     */ import net.minecraft.server.commands.PlaySoundCommand;
/*     */ import net.minecraft.server.commands.PublishCommand;
/*     */ import net.minecraft.server.commands.RecipeCommand;
/*     */ import net.minecraft.server.commands.ReloadCommand;
/*     */ import net.minecraft.server.commands.ReplaceItemCommand;
/*     */ import net.minecraft.server.commands.SaveAllCommand;
/*     */ import net.minecraft.server.commands.SaveOffCommand;
/*     */ import net.minecraft.server.commands.SaveOnCommand;
/*     */ import net.minecraft.server.commands.SayCommand;
/*     */ import net.minecraft.server.commands.ScheduleCommand;
/*     */ import net.minecraft.server.commands.ScoreboardCommand;
/*     */ import net.minecraft.server.commands.SeedCommand;
/*     */ import net.minecraft.server.commands.SetBlockCommand;
/*     */ import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
/*     */ import net.minecraft.server.commands.SetSpawnCommand;
/*     */ import net.minecraft.server.commands.SetWorldSpawnCommand;
/*     */ import net.minecraft.server.commands.SpectateCommand;
/*     */ import net.minecraft.server.commands.SpreadPlayersCommand;
/*     */ import net.minecraft.server.commands.StopCommand;
/*     */ import net.minecraft.server.commands.StopSoundCommand;
/*     */ import net.minecraft.server.commands.SummonCommand;
/*     */ import net.minecraft.server.commands.TagCommand;
/*     */ import net.minecraft.server.commands.TeamCommand;
/*     */ import net.minecraft.server.commands.TeamMsgCommand;
/*     */ import net.minecraft.server.commands.TeleportCommand;
/*     */ import net.minecraft.server.commands.TellRawCommand;
/*     */ import net.minecraft.server.commands.TimeCommand;
/*     */ import net.minecraft.server.commands.TitleCommand;
/*     */ import net.minecraft.server.commands.TriggerCommand;
/*     */ import net.minecraft.server.commands.WeatherCommand;
/*     */ import net.minecraft.server.commands.WhitelistCommand;
/*     */ import net.minecraft.server.commands.WorldBorderCommand;
/*     */ import net.minecraft.server.commands.data.DataCommands;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Commands
/*     */ {
/* 116 */   private static final Logger LOGGER = LogManager.getLogger();
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
/* 129 */   private final CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher();
/*     */   
/*     */   public enum CommandSelection {
/* 132 */     ALL(true, true),
/* 133 */     DEDICATED(false, true),
/* 134 */     INTEGRATED(true, false);
/*     */     
/*     */     private final boolean includeIntegrated;
/*     */     
/*     */     private final boolean includeDedicated;
/*     */     
/*     */     CommandSelection(boolean debug3, boolean debug4) {
/* 141 */       this.includeIntegrated = debug3;
/* 142 */       this.includeDedicated = debug4;
/*     */     }
/*     */   }
/*     */   
/*     */   public Commands(CommandSelection debug1) {
/* 147 */     AdvancementCommands.register(this.dispatcher);
/* 148 */     AttributeCommand.register(this.dispatcher);
/* 149 */     ExecuteCommand.register(this.dispatcher);
/* 150 */     BossBarCommands.register(this.dispatcher);
/* 151 */     ClearInventoryCommands.register(this.dispatcher);
/* 152 */     CloneCommands.register(this.dispatcher);
/* 153 */     DataCommands.register(this.dispatcher);
/* 154 */     DataPackCommand.register(this.dispatcher);
/* 155 */     DebugCommand.register(this.dispatcher);
/* 156 */     DefaultGameModeCommands.register(this.dispatcher);
/* 157 */     DifficultyCommand.register(this.dispatcher);
/* 158 */     EffectCommands.register(this.dispatcher);
/* 159 */     EmoteCommands.register(this.dispatcher);
/* 160 */     EnchantCommand.register(this.dispatcher);
/* 161 */     ExperienceCommand.register(this.dispatcher);
/* 162 */     FillCommand.register(this.dispatcher);
/* 163 */     ForceLoadCommand.register(this.dispatcher);
/* 164 */     FunctionCommand.register(this.dispatcher);
/* 165 */     GameModeCommand.register(this.dispatcher);
/* 166 */     GameRuleCommand.register(this.dispatcher);
/* 167 */     GiveCommand.register(this.dispatcher);
/* 168 */     HelpCommand.register(this.dispatcher);
/* 169 */     KickCommand.register(this.dispatcher);
/* 170 */     KillCommand.register(this.dispatcher);
/* 171 */     ListPlayersCommand.register(this.dispatcher);
/* 172 */     LocateCommand.register(this.dispatcher);
/* 173 */     LocateBiomeCommand.register(this.dispatcher);
/* 174 */     LootCommand.register(this.dispatcher);
/* 175 */     MsgCommand.register(this.dispatcher);
/* 176 */     ParticleCommand.register(this.dispatcher);
/* 177 */     PlaySoundCommand.register(this.dispatcher);
/* 178 */     ReloadCommand.register(this.dispatcher);
/* 179 */     RecipeCommand.register(this.dispatcher);
/* 180 */     ReplaceItemCommand.register(this.dispatcher);
/* 181 */     SayCommand.register(this.dispatcher);
/* 182 */     ScheduleCommand.register(this.dispatcher);
/* 183 */     ScoreboardCommand.register(this.dispatcher);
/* 184 */     SeedCommand.register(this.dispatcher, (debug1 != CommandSelection.INTEGRATED));
/* 185 */     SetBlockCommand.register(this.dispatcher);
/* 186 */     SetSpawnCommand.register(this.dispatcher);
/* 187 */     SetWorldSpawnCommand.register(this.dispatcher);
/* 188 */     SpectateCommand.register(this.dispatcher);
/* 189 */     SpreadPlayersCommand.register(this.dispatcher);
/* 190 */     StopSoundCommand.register(this.dispatcher);
/* 191 */     SummonCommand.register(this.dispatcher);
/* 192 */     TagCommand.register(this.dispatcher);
/* 193 */     TeamCommand.register(this.dispatcher);
/* 194 */     TeamMsgCommand.register(this.dispatcher);
/* 195 */     TeleportCommand.register(this.dispatcher);
/* 196 */     TellRawCommand.register(this.dispatcher);
/* 197 */     TimeCommand.register(this.dispatcher);
/* 198 */     TitleCommand.register(this.dispatcher);
/* 199 */     TriggerCommand.register(this.dispatcher);
/* 200 */     WeatherCommand.register(this.dispatcher);
/* 201 */     WorldBorderCommand.register(this.dispatcher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 210 */       TestCommand.register(this.dispatcher);
/*     */     }
/*     */     
/* 213 */     if (debug1.includeDedicated) {
/* 214 */       BanIpCommands.register(this.dispatcher);
/* 215 */       BanListCommands.register(this.dispatcher);
/* 216 */       BanPlayerCommands.register(this.dispatcher);
/* 217 */       DeOpCommands.register(this.dispatcher);
/* 218 */       OpCommand.register(this.dispatcher);
/* 219 */       PardonCommand.register(this.dispatcher);
/* 220 */       PardonIpCommand.register(this.dispatcher);
/* 221 */       SaveAllCommand.register(this.dispatcher);
/* 222 */       SaveOffCommand.register(this.dispatcher);
/* 223 */       SaveOnCommand.register(this.dispatcher);
/* 224 */       SetPlayerIdleTimeoutCommand.register(this.dispatcher);
/* 225 */       StopCommand.register(this.dispatcher);
/* 226 */       WhitelistCommand.register(this.dispatcher);
/*     */     } 
/*     */     
/* 229 */     if (debug1.includeIntegrated) {
/* 230 */       PublishCommand.register(this.dispatcher);
/*     */     }
/*     */     
/* 233 */     this.dispatcher.findAmbiguities((debug1, debug2, debug3, debug4) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", this.dispatcher.getPath(debug2), this.dispatcher.getPath(debug3), debug4));
/*     */ 
/*     */ 
/*     */     
/* 237 */     this.dispatcher.setConsumer((debug0, debug1, debug2) -> ((CommandSourceStack)debug0.getSource()).onCommandComplete(debug0, debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int performCommand(CommandSourceStack debug1, String debug2) {
/* 243 */     StringReader debug3 = new StringReader(debug2);
/* 244 */     if (debug3.canRead() && debug3.peek() == '/') {
/* 245 */       debug3.skip();
/*     */     }
/* 247 */     debug1.getServer().getProfiler().push(debug2);
/*     */     try {
/* 249 */       return this.dispatcher.execute(debug3, debug1);
/* 250 */     } catch (CommandRuntimeException debug4) {
/* 251 */       debug1.sendFailure(debug4.getComponent());
/* 252 */       return 0;
/* 253 */     } catch (CommandSyntaxException debug4) {
/* 254 */       debug1.sendFailure(ComponentUtils.fromMessage(debug4.getRawMessage()));
/* 255 */       if (debug4.getInput() != null && debug4.getCursor() >= 0) {
/* 256 */         int debug5 = Math.min(debug4.getInput().length(), debug4.getCursor());
/* 257 */         MutableComponent debug6 = (new TextComponent("")).withStyle(ChatFormatting.GRAY).withStyle(debug1 -> debug1.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, debug0)));
/*     */ 
/*     */         
/* 260 */         if (debug5 > 10) {
/* 261 */           debug6.append("...");
/*     */         }
/* 263 */         debug6.append(debug4.getInput().substring(Math.max(0, debug5 - 10), debug5));
/* 264 */         if (debug5 < debug4.getInput().length()) {
/* 265 */           MutableComponent mutableComponent = (new TextComponent(debug4.getInput().substring(debug5))).withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.UNDERLINE });
/* 266 */           debug6.append((Component)mutableComponent);
/*     */         } 
/* 268 */         debug6.append((Component)(new TranslatableComponent("command.context.here")).withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }));
/* 269 */         debug1.sendFailure((Component)debug6);
/*     */       } 
/* 271 */       return 0;
/* 272 */     } catch (Exception debug4) {
/* 273 */       TextComponent textComponent = new TextComponent((debug4.getMessage() == null) ? debug4.getClass().getName() : debug4.getMessage());
/* 274 */       if (LOGGER.isDebugEnabled()) {
/* 275 */         LOGGER.error("Command exception: {}", debug2, debug4);
/* 276 */         StackTraceElement[] debug6 = debug4.getStackTrace();
/* 277 */         for (int debug7 = 0; debug7 < Math.min(debug6.length, 3); debug7++) {
/* 278 */           textComponent.append("\n\n")
/* 279 */             .append(debug6[debug7].getMethodName())
/* 280 */             .append("\n ")
/* 281 */             .append(debug6[debug7].getFileName())
/* 282 */             .append(":")
/* 283 */             .append(String.valueOf(debug6[debug7].getLineNumber()));
/*     */         }
/*     */       } 
/* 286 */       debug1.sendFailure((Component)(new TranslatableComponent("command.failed")).withStyle(debug1 -> debug1.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, debug0))));
/* 287 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 288 */         debug1.sendFailure((Component)new TextComponent(Util.describeError(debug4)));
/* 289 */         LOGGER.error("'" + debug2 + "' threw an exception", debug4);
/*     */       } 
/*     */       
/* 292 */       return 0;
/*     */     } finally {
/* 294 */       debug1.getServer().getProfiler().pop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendCommands(ServerPlayer debug1) {
/* 299 */     Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> debug2 = Maps.newHashMap();
/* 300 */     RootCommandNode<SharedSuggestionProvider> debug3 = new RootCommandNode();
/* 301 */     debug2.put(this.dispatcher.getRoot(), debug3);
/* 302 */     fillUsableCommands((CommandNode<CommandSourceStack>)this.dispatcher.getRoot(), (CommandNode<SharedSuggestionProvider>)debug3, debug1.createCommandSourceStack(), debug2);
/* 303 */     debug1.connection.send((Packet)new ClientboundCommandsPacket(debug3));
/*     */   }
/*     */   
/*     */   private void fillUsableCommands(CommandNode<CommandSourceStack> debug1, CommandNode<SharedSuggestionProvider> debug2, CommandSourceStack debug3, Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> debug4) {
/* 307 */     for (CommandNode<CommandSourceStack> debug6 : (Iterable<CommandNode<CommandSourceStack>>)debug1.getChildren()) {
/* 308 */       if (debug6.canUse(debug3)) {
/* 309 */         ArgumentBuilder<SharedSuggestionProvider, ?> debug7 = debug6.createBuilder();
/* 310 */         debug7.requires(debug0 -> true);
/* 311 */         if (debug7.getCommand() != null)
/*     */         {
/*     */           
/* 314 */           debug7.executes(debug0 -> 0);
/*     */         }
/* 316 */         if (debug7 instanceof RequiredArgumentBuilder) {
/* 317 */           RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder = (RequiredArgumentBuilder<SharedSuggestionProvider, ?>)debug7;
/* 318 */           if (requiredArgumentBuilder.getSuggestionsProvider() != null)
/*     */           {
/*     */             
/* 321 */             requiredArgumentBuilder.suggests(SuggestionProviders.safelySwap(requiredArgumentBuilder.getSuggestionsProvider()));
/*     */           }
/*     */         } 
/* 324 */         if (debug7.getRedirect() != null) {
/* 325 */           debug7.redirect(debug4.get(debug7.getRedirect()));
/*     */         }
/* 327 */         CommandNode<SharedSuggestionProvider> debug8 = debug7.build();
/* 328 */         debug4.put(debug6, debug8);
/* 329 */         debug2.addChild(debug8);
/* 330 */         if (!debug6.getChildren().isEmpty()) {
/* 331 */           fillUsableCommands(debug6, debug8, debug3, debug4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static LiteralArgumentBuilder<CommandSourceStack> literal(String debug0) {
/* 338 */     return LiteralArgumentBuilder.literal(debug0);
/*     */   }
/*     */   
/*     */   public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String debug0, ArgumentType<T> debug1) {
/* 342 */     return RequiredArgumentBuilder.argument(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static Predicate<String> createValidator(ParseFunction debug0) {
/* 346 */     return debug1 -> {
/*     */         try {
/*     */           debug0.parse(new StringReader(debug1));
/*     */           return true;
/* 350 */         } catch (CommandSyntaxException debug2) {
/*     */           return false;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public CommandDispatcher<CommandSourceStack> getDispatcher() {
/* 357 */     return this.dispatcher;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static <S> CommandSyntaxException getParseException(ParseResults<S> debug0) {
/* 362 */     if (!debug0.getReader().canRead())
/* 363 */       return null; 
/* 364 */     if (debug0.getExceptions().size() == 1)
/* 365 */       return debug0.getExceptions().values().iterator().next(); 
/* 366 */     if (debug0.getContext().getRange().isEmpty()) {
/* 367 */       return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(debug0.getReader());
/*     */     }
/* 369 */     return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(debug0.getReader());
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 373 */     RootCommandNode<CommandSourceStack> debug0 = (new Commands(CommandSelection.ALL)).getDispatcher().getRoot();
/* 374 */     Set<ArgumentType<?>> debug1 = ArgumentTypes.findUsedArgumentTypes((CommandNode)debug0);
/* 375 */     Set<ArgumentType<?>> debug2 = (Set<ArgumentType<?>>)debug1.stream().filter(debug0 -> !ArgumentTypes.isTypeRegistered(debug0)).collect(Collectors.toSet());
/* 376 */     if (!debug2.isEmpty()) {
/* 377 */       LOGGER.warn("Missing type registration for following arguments:\n {}", debug2.stream().map(debug0 -> "\t" + debug0).collect(Collectors.joining(",\n")));
/* 378 */       throw new IllegalStateException("Unregistered argument types");
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ParseFunction {
/*     */     void parse(StringReader param1StringReader) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\Commands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */