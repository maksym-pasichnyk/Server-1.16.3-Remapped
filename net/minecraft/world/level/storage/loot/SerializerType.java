/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ public class SerializerType<T> {
/*    */   private final Serializer<? extends T> serializer;
/*    */   
/*    */   public SerializerType(Serializer<? extends T> debug1) {
/*  7 */     this.serializer = debug1;
/*    */   }
/*    */   
/*    */   public Serializer<? extends T> getSerializer() {
/* 11 */     return this.serializer;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\SerializerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */