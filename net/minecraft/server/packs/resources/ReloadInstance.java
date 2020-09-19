package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import net.minecraft.util.Unit;

public interface ReloadInstance {
  CompletableFuture<Unit> done();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\ReloadInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */