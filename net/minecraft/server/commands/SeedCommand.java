/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class SeedCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0, boolean debug1) {
/* 18 */     debug0.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed")
/* 20 */         .requires(debug1 -> (!debug0 || debug1.hasPermission(2))))
/* 21 */         .executes(debug0 -> {
/*    */             long debug1 = ((CommandSourceStack)debug0.getSource()).getLevel().getSeed();
/*    */             MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)(new TextComponent(String.valueOf(debug1))).withStyle(()));
/*    */             ((CommandSourceStack)debug0.getSource()).sendSuccess((Component)new TranslatableComponent("commands.seed.success", new Object[] { mutableComponent }), false);
/*    */             return (int)debug1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SeedCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */