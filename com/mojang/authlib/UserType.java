/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public enum UserType {
/*  7 */   LEGACY("legacy"),
/*  8 */   MOJANG("mojang");
/*    */   static {
/* 10 */     BY_NAME = new HashMap<>();
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
/* 26 */     for (UserType type : values())
/* 27 */       BY_NAME.put(type.name, type); 
/*    */   }
/*    */   
/*    */   private static final Map<String, UserType> BY_NAME;
/*    */   private final String name;
/*    */   
/*    */   UserType(String name) {
/*    */     this.name = name;
/*    */   }
/*    */   
/*    */   public static UserType byName(String name) {
/*    */     return BY_NAME.get(name.toLowerCase());
/*    */   }
/*    */   
/*    */   public String getName() {
/*    */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\UserType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */