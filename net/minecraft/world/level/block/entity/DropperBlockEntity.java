/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class DropperBlockEntity extends DispenserBlockEntity {
/*    */   public DropperBlockEntity() {
/*  8 */     super(BlockEntityType.DROPPER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 13 */     return (Component)new TranslatableComponent("container.dropper");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\DropperBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */