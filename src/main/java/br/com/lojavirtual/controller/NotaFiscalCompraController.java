package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.NotaFiscalCompra;
import br.com.lojavirtual.model.NotaFiscalVenda;
import br.com.lojavirtual.repository.NotaFiscalCompraRepository;
import br.com.lojavirtual.repository.NotaFiscalVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaFiscalCompraController {

    @Autowired
    private NotaFiscalCompraRepository notaFiscalCompraRepository;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @ResponseBody
    @PostMapping(value = "**/salvarNotaFiscalCompra")
    public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionLojaVirtual {

        if (notaFiscalCompra.getId() == null) {

            if (notaFiscalCompra.getDescricaoObs() != null) {
                boolean existe = notaFiscalCompraRepository.existeNotaComDescricao(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());

                if(existe) {
                    throw new ExceptionLojaVirtual("Já existe Nota de compra com essa mesma descrição : " + notaFiscalCompra.getDescricaoObs());
                }
            }


        }


        if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
            throw new ExceptionLojaVirtual("A pessoa juridica deve ser informada");
        }

        if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
            throw new ExceptionLojaVirtual("A empresa deve ser informada");
        }

        if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
            throw new ExceptionLojaVirtual("A conta a pagar deve ser informada");
        }

        NotaFiscalCompra fiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);

        return new ResponseEntity<NotaFiscalCompra>(fiscalCompraSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteNotaFiscalCompra/{id}")
    public ResponseEntity<?> deleteNotaFiscalCompra(@PathVariable("id") Long id) {

        notaFiscalCompraRepository.deleteNotaFiscalCompra(id);
        notaFiscalCompraRepository.deleteById(id);

        return new ResponseEntity("Nota fiscal de compra removida", HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "**/obterNotaFiscalCompra/{id}")
    public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if (notaFiscalCompra == null) {
            throw new ExceptionLojaVirtual("Não econtrou nota fiscal com o id");
        }

        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterNotaFiscalCompraDaVenda/{idvenda}")
    public ResponseEntity<List<NotaFiscalVenda>> obterNotaFiscalCompraDaVenda(@PathVariable("idvenda") Long idvenda) throws ExceptionLojaVirtual {

        List<NotaFiscalVenda> notaFiscalCompra = notaFiscalVendaRepository.buscaNotaPorVenda(idvenda);

        if (notaFiscalCompra == null) {
            throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal de venda com código da venda: " + idvenda);
        }

        return new ResponseEntity<List<NotaFiscalVenda>>(notaFiscalCompra, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterNotaFiscalCompraDaVendaUnico/{idvenda}")
    public ResponseEntity<NotaFiscalVenda> obterNotaFiscalCompraDaVendaUnico(@PathVariable("idvenda") Long idvenda) throws ExceptionLojaVirtual {

        NotaFiscalVenda notaFiscalVendas = notaFiscalVendaRepository.buscaNotaPorVendaUnica(idvenda);

        if (notaFiscalVendas == null) {
            throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal de venda com código da venda: " + idvenda);
        }

        return new ResponseEntity<NotaFiscalVenda>(notaFiscalVendas, HttpStatus.OK);
    }






    @ResponseBody
    @GetMapping(value = "**/buscaNotaFiscalPorDesc/{desc}")
    public ResponseEntity<List<NotaFiscalCompra>> buscaNotaFiscalPorDesc(@PathVariable("desc") String desc) {

        List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaDesc(desc.toUpperCase().trim());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);
    }
}
