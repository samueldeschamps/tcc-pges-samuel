package com.generator.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.target.statik.ValidaCPF;

/*
 * TODO Algumas estratégias que podem ser utilizadas:
 * 
 * - Gerar as strings "" e null;
 * 
 * - Gerar strings aleatórias, dado um comprimento limite;
 *   >> Este comprimento limite poderia ser descoberto analisando o código do método;
 *   >> Se não encontrar o comprimento limite, vai tentando comprimentos de 0 até N;
 *   >> Gerar classes de strings (Apenas dígitos, Apenas letras, Dígitos + Letras, Dígitos + Letras + Especiais, ...);
 *   
 * - Criar heurísticas para definir qual classe de strings a ser gerada de acordo com algumas informações do contexto:
 *   >> Ex: Se o parâmetro contém "nome"/"name": Apenas letras, contendo ou não espaços;
 *   >> Ex: Se o parâmetros contém "numero"/"number"/"num"/"nr": Apenas dígitos;
 *
 */

public class StringCombinator {

//	private static final String CHARS = "abc.";
	private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .,:;=-";
	private static final String DIGIT_CHARS = "0123456789";

	public static void main(String[] args) throws ReflectiveOperationException {
//		generateStrings();
		tryValidCfp();
	}

	private static void tryValidCfp() {
		
		int countValid = 0;
		int limit = 1000;
		for (int i = 0; i < limit; ++i) {
			String str = randomDigitString(11);
			boolean valido = ValidaCPF.isCpfValido(str);
			if (valido) {
				countValid++;
				System.out.println(str);
			}
//			System.out.println(str + " >> " + valido);
		}
		System.out.println("Generated values: " + limit);
		System.out.println("Valid: " + countValid);
		
	}
	
	private static Random random = new Random(System.currentTimeMillis());
	
	private static String randomDigitString(int length) {
		int charCount = DIGIT_CHARS.length();
		char[] result = new char[length];
		for (int i = 0; i < length; ++i) {
			result[i] = DIGIT_CHARS.charAt(random.nextInt(charCount));
		}
		return String.valueOf(result);
	}
	
	private static String randomString(int length) {
		int charCount = CHARS.length();
		char[] result = new char[length];
		for (int i = 0; i < length; ++i) {
			result[i] = CHARS.charAt(random.nextInt(charCount));
		}
		return String.valueOf(result);
	}

	private static void generateStrings() {

		int maxLength = 8;
		List<String> chars = charsToList(CHARS);
		List<List<String>> categories = new ArrayList<>();
		
		long before = System.currentTimeMillis();
		
		for (int len = 1; len <= maxLength; len++) {
			categories.add(chars);
			List<List<String>> results = CombinationGenerator.combine(categories);
			for (List<String> res : results) {
				String str = listToChars(res);
			}
			System.out.println(len + " - done!");
		}

		long after = System.currentTimeMillis();
		System.out.println("Took " + (after - before) / 1000.0 + " seconds.");
	}
	
	private static String listToChars(List<String> list) {
		StringBuilder sb = new StringBuilder(list.size());
		for (String s : list) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	private static List<String> charsToList(String chars) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < chars.length(); ++i) {
			result.add(String.valueOf(chars.charAt(i)));
		}
		return result;
	}
}
