package org.araneaframework.core;

import java.io.Serializable;

import org.araneaframework.Environment;
import org.araneaframework.Relocatable;
import org.araneaframework.Widget;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * A decorator for a widget making it relocatable. A relocatable widget can be
 * moved from one parent to another. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardRelocatableWidgetDecorator extends BaseFilterWidget implements Serializable, Relocatable.RelocatableWidget {
	//*******************************************************************
	// PROTECTED CLASSES
	//*******************************************************************
	protected class RelocatableComponentImpl implements Relocatable.Interface {
		public void overrideEnvironment(Environment newEnv) throws Exception{
			_startWaitingCall();

			_waitNoCall();      
			synchronized (this) {
			  _setEnvironment(newEnv);
			}

			_endWaitingCall();
		}
    
    public Environment getCurrentEnvironment() throws Exception {
      return getEnvironment();
    }
	}

	//*******************************************************************
	// PUBLIC METHODS
	//*******************************************************************
	public Relocatable.Interface _getRelocatable() {
		return new RelocatableComponentImpl();
	}

	/**
	* Constructs a new StandardRelocatableServiceDecorator and sets its child service to child.
	* @param child
	*/
	public StandardRelocatableWidgetDecorator(Widget child) {
		super(child);
	}

	/**
	* Sets the child service of this component.
	* @param child
	*/
	public void setChildWidget(Widget child) {
		this.childWidget = child;
	}

	//*******************************************************************
	// PROTECTED METHODS
	//*******************************************************************
	protected void init() throws Exception {
		childWidget._getComponent().init(new BaseEnvironment() {
			public Object getEntry(Object key) {
				return getEnvironment().getEntry(key);
			}
		});
	}
}
