/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class DifficultyCommand {
/*    */   static {
/* 16 */     ERROR_ALREADY_DIFFICULT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.difficulty.failure", new Object[] { debug0 }));
/*    */   } private static final DynamicCommandExceptionType ERROR_ALREADY_DIFFICULT;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 19 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = Commands.literal("difficulty");
/*    */     
/* 21 */     for (Difficulty debug5 : Difficulty.values()) {
/* 22 */       debug1.then(Commands.literal(debug5.getKey()).executes(debug1 -> setDifficulty((CommandSourceStack)debug1.getSource(), debug0)));
/*    */     }
/*    */     
/* 25 */     debug0.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)debug1
/*    */         
/* 27 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 28 */         .executes(debug0 -> {
/*    */             Difficulty debug1 = ((CommandSourceStack)debug0.getSource()).getLevel().getDifficulty();
/*    */             ((CommandSourceStack)debug0.getSource()).sendSuccess((Component)new TranslatableComponent("commands.difficulty.query", new Object[] { debug1.getDisplayName() }), false);
/*    */             return debug1.getId();
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   public static int setDifficulty(CommandSourceStack debug0, Difficulty debug1) throws CommandSyntaxException {
/* 37 */     MinecraftServer debug2 = debug0.getServer();
/* 38 */     if (debug2.getWorldData().getDifficulty() == debug1) {
/* 39 */       throw ERROR_ALREADY_DIFFICULT.create(debug1.getKey());
/*    */     }
/*    */     
/* 42 */     debug2.setDifficulty(debug1, true);
/* 43 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.difficulty.success", new Object[] { debug1.getDisplayName() }), true);
/*    */     
/* 45 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\DifficultyCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */