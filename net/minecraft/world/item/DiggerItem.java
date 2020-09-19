/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DiggerItem extends TieredItem implements Vanishable {
/*    */   private final Set<Block> blocks;
/*    */   protected final float speed;
/*    */   private final float attackDamageBaseline;
/*    */   private final Multimap<Attribute, AttributeModifier> defaultModifiers;
/*    */   
/*    */   protected DiggerItem(float debug1, float debug2, Tier debug3, Set<Block> debug4, Item.Properties debug5) {
/* 24 */     super(debug3, debug5);
/* 25 */     this.blocks = debug4;
/* 26 */     this.speed = debug3.getSpeed();
/* 27 */     this.attackDamageBaseline = debug1 + debug3.getAttackDamageBonus();
/*    */ 
/*    */     
/* 30 */     ImmutableMultimap.Builder<Attribute, AttributeModifier> debug6 = ImmutableMultimap.builder();
/* 31 */     debug6.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
/* 32 */     debug6.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", debug2, AttributeModifier.Operation.ADDITION));
/* 33 */     this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)debug6.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 38 */     return this.blocks.contains(debug2.getBlock()) ? this.speed : 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurtEnemy(ItemStack debug1, LivingEntity debug2, LivingEntity debug3) {
/* 43 */     debug1.hurtAndBreak(2, debug3, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mineBlock(ItemStack debug1, Level debug2, BlockState debug3, BlockPos debug4, LivingEntity debug5) {
/* 50 */     if (!debug2.isClientSide && debug3.getDestroySpeed((BlockGetter)debug2, debug4) != 0.0F) {
/* 51 */       debug1.hurtAndBreak(1, debug5, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/*    */     }
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot debug1) {
/* 58 */     if (debug1 == EquipmentSlot.MAINHAND) {
/* 59 */       return this.defaultModifiers;
/*    */     }
/* 61 */     return super.getDefaultAttributeModifiers(debug1);
/*    */   }
/*    */   
/*    */   public float getAttackDamage() {
/* 65 */     return this.attackDamageBaseline;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DiggerItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */