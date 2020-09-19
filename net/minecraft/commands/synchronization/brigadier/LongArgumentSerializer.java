/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.LongArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongArgumentSerializer
/*    */   implements ArgumentSerializer<LongArgumentType>
/*    */ {
/*    */   public void serializeToNetwork(LongArgumentType debug1, FriendlyByteBuf debug2) {
/* 15 */     boolean debug3 = (debug1.getMinimum() != Long.MIN_VALUE);
/* 16 */     boolean debug4 = (debug1.getMaximum() != Long.MAX_VALUE);
/* 17 */     debug2.writeByte(BrigadierArgumentSerializers.createNumberFlags(debug3, debug4));
/* 18 */     if (debug3) {
/* 19 */       debug2.writeLong(debug1.getMinimum());
/*    */     }
/* 21 */     if (debug4) {
/* 22 */       debug2.writeLong(debug1.getMaximum());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public LongArgumentType deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 28 */     byte debug2 = debug1.readByte();
/* 29 */     long debug3 = BrigadierArgumentSerializers.numberHasMin(debug2) ? debug1.readLong() : Long.MIN_VALUE;
/* 30 */     long debug5 = BrigadierArgumentSerializers.numberHasMax(debug2) ? debug1.readLong() : Long.MAX_VALUE;
/* 31 */     return LongArgumentType.longArg(debug3, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(LongArgumentType debug1, JsonObject debug2) {
/* 36 */     if (debug1.getMinimum() != Long.MIN_VALUE) {
/* 37 */       debug2.addProperty("min", Long.valueOf(debug1.getMinimum()));
/*    */     }
/* 39 */     if (debug1.getMaximum() != Long.MAX_VALUE)
/* 40 */       debug2.addProperty("max", Long.valueOf(debug1.getMaximum())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\LongArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */