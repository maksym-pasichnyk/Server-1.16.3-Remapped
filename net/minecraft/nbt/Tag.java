/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Tag
/*    */ {
/* 37 */   public static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;
/* 38 */   public static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;
/* 39 */   public static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;
/* 40 */   public static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput paramDataOutput) throws IOException;
/*    */ 
/*    */ 
/*    */   
/*    */   String toString();
/*    */ 
/*    */ 
/*    */   
/*    */   byte getId();
/*    */ 
/*    */ 
/*    */   
/*    */   TagType<?> getType();
/*    */ 
/*    */   
/*    */   Tag copy();
/*    */ 
/*    */   
/*    */   default String getAsString() {
/* 63 */     return toString();
/*    */   }
/*    */   
/*    */   default Component getPrettyDisplay() {
/* 67 */     return getPrettyDisplay("", 0);
/*    */   }
/*    */   
/*    */   Component getPrettyDisplay(String paramString, int paramInt);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */