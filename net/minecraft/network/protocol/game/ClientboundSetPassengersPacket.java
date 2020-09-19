/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSetPassengersPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int vehicle;
/*    */   private int[] passengers;
/*    */   
/*    */   public ClientboundSetPassengersPacket() {}
/*    */   
/*    */   public ClientboundSetPassengersPacket(Entity debug1) {
/* 18 */     this.vehicle = debug1.getId();
/* 19 */     List<Entity> debug2 = debug1.getPassengers();
/* 20 */     this.passengers = new int[debug2.size()];
/*    */     
/* 22 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 23 */       this.passengers[debug3] = ((Entity)debug2.get(debug3)).getId();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.vehicle = debug1.readVarInt();
/* 30 */     this.passengers = debug1.readVarIntArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     debug1.writeVarInt(this.vehicle);
/* 36 */     debug1.writeVarIntArray(this.passengers);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 41 */     debug1.handleSetEntityPassengersPacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetPassengersPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */