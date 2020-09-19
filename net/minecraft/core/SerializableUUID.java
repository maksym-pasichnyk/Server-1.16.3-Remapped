/*    */ package net.minecraft.core;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Arrays;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.Util;
/*    */ 
/*    */ public final class SerializableUUID {
/*    */   static {
/* 11 */     CODEC = Codec.INT_STREAM.comapFlatMap(debug0 -> Util.fixedSize(debug0, 4).map(SerializableUUID::uuidFromIntArray), debug0 -> Arrays.stream(uuidToIntArray(debug0)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<UUID> CODEC;
/*    */ 
/*    */   
/*    */   public static UUID uuidFromIntArray(int[] debug0) {
/* 20 */     return new UUID(debug0[0] << 32L | debug0[1] & 0xFFFFFFFFL, debug0[2] << 32L | debug0[3] & 0xFFFFFFFFL);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int[] uuidToIntArray(UUID debug0) {
/* 27 */     long debug1 = debug0.getMostSignificantBits();
/* 28 */     long debug3 = debug0.getLeastSignificantBits();
/* 29 */     return leastMostToIntArray(debug1, debug3);
/*    */   }
/*    */   
/*    */   private static int[] leastMostToIntArray(long debug0, long debug2) {
/* 33 */     return new int[] { (int)(debug0 >> 32L), (int)debug0, (int)(debug2 >> 32L), (int)debug2 };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\SerializableUUID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */