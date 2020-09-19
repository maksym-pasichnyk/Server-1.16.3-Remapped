/*    */ package net.minecraft.world.level;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public class DataPackConfig {
/* 11 */   public static final DataPackConfig DEFAULT = new DataPackConfig((List<String>)ImmutableList.of("vanilla"), (List<String>)ImmutableList.of());
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.STRING.listOf().fieldOf("Enabled").forGetter(()), (App)Codec.STRING.listOf().fieldOf("Disabled").forGetter(())).apply((Applicative)debug0, DataPackConfig::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DataPackConfig> CODEC;
/*    */   private final List<String> enabled;
/*    */   private final List<String> disabled;
/*    */   
/*    */   public DataPackConfig(List<String> debug1, List<String> debug2) {
/* 22 */     this.enabled = (List<String>)ImmutableList.copyOf(debug1);
/* 23 */     this.disabled = (List<String>)ImmutableList.copyOf(debug2);
/*    */   }
/*    */   
/*    */   public List<String> getEnabled() {
/* 27 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public List<String> getDisabled() {
/* 31 */     return this.disabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\DataPackConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */