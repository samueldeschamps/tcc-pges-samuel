package com.generator.core.res.input;

public class ValidaCpfCnpj {

	private static final int[] PESOS_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] PESOS_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
	public static final int TIPO_CPF = 0;
	public static final int TIPO_CNPJ = 1;

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		Integer digito1 = calcularDigito(cpf.substring(0, 9), PESOS_CPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, PESOS_CPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean isValidCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14))
			return false;

		Integer digito1 = calcularDigito(cnpj.substring(0, 12), PESOS_CNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, PESOS_CNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
	}
	
	public static boolean isValidCPForCNPJ(String cpfCnpj, int tipo) throws IllegalArgumentException {
		switch (tipo) {
			case TIPO_CPF:
				return isValidCPF(cpfCnpj);
			case TIPO_CNPJ:
				return isValidCNPJ(cpfCnpj);
			default:
				throw new IllegalArgumentException("Tipo precisa ser 0 ou 1!");
		}
	}

}