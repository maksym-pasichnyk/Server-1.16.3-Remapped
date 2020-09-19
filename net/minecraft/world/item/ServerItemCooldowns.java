/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class ServerItemCooldowns
/*    */   extends ItemCooldowns {
/*    */   public ServerItemCooldowns(ServerPlayer debug1) {
/* 10 */     this.player = debug1;
/*    */   }
/*    */   private final ServerPlayer player;
/*    */   
/*    */   protected void onCooldownStarted(Item debug1, int debug2) {
/* 15 */     super.onCooldownStarted(debug1, debug2);
/* 16 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(debug1, debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCooldownEnded(Item debug1) {
/* 21 */     super.onCooldownEnded(debug1);
/* 22 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(debug1, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ServerItemCooldowns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */