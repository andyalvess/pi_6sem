package azure_api.model.entity;



public class PessoaEntity {
	
	private int id;
	private String nome;
	private String cpf;
	private String rg;
	private String telefone;
	private String email;
	private String azurePersonId;

	public PessoaEntity(String nome, String cpf, String rg, String telefone, String email) {
		// TODO Auto-generated constructor stub
	}



	public PessoaEntity() {
		// TODO Auto-generated constructor stub
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCpf() {
		return cpf;
	}



	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	public String getRg() {
		return rg;
	}



	public void setRg(String rg) {
		this.rg = rg;
	}



	public String getTelefone() {
		return telefone;
	}



	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}
	
	public String getAzurePersonId() {
		return this.azurePersonId;
	}
	
	public void setAzurePersonId(String azurePersonId) {
		this.azurePersonId = azurePersonId;
	}	
	
	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", email=" + email + ", telefone="
				+ telefone + ", email=" + email  + ", azurePersonId=" + azurePersonId  + "]";
	}


	
	

}
