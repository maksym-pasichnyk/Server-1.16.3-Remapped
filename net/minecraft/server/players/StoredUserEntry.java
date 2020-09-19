/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public abstract class StoredUserEntry<T>
/*    */ {
/*    */   @Nullable
/*    */   private final T user;
/*    */   
/*    */   public StoredUserEntry(@Nullable T debug1) {
/* 12 */     this.user = debug1;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   T getUser() {
/* 17 */     return this.user;
/*    */   }
/*    */   
/*    */   boolean hasExpired() {
/* 21 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract void serialize(JsonObject paramJsonObject);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\StoredUserEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */