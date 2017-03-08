package org.forpdi.core.abstractions;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * <p>
 * Esta classe abstrai lógicas do padrão Factory para criação de componentes. O objetivo é criar
 * componentes (como wrappers) que podem ser melhorados e novos componentes podem ser implementados
 * como plugins.
 * </p>
 * 
 * <p>
 * Uma Factory de componentes aceita o registro de novas classes que implementam a interface desse
 * tipo de componente. Por exemplo, podemos ter uma Factory de conversores de data para cada locale
 * suportado pelo sistema. Porém, um desenvolvedor de um módulo externo pode implementar conversores
 * para outros locales e registrá-los na respectiva factory como plugins.
 * </p>
 * 
 * @author Renato R. R. de Oliveira &lt;renatorro@comp.ufla.br&gt;
 * @param <T> Interface de descreve o tipo de componente que a factory armazenará.
 * 
 * @link org.forpdi.core.company.CompanyThemeFactory CompanyThemeFactory
 * @link org.forpdi.core.user.authz.PermissionFactory PermissionFactory
 */
public abstract class ComponentFactory<T extends IdentifiableComponent> {

	/** Lista de componentes já registrados na factory. */
	private final LinkedList<T> components = new LinkedList<T>();
	
	protected ComponentFactory() {
		
	}
	
	/**
	 * Registra um novo componente nesta factory.
	 * @param component Componenete a ser registrado.
	 * @return Retorna o índice do componente registrado na lista interna.
	 */
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
	
	/**
	 * Recupera um componenete pelo índice.
	 * @param index Índice.
	 * @return Componenete registrado no índice solicitado.
	 */
	public T get(int index) {
		return this.components.get(index);
	}
	
	/**
	 * Recupera um componente pelo seu ID, que por padrão
	 * é nome canônico da classe do componente.
	 * @param componentId ID do componenete.
	 * @return Componente referente ao ID fornecido.
	 */
	public T get(String componentId) {
		for (T component : components) {
			if (component.getId().equals(componentId)) {
				return component;
			}
		}
		return null;
	}
	
	/**
	 * Executa um consumer para cada componente registrado.
	 * @param operation Operação a ser executada.
	 */
	public <R> void each(Consumer<T> operation) {
		for (T component : components) {
			operation.accept(component);
		}
	}
	
	/**
	 * @return Número de componentes registrqados.
	 */
	public int size() {
		return this.components.size();
	}
	
	/**
	 * Serializa os componentes registrados em JSON.
	 * @return JSON que representa os componentes registrados.
	 */
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
