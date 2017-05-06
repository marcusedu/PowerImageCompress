# PowerImageCompress
Simples biblioteca para comprimir e redimensionar imagem no Android

Registro no repositorio Mavem em progresso.

## Recursos

  - Carregar imagem a partir de um `File` ou Caminho do arquivo.
  - Definir Altura e Comprimento desejado.
  - Ajustar a imagem para a resolução desejada.
  - Substituir a imagem do arquivo de origem.
  - Copiar ou mover a imagem para outro diretorio.
  - Passar um ImageView para exibir a imagem processada.
  
## Modo de Usar

Adicione a permissão no seu AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## Importe a Biblioteca

## Começe a usar!

### Salvando no arquivo
```java
File arquivo = PowerImageCompress.doArquivo("/caminho/do/arquivo.jpg")
                  .paraLarguraXAltura(800,600)
                  .manterProporcao(false)
                  .taxaCompressao(65)
                  //.moverPara("/caminho/do/novo/local")
                  //.copiarPara("/caminho/do/novo/local")
                  .substituir();
```

### Recuperando Bitmap

```java
Bitmap mBitmap = PowerImageCompress.doArquivo("/caminho/do/arquivo.jpg")
                  .paraLarguraXAltura(800,600)
                  .manterProporcao(false)
                  .taxaCompressao(65)
                  .pegarBitmap();
```

### Recuperando um array de bytes(`byte[]`) da imagem

```java
byte[] imagemEmBytes = PowerImageCompress.doArquivo("/caminho/do/arquivo.jpg")
                  .paraLarguraXAltura(800,600)
                  .manterProporcao(false)
                  .taxaCompressao(65)
                  .pegarBytes;
```

### Exibindo em um `ImageView`

```java
ImageView mImageView = PowerImageCompress.doArquivo("/caminho/do/arquivo.jpg")
                  .paraLarguraXAltura(800,600)
                  .manterProporcao(false)
                  .taxaCompressao(65)
                  .exibirEm(imageView);
```
