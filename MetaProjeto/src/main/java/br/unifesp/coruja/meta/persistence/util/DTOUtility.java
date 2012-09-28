package br.unifesp.coruja.meta.persistence.util;

import java.beans.ConstructorProperties;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jdto.DTOBinder;
import org.springframework.beans.factory.annotation.Autowired;

import br.unifesp.coruja.meta.persistence.dto.DTO;
import br.unifesp.coruja.meta.persistence.model.EntityModel;
import br.unifesp.coruja.meta.persistence.util.UpdateEntityException;

/**
 * Classe utilitária para ajudar na manutenção dos DTOs.<br>
 * Originalmente funcionava por convenção, mas adaptada para funcionar por configuração, para integrar melhor ao Spring
 */

@SuppressWarnings("rawtypes")
public class DTOUtility {
	
	private String dtoPrefix;
	private String entityPrefix;
	
	@Autowired
	private DTOBinder binder;
	
	/**
	 * Construtor para a classe, criado especificamente para uso pelo Spring.<br>
	 * Define prefixos de classe para as entidades e os DTOs
	 * @param dtoPrefix
	 * @param entityPrefix
	 */
	@ConstructorProperties({"dtoPrefix, entityPrefix"})
	public DTOUtility(String dtoPrefix, String entityPrefix){
		this.dtoPrefix = dtoPrefix;
		this.entityPrefix = entityPrefix;
	}
	
	/**
	 * Função utilitária para encontrar a classe de entidade correspondente a instância de<br>
	 * DTO passada como parâmetro. 
	 * @param dto a instância do DTO de onde queremos derivar a entidade
	 * @return o objeto Class representando a entidade desejada
	 */
	public Class findEntityClassForDTO(DTO dto){
		Class clazz = dto.getClass();
		String name = clazz.getSimpleName();
		try {
			return Class.forName(entityPrefix + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Função utilitária para encontrar a classe de DTO correspondente a instância de<br>
	 * entidade passada como parâmetro. 
	 * @param ent a instância de entidade de onde queremos derivar a DTO
	 * @return o objeto Class representando o DTO desejada
	 */
	public Class findDTOClassForEntity(Object ent){
		Class clazz = ent.getClass();
		String name = clazz.getSimpleName();
		try {
			return Class.forName(dtoPrefix + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 * Função que atualiza uma entidade com base em um DTO. A função utiliza reflexão para derivar dinamicamente<br>
	 * os getters e setters dos objetos, e então executá-los de acordo. Quando for necessário atualizar um campo<br>
	 * de entidade, a função é chamada recursivamente utilizando como parâmetros a entidade e o DTO embutidos.
	 * @param ent a instância de entidade
	 * @param dto a instância de DTO
	 * @throws IllegalArgumentException
	 * @throws UpdateEntityException 
	 */
	@SuppressWarnings("unchecked")
	public void updateEntityFromDTO(EntityModel ent, DTO dto) throws IllegalArgumentException, UpdateEntityException {
		
		if(ent == null) throw new IllegalArgumentException("Entity argument is null");
		if(dto == null) throw new IllegalArgumentException("DTO argument is null");
		
		Class ent_class = ent.getClass();
		Class dto_class = dto.getClass();
		
		Method[] ec_methods = ent_class.getDeclaredMethods();
		Method[] dc_methods = dto_class.getDeclaredMethods();
		
		ArrayList<Method> dc_getters = new ArrayList<Method>();
		ArrayList<Method> ec_setters = new ArrayList<Method>();
		
		for(Method m : dc_methods) {
			if(m.getName().startsWith("get"))
				dc_getters.add(m);
		}
		
		for(Method n : ec_methods) {
			if(n.getName().startsWith("set"))
				ec_setters.add(n);
		}
		
		for(Method get : dc_getters) {
			if(!get.getName().substring(3).equals("Id")){
				for(Method set : ec_setters) {
					if(get.getName().substring(3).equals(set.getName().substring(3))) {
						Object arg = null;
						try {
							arg = get.invoke(dto, (Object[]) null);
							if(arg instanceof List){
								Object test_type = ((List) arg).get(0);
								if(test_type instanceof DTO) {
									throw new IllegalAccessError("Not supported yet");
								}
								else {
									List<Object> ent_list = new ArrayList<Object>();
									for(Object obj : (List<Object>) arg) {
										ent_list.add(obj);
									}
									set.invoke(ent, ent_list);
								}
							}
							else if(arg instanceof DTO){
								Method ent_getter = ent_class.getMethod(get.getName(), (Class[]) null);
								Object embedded_ent = ent_getter.invoke(ent, (Object[]) null);
								updateEntityFromDTO((EntityModel) embedded_ent, (DTO) arg);
							} else set.invoke(ent, arg);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new UpdateEntityException("Erro ao atualizar entidade.");
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							throw new UpdateEntityException("Erro ao atualizar entidade.");
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							throw new UpdateEntityException("Erro ao atualizar entidade.");
						} catch (SecurityException e) {
							e.printStackTrace();
							throw new UpdateEntityException("Erro ao atualizar entidade.");
						}
					}
				}
			}
		}
	}

}