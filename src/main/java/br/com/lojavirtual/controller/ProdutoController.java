package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.Produto;
import br.com.lojavirtual.repository.ProdutoRepository;
import br.com.lojavirtual.service.ServiceSendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @ResponseBody
    @PostMapping(value = "salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual, MessagingException, IOException {

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionLojaVirtual("Empresa responsavel deve ser informada");
        }

        if (produto.getId() == null) {
            List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (!produtos.isEmpty()) {
                throw new ExceptionLojaVirtual("Já existe produto com a descrição: " + produto.getDescricao());
            }
        }

        if (produto.getNome().length() < 9) {
            throw new ExceptionLojaVirtual("Nome do produto deve ter mais de 10 letras");
        }

        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionLojaVirtual("Categoria do produto deve ser informado");
        }

        if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionLojaVirtual("Marca do produto deve ser informado");
        }
        if (produto.getQtdEstoque() < 1) {
            throw new ExceptionLojaVirtual("O produto deve ter no minimo 1 no estoque");
        }

        if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
            throw new ExceptionLojaVirtual("Deve ser informado imagens para o produto");
        }

        if (produto.getImagens().size() < 3) {
            throw new ExceptionLojaVirtual("Deve ser informado pelo menos 3 imagens para o produto");
        }

        if (produto.getImagens().size() > 6) {
            throw new ExceptionLojaVirtual("Deve ser informado no maximo 6 imagens para o produto");
        }

        if (produto.getId() == null) {

            for (int x = 0; x < produto.getImagens().size(); x++) {
                produto.getImagens().get(x).setProduto(produto);
                produto.getImagens().get(x).setEmpresa(produto.getEmpresa());

                String base64Image = "";

                if (produto.getImagens().get(x).getImagemOriginal().contains("dados:imagem")) {
                    base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
                }else {
                    base64Image = produto.getImagens().get(x).getImagemOriginal();
                }

                byte[] imageBytes =  DatatypeConverter.parseBase64Binary(base64Image);

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                if (bufferedImage != null) {

                    int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
                    int largura = Integer.parseInt("800");
                    int altura = Integer.parseInt("600");

                    BufferedImage resizedImage = new BufferedImage(largura, altura, type);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, largura, altura, null);
                    g.dispose();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "jpeg", baos);

                    String miniImgBase64 = "dados:imagem/jpeg;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());

                    produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);

                    bufferedImage.flush();
                    resizedImage.flush();
                    baos.flush();
                    baos.close();

                }
            }
        }


        Produto produtoSalvo = produtoRepository.save(produto);

        if (produto.getAlertaQtdeEstoque() && produto.getQtdEstoque() <= 1) {
            StringBuilder html = new StringBuilder();
            html.append("<h2>")
                    .append("Produto: " + produto.getNome())
                    .append(" com estoque baixo: " + produto.getQtdEstoque());
            html.append("<p> Id Prod.:").append(produto.getId()).append("</p>");

            if (produto.getEmpresa().getEmail() != null) {
                serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
            }
        }

        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcesso(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteProdutoPorId/{id}")
    public ResponseEntity<String> deleteAcessoPorId(@PathVariable("id") Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity<String>("produto Removido", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterProduto/{id}")
    public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual {


        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            throw new ExceptionLojaVirtual("Não encontrou o Produto com o código: " + id);
        }

        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/buscarProdNome/{desc}")
    public ResponseEntity<List<Produto>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase());

        return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
    }
}