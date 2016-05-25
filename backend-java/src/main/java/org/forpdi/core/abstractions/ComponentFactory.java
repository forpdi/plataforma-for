package org.forpdi.core.abstractions;

import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class ComponentFactory<T extends IdentifiableComponent> {

	private final LinkedList<T> components = new LinkedList<T>();
	
	protected ComponentFactory() {
		
	}
	
	public int register(T component) {
		if (component == null) {
			throw new IllegalArgumentException("Null component object passed.");
		}
		if (components.contains(component)) {
			throw new IllegalArgumentException("Duplicate component registering: "+component.getId());
		}
		this.components.add(component);
		return this.components.size()-1;
	}
	
	public T get(int index) {
		return this.components.get(index);
	}
	
	public T get(String componentId) {
		for (T component : components) {
			if (component.getId().equals(componentId)) {
				return component;
			}
		}
		return null;
	}
	
	public <R> void each(Consumer<T> operation) {
		for (T component : components) {
			operation.accept(component);
		}
	}
	
	public int size() {
		return this.components.size();
	}
	
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("[");
		for (int t = 0; t < components.size(); t++) {
			if (t > 0)
				json.append(",");
			T component = components.get(t);
			json
				.append("{")
				.append("\"label\":\"").append(component.getDisplayName()).append("\"")
				.append(",\"id\":\"").append(component.getId()).append("\"")
				.append("}")
			;
		}
		json.append("]");
		return json.toString();
	}
	
}
