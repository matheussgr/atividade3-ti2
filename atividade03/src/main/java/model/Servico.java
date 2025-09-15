package model;

public class Servico {
    private int id;
    private String nome;
    private String descricao;
    private float preco;

    public Servico() {
		id = -1;
		nome = "";
		descricao = "";
		preco = 0.00F;
    }

    public Servico(int id, String nome, String descricao, float preco) {
        setId(id);
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
    }

	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}

    @Override
    public String toString() {
        return "Serviço: " + nome + " | Descrição: " + descricao + " | Preço: R$ " + preco;
    }

	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Servico) obj).getID());
	}	
}