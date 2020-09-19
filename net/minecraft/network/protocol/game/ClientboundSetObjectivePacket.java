/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundSetObjectivePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private String objectiveName;
/*    */   private Component displayName;
/*    */   private ObjectiveCriteria.RenderType renderType;
/*    */   private int method;
/*    */   
/*    */   public ClientboundSetObjectivePacket() {}
/*    */   
/*    */   public ClientboundSetObjectivePacket(Objective debug1, int debug2) {
/* 25 */     this.objectiveName = debug1.getName();
/* 26 */     this.displayName = debug1.getDisplayName();
/* 27 */     this.renderType = debug1.getRenderType();
/* 28 */     this.method = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 33 */     this.objectiveName = debug1.readUtf(16);
/* 34 */     this.method = debug1.readByte();
/*    */     
/* 36 */     if (this.method == 0 || this.method == 2) {
/* 37 */       this.displayName = debug1.readComponent();
/* 38 */       this.renderType = (ObjectiveCriteria.RenderType)debug1.readEnum(ObjectiveCriteria.RenderType.class);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 44 */     debug1.writeUtf(this.objectiveName);
/* 45 */     debug1.writeByte(this.method);
/*    */     
/* 47 */     if (this.method == 0 || this.method == 2) {
/* 48 */       debug1.writeComponent(this.displayName);
/* 49 */       debug1.writeEnum((Enum)this.renderType);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 55 */     debug1.handleAddObjective(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetObjectivePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */