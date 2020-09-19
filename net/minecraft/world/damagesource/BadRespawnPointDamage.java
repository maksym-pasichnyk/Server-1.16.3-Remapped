/*    */ package net.minecraft.world.damagesource;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class BadRespawnPointDamage extends DamageSource {
/*    */   protected BadRespawnPointDamage() {
/* 13 */     super("badRespawnPoint");
/* 14 */     setScalesWithDifficulty();
/* 15 */     setExplosion();
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getLocalizedDeathMessage(LivingEntity debug1) {
/* 20 */     MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)new TranslatableComponent("death.attack.badRespawnPoint.link")).withStyle(debug0 -> debug0.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://bugs.mojang.com/browse/MCPE-28723")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("MCPE-28723"))));
/*    */ 
/*    */ 
/*    */     
/* 24 */     return (Component)new TranslatableComponent("death.attack.badRespawnPoint.message", new Object[] { debug1.getDisplayName(), mutableComponent });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\BadRespawnPointDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */