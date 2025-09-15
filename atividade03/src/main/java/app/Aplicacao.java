package app;

import static spark.Spark.*;
import service.ServicoService;

public class Aplicacao {
	
	private static ServicoService servicoService = new ServicoService();
	
    public static void main(String[] args) {
        port(6789);
        
        
        staticFiles.location("/public");
        
        post("/servico/insert", (request, response) -> servicoService.insert(request, response));

        get("/servico/:id", (request, response) -> servicoService.get(request, response));
        
        get("/servico/list/:orderby", (request, response) -> servicoService.getAll(request, response));

        get("/servico/update/:id", (request, response) -> servicoService.getToUpdate(request, response));        
        
        post("/servico/update/:id", (request, response) -> servicoService.update(request, response));
           
        get("/servico/delete/:id", (request, response) -> servicoService.delete(request, response));
    }
}
