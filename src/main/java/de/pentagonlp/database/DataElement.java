package de.pentagonlp.database;

public class DataElement {
	
	private final String value;
	
	public DataElement(String value) {
		this.value = value;
	}
	
	public boolean isNull() {
		return value==null;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public byte toByte() {
		return Byte.parseByte(value);
	}
	
	public int toInt() {
		return Integer.parseInt(value);
	}
	
	public long toLong() {
		return Long.parseLong(value);
	}
	
	public float toFloat() {
		return Float.parseFloat(value);
	}
	
	public double toDouble() {
		return Double.parseDouble(value);
	}
	
	public boolean toBoolean() {
		return Boolean.parseBoolean(value)||(!isNull()&&value.equals("1"));
	}
	
	public boolean equals(String s) {
		return value.equals(s);
	}
	
	public boolean equals(byte b) {
		return toByte()==b;
	}
	
	public boolean equals(int i) {
		return toInt()==i;
	}
	
	public boolean equals(long l) {
		return toLong()==l;
	}
	
	public boolean equals(Float f) {
		return toFloat()==f;
	}
	
	public boolean equals(double d) {
		return toDouble()==d;
	}
	
	public boolean equals(boolean b) {
		return toBoolean()==b;
	}

}
