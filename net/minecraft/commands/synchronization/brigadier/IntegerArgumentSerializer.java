/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntegerArgumentSerializer
/*    */   implements ArgumentSerializer<IntegerArgumentType>
/*    */ {
/*    */   public void serializeToNetwork(IntegerArgumentType debug1, FriendlyByteBuf debug2) {
/* 15 */     boolean debug3 = (debug1.getMinimum() != Integer.MIN_VALUE);
/* 16 */     boolean debug4 = (debug1.getMaximum() != Integer.MAX_VALUE);
/* 17 */     debug2.writeByte(BrigadierArgumentSerializers.createNumberFlags(debug3, debug4));
/* 18 */     if (debug3) {
/* 19 */       debug2.writeInt(debug1.getMinimum());
/*    */     }
/* 21 */     if (debug4) {
/* 22 */       debug2.writeInt(debug1.getMaximum());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public IntegerArgumentType deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 28 */     byte debug2 = debug1.readByte();
/* 29 */     int debug3 = BrigadierArgumentSerializers.numberHasMin(debug2) ? debug1.readInt() : Integer.MIN_VALUE;
/* 30 */     int debug4 = BrigadierArgumentSerializers.numberHasMax(debug2) ? debug1.readInt() : Integer.MAX_VALUE;
/* 31 */     return IntegerArgumentType.integer(debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(IntegerArgumentType debug1, JsonObject debug2) {
/* 36 */     if (debug1.getMinimum() != Integer.MIN_VALUE) {
/* 37 */       debug2.addProperty("min", Integer.valueOf(debug1.getMinimum()));
/*    */     }
/* 39 */     if (debug1.getMaximum() != Integer.MAX_VALUE)
/* 40 */       debug2.addProperty("max", Integer.valueOf(debug1.getMaximum())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\IntegerArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */