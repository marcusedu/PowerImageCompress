package br.com.powerapps.powerimagecompress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BAImgCprsTest {
    private PAImgCprs compressoArquivo;

    @Test
    public void redimencionaImagemParametrosPadrao() throws Exception {
        compressoArquivo.copiarPara("C:/androidTest/imagemPadrao.jpg");
    }

    @Before
    public void init() {
        compressoArquivo = PowerImageCompress.doArquivo("C:/androidTest/imagem.jpg");
    }
}