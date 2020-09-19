/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*    */ 
/*    */ public class ClientboundUpdateRecipesPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private List<Recipe<?>> recipes;
/*    */   
/*    */   public ClientboundUpdateRecipesPacket() {}
/*    */   
/*    */   public ClientboundUpdateRecipesPacket(Collection<Recipe<?>> debug1) {
/* 22 */     this.recipes = Lists.newArrayList(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 27 */     debug1.handleUpdateRecipes(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 32 */     this.recipes = Lists.newArrayList();
/* 33 */     int debug2 = debug1.readVarInt();
/* 34 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 35 */       this.recipes.add(fromNetwork(debug1));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 41 */     debug1.writeVarInt(this.recipes.size());
/* 42 */     for (Recipe<?> debug3 : this.recipes) {
/* 43 */       toNetwork(debug3, debug1);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Recipe<?> fromNetwork(FriendlyByteBuf debug0) {
/* 52 */     ResourceLocation debug1 = debug0.readResourceLocation();
/* 53 */     ResourceLocation debug2 = debug0.readResourceLocation();
/*    */     
/* 55 */     return ((RecipeSerializer)Registry.RECIPE_SERIALIZER.getOptional(debug1)
/* 56 */       .orElseThrow(() -> new IllegalArgumentException("Unknown recipe serializer " + debug0)))
/* 57 */       .fromNetwork(debug2, debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T extends Recipe<?>> void toNetwork(T debug0, FriendlyByteBuf debug1) {
/* 62 */     debug1.writeResourceLocation(Registry.RECIPE_SERIALIZER.getKey(debug0.getSerializer()));
/* 63 */     debug1.writeResourceLocation(debug0.getId());
/* 64 */     debug0.getSerializer().toNetwork(debug1, (Recipe)debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateRecipesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */