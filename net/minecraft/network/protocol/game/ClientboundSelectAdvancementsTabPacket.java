/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class ClientboundSelectAdvancementsTabPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   @Nullable
/*    */   private ResourceLocation tab;
/*    */   
/*    */   public ClientboundSelectAdvancementsTabPacket() {}
/*    */   
/*    */   public ClientboundSelectAdvancementsTabPacket(@Nullable ResourceLocation debug1) {
/* 18 */     this.tab = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 23 */     debug1.handleSelectAdvancementsTab(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     if (debug1.readBoolean()) {
/* 29 */       this.tab = debug1.readResourceLocation();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     debug1.writeBoolean((this.tab != null));
/* 36 */     if (this.tab != null)
/* 37 */       debug1.writeResourceLocation(this.tab); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSelectAdvancementsTabPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */