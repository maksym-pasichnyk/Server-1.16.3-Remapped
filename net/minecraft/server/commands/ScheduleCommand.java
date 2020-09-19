/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.timers.FunctionCallback;
/*     */ import net.minecraft.world.level.timers.FunctionTagCallback;
/*     */ import net.minecraft.world.level.timers.TimerCallback;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ public class ScheduleCommand {
/*  32 */   private static final SimpleCommandExceptionType ERROR_SAME_TICK = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.schedule.same_tick")); private static final DynamicCommandExceptionType ERROR_CANT_REMOVE; private static final SuggestionProvider<CommandSourceStack> SUGGEST_SCHEDULE; static {
/*  33 */     ERROR_CANT_REMOVE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.schedule.cleared.failure", new Object[] { debug0 }));
/*     */     
/*  35 */     SUGGEST_SCHEDULE = ((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getWorldData().overworldData().getScheduledEvents().getEventsIds(), debug1));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  38 */     debug0.register(
/*  39 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("schedule")
/*  40 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  41 */         .then(
/*  42 */           Commands.literal("function")
/*  43 */           .then(
/*  44 */             Commands.argument("function", (ArgumentType)FunctionArgument.functions())
/*  45 */             .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  46 */             .then((
/*  47 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("time", (ArgumentType)TimeArgument.time())
/*  48 */               .executes(debug0 -> schedule((CommandSourceStack)debug0.getSource(), FunctionArgument.getFunctionOrTag(debug0, "function"), IntegerArgumentType.getInteger(debug0, "time"), true)))
/*  49 */               .then(
/*  50 */                 Commands.literal("append")
/*  51 */                 .executes(debug0 -> schedule((CommandSourceStack)debug0.getSource(), FunctionArgument.getFunctionOrTag(debug0, "function"), IntegerArgumentType.getInteger(debug0, "time"), false))))
/*     */               
/*  53 */               .then(
/*  54 */                 Commands.literal("replace")
/*  55 */                 .executes(debug0 -> schedule((CommandSourceStack)debug0.getSource(), FunctionArgument.getFunctionOrTag(debug0, "function"), IntegerArgumentType.getInteger(debug0, "time"), true)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  60 */         .then(
/*  61 */           Commands.literal("clear")
/*  62 */           .then(
/*  63 */             Commands.argument("function", (ArgumentType)StringArgumentType.greedyString())
/*  64 */             .suggests(SUGGEST_SCHEDULE)
/*  65 */             .executes(debug0 -> remove((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "function"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int schedule(CommandSourceStack debug0, Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> debug1, int debug2, boolean debug3) throws CommandSyntaxException {
/*  72 */     if (debug2 == 0) {
/*  73 */       throw ERROR_SAME_TICK.create();
/*     */     }
/*     */     
/*  76 */     long debug4 = debug0.getLevel().getGameTime() + debug2;
/*     */     
/*  78 */     ResourceLocation debug6 = (ResourceLocation)debug1.getFirst();
/*  79 */     TimerQueue<MinecraftServer> debug7 = debug0.getServer().getWorldData().overworldData().getScheduledEvents();
/*  80 */     ((Either)debug1.getSecond())
/*  81 */       .ifLeft(debug7 -> {
/*     */           String debug8 = debug0.toString();
/*     */           
/*     */           if (debug1) {
/*     */             debug2.remove(debug8);
/*     */           }
/*     */           debug2.schedule(debug8, debug3, (TimerCallback)new FunctionCallback(debug0));
/*     */           debug5.sendSuccess((Component)new TranslatableComponent("commands.schedule.created.function", new Object[] { debug0, Integer.valueOf(debug6), Long.valueOf(debug3) }), true);
/*  89 */         }).ifRight(debug7 -> {
/*     */           String debug8 = "#" + debug0.toString();
/*     */           
/*     */           if (debug1) {
/*     */             debug2.remove(debug8);
/*     */           }
/*     */           debug2.schedule(debug8, debug3, (TimerCallback)new FunctionTagCallback(debug0));
/*     */           debug5.sendSuccess((Component)new TranslatableComponent("commands.schedule.created.tag", new Object[] { debug0, Integer.valueOf(debug6), Long.valueOf(debug3) }), true);
/*     */         });
/*  98 */     return (int)Math.floorMod(debug4, 2147483647L);
/*     */   }
/*     */   
/*     */   private static int remove(CommandSourceStack debug0, String debug1) throws CommandSyntaxException {
/* 102 */     int debug2 = debug0.getServer().getWorldData().overworldData().getScheduledEvents().remove(debug1);
/* 103 */     if (debug2 == 0) {
/* 104 */       throw ERROR_CANT_REMOVE.create(debug1);
/*     */     }
/* 106 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.schedule.cleared.success", new Object[] { Integer.valueOf(debug2), debug1 }), true);
/* 107 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ScheduleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */