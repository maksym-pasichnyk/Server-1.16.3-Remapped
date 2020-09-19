/*    */ package net.minecraft.world.level.storage.loot.parameters;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LootContextParams {
/* 13 */   public static final LootContextParam<Entity> THIS_ENTITY = create("this_entity");
/*    */   
/* 15 */   public static final LootContextParam<Player> LAST_DAMAGE_PLAYER = create("last_damage_player");
/*    */   
/* 17 */   public static final LootContextParam<DamageSource> DAMAGE_SOURCE = create("damage_source");
/*    */   
/* 19 */   public static final LootContextParam<Entity> KILLER_ENTITY = create("killer_entity");
/*    */   
/* 21 */   public static final LootContextParam<Entity> DIRECT_KILLER_ENTITY = create("direct_killer_entity");
/*    */   
/* 23 */   public static final LootContextParam<Vec3> ORIGIN = create("origin");
/*    */   
/* 25 */   public static final LootContextParam<BlockState> BLOCK_STATE = create("block_state");
/*    */   
/* 27 */   public static final LootContextParam<BlockEntity> BLOCK_ENTITY = create("block_entity");
/*    */   
/* 29 */   public static final LootContextParam<ItemStack> TOOL = create("tool");
/*    */   
/* 31 */   public static final LootContextParam<Float> EXPLOSION_RADIUS = create("explosion_radius");
/*    */   
/*    */   private static <T> LootContextParam<T> create(String debug0) {
/* 34 */     return new LootContextParam<>(new ResourceLocation(debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */