package br.com.powerapps.powerimagecompress;

import java.io.File;

class Goiaba {

    static File checaSeArquivoValido(File arquivo) {
        if (arquivo == null) throw new NullPointerException("Arquivo não pode ser nulo");
        else if (!arquivo.exists())
            throw new IllegalArgumentException("Arquivo não existe no armazenamento");
        else if (arquivo.length() == 0)
            throw new IllegalArgumentException("Arquivo inválido");
        else return arquivo;
    }

    static Object checaNull(Object objeto, String msg) {
        if (objeto == null) throw new NullPointerException(msg);
        return objeto;
    }
}
