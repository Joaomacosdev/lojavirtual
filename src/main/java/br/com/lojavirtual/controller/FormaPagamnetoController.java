package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.FormaPagamento;
import br.com.lojavirtual.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FormaPagamnetoController {


    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;


    @ResponseBody
    @PostMapping(value = "**/salvarFormaPagamento")
    public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento)
            throws ExceptionLojaVirtual {


        formaPagamento = formaPagamentoRepository.save(formaPagamento);

        return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/listaFormaPagamento/{idEmpresa}")
    public ResponseEntity<List<FormaPagamento>> listaFormaPagamentoidEmpresa(@PathVariable(value = "idEmpresa") Long idEmpresa){

        return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(idEmpresa), HttpStatus.OK);

    }


    @ResponseBody
    @GetMapping(value = "**/listaFormaPagamento")
    public ResponseEntity<List<FormaPagamento>> listaFormaPagamento(){

        return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(), HttpStatus.OK);

    }


}
