/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class ServerboundRecipeBookSeenRecipePacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private ResourceLocation recipe;
/*    */   
/*    */   public ServerboundRecipeBookSeenRecipePacket() {}
/*    */   
/*    */   public ServerboundRecipeBookSeenRecipePacket(Recipe<?> debug1) {
/* 17 */     this.recipe = debug1.getId();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.recipe = debug1.readResourceLocation();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeResourceLocation(this.recipe);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 33 */     debug1.handleRecipeBookSeenRecipePacket(this);
/*    */   }
/*    */   
/*    */   public ResourceLocation getRecipe() {
/* 37 */     return this.recipe;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundRecipeBookSeenRecipePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */