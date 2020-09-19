/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.swing.JList;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class PlayerListComponent extends JList<String> {
/*    */   private final MinecraftServer server;
/*    */   private int tickCount;
/*    */   
/*    */   public PlayerListComponent(MinecraftServer debug1) {
/* 13 */     this.server = debug1;
/* 14 */     debug1.addTickable(this::tick);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.tickCount++ % 20 == 0) {
/* 19 */       Vector<String> debug1 = new Vector<>();
/* 20 */       for (int debug2 = 0; debug2 < this.server.getPlayerList().getPlayers().size(); debug2++) {
/* 21 */         debug1.add(((ServerPlayer)this.server.getPlayerList().getPlayers().get(debug2)).getGameProfile().getName());
/*    */       }
/* 23 */       setListData(debug1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\gui\PlayerListComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */