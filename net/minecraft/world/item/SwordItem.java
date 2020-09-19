/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class SwordItem extends TieredItem implements Vanishable {
/*    */   private final float attackDamage;
/*    */   
/*    */   public SwordItem(Tier debug1, int debug2, float debug3, Item.Properties debug4) {
/* 24 */     super(debug1, debug4);
/*    */     
/* 26 */     this.attackDamage = debug2 + debug1.getAttackDamageBonus();
/*    */ 
/*    */     
/* 29 */     ImmutableMultimap.Builder<Attribute, AttributeModifier> debug5 = ImmutableMultimap.builder();
/* 30 */     debug5.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
/* 31 */     debug5.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", debug3, AttributeModifier.Operation.ADDITION));
/* 32 */     this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)debug5.build();
/*    */   }
/*    */   private final Multimap<Attribute, AttributeModifier> defaultModifiers;
/*    */   public float getDamage() {
/* 36 */     return this.attackDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canAttackBlock(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/* 41 */     return !debug4.isCreative();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 46 */     if (debug2.is(Blocks.COBWEB)) {
/* 47 */       return 15.0F;
/*    */     }
/*    */     
/* 50 */     Material debug3 = debug2.getMaterial();
/* 51 */     if (debug3 == Material.PLANT || debug3 == Material.REPLACEABLE_PLANT || debug3 == Material.CORAL || debug2.is((Tag)BlockTags.LEAVES) || debug3 == Material.VEGETABLE) {
/* 52 */       return 1.5F;
/*    */     }
/* 54 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurtEnemy(ItemStack debug1, LivingEntity debug2, LivingEntity debug3) {
/* 59 */     debug1.hurtAndBreak(1, debug3, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mineBlock(ItemStack debug1, Level debug2, BlockState debug3, BlockPos debug4, LivingEntity debug5) {
/* 66 */     if (debug3.getDestroySpeed((BlockGetter)debug2, debug4) != 0.0F) {
/* 67 */       debug1.hurtAndBreak(2, debug5, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/*    */     }
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCorrectToolForDrops(BlockState debug1) {
/* 74 */     return debug1.is(Blocks.COBWEB);
/*    */   }
/*    */ 
/*    */   
/*    */   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot debug1) {
/* 79 */     if (debug1 == EquipmentSlot.MAINHAND) {
/* 80 */       return this.defaultModifiers;
/*    */     }
/* 82 */     return super.getDefaultAttributeModifiers(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SwordItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */