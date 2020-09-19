/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundPlaceRecipePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private ResourceLocation recipe;
/*    */   private boolean shiftDown;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.containerId = debug1.readByte();
/* 27 */     this.recipe = debug1.readResourceLocation();
/* 28 */     this.shiftDown = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeByte(this.containerId);
/* 34 */     debug1.writeResourceLocation(this.recipe);
/* 35 */     debug1.writeBoolean(this.shiftDown);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 40 */     debug1.handlePlaceRecipe(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 44 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public ResourceLocation getRecipe() {
/* 48 */     return this.recipe;
/*    */   }
/*    */   
/*    */   public boolean isShiftDown() {
/* 52 */     return this.shiftDown;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlaceRecipePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */