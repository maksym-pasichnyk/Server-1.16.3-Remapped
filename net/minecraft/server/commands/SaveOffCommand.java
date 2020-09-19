/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public class SaveOffCommand {
/* 13 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OFF = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.save.alreadyOff"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 16 */     debug0.register(
/* 17 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-off")
/* 18 */         .requires(debug0 -> debug0.hasPermission(4)))
/* 19 */         .executes(debug0 -> {
/*    */             CommandSourceStack debug1 = (CommandSourceStack)debug0.getSource();
/*    */             boolean debug2 = false;
/*    */             for (ServerLevel debug4 : debug1.getServer().getAllLevels()) {
/*    */               if (debug4 != null && !debug4.noSave) {
/*    */                 debug4.noSave = true;
/*    */                 debug2 = true;
/*    */               } 
/*    */             } 
/*    */             if (!debug2)
/*    */               throw ERROR_ALREADY_OFF.create(); 
/*    */             debug1.sendSuccess((Component)new TranslatableComponent("commands.save.disabled"), true);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SaveOffCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */