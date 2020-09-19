/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ public enum PackType {
/*  4 */   CLIENT_RESOURCES("assets"),
/*  5 */   SERVER_DATA("data");
/*    */   
/*    */   private final String directory;
/*    */ 
/*    */   
/*    */   PackType(String debug3) {
/* 11 */     this.directory = debug3;
/*    */   }
/*    */   
/*    */   public String getDirectory() {
/* 15 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\PackType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */