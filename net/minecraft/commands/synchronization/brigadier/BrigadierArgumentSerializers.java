/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*    */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.arguments.LongArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypes;
/*    */ import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BrigadierArgumentSerializers
/*    */ {
/*    */   public static void bootstrap() {
/* 18 */     ArgumentTypes.register("brigadier:bool", BoolArgumentType.class, (ArgumentSerializer)new EmptyArgumentSerializer(BoolArgumentType::bool));
/* 19 */     ArgumentTypes.register("brigadier:float", FloatArgumentType.class, new FloatArgumentSerializer());
/* 20 */     ArgumentTypes.register("brigadier:double", DoubleArgumentType.class, new DoubleArgumentSerializer());
/* 21 */     ArgumentTypes.register("brigadier:integer", IntegerArgumentType.class, new IntegerArgumentSerializer());
/* 22 */     ArgumentTypes.register("brigadier:long", LongArgumentType.class, new LongArgumentSerializer());
/* 23 */     ArgumentTypes.register("brigadier:string", StringArgumentType.class, new StringArgumentSerializer());
/*    */   }
/*    */   
/*    */   public static byte createNumberFlags(boolean debug0, boolean debug1) {
/* 27 */     byte debug2 = 0;
/* 28 */     if (debug0) {
/* 29 */       debug2 = (byte)(debug2 | 0x1);
/*    */     }
/* 31 */     if (debug1) {
/* 32 */       debug2 = (byte)(debug2 | 0x2);
/*    */     }
/* 34 */     return debug2;
/*    */   }
/*    */   
/*    */   public static boolean numberHasMin(byte debug0) {
/* 38 */     return ((debug0 & 0x1) != 0);
/*    */   }
/*    */   
/*    */   public static boolean numberHasMax(byte debug0) {
/* 42 */     return ((debug0 & 0x2) != 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\BrigadierArgumentSerializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */