package com.arzio.deadessentials.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.util.ForgeListener;
import com.arzio.deadessentials.service.ForgeBukkitEventManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import guava10.com.google.common.collect.LinkedListMultimap;
import guava10.com.google.common.collect.Multimap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.IEventListener;

public class ForgeBukkitEventManagerImpl implements ForgeBukkitEventManager, Listener {

	private Multimap<Plugin, ForgeListener> pluginListeners = LinkedListMultimap.create();
	
	public ForgeBukkitEventManagerImpl(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		this.unregisterEvents(event.getPlugin());
	}
	
	@Override
	public void registerEvents(Plugin plugin, ForgeListener listener) {

		boolean hasRegistered = false;
		
		for (Method m : listener.getClass().getDeclaredMethods()) {

			if (m.isAnnotationPresent(ForgeSubscribe.class)) {
				Class<?>[] parameterTypes = m.getParameterTypes();
				if (parameterTypes.length != 1) {
					throw new IllegalArgumentException(
							"Method " + m + " has @ForgeSubscribe annotation, but requires " + parameterTypes.length
									+ " arguments.  Event handler methods must require a single argument.");
				}
				Class<?> eventType = parameterTypes[0];
				
				if (!Event.class.isAssignableFrom(eventType)) {
					throw new IllegalArgumentException("Method " + m
							+ " has @ForgeSubscribe annotation, but takes a argument that is not a Event " + eventType);
				}

				try {
					registerEventListener(new EventListenerImpl(listener, m));
					hasRegistered = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// Put the listener into the cache only if any @ForgeSubscribe have been registered
		if (hasRegistered) {
			pluginListeners.put(plugin, listener);
		}
	}
	
	@Override
	public Collection<Plugin> getRegisteredPlugins(){
		return Collections.unmodifiableCollection(pluginListeners.keySet());
	}
	
	@Override
	public Collection<ForgeListener> getListeners(Plugin plugin){
		return Collections.unmodifiableCollection(pluginListeners.get(plugin));
	}
	
	@Override
	public void unregisterEvents(ForgeListener listener) {
		
		// To unregister it, we need to get which plugin this listener has came from.
		for (Plugin plugin : this.getRegisteredPlugins()) {
			for (ForgeListener innerListener : this.getListeners(plugin)) {
				
				if (innerListener.equals(listener)) {
					this.unregister(plugin, listener);
				}
				
			}
		}
	}
	
	@Override
	public void unregisterEvents(Plugin plugin) {
		for (ForgeListener listener : this.getListeners(plugin)) {
			this.unregister(plugin, listener);
		}
	}
	
	@Override
	public void unregisterAll() {
		for (Plugin plugin : this.getRegisteredPlugins()) {
			this.unregisterEvents(plugin);
		}
	}
	
	public void unregister(Plugin plugin, ForgeListener listener) {
		pluginListeners.remove(plugin, listener);
		MinecraftForge.EVENT_BUS.unregister(listener);
	}
	
	public void registerEventListener(EventListenerImpl event) throws Exception {
		Field busIdField = EventBus.class.getDeclaredField("busID");
		busIdField.setAccessible(true);
		int busId = busIdField.getInt(MinecraftForge.EVENT_BUS);
		
		DeadEssentials.getInstance().getLogger().log(Level.INFO, "Registering new Forge event listener with BusID "+busId+" with callback for "+event.getMethodCallback().getName());
		new Event().getListenerList().register(busId, event.getPriority(), event);

		Field listenersField = EventBus.class.getDeclaredField("listeners");
		listenersField.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) listenersField.get(MinecraftForge.EVENT_BUS);
		
		ArrayList<IEventListener> others = listeners.get(event.getListenerInstance());
		if (others == null)
		{
			others = new ArrayList<IEventListener>();
			listeners.put(event.getListenerInstance(), others);
		}
		others.add(event);
	}
	
	public static class EventListenerImpl implements IEventListener {

		private ForgeSubscribe subInfo;
		private Method callback;
		private ForgeListener instance;
		private Class<?> eventType;
		
		public EventListenerImpl(ForgeListener instance, Method callback) {
			this.instance = instance;
			this.callback = callback;
			this.subInfo = callback.getAnnotation(ForgeSubscribe.class);
			this.eventType = callback.getParameterTypes()[0];
		}
		
		public EventPriority getPriority() {
			return this.subInfo.priority();
		}
		
		public ForgeListener getListenerInstance() {
			return this.instance;
		}
		
		public Class<?> getEventType(){
			return this.eventType;
		}
		
		public Method getMethodCallback() {
			return this.callback;
		}
		
		@Override
		public void invoke(Event event) {
			if (eventType.equals(event.getClass())) {
				if (!this.subInfo.receiveCanceled() && event.isCanceled()) {
					return;
				}
				try {
					this.callback.invoke(instance, event);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
	}

}
