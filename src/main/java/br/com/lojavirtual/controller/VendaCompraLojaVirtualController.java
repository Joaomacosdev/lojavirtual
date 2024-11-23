package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.dto.ItemVendaLojaDTO;
import br.com.lojavirtual.dto.VendaCompraLojaVirtualDTO;
import br.com.lojavirtual.model.*;
import br.com.lojavirtual.repository.EnderecoRepository;
import br.com.lojavirtual.repository.NotaFiscalVendaRepository;
import br.com.lojavirtual.repository.StatusRastreioRepository;
import br.com.lojavirtual.repository.VendaCompraLojaVirtualReposiory;
import br.com.lojavirtual.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VendaCompraLojaVirtualController {

    @Autowired
    private VendaCompraLojaVirtualReposiory vendaCompraLojaVirtualReposiory;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaController pessoaController;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @Autowired
    private VendaService vendaService;

    @ResponseBody
    @PostMapping(value = "**/salvarVendaLoja")
    public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {


        vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
        vendaCompraLojaVirtual.setPessoa(pessoaFisica);


        vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);


        vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

        vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

        for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
            vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        }


        /*Salva primeiro a venda e todo os dados*/
        vendaCompraLojaVirtual = vendaCompraLojaVirtualReposiory.saveAndFlush(vendaCompraLojaVirtual);

        StatusRastreio statusRastreio = new StatusRastreio();
        statusRastreio.setCentroDistribuicao("Loja local");
        statusRastreio.setCidade("Local");
        statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        statusRastreio.setEstado("Local");
        statusRastreio.setStatus("Inicio de compra");
        statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        statusRastreioRepository.save(statusRastreio);

        /*Associa a venda gravada no banco com a nota fiscal*/
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        /*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda*/
        notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());


        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
        compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());

        compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());

        compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto());
        compraLojaVirtualDTO.setValorFret(vendaCompraLojaVirtual.getValorFret());
        compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

        for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
            ItemVendaLojaDTO itemVendaDTO = new ItemVendaLojaDTO();
            itemVendaDTO.setQuantidade(item.getQuantidade());
            itemVendaDTO.setProduto(item.getProduto());

            compraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
        }


        return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/consultaVendaId/{id}")
    public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("id") Long idVenda) {

        VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualReposiory.findById(idVenda).orElse(new VendaCompraLojaVirtual());

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new VendaCompraLojaVirtual();

        }

        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
        compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
        compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());

        compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());
        compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());

        compraLojaVirtualDTO.setValorDesc(compraLojaVirtual.getValorDesconto());
        compraLojaVirtualDTO.setValorFret(compraLojaVirtual.getValorFret());
        compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

        for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {
            ItemVendaLojaDTO itemVendaDTO = new ItemVendaLojaDTO();
            itemVendaDTO.setQuantidade(item.getQuantidade());
            itemVendaDTO.setProduto(item.getProduto());

            compraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
        }

        return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
    }




    @ResponseBody
    @DeleteMapping(value = "**/deleteVendaTotalBanco/{idVenda}")
    public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable(value = "idVenda") Long idVenda) {

        vendaService.exclusaoTotalVendaBanco(idVenda);

        return new ResponseEntity<String>("Venda excluida com sucesso", HttpStatus.OK);

    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteVendaTotalBanco2/{idVenda}")
    public ResponseEntity<String> deleteVendaTotalBanco2(@PathVariable(value = "idVenda") Long idVenda) {
        vendaService.exclusaoTotalVendaBanco2(idVenda);

        return new ResponseEntity<String>("Venda excluida logicamente com sucesso!", HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping(value = "**/ativaRegistroBanco/{idVenda}")
    public ResponseEntity<String> ativaRegistroBanco(@PathVariable(value = "idVenda") Long idVenda) {
        vendaService.ativaRegistroVendaBanco(idVenda);

        return new ResponseEntity<String>("Venda ativada com sucesso!", HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/consultaVendaDinamica/{valor}/{tipoconsulta}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>>
    consultaVendaDinamica(@PathVariable("valor") String valor,
                          @PathVariable("tipoconsulta") String tipoconsulta) {


        List<VendaCompraLojaVirtual> compraLojaVirtual = null;

        if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {

            compraLojaVirtual =   vendaCompraLojaVirtualReposiory.vendaPorProduto(Long.parseLong(valor));

        }else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
            compraLojaVirtual = vendaCompraLojaVirtualReposiory.vendaPorNomeProduto(valor.toUpperCase().trim());
        }
        else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
            compraLojaVirtual = vendaCompraLojaVirtualReposiory.vendaPorNomeCliente(valor.toUpperCase().trim());
        }
        else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
            compraLojaVirtual = vendaCompraLojaVirtualReposiory.vendaPorEndereCobranca(valor.toUpperCase().trim());
        }
        else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
            compraLojaVirtual = vendaCompraLojaVirtualReposiory.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
        }

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {

            VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            compraLojaVirtualDTO.setPessoa(vcl.getPessoa());

            compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
            compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());

            compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
            compraLojaVirtualDTO.setValorFret(vcl.getValorFret());
            compraLojaVirtualDTO.setId(vcl.getId());


            for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

                ItemVendaLojaDTO itemVendaDTO = new ItemVendaLojaDTO();
                itemVendaDTO.setQuantidade(item.getQuantidade());
                itemVendaDTO.setProduto(item.getProduto());

                compraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(compraLojaVirtualDTO);

        }


        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
    }



    @ResponseBody
    @GetMapping(value = "**/consultaVendaPorProdutoId/{id}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProduto(@PathVariable("id") Long idProd) {


        List<VendaCompraLojaVirtual> compraLojaVirtual = vendaCompraLojaVirtualReposiory.vendaPorProduto(idProd);

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {

            VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            compraLojaVirtualDTO.setPessoa(vcl.getPessoa());

            compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
            compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());

            compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
            compraLojaVirtualDTO.setValorFret(vcl.getValorFret());
            compraLojaVirtualDTO.setId(vcl.getId());


            for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

                ItemVendaLojaDTO itemVendaDTO = new ItemVendaLojaDTO();
                itemVendaDTO.setQuantidade(item.getQuantidade());
                itemVendaDTO.setProduto(item.getProduto());

                compraLojaVirtualDTO.getItemVendaLojas().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(compraLojaVirtualDTO);

        }


        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
    }


}
