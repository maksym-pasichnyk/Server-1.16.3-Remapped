package net.minecraft.network.chat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

public interface ContextAwareComponent {
  MutableComponent resolve(@Nullable CommandSourceStack paramCommandSourceStack, @Nullable Entity paramEntity, int paramInt) throws CommandSyntaxException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\ContextAwareComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */