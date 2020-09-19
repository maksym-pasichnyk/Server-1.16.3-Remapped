/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StructureProcessorList {
/*    */   private final List<StructureProcessor> list;
/*    */   
/*    */   public StructureProcessorList(List<StructureProcessor> debug1) {
/*  9 */     this.list = debug1;
/*    */   }
/*    */   
/*    */   public List<StructureProcessor> list() {
/* 13 */     return this.list;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return "ProcessorList[" + this.list + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */