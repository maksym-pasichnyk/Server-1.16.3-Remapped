/*    */ package net.minecraft.data.models.model;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class DelegatedModel
/*    */   implements Supplier<JsonElement> {
/*    */   private final ResourceLocation parent;
/*    */   
/*    */   public DelegatedModel(ResourceLocation debug1) {
/* 13 */     this.parent = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement get() {
/* 18 */     JsonObject debug1 = new JsonObject();
/* 19 */     debug1.addProperty("parent", this.parent.toString());
/* 20 */     return (JsonElement)debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\DelegatedModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */