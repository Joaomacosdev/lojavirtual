package br.com.lojavirtual.controller;

import br.com.lojavirtual.dto.ImagemProdutoDto;
import br.com.lojavirtual.model.ImagemProduto;
import br.com.lojavirtual.repository.ImagemProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemProdutoController {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepositoey;

    @ResponseBody
    @PostMapping(value = "**/salvarImagemProduto")
    public ResponseEntity<ImagemProdutoDto> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto) {

        imagemProduto = imagemProdutoRepositoey.saveAndFlush(imagemProduto);

        ImagemProdutoDto imagemProdutoDto = new ImagemProdutoDto();
        imagemProdutoDto.setId(imagemProdutoDto.getId());
        imagemProdutoDto.setImagemOriginal(imagemProdutoDto.getImagemOriginal());
        imagemProdutoDto.setImagemMiniatura(imagemProdutoDto.getImagemMiniatura());
        imagemProdutoDto.setProduto(imagemProdutoDto.getId());
        imagemProdutoDto.setEmpresa(imagemProdutoDto.getId());

        return new ResponseEntity<ImagemProdutoDto>(imagemProdutoDto, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteTodasImagens/{idProduto}")
    public ResponseEntity<?> deleteTodasImagens(@PathVariable("idProduto") Long idProduto){


        imagemProdutoRepositoey.deleteImagens(idProduto);

        return new ResponseEntity<>("Imagem removida com sucesso!", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteImagemObjeto")
    public ResponseEntity<?> deleteImagemOBjeto(@RequestBody ImagemProduto imagemProduto){
        imagemProdutoRepositoey.deleteById(imagemProduto.getId());

        if (!imagemProdutoRepositoey.existsById(imagemProduto.getId())) {
            return new ResponseEntity<String>("Imagem já foi, removida ou não existe com esse id" + imagemProduto.getId(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Imagem removida com sucesso!", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deleteImagemProdutoPorId/{id}")
    public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id){

        if (!imagemProdutoRepositoey.existsById(id)) {
            return new ResponseEntity<String>("Imagem já foi removida ou não existe id" + id, HttpStatus.OK);
        }

        imagemProdutoRepositoey.deleteById(id);

        return new ResponseEntity<>("Imagem removida com sucesso!", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterImagemPorProduto/{idProduto}")
    public ResponseEntity<List<ImagemProdutoDto>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto){

        List<ImagemProdutoDto> dtos = new ArrayList<ImagemProdutoDto>();

        List<ImagemProduto> imagemProdutos = imagemProdutoRepositoey.buscaImagemProduto(idProduto);

        for (ImagemProduto imagemProduto : imagemProdutos){
            ImagemProdutoDto imagemProdutoDto = new ImagemProdutoDto();
            imagemProdutoDto.setId(imagemProdutoDto.getId());
            imagemProdutoDto.setImagemOriginal(imagemProdutoDto.getImagemOriginal());
            imagemProdutoDto.setImagemMiniatura(imagemProdutoDto.getImagemMiniatura());
            imagemProdutoDto.setProduto(imagemProdutoDto.getId());
            imagemProdutoDto.setEmpresa(imagemProdutoDto.getId());

            dtos.add(imagemProdutoDto);
        }



        return new ResponseEntity<List<ImagemProdutoDto>>(dtos,HttpStatus.OK);
    }
}
