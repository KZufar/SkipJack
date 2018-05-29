package SkipJack;

import java.util.ArrayList;

public class SkipJack {

	public static final int BLOCK_SIZE = 8;
	public static final int KEY_LENGTH = 10;
	
	private int[] K;
	
	public SkipJack()
	{
		K = new int[12];
		int [] arr = {1, 2, 3, 4, 3, 6, 7, 5, 9, 12, 2, 1};
		//any nums
		for (int i=0; i<K.length; i++) 
		{
			K[i] = arr [i];
			//read into array
		}
	}

	public SkipJack(byte [] key)
	{
		K = new int[key.length];
		for (int i=0; i<K.length; i++) 
		{
			K[i] = (int)key [i];
		}
	}

	public byte[] encrypt(byte[] bytes) 
	{
		return this.handleBlocks(bytes, true);
	}

	public byte[] decrypt (byte[] bytes) 
	{
		return this.handleBlocks(bytes, false);
	}
	
	private byte [] copyBlock (byte [] bytes, int bpos, int length) 
	{
		byte [] block = new byte [BLOCK_SIZE];
		for (int i= 0; i<length; i++) 
		{
			block [i] = bytes [bpos + i];
		}
		return block;
	}
	
	private byte [] handleBlocks (byte[] bytes, boolean isEncrypted) 
	{
		ArrayList<byte[]> list = new ArrayList<byte[]> ();
		int blocks = (int) Math.ceil(((double)bytes.length)/BLOCK_SIZE);
		byte [] result = new byte [blocks * BLOCK_SIZE];
		int bpos = 0;
		int length = BLOCK_SIZE;
		
		for (int i=0; i<blocks; i++) 
		{
			if (length > (bytes.length - bpos ))
			{
				length = bytes.length - bpos;
			}
			byte [] ib = this.copyBlock(bytes, bpos, length);
			// byte [] ob = new byte [BLOCK_SIZE];
			byte [] ob = null;
			if (isEncrypted)
			{
				ob = this.encryptBlock(ib);
			} 
			else 
			{
				ob = this.decryptBlock (ib);
			}
	
			list.add(ob);
			bpos += BLOCK_SIZE;
		}
		
		int count = 0;
		for (int i=0; i< list.size(); i++) 
		{
			byte [] block = list.get(i);
			for (int j=0;j<block.length;j++) 
			{
				result [count ++] = block[j];
			}
		}
		
		return result;
	}
	public byte [] encryptBlock (byte[] ib)
	{
		byte [] ob = new byte [BLOCK_SIZE];
		int count = 0;
	    int w1 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w2 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w3 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w4 = (ib[count++]&0xFF) << 8 | (ib[count  ]&0xFF);
	
	    // A
	    w1  = g(w1, 0); w4 ^= w1 ^ 1;
	    w4  = g(w4, 4); w3 ^= w4 ^ 2;
	    w3  = g(w3, 8); w2 ^= w3 ^ 3;
	    w2  = g(w2, 2); w1 ^= w2 ^ 4;
	    w1  = g(w1, 6); w4 ^= w1 ^ 5;
	    w4  = g(w4, 0); w3 ^= w4 ^ 6;
	    w3  = g(w3, 4); w2 ^= w3 ^ 7;
	    w2  = g(w2, 8); w1 ^= w2 ^ 8;
	    // B
	    w2 ^= w1 ^  9; w1  = g(w1, 2);
	    w1 ^= w4 ^ 10; w4  = g(w4, 6);
	    w4 ^= w3 ^ 11; w3  = g(w3, 0);
	    w3 ^= w2 ^ 12; w2  = g(w2, 4);
	    w2 ^= w1 ^ 13; w1  = g(w1, 8);
	    w1 ^= w4 ^ 14; w4  = g(w4, 2);
	    w4 ^= w3 ^ 15; w3  = g(w3, 6);
	    w3 ^= w2 ^ 16; w2  = g(w2, 0);
	    // A
	    w1  = g(w1, 4); w4 ^= w1 ^ 17;
	    w4  = g(w4, 8); w3 ^= w4 ^ 18;
	    w3  = g(w3, 2); w2 ^= w3 ^ 19;
	    w2  = g(w2, 6); w1 ^= w2 ^ 20;
	    w1  = g(w1, 0); w4 ^= w1 ^ 21;
	    w4  = g(w4, 4); w3 ^= w4 ^ 22;
	    w3  = g(w3, 8); w2 ^= w3 ^ 23;
	    w2  = g(w2, 2); w1 ^= w2 ^ 24;
	    // B
	    w2 ^= w1 ^ 25; w1  = g(w1, 6);
	    w1 ^= w4 ^ 26; w4  = g(w4, 0);
	    w4 ^= w3 ^ 27; w3  = g(w3, 4);
	    w3 ^= w2 ^ 28; w2  = g(w2, 8);
	    w2 ^= w1 ^ 29; w1  = g(w1, 2);
	    w1 ^= w4 ^ 30; w4  = g(w4, 6);
	    w4 ^= w3 ^ 31; w3  = g(w3, 0);
	    w3 ^= w2 ^ 32; w2  = g(w2, 4);
	
	    count = 0;
	    ob[count++] = (byte)(w1 >>> 8);
	    ob[count++] = (byte)(w1      );
	    ob[count++] = (byte)(w2 >>> 8);
	    ob[count++] = (byte)(w2      );
	    ob[count++] = (byte)(w3 >>> 8);
	    ob[count++] = (byte)(w3      );
	    ob[count++] = (byte)(w4 >>> 8);
	    ob[count  ] = (byte)(w4      );
	    
	    return ob;
	}

	public byte[] decryptBlock (byte[] ib)
	{
		byte [] ob = new byte [BLOCK_SIZE];
		int count = 0;
	    int w1 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w2 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w3 = (ib[count++]&0xFF) << 8 | (ib[count++]&0xFF);
	    int w4 = (ib[count++]&0xFF) << 8 | (ib[count  ]&0xFF);
	
	    // B-1
	    w2  = ginv(w2,  7); w3 ^= w2 ^ 32;
	    w3  = ginv(w3,  3); w4 ^= w3 ^ 31;
	    w4  = ginv(w4,  9); w1 ^= w4 ^ 30;
	    w1  = ginv(w1,  5); w2 ^= w1 ^ 29;
	    w2  = ginv(w2, 11); w3 ^= w2 ^ 28;
	    w3  = ginv(w3,  7); w4 ^= w3 ^ 27;
	    w4  = ginv(w4,  3); w1 ^= w4 ^ 26;
	    w1  = ginv(w1,  9); w2 ^= w1 ^ 25;
	    // A-1
	    w1 ^= w2 ^ 24; w2 = ginv(w2,  5);
	    w2 ^= w3 ^ 23; w3 = ginv(w3, 11);
	    w3 ^= w4 ^ 22; w4 = ginv(w4,  7);
	    w4 ^= w1 ^ 21; w1 = ginv(w1,  3);
	    w1 ^= w2 ^ 20; w2 = ginv(w2,  9);
	    w2 ^= w3 ^ 19; w3 = ginv(w3,  5);
	    w3 ^= w4 ^ 18; w4 = ginv(w4, 11);
	    w4 ^= w1 ^ 17; w1 = ginv(w1,  7);
	    // B-1
	    w2  = ginv(w2,  3); w3 ^= w2 ^ 16;
	    w3  = ginv(w3,  9); w4 ^= w3 ^ 15;
	    w4  = ginv(w4,  5); w1 ^= w4 ^ 14;
	    w1  = ginv(w1, 11); w2 ^= w1 ^ 13;
	    w2  = ginv(w2,  7); w3 ^= w2 ^ 12;
	    w3  = ginv(w3,  3); w4 ^= w3 ^ 11;
	    w4  = ginv(w4,  9); w1 ^= w4 ^ 10;
	    w1  = ginv(w1,  5); w2 ^= w1 ^  9;
	    // A-1
	    w1 ^= w2 ^ 8; w2 = ginv(w2, 11);
	    w2 ^= w3 ^ 7; w3 = ginv(w3,  7);
	    w3 ^= w4 ^ 6; w4 = ginv(w4,  3);
	    w4 ^= w1 ^ 5; w1 = ginv(w1,  9);
	    w1 ^= w2 ^ 4; w2 = ginv(w2,  5);
	    w2 ^= w3 ^ 3; w3 = ginv(w3, 11);
	    w3 ^= w4 ^ 2; w4 = ginv(w4,  7);
	    w4 ^= w1 ^ 1; w1 = ginv(w1,  3);
	
	    count = 0;
	    ob[count++] = (byte)(w1 >>> 8);
	    ob[count++] = (byte)(w1      );
	    ob[count++] = (byte)(w2 >>> 8);
	    ob[count++] = (byte)(w2      );
	    ob[count++] = (byte)(w3 >>> 8);
	    ob[count++] = (byte)(w3      );
	    ob[count++] = (byte)(w4 >>> 8);
	    ob[count  ] = (byte)(w4      );
	    
	    return ob;
	
	}

	private final int g (int word, int count)
	{
		int g1, g2, g3, g4, g5, g6;
	    g1 = word >>> 8;
	    g2  = (word & 0xFF);
	    g3 = FTable[g2  ^ K[count  ] & 0xFF] ^ g1;
	    g4 = FTable[g3  ^ K[count+1] & 0xFF] ^ g2;
	    g5 = FTable[g4  ^ K[count+2] & 0xFF] ^ g3;
	    g6 = FTable[g5  ^ K[count+3] & 0xFF] ^ g4;
	    
	    return (g5 << 8) + g6;
	}
	
	private final int ginv(int word, int count)
	{
	    int g1, g2, g3, g4, g5, g6;
	    g5 = word >>> 8;
	    g6  = (word & 0xFF);
	    g4 = FTable[g5  ^ K[count  ] & 0xFF] ^ g6;
	    g3 = FTable[g4  ^ K[count-1] & 0xFF] ^ g5;
	    g2 = FTable[g3  ^ K[count-2] & 0xFF] ^ g4;
	    g1 = FTable[g2  ^ K[count-3] & 0xFF] ^ g3;
	    
	    return (g1 << 8) + g2;
	}
	
	private static final int FTable[] =
	{
		0xA3, 0xD7, 0x09, 0x83, 0xF8, 0x48, 0xF6, 0xF4,
	    0xB3, 0x21, 0x15, 0x78, 0x99, 0xB1, 0xAF, 0xF9,
	    0xE7, 0x2D, 0x4D, 0x8A, 0xCE, 0x4C, 0xCA, 0x2E,
	    0x52, 0x95, 0xD9, 0x1E, 0x4E, 0x38, 0x44, 0x28,
	    0x0A, 0xDF, 0x02, 0xA0, 0x17, 0xF1, 0x60, 0x68,
	    0x12, 0xB7, 0x7A, 0xC3, 0xE9, 0xFA, 0x3D, 0x53,
	    0x96, 0x84, 0x6B, 0xBA, 0xF2, 0x63, 0x9A, 0x19,
	    0x7C, 0xAE, 0xE5, 0xF5, 0xF7, 0x16, 0x6A, 0xA2,
	    0x39, 0xB6, 0x7B, 0x0F, 0xC1, 0x93, 0x81, 0x1B,
	    0xEE, 0xB4, 0x1A, 0xEA, 0xD0, 0x91, 0x2F, 0xB8,
	    0x55, 0xB9, 0xDA, 0x85, 0x3F, 0x41, 0xBF, 0xE0,
	    0x5A, 0x58, 0x80, 0x5F, 0x66, 0x0B, 0xD8, 0x90,
	    0x35, 0xD5, 0xC0, 0xA7, 0x33, 0x06, 0x65, 0x69,
	    0x45, 0x00, 0x94, 0x56, 0x6D, 0x98, 0x9B, 0x76,
	    0x97, 0xFC, 0xB2, 0xC2, 0xB0, 0xFE, 0xDB, 0x20,
	    0xE1, 0xEB, 0xD6, 0xE4, 0xDD, 0x47, 0x4A, 0x1D,
	    0x42, 0xED, 0x9E, 0x6E, 0x49, 0x3C, 0xCD, 0x43,
	    0x27, 0xD2, 0x07, 0xD4, 0xDE, 0xC7, 0x67, 0x18,
	    0x89, 0xCB, 0x30, 0x1F, 0x8D, 0xC6, 0x8F, 0xAA,
	    0xC8, 0x74, 0xDC, 0xC9, 0x5D, 0x5C, 0x31, 0xA4,
	    0x70, 0x88, 0x61, 0x2C, 0x9F, 0x0D, 0x2B, 0x87,
	    0x50, 0x82, 0x54, 0x64, 0x26, 0x7D, 0x03, 0x40,
	    0x34, 0x4B, 0x1C, 0x73, 0xD1, 0xC4, 0xFD, 0x3B,
	    0xCC, 0xFB, 0x7F, 0xAB, 0xE6, 0x3E, 0x5B, 0xA5,
	    0xAD, 0x04, 0x23, 0x9C, 0x14, 0x51, 0x22, 0xF0,
	    0x29, 0x79, 0x71, 0x7E, 0xFF, 0x8C, 0x0E, 0xE2,
	    0x0C, 0xEF, 0xBC, 0x72, 0x75, 0x6F, 0x37, 0xA1,
	    0xEC, 0xD3, 0x8E, 0x62, 0x8B, 0x86, 0x10, 0xE8,
	    0x08, 0x77, 0x11, 0xBE, 0x92, 0x4F, 0x24, 0xC5,
	    0x32, 0x36, 0x9D, 0xCF, 0xF3, 0xA6, 0xBB, 0xAC,
	    0x5E, 0x6C, 0xA9, 0x13, 0x57, 0x25, 0xB5, 0xE3,
	    0xBD, 0xA8, 0x3A, 0x01, 0x05, 0x59, 0x2A, 0x46
	};

}
