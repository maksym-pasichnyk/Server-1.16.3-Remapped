/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import net.minecraft.commands.CommandFunction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public class FunctionCallback
/*    */   implements TimerCallback<MinecraftServer> {
/*    */   public FunctionCallback(ResourceLocation debug1) {
/* 12 */     this.functionId = debug1;
/*    */   }
/*    */   private final ResourceLocation functionId;
/*    */   
/*    */   public void handle(MinecraftServer debug1, TimerQueue<MinecraftServer> debug2, long debug3) {
/* 17 */     ServerFunctionManager debug5 = debug1.getFunctions();
/* 18 */     debug5.get(this.functionId).ifPresent(debug1 -> debug0.execute(debug1, debug0.getGameLoopSender()));
/*    */   }
/*    */   
/*    */   public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FunctionCallback> {
/*    */     public Serializer() {
/* 23 */       super(new ResourceLocation("function"), FunctionCallback.class);
/*    */     }
/*    */ 
/*    */     
/*    */     public void serialize(CompoundTag debug1, FunctionCallback debug2) {
/* 28 */       debug1.putString("Name", debug2.functionId.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     public FunctionCallback deserialize(CompoundTag debug1) {
/* 33 */       ResourceLocation debug2 = new ResourceLocation(debug1.getString("Name"));
/* 34 */       return new FunctionCallback(debug2);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\timers\FunctionCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */