/*    */ package net.minecraft.server.packs.metadata.pack;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class PackMetadataSection {
/*  6 */   public static final PackMetadataSectionSerializer SERIALIZER = new PackMetadataSectionSerializer();
/*    */   
/*    */   private final Component description;
/*    */   private final int packFormat;
/*    */   
/*    */   public PackMetadataSection(Component debug1, int debug2) {
/* 12 */     this.description = debug1;
/* 13 */     this.packFormat = debug2;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 17 */     return this.description;
/*    */   }
/*    */   
/*    */   public int getPackFormat() {
/* 21 */     return this.packFormat;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\metadata\pack\PackMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */