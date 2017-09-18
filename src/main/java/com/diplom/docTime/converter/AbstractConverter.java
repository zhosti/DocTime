package com.diplom.docTime.converter;

import java.lang.reflect.InvocationTargetException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.diplom.docTime.facade.AbstractFacade;
import com.diplom.docTime.util.JsfUtil;


public abstract class AbstractConverter<TypeId, TypeObject> implements Converter {
	
	private static Log log = LogFactory.getLog(AbstractConverter.class);

	private AbstractFacade<TypeObject> ejbFacade;
	
	protected AbstractFacade<TypeObject> getFacade() {
		return ejbFacade;
	}
	
	
	protected void setFacade(AbstractFacade<TypeObject> ejbFacade) {
		this.ejbFacade = ejbFacade;
	}
	
	private Class<TypeId> idClass;
	
	private Class<TypeObject> objectClass;
	
	private String getMethodName;
	
	private static final String DEFAULT_GET_METHOD_NAME = "getId";
	
	/**
	 * tipove na id-to i
	 * 
	 * @param idClass
	 * @param objectClass
	 */
	public void initTypes(Class<TypeId> idClass, Class<TypeObject> objectClass, String getMethodName) {
		this.idClass = idClass;
		this.objectClass = objectClass;
		
		if (getMethodName == null) {
			this.getMethodName = DEFAULT_GET_METHOD_NAME;
		}else {
			this.getMethodName = getMethodName;
		}
		
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty() || JsfUtil.isDummySelectItem(component, value)) {
			return null;
		}
		
		Object id = null;
		
		if (idClass == Integer.class) {
			id = Integer.valueOf(value);
		}
		else if (idClass == Long.class) {
			id = Integer.valueOf(value);
		}
		else if (idClass == String.class) {
			id = value;
		}
		
		return ejbFacade.find(id);
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value.getClass() == objectClass) {
			
			java.lang.reflect.Method method;
			
			try {
				method = value.getClass().getMethod(this.getMethodName);
				
				return method.invoke(value).toString();
			}
			catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				log.error("Converting Exception", e);
			}
		}
		return null;
	}
	
	public abstract void init();
	
}
