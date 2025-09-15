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
		
		String umServico = "";
		if(tipo != FORM_INSERT) {
			umServico += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/servico/list/1\">Novo Servico</a></b></font></td>";
			umServico += "\t\t</tr>";
			umServico += "\t</table>";
			umServico += "\t<br>";			
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
			umServico += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umServico += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""+ servico.getNome() +"\"></td>";
			umServico += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umServico += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ servico.getPreco() +"\"></td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umServico += "\t\t</tr>";
			umServico += "\t</table>";
			umServico += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umServico += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Servico (ID " + servico.getID() + ")</b></font></td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td>&nbsp;Descrição: "+ servico.getDescricao() +"</td>";
			umServico += "\t\t\t<td>Preco: "+ servico.getPreco() +"</td>";
			umServico += "\t\t</tr>";
			umServico += "\t\t<tr>";
			umServico += "\t\t\t<td>&nbsp;</td>";
			umServico += "\t\t</tr>";
			umServico += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-SERVICO>", umServico);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Serviços</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/servico/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/servico/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/servico/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Servico> servicos;
		if (orderBy == FORM_ORDERBY_ID) {                 	servicos = servicoDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		servicos = servicoDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			servicos = servicoDAO.getOrderByPreco();
		} else {											servicos = servicoDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Servico p : servicos) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getNome() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/servico/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/servico/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteServico('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-SERVICO>", list);				
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
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
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
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
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
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
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
            resp = "Serviço (ID \" + servico.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
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
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}