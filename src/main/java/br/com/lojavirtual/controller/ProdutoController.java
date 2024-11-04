package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.Produto;
import br.com.lojavirtual.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @ResponseBody
    @PostMapping(value = "salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual {

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionLojaVirtual("Empresa responsavel deve ser informada");
        }

        if (produto.getId() == null) {
            List<Produto> produtos  = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (!produtos.isEmpty()) {
                throw new ExceptionLojaVirtual("Já existe produto com a descrição: " + produto.getDescricao());
            }
        }


        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionLojaVirtual("Categoria do produto deve ser informado");
        }

        if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionLojaVirtual("Marca do produto deve ser informado");
        }


        produtoRepository.save(produto);

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