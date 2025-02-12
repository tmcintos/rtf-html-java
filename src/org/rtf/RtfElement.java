package org.rtf;

/**
 * This class provides the base technology for debugging and is used as
 * superclass for specific RTF elements like groups, control words, control
 * symbols and texts.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public abstract class RtfElement {
	/**
	 * Outputs debug information.
	 *
	 * @param level
	 *            a value greater than or equal to 0 that specifies the number
	 *            of spaces by which the text should be indented
	 */
	protected abstract void dump(int level);

	/**
	 * Outputs a text indent.
	 *
	 * @param level
	 *            a value greater than or equal to 0 that specifies the number
	 *            of spaces
	 */
	protected void indent(int level) {
		for (int i = 0; i < level; i++) {
			System.out.println("&nbsp;");
		}
	}

	/**
	 * Tests for a font number (\fN) element.
	 *
	 * @return true if this element is a font number (\fN) element
	 */
	public boolean isFontNumber() {
		return false;
	}
}