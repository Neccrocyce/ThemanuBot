package bots.ThemanuBot;

import java.nio.ByteBuffer;

public class Packet {
	/*
	 * list of protocols
	 */
	
	//parameters
	private final int[][] parameters = CsvReader.getInstance().getParameters();
	
	//values
	private int protocol;
	private boolean[] booleans;
	private byte[] bytes;
	private int[] ints;	
	private long[] longs;
	private double[] doubles;
	private String[] strings;
	
	
	public Packet () {
	}
	
	public Packet (byte protocol, boolean[] booleans, byte[] bytes, int[] ints, long[] longs, double[] doubles, String[] strings) {
		this.protocol = Byte.toUnsignedInt(protocol);
		this.booleans = booleans;
		this.bytes = bytes;
		this.ints = ints;
		this.longs = longs;
		this.doubles = doubles;
		this.strings = strings;
	}	
	
	/**
	 * 
	 * @param data received byte-array; first byte saves the protocol
	 * @return an Packet object or null if byte-array represents an invalid packet
	 */
	public Packet extract (byte[] data) {
		int i = 1;
		try {
			this.protocol = Byte.toUnsignedInt(data[0]);		
			this.booleans = new boolean[parameters[protocol][0]];
			this.bytes = new byte[parameters[protocol][1]];
			this.ints = new int[parameters[protocol][2]];
			this.longs = new long[parameters[protocol][3]];
			this.doubles = new double[parameters[protocol][4]];
			this.strings = new String[parameters[protocol][5]];
			
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < parameters[protocol][j]; k++) {
					switch (j) {
					//boolean
					case 0:
						booleans[k] = data[i] > 0;
						i++;
						break;
					//byte
					case 1:
						bytes[k] = data[i];
						i++;
						break;
					//int
					case 2:
						ints[k] = ByteBuffer.wrap(data).getInt(i);
						i = i + 4;
						break;
					//long
					case 3:
						longs[k] = ByteBuffer.wrap(data).getLong(i);
						i = i + 8;
						break;
					//double
					case 4:
						doubles[k] = ByteBuffer.wrap(data).getDouble(i);
						i = i + 8;
						break;
					//String
					case 5:
						int len = ByteBuffer.wrap(data).getShort(i);
						i += 2;
						strings[k] = "";
						for (int l = 0; l < len; i++) {
							strings[k] += (char) (data[i + l]);
						}
						i = i + len;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		return this;
	}
	
	/**
	 * 
	 * @return byte-array representing the packet or null if packet is invalid
	 * @throws IllegalArgumentException if a String value has too many characters (>32767)
	 */
	public byte[] pack () throws IllegalArgumentException {
		if (!isValidPacket()) {
			return null;
		}
		int len = 1 + booleans.length + bytes.length + ints.length * 4 + longs.length * 8 + doubles.length * 8;
		for (String s : strings) {
			len += 1 + s.length();
		}
		ByteBuffer buf = ByteBuffer.allocate(len);
		//protocol
		buf.put(0, (byte) protocol);
		int i = 1;
		for (int j = 0; j < 6; j++) {
			//booleans
			for (boolean b : booleans) {
				buf.put(i, (byte) (b ? 1 : 0));
				i++;
			}
			//bytes
			for (byte b : bytes) {
				buf.put(i, b);
				i++;
			}
			//ints
			for (int n : ints) {
				buf.putInt(i, n);
				i = i + 4;
			}
			//longs
			for (long l : longs) {
				buf.putLong(i,l);
				i = i + 8;
			}
			//doubles
			for (double d : doubles) {
				buf.putDouble(i, d);
				i = i + 8;
			}
			//strings
			for (String s : strings) {
				if (s.length() >= Short.MAX_VALUE) {
					throw new IllegalArgumentException("Too many characters in String");
				}
				buf.putShort(i, (short) (s.length()));
				i += 2;
				byte sa[] = s.getBytes();
				for (byte b : sa) {
					buf.put(i, b);
					i++;
				}
			}
		}
		return buf.array();
	}
	
	public boolean isValidPacket () {
		if (protocol < 1 || protocol > 255) {
			return false;
		}
		if (booleans == null || booleans.length != parameters[protocol][0]) {
			return false;
		}
		if (bytes == null || bytes.length != parameters[protocol][1]) {
			return false;
		}
		if (ints == null || ints.length != parameters[protocol][2]) {
			return false;
		}
		if (longs == null || longs.length != parameters[protocol][3]) {
			return false;
		}
		if (doubles == null || doubles.length != parameters[protocol][4]) {
			return false;
		}
		if (strings == null || strings.length != parameters[protocol][5]) {
			return false;
		}
		return true;
	}
	
	public void setProtocol (byte protocol) {
		this.protocol = protocol;
	}
	
	public void setBooleans (boolean[] booleans) {
		this.booleans = booleans;
	}
	
	public void setBytes (byte[] bytes) {
		this.bytes = bytes;
	}
	
	public void setInts (int[] ints) {
		this.ints = ints;
	}
	
	public void setLongs (long[] longs) {
		this.longs = longs;
	}
	
	public void setDoubles (double[] doubles) {
		this.doubles = doubles;
	}
	
	public void setStrings (String[] strings) {
		this.strings = strings;
	}
	
	public int getProtocol() {
		return protocol;
	}
}
