/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.nio.file.spi.FileSystemProvider;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.profiling.ProfileResults;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DebugCommand {
/* 31 */   private static final Logger LOGGER = LogManager.getLogger();
/* 32 */   private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.debug.notRunning"));
/* 33 */   private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.debug.alreadyRunning")); @Nullable
/*    */   private static final FileSystemProvider ZIP_FS_PROVIDER;
/*    */   static {
/* 36 */     ZIP_FS_PROVIDER = FileSystemProvider.installedProviders().stream().filter(debug0 -> debug0.getScheme().equalsIgnoreCase("jar")).findFirst().orElse(null);
/*    */   }
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 39 */     debug0.register(
/* 40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debug")
/* 41 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 42 */         .then(Commands.literal("start").executes(debug0 -> start((CommandSourceStack)debug0.getSource()))))
/* 43 */         .then(Commands.literal("stop").executes(debug0 -> stop((CommandSourceStack)debug0.getSource()))))
/* 44 */         .then(Commands.literal("report").executes(debug0 -> report((CommandSourceStack)debug0.getSource()))));
/*    */   }
/*    */ 
/*    */   
/*    */   private static int start(CommandSourceStack debug0) throws CommandSyntaxException {
/* 49 */     MinecraftServer debug1 = debug0.getServer();
/* 50 */     if (debug1.isProfiling()) {
/* 51 */       throw ERROR_ALREADY_RUNNING.create();
/*    */     }
/* 53 */     debug1.startProfiling();
/* 54 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.debug.started", new Object[] { "Started the debug profiler. Type '/debug stop' to stop it." }), true);
/* 55 */     return 0;
/*    */   }
/*    */   
/*    */   private static int stop(CommandSourceStack debug0) throws CommandSyntaxException {
/* 59 */     MinecraftServer debug1 = debug0.getServer();
/* 60 */     if (!debug1.isProfiling()) {
/* 61 */       throw ERROR_NOT_RUNNING.create();
/*    */     }
/* 63 */     ProfileResults debug2 = debug1.finishProfiling();
/* 64 */     File debug3 = new File(debug1.getFile("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
/*    */     
/* 66 */     debug2.saveResults(debug3);
/*    */     
/* 68 */     float debug4 = (float)debug2.getNanoDuration() / 1.0E9F;
/* 69 */     float debug5 = debug2.getTickDuration() / debug4;
/* 70 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.debug.stopped", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(debug4) }), Integer.valueOf(debug2.getTickDuration()), String.format("%.2f", new Object[] { Float.valueOf(debug5) }) }), true);
/*    */     
/* 72 */     return Mth.floor(debug5);
/*    */   }
/*    */   
/*    */   private static int report(CommandSourceStack debug0) {
/* 76 */     MinecraftServer debug1 = debug0.getServer();
/* 77 */     String debug2 = "debug-report-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date());
/*    */ 
/*    */     
/*    */     try {
/* 81 */       Path debug4 = debug1.getFile("debug").toPath();
/* 82 */       Files.createDirectories(debug4, (FileAttribute<?>[])new FileAttribute[0]);
/* 83 */       if (SharedConstants.IS_RUNNING_IN_IDE || ZIP_FS_PROVIDER == null) {
/* 84 */         Path debug3 = debug4.resolve(debug2);
/* 85 */         debug1.saveDebugReport(debug3);
/*    */       } else {
/* 87 */         Path debug3 = debug4.resolve(debug2 + ".zip");
/* 88 */         try (FileSystem debug5 = ZIP_FS_PROVIDER.newFileSystem(debug3, (Map<String, ?>)ImmutableMap.of("create", "true"))) {
/* 89 */           debug1.saveDebugReport(debug5.getPath("/", new String[0]));
/*    */         } 
/*    */       } 
/*    */       
/* 93 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.debug.reportSaved", new Object[] { debug2 }), false);
/* 94 */       return 1;
/* 95 */     } catch (IOException debug3) {
/* 96 */       LOGGER.error("Failed to save debug dump", debug3);
/* 97 */       debug0.sendFailure((Component)new TranslatableComponent("commands.debug.reportFailed"));
/* 98 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\DebugCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */