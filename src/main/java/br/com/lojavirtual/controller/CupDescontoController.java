package br.com.lojavirtual.controller;

import br.com.lojavirtual.model.CupDesc;
import br.com.lojavirtual.repository.CupDescontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CupDescontoController {

    @Autowired
    private CupDescontoRepository cupDescontoRepository;

    @ResponseBody
    @GetMapping(value = "**/listaCupDesc/{idEmpresa}")
    public ResponseEntity<List<CupDesc>> listaCupDesc(@PathVariable("idEmpresa") Long idEmpresa) {

        return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.cupDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/listaCupDesc")
    public ResponseEntity<List<CupDesc>> listaCupDesc() {

        return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.findAll(), HttpStatus.OK);
    }
}
