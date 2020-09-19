/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public interface EntityDataSerializer<T> {
/*    */   void write(FriendlyByteBuf paramFriendlyByteBuf, T paramT);
/*    */   
/*    */   T read(FriendlyByteBuf paramFriendlyByteBuf);
/*    */   
/*    */   default EntityDataAccessor<T> createAccessor(int debug1) {
/* 11 */     return new EntityDataAccessor<>(debug1, this);
/*    */   }
/*    */   
/*    */   T copy(T paramT);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */