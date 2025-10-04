package com.ecominds.tf_arquiweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecominds.tf_arquiweb.dto.OrganizadorDTO;
import com.ecominds.tf_arquiweb.entidades.Organizador;
import com.ecominds.tf_arquiweb.intefaces.iOrganizadorServices;

@RestController
@RequestMapping("/api/organizadores")
@CrossOrigin(origins = "*")
public class OrganizadorController {

    @Autowired
    private iOrganizadorServices organizadorServices;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<OrganizadorDTO>> listar() {
        List<Organizador> organizadores = organizadorServices.list();
        List<OrganizadorDTO> organizadoresDTO = organizadores.stream()
                .map(organizador -> modelMapper.map(organizador, OrganizadorDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(organizadoresDTO, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<OrganizadorDTO> registrar(@RequestBody OrganizadorDTO organizadorDTO) {
        Organizador organizador = modelMapper.map(organizadorDTO, Organizador.class);
        organizadorServices.insert(organizador);
        OrganizadorDTO nuevoOrganizadorDTO = modelMapper.map(organizador, OrganizadorDTO.class);
        return new ResponseEntity<>(nuevoOrganizadorDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizadorDTO> actualizar(@PathVariable Integer id, @RequestBody OrganizadorDTO organizadorDTO) {
        organizadorDTO.setId(id);
        Organizador organizador = modelMapper.map(organizadorDTO, Organizador.class);
        organizadorServices.update(organizador);
        OrganizadorDTO organizadorActualizadoDTO = modelMapper.map(organizador, OrganizadorDTO.class);
        return new ResponseEntity<>(organizadorActualizadoDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        organizadorServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizadorDTO> obtenerPorId(@PathVariable Integer id) {
        Organizador organizador = organizadorServices.listId(id);
        if (organizador != null) {
            OrganizadorDTO organizadorDTO = modelMapper.map(organizador, OrganizadorDTO.class);
            return new ResponseEntity<>(organizadorDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<OrganizadorDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<Organizador> organizadores = organizadorServices.findByNombreContaining(nombre);
        List<OrganizadorDTO> organizadoresDTO = organizadores.stream()
                .map(organizador -> modelMapper.map(organizador, OrganizadorDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(organizadoresDTO, HttpStatus.OK);
    }
}
