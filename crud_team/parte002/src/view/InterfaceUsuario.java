package src.view;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import src.model.*;
import src.controller.*;

public class InterfaceUsuario {

	public static final String DATA_FOLDER = "./src/model/dados/";

	private Scanner ler;
	private String nome;
	private String email;
	private String senha;
	private PrimaryIndexCRUD<Usuario, pcvDireto> arqUsuario;
	private SecondaryIndexCRUD<pcvUsuario> secondaryIndex;
	private PerguntaController perguntaController;

	private String entrada;
	private boolean forcarEntrada = false;
	private String entradaForcada = "";
	private String confirmar;

	private int idUsuario = -1;

	public InterfaceUsuario() throws Exception {
		arqUsuario = new PrimaryIndexCRUD<Usuario, pcvDireto>(Usuario.class.getConstructor(),
				pcvDireto.class.getConstructor(), pcvDireto.class.getConstructor(int.class, long.class),
				DATA_FOLDER + "usuario.db");
		secondaryIndex = new SecondaryIndexCRUD<pcvUsuario>(pcvUsuario.class.getConstructor(),
				pcvUsuario.class.getConstructor(String.class, int.class));

		perguntaController = new PerguntaController(DATA_FOLDER + "pergunta.db", 100,
				DATA_FOLDER + "perguntaArvore.db");
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
						System.out.println("Consultando...");

						break;
					case 3:
						// Acessar notificações
						break;
					case 0:
						System.out.println("Deslogando...");
						idUsuario = -1;
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
		new File(DATA_FOLDER + "usuarios.db").delete();
		new File(DATA_FOLDER + "usuarios.hash_c.db").delete();
		new File(DATA_FOLDER + "usuarios.hash_d.db").delete();
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

		// Buscar o ID do usuário usando o índice secundário externo (de e-mails);
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

	public void ArquivarPergunta()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		ler = new Scanner(System.in);
		List<Pergunta> perguntas = ListarPerguntas();
		System.out.println("| Informe o ID da pergunta a ser arquivada: ");
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

		for (Pergunta pergunta : minhasPerguntas) {
			String isArquivado = (pergunta.isAtiva() ? "" : "(Arquivada)");
			System.out.println(String.format("\n%d. %s", minhasPerguntas.indexOf(pergunta) + 1, isArquivado));
			System.out.println(pergunta.getCriacaoString());
			System.out.println(pergunta.getPergunta());
		}

		return minhasPerguntas;
	}

	public void AlterarPergunta()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		List<Pergunta> perguntas = ListarPerguntas();
		System.out.println("| Escolha o ID da pergunta:");

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

			System.out.println("| Insira sua pergunta: ");

			String conteudoPergunta = ler.nextLine();

			if (conteudoPergunta.equals("")) {
				return;
			}

			pergunta.setPergunta(conteudoPergunta);

			System.out.println("| Deseja confirma alteração(S/N): ");

			String validacao = ler.nextLine().toLowerCase();

			if (validacao.equals("s")) {
				perguntaController.update(pergunta);
				System.out.println("| Pergunta alterada com sucesso!");
			}
		}

		return;
	}

	public void IncluirPergunta() throws Exception {

		System.out.println("| Insira sua pergunta:");
		System.out.print("\t-> ");
		Pergunta pergunta = new Pergunta(idUsuario, ler.nextLine());

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