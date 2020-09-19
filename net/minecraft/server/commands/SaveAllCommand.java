/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class SaveAllCommand {
/* 14 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.save.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 17 */     debug0.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-all")
/* 19 */         .requires(debug0 -> debug0.hasPermission(4)))
/* 20 */         .executes(debug0 -> saveAll((CommandSourceStack)debug0.getSource(), false)))
/* 21 */         .then(
/* 22 */           Commands.literal("flush")
/* 23 */           .executes(debug0 -> saveAll((CommandSourceStack)debug0.getSource(), true))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int saveAll(CommandSourceStack debug0, boolean debug1) throws CommandSyntaxException {
/* 29 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.save.saving"), false);
/*    */     
/* 31 */     MinecraftServer debug2 = debug0.getServer();
/* 32 */     debug2.getPlayerList().saveAll();
/*    */     
/* 34 */     boolean debug3 = debug2.saveAllChunks(true, debug1, true);
/*    */     
/* 36 */     if (!debug3) {
/* 37 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 40 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.save.success"), true);
/*    */     
/* 42 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SaveAllCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */