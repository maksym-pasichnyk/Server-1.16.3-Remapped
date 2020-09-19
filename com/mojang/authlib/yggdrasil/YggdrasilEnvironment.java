/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.mojang.authlib.Environment;
/*    */ import java.util.Optional;
/*    */ import java.util.StringJoiner;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public enum YggdrasilEnvironment
/*    */   implements Environment {
/* 11 */   PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   STAGING("https://yggdrasil-auth-staging.mojang.com", "https://api-staging.mojang.com", "https://yggdrasil-auth-session-staging.mojang.zone");
/*    */ 
/*    */   
/*    */   private final String authHost;
/*    */   
/*    */   private final String accountsHost;
/*    */   
/*    */   private final String sessionHost;
/*    */ 
/*    */   
/*    */   YggdrasilEnvironment(String authHost, String accountsHost, String sessionHost) {
/* 27 */     this.authHost = authHost;
/* 28 */     this.accountsHost = accountsHost;
/* 29 */     this.sessionHost = sessionHost;
/*    */   }
/*    */   
/*    */   public String getAuthHost() {
/* 33 */     return this.authHost;
/*    */   }
/*    */   
/*    */   public String getAccountsHost() {
/* 37 */     return this.accountsHost;
/*    */   }
/*    */   
/*    */   public String getSessionHost() {
/* 41 */     return this.sessionHost;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 45 */     return name();
/*    */   }
/*    */ 
/*    */   
/*    */   public String asString() {
/* 50 */     return (new StringJoiner(", ", "", ""))
/* 51 */       .add("authHost='" + this.authHost + "'")
/* 52 */       .add("accountsHost='" + this.accountsHost + "'")
/* 53 */       .add("sessionHost='" + this.sessionHost + "'")
/* 54 */       .add("name='" + getName() + "'")
/* 55 */       .toString();
/*    */   }
/*    */   
/*    */   public static Optional<YggdrasilEnvironment> fromString(@Nullable String value) {
/* 59 */     return 
/* 60 */       Stream.<YggdrasilEnvironment>of(values())
/* 61 */       .filter(env -> (value != null && value.equalsIgnoreCase(env.name())))
/* 62 */       .findFirst();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\YggdrasilEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */