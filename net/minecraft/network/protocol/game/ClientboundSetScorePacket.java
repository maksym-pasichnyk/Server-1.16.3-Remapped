/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ 
/*    */ public class ClientboundSetScorePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/* 14 */   private String owner = "";
/*    */   
/*    */   @Nullable
/*    */   private String objectiveName;
/*    */   
/*    */   private int score;
/*    */   
/*    */   private ServerScoreboard.Method method;
/*    */   
/*    */   public ClientboundSetScorePacket(ServerScoreboard.Method debug1, @Nullable String debug2, String debug3, int debug4) {
/* 24 */     if (debug1 != ServerScoreboard.Method.REMOVE && debug2 == null) {
/* 25 */       throw new IllegalArgumentException("Need an objective name");
/*    */     }
/* 27 */     this.owner = debug3;
/* 28 */     this.objectiveName = debug2;
/* 29 */     this.score = debug4;
/* 30 */     this.method = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 35 */     this.owner = debug1.readUtf(40);
/* 36 */     this.method = (ServerScoreboard.Method)debug1.readEnum(ServerScoreboard.Method.class);
/* 37 */     String debug2 = debug1.readUtf(16);
/* 38 */     this.objectiveName = Objects.equals(debug2, "") ? null : debug2;
/*    */     
/* 40 */     if (this.method != ServerScoreboard.Method.REMOVE) {
/* 41 */       this.score = debug1.readVarInt();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 47 */     debug1.writeUtf(this.owner);
/* 48 */     debug1.writeEnum((Enum)this.method);
/* 49 */     debug1.writeUtf((this.objectiveName == null) ? "" : this.objectiveName);
/*    */     
/* 51 */     if (this.method != ServerScoreboard.Method.REMOVE) {
/* 52 */       debug1.writeVarInt(this.score);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 58 */     debug1.handleSetScore(this);
/*    */   }
/*    */   
/*    */   public ClientboundSetScorePacket() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetScorePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */