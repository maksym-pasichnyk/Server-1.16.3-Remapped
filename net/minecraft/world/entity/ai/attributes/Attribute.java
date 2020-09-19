/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ public class Attribute
/*    */ {
/*    */   private final double defaultValue;
/*    */   private boolean syncable;
/*    */   private final String descriptionId;
/*    */   
/*    */   protected Attribute(String debug1, double debug2) {
/* 10 */     this.defaultValue = debug2;
/* 11 */     this.descriptionId = debug1;
/*    */   }
/*    */   
/*    */   public double getDefaultValue() {
/* 15 */     return this.defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClientSyncable() {
/* 20 */     return this.syncable;
/*    */   }
/*    */   
/*    */   public Attribute setSyncable(boolean debug1) {
/* 24 */     this.syncable = debug1;
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public double sanitizeValue(double debug1) {
/* 29 */     return debug1;
/*    */   }
/*    */   
/*    */   public String getDescriptionId() {
/* 33 */     return this.descriptionId;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */