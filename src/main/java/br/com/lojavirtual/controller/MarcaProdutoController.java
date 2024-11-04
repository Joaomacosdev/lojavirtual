package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.MarcaProduto;
import br.com.lojavirtual.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RestController
public class MarcaProdutoController {

    @Autowired
    private MarcaRepository marcaRepository;

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/salvarMarca") /*Mapeando a url para receber JSON*/
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody MarcaProduto marcaProduto) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/

        if (marcaProduto.getId() == null) {
            List<MarcaProduto> marcaProdutos = marcaRepository.buscarMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());
            if (!marcaProdutos.isEmpty()) {
                throw new ExceptionLojaVirtual("Já existe marca com essa descrição: " + marcaProduto.getNomeDesc());
            }
        }

        MarcaProduto marcaProdutoSalvo = marcaRepository.save(marcaProduto);

        return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteMarca") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteMarca(@RequestBody MarcaProduto marcaProduto) { /*Recebe o JSON e converte pra Objeto*/

        marcaRepository.deleteById(marcaProduto.getId());

        return new ResponseEntity("marca Removido", HttpStatus.OK);
    }

    //@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
    @ResponseBody
    @DeleteMapping(value = "**/deleteMarcaPorId/{id}")
    public ResponseEntity<?> deleteMarcaPorId(@PathVariable("id") Long id) {

        marcaRepository.deleteById(id);

        return new ResponseEntity("Marca Removido", HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "**/obterMarcaProduto/{id}")
    public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual {


        MarcaProduto marcaProduto = marcaRepository.findById(id).orElse(null);

        if (marcaProduto == null) {
            throw new ExceptionLojaVirtual("Não encontrou a marca com o código: " + id);
        }

        return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);


    }


    @ResponseBody
    @GetMapping(value = "**/buscarMarcaProdutoPorDesc/{desc}")
    public ResponseEntity<List<MarcaProduto>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<MarcaProduto> marcaProdutos = marcaRepository.buscarMarcaDesc(desc.toUpperCase().trim());

        return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
    }


}
