/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class EnvironmentParser
/*    */ {
/*    */   private static final String PROP_PREFIX = "minecraft.api.";
/* 12 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public static final String PROP_ENV = "minecraft.api.env";
/*    */   public static final String PROP_AUTH_HOST = "minecraft.api.auth.host";
/*    */   public static final String PROP_ACCOUNT_HOST = "minecraft.api.account.host";
/*    */   public static final String PROP_SESSION_HOST = "minecraft.api.session.host";
/*    */   
/*    */   public static Optional<Environment> getEnvironmentFromProperties() {
/* 20 */     String envName = System.getProperty("minecraft.api.env");
/*    */ 
/*    */     
/* 23 */     Optional<Environment> env = YggdrasilEnvironment.fromString(envName).map(Environment.class::cast);
/*    */     
/* 25 */     return env.isPresent() ? env : fromHostNames();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Optional<Environment> fromHostNames() {
/* 31 */     String auth = System.getProperty("minecraft.api.auth.host");
/* 32 */     String account = System.getProperty("minecraft.api.account.host");
/* 33 */     String session = System.getProperty("minecraft.api.session.host");
/*    */     
/* 35 */     if (auth != null && account != null && session != null)
/* 36 */       return Optional.of(Environment.create(auth, account, session, "properties")); 
/* 37 */     if (auth != null || account != null || session != null) {
/* 38 */       LOGGER.info("Ignoring hosts properties. All need to be set: " + 
/* 39 */           Arrays.<String>asList(new String[] { "minecraft.api.auth.host", "minecraft.api.account.host", "minecraft.api.session.host" }));
/*    */     }
/* 41 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\EnvironmentParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */