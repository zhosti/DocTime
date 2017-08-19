package com.diplom.docTime.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.exception.ConstraintViolationException;

import com.diplom.docTime.facade.AbstractFacade;
import com.diplom.docTime.util.JsfUtil;


/**
 * Represents an abstract shell of to be used as JSF Controller to be used in
 * AJAX-enabled applications. No outcomes will be generated from its methods
 * since handling is designed to be done inside one page.
 * 
 * @param <T>
 *            the concrete Entity type of the Controller bean to be created
 */
public abstract class AbstractController<T> implements Serializable {
	
	protected static final long serialVersionUID = 1L;
	
	private AbstractFacade<T> ejbFacade;
	private Class<T> itemClass;
	private T selected;
	protected Collection<T> items;
	private String bundleName = "/common";
//	private static Log log = LogFactory.getLog(AbstractController.class);
	
	private enum PersistAction {
		
		CREATE, DELETE, UPDATE
	}
	
	public AbstractController() {
	}
	
	public AbstractController(Class<T> itemClass) {
		this.itemClass = itemClass;
	}
	
	/**
	 * Initialize the concrete controller bean. This AbstractController requires
	 * the EJB Facade object for most operations, and that task is performed by
	 * the concrete controller bean.
	 * <p>
	 * In addition, each controller for an entity that has Many-To-One
	 * relationships, needs to establish references to those entities'
	 * controllers in order to display their information from a context menu.
	 */
	public abstract void init();
	
	/**
	 * Retrieve the current EJB Facade object so that other beans in this
	 * package can perform additional data layer tasks (e.g. additional queries)
	 * 
	 * @return the concrete EJB Facade associated with the concrete controller
	 *         bean.
	 */
	protected AbstractFacade<T> getFacade() {
		return ejbFacade;
	}
	
	/**
	 * Sets the concrete EJB Facade object so that data layer actions can be
	 * performed. This applies to all basic CRUD actions this controller
	 * performs.
	 * 
	 * @param ejbFacade
	 *            the concrete EJB Facade to perform data layer actions with
	 */
	protected void setFacade(AbstractFacade<T> ejbFacade) {
		this.ejbFacade = ejbFacade;
	}
	
	/**
	 * Retrieve the currently selected item.
	 * 
	 * @return the currently selected Entity
	 */
	public T getSelected() {
		return selected;
	}
	
	/**
	 * Pass in the currently selected item.
	 * 
	 * @param selected
	 *            the Entity that should be set as selected
	 */
	public void setSelected(T selected) {
		this.selected = selected;
	}
	
	/**
	 * Sets any embeddable key fields if an Entity uses composite keys. If the
	 * entity does not have composite keys, this method performs no actions and
	 * exists purely to be overridden inside a concrete controller class.
	 */
	protected void setEmbeddableKeys() {
		// Nothing to do if entity does not have any embeddable key.
		System.err.println("Set Embeddable Key Here");
	}
	
	/**
	 * Sets the concrete embedded key of an Entity that uses composite keys.
	 * This method will be overriden inside concrete controller classes and does
	 * nothing if the specific entity has no composite keys.
	 */
	protected void initializeEmbeddableKey() {
		// Nothing to do if entity does not have any embeddable key.
		System.err.println("initialize Embeddable Key Here");
	}
	
	protected void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	protected String getBundleName() {
		return bundleName;
	}
	
	/**
	 * Returns all items as a Collection object.
	 * 
	 * @return a collection of Entity items returned by the data layer
	 */
	public Collection<T> getItems() {
		if (items == null) {
			items = this.ejbFacade.findAll();
		}
		return items;
	}
	
	/**
	 * Pass in collection of items
	 * 
	 * @param items
	 *            a collection of Entity items
	 */
	public void setItems(Collection<T> items) {
		this.items = items;
	}
	
	public void refreshTable() {
		setItems(null); // Invalidate list of items to trigger re-query.
	}
	
	/**
	 * Apply changes to an existing item to the data layer.
	 * 
	 * @param event
	 *            an event from the widget that wants to save an Entity to the
	 *            data layer
	 */
	public void save(ActionEvent event) {
		String msg = getBundle().getString(itemClass.getSimpleName() + "Updated");
		persist(PersistAction.UPDATE, msg);
	}
	
	/**
	 * Store a new item in the data layer.
	 * 
	 * @param event
	 *            an event from the widget that wants to save a new Entity to
	 *            the data layer
	 */
	public void saveNew(ActionEvent event) {
		String msg = getBundle().getString(itemClass.getSimpleName() + "Created");
		persist(PersistAction.CREATE, msg);
		if (!isValidationFailed()) {
			items = null; // Invalidate list of items to trigger re-query.
		}
	}
	
	/**
	 * Remove an existing item from the data layer.
	 * 
	 * @param event
	 *            an event from the widget that wants to delete an Entity from
	 *            the data layer
	 */
	public void delete(ActionEvent event) {
		String msg = getBundle().getString(itemClass.getSimpleName() + "Deleted");
		persist(PersistAction.DELETE, msg);
		if (!isValidationFailed()) {
			selected = null; // Remove selection
			items = null; // Invalidate list of items to trigger re-query.
		}
	}
	
	/**
	 * Performs any data modification actions for an entity. The actions that
	 * can be performed by this method are controlled by the
	 * {@link PersistAction} enumeration and are either CREATE, EDIT or DELETE.
	 * 
	 * @param persistAction
	 *            a specific action that should be performed on the current item
	 * @param successMessage
	 *            a message that should be displayed when persisting the item
	 *            succeeds
	 */
	 private void persist(PersistAction persistAction, String successMessage) {
			if (!JsfUtil.isValidationFailed() && (selected != null)) {
				this.setEmbeddableKeys();
				try {
					if (persistAction != PersistAction.DELETE) {
						mergeSelected();
					} else {
						this.ejbFacade.remove(selected);
					}
					JsfUtil.addSuccessMessage(successMessage);
				} catch (Exception ex) {
					if (JsfUtil.checkExceptionIsCausedBy(ex, ConstraintViolationException.class)) {
						JsfUtil.addErrorMessage(getBundle().getString("ConstraintValidatonError"));
					} else {
						String msg = ex.getCause().getLocalizedMessage();
						if (msg.length() > 0) {
							JsfUtil.addErrorMessage(msg);
						} else {
							JsfUtil.addErrorMessage(ex, getBundle().getString("PersistenceErrorOccured"));
							ex.printStackTrace();
						}
					}
				}
			}
		}
	
	protected void mergeSelected() {
		selected = this.ejbFacade.edit(selected);
	}
	
	/**
	 * Creates a new instance of an underlying entity and assigns it to Selected
	 * property.
	 * 
	 * @param event
	 *            an event from the widget that wants to create a new, unmanaged
	 *            Entity for the data layer
	 * @return a new, unmanaged Entity
	 */
	public T prepareCreate(ActionEvent event) {
		T newItem;
		try {
			newItem = itemClass.newInstance();
			this.selected = newItem;
			initializeEmbeddableKey();
			return newItem;
		}
		catch (InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * overload-vam za da moga da izpolzvam kato prerender
	 * 
	 * @return
	 */
	public T prepareCreate() {
		// if (!FacesContext.getCurrentInstance().isPostback()) {
		
		T newItem;
		try {
			newItem = itemClass.newInstance();
			this.selected = newItem;
			initializeEmbeddableKey();
			return newItem;
			
		}
		catch (InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		
		// }
		return null;
	}
	
	/**
	 * Inform the user interface whether any validation error exist on a page.
	 * 
	 * @return a logical value whether form validation has passed or failed
	 */
	public boolean isValidationFailed() {
		return JsfUtil.isValidationFailed();
	}
	
	/**
	 * Retrieve all messages as a String to be displayed on the page.
	 * 
	 * @param clientComponent
	 *            the component for which the message applies
	 * @param defaultMessage
	 *            a default message to be shown
	 * @return a concatenation of all messages
	 */
	public String getComponentMessages(String clientComponent, String defaultMessage) {
		return JsfUtil.getComponentMessages(clientComponent, defaultMessage);
	}
	
	/**
	 * Retrieve a collection of Entity items for a specific Controller from
	 * another JSF page via Request parameters.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initParams() {
		Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(itemClass.getSimpleName() + "_items");
		if (paramItems != null) {
			this.items = (Collection<T>) paramItems;
		}
	}
	
	public Class<T> getItemClass() {
		return itemClass;
	}
	
	public ResourceBundle getBundle() {
		return ResourceBundle.getBundle(bundleName);
	}
	
	/**
	 * za tablicite e nujno List za mogat da badat sortirani po default
	 * 
	 * @param items
	 * @return
	 */
	public List<T> convertItems(Collection<T> items) {
		return new ArrayList<T>(items);
	}
	
}
