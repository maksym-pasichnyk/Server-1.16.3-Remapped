/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import java.util.StringJoiner;
/*    */ 
/*    */ 
/*    */ public interface Environment
/*    */ {
/*    */   String getAuthHost();
/*    */   
/*    */   String getAccountsHost();
/*    */   
/*    */   String getSessionHost();
/*    */   
/*    */   String getName();
/*    */   
/*    */   String asString();
/*    */   
/*    */   static Environment create(final String auth, final String account, final String session, final String name) {
/* 19 */     return new Environment()
/*    */       {
/*    */         public String getAuthHost() {
/* 22 */           return auth;
/*    */         }
/*    */ 
/*    */         
/*    */         public String getAccountsHost() {
/* 27 */           return account;
/*    */         }
/*    */ 
/*    */         
/*    */         public String getSessionHost() {
/* 32 */           return session;
/*    */         }
/*    */ 
/*    */         
/*    */         public String getName() {
/* 37 */           return name;
/*    */         }
/*    */ 
/*    */         
/*    */         public String asString() {
/* 42 */           return (new StringJoiner(", ", "", ""))
/* 43 */             .add("authHost='" + getAuthHost() + "'")
/* 44 */             .add("accountsHost='" + getAccountsHost() + "'")
/* 45 */             .add("sessionHost='" + getSessionHost() + "'")
/* 46 */             .add("name='" + getName() + "'")
/* 47 */             .toString();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\Environment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */