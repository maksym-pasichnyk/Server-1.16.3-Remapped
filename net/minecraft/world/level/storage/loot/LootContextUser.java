/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ 
/*    */ public interface LootContextUser
/*    */ {
/*    */   default Set<LootContextParam<?>> getReferencedContextParams() {
/* 10 */     return (Set<LootContextParam<?>>)ImmutableSet.of();
/*    */   }
/*    */   
/*    */   default void validate(ValidationContext debug1) {
/* 14 */     debug1.validateUser(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContextUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */