package com.diplom.docTime.util;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;

public class JsfUtil {
	
	public static void addErrorMessage(Exception ex, String defaultMsg) {
		String msg = ex.getLocalizedMessage();
		if (msg != null && msg.length() > 0) {
			addErrorMessage(msg);
		}
		else {
			addErrorMessage(defaultMsg);
		}
	}
	
	public static void addErrorMessages(List<String> messages) {
		for (String message : messages) {
			addErrorMessage(message);
		}
	}
	
	public static void addErrorMessageFromBundle(String key, String bundleName) {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		String msg = bundle.getString(key);
		addErrorMessage(msg);
	}
	
	public static void addErrorMessage(String msg) {
		addErrorMessage(msg, null);
	}
	
	public static void addWarningMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}
	
	public static void addErrorMessage(String msg, String componentId) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		FacesContext.getCurrentInstance().addMessage(componentId, facesMsg);
		FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF page if we raise an error message
	}
	
	public static void addSuccessMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}
	
	public static Throwable getRootCause(Throwable cause) {
		if (cause != null) {
			Throwable source = cause.getCause();
			if (source != null) {
				return getRootCause(source);
			}
			else {
				return cause;
			}
		}
		return null;
	}
	
	public static boolean checkExceptionIsCausedBy(Throwable ex, Class<?> causedBy) {
		if (ex != null) {
			Throwable source = ex.getCause();
			if (source != null) {
				if (source.getClass().equals(causedBy)) {
					return true;
				}
				else {
					return checkExceptionIsCausedBy(source, causedBy);
				}
			}
		}
		return false;
	}
	
	public static boolean isValidationFailed() {
		return FacesContext.getCurrentInstance().isValidationFailed();
	}
	
	public static boolean isDummySelectItem(UIComponent component, String value) {
		for (UIComponent children : component.getChildren()) {
			if (children instanceof UISelectItem) {
				UISelectItem item = (UISelectItem) children;
				if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	public static String getComponentMessages(String clientComponent, String defaultMessage) {
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent component = UIComponent.getCurrentComponent(fc).findComponent(clientComponent);
		if (component instanceof UIInput) {
			UIInput inputComponent = (UIInput) component;
			if (inputComponent.isValid()) {
				return defaultMessage;
			}
			else {
				Iterator<FacesMessage> iter = fc.getMessages(inputComponent.getClientId());
				if (iter.hasNext()) {
					return iter.next().getDetail();
				}
			}
		}
		return "";
	}
	
}
