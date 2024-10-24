package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import br.com.lojavirtual.util.ValidaCPF;
import br.com.lojavirtual.util.ValidaCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class PessoaController {

    @Autowired
    private PessoaRepository pesssoaRepository;

    @Autowired
    private PessoaUserService pessoaUserService;

    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "**/salvarPj")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {

        if (pessoaJuridica == null) {
            throw new ExceptionLojaVirtual("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaJuridica.getId() == null && pesssoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionLojaVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }

        if (pessoaJuridica.getId() == null && pesssoaRepository.existeinscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
            throw new ExceptionLojaVirtual("Já existe Inscricao estadual cadastrado com o número: " + pessoaJuridica.getInscEstadual());
        }

        if (!ValidaCnpj.isCNPJ(pessoaJuridica.getCnpj())){
            throw new ExceptionLojaVirtual("Cnpj : " + pessoaJuridica.getCnpj() + " está inválido.");
        }

        pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping(value = "**/salvarPf")
    public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLojaVirtual {

        if (pessoaFisica == null) {
            throw new ExceptionLojaVirtual("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaFisica.getId() == null && pesssoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
            throw new ExceptionLojaVirtual("Já existe um CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }

        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())){
            throw new ExceptionLojaVirtual("Cpf : " + pessoaFisica.getCpf() + " está inválido.");
        }

        pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);

    }
}
