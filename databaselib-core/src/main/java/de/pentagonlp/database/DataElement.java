package de.pentagonlp.database;

/**
 * Wrapper class for {@link String} to cast to different data types. Used to
 * wrap around values fetched from the database.
 * 
 * @author PentagonLP
 * 
 */
public class DataElement {

	/**
	 * The value itself, stored as a {@link String}
	 * 
	 */
	private final String value;

	/**
	 * Creates a new {@link DataElement} with a given {@link String} to interpret
	 * 
	 * @param value The {@link String} to interpret
	 * 
	 */
	public DataElement(String value) {
		this.value = value;
	}

	/**
	 * Tests whether the stored {@link String} is {@code null}
	 * 
	 * @return {@code true} if the stored {@link String} is {@code null}
	 * 
	 */
	public boolean isNull() {
		return value == null;
	}

	/**
	 * Returns the stored {@link String}
	 * 
	 * @return The stored {@link String}
	 * 
	 */
	@Override
	public String toString() {
		return value;
	}

	/**
	 * Parses the stored {@link String} to a {@code byte}
	 * 
	 * @return The stored {@link String} as a {@code byte}
	 * 
	 * @see Byte#parseByte(String)
	 * 
	 */
	public byte toByte() {
		return Byte.parseByte(value);
	}

	/**
	 * Parses the stored {@link String} to an {@code int}
	 * 
	 * @return The stored {@link String} as an {@code int}
	 * 
	 * @see Integer#parseInt(String)
	 * 
	 */
	public int toInt() {
		return Integer.parseInt(value);
	}

	/**
	 * Parses the stored {@link String} to a {@code long}
	 * 
	 * @return The stored {@link String} as a {@code long}
	 * 
	 * @see Long#parseLong(String)
	 * 
	 */
	public long toLong() {
		return Long.parseLong(value);
	}

	/**
	 * Parses the stored {@link String} to a {@code float}
	 * 
	 * @return The stored {@link String} as a {@code float}
	 * 
	 * @see Float#parseFloat(String)
	 * 
	 */
	public float toFloat() {
		return Float.parseFloat(value);
	}

	/**
	 * Parses the stored {@link String} to a {@code double}
	 * 
	 * @return The stored {@link String} as a {@code double}
	 * 
	 * @see Double#parseDouble(String)
	 * 
	 */
	public double toDouble() {
		return Double.parseDouble(value);
	}

	/**
	 * Parses the stored {@link String} to a {@code boolean}<br>
	 * Uses {@link Boolean#parseBoolean(String)}, but also interprets {@code 1} as
	 * {@code true} and {@code 0} as {@code false}.
	 * 
	 * @return The stored {@link String} as a {@code boolean}
	 * 
	 */
	public boolean toBoolean() {
		return Boolean.parseBoolean(value) || (!isNull() && value.equals("1"));
	}

	/**
	 * Tests whether the stored {@link String} equals another {@link String}
	 * 
	 * @param s Another String to compare to the stored {@link String}.
	 * 
	 * @return Whether stored {@link String} equals another {@link String}
	 * 
	 */
	public boolean equals(String s) {
		return value.equals(s);
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code byte}
	 * 
	 * @param b The {@code byte} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code byte}
	 * 
	 */
	public boolean equals(byte b) {
		return toByte() == b;
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code int}
	 * 
	 * @param i The {@code int} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code int}
	 * 
	 */
	public boolean equals(int i) {
		return toInt() == i;
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code long}
	 * 
	 * @param l The {@code long} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code long}
	 * 
	 */
	public boolean equals(long l) {
		return toLong() == l;
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code float}
	 * 
	 * @param f The {@code float} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code float}
	 * 
	 */
	public boolean equals(float f) {
		return toFloat() == f;
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code double}
	 * 
	 * @param d The {@code double} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code double}
	 * 
	 */
	public boolean equals(double d) {
		return toDouble() == d;
	}

	/**
	 * Tests whether the parsed version of the stored {@link String} equals to a
	 * given {@code boolean}
	 * 
	 * @param b The {@code boolean} to compare to
	 * 
	 * @return Whether the parsed version of the stored {@link String} equals to the
	 *         {@code boolean}
	 * 
	 */
	public boolean equals(boolean b) {
		return toBoolean() == b;
	}

}
