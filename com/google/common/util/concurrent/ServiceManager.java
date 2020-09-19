/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ServiceManager
/*     */ {
/* 124 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 125 */   private static final ListenerCallQueue.Callback<Listener> HEALTHY_CALLBACK = new ListenerCallQueue.Callback<Listener>("healthy()")
/*     */     {
/*     */       void call(ServiceManager.Listener listener)
/*     */       {
/* 129 */         listener.healthy();
/*     */       }
/*     */     };
/* 132 */   private static final ListenerCallQueue.Callback<Listener> STOPPED_CALLBACK = new ListenerCallQueue.Callback<Listener>("stopped()")
/*     */     {
/*     */       void call(ServiceManager.Listener listener)
/*     */       {
/* 136 */         listener.stopped();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServiceManagerState state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<Service> services;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopped() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(Service service) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager(Iterable<? extends Service> services) {
/* 194 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 195 */     if (copy.isEmpty()) {
/*     */ 
/*     */       
/* 198 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
/*     */ 
/*     */ 
/*     */       
/* 202 */       copy = ImmutableList.of(new NoOpService());
/*     */     } 
/* 204 */     this.state = new ServiceManagerState((ImmutableCollection<Service>)copy);
/* 205 */     this.services = copy;
/* 206 */     WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
/*     */     
/* 208 */     for (UnmodifiableIterator<Service> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 209 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */ 
/*     */       
/* 212 */       Preconditions.checkArgument((service.state() == Service.State.NEW), "Can only manage NEW services, %s", service); }
/*     */ 
/*     */ 
/*     */     
/* 216 */     this.state.markReady();
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
/*     */   public void addListener(Listener listener, Executor executor) {
/* 243 */     this.state.addListener(listener, executor);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(Listener listener) {
/* 263 */     this.state.addListener(listener, MoreExecutors.directExecutor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync() {
/*     */     UnmodifiableIterator<Service> unmodifiableIterator;
/* 276 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 277 */       Service.State state = service.state();
/* 278 */       Preconditions.checkState((state == Service.State.NEW), "Service %s is %s, cannot start it.", service, state); }
/*     */     
/* 280 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/*     */       try {
/* 282 */         this.state.tryStartTiming(service);
/* 283 */         service.startAsync();
/* 284 */       } catch (IllegalStateException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 289 */         logger.log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       }  }
/*     */     
/* 292 */     return this;
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
/*     */   public void awaitHealthy() {
/* 304 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 319 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync() {
/* 330 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 331 */       service.stopAsync(); }
/*     */     
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void awaitStopped() {
/* 342 */     this.state.awaitStopped();
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
/*     */   public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 355 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHealthy() {
/* 365 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 366 */       if (!service.isRunning()) {
/* 367 */         return false;
/*     */       } }
/*     */     
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultimap<Service.State, Service> servicesByState() {
/* 380 */     return this.state.servicesByState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Service, Long> startupTimes() {
/* 391 */     return this.state.startupTimes();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 396 */     return MoreObjects.toStringHelper(ServiceManager.class)
/* 397 */       .add("services", Collections2.filter((Collection)this.services, Predicates.not(Predicates.instanceOf(NoOpService.class))))
/* 398 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceManagerState
/*     */   {
/* 406 */     final Monitor monitor = new Monitor();
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 410 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 412 */     final Multiset<Service.State> states = this.servicesByState
/* 413 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 416 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int numberOfServices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 443 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class AwaitHealthGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       public boolean isSatisfied() {
/* 454 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices || ServiceManager.ServiceManagerState.this.states
/* 455 */           .contains(Service.State.STOPPING) || ServiceManager.ServiceManagerState.this.states
/* 456 */           .contains(Service.State.TERMINATED) || ServiceManager.ServiceManagerState.this.states
/* 457 */           .contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 464 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class StoppedGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       public boolean isSatisfied() {
/* 474 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 481 */     final List<ListenerCallQueue<ServiceManager.Listener>> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ServiceManagerState(ImmutableCollection<Service> services) {
/* 490 */       this.numberOfServices = services.size();
/* 491 */       this.servicesByState.putAll(Service.State.NEW, (Iterable)services);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void tryStartTiming(Service service) {
/* 499 */       this.monitor.enter();
/*     */       try {
/* 501 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 502 */         if (stopwatch == null) {
/* 503 */           this.startupTimers.put(service, Stopwatch.createStarted());
/*     */         }
/*     */       } finally {
/* 506 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void markReady() {
/* 515 */       this.monitor.enter();
/*     */       try {
/* 517 */         if (!this.transitioned) {
/*     */           
/* 519 */           this.ready = true;
/*     */         } else {
/*     */           
/* 522 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 523 */           for (UnmodifiableIterator<Service> unmodifiableIterator = servicesByState().values().iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 524 */             if (service.state() != Service.State.NEW) {
/* 525 */               servicesInBadStates.add(service);
/*     */             } }
/*     */           
/* 528 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 534 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 539 */       Preconditions.checkNotNull(listener, "listener");
/* 540 */       Preconditions.checkNotNull(executor, "executor");
/* 541 */       this.monitor.enter();
/*     */       
/*     */       try {
/* 544 */         if (!this.stoppedGuard.isSatisfied()) {
/* 545 */           this.listeners.add(new ListenerCallQueue<>(listener, executor));
/*     */         }
/*     */       } finally {
/* 548 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy() {
/* 553 */       this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
/*     */       try {
/* 555 */         checkHealthy();
/*     */       } finally {
/* 557 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 562 */       this.monitor.enter();
/*     */       try {
/* 564 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
/* 565 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + 
/*     */ 
/*     */               
/* 568 */               Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/* 570 */         checkHealthy();
/*     */       } finally {
/* 572 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 577 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 578 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 582 */       this.monitor.enter();
/*     */       try {
/* 584 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
/* 585 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + 
/*     */ 
/*     */               
/* 588 */               Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         }
/*     */       } finally {
/* 591 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     ImmutableMultimap<Service.State, Service> servicesByState() {
/* 596 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 597 */       this.monitor.enter();
/*     */       try {
/* 599 */         for (Map.Entry<Service.State, Service> entry : (Iterable<Map.Entry<Service.State, Service>>)this.servicesByState.entries()) {
/* 600 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 601 */             builder.put(entry);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 605 */         this.monitor.leave();
/*     */       } 
/* 607 */       return (ImmutableMultimap<Service.State, Service>)builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes() {
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 612 */       this.monitor.enter();
/*     */       try {
/* 614 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 616 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 617 */           Service service = entry.getKey();
/* 618 */           Stopwatch stopWatch = entry.getValue();
/* 619 */           if (!stopWatch.isRunning() && !(service instanceof ServiceManager.NoOpService)) {
/* 620 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */           }
/*     */         } 
/*     */       } finally {
/* 624 */         this.monitor.leave();
/*     */       } 
/* 626 */       Collections.sort(loadTimes, 
/*     */           
/* 628 */           (Comparator<? super Map.Entry<Service, Long>>)Ordering.natural()
/* 629 */           .onResultOf(new Function<Map.Entry<Service, Long>, Long>()
/*     */             {
/*     */               public Long apply(Map.Entry<Service, Long> input)
/*     */               {
/* 633 */                 return input.getValue();
/*     */               }
/*     */             }));
/* 636 */       return ImmutableMap.copyOf(loadTimes);
/*     */     }
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
/*     */     void transitionService(Service service, Service.State from, Service.State to) {
/* 651 */       Preconditions.checkNotNull(service);
/* 652 */       Preconditions.checkArgument((from != to));
/* 653 */       this.monitor.enter();
/*     */       try {
/* 655 */         this.transitioned = true;
/* 656 */         if (!this.ready) {
/*     */           return;
/*     */         }
/*     */         
/* 660 */         Preconditions.checkState(this.servicesByState
/* 661 */             .remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */ 
/*     */ 
/*     */         
/* 665 */         Preconditions.checkState(this.servicesByState
/* 666 */             .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 671 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 672 */         if (stopwatch == null) {
/*     */           
/* 674 */           stopwatch = Stopwatch.createStarted();
/* 675 */           this.startupTimers.put(service, stopwatch);
/*     */         } 
/* 677 */         if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
/*     */           
/* 679 */           stopwatch.stop();
/* 680 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 681 */             ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 687 */         if (to == Service.State.FAILED) {
/* 688 */           fireFailedListeners(service);
/*     */         }
/*     */         
/* 691 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
/*     */ 
/*     */           
/* 694 */           fireHealthyListeners();
/* 695 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 696 */           fireStoppedListeners();
/*     */         } 
/*     */       } finally {
/* 699 */         this.monitor.leave();
/*     */         
/* 701 */         executeListeners();
/*     */       } 
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void fireStoppedListeners() {
/* 707 */       ServiceManager.STOPPED_CALLBACK.enqueueOn(this.listeners);
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void fireHealthyListeners() {
/* 712 */       ServiceManager.HEALTHY_CALLBACK.enqueueOn(this.listeners);
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void fireFailedListeners(final Service service) {
/* 717 */       (new ListenerCallQueue.Callback<ServiceManager.Listener>("failed({service=" + service + "})")
/*     */         {
/*     */           void call(ServiceManager.Listener listener) {
/* 720 */             listener.failure(service);
/*     */           }
/* 722 */         }).enqueueOn(this.listeners);
/*     */     }
/*     */ 
/*     */     
/*     */     void executeListeners() {
/* 727 */       Preconditions.checkState(
/* 728 */           !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */ 
/*     */       
/* 731 */       for (int i = 0; i < this.listeners.size(); i++) {
/* 732 */         ((ListenerCallQueue)this.listeners.get(i)).execute();
/*     */       }
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 738 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
/*     */ 
/*     */ 
/*     */         
/* 742 */         IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/* 743 */         throw exception;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */ 
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */ 
/*     */     
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state) {
/* 760 */       this.service = service;
/* 761 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void starting() {
/* 766 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 767 */       if (state != null) {
/* 768 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 769 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 770 */           ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void running() {
/* 777 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 778 */       if (state != null) {
/* 779 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stopping(Service.State from) {
/* 785 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 786 */       if (state != null) {
/* 787 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void terminated(Service.State from) {
/* 793 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 794 */       if (state != null) {
/* 795 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 796 */           ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 801 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {
/* 807 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 808 */       if (state != null) {
/*     */ 
/*     */         
/* 811 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/* 812 */         if (log) {
/* 813 */           ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 818 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     private NoOpService() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected void doStart() {
/* 834 */       notifyStarted();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doStop() {
/* 839 */       notifyStopped();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable {
/*     */     private EmptyServiceManagerWarning() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */