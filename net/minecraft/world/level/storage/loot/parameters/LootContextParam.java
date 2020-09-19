/*    */ package net.minecraft.world.level.storage.loot.parameters;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class LootContextParam<T> {
/*    */   private final ResourceLocation name;
/*    */   
/*    */   public LootContextParam(ResourceLocation debug1) {
/*  9 */     this.name = debug1;
/*    */   }
/*    */   
/*    */   public ResourceLocation getName() {
/* 13 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return "<parameter " + this.name + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */