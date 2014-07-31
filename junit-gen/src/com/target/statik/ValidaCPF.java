package com.target.statik;

public class ValidaCPF {

	public static boolean isCpfValido(String cpf) {
		char dig10, dig11;
		int sm, i, r, num, peso;
		if (cpf.length() != 11) {
			return false;
		}
		sm = 0;
		peso = 10;
		for (i = 0; i < 9; i++) {
			num = (int) (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}
		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11)) {
			dig10 = '0';
		} else {
			dig10 = (char) (r + 48);
		}
		sm = 0;
		peso = 11;
		for (i = 0; i < 10; i++) {
			num = (int) (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}
		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11)) {
			dig11 = '0';
		} else {
			dig11 = (char) (r + 48);
		}
		return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
	}

	public static String formatarCPF(String cpf) throws StringIndexOutOfBoundsException {
		StringBuilder sb = new StringBuilder();
		sb.append(cpf.substring(0, 3));
		sb.append(".");
		sb.append(cpf.substring(3, 6));
		sb.append(".");
		sb.append(cpf.substring(6, 9));
		sb.append("-");
		sb.append(cpf.substring(9, 11));
		return sb.toString();
	}
	
}
