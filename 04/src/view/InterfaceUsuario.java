package src.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import src.model.*;
import src.controller.*;

public class InterfaceUsuario {

	public static final String DATA_FOLDER = "./data/";

	private Scanner ler;
	private String nome;
	private String email;
	private String senha;
	private PrimaryIndexCRUD<Usuario, pcvDireto> arqUsuario;
	private SecondaryIndexCRUD<pcvUsuario> secondaryIndex;
	private PerguntaController perguntaController;
	private RespostaController respostaController;

	private String entrada;
	private boolean forcarEntrada = false;
	private String entradaForcada = "";
	private String confirmar;

	private int idUsuario = -1;
	private int idPergunta = -1; // pergunta selecionada

	public InterfaceUsuario() throws Exception {
		arqUsuario = new PrimaryIndexCRUD<Usuario, pcvDireto>(Usuario.class.getConstructor(),
				pcvDireto.class.getConstructor(), pcvDireto.class.getConstructor(int.class, long.class), DATA_FOLDER);
		secondaryIndex = new SecondaryIndexCRUD<pcvUsuario>(pcvUsuario.class.getConstructor(),
				pcvUsuario.class.getConstructor(String.class, int.class), DATA_FOLDER);

		perguntaController = new PerguntaController(DATA_FOLDER, "pergunta.db", 100, "perguntaArvore.db");
		respostaController = new RespostaController(DATA_FOLDER, "resposta.db", 100);
	}

	public void MenuPrincipalPerguntas() {
		ler = new Scanner(System.in);

		String line;
		int opcao = 0;
		try {
			do {
				ImprimirMenuPrincipalPerguntas();
				line = ler.nextLine();
				opcao = Integer.parseInt(line);
				switch (opcao) {
					case 1:
						// Chamar Interface Criação de perguntas
						System.out.println("Criando Pergunta...");
						MenuCriacaoPerguntas();
						break;
					case 2:
						// Chamar interface de consultar/responder perguntas
						MenuPesquisaPerguntas();

						break;
					case 3:
						// Acessar notificações
						break;
					case 0:
						System.out.println("Deslogando...");
						Logout();
						break;
					default:
						System.out.println("Opçao inválida");
						break;
				}
			} while (opcao != 0);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PrintBox(String content, int extraSpaces) {
		int size = content.length() + extraSpaces;

		System.out.print("+--");
		for (int i = 0; i < size; i++) {
			System.out.print("-");
		}
		System.out.print("+\n");

		String contentBox = "| " + content;
		System.out.print(contentBox);
		for (int i = 0; i < (size - contentBox.length()) * 2; i++) {
			System.out.print(" ");
		}
		System.out.print("|\n");

		System.out.print("+--");
		for (int i = 0; i < size; i++) {
			System.out.print("-");
		}
		System.out.print("+\n");
	}

	public void PrintarPerguntaParaResposta(Pergunta perguntaEscolhida)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, Exception {
		Usuario autor = arqUsuario.read(perguntaEscolhida.getIdUsuario());

		PrintBox(perguntaEscolhida.getPergunta(), 5);

		System.out.println("Criada em " + perguntaEscolhida.getCriacaoString() + " por " + autor.getNome());
		System.out.println("Palavras chave: " + perguntaEscolhida.getPalavrasChave());
		System.out.println("Nota: " + perguntaEscolhida.getNota());

		System.out.println("RESPOSTAS");
		System.out.println("---------");
	}

	public void MenuVisualizacaoPergunta(Pergunta perguntaEscolhida)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, Exception {
		if (perguntaEscolhida != null) {

			idPergunta = perguntaEscolhida.getID();

			PrintarPerguntaParaResposta(perguntaEscolhida);

			ImprimirMenuResponder();

			ler = new Scanner(System.in);

			String line = "";
			int opcao = 0;
			try {
				line = ler.nextLine();
				opcao = Integer.parseInt(line);
				switch (opcao) {
					case 1:
						MenuRespostas(perguntaEscolhida);
						break;
					case 2:
						// TODO Avaliar Resposta
						break;

					case 0:
						MenuCriacaoPerguntas();
						break;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void ImprimirMenuResponder() {
		System.out.println("\t 1) Responder");
		System.out.println("\t 2) Avaliar");
		System.out.println("\n\t 0) Retornar");
	}

	public void MenuRespostas(Pergunta perguntaEscolhida)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, Exception {

		PrintarPerguntaParaResposta(perguntaEscolhida);

		ImprimirMenuRespostas();

		ler = new Scanner(System.in);

		String line = "";
		int opcao = 0;
		try {
			line = ler.nextLine();
			opcao = Integer.parseInt(line);
			switch (opcao) {
				case 1:
					ListarRespostas(idPergunta);
					break;
				case 2:
					int idResposta = IncluirResposta(idPergunta);
					if (idResposta == -1) {
						MenuRespostas(perguntaEscolhida);
					}
					break;

				case 3:
					AlterarResposta(idPergunta);
					break;

				case 4:
					ArquivarResposta(idPergunta);
					break;
				case 0:
					// TODO verificar se esta correto
					MenuVisualizacaoPergunta(perguntaEscolhida);
					break;
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ImprimirMenuRespostas() {
		System.out.println("\t 1) Listar suas respostas");
		System.out.println("\t 2) Incluir uma resposta");
		System.out.println("\t 3) Alterar uma resposta");
		System.out.println("\t 4) Arquivar uma resposta");
		System.out.println("\n\t 0) Retornar ao menu anterior");
	}

	public List<Resposta> ListarRespostas(int idPergunta)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

		List<Resposta> respostas = respostaController.readAllRespostaPergunta(idPergunta);

		for (int i = 0; i < respostas.size(); i++) {
			Resposta resposta = respostas.get(i);

			String nomeUsusario = arqUsuario.read(resposta.getIdUsuario()).getNome();

			System.out.println(resposta.toString(i + 1, nomeUsusario));
		}

		return respostas;
	}

	public int IncluirResposta(int idPergunta) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, Exception {
		System.out.println("| Insira sua resposta:");
		System.out.print("\t-> ");
		String resposta = ler.nextLine();

		if (resposta == null || resposta.isEmpty()) {
			return -1;
		}

		System.out.println("| Confirmar resposta? (S/N)");
		System.out.print("\t-> ");
		String confirmar = ler.nextLine();

		if (confirmar.toLowerCase() == "n") {
			return -1;
		}

		Resposta objResposta = new Resposta(idPergunta, this.idUsuario, resposta);
		int id = respostaController.create(objResposta);
		objResposta.setID(id);
		System.out.println("Resposta criada: " + id);
		System.out.println("Resposta : " + objResposta.toString());
		System.out.println(String.format("Pergunta id:%d respondida", idPergunta));

		return id;

	}

	public void AlterarResposta(int idPergunta) {
		// TODO alterar Respostas
	}

	public void ArquivarResposta(int idPergunta) {
		// TODO arquivar Respostas
	}

	public void MenuSelecaoPerguntas(ArrayList<Pergunta> perguntas) {
		// Criar um hash para perguntas
		HashMap<Integer, Pergunta> perguntasMap = new HashMap<>();

		ler = new Scanner(System.in);

		int posicao = 1;
		for (Pergunta pergunta : perguntas) {
			System.out.println(pergunta.toString(posicao));
			perguntasMap.put(posicao++, pergunta);
		}

		System.out.println("\tInforme o número da pergunta que deseja visitar");
		System.out.println("\tInforme \"0\" para sair");
		System.out.print("\t-> ");

		String line = "";
		int idEscolhido = 0;
		try {
			line = ler.nextLine();
			idEscolhido = Integer.parseInt(line);
			Pergunta perguntaEscolhida = perguntasMap.get(idEscolhido);
			MenuVisualizacaoPergunta(perguntaEscolhida);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void MenuPesquisaPerguntas() {
		ler = new Scanner(System.in);

		String line = "";
		try {
			do {
				ImprimirMenuPesquisaPalavrasChave();
				line = ler.nextLine().toLowerCase();
				if (!line.equals("0")) {
					ArrayList<Integer> listOfIds = lerIdsPorPalavrasChave(line);
					ArrayList<Pergunta> perguntas = lerPerguntasPorId(listOfIds);
					if (!perguntas.isEmpty()) {
						MenuSelecaoPerguntas(perguntas);
					} else {
						System.out.println("Nenhuma pergunta encontrada");
					}
				}

			} while (!line.equals("0"));

		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Pergunta> lerPerguntasPorId(ArrayList<Integer> listOfIds)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, Exception {
		ArrayList<Pergunta> perguntas = new ArrayList<>();
		for (Integer id : listOfIds) {
			Pergunta pergunta = perguntaController.read(id);
			if (pergunta != null) {
				perguntas.add(pergunta);
			}
		}

		return perguntas;
	}

	public ArrayList<Integer> lerIdsPorPalavrasChave(String palavrasChave) throws Exception {
		ArrayList<Integer> listOfIds = new ArrayList<>();

		String[] palavrasChaveVector = palavrasChave.split(";");
		// for(int i = 0; i < palavrasChaveVector.length; i++) {
		// System.out.println(palavrasChaveVector[i]);
		// }
		for (int i = 0; i < palavrasChaveVector.length; i++) {
			// System.out.println(palavrasChaveVector[i]);
			int[] setOfIds = perguntaController.readPorPalavraChave(palavrasChaveVector[i]);
			for (int j = 0; j < setOfIds.length; j++) {
				// System.out.println(setOfIds[j]);
				listOfIds.add(setOfIds[j]);
			}
		}
		// System.out.println(listOfIds.toString());
		return listOfIds;
	}

	public void MenuCriacaoPerguntas() {
		ler = new Scanner(System.in);

		String line;
		int opcao = 0;
		try {
			do {
				ImprimirMenuCriacaoPerguntas();
				line = ler.nextLine();
				opcao = Integer.parseInt(line);
				switch (opcao) {
					case 1:
						// Listar perguntas
						ListarPerguntas();

						System.out.println("|   Pressione qualquer tecla para continuar...");
						ler.nextLine();
						break;
					case 2:
						IncluirPergunta();

						break;
					case 3:
						AlterarPergunta();
						break;

					case 4:
						ArquivarPergunta();
						break;
					case 0:
						System.out.println("Retornando...");
						break;
					default:
						System.out.println("Opçao inválida");
						break;
				}
			} while (opcao != 0);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean Menu() throws Exception {

		ler = new Scanner(System.in);
		boolean isLogin = false;
		do {
			if (forcarEntrada) {
				entrada = entradaForcada;
				forcarEntrada = false;
			} else {
				ImprimirMenu();

				System.out.println("\n| Opção: ");
				System.out.print("\t-> ");
				entrada = ler.nextLine();
				System.out.println();
			}

			switch (entrada) {
				case "1":
					isLogin = Login();
					break;
				case "2":
					isLogin = Cadastro();
					break;
				case "0":
					System.out.println("| Fim do programa.");
					break;

				default:
					System.out.println("| Entrada Invalida.");
					break;
			}

		} while (!entrada.equals("0"));

		return isLogin;
	}

	public void ImprimirMenuPrincipalPerguntas() {
		System.out.println("\n----------------\nPerguntas 1.0\n----------------\n");
		System.out.println("INÍCIO");
		System.out.println("\t1) Criação de perguntas");
		System.out.println("\t2) Consultar/responder perguntas");
		System.out.println("\t3) Notificações: 0");
		System.out.println("\t0) Sair");
		System.out.print("\t-> ");
	}

	public void ImprimirMenuCriacaoPerguntas() {
		System.out.println("\n----------------\nPerguntas 1.0\n----------------\n");
		System.out.println("INÍCIO > CRIAÇÃO DE PERGUNTAS");
		System.out.println("\t1) Listar");
		System.out.println("\t2) Incluir");
		System.out.println("\t3) Alterar");
		System.out.println("\t4) Arquivar");
		System.out.println("\t0) Retornar ao menu anterior");
		System.out.print("\t-> ");
	}

	public void ImprimirMenu() {
		System.out.println("\n----------------\nMenu\n----------------\n");
		System.out.println("| Acesso:\n");
		System.out.println("\t1 - Acesso ao Sistema");
		System.out.println("\t2 - Novo usuário(primeiro acesso)\n");
		System.out.println("\t0 - Sair");
	}

	public void DeleteDados() {
		// TODO deletar tudo da pasta DATA_FOLDER
		new File(DATA_FOLDER + "usuarios.db").delete();
		new File(DATA_FOLDER + "usuarios.hash_c.db").delete();
		new File(DATA_FOLDER + "usuarios.hash_d.db").delete();
	}

	public void ImprimirMenuPesquisaPalavrasChave() {
		System.out.println("\n----------------\nPerguntas 1.0\n----------------\n");
		System.out.println("INÍCIO > PERGUNTAS");
		System.out.println("\tBusque as perguntas por palavra chave separadas por ponto e vírgula");
		System.out.println("\tEx: política;Brasil;eleições");
		System.out.println("\tPara cancelar entre com \"0\"");
		System.out.println("\tPalavras chave:");
		System.out.print("\t-> ");
	}

	public boolean Login() throws Exception {
		System.out.println("------------------------------");
		System.out.println("LOGIN");
		System.out.println("------------------------------");

		// Solicitar o e-mail do usuário;
		System.out.println("| Insira seu email: ");
		System.out.print("\t-> ");
		email = ler.nextLine();

		boolean isLogin = false;

		// Buscar o número do usuário usando o índice secundário externo (de e-mails);
		int id = secondaryIndex.read(email);
		if (id != -1) {
			Usuario user = arqUsuario.read(id);

			System.out.println("| Insira sua senha: ");
			System.out.print("\t-> ");
			senha = ler.nextLine();

			if (user.getSenha() == senha.hashCode()) {
				// Senha correta
				System.out.println("| Acesso garantido -> tela principal.");
				idUsuario = user.getID();
				MenuPrincipalPerguntas();
			} else {
				System.out.println("| ERRO: senha incorreta.");
			}
		} else {
			System.out.println("| ERRO: Email não cadastrado.");
		}

		return isLogin;
	}

	public void Logout() {
		idUsuario = -1;
		idPergunta = -1;
	}

	public void ArquivarPergunta()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		ler = new Scanner(System.in);
		List<Pergunta> perguntas = ListarPerguntas();
		System.out.println("| Informe o número da pergunta a ser arquivada: ");
		System.out.println("| 0 - Sair");
		System.out.print("\t-> ");
		int arrayIndex = Integer.parseInt(ler.nextLine()) - 1;

		Pergunta pergunta = null;

		try {
			pergunta = perguntas.get(arrayIndex);
		} catch (Exception e) {
			return;
		}

		boolean success = false;
		if (pergunta != null && pergunta.isAtiva() == true) {
			int idPergunta = pergunta.getID();

			if (pergunta.getIdUsuario() == idUsuario) {
				success = perguntaController.archiving(idPergunta);
			}
		}

		if (success) {
			System.out.println("| Pergunta arquivada com sucesso!");
		} else {
			System.out.println("| Ocorreu algum erro ao arquivar a pergunta!");
		}
	}

	public List<Pergunta> ListarPerguntas()

			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		System.out.println("MINHAS PERGUNTAS");
		List<Pergunta> minhasPerguntas = perguntaController.readAll(idUsuario);

		for (int i = 0; i < minhasPerguntas.size(); i++) {
			System.out.println(minhasPerguntas.get(i).toString(i + 1));
		}

		return minhasPerguntas;
	}

	public void AlterarPergunta()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		List<Pergunta> perguntas = ListarPerguntas();
		System.out.println("| Escolha o número da pergunta:");

		int arrayIndex = Integer.parseInt(ler.nextLine()) - 1;

		if (arrayIndex == -1) {
			return;
		}

		Pergunta pergunta = null;
		try {
			pergunta = perguntas.get(arrayIndex);
		} catch (Exception e) {
			return;
		}

		// Assegurar a scopo de pergunta
		if (pergunta.getIdUsuario() == idUsuario && pergunta.isAtiva()) {
			Pergunta perguntaAntiga = pergunta.clone();
			System.out.println("| Insira sua pergunta: ");
			String conteudoPergunta = ler.nextLine();

			if (conteudoPergunta.equals("")) {
				return;
			}

			System.out.println("| Insira as novas palavras chave: ");
			String palavrasChave = ler.nextLine();

			pergunta.setPergunta(conteudoPergunta);
			pergunta.setPalavrasChave(palavrasChave);

			System.out.println("| Deseja confirma alteração(S/N): ");

			String validacao = ler.nextLine().toLowerCase();

			if (validacao.equals("s")) {
				perguntaController.update(pergunta, perguntaAntiga);

				System.out.println("| Pergunta alterada com sucesso!");
			}
		}

		return;
	}

	public void IncluirPergunta() throws Exception {

		System.out.println("| Insira sua pergunta:");
		System.out.print("\t-> ");
		Pergunta pergunta = new Pergunta(idUsuario, ler.nextLine());

		System.out.println("| Insira algumas palavras chaves para essa pergunta:");
		System.out.print("\t-> ");
		String palavrasChaves = ler.nextLine();
		pergunta.setPalavrasChave(palavrasChaves);

		if (pergunta.getPergunta().isEmpty())
			return;

		perguntaController.create(pergunta);

		return;

	}

	public boolean Cadastro() throws Exception {
		print_user_info();

		System.out.println("| Insira seu email:");
		System.out.print("\t-> ");
		email = ler.nextLine();
		email = email.toLowerCase();
		boolean isLogin = false;

		if (!email.isEmpty()) {

			int idEncontrado = secondaryIndex.read(email);

			if (idEncontrado > 0) {
				System.out.println("| Email ja cadastrado.");
				entradaForcada = "1";
				forcarEntrada = true;
			} else {
				System.out.println("| Insira seu nome: ");
				System.out.print("\t-> ");
				nome = ler.nextLine();

				String confirmarSenha;

				do {
					System.out.println("| Insira sua senha: ");
					System.out.print("\t-> ");
					senha = ler.nextLine();

					System.out.println("| Confirme sua senha: ");
					System.out.print("\t-> ");
					confirmarSenha = ler.nextLine();

				} while (!confirmarSenha.equals(senha));

				System.out.println("| Confirmar cadastro? (S/N)");
				System.out.print("\t-> ");
				confirmar = ler.nextLine();

				if (confirmar.toUpperCase().equals("S")) {
					// informar usuario criado;
					int id = arqUsuario.create(new Usuario(nome, email, senha));
					secondaryIndex.create(email, id);
					System.out.println("| Usuario Criado com sucesso.");
					isLogin = true;
				}
			}
		}

		return isLogin;
	}

	void print_user_info() {
		System.out.println("------------------------------");
		System.out.println("CADASTRO");
		System.out.println("------------------------------");
	}
}