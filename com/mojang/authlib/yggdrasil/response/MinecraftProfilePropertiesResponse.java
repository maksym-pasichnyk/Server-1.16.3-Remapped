/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class MinecraftProfilePropertiesResponse
/*    */   extends Response {
/*    */   private UUID id;
/*    */   private String name;
/*    */   private PropertyMap properties;
/*    */   
/*    */   public UUID getId() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 17 */     return this.name;
/*    */   }
/*    */   
/*    */   public PropertyMap getProperties() {
/* 21 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\response\MinecraftProfilePropertiesResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */