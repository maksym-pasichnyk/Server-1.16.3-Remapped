/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FloatArgumentSerializer
/*    */   implements ArgumentSerializer<FloatArgumentType>
/*    */ {
/*    */   public void serializeToNetwork(FloatArgumentType debug1, FriendlyByteBuf debug2) {
/* 15 */     boolean debug3 = (debug1.getMinimum() != -3.4028235E38F);
/* 16 */     boolean debug4 = (debug1.getMaximum() != Float.MAX_VALUE);
/* 17 */     debug2.writeByte(BrigadierArgumentSerializers.createNumberFlags(debug3, debug4));
/* 18 */     if (debug3) {
/* 19 */       debug2.writeFloat(debug1.getMinimum());
/*    */     }
/* 21 */     if (debug4) {
/* 22 */       debug2.writeFloat(debug1.getMaximum());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatArgumentType deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 28 */     byte debug2 = debug1.readByte();
/* 29 */     float debug3 = BrigadierArgumentSerializers.numberHasMin(debug2) ? debug1.readFloat() : -3.4028235E38F;
/* 30 */     float debug4 = BrigadierArgumentSerializers.numberHasMax(debug2) ? debug1.readFloat() : Float.MAX_VALUE;
/* 31 */     return FloatArgumentType.floatArg(debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(FloatArgumentType debug1, JsonObject debug2) {
/* 36 */     if (debug1.getMinimum() != -3.4028235E38F) {
/* 37 */       debug2.addProperty("min", Float.valueOf(debug1.getMinimum()));
/*    */     }
/* 39 */     if (debug1.getMaximum() != Float.MAX_VALUE)
/* 40 */       debug2.addProperty("max", Float.valueOf(debug1.getMaximum())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\FloatArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */