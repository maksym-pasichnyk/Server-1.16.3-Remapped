/*    */ package io.netty.util;
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
/*    */ @Deprecated
/*    */ public final class DomainMappingBuilder<V>
/*    */ {
/*    */   private final DomainNameMappingBuilder<V> builder;
/*    */   
/*    */   public DomainMappingBuilder(V defaultValue) {
/* 37 */     this.builder = new DomainNameMappingBuilder<V>(defaultValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DomainMappingBuilder(int initialCapacity, V defaultValue) {
/* 48 */     this.builder = new DomainNameMappingBuilder<V>(initialCapacity, defaultValue);
/*    */   }
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
/*    */   public DomainMappingBuilder<V> add(String hostname, V output) {
/* 64 */     this.builder.add(hostname, output);
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DomainNameMapping<V> build() {
/* 75 */     return this.builder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\DomainMappingBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */