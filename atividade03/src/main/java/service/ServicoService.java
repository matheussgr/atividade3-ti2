package service;

import java.util.Scanner;
import java.io.File;
import java.util.List;
import dao.ServicoDAO;
import model.Servico;
import spark.Request;
import spark.Response;

public class ServicoService {

    private ServicoDAO servicoDAO = new ServicoDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_DESCRICAO = 2;
    private final int FORM_ORDERBY_PRECO = 3;
    
    
    public ServicoService() {
        makeForm();
    }

    
    public void makeForm() {
        makeForm(FORM_INSERT, new Servico(), FORM_ORDERBY_DESCRICAO);
    }

    
    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Servico(), orderBy);
    }

    
    public void makeForm(int tipo, Servico servico, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try{
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while(entrada.hasNext()){
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        }  catch (Exception e) { System.out.println(e.getMessage()); }
        
        StringBuilder sbForm = new StringBuilder();

        if(tipo != FORM_INSERT) {
            sbForm.append("<div class='mb-3'><a href='/servico/list/1' class='btn btn-link'>Novo Serviço</a></div>");
        }

        if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/servico/";
            String name, descricao, buttonLabel;
            if (tipo == FORM_INSERT){
                action += "insert";
                name = "Inserir Serviço";
                descricao = "Corte de cabelo, Aula Particular, ...";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + servico.getID();
                name = "Atualizar Serviço (ID " + servico.getID() + ")";
                descricao = servico.getDescricao();
                buttonLabel = "Atualizar";
            }

            sbForm.append("<div class='form--card'>");
            sbForm.append("<form class='row g-3' action='" + action + "' method='post' id='form-add'>");

            // Nome
            sbForm.append("<div class='col-md-4'>");
            sbForm.append("<label class='form-label'>Nome</label>");
            sbForm.append("<input class='form-control' type='text' name='nome' value='" + servico.getNome() + "'/>");
            sbForm.append("</div>");

            // Descrição
            sbForm.append("<div class='col-md-5'>");
            sbForm.append("<label class='form-label'>Descrição</label>");
            sbForm.append("<input class='form-control' type='text' name='descricao' value='" + descricao + "'/>");
            sbForm.append("</div>");

            // Preço
            sbForm.append("<div class='col-md-2'>");
            sbForm.append("<label class='form-label'>Preço</label>");
            sbForm.append("<input class='form-control' type='text' name='preco' value='" + servico.getPreco() + "'/>");
            sbForm.append("</div>");

            // Botão
            sbForm.append("<div class='col-md-1 d-flex align-items-end'>");
            sbForm.append("<button type='submit' class='btn btn-success btn-main'>" + buttonLabel + "</button>");
            sbForm.append("</div>");

            sbForm.append("</form>");
            sbForm.append("</div>");
        } else if (tipo == FORM_DETAIL) {
            sbForm.append("<div class='form--card'>");
            sbForm.append("<h5>Detalhar Serviço (ID " + servico.getID() + ")</h5>");
            sbForm.append("<p><strong>Nome:</strong> " + servico.getNome() + "</p>");
            sbForm.append("<p><strong>Descrição:</strong> " + servico.getDescricao() + "</p>");
            sbForm.append("<p><strong>Preço:</strong> R$ " + servico.getPreco() + "</p>");
            sbForm.append("</div>");
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }

        form = form.replace("<UM-SERVICO>", sbForm.toString());
        
        List<Servico> servicos;
        if (orderBy == FORM_ORDERBY_ID) {                     servicos = servicoDAO.getOrderByID();
        } else if (orderBy == FORM_ORDERBY_DESCRICAO) {       servicos = servicoDAO.getOrderByDescricao();
        } else if (orderBy == FORM_ORDERBY_PRECO) {           servicos = servicoDAO.getOrderByPreco();
        } else {                                              servicos = servicoDAO.get();
        }

        // --- tabela bonita ---
        StringBuilder sbList = new StringBuilder();
        sbList.append("<div class='table-responsive'>");
        sbList.append("<table class='table tabela-servicos'>");
        sbList.append("<thead><tr>");
        sbList.append("<th><a href='/servico/list/" + FORM_ORDERBY_ID + "' style='text-decoration:none; color:black;'>ID</a></th>");
        sbList.append("<th>Nome</th>");
        sbList.append("<th><a href='/servico/list/" + FORM_ORDERBY_DESCRICAO + "' style='text-decoration:none; color:black;'>Descrição</a></th>");
        sbList.append("<th><a href='/servico/list/" + FORM_ORDERBY_PRECO + "' style='text-decoration:none; color:black;'>Preço</a></th>");
        sbList.append("<th class='small-col'>Detalhar</th>");
        sbList.append("<th class='small-col'>Atualizar</th>");
        sbList.append("<th class='small-col'>Excluir</th>");
        sbList.append("</tr></thead>");
        sbList.append("<tbody>");

        for (Servico p : servicos) {
            sbList.append("<tr>");
            sbList.append("<td>" + p.getID() + "</td>");
            sbList.append("<td>" + p.getNome() + "</td>");
            sbList.append("<td>" + p.getDescricao() + "</td>");
            sbList.append("<td>R$ " + p.getPreco() + "</td>");

            // Detalhar
            sbList.append("<td class='text-center'>");
            sbList.append("<a href='/servico/" + p.getID() + "' class='icon-btn' title='Detalhar'><img src='/image/detail.png' alt='Detalhar'/></a>");
            sbList.append("</td>");

            // Atualizar
            sbList.append("<td class='text-center'>");
            sbList.append("<a href='/servico/update/" + p.getID() + "' class='icon-btn' title='Atualizar'><img src='/image/update.png' alt='Atualizar'/></a>");
            sbList.append("</td>");

            // Excluir
            sbList.append("<td class='text-center'>");
            sbList.append("<a href=\"javascript:confirmarDeleteServico('" + p.getID() + "', '" + p.getNome() + "');\" class='icon-btn' title='Excluir'><img src='/image/delete.png' alt='Excluir'/></a>");
            sbList.append("</td>");

            sbList.append("</tr>");
        }
        sbList.append("</tbody>");
        sbList.append("</table>");
        sbList.append("</div>");

        form = form.replace("<LISTAR-SERVICO>", sbList.toString());        
    }
    
    
    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        String descricao = request.queryParams("descricao");
        float preco = Float.parseFloat(request.queryParams("preco"));

        String resp = "";
        
        Servico servico = new Servico(-1, nome, descricao, preco);
        
        if(servicoDAO.insert(servico) == true) {
            resp = "Serviço (" + nome + ") inserido!";
            response.status(201); // 201 Created
        } else {
            resp = " (" + nome + ") não inserido!";
            response.status(404); // 404 Not found
        }
            
        makeForm();
        return form = form.replace("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        Servico servico = (Servico) servicoDAO.get(id);
        
        if (servico != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, servico, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Serviço " + id + " não encontrado.";
            makeForm();
            form = form.replace("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }

    
    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        Servico servico = (Servico) servicoDAO.get(id);
        
        if (servico != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, servico, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Serviço " + id + " não encontrado.";
            makeForm();
            form = form.replace("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }
    
    
    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }           
    
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Servico servico = servicoDAO.get(id);
        String resp = "";       

        if (servico != null) {
            servico.setNome(request.queryParams("nome"));
            servico.setDescricao(request.queryParams("descricao"));
            servico.setPreco(Float.parseFloat(request.queryParams("preco")));
            servicoDAO.update(servico);
            response.status(200); // success
            resp = "Serviço (ID " + servico.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Serviço (ID " + id + ") não encontrado!";
        }
        makeForm();
        return form = form.replace("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Servico servico = servicoDAO.get(id);
        String resp = "";       

        if (servico != null) {
            servicoDAO.delete(id);
            response.status(200); // success
            resp = "Serviço (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Serviço (" + id + ") não encontrado!";
        }
        makeForm();
        return form = form.replace("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }
}
