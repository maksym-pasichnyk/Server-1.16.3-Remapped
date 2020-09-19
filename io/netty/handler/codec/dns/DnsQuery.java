package io.netty.handler.codec.dns;

public interface DnsQuery extends DnsMessage {
  DnsQuery setId(int paramInt);
  
  DnsQuery setOpCode(DnsOpCode paramDnsOpCode);
  
  DnsQuery setRecursionDesired(boolean paramBoolean);
  
  DnsQuery setZ(int paramInt);
  
  DnsQuery setRecord(DnsSection paramDnsSection, DnsRecord paramDnsRecord);
  
  DnsQuery addRecord(DnsSection paramDnsSection, DnsRecord paramDnsRecord);
  
  DnsQuery addRecord(DnsSection paramDnsSection, int paramInt, DnsRecord paramDnsRecord);
  
  DnsQuery clear(DnsSection paramDnsSection);
  
  DnsQuery clear();
  
  DnsQuery touch();
  
  DnsQuery touch(Object paramObject);
  
  DnsQuery retain();
  
  DnsQuery retain(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */