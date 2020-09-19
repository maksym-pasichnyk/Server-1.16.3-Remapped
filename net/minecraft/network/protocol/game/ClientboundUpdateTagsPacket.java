/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.tags.TagContainer;
/*    */ 
/*    */ public class ClientboundUpdateTagsPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private TagContainer tags;
/*    */   
/*    */   public ClientboundUpdateTagsPacket() {}
/*    */   
/*    */   public ClientboundUpdateTagsPacket(TagContainer debug1) {
/* 16 */     this.tags = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 21 */     this.tags = TagContainer.deserializeFromNetwork(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.tags.serializeToNetwork(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 31 */     debug1.handleUpdateTags(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateTagsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */