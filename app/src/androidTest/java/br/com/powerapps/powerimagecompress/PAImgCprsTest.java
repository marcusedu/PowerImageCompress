package br.com.powerapps.powerimagecompress;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PAImgCprsTest {
    private PAImgCprs compress;
    private File arquivoBase;

    @Before
    public void setUp() throws Exception {
        arquivoBase = new File("/sdcard/img.jpg");
        compress = PowerImageCompress.doArquivo(arquivoBase);
    }

    @Test
    public void deveLancarUmaExcepcaoNull() throws Exception {
        try {
            File nada = null;
            PowerImageCompress.doArquivo(nada);
            assertTrue(false);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deveLancarUmaExcepcaoIllegalArgument() throws Exception {
        try {
            PowerImageCompress.doArquivo("/storega/emulated/0/");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deveRedimencionarDistorcendoAProporção() throws Exception {
        Bitmap bitmap = compress.paraLarguraXAltura(800, 600)
                .taxaCompressao(65)
                .manterProporcao(false)
                .pegarBitmap();
        assertEquals(600, bitmap.getHeight());
        assertEquals(800, bitmap.getWidth());
    }

    @Test
    public void deveRedimencionarMantendoAProporção() throws Exception {
        Bitmap bitmap = compress.paraLarguraXAltura(800, 600)
                .taxaCompressao(65)
                .manterProporcao(true)
                .pegarBitmap();
        assertNotEquals(600, bitmap.getHeight());
        assertEquals(800, bitmap.getWidth());
    }

    @Test
    public void deveCopiarParaNovoArquivo() throws Exception {
        File novo = new File("/sdcard/imgNovo.jpg");
        File novoRecebido = compress
                .manterProporcao(false)
                .copiarPara(novo);
        assertTrue(novo.equals(novoRecebido));
    }

    @Test
    public void deveCopiarCorretamenteOArquivoParaODiretorioInformado() throws Exception {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/test/");
        File resultado = compress.copiarPara(file);
        assertEquals("img.jpg", resultado.getName());
    }

    @Test
    public void deveCopiarCorretamenteOArquivoParaNovoArquivo() throws Exception {
        File arquivoCopiado = compress.copiarPara(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/test/copiaImg.jpg");
        assertNotEquals(arquivoBase.getName(), arquivoCopiado.getName());
    }

    @Test
    public void deveMoverCorretamenteOArquivoParaODiretorioInformado() throws Exception {
        File movido = compress.moverPara(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/test/");
        assertTrue(movido.length() > 0);
        assertFalse(arquivoBase.exists());
        assertEquals(arquivoBase.getName(), movido.getName());
    }

    @Test
    public void deveMoverCorretamenteOArquivoParaNovoArquivo() throws Exception {
        File movido = compress.moverPara(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/test/movidoImg.jpg");
        assertTrue(movido.length() > 0);
        assertFalse(arquivoBase.exists());
        assertNotEquals(arquivoBase.getName(), movido.getName());
    }
}
