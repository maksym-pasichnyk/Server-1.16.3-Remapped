/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundEditBookPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private ItemStack book;
/*    */   private boolean signing;
/*    */   private InteractionHand hand;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.book = debug1.readItem();
/* 27 */     this.signing = debug1.readBoolean();
/* 28 */     this.hand = (InteractionHand)debug1.readEnum(InteractionHand.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeItem(this.book);
/* 34 */     debug1.writeBoolean(this.signing);
/* 35 */     debug1.writeEnum((Enum)this.hand);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 40 */     debug1.handleEditBook(this);
/*    */   }
/*    */   
/*    */   public ItemStack getBook() {
/* 44 */     return this.book;
/*    */   }
/*    */   
/*    */   public boolean isSigning() {
/* 48 */     return this.signing;
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 52 */     return this.hand;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundEditBookPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */