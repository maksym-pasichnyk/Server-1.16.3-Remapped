/*    */ package net.minecraft.world.item;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundChatPacket;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ScaffoldingBlockItem extends BlockItem {
/*    */   public ScaffoldingBlockItem(Block debug1, Item.Properties debug2) {
/* 22 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext debug1) {
/* 28 */     BlockPos debug2 = debug1.getClickedPos();
/* 29 */     Level debug3 = debug1.getLevel();
/*    */     
/* 31 */     BlockState debug4 = debug3.getBlockState(debug2);
/* 32 */     Block debug5 = getBlock();
/* 33 */     if (debug4.is(debug5)) {
/*    */       Direction debug6;
/* 35 */       if (debug1.isSecondaryUseActive()) {
/* 36 */         debug6 = debug1.isInside() ? debug1.getClickedFace().getOpposite() : debug1.getClickedFace();
/*    */       } else {
/* 38 */         debug6 = (debug1.getClickedFace() == Direction.UP) ? debug1.getHorizontalDirection() : Direction.UP;
/*    */       } 
/*    */       
/* 41 */       int debug7 = 0;
/* 42 */       BlockPos.MutableBlockPos debug8 = debug2.mutable().move(debug6);
/* 43 */       while (debug7 < 7) {
/* 44 */         if (!debug3.isClientSide && !Level.isInWorldBounds((BlockPos)debug8)) {
/*    */           
/* 46 */           Player debug9 = debug1.getPlayer();
/* 47 */           int debug10 = debug3.getMaxBuildHeight();
/* 48 */           if (debug9 instanceof ServerPlayer && debug8.getY() >= debug10) {
/* 49 */             ClientboundChatPacket debug11 = new ClientboundChatPacket((Component)(new TranslatableComponent("build.tooHigh", new Object[] { Integer.valueOf(debug10) })).withStyle(ChatFormatting.RED), ChatType.GAME_INFO, Util.NIL_UUID);
/* 50 */             ((ServerPlayer)debug9).connection.send((Packet)debug11);
/*    */           } 
/*    */           
/*    */           break;
/*    */         } 
/* 55 */         debug4 = debug3.getBlockState((BlockPos)debug8);
/*    */         
/* 57 */         if (!debug4.is(getBlock())) {
/* 58 */           if (debug4.canBeReplaced(debug1)) {
/* 59 */             return BlockPlaceContext.at(debug1, (BlockPos)debug8, debug6);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/* 64 */         debug8.move(debug6);
/* 65 */         if (debug6.getAxis().isHorizontal()) {
/* 66 */           debug7++;
/*    */         }
/*    */       } 
/*    */       
/* 70 */       return null;
/*    */     } 
/*    */     
/* 73 */     if (ScaffoldingBlock.getDistance((BlockGetter)debug3, debug2) == 7) {
/* 74 */       return null;
/*    */     }
/*    */     
/* 77 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mustSurvive() {
/* 82 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ScaffoldingBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */