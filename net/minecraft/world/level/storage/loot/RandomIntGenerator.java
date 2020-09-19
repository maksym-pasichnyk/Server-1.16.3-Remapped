/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public interface RandomIntGenerator
/*    */ {
/*  8 */   public static final ResourceLocation CONSTANT = new ResourceLocation("constant");
/*  9 */   public static final ResourceLocation UNIFORM = new ResourceLocation("uniform");
/* 10 */   public static final ResourceLocation BINOMIAL = new ResourceLocation("binomial");
/*    */   
/*    */   int getInt(Random paramRandom);
/*    */   
/*    */   ResourceLocation getType();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\RandomIntGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */