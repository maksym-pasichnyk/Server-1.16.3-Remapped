/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class PlayerMap
/*    */ {
/* 10 */   private final Object2BooleanMap<ServerPlayer> players = (Object2BooleanMap<ServerPlayer>)new Object2BooleanOpenHashMap();
/*    */   
/*    */   public Stream<ServerPlayer> getPlayers(long debug1) {
/* 13 */     return this.players.keySet().stream();
/*    */   }
/*    */   
/*    */   public void addPlayer(long debug1, ServerPlayer debug3, boolean debug4) {
/* 17 */     this.players.put(debug3, debug4);
/*    */   }
/*    */   
/*    */   public void removePlayer(long debug1, ServerPlayer debug3) {
/* 21 */     this.players.removeBoolean(debug3);
/*    */   }
/*    */   
/*    */   public void ignorePlayer(ServerPlayer debug1) {
/* 25 */     this.players.replace(debug1, true);
/*    */   }
/*    */   
/*    */   public void unIgnorePlayer(ServerPlayer debug1) {
/* 29 */     this.players.replace(debug1, false);
/*    */   }
/*    */   
/*    */   public boolean ignoredOrUnknown(ServerPlayer debug1) {
/* 33 */     return this.players.getOrDefault(debug1, true);
/*    */   }
/*    */   
/*    */   public boolean ignored(ServerPlayer debug1) {
/* 37 */     return this.players.getBoolean(debug1);
/*    */   }
/*    */   
/*    */   public void updatePlayer(long debug1, long debug3, ServerPlayer debug5) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\PlayerMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */