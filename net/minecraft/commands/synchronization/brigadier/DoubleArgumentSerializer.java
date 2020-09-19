/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleArgumentSerializer
/*    */   implements ArgumentSerializer<DoubleArgumentType>
/*    */ {
/*    */   public void serializeToNetwork(DoubleArgumentType debug1, FriendlyByteBuf debug2) {
/* 15 */     boolean debug3 = (debug1.getMinimum() != -1.7976931348623157E308D);
/* 16 */     boolean debug4 = (debug1.getMaximum() != Double.MAX_VALUE);
/* 17 */     debug2.writeByte(BrigadierArgumentSerializers.createNumberFlags(debug3, debug4));
/* 18 */     if (debug3) {
/* 19 */       debug2.writeDouble(debug1.getMinimum());
/*    */     }
/* 21 */     if (debug4) {
/* 22 */       debug2.writeDouble(debug1.getMaximum());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleArgumentType deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 28 */     byte debug2 = debug1.readByte();
/* 29 */     double debug3 = BrigadierArgumentSerializers.numberHasMin(debug2) ? debug1.readDouble() : -1.7976931348623157E308D;
/* 30 */     double debug5 = BrigadierArgumentSerializers.numberHasMax(debug2) ? debug1.readDouble() : Double.MAX_VALUE;
/* 31 */     return DoubleArgumentType.doubleArg(debug3, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(DoubleArgumentType debug1, JsonObject debug2) {
/* 36 */     if (debug1.getMinimum() != -1.7976931348623157E308D) {
/* 37 */       debug2.addProperty("min", Double.valueOf(debug1.getMinimum()));
/*    */     }
/* 39 */     if (debug1.getMaximum() != Double.MAX_VALUE)
/* 40 */       debug2.addProperty("max", Double.valueOf(debug1.getMaximum())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\DoubleArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */