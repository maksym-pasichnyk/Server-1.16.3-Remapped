/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ 
/*    */ public class ClientboundSetDisplayObjectivePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int slot;
/*    */   private String objectiveName;
/*    */   
/*    */   public ClientboundSetDisplayObjectivePacket() {}
/*    */   
/*    */   public ClientboundSetDisplayObjectivePacket(int debug1, @Nullable Objective debug2) {
/* 19 */     this.slot = debug1;
/*    */     
/* 21 */     if (debug2 == null) {
/* 22 */       this.objectiveName = "";
/*    */     } else {
/* 24 */       this.objectiveName = debug2.getName();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 30 */     this.slot = debug1.readByte();
/* 31 */     this.objectiveName = debug1.readUtf(16);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeByte(this.slot);
/* 37 */     debug1.writeUtf(this.objectiveName);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 42 */     debug1.handleSetDisplayObjective(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetDisplayObjectivePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */