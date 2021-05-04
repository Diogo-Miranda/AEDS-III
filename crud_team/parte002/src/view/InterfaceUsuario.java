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
						break;
					case 2:
						// Incluir pergunta
						// Solicitar a descrição da pergunta;
						// Se a pergunta estiver em branco, retornar ao menu de criação de perguntas;
						// Solicitar a confirmação da inclusão da nova pergunta;
						// Se o usuário não confirmar a inclusão, voltar ao menu de criação de
						// perguntas;
						// Incluir a pergunta no arquivo, por meio do método create(), usando o texto da
						// pergunta, a data/hora da criação, a nota e o ID do usuário;
						// O método retornará o ID da nova pergunta;
						// Incluir o par ID do usuário e ID da pergunta na árvore B+ do relacionamento.
						IncluirPergunta();
						break;
					case 3:
						// Alterar pergunta
						// Obter a lista de IDs de perguntas na Árvore B+ usando o ID do usuário;
						// Para cada ID nessa lista,
						// Obter os dados da pergunta usando o método read(ID) do CRUD;
						// Se a pergunta estiver ativa, apresentar os seus dados na tela.
						// Solicitar do usuário o número da pergunta que deseja alterar;
						// Se o usuário digitar 0, retornar ao menu de perguntas;
						// Usando o ID da pergunta escolhida, recuperar os dados da pergunta usando o
						// método read(ID) do CRUD;
						// Apresentar os dados da pergunta na tela;
						// Solicitar a nova redação da pergunta ;
						// Se o usuário deixar esse campo em branco, retornar ao menu de perguntas;
						// Solicitar a confirmação de alteração ao usuário;
						// Se o usuário não confirmar a alteração, voltar ao menu de perguntas;
						// Alterar os dados da pergunta por meio do método update() do CRUD;
						// Apresentar mensagem de confirmação da alteração;
						// Voltar ao menu de perguntas.
						break;

					case 4:
						// Arquivar pergunta
						// Obter a lista de IDs de perguntas na Árvore B+ usando o ID do usuário;
						// Para cada ID nessa lista,
						// Obter os dados da pergunta usando o método read(ID) do CRUD;
						// Se a pergunta estiver ativa, apresentar os seus dados na tela.
						// Solicitar do usuário o número da pergunta que deseja arquivar;
						// Se o usuário digitar 0, retornar ao menu de perguntas;
						// Usando o ID da pergunta escolhida, recuperar os dados da pergunta usando o
						// método read(ID) do CRUD;
						// Apresentar os dados da pergunta na tela;
						// Solicitar a confirmação de arquivamento ao usuário;
						// Se o usuário não confirmar o arquivamento, voltar ao menu de perguntas;
						// Arquivar a pergunta por meio do método update() do CRUD, mudando apenas o
						// valor do atributo ativa para false.
						// Apresentar mensagem de confirmação do arquivamento;
						// Voltar ao menu de perguntas.
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
		System.out.println("PERGUNTAS 1.0");
		System.out.println("=============");
		System.out.println("INÍCIO");
		System.out.println("1) Criação de perguntas");
		System.out.println("2) Consultar/responder perguntas");
		System.out.println("3) Notificações: 0");
		System.out.println("0) Sair");
	}

	public void ImprimirMenuCriacaoPerguntas() {
		System.out.println("PERGUNTAS 1.0");
		System.out.println("=============");
		System.out.println("INÍCIO > CRIAÇÃO DE PERGUNTAS");
		System.out.println("1) Listar");
		System.out.println("2) Incluir");
		System.out.println("3) Alterar");
		System.out.println("4) Arquivar");
		System.out.println("0) Retornar ao menu anterior");
	}

	public void ImprimirMenu() {
		System.out.println("\n=============\nMenu\n=============\n");
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
  
	public void ListarPerguntas()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
		System.out.println("MINHAS PERGUNTAS");
		List<Pergunta> minhasPerguntas = perguntaController.readAll(idUsuario);

		for (Pergunta pergunta : minhasPerguntas) {
			String isArquivado = (pergunta.isAtiva() ? "" : "(Arquivada)");
			System.out.println(String.format("\n%d. %s", pergunta.getID(), isArquivado));
			System.out.println(pergunta.getCriacaoString());
			System.out.println(pergunta.getPergunta());
		}

		System.out.println("\n\nPressione qualquer tecla para continuar...");
		ler.nextLine();

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

	public void Cadastro() throws Exception {
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
