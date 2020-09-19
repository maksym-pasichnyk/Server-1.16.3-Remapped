/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ 
/*    */ public class ClientboundMerchantOffersPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private MerchantOffers offers;
/*    */   private int villagerLevel;
/*    */   private int villagerXp;
/*    */   private boolean showProgress;
/*    */   private boolean canRestock;
/*    */   
/*    */   public ClientboundMerchantOffersPacket() {}
/*    */   
/*    */   public ClientboundMerchantOffersPacket(int debug1, MerchantOffers debug2, int debug3, int debug4, boolean debug5, boolean debug6) {
/* 22 */     this.containerId = debug1;
/* 23 */     this.offers = debug2;
/* 24 */     this.villagerLevel = debug3;
/* 25 */     this.villagerXp = debug4;
/* 26 */     this.showProgress = debug5;
/* 27 */     this.canRestock = debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 32 */     this.containerId = debug1.readVarInt();
/* 33 */     this.offers = MerchantOffers.createFromStream(debug1);
/* 34 */     this.villagerLevel = debug1.readVarInt();
/* 35 */     this.villagerXp = debug1.readVarInt();
/* 36 */     this.showProgress = debug1.readBoolean();
/* 37 */     this.canRestock = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 42 */     debug1.writeVarInt(this.containerId);
/* 43 */     this.offers.writeToStream(debug1);
/* 44 */     debug1.writeVarInt(this.villagerLevel);
/* 45 */     debug1.writeVarInt(this.villagerXp);
/* 46 */     debug1.writeBoolean(this.showProgress);
/* 47 */     debug1.writeBoolean(this.canRestock);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 52 */     debug1.handleMerchantOffers(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMerchantOffersPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */