/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public interface TagType<T extends Tag> {
/*    */   T load(DataInput paramDataInput, int paramInt, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   default boolean isValue() {
/* 10 */     return false;
/*    */   }
/*    */   
/*    */   String getName();
/*    */   
/*    */   String getPrettyName();
/*    */   
/*    */   static TagType<EndTag> createInvalid(final int id) {
/* 18 */     return new TagType<EndTag>()
/*    */       {
/*    */         public EndTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/* 21 */           throw new IllegalArgumentException("Invalid tag id: " + id);
/*    */         }
/*    */ 
/*    */         
/*    */         public String getName() {
/* 26 */           return "INVALID[" + id + "]";
/*    */         }
/*    */ 
/*    */         
/*    */         public String getPrettyName() {
/* 31 */           return "UNKNOWN_" + id;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\TagType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */