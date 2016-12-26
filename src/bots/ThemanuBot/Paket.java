package bots.ThemanuBot;

import java.nio.ByteBuffer;

public class Paket {
	/*
	 * list of protocols
	 */
	//ERROR
	public final static byte ERR_UNKNOWN_PROTOCOL = 0x00;
	public final static byte ERR_MISSING_PARAMETERS = 0x01;
	public final static byte ERR_WRONG_PARAMETER = 0x02;
	//GENERAL
	public final static byte GEN_LOGIN_RT = 0x20;
	public final static byte GEN_LOGIN_RY = 0x21;
	public final static byte GEN_UPDATE_RT = 0x22;
	public final static byte GEN_UPDATE_RY = 0x23;
	public final static byte GEN_STANDBY_RT = 0x24;
	public final static byte GEN_STANDBY_RY = 0x25;
	public final static byte GEN_UPDATE_AVAILABLE = 0x26;
	//NEWS
	public final static byte NEWS_CREATE_RT = 0x50;
	public final static byte NEWS_CREATE_RY = 0x51;
	public final static byte NEWS_EDIT_RT = 0x52;
	public final static byte NEWS_EDIT_RY = 0x53;
	
	//parameters
	private final byte[][] parameters = new byte[256][8];
	
	//values
	private int protocol;
	private boolean[] booleans;	//0,1
	private byte[] bytes;		//2
//	private short[] shorts;		//3
	private int[] ints;			//4
	private long[] longs;		//5
//	private float[] floats;		//6
	private double[] doubles;	//7
	private String[] strings;		//8
	
	/**
	 * 
	 */
	public Paket () {
		setParameters();
	}
	
//	private Paket setEmpty () {
//		this.protocol = 0;		
//		this.booleans = new boolean[0];
//		this.bytes = new byte[0];
//		this.ints = new int[0];
//		this.longs = new long[0];
//		this.doubles = new double[0];
//		this.strings = new String[0];
//		return this;
//	}
	
	private void setParameters () {
		/* Usage:
		 * parameters[i] = new byte[] {numberOfBooleans, numberOfBytes, numberOfInts, numberOfLongs, numberOfDoubles, numberOfStrings};
		 */
		//ERROR
		parameters[0x00] = new byte[] {0,0,0,0,0,1};
		parameters[0x01] = new byte[] {0,0,0,0,0,1};
		parameters[0x02] = new byte[] {0,0,0,0,0,1};
		//GENERAL
		parameters[0x20] = new byte[] {0,0,0,0,0,3};
		parameters[0x21] = new byte[] {2,0,1,0,0,0};
		parameters[0x22] = new byte[] {0,0,1,0,0,0};
		parameters[0x23] = new byte[] {0,0,0,0,0,0};
		parameters[0x24] = new byte[] {0,0,0,0,0,1};
		parameters[0x25] = new byte[] {0,0,0,0,0,0};
		parameters[0x26] = new byte[] {0,0,0,0,0,0};
		//NEWS
		parameters[0x50] = new byte[] {0,0,1,0,0,1};
		parameters[0x51] = new byte[] {0,0,0,0,0,0};
		parameters[0x52] = new byte[] {0,0,2,0,0,2};
		parameters[0x53] = new byte[] {0,0,0,0,0,0};
	}
	
	/**
	 * 
	 * @param data received byte-array; first byte saves the protocol
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Paket extract (byte[] data) throws IllegalArgumentException {
		this.protocol = Byte.toUnsignedInt(data[0]);		
		this.booleans = new boolean[parameters[protocol][0]];
		this.bytes = new byte[parameters[protocol][1]];
		this.ints = new int[parameters[protocol][2]];
		this.longs = new long[parameters[protocol][3]];
		this.doubles = new double[parameters[protocol][4]];
		this.strings = new String[parameters[protocol][5]];
		
		int i = 1;
		try {
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
						int len = Byte.toUnsignedInt(data[i]);
						i++;
						strings[k] = "";
						for (int l = 0; l < len; i++) {
							strings[k] += (char) (data[i + l]);
						}
						i = i + len;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
		return this;
	}
	
	/**
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	public byte[] pack () throws IllegalArgumentException {
		int len = 1 + booleans.length + bytes.length + ints.length * 4 + longs.length * 8 + doubles.length * 8;
		for (String s : strings) {
			len += 1 + s.length();
		}
		ByteBuffer buf = ByteBuffer.allocate(len);
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
				if (s.length() > 255) {
					throw new IllegalArgumentException("Too many characters in String");
				}
				buf.put(i, (byte) (s.length()));
				i++;
				byte sa[] = s.getBytes();
				for (byte b : sa) {
					buf.put(i, b);
					i++;
				}
			}
		}
		return buf.array();
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
	
	
	
	
	
}
