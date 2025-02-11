package org.rtf;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an RTF group in the element tree.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public class RtfGroup extends RtfElement {
	/**
	 * Instance of the parent group element
	 */
	public RtfGroup parent;

	/**
	 * List of child elements (group, control word, control symbol, text)
	 */
	public List<RtfElement> children;

	/**
	 * Creates a new group element.
	 */
	public RtfGroup() {
		parent = null;
		children = new ArrayList<>();
	}

	/**
	 * Get the first child.
	 *
	 * @return the first child, or null if none
	 */
	protected RtfElement getFirstChild() {
		if (children.isEmpty()) {
			return null;
		}
		return children.get(0);
	}

	/**
	 * Get the destination.
	 *
	 * @return The first RtfControlWord of the group, or null if there is none.
	 */
	public RtfControlWord getDestination() {
		RtfElement firstChild = getFirstChild();

		// If the first child is an ignorable destination marker, then skip it.
		if (firstChild instanceof RtfControlSymbol) {
			RtfControlSymbol rtfControlSymbol = (RtfControlSymbol) firstChild;
			if (rtfControlSymbol.isIgnorableDestinationMarker() && children.size() > 1) {
				firstChild = children.get(1);
			}
		}

		if (firstChild instanceof RtfControlWord) {
			return (RtfControlWord) firstChild;
		}

		return null;
	}

	/**
	 * Gets the group type.
	 *
	 * @return control word of the first child as type or an empty string if
	 *         there are no children or the first child is not a control word
	 */
	public String getType() {
		RtfControlWord destination = getDestination();
		return (destination != null) ? destination.word : "";
	}

	/**
	 * Indicates whether this entire group should be ignored when its destination is unknown.
	 *
	 * @return true if the first child of the group is an ignorable destination marker, false otherwise
	 */
	public boolean isIgnorableDestination() {
		RtfElement firstChild = getFirstChild();
		RtfControlSymbol controlSymbol = firstChild instanceof RtfControlSymbol ? (RtfControlSymbol)firstChild : null;

		return (controlSymbol != null) && controlSymbol.isIgnorableDestinationMarker();
	}

	/**
	 * Outputs debug information.
	 */
	public void dump() {
		dump(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.rtf.RtfElement#dump(int)
	 */
	@Override
	public void dump(int level) {
		System.out.println("<div>");
		indent(level);
		System.out.println("{");
		System.out.println("</div>");

		for (RtfElement child : children) {
			if (child instanceof RtfGroup) {
				RtfGroup group = (RtfGroup) child;

				// Can we ignore this group?
				if (group.getType().equals("fonttbl")) {
					continue;
				}
				if (group.getType().equals("colortbl")) {
					continue;
				}
				if (group.getType().equals("stylesheet")) {
					continue;
				}
				if (group.getType().equals("info")) {
					continue;
				}

				// Skip any pictures and destinations.
				if (group.getType().length() >= 4 && group.getType().substring(0, 4).equals("pict")) {
					continue;
				}
				if (group.isIgnorableDestination()) {
					continue;
				}
			}

			child.dump(level + 2);
		}

		System.out.println("<div>");
		indent(level);
		System.out.println("}");
		System.out.println("</div>");
	}
}