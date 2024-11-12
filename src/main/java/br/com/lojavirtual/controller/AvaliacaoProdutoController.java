package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.AvaliacaoProduto;
import br.com.lojavirtual.repository.AvaliacaoProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AvaliacaoProdutoController {

    @Autowired
    private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

    @ResponseBody
    @PostMapping(value = "**/salvaAvaliacaoProduto")
    public ResponseEntity<AvaliacaoProduto> salvaAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionLojaVirtual {

        if (avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)) {
            throw new ExceptionLojaVirtual("Informe a empresa dona do registro");
        }

        if (avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)) {
            throw new ExceptionLojaVirtual("A avaliacao deve conter o produto associado ");
        }

        if (avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)) {
            throw new ExceptionLojaVirtual("A avaliacao deve conter uma pessoa ou cliente associado ");
        }

        avaliacaoProduto = avaliacaoProdutoRepository.save(avaliacaoProduto);

        return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteAvaliacaoPessoa/{idAvaliacao}")
    public ResponseEntity<?> deleteAvaliacaoPessoa(@PathVariable("id") Long idAvaliacao) {
        avaliacaoProdutoRepository.deleteById(idAvaliacao);

        return new ResponseEntity("Acesso removido", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/avaliacaoProduto/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoProduto(@PathVariable("idProduto") Long idProduto) {
        List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProduto(idProduto);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/avaliacaoPessoa/{idPessoa}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoPessoa(@PathVariable("idPessoa")  Long idPessoa) {

        List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "**/avaliacaoProdutoPessoa/{idProduto}/{idPessoa}")
    public ResponseEntity<List<AvaliacaoProduto>> avaliacaoProdutoPessoa(@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) {

        List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idProduto, idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);

    }
}