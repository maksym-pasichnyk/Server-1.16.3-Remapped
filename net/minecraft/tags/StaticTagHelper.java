/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class StaticTagHelper<T>
/*    */ {
/* 15 */   private TagCollection<T> source = TagCollection.empty();
/* 16 */   private final List<Wrapper<T>> wrappers = Lists.newArrayList();
/*    */   private final Function<TagContainer, TagCollection<T>> collectionGetter;
/*    */   
/*    */   public StaticTagHelper(Function<TagContainer, TagCollection<T>> debug1) {
/* 20 */     this.collectionGetter = debug1;
/*    */   }
/*    */   
/*    */   public Tag.Named<T> bind(String debug1) {
/* 24 */     Wrapper<T> debug2 = new Wrapper<>(new ResourceLocation(debug1));
/* 25 */     this.wrappers.add(debug2);
/* 26 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset(TagContainer debug1) {
/* 36 */     TagCollection<T> debug2 = this.collectionGetter.apply(debug1);
/* 37 */     this.source = debug2;
/* 38 */     this.wrappers.forEach(debug1 -> debug1.rebind(debug0::getTag));
/*    */   }
/*    */   
/*    */   public TagCollection<T> getAllTags() {
/* 42 */     return this.source;
/*    */   }
/*    */   
/*    */   public List<? extends Tag.Named<T>> getWrappers() {
/* 46 */     return (List)this.wrappers;
/*    */   }
/*    */   
/*    */   public Set<ResourceLocation> getMissingTags(TagContainer debug1) {
/* 50 */     TagCollection<T> debug2 = this.collectionGetter.apply(debug1);
/* 51 */     Set<ResourceLocation> debug3 = (Set<ResourceLocation>)this.wrappers.stream().map(Wrapper::getName).collect(Collectors.toSet());
/* 52 */     ImmutableSet<ResourceLocation> debug4 = ImmutableSet.copyOf(debug2.getAvailableTags());
/* 53 */     return (Set<ResourceLocation>)Sets.difference(debug3, (Set)debug4);
/*    */   }
/*    */   
/*    */   static class Wrapper<T> implements Tag.Named<T> {
/*    */     @Nullable
/*    */     private Tag<T> tag;
/*    */     protected final ResourceLocation name;
/*    */     
/*    */     private Wrapper(ResourceLocation debug1) {
/* 62 */       this.name = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public ResourceLocation getName() {
/* 67 */       return this.name;
/*    */     }
/*    */     
/*    */     private Tag<T> resolve() {
/* 71 */       if (this.tag == null) {
/* 72 */         throw new IllegalStateException("Tag " + this.name + " used before it was bound");
/*    */       }
/* 74 */       return this.tag;
/*    */     }
/*    */     
/*    */     void rebind(Function<ResourceLocation, Tag<T>> debug1) {
/* 78 */       this.tag = debug1.apply(this.name);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean contains(T debug1) {
/* 83 */       return resolve().contains(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public List<T> getValues() {
/* 88 */       return resolve().getValues();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\StaticTagHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */