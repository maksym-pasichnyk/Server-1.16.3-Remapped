/*    */ package com.mojang.bridge;
/*    */ 
/*    */ import com.mojang.bridge.launcher.Launcher;
/*    */ import com.mojang.bridge.launcher.LauncherProvider;
/*    */ import java.util.ServiceLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bridge
/*    */ {
/*    */   private static boolean INITIALIZED;
/*    */   private static Launcher LAUNCHER;
/*    */   
/*    */   public static Launcher getLauncher() {
/* 28 */     if (!INITIALIZED) {
/* 29 */       synchronized (Bridge.class) {
/* 30 */         if (!INITIALIZED) {
/* 31 */           LAUNCHER = createLauncher();
/* 32 */           INITIALIZED = true;
/*    */         } 
/*    */       } 
/*    */     }
/* 36 */     return LAUNCHER;
/*    */   }
/*    */   
/*    */   private static Launcher createLauncher() {
/* 40 */     for (LauncherProvider provider : ServiceLoader.<LauncherProvider>load(LauncherProvider.class)) {
/* 41 */       Launcher launcher = provider.createLauncher();
/* 42 */       if (launcher != null) {
/* 43 */         return launcher;
/*    */       }
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\bridge\Bridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */