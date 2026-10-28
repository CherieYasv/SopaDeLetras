package com.example.modelo;

public class SopaDeLetras extends Sopa {

    @Override
    protected String[] separar(String linea) {
        return linea.replaceAll("\\s+", "")
                .toUpperCase()
                .chars()
                .mapToObj(ch -> String.valueOf((char) ch))
                .toArray(String[]::new);
    }

    @Override
    protected boolean esElementoValido(String elemento) {
        return elemento.length() == 1 && Character.isLetter(elemento.charAt(0));
    }
}
