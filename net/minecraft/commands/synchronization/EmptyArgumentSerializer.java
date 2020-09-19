/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class EmptyArgumentSerializer<T extends ArgumentType<?>>
/*    */   implements ArgumentSerializer<T> {
/*    */   private final Supplier<T> constructor;
/*    */   
/*    */   public EmptyArgumentSerializer(Supplier<T> debug1) {
/* 13 */     this.constructor = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(T debug1, FriendlyByteBuf debug2) {}
/*    */ 
/*    */   
/*    */   public T deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 22 */     return this.constructor.get();
/*    */   }
/*    */   
/*    */   public void serializeToJson(T debug1, JsonObject debug2) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\EmptyArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */