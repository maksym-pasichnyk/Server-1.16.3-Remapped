/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.stats.RecipeBookSettings;
/*    */ 
/*    */ public class ClientboundRecipePacket implements Packet<ClientGamePacketListener> {
/*    */   private State state;
/*    */   private List<ResourceLocation> recipes;
/*    */   private List<ResourceLocation> toHighlight;
/*    */   private RecipeBookSettings bookSettings;
/*    */   
/*    */   public enum State {
/* 16 */     INIT, ADD, REMOVE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundRecipePacket() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundRecipePacket(State debug1, Collection<ResourceLocation> debug2, Collection<ResourceLocation> debug3, RecipeBookSettings debug4) {
/* 29 */     this.state = debug1;
/* 30 */     this.recipes = (List<ResourceLocation>)ImmutableList.copyOf(debug2);
/* 31 */     this.toHighlight = (List<ResourceLocation>)ImmutableList.copyOf(debug3);
/* 32 */     this.bookSettings = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 37 */     debug1.handleAddOrRemoveRecipes(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 42 */     this.state = (State)debug1.readEnum(State.class);
/*    */     
/* 44 */     this.bookSettings = RecipeBookSettings.read(debug1);
/*    */     
/* 46 */     int debug2 = debug1.readVarInt();
/* 47 */     this.recipes = Lists.newArrayList(); int debug3;
/* 48 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 49 */       this.recipes.add(debug1.readResourceLocation());
/*    */     }
/*    */     
/* 52 */     if (this.state == State.INIT) {
/* 53 */       debug2 = debug1.readVarInt();
/* 54 */       this.toHighlight = Lists.newArrayList();
/* 55 */       for (debug3 = 0; debug3 < debug2; debug3++) {
/* 56 */         this.toHighlight.add(debug1.readResourceLocation());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 63 */     debug1.writeEnum(this.state);
/*    */     
/* 65 */     this.bookSettings.write(debug1);
/*    */     
/* 67 */     debug1.writeVarInt(this.recipes.size());
/* 68 */     for (ResourceLocation debug3 : this.recipes) {
/* 69 */       debug1.writeResourceLocation(debug3);
/*    */     }
/*    */     
/* 72 */     if (this.state == State.INIT) {
/* 73 */       debug1.writeVarInt(this.toHighlight.size());
/* 74 */       for (ResourceLocation debug3 : this.toHighlight)
/* 75 */         debug1.writeResourceLocation(debug3); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRecipePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */