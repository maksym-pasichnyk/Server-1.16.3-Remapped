package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;

public interface DnsMessage extends ReferenceCounted {
  int id();
  
  DnsMessage setId(int paramInt);
  
  DnsOpCode opCode();
  
  DnsMessage setOpCode(DnsOpCode paramDnsOpCode);
  
  boolean isRecursionDesired();
  
  DnsMessage setRecursionDesired(boolean paramBoolean);
  
  int z();
  
  DnsMessage setZ(int paramInt);
  
  int count(DnsSection paramDnsSection);
  
  int count();
  
  <T extends DnsRecord> T recordAt(DnsSection paramDnsSection);
  
  <T extends DnsRecord> T recordAt(DnsSection paramDnsSection, int paramInt);
  
  DnsMessage setRecord(DnsSection paramDnsSection, DnsRecord paramDnsRecord);
  
  <T extends DnsRecord> T setRecord(DnsSection paramDnsSection, int paramInt, DnsRecord paramDnsRecord);
  
  DnsMessage addRecord(DnsSection paramDnsSection, DnsRecord paramDnsRecord);
  
  DnsMessage addRecord(DnsSection paramDnsSection, int paramInt, DnsRecord paramDnsRecord);
  
  <T extends DnsRecord> T removeRecord(DnsSection paramDnsSection, int paramInt);
  
  DnsMessage clear(DnsSection paramDnsSection);
  
  DnsMessage clear();
  
  DnsMessage touch();
  
  DnsMessage touch(Object paramObject);
  
  DnsMessage retain();
  
  DnsMessage retain(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */