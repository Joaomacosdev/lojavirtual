package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.NotaItemProduto;
import br.com.lojavirtual.repository.NotaItemProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaItemProdutoController {

    @Autowired
    private NotaItemProdutoRepository notaItemProdutoRepository;

    @Transactional
    @PostMapping(value = "**/salvarNotaItemProduto")
    public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionLojaVirtual {

        if (notaItemProduto.getId() == null){

            if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId()  <= 0) {
                throw new ExceptionLojaVirtual("O produto deve ser informado.");

            }

            if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId()  <= 0) {
                throw new ExceptionLojaVirtual("O produto deve ser informado.");
            }

            List<NotaItemProduto> notaExistente = notaItemProdutoRepository.
                    buscaNotaItemProdutoPorProdutoNota(notaItemProduto.getProduto().getId(),
                            notaItemProduto.getNotaFiscalCompra().getId());
            if (!notaExistente.isEmpty()){
                throw new ExceptionLojaVirtual("Já existe esse produto cadastrado para essa nota");
            }
        }

        if (notaItemProduto.getQuantidade() <= 0) {
            throw new ExceptionLojaVirtual("A quantidade deve ser maior que zero.");

        }

        NotaItemProduto notaItemProdutoSalvo = notaItemProdutoRepository.save(notaItemProduto);

        notaItemProdutoSalvo = notaItemProdutoRepository.findById(notaItemProduto.getId()).get();

        return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalvo, HttpStatus.OK);

    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteNotaFiscalItem/{id}")
    public ResponseEntity<?> deleteNotaFiscalItem(@PathVariable("id") Long id) {

        notaItemProdutoRepository.deleteByIdNotaItem(id);

        return new ResponseEntity("Nota item produto removido", HttpStatus.OK);
    }
}

