/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import net.minecraft.commands.CommandFunction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ import net.minecraft.tags.Tag;
/*    */ 
/*    */ public class FunctionTagCallback implements TimerCallback<MinecraftServer> {
/*    */   private final ResourceLocation tagId;
/*    */   
/*    */   public FunctionTagCallback(ResourceLocation debug1) {
/* 14 */     this.tagId = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(MinecraftServer debug1, TimerQueue<MinecraftServer> debug2, long debug3) {
/* 19 */     ServerFunctionManager debug5 = debug1.getFunctions();
/* 20 */     Tag<CommandFunction> debug6 = debug5.getTag(this.tagId);
/* 21 */     for (CommandFunction debug8 : debug6.getValues())
/* 22 */       debug5.execute(debug8, debug5.getGameLoopSender()); 
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends TimerCallback.Serializer<MinecraftServer, FunctionTagCallback> {
/*    */     public Serializer() {
/* 28 */       super(new ResourceLocation("function_tag"), FunctionTagCallback.class);
/*    */     }
/*    */ 
/*    */     
/*    */     public void serialize(CompoundTag debug1, FunctionTagCallback debug2) {
/* 33 */       debug1.putString("Name", debug2.tagId.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     public FunctionTagCallback deserialize(CompoundTag debug1) {
/* 38 */       ResourceLocation debug2 = new ResourceLocation(debug1.getString("Name"));
/* 39 */       return new FunctionTagCallback(debug2);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\timers\FunctionTagCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */