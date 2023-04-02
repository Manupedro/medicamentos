package br.edu.ufrb.aluno.fetch.controllers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    //Pegue os medicamentos do armario
    @GetMapping("/get/medicamentos")
    public ArrayList<Medicamento> getMedicamentos() {
       // armario.setMedicamentos(medicamento);
        return armario.getMedicamentos();
    } 

    //Crie um novo medicamento
    @PostMapping("/create/medicamento")
    public void put(@RequestBody String medicamentoACadastrar) {
        
        String json = medicamentoACadastrar;
        
        
        try{
            ObjectMapper mapper = new ObjectMapper();
            EntradaCadastro entradaCadastro = mapper.readValue(json, EntradaCadastro.class);

            Medicamento medicamentoNovo = new Medicamento(
                entradaCadastro.getCodigo(),
                entradaCadastro.getQuantidade(), 
                entradaCadastro.getPesoEmGramas(),
                entradaCadastro.isStatusGenerico(),
                entradaCadastro.isStatusTarjaPreta(),
                entradaCadastro.getNome(),
                entradaCadastro.getFabricante(),
                entradaCadastro.getOutrasInformacoes());

                if(armario.getMedicamentos().size() < 1){

                    armario.setMedicamentos(medicamentoNovo);
                }
                for (Medicamento remedio : armario.getMedicamentos()) {
                    System.out.println("Medicamento: " + remedio.getNome());

                        if(remedio.equals(medicamentoNovo)){
                            System.out.println("O medicamento adicionado " + medicamentoNovo.getNome() + "Já existe. Atualizamos a quantidade dele para " + (remedio.getQuantidade() + medicamentoNovo.getQuantidade()) + " unidades");
                            remedio.setQuantidade(remedio.getQuantidade() + 1);
                            break;
                        }else{
                            armario.setMedicamentos(medicamentoNovo);
                        }
                    
                
                }
                
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Deu erro pae");
        }
        
    } 

    
}