package net.minecraft.server.packs.repository;

import java.util.function.Consumer;

public interface RepositorySource {
  void loadPacks(Consumer<Pack> paramConsumer, Pack.PackConstructor paramPackConstructor);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\RepositorySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */