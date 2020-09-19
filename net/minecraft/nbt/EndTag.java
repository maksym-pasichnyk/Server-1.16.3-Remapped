/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndTag
/*    */   implements Tag
/*    */ {
/* 15 */   public static final TagType<EndTag> TYPE = new TagType<EndTag>()
/*    */     {
/*    */       public EndTag load(DataInput debug1, int debug2, NbtAccounter debug3) {
/* 18 */         debug3.accountBits(64L);
/* 19 */         return EndTag.INSTANCE;
/*    */       }
/*    */ 
/*    */       
/*    */       public String getName() {
/* 24 */         return "END";
/*    */       }
/*    */ 
/*    */       
/*    */       public String getPrettyName() {
/* 29 */         return "TAG_End";
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean isValue() {
/* 34 */         return true;
/*    */       }
/*    */     };
/*    */   
/* 38 */   public static final EndTag INSTANCE = new EndTag();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(DataOutput debug1) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 49 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagType<EndTag> getType() {
/* 54 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "END";
/*    */   }
/*    */ 
/*    */   
/*    */   public EndTag copy() {
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 69 */     return TextComponent.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\EndTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */