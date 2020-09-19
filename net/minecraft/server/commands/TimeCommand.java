/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.TimeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public class TimeCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 17 */     debug0.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time")
/* 19 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 20 */         .then((
/* 21 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set")
/* 22 */           .then(
/* 23 */             Commands.literal("day")
/* 24 */             .executes(debug0 -> setTime((CommandSourceStack)debug0.getSource(), 1000))))
/* 25 */           .then(
/* 26 */             Commands.literal("noon")
/* 27 */             .executes(debug0 -> setTime((CommandSourceStack)debug0.getSource(), 6000))))
/* 28 */           .then(
/* 29 */             Commands.literal("night")
/* 30 */             .executes(debug0 -> setTime((CommandSourceStack)debug0.getSource(), 13000))))
/* 31 */           .then(
/* 32 */             Commands.literal("midnight")
/* 33 */             .executes(debug0 -> setTime((CommandSourceStack)debug0.getSource(), 18000))))
/* 34 */           .then(
/* 35 */             Commands.argument("time", (ArgumentType)TimeArgument.time())
/* 36 */             .executes(debug0 -> setTime((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "time"))))))
/*    */ 
/*    */         
/* 39 */         .then(
/* 40 */           Commands.literal("add")
/* 41 */           .then(
/* 42 */             Commands.argument("time", (ArgumentType)TimeArgument.time())
/* 43 */             .executes(debug0 -> addTime((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "time"))))))
/*    */ 
/*    */         
/* 46 */         .then((
/* 47 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("query")
/* 48 */           .then(
/* 49 */             Commands.literal("daytime")
/* 50 */             .executes(debug0 -> queryTime((CommandSourceStack)debug0.getSource(), getDayTime(((CommandSourceStack)debug0.getSource()).getLevel())))))
/*    */           
/* 52 */           .then(
/* 53 */             Commands.literal("gametime")
/* 54 */             .executes(debug0 -> queryTime((CommandSourceStack)debug0.getSource(), (int)(((CommandSourceStack)debug0.getSource()).getLevel().getGameTime() % 2147483647L)))))
/*    */           
/* 56 */           .then(
/* 57 */             Commands.literal("day")
/* 58 */             .executes(debug0 -> queryTime((CommandSourceStack)debug0.getSource(), (int)(((CommandSourceStack)debug0.getSource()).getLevel().getDayTime() / 24000L % 2147483647L))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int getDayTime(ServerLevel debug0) {
/* 65 */     return (int)(debug0.getDayTime() % 24000L);
/*    */   }
/*    */   
/*    */   private static int queryTime(CommandSourceStack debug0, int debug1) {
/* 69 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.time.query", new Object[] { Integer.valueOf(debug1) }), false);
/* 70 */     return debug1;
/*    */   }
/*    */   
/*    */   public static int setTime(CommandSourceStack debug0, int debug1) {
/* 74 */     for (ServerLevel debug3 : debug0.getServer().getAllLevels()) {
/* 75 */       debug3.setDayTime(debug1);
/*    */     }
/* 77 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.time.set", new Object[] { Integer.valueOf(debug1) }), true);
/* 78 */     return getDayTime(debug0.getLevel());
/*    */   }
/*    */   
/*    */   public static int addTime(CommandSourceStack debug0, int debug1) {
/* 82 */     for (ServerLevel debug3 : debug0.getServer().getAllLevels()) {
/* 83 */       debug3.setDayTime(debug3.getDayTime() + debug1);
/*    */     }
/* 85 */     int debug2 = getDayTime(debug0.getLevel());
/* 86 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.time.set", new Object[] { Integer.valueOf(debug2) }), true);
/* 87 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TimeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */