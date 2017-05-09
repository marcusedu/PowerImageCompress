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
    private float largura = 1280, altura = 780;
    private int taxaCompressao = 65;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private boolean manterResolucao = false;
    private boolean manterProporcao = true;

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
    public PAImgCprs paraAltura(int altura) {
        this.altura = altura;
        return this;
    }

    /**
     * Define a largura maxima em pixels que a imagem irá ter.
     *
     * @param largura desejada para a imagem final
     * @return Builder
     */
    public PAImgCprs paraLargura(int largura) {
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
    public PAImgCprs paraLarguraXAltura(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        return this;
    }

    /**
     * Define se a imagem será redimensionada ou somente comprimida
     *
     * @param redimensionar true para não manterResolucao, valor padrão false
     * @return Builder
     */
    public PAImgCprs manterResolucao(boolean redimensionar) {
        this.manterResolucao = redimensionar;
        return this;
    }

    /**
     * Define se deverá manter a proporção da imagem redimensionada ou se deverá distorcer a imagem
     * para os valores de altura x largura definidos.
     *
     * @param proporcao false para distorcer, valor padrão true.
     * @return Buildes
     */
    public PAImgCprs manterProporcao(boolean proporcao) {
        this.manterProporcao = proporcao;
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
    public PAImgCprs taxaCompressao(int taxaCompressao) {
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
        float scalaLagura = 1;
        float scalaAltura = 1;
        Bitmap novoBmp;
        Bitmap bmpOriginal = BitmapFactory.decodeFile(imagemAComprimir.getAbsolutePath());

        int larguraAntiga = bmpOriginal.getWidth();
        int alturaAntiga = bmpOriginal.getHeight();

        if (!manterResolucao) {
            if (manterProporcao) {
                if (larguraAntiga > largura) {
                    scalaAltura = scalaLagura = largura / larguraAntiga;
                } else if (alturaAntiga > altura) {
                    scalaAltura = scalaLagura = altura / alturaAntiga;
                }
            } else {
                scalaLagura = largura / larguraAntiga;
                scalaAltura = altura / alturaAntiga;
            }
        }

        matrix.postScale(scalaLagura, scalaAltura);

        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, larguraAntiga, alturaAntiga, matrix, true);
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
        return salvarNoArquivo(imagemAComprimir, true);
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

    /**
     * Aqui você define qual será o formato de compressão se PNG, JPEG ou WEBP.
     *
     * @param formato Formato de comprensão de imagens, padrão JPEG
     * @return Builder
     */
    public PAImgCprs comprimirNoFormato(Bitmap.CompressFormat formato) {
        this.compressFormat = formato;
        return this;
    }

    private File salvarNoArquivo(File aquivoDestino, boolean excluirOrigen) {
        try {
            Goiaba.checaSeArquivoValido(aquivoDestino);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "salvarNoArquivo: Arquivo não existe e será criado", e);

        }
        if (aquivoDestino.isDirectory()) {
            aquivoDestino = new File(aquivoDestino.getPath(), imagemAComprimir.getName());
        }

        aquivoDestino = new File(aquivoDestino
                .getAbsolutePath()
                .replaceAll("(\\.[\\w\\d]+$)"
                        , "." + compressFormat.name()
                                .toLowerCase()));
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
                if (excluirOrigen && !imagemAComprimir.equals(aquivoDestino))
                    imagemAComprimir.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aquivoDestino;
    }
}
