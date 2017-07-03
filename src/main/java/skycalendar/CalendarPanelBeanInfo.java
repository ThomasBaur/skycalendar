package skycalendar;

import java.awt.Image;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.beans.VetoableChangeListener;

import javax.swing.JPanel;
import javax.swing.event.AncestorListener;

/**
 * The BeanInfo class for CalendarPanel.
 */
public class CalendarPanelBeanInfo extends SimpleBeanInfo {

	/**
	 * This method returns an image object that can be used to represent the
	 * bean in toolboxes, toolbars, etc.
	 * 
	 * @param iconKind
	 *            The kind of icon requested. This should be one of the constant
	 *            values ICON_COLOR_16x16, ICON_COLOR_32x32, ICON_MONO_16x16, or
	 *            ICON_MONO_32x32
	 * @return An image object representing the requested icon. May return null
	 *         if no suitable icon is available.
	 */
	public Image getIcon(int iconKind) {
		Image image = null;
		if (iconKind == BeanInfo.ICON_COLOR_16x16) {
			image = loadImage("CalendarPanel_16.gif");
		} else if (iconKind == BeanInfo.ICON_COLOR_32x32) {
			image = loadImage("CalendarPanel_32.gif");
		}
		return image;
	}

	/**
	 * Gets the beans PropertyDescriptors.
	 * 
	 * @return An array of PropertyDescriptors describing the editable
	 *         properties supported by this bean.
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor propertyDescriptor[] = {
				// property("alignmentX",
				// "getAlignmentX",
				// "setAlignmentX",
				// "The preferred horizontal alignment of the component."),
				// property("alignmentY",
				// "getAlignmentY",
				// "setAlignmentY",
				// "The preferred vertical alignment of the component."),
				property("toolTipText", "getToolTipText", "setToolTipText",
						"The text to display in a tooltip."),
				property("name", "getName", "setName",
						"The name of this component."),
				property("maximumSize", "getMaximumSize", "setMaximumSize",
						"The maximum size of this component."),
				property("minimumSize", "getMinimumSize", "setMinimumSize",
						"The minimum size of this component."),
				property("preferredSize", "getPreferredSize",
						"setPreferredSize",
						"The preferred size of this component."),
				// property("layout",
				// "getLayout",
				// "setLayout",
				// "The LayoutManager for the panel."),
				property("opaque", "isOpaque", "setOpaque",
						"Whether this component is opaque."),
				property("border", "getBorder", "setBorder",
						"The component's border."),
				property("showMoon", "isShowMoon", "setShowMoon",
						"Whether the moon visibility shall be displayed."),
				property("showSun", "isShowSun", "setShowSun",
						"Whether the sun visibility shall be displayed."),
				property("showNaut", "isShowNaut", "setShowNaut",
						"Whether the nautical twilight shall be displayed."),
				property("showAstro", "isShowAstro", "setShowAstro",
						"Whether the astronomical twilight shall be displayed."),
				property("showCustom", "isShowCustom", "setShowCustom",
						"Whether the visibility of the custom object shall be displayed."),
				property("showGrid", "isShowGrid", "setShowGrid",
						"Whether the grid shall be displayed."),
				property("longitude", "getLongitude", "setLongitude",
						"The longitude of the observers position."),
				property("latitude", "getLatitude", "setLatitude",
						"The latitude of the observers position."),
				property("desiredTimeZone", "getDesiredTimeZone", "setDesiredTimeZone",
						"The timezone of the observers position.") };
		return propertyDescriptor;
	}

	/**
	 * Gets the beans EventSetDescriptors.
	 * 
	 * @return An array of EventSetDescriptors describing the catcheable events
	 *         supported by this bean.
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		EventSetDescriptor eventSetDescriptor[] = {
				eventSet("ancestor", AncestorListener.class, new String[] {
						"ancestorAdded", "ancestorMoved", "ancestorRemoved" },
						"addAncestorListener", "removeAncestorListener"),
				eventSet("component", ComponentListener.class, new String[] {
						"componentHidden", "componentMoved",
						"componentResized", "componentShown" },
						"addComponentListener", "removeComponentListener"),
				eventSet("container", ContainerListener.class, new String[] {
						"componentAdded", "componentRemoved" },
						"addContainerListener", "removeContainerListener"),
				eventSet("key", KeyListener.class, new String[] { "keyPressed",
						"keyReleased", "keyTyped" }, "addKeyListener",
						"removeKeyListener"),
				eventSet("mouse", MouseListener.class, new String[] {
						"mouseClicked", "mouseEntered", "mousePressed" },
						"addMouseListener", "removeMouseListener"),
				eventSet("mouseMotion", MouseMotionListener.class,
						new String[] { "mouseMoved", "mouseDragged" },
						"addMouseMotionListener", "removeMouseMotionListener"),
				eventSet("propertyChange", PropertyChangeListener.class,
						new String[] { "propertyChange" },
						"addPropertyChangeListener",
						"removePropertyChangeListener"),
				eventSet("vetoableChange", VetoableChangeListener.class,
						new String[] { "vetoableChange" },
						"addVetoableChangeListener",
						"removeVetoableChangeListener") };
		return eventSetDescriptor;
	}

	/**
	 * A static convenience method to create a PropertyDescriptor from given
	 * values.
	 * 
	 * @param propertyName
	 *            The name of the property which is to be described.
	 * @param getter
	 *            The name of the getter method for this property.
	 * @param setter
	 *            The name of the setter method for this property.
	 * @param propertyDescription
	 *            A string describing the purpose of this property.
	 * @return A PropertyDescriptor object for the given values.
	 */
	private static PropertyDescriptor property(String propertyName,
			String getter, String setter, String propertyDescription) {
		try {
			PropertyDescriptor p;
			if (getter.length() == 0)
				p = new PropertyDescriptor(propertyName, CalendarPanel.class);
			else
				p = new PropertyDescriptor(propertyName, CalendarPanel.class,
						getter, setter);
			p.setShortDescription(propertyDescription);
			return p;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * A static convenience method to create an EventDescriptor from given
	 * values.
	 * 
	 * @param eventSetName
	 *            The name of the eventset which is to be described.
	 * @param listenerClass
	 *            The Interface to be implemented to listen to this eventset.
	 * @param listener
	 *            The array of listener method names for this event.
	 * @return An EventSetDescriptor object for the given values.
	 */
	private static EventSetDescriptor eventSet(String eventSetName,
			Class<?> listenerClass, String[] listener, String addListenerName,
			String removeListenerName) {
		try {
			EventSetDescriptor es;
			es = new EventSetDescriptor(JPanel.class, eventSetName,
					listenerClass, listener, addListenerName,
					removeListenerName);
			return es;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
