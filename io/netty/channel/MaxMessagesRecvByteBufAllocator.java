package io.netty.channel;

public interface MaxMessagesRecvByteBufAllocator extends RecvByteBufAllocator {
  int maxMessagesPerRead();
  
  MaxMessagesRecvByteBufAllocator maxMessagesPerRead(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\MaxMessagesRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */