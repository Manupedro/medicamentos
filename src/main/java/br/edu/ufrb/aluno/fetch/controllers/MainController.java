package br.edu.ufrb.aluno.fetch.controllers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufrb.aluno.fetch.entities.ArmarioDeMedicamentos;
import br.edu.ufrb.aluno.fetch.entities.Medicamento;
import br.edu.ufrb.aluno.fetch.entities.EntradaCadastro;
/**
* ExampleController
*
* @author Daniel Padua
*/
@RestController
@RequestMapping("api/armario")
public class MainController {
    ArmarioDeMedicamentos armario = new ArmarioDeMedicamentos();
    
    int nextID = 0;

    //Pegue todos os medicamentos do armario
    @GetMapping("/get/medicamentos")
    public ArrayList<Medicamento> getMedicamentos() {
       // armario.setMedicamentos(medicamento);
        return armario.getMedicamentos();
    }

    @GetMapping("/get/medicamento")
    public ArrayList<Medicamento> getSearchMedicamento(@RequestParam String nome) {
       // armario.setMedicamentos(medicamento);
        System.out.println(armario.searchMedicamento(nome));

        return armario.searchMedicamento(nome);
    } 

    //Crie um novo medicamento
    @PostMapping("/create/medicamento")
    public void put(@RequestBody String medicamentoACadastrar) {
        
        String json = medicamentoACadastrar;
        
        
        try{
            ObjectMapper mapper = new ObjectMapper();
            EntradaCadastro entradaCadastro = mapper.readValue(json, EntradaCadastro.class);

            Medicamento medicamentoNovo = new Medicamento(
                entradaCadastro.generateId(entradaCadastro.getNome(), nextID),
                entradaCadastro.getQuantidade(), 
                entradaCadastro.getPesoEmGramas(),
                entradaCadastro.isStatusGenerico(),
                entradaCadastro.isStatusTarjaPreta(),
                entradaCadastro.getNome(),
                entradaCadastro.getFabricante(),
                entradaCadastro.getOutrasInformacoes());

                
                
                //Adicionando o primeiro medicamento
                if(armario.getMedicamentos().size() < 1){

                    armario.setMedicamentos(medicamentoNovo);
                    nextID++;

                
                }
                //adicionando os demais medicamentos
                else{

                    boolean jaExiste = false;
                    //Passeando por todos os medicamentos no armario
                    for (Medicamento remedio : armario.getMedicamentos()) {
                        System.out.println("Medicamento: " + remedio.getNome());
    
                        //Verificando se já tem um remedio igual ao que vc está querendo cadastrar
                         if(remedio.equals(medicamentoNovo)){
                            //aumentando a quantidade do medicamento
                            jaExiste = true;
                            remedio.setQuantidade(remedio.getQuantidade() + medicamentoNovo.getQuantidade());
                            break;
                        }
                        
                    }
                    
                    if(!jaExiste){
                        //colocando o medicamento no armario
                        armario.setMedicamentos(medicamentoNovo);
                        nextID++;

                    }
                }
                    //Adicionando demais medicamentos no armario
                
                    
            
                    
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Deu erro pae");
        }
        
    }

    //Remoção de medicamentos
    /* @GetMapping("remove/medicamentos")
    public ResponseEntity<String> listaDeRemedios() {
        String lista = new String();
        for (Medicamento remedio : armario.getMedicamentos()) {
            lista += "Medicamento: " + remedio.getNome()
            + " Código: " + remedio.getCodigo() + "\n";
        }
        return ResponseEntity.ok(lista);
    } */


    @DeleteMapping("/remove/medicamento")
    public ResponseEntity<String> removeMedicamento(@RequestParam String codigo) {
        String lista = new String();
        for (Medicamento remedio : armario.getMedicamentos()) {
            lista += "Medicamento: " + remedio.getNome()
            + " Código: " + remedio.getCodigo() + "\n";
        }
        
        System.out.println(lista);

        return armario.removeMedicamento(codigo);
        

    }


    
}