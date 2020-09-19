/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public class ProfileSearchResultsResponse
/*    */   extends Response {
/*    */   private GameProfile[] profiles;
/*    */   
/*    */   public GameProfile[] getProfiles() {
/* 16 */     return this.profiles;
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<ProfileSearchResultsResponse> {
/*    */     public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 22 */       ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
/*    */       
/* 24 */       if (json instanceof JsonObject) {
/* 25 */         JsonObject object = (JsonObject)json;
/* 26 */         if (object.has("error")) {
/* 27 */           result.setError(object.getAsJsonPrimitive("error").getAsString());
/*    */         }
/* 29 */         if (object.has("errorMessage")) {
/* 30 */           result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
/*    */         }
/* 32 */         if (object.has("cause")) {
/* 33 */           result.setError(object.getAsJsonPrimitive("cause").getAsString());
/*    */         }
/*    */       } else {
/* 36 */         result.profiles = (GameProfile[])context.deserialize(json, GameProfile[].class);
/*    */       } 
/*    */       
/* 39 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\response\ProfileSearchResultsResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */