package SkipJack;

import java.util.ArrayList;

public class Test
{
	public static void main (String [] args)
	{
		String input = "Edward11";
		String key = "oweinf41qbqk";
		System.out.println ("Original Text: " + input);
		SkipJack skipJack = new SkipJack(key.getBytes());
		byte[] cipher = skipJack.encrypt(input.getBytes());
		String encyptedText = new String(cipher);
		System.out.println ("Encypted Text: " + encyptedText);
		String decyptedText = new String(skipJack.decrypt(cipher));
		System.out.println ("Decypted Text: " + decyptedText);

		/*
        Хэш значение
        */
		byte[] hashArr = Hash4Mode.HashFunction(input.getBytes(), skipJack, "Any_text");
		System.out.println();
		String hash = new String(hashArr);

		String res = hash + input;
		byte[] resArr = res.getBytes();
		byte[] reshashArr = Hash4Mode.HashFunction(resArr, skipJack, "Any_text");

		String hashRes = new String(reshashArr);
		String HASH = hash + hashRes;
		System.out.println("Хэш значение: " + HASH);

		/*
        Цифровая подпись
        */
		DigitalSignature8 signature = new DigitalSignature8();
		signature.Sign(input, HASH.getBytes());


        /*
        Режим шифрования Counter
        */
		String text = "ThisTextThisTextThisTextThisTextThisTextThisTextThisText";
		String S0 = "56789123";
		byte[] sB = text.getBytes();
		String Cip = "";
		ArrayList<byte[]> P = Hash4Mode.toBlocks(sB);
		ArrayList<byte[]> Y = new ArrayList<>();
		ArrayList<byte[]> C = new ArrayList<>();

		int g = 0;
		for (int j = 0; j < P.size(); j++) {
			if (j == 0) {
				g = Integer.parseInt(S0);
			}
			g = g + 1;
			S0 += String.valueOf(g);
		}
		System.out.println("S0: " + S0);
		ArrayList<byte[]> S = Hash4Mode.toBlocks(S0.getBytes());
		for (int i = 0; i < P.size(); i++) {
			byte[] t = P.get(i);
			byte[] current = skipJack.encrypt(t);
			Cip += new String(current);
			C.add(current);
		}
		System.out.println("До использования режима: \n" + Cip);
		ArrayList<byte[]> CopyP = new ArrayList<>(P.size());
		for (int i = 0; i < P.size(); i++) {
			byte[] t = C.get(i);
			byte[] current = skipJack.decrypt(t);
			CopyP.add(current);
		}
		System.out.println("После применения режима шифрования: ");
		for (int i = 0; i < P.size(); i++) {
			Y.add(i, skipJack.encrypt(S.get(i)));
			SkipJack skip = new SkipJack(Y.get(i));
			C.set(i, Hash4Mode.xor(P.get(i), Y.get(i)));
			System.out.print(new String(C.get(i)));
			P.set(i, Hash4Mode.xor(C.get(i), Y.get(i)));
		}
		System.out.println("\nПосле расшифрования:");
		for (int i = 0; i < P.size(); i++) {
			String p = new String(CopyP.get(i));
			System.out.print(p);
		}

	}
}
