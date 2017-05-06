package br.com.powerapps.powerimagecompress;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PowerImageCompress {

    public static PAImgCprs doArquivo(@NotNull String caminho) {
        return new PAImgCprs(caminho);
    }

    public static PAImgCprs doArquivo(@NotNull File arquivo) {
        return new PAImgCprs(arquivo);
    }
}
