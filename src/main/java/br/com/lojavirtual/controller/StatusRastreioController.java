package br.com.lojavirtual.controller;

import br.com.lojavirtual.model.StatusRastreio;
import br.com.lojavirtual.repository.StatusRastreioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusRastreioController {

    private StatusRastreioRepository statusRastreioRepository;

    @ResponseBody
    @GetMapping(value = "listaRastreioVenda")
    public ResponseEntity<List<StatusRastreio>> listaRastreioVenda(@PathVariable ("idVenda") Long idVenda) {

        List<StatusRastreio> statusRastreios = statusRastreioRepository.listaRastreioVenda(idVenda);

        return new ResponseEntity<List<StatusRastreio>>(statusRastreios, HttpStatus.OK);
    }
}
