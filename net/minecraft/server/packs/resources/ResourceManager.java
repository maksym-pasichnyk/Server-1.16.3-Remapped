/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.resources.ResourceLocation;
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
/*    */ public interface ResourceManager
/*    */ {
/*    */   Resource getResource(ResourceLocation paramResourceLocation) throws IOException;
/*    */   
/*    */   List<Resource> getResources(ResourceLocation paramResourceLocation) throws IOException;
/*    */   
/*    */   Collection<ResourceLocation> listResources(String paramString, Predicate<String> paramPredicate);
/*    */   
/*    */   public enum Empty
/*    */     implements ResourceManager
/*    */   {
/* 37 */     INSTANCE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Resource getResource(ResourceLocation debug1) throws IOException {
/* 46 */       throw new FileNotFoundException(debug1.toString());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public List<Resource> getResources(ResourceLocation debug1) {
/* 56 */       return (List<Resource>)ImmutableList.of();
/*    */     }
/*    */ 
/*    */     
/*    */     public Collection<ResourceLocation> listResources(String debug1, Predicate<String> debug2) {
/* 61 */       return (Collection<ResourceLocation>)ImmutableSet.of();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\ResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */