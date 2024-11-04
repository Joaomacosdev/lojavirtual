package br.com.lojavirtual.controller;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.dto.CategoriaProdutoDto;
import br.com.lojavirtual.model.CategoriaProduto;
import br.com.lojavirtual.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @ResponseBody
    @GetMapping(value = "**/buscarPorDescCatgoria/{desc}")
    public ResponseEntity<List<CategoriaProduto>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<CategoriaProduto> acesso = categoriaProdutoRepository.buscarCategoriaDes(desc.toUpperCase());

        return new ResponseEntity<List<CategoriaProduto>>(acesso,HttpStatus.OK);
    }


    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "**/deleteCategoria") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteAcesso(@RequestBody CategoriaProduto categoriaProduto) { /*Recebe o JSON e converte pra Objeto*/

        if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
            return new ResponseEntity("Categoria já foi removida",HttpStatus.OK);
        }

        categoriaProdutoRepository.deleteById(categoriaProduto.getId());

        return new ResponseEntity("Categoria Removida",HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping(value = "**/salvarCategoria")
    private ResponseEntity<CategoriaProdutoDto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLojaVirtual {

        if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null )){
            throw new ExceptionLojaVirtual("A empresa deve ser informada");
        }

        if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc())) {
            throw new ExceptionLojaVirtual("Não pode cadastar categoria com mesmo nome.");
        }


        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

        CategoriaProdutoDto categoriaProdutoDto = new CategoriaProdutoDto();
        categoriaProdutoDto.setId(categoriaSalva.getId());
        categoriaProdutoDto.setNomeDesc(categoriaSalva.getNomeDesc());
        categoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

        return new ResponseEntity<CategoriaProdutoDto>(categoriaProdutoDto , HttpStatus.OK);
    }

}
