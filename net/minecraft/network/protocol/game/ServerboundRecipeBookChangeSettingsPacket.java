/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundRecipeBookChangeSettingsPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private RecipeBookType bookType;
/*    */   private boolean isOpen;
/*    */   private boolean isFiltering;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.bookType = (RecipeBookType)debug1.readEnum(RecipeBookType.class);
/* 26 */     this.isOpen = debug1.readBoolean();
/* 27 */     this.isFiltering = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeEnum((Enum)this.bookType);
/* 33 */     debug1.writeBoolean(this.isOpen);
/* 34 */     debug1.writeBoolean(this.isFiltering);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 39 */     debug1.handleRecipeBookChangeSettingsPacket(this);
/*    */   }
/*    */   
/*    */   public RecipeBookType getBookType() {
/* 43 */     return this.bookType;
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 47 */     return this.isOpen;
/*    */   }
/*    */   
/*    */   public boolean isFiltering() {
/* 51 */     return this.isFiltering;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundRecipeBookChangeSettingsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */