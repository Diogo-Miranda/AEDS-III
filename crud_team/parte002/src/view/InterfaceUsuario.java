package src.view;

import java.util.Scanner;
import java.io.File;
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

	String entrada;
	boolean forcarEntrada = false;
	String entradaForcada = "";
	String confirmar;

	public InterfaceUsuario() throws Exception {
		arqUsuario = new PrimaryIndexCRUD<Usuario, pcvDireto>(Usuario.class.getConstructor(),
				pcvDireto.class.getConstructor(), pcvDireto.class.getConstructor(int.class, long.class),
				DATA_FOLDER + "usuario.db");
		secondaryIndex = new SecondaryIndexCRUD<pcvUsuario>(pcvUsuario.class.getConstructor(),
				pcvUsuario.class.getConstructor(String.class, int.class));
	}

	public void Menu() throws Exception {

		ler = new Scanner(System.in);
		do {
			;
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
				Login();
				break;

			case "2":
				Cadastro();
				break;

			case "0":
				System.out.println("| Fim do programa.");
				break;

			default:
				System.out.println("| Entrada Invalida.");
				break;
			}

		} while (!entrada.equals("0"));

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

	public void Login() throws Exception {
		System.out.println("------------------------------");
		System.out.println("LOGIN");
		System.out.println("------------------------------");

		// Solicitar o e-mail do usuário;
		System.out.println("| Insira seu email: ");
		System.out.print("\t-> ");
		email = ler.nextLine();

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

			} else {
				System.out.println("| ERRO: senha incorreta.");
			}
			return;
		} else {
			System.out.println("| ERRO: Email não cadastrado.");
			return;
		}

	}

	public void Cadastro() throws Exception {

		print_user_info();

		System.out.println("| Insira seu email:");
		System.out.print("\t-> ");
		email = ler.nextLine();
		email = email.toLowerCase();

		if (email.isEmpty())
			return;

		int idEncontrado = secondaryIndex.read(email);

		if (idEncontrado > 0) {
			System.out.println("| Email ja cadastrado.");
			entradaForcada = "1";
			forcarEntrada = true;
			return;
		}

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
		}

		return;

	}

	void print_user_info() {
		System.out.println("------------------------------");
		System.out.println("CADASTRO");
		System.out.println("------------------------------");
	}
}
