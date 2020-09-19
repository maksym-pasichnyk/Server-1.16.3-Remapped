/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class ClientboundPlaceGhostRecipePacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int containerId;
/*    */   private ResourceLocation recipe;
/*    */   
/*    */   public ClientboundPlaceGhostRecipePacket() {}
/*    */   
/*    */   public ClientboundPlaceGhostRecipePacket(int debug1, Recipe<?> debug2) {
/* 18 */     this.containerId = debug1;
/* 19 */     this.recipe = debug2.getId();
/*    */   }
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
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 32 */     this.containerId = debug1.readByte();
/* 33 */     this.recipe = debug1.readResourceLocation();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeByte(this.containerId);
/* 39 */     debug1.writeResourceLocation(this.recipe);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 44 */     debug1.handlePlaceRecipe(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlaceGhostRecipePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */