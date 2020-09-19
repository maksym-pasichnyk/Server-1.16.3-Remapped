/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ 
/*    */ public class WeatherCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 18 */     debug0.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("weather")
/* 20 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 21 */         .then((
/* 22 */           (LiteralArgumentBuilder)Commands.literal("clear")
/* 23 */           .executes(debug0 -> setClear((CommandSourceStack)debug0.getSource(), 6000)))
/* 24 */           .then(
/* 25 */             Commands.argument("duration", (ArgumentType)IntegerArgumentType.integer(0, 1000000))
/* 26 */             .executes(debug0 -> setClear((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "duration") * 20)))))
/*    */ 
/*    */         
/* 29 */         .then((
/* 30 */           (LiteralArgumentBuilder)Commands.literal("rain")
/* 31 */           .executes(debug0 -> setRain((CommandSourceStack)debug0.getSource(), 6000)))
/* 32 */           .then(
/* 33 */             Commands.argument("duration", (ArgumentType)IntegerArgumentType.integer(0, 1000000))
/* 34 */             .executes(debug0 -> setRain((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "duration") * 20)))))
/*    */ 
/*    */         
/* 37 */         .then((
/* 38 */           (LiteralArgumentBuilder)Commands.literal("thunder")
/* 39 */           .executes(debug0 -> setThunder((CommandSourceStack)debug0.getSource(), 6000)))
/* 40 */           .then(
/* 41 */             Commands.argument("duration", (ArgumentType)IntegerArgumentType.integer(0, 1000000))
/* 42 */             .executes(debug0 -> setThunder((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "duration") * 20)))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setClear(CommandSourceStack debug0, int debug1) {
/* 49 */     debug0.getLevel().setWeatherParameters(debug1, 0, false, false);
/* 50 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.weather.set.clear"), true);
/* 51 */     return debug1;
/*    */   }
/*    */   
/*    */   private static int setRain(CommandSourceStack debug0, int debug1) {
/* 55 */     debug0.getLevel().setWeatherParameters(0, debug1, true, false);
/* 56 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.weather.set.rain"), true);
/* 57 */     return debug1;
/*    */   }
/*    */   
/*    */   private static int setThunder(CommandSourceStack debug0, int debug1) {
/* 61 */     debug0.getLevel().setWeatherParameters(0, debug1, true, true);
/* 62 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.weather.set.thunder"), true);
/* 63 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\WeatherCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */