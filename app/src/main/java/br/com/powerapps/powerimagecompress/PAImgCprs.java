package br.com.powerapps.powerimagecompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PAImgCprs {
    private File imagemAComprimir;
    private String TAG = "PAImgCprs";
    private int largura = 1280, altura = 780, taxaCompressao = 65;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private boolean redimensionar = false;
    private boolean proporcao = true;

    PAImgCprs(@NotNull String caminhoDaImagem) {
        File arquivo = new File(caminhoDaImagem);
        this.imagemAComprimir = Goiaba.checaSeArquivoValido(arquivo);
    }


    PAImgCprs(@NotNull File arquivo) {
        this.imagemAComprimir = Goiaba.checaSeArquivoValido(arquivo);
    }

    /**
     * Define a altura maxima em pixels que a imagem irá ter.
     * Valor padrão 780
     *
     * @param altura desejada para imagem final
     * @return Builder
     */
    public PAImgCprs paraAltura(@NotNull int altura) {
        this.altura = altura;
        return this;
    }

    /**
     * Define a largura maxima em pixels que a imagem irá ter.
     *
     * @param largura desejada para a imagem final
     * @return Builder
     */
    public PAImgCprs paraLargura(@NotNull int largura) {
        this.largura = largura;
        return this;
    }

    /**
     * Define a largura e altura maxima em pixels que a imagem irá ter.
     *
     * @param largura desejada para a imagem final, valor padrão 780
     * @param altura  altura desejada para imagem final, valor padrão 1280
     * @return Builder
     */
    public PAImgCprs paraLarguraXAltura(@NotNull int largura, @NotNull int altura) {
        this.largura = largura;
        this.altura = altura;
        return this;
    }

    /**
     * Define se a imagem será redimensionada ou somente comprimida
     *
     * @param redimensionar true para não redimensionar, valor padrão false
     * @return Builder
     */
    public PAImgCprs manterResolucao(@NotNull boolean redimensionar) {
        this.redimensionar = redimensionar;
        return this;
    }

    /**
     * Define se deverá manter a proporção da imagem redimensionada ou se deverá distorcer a imagem
     * para os valores de altura x largura definidos.
     *
     * @param proporcao false para distorcer, valor padrão true.
     * @return Buildes
     */
    public PAImgCprs manterProporcao(@NotNull boolean proporcao) {
        this.proporcao = proporcao;
        return this;
    }

    /**
     * Define a taxa de compressão do algoritmo de compressão
     * quanto mais proxima de 0 menor ficará a imagem e a imagem terá baixa qualidade
     * quanto mais proxima de 100 maior será a imagem e sua terá boa qualidade.
     *
     * @param taxaCompressao valor de 0 a 100, valor padrão 65
     * @return Builder
     */
    public PAImgCprs taxaCompressao(@NotNull int taxaCompressao) {
        if (taxaCompressao < 0 || taxaCompressao > 100)
            throw new IllegalArgumentException("Taxa da compressão deve estar entre 0 e 100");
        this.taxaCompressao = taxaCompressao;
        return this;
    }

    /**
     * Converte a imagem para um array de byte
     *
     * @return Array de bytes da imagem
     */
    public byte[] pegarBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Matrix matrix = new Matrix();
        float scala = 1;
        Bitmap novoBmp;
        Bitmap bmpOriginal = BitmapFactory.decodeFile(imagemAComprimir.getAbsolutePath());

        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        if (w > largura) {
            scala = largura / w;
        } else if (h > altura) {
            scala = altura / h;
        }
        if (!proporcao)
            matrix.postScale(largura / w, altura / h);
        else
            matrix.postScale(scala, scala);

        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);
        novoBmp.compress(compressFormat, taxaCompressao, stream);
        return stream.toByteArray();
    }

    /**
     * Converte a imagem para um Bitmap
     *
     * @return Bitmap da imagem
     */
    public Bitmap pegarBitmap() {
        byte[] bytes = pegarBytes();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Substitui a imagem de origem pela nova imagem.
     * OBS.: Não pode ser desfeito.
     *
     * @return O arquivo da imagem convertida.
     */
    public File substituir() {
        return salvarNoArquivo(imagemAComprimir, false);
    }

    /**
     * Define uma pasta ou arquivo destino para a imagem convertida e exclui o arquivo origem.
     *
     * @param caminho do diretorio ou arquivo do destino.
     * @return File do arquivo convertido.
     */
    public File moverPara(@NotNull String caminho) {
        return moverPara(new File(caminho));
    }

    /**
     * Define uma pasta ou arquivo destino para a imagem convertida e exclui o arquivo origem.
     *
     * @param arquivo do diretorio ou arquivo do destino.
     * @return File do arquivo convertido.
     */
    public File moverPara(@NotNull File arquivo) {
        return salvarNoArquivo(arquivo, true);
    }

    /**
     * Cria uma copia da imagem comprimida no caminho definido
     *
     * @param caminho do Diretorio ou Arquivo
     * @return File do arquivo convertido.
     */
    public File copiarPara(@NotNull String caminho) {
        return copiarPara(new File(caminho));
    }

    /**
     * Cria uma copia da imagem comprimida no caminho definido
     *
     * @param arquivo do diretorio ou arquivo.
     * @return File do arquivo convertido.
     */
    public File copiarPara(@NotNull File arquivo) {
        return salvarNoArquivo(arquivo, false);
    }

    /**
     * Exibe a imagem no ImageView.
     *
     * @param imageView ImageView que irá exibir a imagem;
     * @return O mesmo ImageView passado no argumento.
     */
    public ImageView exibirEm(@NotNull ImageView imageView) {
        Goiaba.checaNull(imageView, "ImageView não pode ser nullo");
        imageView.setImageBitmap(pegarBitmap());
        return imageView;
    }

    private File salvarNoArquivo(File aquivoDestino, boolean excluirOrigen) {
        Goiaba.checaSeArquivoValido(aquivoDestino);
        if (aquivoDestino.isDirectory()) {
            aquivoDestino = new File(aquivoDestino.getPath() + imagemAComprimir.getName());
        }
        byte[] ibagem = pegarBytes();
        FileOutputStream escreverImagem = null;
        try {
            escreverImagem = new FileOutputStream(aquivoDestino);
            escreverImagem.write(ibagem);
            escreverImagem.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "comprimirESubstituir: Não foi possivel localizar o arquivo", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "comprimirESubstituir: Não foi possivel substituir o arquivo", e);
        } finally {
            if (escreverImagem != null) try {
                escreverImagem.close();
                if (excluirOrigen) imagemAComprimir.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aquivoDestino;
    }
}
