package com.generator.core.res.input;

public class IntOperationsDefault {

	public static int gcd(int p, int q) {
		while (q != 0) {
			int temp = q;
			q = p % q;
			p = temp;
		}
		return p;
	}

	// public static int factorial(int value) throws Exception {
	// if (value < 0) {
	// throw new Exception("value is smaller than 0");
	// }
	// int fat = 1;
	// for (int i = 2; i <= value; i++) {
	// fat = fat * i;
	// }
	// return fat;
	// }

	public static float blah(int x, int y) {
		int pow;
		if (y < 0) {
			pow = -y;
		} else {
			pow = y;
		}

		int z = 1;
		while (pow != 0) {
			z = z * x;
			pow = pow - 1;
		}
		float answer = z;
		if (y < 0) {
			answer = 1 / z;
		}
		answer = answer + 1;
		return answer;
	}

	public static String avaliaTriangulo(int a, int b, int c) {
		if (a < b + c && b < a + c && c < a + b) {
			if (a > 0 && b > 0 && c > 0) {
				if (a == b && b == c && c == a) {
					return "Equiláteo";
				} else if (a != b && b != c && c != a) {
					return "Escaleno";
				} else {
					return "Isósceles";
				}
			}
		}
		return "Não é triângulo";
	}

	// Para ter um teste com switch
	public static String avaliaFaixaEtaria(int idade) {
		if (idade < 0) {
			return null;
		}
		switch (idade) {
			case 0:
				return "Recém nascido.";
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				return "Criança";
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				return "Adolescente";
			default:
				if (idade < 60) {
					return "Adulto";
				} else {
					return "Idoso";
				}
		}
	}

}
