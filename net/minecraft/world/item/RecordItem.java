/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JukeboxBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecordItem
/*    */   extends Item
/*    */ {
/* 25 */   private static final Map<SoundEvent, RecordItem> BY_NAME = Maps.newHashMap();
/*    */   
/*    */   private final int analogOutput;
/*    */   private final SoundEvent sound;
/*    */   
/*    */   protected RecordItem(int debug1, SoundEvent debug2, Item.Properties debug3) {
/* 31 */     super(debug3);
/*    */     
/* 33 */     this.analogOutput = debug1;
/* 34 */     this.sound = debug2;
/*    */     
/* 36 */     BY_NAME.put(this.sound, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 41 */     Level debug2 = debug1.getLevel();
/* 42 */     BlockPos debug3 = debug1.getClickedPos();
/*    */     
/* 44 */     BlockState debug4 = debug2.getBlockState(debug3);
/* 45 */     if (!debug4.is(Blocks.JUKEBOX) || ((Boolean)debug4.getValue((Property)JukeboxBlock.HAS_RECORD)).booleanValue()) {
/* 46 */       return InteractionResult.PASS;
/*    */     }
/*    */     
/* 49 */     ItemStack debug5 = debug1.getItemInHand();
/* 50 */     if (!debug2.isClientSide) {
/* 51 */       ((JukeboxBlock)Blocks.JUKEBOX).setRecord((LevelAccessor)debug2, debug3, debug4, debug5);
/* 52 */       debug2.levelEvent(null, 1010, debug3, Item.getId(this));
/* 53 */       debug5.shrink(1);
/*    */       
/* 55 */       Player debug6 = debug1.getPlayer();
/* 56 */       if (debug6 != null) {
/* 57 */         debug6.awardStat(Stats.PLAY_RECORD);
/*    */       }
/*    */     } 
/* 60 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */   }
/*    */   
/*    */   public int getAnalogOutput() {
/* 64 */     return this.analogOutput;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\RecordItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */