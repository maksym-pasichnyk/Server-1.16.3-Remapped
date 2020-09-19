/*    */ package net.minecraft.world.item.alchemy;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ 
/*    */ public class Potion {
/*    */   private final String name;
/*    */   
/*    */   public static Potion byName(String debug0) {
/* 15 */     return (Potion)Registry.POTION.get(ResourceLocation.tryParse(debug0));
/*    */   }
/*    */   
/*    */   private final ImmutableList<MobEffectInstance> effects;
/*    */   
/*    */   public Potion(MobEffectInstance... debug1) {
/* 21 */     this(null, debug1);
/*    */   }
/*    */   
/*    */   public Potion(@Nullable String debug1, MobEffectInstance... debug2) {
/* 25 */     this.name = debug1;
/* 26 */     this.effects = ImmutableList.copyOf((Object[])debug2);
/*    */   }
/*    */   
/*    */   public String getName(String debug1) {
/* 30 */     return debug1 + ((this.name == null) ? Registry.POTION.getKey(this).getPath() : this.name);
/*    */   }
/*    */   
/*    */   public List<MobEffectInstance> getEffects() {
/* 34 */     return (List<MobEffectInstance>)this.effects;
/*    */   }
/*    */   
/*    */   public boolean hasInstantEffects() {
/* 38 */     if (!this.effects.isEmpty()) {
/* 39 */       for (UnmodifiableIterator<MobEffectInstance> unmodifiableIterator = this.effects.iterator(); unmodifiableIterator.hasNext(); ) { MobEffectInstance debug2 = unmodifiableIterator.next();
/* 40 */         if (debug2.getEffect().isInstantenous()) {
/* 41 */           return true;
/*    */         } }
/*    */     
/*    */     }
/*    */     
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\alchemy\Potion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */