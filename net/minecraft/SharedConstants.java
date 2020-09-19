/*     */ package net.minecraft;
/*     */ 
/*     */ import com.mojang.bridge.game.GameVersion;
/*     */ import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import java.time.Duration;
/*     */ import net.minecraft.commands.BrigadierExceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SharedConstants
/*     */ {
/*  85 */   public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final long MAXIMUM_TICK_TIME_NANOS = Duration.ofMillis(300L).toNanos();
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean CHECK_DATA_FIXER_SCHEMA = true;
/*     */ 
/*     */   
/*     */   public static boolean IS_RUNNING_IN_IDE;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAllowedChatCharacter(char debug0) {
/* 102 */     return (debug0 != '§' && debug0 >= ' ' && debug0 != '');
/*     */   }
/*     */   
/* 105 */   public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', Character.MIN_VALUE, '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
/*     */ 
/*     */ 
/*     */   
/*     */   private static GameVersion CURRENT_VERSION;
/*     */ 
/*     */ 
/*     */   
/*     */   public static String filterText(String debug0) {
/* 114 */     StringBuilder debug1 = new StringBuilder();
/*     */     
/* 116 */     for (char debug5 : debug0.toCharArray()) {
/* 117 */       if (isAllowedChatCharacter(debug5)) {
/* 118 */         debug1.append(debug5);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return debug1.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GameVersion getCurrentVersion() {
/* 136 */     if (CURRENT_VERSION == null) {
/* 137 */       CURRENT_VERSION = DetectedVersion.tryDetectVersion();
/*     */     }
/* 139 */     return CURRENT_VERSION;
/*     */   }
/*     */   
/*     */   static {
/* 143 */     ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
/* 144 */     CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES = false;
/* 145 */     CommandSyntaxException.BUILT_IN_EXCEPTIONS = (BuiltInExceptionProvider)new BrigadierExceptions();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\SharedConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */