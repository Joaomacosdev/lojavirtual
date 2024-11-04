package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.ContaPagar;
import br.com.lojavirtual.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContaPagarController {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @ResponseBody
    @PostMapping(value = "**/salvarContaPagar")
    public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody ContaPagar contaPagar) throws ExceptionLojaVirtual {

        if (contaPagar.getId() == null) {
            List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());

            if (!contaPagars.isEmpty()) {
                throw new ExceptionLojaVirtual("Já existe Conta a Pagar com a descrição: " + contaPagar.getDescricao());
            }

            if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {
                throw new ExceptionLojaVirtual("A pessoa responsável pela conta deve ser informada");
            }

            if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
                throw new ExceptionLojaVirtual("A Empresa responsável pela conta deve ser informada");
            }
        }


        ContaPagar contaPagars = contaPagarRepository.save(contaPagar);

        return new ResponseEntity<ContaPagar>(contaPagars, HttpStatus.OK);
    }



    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteConta") /*Mapeando a url para receber JSON*/
    public ResponseEntity<String> deleteAcesso(@RequestBody ContaPagar contaPagar) { /*Recebe o JSON e converte pra Objeto*/

        contaPagarRepository.deleteById(contaPagar.getId());

        return new ResponseEntity<String>("conta Removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteContaPagarId/{id}")
    public ResponseEntity<String> deleteAcessoPorId(@PathVariable("id") Long id) {

        contaPagarRepository.deleteById(id);

        return new ResponseEntity<String>("conta pagar removida", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterContaPagar/{id}")
    public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionLojaVirtual {


        ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

        if (contaPagar == null) {
            throw new ExceptionLojaVirtual("Não econtrou conta a pagar com código: " + id);
        }

        return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/buscarContaPagarDesc/{desc}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarDesc(@PathVariable("desc") String desc) {

        List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(desc.toUpperCase());

        return new ResponseEntity<List<ContaPagar>>(contaPagars, HttpStatus.OK);
    }
}