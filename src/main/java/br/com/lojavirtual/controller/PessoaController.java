package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.dto.CepDto;
import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.EnderecoRepository;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import br.com.lojavirtual.util.ValidaCPF;
import br.com.lojavirtual.util.ValidaCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RestController
public class PessoaController {

    @Autowired
    private PessoaRepository pesssoaRepository;

    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @ResponseBody
    @GetMapping(value = "**/consultaCep/{cep}")
    public ResponseEntity<CepDto> consultaCep(@PathVariable("cep") String cep) {
        return new ResponseEntity<CepDto>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
    }

    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "**/salvarPj")
    public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {

        if (pessoaJuridica == null) {
            throw new ExceptionLojaVirtual("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaJuridica.getId() == null && pesssoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionLojaVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }

        if (pessoaJuridica.getId() == null && pesssoaRepository.existeinscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
            throw new ExceptionLojaVirtual("Já existe Inscricao estadual cadastrado com o número: " + pessoaJuridica.getInscEstadual());
        }

        if (!ValidaCnpj.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new ExceptionLojaVirtual("Cnpj : " + pessoaJuridica.getCnpj() + " está inválido.");
        }

        if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                CepDto cepDto = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
                pessoaJuridica.getEnderecos().get(p).setBairro(cepDto.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setComplemento(cepDto.getComplemento());
                pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
                pessoaJuridica.getEnderecos().get(p).setUf(cepDto.getUf());
            }
        } else {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++){
                Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

                if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())){
                    CepDto cepDto = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

                    pessoaJuridica.getEnderecos().get(p).setBairro(cepDto.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setComplemento(cepDto.getComplemento());
                    pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
                    pessoaJuridica.getEnderecos().get(p).setUf(cepDto.getUf());
                }
            }
        }

        pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping(value = "**/salvarPf")
    public ResponseEntity<PessoaFisica> salvarPf(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionLojaVirtual {

        if (pessoaFisica == null) {
            throw new ExceptionLojaVirtual("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaFisica.getId() == null && pesssoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
            throw new ExceptionLojaVirtual("Já existe um CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }

        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionLojaVirtual("Cpf : " + pessoaFisica.getCpf() + " está inválido.");
        }

        pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);

    }
}
