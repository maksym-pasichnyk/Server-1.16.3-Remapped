/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.world.level.storage.WorldData;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ReloadCommand {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public static void reloadPacks(Collection<String> debug0, CommandSourceStack debug1) {
/* 22 */     debug1.getServer().reloadResources(debug0).exceptionally(debug1 -> {
/*    */           LOGGER.warn("Failed to execute reload", debug1);
/*    */           debug0.sendFailure((Component)new TranslatableComponent("commands.reload.failure"));
/*    */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   private static Collection<String> discoverNewPacks(PackRepository debug0, WorldData debug1, Collection<String> debug2) {
/* 30 */     debug0.reload();
/* 31 */     Collection<String> debug3 = Lists.newArrayList(debug2);
/* 32 */     Collection<String> debug4 = debug1.getDataPackConfig().getDisabled();
/*    */     
/* 34 */     for (String debug6 : debug0.getAvailableIds()) {
/* 35 */       if (!debug4.contains(debug6) && !debug3.contains(debug6)) {
/* 36 */         debug3.add(debug6);
/*    */       }
/*    */     } 
/* 39 */     return debug3;
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 43 */     debug0.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload")
/* 44 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 45 */         .executes(debug0 -> {
/*    */             CommandSourceStack debug1 = (CommandSourceStack)debug0.getSource();
/*    */             MinecraftServer debug2 = debug1.getServer();
/*    */             PackRepository debug3 = debug2.getPackRepository();
/*    */             WorldData debug4 = debug2.getWorldData();
/*    */             Collection<String> debug5 = debug3.getSelectedIds();
/*    */             Collection<String> debug6 = discoverNewPacks(debug3, debug4, debug5);
/*    */             debug1.sendSuccess((Component)new TranslatableComponent("commands.reload.success"), true);
/*    */             reloadPacks(debug6, debug1);
/*    */             return 0;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ReloadCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */