package br.edu.ufrb.aluno.fetch.controllers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
import br.edu.ufrb.aluno.fetch.entities.Formulario;
/**
* ExampleController
*
* @author Daniel Padua
*/
@RestController
@RequestMapping("api/armario")
public class MainController { //Crie um novo armario
    ArmarioDeMedicamentos armario = new ArmarioDeMedicamentos(); //Cria um array vazio no armario, onde se coloca, edita e retira medicamentos.
    
    int nextID = 0; //Auxilia na criação do codigo dos medicamentos. (Contador)

    //Pegue todos os medicamentos do armario e exibe na tela.
    @GetMapping("/get/medicamentos")
    public ArrayList<Medicamento> getMedicamentos() {
       // armario.setMedicamentos(medicamento);
        return armario.getMedicamentos();
    }

    @GetMapping("/get/medicamento")
    public ArrayList<Medicamento> getSearchMedicamento(@RequestParam String nome) { //RequestParam = Parametro que voce quer receber, nesse caso uma string.
       // armario.setMedicamentos(medicamento);
        //System.out.println(armario.searchMedicamento(nome)); 

        return armario.searchMedicamento(nome); //Pesquise pelo medicamento por letra(string) ou por código
    } 

    //Crie um novo medicamento
    @PostMapping("/create/medicamento") //Rota para criar um medicamento.
    public void put(@RequestBody String medicamentoACadastrar) {      
        String json = medicamentoACadastrar;
        
        try{
            ObjectMapper mapper = new ObjectMapper();
            EntradaCadastro entradaCadastro = mapper.readValue(json, EntradaCadastro.class);

            Medicamento medicamentoNovo = new Medicamento( //pegue os atributos da entrada cadastro e crie um novo medicamento. 
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
                       // System.out.println("Medicamento: " + remedio.getNome());
    
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
            //System.out.println("Deu erro pae");
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

    @PostMapping("/register")
    public String nameRegistration(@ModelAttribute Formulario formulario){
        System.out.println(formulario.toString());

        return "index";
    }

    @DeleteMapping("/remove/medicamento")
    public ResponseEntity<String> removeMedicamento(@RequestParam String codigo) {
        String lista = new String();
        for (Medicamento remedio : armario.getMedicamentos()) {
            lista += "Medicamento: " + remedio.getNome()
            + " Código: " + remedio.getCodigo() + "\n";
        }
        //System.out.println(lista);
        return armario.removeMedicamento(codigo);
    }

    @PatchMapping("/edit/medicamento")
    public ResponseEntity<String> editMedicamento(@RequestParam String codigoAntigo, @RequestParam String codigoNovo, 
    @RequestParam String quantidade,
    @RequestParam String peso,
    @RequestParam String generico,
    @RequestParam String tarjaPreta,
    @RequestParam String nome,
    @RequestParam String fabricante,
    @RequestParam String info){

        return armario.editMedicamento(codigoAntigo, codigoNovo, Integer.parseInt(quantidade), Integer.parseInt(peso), Boolean.parseBoolean(generico), Boolean.parseBoolean(tarjaPreta), nome, fabricante, info); 
    }   
}