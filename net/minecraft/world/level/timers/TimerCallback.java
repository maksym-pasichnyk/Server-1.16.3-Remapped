/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TimerCallback<T> {
/*    */   void handle(T paramT, TimerQueue<T> paramTimerQueue, long paramLong);
/*    */   
/*    */   public static abstract class Serializer<T, C extends TimerCallback<T>> {
/*    */     private final ResourceLocation id;
/*    */     private final Class<?> cls;
/*    */     
/*    */     public Serializer(ResourceLocation debug1, Class<?> debug2) {
/* 15 */       this.id = debug1;
/* 16 */       this.cls = debug2;
/*    */     }
/*    */     
/*    */     public ResourceLocation getId() {
/* 20 */       return this.id;
/*    */     }
/*    */     
/*    */     public Class<?> getCls() {
/* 24 */       return this.cls;
/*    */     }
/*    */     
/*    */     public abstract void serialize(CompoundTag param1CompoundTag, C param1C);
/*    */     
/*    */     public abstract C deserialize(CompoundTag param1CompoundTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\timers\TimerCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */