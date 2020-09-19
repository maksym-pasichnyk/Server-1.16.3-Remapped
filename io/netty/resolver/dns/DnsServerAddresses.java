/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DnsServerAddresses
/*     */ {
/*     */   @Deprecated
/*     */   public static List<InetSocketAddress> defaultAddressList() {
/*  43 */     return DefaultDnsServerAddressStreamProvider.defaultAddressList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static DnsServerAddresses defaultAddresses() {
/*  61 */     return DefaultDnsServerAddressStreamProvider.defaultAddresses();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses sequential(Iterable<? extends InetSocketAddress> addresses) {
/*  69 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses sequential(InetSocketAddress... addresses) {
/*  77 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses sequential0(InetSocketAddress... addresses) {
/*  81 */     if (addresses.length == 1) {
/*  82 */       return singleton(addresses[0]);
/*     */     }
/*     */     
/*  85 */     return new DefaultDnsServerAddresses("sequential", addresses)
/*     */       {
/*     */         public DnsServerAddressStream stream() {
/*  88 */           return new SequentialDnsServerAddressStream(this.addresses, 0);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses shuffled(Iterable<? extends InetSocketAddress> addresses) {
/*  98 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses shuffled(InetSocketAddress... addresses) {
/* 106 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses shuffled0(InetSocketAddress[] addresses) {
/* 110 */     if (addresses.length == 1) {
/* 111 */       return singleton(addresses[0]);
/*     */     }
/*     */     
/* 114 */     return new DefaultDnsServerAddresses("shuffled", addresses)
/*     */       {
/*     */         public DnsServerAddressStream stream() {
/* 117 */           return new ShuffledDnsServerAddressStream(this.addresses);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses rotational(Iterable<? extends InetSocketAddress> addresses) {
/* 129 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses rotational(InetSocketAddress... addresses) {
/* 139 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses rotational0(InetSocketAddress[] addresses) {
/* 143 */     if (addresses.length == 1) {
/* 144 */       return singleton(addresses[0]);
/*     */     }
/*     */     
/* 147 */     return new RotationalDnsServerAddresses(addresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DnsServerAddresses singleton(InetSocketAddress address) {
/* 154 */     if (address == null) {
/* 155 */       throw new NullPointerException("address");
/*     */     }
/* 157 */     if (address.isUnresolved()) {
/* 158 */       throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + address);
/*     */     }
/*     */     
/* 161 */     return new SingletonDnsServerAddresses(address);
/*     */   }
/*     */   private static InetSocketAddress[] sanitize(Iterable<? extends InetSocketAddress> addresses) {
/*     */     List<InetSocketAddress> list;
/* 165 */     if (addresses == null) {
/* 166 */       throw new NullPointerException("addresses");
/*     */     }
/*     */ 
/*     */     
/* 170 */     if (addresses instanceof Collection) {
/* 171 */       list = new ArrayList<InetSocketAddress>(((Collection)addresses).size());
/*     */     } else {
/* 173 */       list = new ArrayList<InetSocketAddress>(4);
/*     */     } 
/*     */     
/* 176 */     for (InetSocketAddress a : addresses) {
/* 177 */       if (a == null) {
/*     */         break;
/*     */       }
/* 180 */       if (a.isUnresolved()) {
/* 181 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 183 */       list.add(a);
/*     */     } 
/*     */     
/* 186 */     if (list.isEmpty()) {
/* 187 */       throw new IllegalArgumentException("empty addresses");
/*     */     }
/*     */     
/* 190 */     return list.<InetSocketAddress>toArray(new InetSocketAddress[list.size()]);
/*     */   }
/*     */   
/*     */   private static InetSocketAddress[] sanitize(InetSocketAddress[] addresses) {
/* 194 */     if (addresses == null) {
/* 195 */       throw new NullPointerException("addresses");
/*     */     }
/*     */     
/* 198 */     List<InetSocketAddress> list = new ArrayList<InetSocketAddress>(addresses.length);
/* 199 */     for (InetSocketAddress a : addresses) {
/* 200 */       if (a == null) {
/*     */         break;
/*     */       }
/* 203 */       if (a.isUnresolved()) {
/* 204 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 206 */       list.add(a);
/*     */     } 
/*     */     
/* 209 */     if (list.isEmpty()) {
/* 210 */       return DefaultDnsServerAddressStreamProvider.defaultAddressArray();
/*     */     }
/*     */     
/* 213 */     return list.<InetSocketAddress>toArray(new InetSocketAddress[list.size()]);
/*     */   }
/*     */   
/*     */   public abstract DnsServerAddressStream stream();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */