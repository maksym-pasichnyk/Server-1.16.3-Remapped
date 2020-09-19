/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.util.HttpUtil;
/*    */ 
/*    */ public class PublishCommand {
/* 18 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.publish.failed")); static {
/* 19 */     ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.publish.alreadyPublished", new Object[] { debug0 }));
/*    */   } private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 22 */     debug0.register(
/* 23 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("publish")
/* 24 */         .requires(debug0 -> debug0.hasPermission(4)))
/* 25 */         .executes(debug0 -> publish((CommandSourceStack)debug0.getSource(), HttpUtil.getAvailablePort())))
/* 26 */         .then(
/* 27 */           Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(0, 65535))
/* 28 */           .executes(debug0 -> publish((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "port")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int publish(CommandSourceStack debug0, int debug1) throws CommandSyntaxException {
/* 34 */     if (debug0.getServer().isPublished()) {
/* 35 */       throw ERROR_ALREADY_PUBLISHED.create(Integer.valueOf(debug0.getServer().getPort()));
/*    */     }
/* 37 */     if (!debug0.getServer().publishServer(debug0.getServer().getDefaultGameType(), false, debug1)) {
/* 38 */       throw ERROR_FAILED.create();
/*    */     }
/* 40 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.publish.success", new Object[] { Integer.valueOf(debug1) }), true);
/* 41 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\PublishCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */